package com.teamabnormals.blueprint.core;

import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.client.renderer.BlueprintBoatRenderer;
import com.teamabnormals.blueprint.client.renderer.block.BlueprintChestBlockEntityRenderer;
import com.teamabnormals.blueprint.core.events.client.EntityRendererEvents;
import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.events.lifecycle.ServerLifecycleEvents;
import com.teamabnormals.blueprint.core.other.BlueprintCommonEvents;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.blueprint.core.registry.BlueprintBlockEntityTypes;
import com.teamabnormals.blueprint.core.registry.BlueprintBoatTypes;
import com.teamabnormals.blueprint.core.registry.BlueprintEntityTypes;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import com.teamabnormals.blueprint.core.util.PlatformUtil;
import com.teamabnormals.blueprint.core.util.network.BlueprintNetworkChannel;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

/**
 * Mod class for the Blueprint mod.
 *
 * @author SmellyModder (Luke Tonon)
 * @author bageldotjpg
 * @author Jackson
 * @author abigailfails
 * @author ebo2022
 */
public class Blueprint {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "blueprint";
    public static final String PROTOCOL_VERSION = "BP2";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public static final BlueprintNetworkChannel CHANNEL = BlueprintNetworkChannel.create(new ResourceLocation(MOD_ID, "net"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init() {
        ModLifecycleEvents.LOAD_COMPLETE.registerListener(Blueprint::postLoadingSetup);
        ServerLifecycleEvents.ABOUT_TO_START.registerListener(DataUtil::onServerAboutToStart);
        if (PlatformUtil.getSide().isClient()) {
            EntityRendererEvents.REGISTER_LAYER_DEFINITIONS.registerListener(Blueprint::registerLayerDefinitions);
            EntityRendererEvents.REGISTER_RENDERERS.registerListener(Blueprint::rendererSetup);
        }
    }

    @Environment(EnvType.CLIENT)
    private static void registerLayerDefinitions(EntityRendererEvents.RegisterLayerDefinitionsEvent.Context context) {
        BlueprintBoatTypes.registerLayerDefinitions(context);
    }

    @Environment(EnvType.CLIENT)
    private static void rendererSetup(EntityRendererEvents.RegisterRenderersEvent.Context context) {
        context.registerEntityRenderer(BlueprintEntityTypes.BOAT.get(), ctx -> new BlueprintBoatRenderer(ctx, false));
        context.registerEntityRenderer(BlueprintEntityTypes.CHEST_BOAT.get(), ctx -> new BlueprintBoatRenderer(ctx, true));
        context.registerEntityRenderer(BlueprintEntityTypes.FALLING_BLOCK.get(), FallingBlockRenderer::new);

        context.registerBlockEntityRenderer(BlueprintBlockEntityTypes.CHEST.get(), BlueprintChestBlockEntityRenderer::new);
        context.registerBlockEntityRenderer(BlueprintBlockEntityTypes.TRAPPED_CHEST.get(), BlueprintChestBlockEntityRenderer::new);
        context.registerBlockEntityRenderer(BlueprintBlockEntityTypes.SIGN.get(), SignRenderer::new);
        context.registerBlockEntityRenderer(BlueprintBlockEntityTypes.HANGING_SIGN.get(), HangingSignRenderer::new);
    }

    private static void postLoadingSetup(IParallelDispatcher dispatcher) {
        dispatcher.enqueueWork(() -> {
            Chicken.FOOD_ITEMS = DataUtil.compoundIngredient(Chicken.FOOD_ITEMS, Ingredient.of(BlueprintItemTags.CHICKEN_FOOD));
            Pig.FOOD_ITEMS = DataUtil.compoundIngredient(Pig.FOOD_ITEMS, Ingredient.of(BlueprintItemTags.PIG_FOOD));
            Strider.FOOD_ITEMS = DataUtil.compoundIngredient(Strider.FOOD_ITEMS, Ingredient.of(BlueprintItemTags.STRIDER_FOOD));
            Strider.TEMPT_ITEMS = DataUtil.compoundIngredient(Strider.TEMPT_ITEMS, Ingredient.of(BlueprintItemTags.STRIDER_TEMPT_ITEMS));
            Ocelot.TEMPT_INGREDIENT = DataUtil.compoundIngredient(Ocelot.TEMPT_INGREDIENT, Ingredient.of(BlueprintItemTags.OCELOT_FOOD));
            Cat.TEMPT_INGREDIENT = DataUtil.compoundIngredient(Cat.TEMPT_INGREDIENT, Ingredient.of(BlueprintItemTags.CAT_FOOD));

            DataUtil.getSortedAlternativeDispenseBehaviors().forEach(DataUtil.AlternativeDispenseBehavior::register);
            BlueprintCommonEvents.SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS = DataUtil.getSortedCustomNoteBlockInstruments();
        });
    }

    // TODO: register messages once all of them are added
}
