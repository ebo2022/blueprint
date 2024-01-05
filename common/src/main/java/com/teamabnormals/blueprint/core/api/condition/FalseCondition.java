package com.teamabnormals.blueprint.core.api.condition;

public record FalseCondition() implements IBlueprintResourceCondition.PlatformPlaceholder {
    public static final FalseCondition INSTANCE = new FalseCondition();
}
