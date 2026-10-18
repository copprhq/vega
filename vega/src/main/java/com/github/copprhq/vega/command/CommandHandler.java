package com.github.copprhq.vega.command;

import com.coppr.supernova.functional.Result;

import java.util.concurrent.CompletableFuture;

public interface CommandHandler<I, O> {

    CompletableFuture<Result<O>> handle(I input);

}
