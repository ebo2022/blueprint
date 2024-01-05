package com.teamabnormals.blueprint.core.api.condition;

public record NotCondition(IBlueprintResourceCondition invert) implements IBlueprintResourceCondition.PlatformPlaceholder {
}
