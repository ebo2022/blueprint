package com.teamabnormals.blueprint.core.mixin;

import com.teamabnormals.blueprint.core.api.StructureBlockStateReplacer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

	@ModifyVariable(method = "placeInWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;nbt:Lnet/minecraft/nbt/CompoundTag;", ordinal = 0), index = 22)
	private BlockState placeInWorld(BlockState state, ServerLevelAccessor level) {
		return StructureBlockStateReplacer.getReplacementState(level, state);
	}
}