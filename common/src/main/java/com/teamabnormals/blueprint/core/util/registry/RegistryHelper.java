package com.teamabnormals.blueprint.core.util.registry;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A class that works as a parent holder to children {@link ISubRegistryHelper}s.
 * <p>
 * A map is stored inside this that maps a {@link Registry} to a {@link ISubRegistryHelper} to get the child {@link ISubRegistryHelper} for a specific {@link Registry}.
 * A value put for a key in this map is a {@link ISubRegistryHelper} with the same parameterized type as the key.
 * </p>
 * Use the {@link #putSubHelper(ResourceKey, ISubRegistryHelper)} method to put a {@link ISubRegistryHelper} for a {@link Registry}.
 *
 * @author SmellyModder (Luke Tonon)
 */
public class RegistryHelper {
	private final Map<ResourceKey<? extends Registry<?>>, ISubRegistryHelper<?>> subHelpers = new HashMap<>();
	protected final String modId;

	public RegistryHelper(String modId) {
		this.modId = modId;
		this.putDefaultSubHelpers();
	}

	/**
	 * Creates a new {@link RegistryHelper} with a specified mod ID and then accepts a consumer onto it.
	 *
	 * @param modId    The mod ID for this helper.
	 * @param consumer A consumer to accept after the helper has been initialized.
	 * @return A new {@link RegistryHelper} with a specified mod ID that has had a consumer accepted onto it.
	 */
	public static RegistryHelper create(String modId, Consumer<RegistryHelper> consumer) {
		RegistryHelper helper = new RegistryHelper(modId);
		consumer.accept(helper);
		return helper;
	}

	/**
	 * @return The mod id belonging to this {@link RegistryHelper}.
	 */
	public String getModId() {
		return this.modId;
	}

	/**
	 * Creates a {@link ResourceLocation} for a string prefixed with the mod id.
	 *
	 * @param name The string to prefix.
	 * @return A {@link ResourceLocation} for a string prefixed with the mod id
	 */
	public ResourceLocation prefix(String name) {
		return new ResourceLocation(this.modId, name);
	}

	/**
	 * Creates a {@link ResourceKey} for a string prefixed with the mod id.
	 *
	 * @param name The string to prefix.
	 * @return A {@link ResourceKey} for a string prefixed with the mod id
	 */
	public <T> ResourceKey<T> createKey(ResourceKey<? extends Registry<T>> registry, String name) {
		return ResourceKey.create(registry, this.prefix(name));
	}

	/**
	 * Puts a {@link ISubRegistryHelper} for a {@link Registry}.
	 *
	 * @param registry  The {@link ResourceKey} of the {@link Registry} the {@link ISubRegistryHelper} is for.
	 * @param subHelper The {@link ISubRegistryHelper} to be mapped.
	 * @param <V>       The type of objects to register in the helper.
	 */
	public <V> void putSubHelper(ResourceKey<? extends Registry<V>> registry, ISubRegistryHelper<V> subHelper) {
		this.subHelpers.put(registry, subHelper);
	}

	/**
	 * Puts the default {@link ISubRegistryHelper}s onto the map.
	 */
	protected void putDefaultSubHelpers() {
		this.putSubHelper(Registries.ITEM, new ItemSubRegistryHelper(this));
		this.putSubHelper(Registries.BLOCK, new BlockSubRegistryHelper(this));
		this.putSubHelper(Registries.BLOCK_TYPE, new BlockTypeSubRegistryHelper(this));
		this.putSubHelper(Registries.SOUND_EVENT, new SoundSubRegistryHelper(this));
		this.putSubHelper(Registries.BLOCK_ENTITY_TYPE, new BlockEntitySubRegistryHelper(this));
		this.putSubHelper(Registries.ENTITY_TYPE, new EntitySubRegistryHelper(this));
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public <T, S extends ISubRegistryHelper<T>> S getSubHelper(ResourceKey<? extends Registry<T>> registry) {
		S subHelper = (S) this.subHelpers.get(registry);
		if (subHelper == null) {
			throw new NullPointerException("No Sub Helper is registered for the registry: " + registry);
		}
		return subHelper;
	}

	@NotNull
	public <T extends AbstractSubRegistryHelper<Item>> T getItemSubHelper() {
		return this.getSubHelper(Registries.ITEM);
	}

	@NotNull
	public <T extends AbstractSubRegistryHelper<Block>> T getBlockSubHelper() {
		return this.getSubHelper(Registries.BLOCK);
	}

	@NotNull
	public <T extends AbstractSubRegistryHelper<MapCodec<? extends Block>>> T getBlockTypeSubHelper() {
		return this.getSubHelper(Registries.BLOCK_TYPE);
	}

	@NotNull
	public <T extends AbstractSubRegistryHelper<SoundEvent>> T getSoundSubHelper() {
		return this.getSubHelper(Registries.SOUND_EVENT);
	}

	@NotNull
	public <T extends AbstractSubRegistryHelper<BlockEntityType<?>>> T getBlockEntitySubHelper() {
		return this.getSubHelper(Registries.BLOCK_ENTITY_TYPE);
	}

	@NotNull
	public <T extends AbstractSubRegistryHelper<EntityType<?>>> T getEntitySubHelper() {
		return this.getSubHelper(Registries.ENTITY_TYPE);
	}
}