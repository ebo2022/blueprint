package com.teamabnormals.blueprint.core.other;

import com.teamabnormals.blueprint.core.util.DataUtil.CustomNoteBlockInstrument;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
							NetworkUtil.spawnParticle(NOTE_KEY, (ServerLevel) level, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, note / 24.0D, 0.0D, 0.0D);
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