package com.teamabnormals.blueprint.core;

import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.other.BlueprintCommonEvents;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.crafting.Ingredient;

public class Blueprint {
    public static final String MOD_ID = "blueprint";

    public static void init() {
        ModLifecycleEvents.LOAD_COMPLETE.registerListener(Blueprint::onLoadComplete);
    }

    private static void onLoadComplete(IParallelDispatcher dispatcher) {
        dispatcher.enqueueWork(() -> {
            Chicken.FOOD_ITEMS = DataUtil.compoundIngredient(Chicken.FOOD_ITEMS, Ingredient.of(BlueprintItemTags.CHICKEN_FOOD));
            Pig.FOOD_ITEMS = DataUtil.compoundIngredient(Pig.FOOD_ITEMS, Ingredient.of(BlueprintItemTags.PIG_FOOD));
            Strider.FOOD_ITEMS = DataUtil.compoundIngredient(Strider.FOOD_ITEMS, Ingredient.of(BlueprintItemTags.STRIDER_FOOD));
            Strider.TEMPT_ITEMS = DataUtil.compoundIngredient(Strider.TEMPT_ITEMS, Ingredient.of(BlueprintItemTags.STRIDER_TEMPT_ITEMS));
            Ocelot.TEMPT_INGREDIENT = DataUtil.compoundIngredient(Ocelot.TEMPT_INGREDIENT, Ingredient.of(BlueprintItemTags.OCELOT_FOOD));
            Cat.TEMPT_INGREDIENT = DataUtil.compoundIngredient(Cat.TEMPT_INGREDIENT, Ingredient.of(BlueprintItemTags.CAT_FOOD));

            DataUtil.getSortedAlternativeDispenseBehaviors().forEach(DataUtil.AlternativeDispenseBehavior::register);
            BlueprintCommonEvents.SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS = DataUtil.getSortedCustomNoteBlockInstruments();
        });
    }
}
