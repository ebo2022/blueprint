package com.minecraftabnormals.abnormals_core.common.blocks.wood;

import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemCategoryFiller;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.NonNullList;

public class AbnormalsLeavesBlock extends LeavesBlock {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.DARK_OAK_LEAVES);

	public AbnormalsLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this.asItem(), group, items);
	}
}