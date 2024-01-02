package com.teamabnormals.blueprint.core.registry;

import com.teamabnormals.blueprint.common.block.BlueprintBeehiveBlock;
import com.teamabnormals.blueprint.common.block.BlueprintChiseledBookShelfBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintChestBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintTrappedChestBlock;
import com.teamabnormals.blueprint.common.block.entity.*;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Registry class for the built-in {@link BlockEntityType}s.
 */
public final class BlueprintBlockEntityTypes {
	public static final BlockEntitySubRegistryHelper HELPER = Blueprint.REGISTRY_HELPER.getBlockEntitySubHelper();

	public static final RegistryHolder<BlockEntityType<?>, BlockEntityType<BlueprintSignBlockEntity>> SIGN = HELPER.createBlockEntity("sign", BlueprintSignBlockEntity::new, () -> BlueprintSignBlockEntity.VALID_BLOCKS);
	public static final RegistryHolder<BlockEntityType<?>, BlockEntityType<BlueprintHangingSignBlockEntity>> HANGING_SIGN = HELPER.createBlockEntity("hanging_sign", BlueprintHangingSignBlockEntity::new, () -> BlueprintHangingSignBlockEntity.VALID_BLOCKS);
	public static final RegistryHolder<BlockEntityType<?>, BlockEntityType<BlueprintBeehiveBlockEntity>> BEEHIVE = HELPER.createBlockEntity("beehive", BlueprintBeehiveBlockEntity::new, BlueprintBeehiveBlock.class);
	public static final RegistryHolder<BlockEntityType<?>, BlockEntityType<BlueprintChiseledBookShelfBlockEntity>> CHISELED_BOOKSHELF = HELPER.createBlockEntity("chiseled_bookshelf", BlueprintChiseledBookShelfBlockEntity::new, BlueprintChiseledBookShelfBlock.class);
	public static final RegistryHolder<BlockEntityType<?>, BlockEntityType<BlueprintChestBlockEntity>> CHEST = HELPER.createBlockEntity("chest", BlueprintChestBlockEntity::new, BlueprintChestBlock.class);
	public static final RegistryHolder<BlockEntityType<?>, BlockEntityType<BlueprintTrappedChestBlockEntity>> TRAPPED_CHEST = HELPER.createBlockEntity("trapped_chest", BlueprintTrappedChestBlockEntity::new, BlueprintTrappedChestBlock.class);
}