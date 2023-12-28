package com.teamabnormals.blueprint.core.util.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * An extension of the vanilla {@link Holder} which includes additional info and some utility methods.
 * <p>Based on NeoForge's deferred holders.</p>
 *
 * @param <T> The type of object being held by the registry holder
 * @author ebo2022
 */
public interface RegistryHolder<T, R extends T> extends Holder<T>, Supplier<R> {

    /**
     * Gets the location of the registered object.
     *
     * @return The location of the registered object.
     */
    ResourceLocation getId();

    /**
     * Gets a {@link ResourceKey} pointing to the registered object.
     *
     * @return A {@link ResourceKey} pointing to the registered object.
     */
    ResourceKey<T> getKey();

    /**
     * Gets an optional of the held value based on whether it's present.
     *
     * @return An optional of the held value based on whether it's present.
     */
    default Optional<T> asOptional() {
        return isBound() ? Optional.of(value()) : Optional.empty();
    }
}
