package com.teamabnormals.blueprint.core.api.condition;

import com.google.common.base.Preconditions;

import java.util.List;

public record ModsLoadedCondition(boolean require, List<String> mods) implements IBlueprintResourceCondition.PlatformPlaceholder {
    public ModsLoadedCondition(String mod) {
        this(true, List.of(mod));
    }
}
