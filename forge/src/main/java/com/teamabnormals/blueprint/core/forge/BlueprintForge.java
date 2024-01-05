package com.teamabnormals.blueprint.core.forge;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.api.WoodTypeRegistryHelper;
import com.teamabnormals.blueprint.core.events.client.EntityRendererEvents;
import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.registry.forge.BlueprintForgeResourceConditions;
import com.teamabnormals.blueprint.core.util.forge.ForgeUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

/**
 * The Forge mod class for Blueprint.
 *
 * @author SmellyModder (Luke Tonon)
 * @author bageldotjpg
 * @author Jackson
 * @author abigailfails
 * @author ebo2022
 */
@Mod(Blueprint.MOD_ID)
public class BlueprintForge {

    public BlueprintForge() {
        Blueprint.init();
        ForgeUtil.addModBusConsumer(Blueprint.MOD_ID, bus -> {
            bus.addListener(BlueprintForgeResourceConditions::registerOnEvent);
            bus.<FMLCommonSetupEvent>addListener(event -> {
                ModLifecycleEvents.COMMON_SETUP.getInvoker().event(ForgeUtil.createParallelDispatcher(event));
                event.enqueueWork(WoodTypeRegistryHelper::registerWoodTypes);
            });
            bus.<FMLLoadCompleteEvent>addListener(event -> ModLifecycleEvents.LOAD_COMPLETE.getInvoker().event(ForgeUtil.createParallelDispatcher(event)));
            if (FMLEnvironment.dist == Dist.CLIENT) {
                bus.<FMLClientSetupEvent>addListener(event -> {
                    ModLifecycleEvents.CLIENT_SETUP.getInvoker().event(ForgeUtil.createParallelDispatcher(event));
                    WoodTypeRegistryHelper.getWoodTypes().forEach(Sheets::addWoodType);
                });
                bus.<EntityRenderersEvent.RegisterRenderers>addListener(event -> EntityRendererEvents.REGISTER_RENDERERS.getInvoker().event(new EntityRendererContextImpl(event)));
                bus.<EntityRenderersEvent.RegisterLayerDefinitions>addListener(event -> EntityRendererEvents.REGISTER_LAYER_DEFINITIONS.getInvoker().event(new LayerDefinitionContextImpl(event)));
            } else if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
                bus.<FMLDedicatedServerSetupEvent>addListener(event -> ModLifecycleEvents.DEDICATED_SERVER_SETUP.getInvoker().event(ForgeUtil.createParallelDispatcher(event)));
            }
        });
    }



    private record EntityRendererContextImpl(EntityRenderersEvent.RegisterRenderers event) implements EntityRendererEvents.RegisterRenderersEvent.Context {

        @Override
        public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider) {
            this.event.registerEntityRenderer(entityType, entityRendererProvider);
        }

        @Override
        public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
            this.event.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
        }
    }

    private record LayerDefinitionContextImpl(EntityRenderersEvent.RegisterLayerDefinitions event) implements EntityRendererEvents.RegisterLayerDefinitionsEvent.Context {

        @Override
        public void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
            event.registerLayerDefinition(layerLocation, supplier);
        }
    }
}
