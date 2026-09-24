package com.github.copprhq.vega.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

@Mojo(name = "repackage", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class VegaMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "vega.mainClass", defaultValue = "com.coppr.vega.VegaBootstrap")
    private String mainClass;

    @Override
    public void execute() throws MojoExecutionException {
        File buildDir = new File(project.getBuild().getDirectory());
        String finalName = project.getBuild().getFinalName() + ".jar";
        File originalJar = new File(buildDir, finalName);

        if (!originalJar.exists()) {
            throw new MojoExecutionException("Target JAR not found at: " + originalJar.getAbsolutePath());
        }

        getLog().info("========================================");
        getLog().info("Vega Repackaging Fat-JAR for: " + originalJar.getName());
        getLog().info("========================================");

        try {
            createFatJar(originalJar, mainClass);
            getLog().info("Successfully built Vega Fat-JAR with Main-Class: " + mainClass);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to create Vega Fat-JAR", e);
        }
    }

    private void createFatJar(File thinJar, String mainClassName) throws IOException {
        File tempFile = new File(thinJar.getParentFile(), thinJar.getName() + ".tmp");

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainClassName);

        Set<String> addedEntries = new HashSet<>();

        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempFile), manifest)) {
            addJarContents(thinJar, jos, addedEntries, true);

            for (Artifact artifact : project.getArtifacts()) {
                File depFile = artifact.getFile();
                if (depFile != null && depFile.exists() && depFile.getName().endsWith(".jar")) {
                    getLog().debug("Bundling dependency: " + depFile.getName());
                    addJarContents(depFile, jos, addedEntries, false);
                }
            }
        }

        if (thinJar.delete()) {
            if (!tempFile.renameTo(thinJar)) {
                throw new IOException("Could not replace original JAR with Fat-JAR.");
            }
        } else {
            throw new IOException("Could not delete original thin JAR.");
        }
    }

    private void addJarContents(File jarFile, JarOutputStream jos, Set<String> addedEntries, boolean isMainJar) throws IOException {
        try (JarInputStream jis = new JarInputStream(new FileInputStream(jarFile))) {
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();

                if (name.equalsIgnoreCase("META-INF/MANIFEST.MF") ||
                        name.startsWith("META-INF/SF") ||
                        name.startsWith("META-INF/DSA") ||
                        name.startsWith("META-INF/RSA") ||
                        entry.isDirectory()) {
                    continue;
                }

                if (!addedEntries.add(name)) {
                    continue;
                }

                jos.putNextEntry(new JarEntry(name));
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = jis.read(buffer)) != -1) {
                    jos.write(buffer, 0, bytesRead);
                }
                jos.closeEntry();
            }
        }
    }
}