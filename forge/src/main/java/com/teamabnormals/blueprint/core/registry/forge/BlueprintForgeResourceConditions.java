package com.teamabnormals.blueprint.core.registry.forge;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.api.condition.*;
import com.teamabnormals.blueprint.core.api.condition.forge.CustomConditionWrapper;
import com.teamabnormals.blueprint.core.api.condition.forge.BlueprintTagEmptyCondition;
import com.teamabnormals.blueprint.core.api.condition.forge.BlueprintTagPopulatedCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains Blueprint's forge resource conditions and utilities to convert between Blueprint and Forge conditions.
 *
 * @author ebo2022
 */
public class BlueprintForgeResourceConditions {
    public static final ResourceLocation TAG_POPULATED = Blueprint.REGISTRY_HELPER.prefix("tag_populated");
    public static final ResourceLocation TAG_EMPTY = Blueprint.REGISTRY_HELPER.prefix("tag_empty");

    /**
     * Registers Blueprint's resource conditions.
     * <p><b>This is for internal use only!</b></p>
     */
    public static void registerOnEvent(RegisterEvent event) {
        event.register(NeoForgeRegistries.Keys.CONDITION_CODECS, helper -> {
            helper.register(TAG_POPULATED, BlueprintTagPopulatedCondition.CODEC);
            helper.register(TAG_EMPTY, BlueprintTagEmptyCondition.CODEC);
        });
    }

    /**
     * Transforms an {@link IBlueprintResourceCondition} into its forge version.
     *
     * @param condition The {@link IBlueprintResourceCondition} to transform.
     * @return An {@link ICondition} equivalent to the original condition in behavior.
     */
    @SuppressWarnings("unchecked")
    public static ICondition transform(IBlueprintResourceCondition condition) {
        if (condition instanceof IBlueprintResourceCondition.PlatformPlaceholder placeholder) {
            if (placeholder == TrueCondition.INSTANCE) {
                return net.neoforged.neoforge.common.conditions.TrueCondition.INSTANCE;
            } else if (placeholder == FalseCondition.INSTANCE) {
                return net.neoforged.neoforge.common.conditions.FalseCondition.INSTANCE;
            } else if (placeholder instanceof AndCondition andCondition) {
                return new net.neoforged.neoforge.common.conditions.AndCondition(andCondition.children().stream().map(BlueprintForgeResourceConditions::transform).toList());
            } else if (placeholder instanceof OrCondition orCondition) {
                return new net.neoforged.neoforge.common.conditions.OrCondition(orCondition.children().stream().map(BlueprintForgeResourceConditions::transform).toList());
            } else if (placeholder instanceof NotCondition notCondition) {
                return new net.neoforged.neoforge.common.conditions.NotCondition(transform(notCondition.invert()));
            } else if (placeholder instanceof TagPopulatedCondition<?> tagPopulatedCondition) {
                return new BlueprintTagPopulatedCondition<>(tagPopulatedCondition.tagKey());
            } else if (placeholder instanceof TagEmptyCondition<?> tagEmptyCondition) {
                // Use Forge's version for items if possible
                if (tagEmptyCondition.tagKey().registry() == Registries.ITEM)
                    return new net.neoforged.neoforge.common.conditions.TagEmptyCondition((TagKey<Item>) tagEmptyCondition.tagKey());
                return new BlueprintTagEmptyCondition<>(tagEmptyCondition.tagKey());
            } else if (placeholder instanceof ModsLoadedCondition modCondition) {
                if (modCondition.mods().isEmpty())
                    throw new IllegalStateException("Empty modlist in ModsLoadedCondition");
                if (modCondition.mods().size() == 1)
                    return new ModLoadedCondition(modCondition.mods().get(0));
                return modCondition.require() ?
                        new net.neoforged.neoforge.common.conditions.AndCondition(modCondition.mods().stream().<ICondition>map(ModLoadedCondition::new).toList()) :
                        new net.neoforged.neoforge.common.conditions.OrCondition(modCondition.mods().stream().<ICondition>map(ModLoadedCondition::new).toList());
            } else {
                throw new IllegalStateException("Unhandled Blueprint placeholder condition: " + condition.getClass().getSimpleName());
            }
        }
        return new CustomConditionWrapper<>(condition);
    }
}
