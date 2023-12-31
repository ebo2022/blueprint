package com.teamabnormals.blueprint.core.mixin.forge;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {

    @Invoker("addMix")
    static void blueprint$addMix(Potion input, Item reactant, Potion result) {
        throw new AssertionError();
    }
}
