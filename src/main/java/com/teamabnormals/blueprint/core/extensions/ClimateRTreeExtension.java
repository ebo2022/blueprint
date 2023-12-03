package com.teamabnormals.blueprint.core.extensions;

import net.minecraft.world.level.biome.Climate;

public interface ClimateRTreeExtension<T> {

    SearchResult<T> blueprint$deepSearch(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric);

    record SearchResult<T>(Climate.RTree.Leaf<T> ultimate, long ultimateDistance, Climate.RTree.Leaf<T> penultimate, long penultimateDistance) {
    }
}
