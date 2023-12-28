package com.teamabnormals.blueprint.core.other;

import com.teamabnormals.blueprint.core.util.DataUtil.CustomNoteBlockInstrument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author abigailfails
 */
public final class BlueprintCommonEvents {
	public static final String NOTE_KEY = "minecraft:note";
	public static List<CustomNoteBlockInstrument> SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS = null;

	public static boolean onNoteBlockPlay(Level level, BlockPos pos, int note) {
		if (SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS != null) {
			if (!level.isClientSide()) {
				for (CustomNoteBlockInstrument instrument : SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS) {
					if (instrument.test(level, pos.relative(instrument.worksAboveNoteBlock() ? Direction.UP : Direction.DOWN))) {
						float pitch;
						if (instrument.isTunable()) {
							pitch = (float) Math.pow(2.0D, (note - 12) / 12.0D);
							// TODO: replace this with NetworkUtil.spawnParticle when that's implemented
							//level.addParticle(
									//ParticleTypes.NOTE, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.2, (double)blockPos.getZ() + 0.5, (double)k / 24.0, 0.0, 0.0
							//);
						} else {
							pitch = 1.0F;
						}
						SoundEvent sound = instrument.getSound();
						level.playSeededSound(
								null,
								(double) pos.getX() + 0.5,
								(double) pos.getY() + 0.5,
								(double) pos.getZ() + 0.5,
								sound,
								SoundSource.RECORDS,
								3.0F,
								pitch,
								level.random.nextLong()
						);
						return false;
					}
				}
			}
		}
		return true;
	}

	// TODO: implement separately on each platform
	//public static float onBreakSpeed(Entity entity, BlockState blockState) {
		//if (blockState.is(BlueprintBlockTags.LEAF_PILES) && event.getEntity().getMainHandItem().is(Tags.Items.SHEARS))
			//event.setNewSpeed(15.0F);
	//}
}