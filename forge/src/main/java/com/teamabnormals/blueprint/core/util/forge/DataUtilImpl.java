package com.teamabnormals.blueprint.core.util.forge;

import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.core.api.condition.*;
import com.teamabnormals.blueprint.core.mixin.forge.GiveGiftToHeroAccessor;
import com.teamabnormals.blueprint.core.mixin.forge.PotionBrewingAccessor;
import com.teamabnormals.blueprint.core.api.condition.forge.CustomConditionWrapper;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class DataUtilImpl {

    public static void registerFlammable(Block block, int encouragement, int flammability) {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(block, encouragement, flammability);
    }

    public static void registerCompostable(ItemLike item, float chance) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }

    public static void addMix(Potion input, Item reactant, Potion result) {
        PotionBrewingAccessor.blueprint$addMix(input, reactant, result);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerBlockColor(BlockColor color, List<Holder<Block>> blocksIn) {
        blocksIn.removeIf(block -> !block.isBound());
        if (blocksIn.size() > 0) {
            Block[] blocks = new Block[blocksIn.size()];
            for (int i = 0; i < blocksIn.size(); i++) {
                blocks[i] = blocksIn.get(i).value();
            }
            Minecraft.getInstance().getBlockColors().register(color, blocks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerBlockItemColor(ItemColor color, List<Holder<Block>> blocksIn) {
        blocksIn.removeIf(block -> !block.isBound());
        if (blocksIn.size() > 0) {
            Block[] blocks = new Block[blocksIn.size()];
            for (int i = 0; i < blocksIn.size(); i++) {
                blocks[i] = blocksIn.get(i).value();
            }
            Minecraft.getInstance().getItemColors().register(color, blocks);
        }
    }

    public static void registerVillagerGift(VillagerProfession profession) {
        ResourceLocation name = BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession);
        if (name != null) {
            GiveGiftToHeroAccessor.blueprint$getGifts().put(profession, new ResourceLocation(name.getNamespace(), "gameplay/hero_of_the_village/" + name.getPath() + "_gift"));
        }
    }

    public static Ingredient compoundIngredient(Ingredient... ingredients) {
        return CompoundIngredient.of(ingredients);
    }

    public static <T extends IBlueprintResourceCondition> void registerResourceCondition(ResourceLocation name, Codec<T> codec) {
        ForgeUtil.<RegisterEvent>addModBusListener(name.getNamespace(), evt -> evt.register(NeoForgeRegistries.Keys.CONDITION_CODECS, helper -> helper.register(name, codec.xmap(CustomConditionWrapper::new, CustomConditionWrapper::condition))));
    }
}
