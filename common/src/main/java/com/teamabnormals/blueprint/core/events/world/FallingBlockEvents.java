package com.teamabnormals.blueprint.core.events.world;

import com.teamabnormals.blueprint.core.events.SimpleEvent;
import com.teamabnormals.blueprint.core.util.EventUtil;
import com.teamabnormals.blueprint.core.util.mutable.DirectMutable;
import com.teamabnormals.blueprint.core.util.mutable.Mutable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This class holds two events related to falling blocks.
 *
 * @author Markus1002
 * @author ebo2022
 */
public interface FallingBlockEvents {
    SimpleEvent<FallingBlockEvent> FALLING_BLOCK = EventUtil.createVoid(FallingBlockEvent.class);

    SimpleEvent<FallingBlockTickEvent> FALLING_BLOCK_TICK = EventUtil.createBoolean(FallingBlockTickEvent.class);

    /**
     * Handles the processing of the {@link FallingBlockEvent}.
     */
    static FallingBlockEntity onBlockFall(Level level, BlockPos pos, BlockState state, FallingBlockEntity fallingBlockEntity) {
        Mutable<FallingBlockEntity> entity = DirectMutable.withInitial(fallingBlockEntity);
        FALLING_BLOCK.getInvoker().event(level, pos, state, entity);
        return entity.get();
    }

    /**
     * The functional interface for representing listeners of the {@link #FALLING_BLOCK} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface FallingBlockEvent {
        /**
         * Called when a {@link FallingBlockEntity} is about to be spawned in the {@link FallingBlockEntity#fall(Level, BlockPos, BlockState)} method.
         *
         * @param level              The level the falling block is in.
         * @param pos                The {@link BlockPos} of the block that is falling.
         * @param state              The {@link BlockState} of the falling block.
         * @param fallingBlockEntity The {@link FallingBlockEntity} to use. This can be modified by events to return a different entity instead.
         */
        void event(Level level, BlockPos pos, BlockState state, Mutable<FallingBlockEntity> fallingBlockEntity);
    }

    /**
     * The functional interface for representing listeners of the {@link #FALLING_BLOCK_TICK} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface FallingBlockTickEvent {
        /**
         * Called when a {@link FallingBlockEntity} is ticked in the {@link FallingBlockEntity#tick()} method.
         *
         * @param fallingBlockEntity The {@link FallingBlockEntity} being ticked.
         * @return Whether the {@link FallingBlockEntity} should be updated.
         */
        boolean event(FallingBlockEntity fallingBlockEntity);
    }
}
