package com.github.copprhq.vega.application;

import com.github.copprhq.vega.DependencyInjection;
import com.github.copprhq.vega.Properties;
import com.github.copprhq.vega.command.CommandHandler;
import com.github.copprhq.vega.component.Component;
import com.github.copprhq.vega.repository.Repository;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ApplicationFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFactory.class);

    private ApplicationFactory() {
    }

    public static VegaApplication create() {
        LOGGER.info("Creating Vega application...");

        try (ScanResult scanResult = new ClassGraph().enableClassInfo().enableAnnotationInfo()
                .scan()) {
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

            ClassInfoList candidates = scanResult.getClassesImplementing(Repository.class)
                    .union(scanResult.getClassesImplementing(CommandHandler.class))
                    .union(scanResult.getClassesWithAnnotation(Component.class.getName()))
                    .union(scanResult.getClassesWithAnnotation(
                            DependencyInjection.class.getName()));

            Properties properties = null;

            for (ClassInfo info : candidates) {
                if (!info.getPackageName().startsWith(applicationPackage)) {
                    continue;
                }

                Class<?> candidate = info.loadClass();

                if (candidate.isAnnotationPresent(DependencyInjection.Ignore.class)) {
                    continue;
                }

                if (Properties.class.isAssignableFrom(candidate)) {
                    properties = (Properties) candidate.getDeclaredConstructor().newInstance();
                    context.addBean(Properties.class, properties);
                    continue;
                }

                if (properties == null) {
                    properties = new Properties.VegaProperties();
                    context.addBean(Properties.class, properties);
                }

                context.addBean(candidate, context.getBean(candidate));
            }

            VegaApplication application =
                    (VegaApplication) context.createBean(applicationClass);

            Field field = VegaApplication.class
                    .getDeclaredField("applicationContext");

            field.setAccessible(true);
            field.set(application, context);

            return application;
        } catch (Throwable throwable) {
            throw new ApplicationCreationException(throwable);
        }
    }
}
