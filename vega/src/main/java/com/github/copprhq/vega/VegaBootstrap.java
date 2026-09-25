package com.github.copprhq.vega;

import com.github.copprhq.vega.application.Application;
import com.github.copprhq.vega.application.ApplicationContext;
import com.github.copprhq.vega.command.CommandHandler;
import com.github.copprhq.vega.command.CommandHandlers;
import com.github.copprhq.vega.repository.Repositories;
import com.github.copprhq.vega.repository.Repository;
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

            Properties properties = new Properties.VegaProperties();

            Repositories repositories = new Repositories(properties);
            CommandHandlers commandHandlers = new CommandHandlers();

            for (ClassInfo info : scanResult.getAllClasses()) {
                if (!info.getPackageName().startsWith(applicationPackage)) continue;

                Class<?> candidate = info.loadClass();

                if (Properties.class.isAssignableFrom(candidate) && !candidate.equals(Properties.class)) {
                    Properties properties1 = (Properties) candidate.getDeclaredConstructor().newInstance();
                    properties = properties1;

                    repositories = new Repositories(properties1);
                    continue;
                }

                if (candidate.isInterface()) {
                    if (Repository.class.isAssignableFrom(candidate)) {
                        repositories.create((Class<? extends Repository>) candidate);
                        continue;
                    }

                    if (CommandHandler.class.isAssignableFrom(candidate)) {
                        CommandHandler<?, ?> commandHandler = (CommandHandler<?, ?>) candidate.
                                getDeclaredConstructor().
                                newInstance();
                        commandHandlers.create(commandHandler);
                    }
                }
            }

            ApplicationContext applicationContext = new ApplicationContext(
                    properties, commandHandlers, repositories);

            Application application = (Application) applicationClass.getDeclaredConstructor().newInstance();
            application.setContext(applicationContext);


        } catch (Throwable throwable) {
            System.out.println("Failure on starting Vega bootstrap:");
            System.out.println(throwable.getMessage());
            System.exit(1);
        }
    }
}