package com.teamabnormals.blueprint.common.block.thatch;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A {@link StairBlock} extension with certain methods overridden to accommodate models for thatch-type stairs.
 */
@SuppressWarnings("deprecation")
public class ThatchStairBlock extends StairBlock {

	public ThatchStairBlock(BlockState state, Properties properties) {
		super(state, properties);
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