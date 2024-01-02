package com.teamabnormals.blueprint.core.registry;

import com.teamabnormals.blueprint.common.entity.BlueprintBoat;
import com.teamabnormals.blueprint.common.entity.BlueprintChestBoat;
import com.teamabnormals.blueprint.common.entity.BlueprintFallingBlockEntity;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * Registry class for the built-in {@link EntityType}s.
 */
public final class BlueprintEntityTypes {
	private static final EntitySubRegistryHelper HELPER = Blueprint.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryHolder<EntityType<?>, EntityType<BlueprintBoat>> BOAT = HELPER.createEntity("boat", BlueprintBoat::new, MobCategory.MISC, 1.375F, 0.5625F);
	public static final RegistryHolder<EntityType<?>, EntityType<BlueprintChestBoat>> CHEST_BOAT = HELPER.createEntity("chest_boat", BlueprintChestBoat::new, MobCategory.MISC, 1.375F, 0.5625F);
	public static final RegistryHolder<EntityType<?>, EntityType<BlueprintFallingBlockEntity>> FALLING_BLOCK = HELPER.createEntity("falling_block", BlueprintFallingBlockEntity::new, MobCategory.MISC, 0.98F, 0.98F);
}