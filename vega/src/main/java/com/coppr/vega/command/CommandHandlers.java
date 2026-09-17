package com.coppr.vega.command;

import com.coppr.supernova.extension.Extensible;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandHandlers extends Extensible {

    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers =
            new ConcurrentHashMap<>();

    public CommandHandlers() {
    }

    public <R extends Response, C extends Command> void addCommandHandler(
            CommandHandler<R, C> commandHandler
    ) {
        commandHandlers.put(commandHandler.getClass(), commandHandler);
    }

    @SuppressWarnings("unchecked")
    public <R extends Response, C extends Command, H extends CommandHandler<R, C>> H commandHandler(
            Class<H> commandHandlerClass
    ) {
        return (H) commandHandlers.get(commandHandlerClass);
    }

    public List<CommandHandler<?, ?>> commandHandlers() {
        return new ArrayList<>(commandHandlers.values());
    }

}
