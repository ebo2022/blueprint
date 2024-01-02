package com.teamabnormals.blueprint.common.item;

import net.minecraft.world.item.Item;

/**
 * An {@link Item} extension that stores a specified burn time for the item.
 * <p>Blueprint automatically handles registering the burn time for the item.</p>
 */
public class FuelItem extends Item {
	private final int burnTime;

	public FuelItem(int burnTime, Properties properties) {
		super(properties);
		this.burnTime = burnTime;
	}

	public int getBurnTime() {
		return this.burnTime;
	}
}