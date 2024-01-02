package com.teamabnormals.blueprint.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * An {@link BlockItem} extension that stores a specified burn time for the item.
 * <p>Blueprint automatically handles registering the burn time for the item.</p>
 */
public class FuelBlockItem extends BlockItem {
	private final int burnTime;

	public FuelBlockItem(Block block, int burnTime, Properties properties) {
		super(block, properties);
		this.burnTime = burnTime;
	}

	public int getBurnTime() {
		return this.burnTime;
	}
}