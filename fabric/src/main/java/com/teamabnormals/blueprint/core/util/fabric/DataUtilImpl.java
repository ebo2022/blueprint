package com.teamabnormals.blueprint.core.util.fabric;

import com.teamabnormals.blueprint.core.mixin.fabric.IngredientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.VillagerInteractionRegistries;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ApiStatus.Internal
public class DataUtilImpl {

    public static void registerFlammable(Block block, int encouragement, int flammability) {
        FlammableBlockRegistry.getDefaultInstance().add(block, encouragement, flammability);
    }

    public static void registerCompostable(ItemLike item, float chance) {
        CompostingChanceRegistry.INSTANCE.add(item, chance);
    }

    public static void addMix(Potion input, Item reactant, Potion result) {
        FabricBrewingRecipeRegistry.registerPotionRecipe(input, Ingredient.of(reactant), result);
    }

    @Environment(EnvType.CLIENT)
    public static void registerBlockColor(BlockColor color, List<Holder<Block>> blocksIn) {
        ColorProviderRegistry.BLOCK.register(color, blocksIn.stream().filter(Holder::isBound).map(Holder::value).toArray(Block[]::new));
    }

    @Environment(EnvType.CLIENT)
    public static void registerBlockItemColor(ItemColor color, List<Holder<Block>> blocksIn) {
        ColorProviderRegistry.ITEM.register(color, blocksIn.stream().filter(Holder::isBound).map(Holder::value).toArray(Block[]::new));
    }

    public static void registerVillagerGift(VillagerProfession profession) {
        ResourceLocation name = BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession);
        if (name != null) {
            VillagerInteractionRegistries.registerGiftLootTable(profession, new ResourceLocation(name.getNamespace(), "gameplay/hero_of_the_village/" + name.getPath() + "_gift"));
        }
    }

    public static Ingredient compoundIngredient(Ingredient... ingredients) {
        if (ingredients.length == 0) {
            return Ingredient.EMPTY;
        } else if (ingredients.length == 1) {
            return ingredients[0];
        } else {
            List<Ingredient.Value> values = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                Collections.addAll(values, ((IngredientAccessor) (Object) ingredient).blueprint$getValues());
            }
            return IngredientAccessor.blueprint$fromValues(values.stream());
        }
    }
}
