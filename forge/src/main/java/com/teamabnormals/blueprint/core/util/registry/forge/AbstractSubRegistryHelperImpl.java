package com.teamabnormals.blueprint.core.util.registry.forge;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Lifecycle;
import com.teamabnormals.blueprint.core.util.forge.ForgeUtil;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public class AbstractSubRegistryHelperImpl {
    public static <T> ISubRegistryHelper.RegistryWrapper<T> createWrapper(String namespace, ResourceKey<? extends Registry<T>> registry) {
        return new RegistryWrapperImpl<>(namespace, registry);
    }

    private static class RegistryWrapperImpl<T> implements ISubRegistryHelper.RegistryWrapper<T> {
        private final DeferredRegister<T> deferredRegister;

        private RegistryWrapperImpl(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
            this.deferredRegister = DeferredRegister.create(registryKey, namespace);
            ForgeUtil.addModBusConsumer(namespace, this.deferredRegister::register);
        }

        @Override
        public <R extends T> RegistryHolder<T, R> register(String name, Supplier<? extends R> object) {
            return new RegistryHolderImpl<>(this.deferredRegister.register(name, object));
        }
    }

    private record RegistryHolderImpl<T, R extends T>(DeferredHolder<T, R> holder) implements RegistryHolder<T, R> {

        @Override
        public ResourceLocation getId() {
            return this.holder.getId();
        }

        @Override
        public ResourceKey<T> getKey() {
            return this.holder.getKey();
        }

        @Override
        public R get() {
            return this.holder.get();
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
        public boolean is(ResourceLocation arg) {
            return this.holder.is(arg);
        }

        @Override
        public boolean is(ResourceKey<T> arg) {
            return this.holder.is(arg);
        }

        @Override
        public boolean is(Predicate<ResourceKey<T>> predicate) {
            return this.holder.is(predicate);
        }

        @Override
        public boolean is(TagKey<T> arg) {
            return this.holder.is(arg);
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
        public boolean canSerializeIn(HolderOwner<T> arg) {
            return this.holder.canSerializeIn(arg);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj instanceof RegistryHolderImpl<?, ?> rh && rh.holder.equals(this.holder);
        }

        @Override
        public int hashCode() {
            return this.holder.hashCode();
        }

        @Override
        public String toString() {
            return this.holder.toString();
        }
    }
}
