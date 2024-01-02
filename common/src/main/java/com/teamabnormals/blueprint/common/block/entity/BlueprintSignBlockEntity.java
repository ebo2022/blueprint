package com.teamabnormals.blueprint.common.block.entity;

import com.teamabnormals.blueprint.core.registry.BlueprintBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;

/**
 * A {@link SignBlockEntity} extension used for Blueprint's signs.
 */
public class BlueprintSignBlockEntity extends SignBlockEntity {
	public static final HashSet<Block> VALID_BLOCKS = new HashSet<>();

	public BlueprintSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return BlueprintBlockEntityTypes.SIGN.get();
	}
}