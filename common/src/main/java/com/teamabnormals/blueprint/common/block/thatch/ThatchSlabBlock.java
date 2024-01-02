package com.teamabnormals.blueprint.common.block.thatch;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A {@link SlabBlock} extension with certain methods overridden to accommodate models for thatch-type slabs.
 */
@SuppressWarnings("deprecation")
public class ThatchSlabBlock extends SlabBlock {

	public ThatchSlabBlock(Properties properties) {
		super(properties);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}
}