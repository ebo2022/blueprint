package com.teamabnormals.blueprint.common.item;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * A {@link BEWLRBlockItem} extension that stores a specified burn time for the item.
 * <p>Blueprint automatically handles registering the burn time for the item.</p>
 *
 * @author SmellyModder (Luke Tonon)
 * @see BEWLRBlockItem
 */
public class BEWLRFuelBlockItem extends BEWLRBlockItem {
	private final int burnTime;

	public BEWLRFuelBlockItem(Block block, Properties properties, Supplier<Supplier<LazyBEWLR>> bewlr, int burnTime) {
		super(block, properties, bewlr);
		this.burnTime = burnTime;
	}

	public int getBurnTime() {
		return this.burnTime;
	}
}