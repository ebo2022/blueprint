package com.teamabnormals.blueprint.core.util.mutable;

import java.util.function.Supplier;

/**
 * Represents a stored value that can be retrieved and be changed.
 *
 * @param <T> The type of value.
 * @see OutsourcedMutable
 */
public interface Mutable<T> extends Supplier<T> {

    /**
     * @return The currently stored value.
     */
    @Override
    T get();

    /**
     * Sets the new value to be stored.
     *
     * @param newValue The new value.
     */
    void set(T newValue);
}
