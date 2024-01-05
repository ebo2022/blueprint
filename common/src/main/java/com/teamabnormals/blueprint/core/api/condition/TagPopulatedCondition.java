package com.teamabnormals.blueprint.core.api.condition;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public record TagPopulatedCondition<T>(TagKey<T> tagKey) implements IBlueprintResourceCondition.PlatformPlaceholder {
}
