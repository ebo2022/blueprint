package com.teamabnormals.blueprint.core.mixin.fabric;

import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(Ingredient.class)
public interface IngredientAccessor {

    @Accessor("values")
    Ingredient.Value[] blueprint$getValues();

    @Invoker("fromValues")
    static Ingredient blueprint$fromValues(Stream<? extends Ingredient.Value> values) {
        throw new AssertionError();
    }
}
