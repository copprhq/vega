package com.github.copprhq.vega;

import com.github.copprhq.vega.application.VegaApplication;
import com.github.copprhq.vega.application.ApplicationContext;
import com.github.copprhq.vega.command.CommandHandler;
import com.github.copprhq.vega.component.Component;
import com.github.copprhq.vega.repository.Repository;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public final class VegaBootstrap {

    private VegaBootstrap() {
    }

    public static void main(String[] args) {
        System.out.println("Starting Vega bootstrap...");

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            ClassInfoList applications = scanResult.getSubclasses(VegaApplication.class);
            if (applications.isEmpty()) {
                throw new IllegalStateException("No VegaApplication subclass found.");
            }
            if (applications.size() > 1) {
                throw new IllegalStateException("Multiple VegaApplication subclasses found: "
                                + applications.getNames());
            }

            Class<?> applicationClass = applications.getFirst().loadClass();
            String applicationPackage = applicationClass.getPackageName();
            ApplicationContext context = new ApplicationContext();

            for (ClassInfo info : scanResult.getAllClasses()) {
                if (!info.getPackageName().startsWith(applicationPackage)) continue;

                Class<?> candidate = info.loadClass();
                if (candidate.equals(applicationClass)) continue;
                if (candidate.isAnnotationPresent(DependencyInjection.Ignore.class)) continue;

                boolean eligible = Repository.class.isAssignableFrom(candidate)
                                || CommandHandler.class.isAssignableFrom(candidate)
                                || candidate.isAnnotationPresent(Component.class)
                                || candidate.isAnnotationPresent(DependencyInjection.class);

                if (!eligible) continue;

                context.addBean(candidate, context.getBean(candidate));
            }

            VegaApplication application = (VegaApplication) context.createBean(applicationClass);
            application.setApplicationContext(context);

            System.out.println("started");
        } catch (Throwable throwable) {
            System.out.println("Failure on starting Vega bootstrap:");
            System.out.println(throwable.getMessage());
            System.exit(1);
        }
    }
}