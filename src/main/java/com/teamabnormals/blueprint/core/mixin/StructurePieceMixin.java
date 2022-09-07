package com.teamabnormals.blueprint.core.mixin;

import com.teamabnormals.blueprint.core.api.StructureBlockStateReplacer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StructurePiece.class)
public class StructurePieceMixin {

	@ModifyVariable(method = "createChest(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Ljava/util/Random;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.BY, by = -2), argsOnly = true)
	protected BlockState createChest(BlockState state, ServerLevelAccessor accessor) {
		return StructureBlockStateReplacer.getReplacementState(accessor, state);
	}

	@ModifyVariable(method = "placeBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.BY, by = -2), argsOnly = true)
	protected BlockState placeBlock(BlockState state, WorldGenLevel level) {
		return StructureBlockStateReplacer.getReplacementState(level, state);
	}
}