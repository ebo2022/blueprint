package com.teamabnormals.blueprint.core.util.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * An abstract implementation class of {@link ISubRegistryHelper}.
 * This contains a {@link RegistryHelper} parent and a {@link RegistryWrapper} to register objects.
 * <p> It is recommended you use this for making a new {@link ISubRegistryHelper}. </p>
 *
 * @param <T> The type of objects this helper registers.
 * @author SmellyModder (Luke Tonon)
 * @author ebo2022
 * @see ISubRegistryHelper
 */
public abstract class AbstractSubRegistryHelper<T> implements ISubRegistryHelper<T> {
	protected final RegistryHelper parent;
	protected final RegistryWrapper<T> wrapper;

	public AbstractSubRegistryHelper(RegistryHelper parent, ResourceKey<? extends Registry<T>> registry) {
		this.parent = parent;
		this.wrapper = createWrapper(parent.getModId(), registry);
	}

	/**
	 * @return The parent {@link RegistryHelper} this is a child of.
	 */
	@Override
	public RegistryHelper getParent() {
		return this.parent;
	}

	/**
	 * @return The {@link RegistryWrapper} belonging to this {@link AbstractSubRegistryHelper}.
	 */
	@Override
	public RegistryWrapper<T> getWrapper() {
		return this.wrapper;
	}

	/**
	 * Creates a new {@link RegistryWrapper} for the given namespace and registry.
	 *
	 * @param namespace    The namespace to register objects under, and to use as a mod id to find the appropriate Forge event bus.
	 * @param registryKey  The {@link ResourceKey} of the registry the wrapper is for.
	 * @param <T>          The type of object being registered.
	 * @return A new {@link RegistryWrapper} for the given namespace and registry.
	 */
	@ExpectPlatform
	protected static <T> RegistryWrapper<T> createWrapper(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
		throw new AssertionError();
	}
}