package com.coppr.vega.command;

import com.coppr.supernova.functional.Result;

import java.util.concurrent.CompletableFuture;

/**
 * Handles a command and produces a result asynchronously.
 *
 * @param <T> the type of value produced when the command is handled successfully
 * @param <C> the type of command accepted by this handler
 */
public interface CommandHandler<T, C extends Command> {

    /**
     * Handles the given command.
     *
     * <p>The returned future represents the asynchronous execution of the
     * command. The resulting {@link Result} represents whether the command
     * was handled successfully or failed.</p>
     *
     * @param command the command to handle
     * @return a future containing the result of handling the command
     */
    CompletableFuture<Result<T>> handle(C command);

}