package com.teamabnormals.blueprint.core.util.mutable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A {@link Mutable} implementation that gets its value from another class.
 * <p>Useful for turning a pair of "get" and "set" methods from a Forge event into a single parameter for a {@link com.teamabnormals.blueprint.core.events.SimpleEvent}.</p>
 *
 * @param <T> The type of value being accessed.
 * @param <P> The class the value is being sourced from.
 * @author ebo2022
 */
public class OutsourcedMutable<T, P> implements Mutable<T> {
    private final P provider;
    private final Function<P, T> valueGetter;
    private final BiConsumer<P, T> valueSetter;

    public OutsourcedMutable(P provider, Function<P, T> valueGetter, BiConsumer<P, T> valueSetter) {
        this.provider = provider;
        this.valueGetter = valueGetter;
        this.valueSetter = valueSetter;
    }

    @Override
    public T get() {
        return this.valueGetter.apply(this.provider);
    }

    @Override
    public void set(T newValue) {
        this.valueSetter.accept(this.provider, newValue);
    }
}
