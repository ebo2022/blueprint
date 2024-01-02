package com.teamabnormals.blueprint.core.util.registry.fabric;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Lifecycle;
import com.teamabnormals.blueprint.core.util.registry.ISubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
@SuppressWarnings({"unchecked", "rawtypes"})
public class AbstractSubRegistryHelperImpl {
    public static <T> ISubRegistryHelper.RegistryWrapper<T> createWrapper(String namespace, ResourceKey<? extends Registry<T>> registry) {
        return new RegistryWrapperImpl<>(namespace, registry);
    }

    private static class RegistryWrapperImpl<T> implements ISubRegistryHelper.RegistryWrapper<T> {
        private final String namespace;
        private final ResourceKey<? extends Registry<T>> key;
        private final WritableRegistry<T> registry;

        private RegistryWrapperImpl(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
            this.namespace = namespace;
            this.key = registryKey;
            this.registry = (WritableRegistry<T>) BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryKey);
        }

        @Override
        public <R extends T> RegistryHolder<T, R> register(String name, Supplier<? extends R> object) {
            Holder.Reference<T> reference = this.registry.register(ResourceKey.create(this.registry.key(), new ResourceLocation(this.namespace, name)), object.get(), Lifecycle.stable());
            return new RegistryHolderImpl<>(reference);
        }
    }

    private record RegistryHolderImpl<T, R extends T>(Reference<T> holder) implements RegistryHolder<T, R> {

        @Override
        public ResourceLocation getId() {
            return this.holder.key().location();
        }

        @Override
        public ResourceKey<T> getKey() {
            return this.holder.key();
        }

        @Override
        public R get() {
            return (R) this.holder.value();
        }

        @Override
        public T value() {
            return this.holder.value();
        }

        @Override
        public boolean isBound() {
            return this.holder.isBound();
        }

        @Override
        public boolean is(ResourceLocation resourceLocation) {
            return this.holder.is(resourceLocation);
        }

        @Override
        public boolean is(ResourceKey<T> resourceKey) {
            return this.holder.is(resourceKey);
        }

        @Override
        public boolean is(Predicate<ResourceKey<T>> predicate) {
            return this.holder.is(predicate);
        }

        @Override
        public boolean is(TagKey<T> tagKey) {
            return this.holder.is(tagKey);
        }

        @Override
        public Stream<TagKey<T>> tags() {
            return this.holder.tags();
        }

        @Override
        public Either<ResourceKey<T>, T> unwrap() {
            return this.holder.unwrap();
        }

        @Override
        public Optional<ResourceKey<T>> unwrapKey() {
            return this.holder.unwrapKey();
        }

        @Override
        public Kind kind() {
            return this.holder.kind();
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> holderOwner) {
            return this.holder.canSerializeIn(holderOwner);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj instanceof RegistryHolder<?, ?> rh && rh.getKey() == this.getKey();
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }

        @Override
        public String toString() {
            return String.format(Locale.ENGLISH, "DeferredHolder{%s}", this.getKey());
        }
    }
}
