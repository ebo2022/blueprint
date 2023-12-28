package com.teamabnormals.blueprint.core.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * An interface that allows mods to queue non-threadsafe work until it is safe to run.
 *
 * @author ebo2022
 */
public interface IParallelDispatcher {

    /**
     * An empty dispatcher that executes work immediately.
     */
    IParallelDispatcher EMPTY = new IParallelDispatcher() {
        @Override
        public CompletableFuture<Void> enqueueWork(Runnable work) {
            work.run();
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
            return CompletableFuture.completedFuture(work.get());
        }
    };

    /**
     * Queues non-threadsafe work to happen later when it is safe to be done.
     *
     * @param work The work to run.
     * @return A {@link CompletableFuture} for when the work is done.
     */
    CompletableFuture<Void> enqueueWork(Runnable work);

    /**
     * Queues non-threadsafe work to happen later when it is safe to be done.
     *
     * @param work The work to run.
     * @return A {@link CompletableFuture} for when the work is done.
     */
    <T> CompletableFuture<T> enqueueWork(Supplier<T> work);
}
