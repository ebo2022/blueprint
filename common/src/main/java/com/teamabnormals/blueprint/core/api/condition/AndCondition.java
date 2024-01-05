package com.teamabnormals.blueprint.core.api.condition;

import java.util.List;

public record AndCondition(List<IBlueprintResourceCondition> children) implements IBlueprintResourceCondition.PlatformPlaceholder {
}
