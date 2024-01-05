package com.teamabnormals.blueprint.core.api.condition.forge;

import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.core.api.condition.IBlueprintResourceCondition;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Objects;

/**
 * A custom {@link ICondition} to hold an {@link IBlueprintResourceCondition} and execute its behavior.
 * <p>The held condition needs to have its codec registered in {@link com.teamabnormals.blueprint.core.util.DataUtil#registerResourceCondition(ResourceLocation, Codec)} to work properly.</p>
 *
 * @param condition The condition to wrap.
 * @author ebo2022
 */
public record CustomConditionWrapper<T extends IBlueprintResourceCondition>(T condition) implements ICondition {

    @Override
    public boolean test(IContext iContext) {
        return this.condition.test();
    }

    // Return from the registry since that's easier for this use case
    @Override
    public Codec<? extends ICondition> codec() {
        return Objects.requireNonNull(NeoForgeRegistries.CONDITION_SERIALIZERS.get(this.condition.getName()), "Missing codec for custom resource condition " + this.condition.getName());
    }
}