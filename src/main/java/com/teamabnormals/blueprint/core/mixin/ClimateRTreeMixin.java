package com.teamabnormals.blueprint.core.mixin;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.extensions.ClimateRTreeExtension;
import com.teamabnormals.blueprint.core.registry.BlueprintBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(Climate.RTree.class)
public class ClimateRTreeMixin<T> implements ClimateRTreeExtension<T> {
    @Shadow
    @Final
    private Climate.RTree.Node<T> root;
    @Shadow
    @Final
    private ThreadLocal<Climate.RTree.Leaf<T>> lastResult;
    @Unique
    private final ThreadLocal<Climate.RTree.Leaf<T>> lastPenultimateResult = new ThreadLocal<>();


    @Override
    public SearchResult<T> blueprint$deepSearch(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) {
        SearchResult<T> result = this.blueprint$searchNode(this.root, target.toParameterArray(), this.lastResult.get(), this.lastPenultimateResult.get(), distanceMetric);
        this.lastResult.set(result.ultimate());
        this.lastResult.set(result.penultimate());
        return result;
    }

    @Unique
    private SearchResult<T> blueprint$searchNode(Climate.RTree.Node<T> node, long[] parameters, @Nullable Climate.RTree.Leaf<T> lastUltimate, @Nullable Climate.RTree.Leaf<T> lastPenultimate, Climate.DistanceMetric<T> distanceMetric) {
        long ultimateDistance = lastUltimate != null ? distanceMetric.distance(lastUltimate, parameters) : Long.MAX_VALUE;
        long penultimateDistance = lastPenultimate != null ? distanceMetric.distance(lastPenultimate, parameters) : Long.MAX_VALUE;
        long nodeDistance = Long.MAX_VALUE;
        Climate.RTree.Leaf<T> ultimate = lastUltimate;
        Climate.RTree.Leaf<T> penultimate = lastPenultimate;


        if (ultimateDistance > penultimateDistance) {
            Climate.RTree.Leaf<T> newPenultimate = ultimate;
            ultimate = penultimate;
            penultimate = newPenultimate;
            long newDistance = ultimateDistance;
            ultimateDistance = penultimateDistance;
            penultimateDistance = newDistance;
        }

        if (node instanceof Climate.RTree.SubTree<T> subTree) {
            for (Climate.RTree.Node<T> currentNode : subTree.children) {
                nodeDistance = distanceMetric.distance(currentNode, parameters);
                // Continue searching the current node if its penultimate distance is closer than the previous result
                if (nodeDistance < penultimateDistance) {
                    SearchResult<T> endResult = this.blueprint$searchNode(currentNode, parameters, lastUltimate, lastPenultimate, distanceMetric);
                    Climate.RTree.Leaf<T> leaf = endResult.ultimate();
                    // Set the node distance to that of the leaf (should be closer)
                    nodeDistance = endResult.ultimateDistance();
                    // The penultimate leaf should be a different biome than the ultimate for it to be valid
                    if (nodeDistance < ultimateDistance) {
                        if (!this.blueprint$getBiomeKey(leaf).equals(this.blueprint$getBiomeKey(ultimate))) {
                            penultimate = ultimate;
                            penultimateDistance = ultimateDistance;
                        }
                        ultimate = leaf;
                        ultimateDistance = nodeDistance;
                    } else if (!this.blueprint$getBiomeKey(leaf).equals(this.blueprint$getBiomeKey(ultimate))) {
                        penultimate = leaf;
                        penultimateDistance = nodeDistance;
                    }
                }
            }
            return new SearchResult<>(ultimate, ultimateDistance, penultimate, penultimateDistance);
        } else if (node instanceof Climate.RTree.Leaf<T> leaf) {
            nodeDistance = distanceMetric.distance(leaf, parameters);
            // Same logic as the subtree
            if (nodeDistance < penultimateDistance) {
                if (nodeDistance < ultimateDistance) {
                    if (!this.blueprint$getBiomeKey(leaf).equals(this.blueprint$getBiomeKey(ultimate))) {
                        penultimate = ultimate;
                        penultimateDistance = ultimateDistance;
                    }
                    ultimate = leaf;
                    ultimateDistance = nodeDistance;
                } else if (!this.blueprint$getBiomeKey(leaf).equals(this.blueprint$getBiomeKey(ultimate))) {
                    penultimate = leaf;
                    penultimateDistance = nodeDistance;
                }
            }
            return new SearchResult<>(ultimate, ultimateDistance, penultimate, penultimateDistance);
        } else {
            // This should be near-impossible unless there's no root node, just return the previous result in this case
            return new SearchResult<>(ultimate, ultimateDistance, penultimate, penultimateDistance);
        }
    }

    @Unique
    private ResourceKey<?> blueprint$getBiomeKey(@Nullable Climate.RTree.Leaf<T> leafNode) {
        if (leafNode == null) {
            // This key could be anything as it's only used to compare against others
            return BlueprintBiomes.ORIGINAL_SOURCE_MARKER;
        } else {
            return ((Holder<?>) leafNode.value).unwrapKey().orElseThrow();
        }
    }
}
