package com.teamabnormals.blueprint.core.registry.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.api.condition.*;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.impl.resource.conditions.ResourceConditionsImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;

/**
 * Contains Blueprint's fabric resource conditions and utilities to convert between Blueprint and Fabric conditions..
 */
public class BlueprintFabricResourceConditions {
    public static final ResourceLocation TRUE = new ResourceLocation(Blueprint.MOD_ID, "true");
    public static final ResourceLocation FALSE = new ResourceLocation(Blueprint.MOD_ID, "false");
    public static final ResourceLocation TAG_EMPTY = new ResourceLocation(Blueprint.MOD_ID, "tag_empty");

    public static final ConditionJsonProvider TRUE_CONDITION = new ConditionJsonProvider() {
        @Override
        public ResourceLocation getConditionId() {
            return TRUE;
        }

        @Override
        public void writeParameters(JsonObject object) {
        }
    };
    public static final ConditionJsonProvider FALSE_CONDITION = new ConditionJsonProvider() {
        @Override
        public ResourceLocation getConditionId() {
            return FALSE;
        }

        @Override
        public void writeParameters(JsonObject object) {
        }
    };
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Registers Blueprint's resource conditions.
     * <p><b>This is for internal use only!</b></p>
     */
    public static void register() {
        ResourceConditions.register(TRUE, json -> true);
        ResourceConditions.register(FALSE, json -> false);
        ResourceConditions.register(TAG_EMPTY, json -> {
            @Nullable
            @SuppressWarnings("UnstableApiUsage")
            Map<ResourceKey<?>, Map<ResourceLocation, Collection<Holder<?>>>> allTags = ResourceConditionsImpl.LOADED_TAGS.get();

            if (allTags == null) {
                LOGGER.warn("Can't retrieve deserialized tags. Failing tags_empty resource condition check.");
                return false;
            }

            Map<ResourceLocation, Collection<Holder<?>>> registryTags = allTags.get(ResourceKey.createRegistryKey(new ResourceLocation(GsonHelper.getAsString(json, "registry", "minecraft:item"))));
            Collection<Holder<?>> tag = registryTags.get(new ResourceLocation(GsonHelper.getAsString(json, "tag")));
            return tag == null || tag.isEmpty();
        });
    }

    /**
     * Creates a {@link ConditionJsonProvider} for the {@link #TAG_EMPTY} condition which checks if the given tag is empty.
     *
     * @param tagKey The {@link TagKey} to check whether it's empty or not.
     * @return A {@link ConditionJsonProvider} for the {@link #TAG_EMPTY condition}.
     */
    public static <T> ConditionJsonProvider tagEmpty(TagKey<T> tagKey) {
        return new ConditionJsonProvider() {
            @Override
            public ResourceLocation getConditionId() {
                return TAG_EMPTY;
            }

            @Override
            public void writeParameters(JsonObject object) {
                // mimic Fabric's tag populated condition using the item registry as a default
                if (tagKey.registry() != Registries.ITEM)
                    object.addProperty("registry", tagKey.registry().location().toString());
                object.addProperty("tag", tagKey.location().toString());
            }
        };
    }

    public static ConditionJsonProvider transform(IBlueprintResourceCondition condition) {
        if (condition instanceof IBlueprintResourceCondition.PlatformPlaceholder placeholder) {
            if (placeholder == TrueCondition.INSTANCE) {
                return TRUE_CONDITION;
            } else if (placeholder == FalseCondition.INSTANCE) {
                return FALSE_CONDITION;
            } else if (placeholder instanceof AndCondition andCondition) {
                return DefaultResourceConditions.and(andCondition.children().stream().map(BlueprintFabricResourceConditions::transform).toArray(ConditionJsonProvider[]::new));
            } else if (placeholder instanceof OrCondition orCondition) {
                return DefaultResourceConditions.or(orCondition.children().stream().map(BlueprintFabricResourceConditions::transform).toArray(ConditionJsonProvider[]::new));
            } else if (placeholder instanceof NotCondition notCondition) {
                return DefaultResourceConditions.not(transform(notCondition.invert()));
            } else if (placeholder instanceof TagPopulatedCondition<?> tagPopulatedCondition) {
                return DefaultResourceConditions.tagsPopulated(tagPopulatedCondition.tagKey());
            } else if (placeholder instanceof TagEmptyCondition<?> tagEmptyCondition) {
                return tagEmpty(tagEmptyCondition.tagKey());
            } else if (placeholder instanceof ModsLoadedCondition modCondition) {
                if (modCondition.mods().isEmpty())
                    throw new IllegalStateException("Empty modlist in ModsLoadedCondition");
                if (modCondition.mods().size() == 1)
                    return DefaultResourceConditions.allModsLoaded(modCondition.mods().get(0));
                return modCondition.require() ?
                        DefaultResourceConditions.allModsLoaded(modCondition.mods().toArray(String[]::new)) :
                        DefaultResourceConditions.anyModLoaded(modCondition.mods().toArray(String[]::new));
            } else {
                throw new IllegalStateException("Unhandled Blueprint placeholder condition: " + condition.getClass().getSimpleName());
            }
        }
        return new CustomConditionJsonProvider<>(condition);
    }

    private record CustomConditionJsonProvider<T extends IBlueprintResourceCondition>(T condition) implements ConditionJsonProvider {

        @Override
        public ResourceLocation getConditionId() {
            return this.condition.getName();
        }

        // not very pretty... fabric gives us a JSON object so we have to copy everything over which is rather hacky
        @Override
        public void writeParameters(JsonObject object) {
            @SuppressWarnings("unchecked")
            Codec<T> codec = (Codec<T>) this.condition.codec();
            JsonObject toCopy = codec.encodeStart(JsonOps.INSTANCE, this.condition).getOrThrow(false, LOGGER::error).getAsJsonObject();
            toCopy.keySet().forEach(key -> {
                JsonElement element = toCopy.get(key);
                object.add(key, element);
            });
        }
    }
}
