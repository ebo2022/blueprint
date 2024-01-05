package com.teamabnormals.blueprint.core.api.condition;

import net.minecraft.tags.TagKey;

public record TagEmptyCondition<T>(TagKey<T> tagKey) implements IBlueprintResourceCondition.PlatformPlaceholder {
}
