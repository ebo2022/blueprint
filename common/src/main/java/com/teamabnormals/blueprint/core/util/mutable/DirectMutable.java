package com.teamabnormals.blueprint.core.util.mutable;

/**
 * A {@link Mutable} implementation that directly stores its value.
 *
 * @param <T> The type of value being stored.
 * @author ebo2022
 */
public class DirectMutable<T> implements Mutable<T> {

    private T value;

    /**
     * Creates a {@link DirectMutable} initialized with the given value.
     *
     * @param value The initial value to use.
     * @return A {@link DirectMutable} initialized with the given value.
     */
    public static <T> DirectMutable<T> withInitial(T value) {
        DirectMutable<T> mutable = new DirectMutable<>();
        mutable.value = value;
        return mutable;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void set(T newValue) {
        this.value = newValue;
    }
}
