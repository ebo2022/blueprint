package com.teamabnormals.blueprint.core.util.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

/**
 * An interface for 'sub' registry helpers used in {@link RegistryHelper}.
 *
 * @param <T> The type of objects this helper registers.
 * @author SmellyModder (Luke Tonon)
 * @author ebo2022
 */
public interface ISubRegistryHelper<T> {
	/**
	 * @return The {@link RegistryHelper} this is a child of.
	 */
	RegistryHelper getParent();

	/**
	 * @return The {@link RegistryWrapper} for registering.
	 */
	RegistryWrapper<T> getWrapper();

	/**
	 * Provides access to platform-specific registry methods, intended for internal use in {@link ISubRegistryHelper}s.
	 *
	 * @param <T> The type of object this wrapper registers.
	 */
	@ApiStatus.NonExtendable
	interface RegistryWrapper<T> {

		/**
		 * Handles platform-specific registration of the given object.
		 * <p>On Fabric, the object is immediately added to the appropriate registry since no wait is required.</p>
		 * <p>On Forge, the object is registered to a backing <code>DeferredRegister</code> to be processed later.</p>
		 *
		 * @param name   The name to register the object under.
		 * @param object A supplier creating a new instance of the object to register.
		 * @return A {@link RegistryHolder} for the registered object.
		 */
		<R extends T> RegistryHolder<T, R> register(String name, Supplier<? extends R> object);
	}
}