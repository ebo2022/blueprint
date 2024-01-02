package com.teamabnormals.blueprint.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;

import java.util.function.Supplier;

/**
 * A {@link RotatedPillarBlock} extension that has a block it can be stripped into.
 * <p>Blueprint automatically handles registering stripping behavior.</p>
 */
public class LogBlock extends RotatedPillarBlock {
	private final Supplier<? extends Block> block;

	public LogBlock(Supplier<? extends Block> strippedBlock, Properties properties) {
		super(properties);
		this.block = strippedBlock;
	}

	public Supplier<? extends Block> getBlock() {
		return this.block;
	}
}