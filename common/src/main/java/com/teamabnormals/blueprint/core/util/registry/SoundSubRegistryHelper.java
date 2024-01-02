package com.teamabnormals.blueprint.core.util.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * A basic {@link AbstractSubRegistryHelper} for sounds. This contains some useful registering methods for sounds.
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 */
public class SoundSubRegistryHelper extends AbstractSubRegistryHelper<SoundEvent> {

	public SoundSubRegistryHelper(RegistryHelper parent) {
		super(parent, Registries.SOUND_EVENT);
	}

	/**
	 * Creates and registers a {@link SoundEvent}.
	 *
	 * @param name The sound's name.
	 * @return A {@link RegistryHolder} containing the created {@link SoundEvent}.
	 */
	public RegistryHolder<SoundEvent, ?> createSoundEvent(String name) {
		return this.wrapper.register(name, () -> SoundEvent.createVariableRangeEvent(this.parent.prefix(name)));
	}
}