package com.teamabnormals.blueprint.core.events.client;

import com.teamabnormals.blueprint.core.events.SimpleEvent;
import com.teamabnormals.blueprint.core.util.EventUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Events related to registering entity renderers and layer definitions.
 *
 * @author ebo2022
 */
public interface EntityRendererEvents {
    SimpleEvent<RegisterRenderersEvent> REGISTER_RENDERERS = EventUtil.createVoid(RegisterRenderersEvent.class);
    SimpleEvent<RegisterLayerDefinitionsEvent> REGISTER_LAYER_DEFINITIONS = EventUtil.createVoid(RegisterLayerDefinitionsEvent.class);

    /**
     * The functional interface representing listeners for the {@link #REGISTER_RENDERERS} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface RegisterRenderersEvent {
        /**
         * Called when entity and block-entity renderers are ready to be registered.
         *
         * @param context The context to register the renderers.
         */
        void event(Context context);

        /**
         * Provides methods for registering entity and block-entity renderers.
         */
        interface Context {
            /**
             * Registers an entity renderer.
             *
             * @param entityType             The {@link EntityType} the renderer is for.
             * @param entityRendererProvider The {@link EntityRendererProvider} for the entity renderer.
             */
            <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);

            /**
             * Registers a block-entity renderer.
             *
             * @param blockEntityType             The {@link BlockEntityType} the renderer is for.
             * @param blockEntityRendererProvider The {@link BlockEntityRendererProvider} for the block-entity renderer.
             */
            <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider);
        }
    }

    /**
     * The functional interface for representing listeners of the {@link #REGISTER_LAYER_DEFINITIONS} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface RegisterLayerDefinitionsEvent {
        /**
         * Called when layer definitions are ready to be registered.
         *
         * @param context The context to register layer definitions.
         */
        void event(Context context);

        /**
         * Provides a method for registering layer definitions.
         */
        interface Context {
            /**
             * Registers a layer definition.
             *
             * @param layerLocation The {@link ModelLayerLocation} to use for the layer definition.
             * @param supplier      A {@link Supplier} to the layer definition.
             */
            void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier);
        }
    }
}
