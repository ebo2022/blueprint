package com.teamabnormals.blueprint.core.api.condition;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents a resource condition, similar to <code>ICondition</code> on Forge.
 */
public interface IBlueprintResourceCondition {

    /**
     * @return Whether the condition has been met.
     */
    boolean test();

    /**
     * @return A {@link Codec} to encode and decode instances of this condition. It should also be registered using
     */
    Codec<? extends IBlueprintResourceCondition> codec();

    /**
     * @return The name of the condition.
     */
    ResourceLocation getName();

    /**
     * Marks resource conditions which Blueprint will transform into platform-specific versions during data generation.
     * <p><b>Mods should not have to use this class for any reason!</b></p>
     */
    interface PlatformPlaceholder extends IBlueprintResourceCondition {

        @Override
        default boolean test() {
            throw new UnsupportedOperationException();
        }

        @Override
        default Codec<? extends IBlueprintResourceCondition> codec() {
            throw new UnsupportedOperationException();
        }

        @Override
        default ResourceLocation getName() {
            throw new UnsupportedOperationException();
        }
    }
}
