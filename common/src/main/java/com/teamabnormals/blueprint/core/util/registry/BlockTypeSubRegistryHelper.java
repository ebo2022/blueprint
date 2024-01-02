package com.teamabnormals.blueprint.core.util.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

/**
 * A basic {@link AbstractSubRegistryHelper} to help register block codecs.
 *
 * @author ebo2022
 */
public class BlockTypeSubRegistryHelper extends AbstractSubRegistryHelper<MapCodec<? extends Block>> {

    public BlockTypeSubRegistryHelper(RegistryHelper parent) {
        super(parent, Registries.BLOCK_TYPE);
    }

    /**
     * Adds the given block type codec to be registered.
     *
     * @param name  The name for the block type.
     * @param codec The {@link MapCodec} to use for the block type.
     * @return A {@link RegistryHolder} for the registered block type.
     */
    public <T extends Block> RegistryHolder<MapCodec<? extends Block>, MapCodec<T>> createBlockType(String name, MapCodec<T> codec) {
        return this.wrapper.register(name, () -> codec);
    }
}
