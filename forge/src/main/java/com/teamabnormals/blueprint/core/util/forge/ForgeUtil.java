package com.teamabnormals.blueprint.core.util.forge;

import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utility methods to help with registration of Forge content.
 *
 * @author ebo2022
 */
public class ForgeUtil {

    /**
     * Gets a mod {@link IEventBus} by its mod name if it exists.
     *
     * @param modId The mod id to use to look for the event bus.
     * @return The mod {@link IEventBus} if it exists.
     */
    public static IEventBus getModEventBus(String modId) {
        return ModList.get()
                .getModContainerById(modId)
                .map(ModContainer::getEventBus)
                .orElseThrow(() -> new IllegalStateException("No event bus found for mod '" + modId + "'"));
    }

    /**
     * Adds a listener to the event bus with the given mod id.
     *
     * @param modId The mod id to use to look for the event bus.
     * @param listener
     */
    public static <T extends Event & IModBusEvent> void addModBusListener(String modId, Consumer<T> listener) {
        getModEventBus(modId).addListener(listener);
    }

    /**
     * Adds a consumer to accept the event bus with the given mod id.
     *
     * @param modId    The mod id to use to look for the event bus.
     * @param consumer The consumer to use the event bus for.
     */
    public static void addModBusConsumer(String modId, Consumer<IEventBus> consumer) {
        consumer.accept(getModEventBus(modId));
    }

    /**
     * Creates an {@link IParallelDispatcher} that wraps the given {@link ParallelDispatchEvent}.
     *
     * @param event An {@link Event} that extends {@link ParallelDispatchEvent}.
     * @return An {@link IParallelDispatcher} that wraps the given {@link ParallelDispatchEvent}.
     */
    public static <T extends ParallelDispatchEvent> IParallelDispatcher createParallelDispatcher(T event) {
        return new EventBasedParallelDispatcher(event);
    }

    private record EventBasedParallelDispatcher(ParallelDispatchEvent event) implements IParallelDispatcher {

        @Override
        public CompletableFuture<Void> enqueueWork(Runnable work) {
            return this.event.enqueueWork(work);
        }

        @Override
        public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
            return this.event.enqueueWork(work);
        }
    }
}
