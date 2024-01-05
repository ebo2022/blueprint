package com.teamabnormals.blueprint.core.api.condition;

public record TrueCondition() implements IBlueprintResourceCondition.PlatformPlaceholder {
    public static final TrueCondition INSTANCE = new TrueCondition();
}
