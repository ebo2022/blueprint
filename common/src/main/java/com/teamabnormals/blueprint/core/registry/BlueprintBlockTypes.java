package com.teamabnormals.blueprint.core.registry;

import com.mojang.serialization.MapCodec;
import com.teamabnormals.blueprint.common.block.BlueprintFallingBlock;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.registry.BlockTypeSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHolder;
import net.minecraft.world.level.block.Block;

/**
 * Built-in block type codecs for Blueprint's blocks.
 *
 * @author ebo2022
 */
public class BlueprintBlockTypes {
    private static final BlockTypeSubRegistryHelper HELPER = Blueprint.REGISTRY_HELPER.getBlockTypeSubHelper();

    public static final RegistryHolder<MapCodec<? extends Block>, MapCodec<BlueprintFallingBlock>> FALLING_BLOCK = HELPER.createBlockType("falling_block", BlueprintFallingBlock.CODEC);
}
