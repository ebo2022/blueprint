package com.teamabnormals.blueprint.core.util.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class BlockUtilImpl {

    public static SoundEvent getPlaceSound(BlockState state, Level level, BlockPos pos, @Nullable Entity entity) {
        return state.getSoundType().getPlaceSound();
    }
}
