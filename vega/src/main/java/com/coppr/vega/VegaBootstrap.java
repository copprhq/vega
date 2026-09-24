package com.coppr.vega;

import com.coppr.vega.application.Application;
import com.coppr.vega.application.ApplicationContext;
import com.coppr.vega.repository.Repositories;
import com.coppr.vega.repository.Repository;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public final class VegaBootstrap {

    private VegaBootstrap() {
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        System.out.println("Starting Vega bootstrap...");

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            ClassInfoList applications = scanResult.getSubclasses(Application.class.getName());
            if (applications.isEmpty()) {
                throw new IllegalStateException("No Application subclass found.");
            }

            if (applications.size() > 1) {
                throw new IllegalStateException("Multiple Application subclasses found: " +
                        applications.getNames());
            }

            Class<?> applicationClass = applications.getFirst().loadClass();
            String applicationPackage = applicationClass.getPackageName();

            Properties properties = Properties.getInstance();
            Repositories repositories = null;

            for (ClassInfo info : scanResult.getAllClasses()) {
                if (!info.getPackageName().startsWith(applicationPackage)) continue;

                Class<?> candidate = info.loadClass();

                if (Properties.class.isAssignableFrom(candidate) && !candidate.equals(Properties.class)) {
                    properties = (Properties) candidate.getDeclaredConstructor().newInstance();
                    continue;
                }

                if (candidate.isInterface() && Repository.class.isAssignableFrom(candidate)) {
                    repositories = new Repositories(properties);
                    repositories.create((Class<? extends Repository>) candidate);
                }
            }

            ApplicationContext applicationContext = new ApplicationContext();
            applicationContext.addBean(properties).addBean(repositories);

            Application application = (Application) applicationClass.getDeclaredConstructor().newInstance();
            application.setContext(applicationContext);

            System.out.println("started");
        } catch (Throwable throwable) {
            System.out.println("Failure on starting Vega bootstrap:");
            System.out.println(throwable.getMessage());
            System.exit(1);
        }
    }
}