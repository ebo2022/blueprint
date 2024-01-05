package com.teamabnormals.blueprint.core.api.condition.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * An {@link ICondition} that checks if a {@link TagKey} has any values bound to it.
 *
 * @param tagKey The {@link TagKey} to check if values are present for it.
 * @author ebo2022
 */
public record BlueprintTagPopulatedCondition<T>(TagKey<T> tagKey) implements ICondition {
    public static final Codec<BlueprintTagPopulatedCondition<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("registry").forGetter(condition -> condition.tagKey.registry().location()),
            ResourceLocation.CODEC.fieldOf("tag").forGetter(condition -> condition.tagKey.location())
    ).apply(instance, (registry, tag) -> {
        return new BlueprintTagPopulatedCondition<>(TagKey.create(ResourceKey.createRegistryKey(registry), tag));
    }));

    @Override
    public boolean test(IContext iContext) {
        return !iContext.getTag(this.tagKey).isEmpty();
    }

    @Override
    public Codec<BlueprintTagPopulatedCondition<?>> codec() {
        return CODEC;
    }
}
