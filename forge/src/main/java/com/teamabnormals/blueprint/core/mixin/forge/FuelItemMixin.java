package com.teamabnormals.blueprint.core.mixin.forge;


import com.teamabnormals.blueprint.common.item.BEWLRFuelBlockItem;
import com.teamabnormals.blueprint.common.item.FuelBlockItem;
import com.teamabnormals.blueprint.common.item.FuelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({FuelItem.class, FuelBlockItem.class, BEWLRFuelBlockItem.class})
public abstract class FuelItemMixin implements IItemExtension {
    @Shadow(remap = false)
    @Final
    private int burnTime;

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }
}
