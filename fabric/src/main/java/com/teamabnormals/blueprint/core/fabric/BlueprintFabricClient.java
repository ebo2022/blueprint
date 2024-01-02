package com.teamabnormals.blueprint.core.fabric;

import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.blueprint.core.events.client.EntityRendererEvents;
import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlueprintFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererEvents.REGISTER_RENDERERS.getInvoker().event(new EntityRendererContextImpl());
        EntityRendererEvents.REGISTER_LAYER_DEFINITIONS.getInvoker().event(new LayerDefinitionContextImpl());
        BlueprintFabric.forType(BuiltInRegistries.ITEM, BEWLRBlockItem.class, item -> {
            try {
                BuiltinItemRendererRegistry.INSTANCE.register(item, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
                    BEWLRBlockItem.LazyBEWLR lazyBEWLR = item.getBewlr().get();
                    BlockEntityWithoutLevelRenderer value = lazyBEWLR.value;
                    if (value != null)
                        value.renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                    else
                        lazyBEWLR.cache(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()).renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ModLifecycleEvents.COMMON_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.CLIENT_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.LOAD_COMPLETE.getInvoker().event(IParallelDispatcher.EMPTY);
        BlueprintFabric.onPostInitialize();
    }

    private static class EntityRendererContextImpl implements EntityRendererEvents.RegisterRenderersEvent.Context {

        @Override
        public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider) {
            EntityRendererRegistry.register(entityType, entityRendererProvider);
        }

        @Override
        public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
            BlockEntityRenderers.register(blockEntityType, blockEntityRendererProvider);
        }
    }

    private static class LayerDefinitionContextImpl implements EntityRendererEvents.RegisterLayerDefinitionsEvent.Context {

        @Override
        public void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
            EntityModelLayerRegistry.registerModelLayer(layerLocation, supplier::get);
        }
    }
}
