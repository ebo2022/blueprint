package com.teamabnormals.blueprint.client.renderer;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.registry.BlueprintBoatTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

/**
 * The entity renderer responsible for the rendering of Blueprint's boat entities.
 */
@Environment(EnvType.CLIENT)
public class BlueprintBoatRenderer extends BoatRenderer {
	private final Map<BlueprintBoatTypes.BlueprintBoatType, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

	public BlueprintBoatRenderer(EntityRendererProvider.Context context, boolean chest) {
		super(context, chest);
		this.boatResources = BlueprintBoatTypes.createBoatResources(context, chest);
	}

	@Override
	public ResourceLocation getTextureLocation(Boat boat) {
		return boat instanceof HasBlueprintBoatType hasBlueprintBoatType ? this.boatResources.get(hasBlueprintBoatType.getBoatType()).getFirst() : super.getTextureLocation(boat);
	}
}