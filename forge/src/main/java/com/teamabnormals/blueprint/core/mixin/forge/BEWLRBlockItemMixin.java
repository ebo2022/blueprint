package com.teamabnormals.blueprint.core.mixin.forge;

import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(BEWLRBlockItem.class)
public class BEWLRBlockItemMixin extends BlockItem {

    @Shadow(remap = false)
    @Final
    private Supplier<BEWLRBlockItem.LazyBEWLR> bewlr;

    public BEWLRBlockItemMixin(Block arg, Properties arg2) {
        super(arg, arg2);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                BEWLRBlockItem.LazyBEWLR lazyBEWLR = BEWLRBlockItemMixin.this.bewlr.get();
                BlockEntityWithoutLevelRenderer value = lazyBEWLR.value;
                if (value != null)
                    return value;
                return lazyBEWLR.cache(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        });
    }
}