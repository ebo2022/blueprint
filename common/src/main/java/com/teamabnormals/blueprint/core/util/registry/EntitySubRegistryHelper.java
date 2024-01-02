package com.teamabnormals.blueprint.core.util.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;

/**
 * A basic {@link AbstractSubRegistryHelper} for entities.
 * <p>This contains some useful registering methods for entities.</p>
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 */
public class EntitySubRegistryHelper extends AbstractSubRegistryHelper<EntityType<?>> {

	public EntitySubRegistryHelper(RegistryHelper parent) {
		super(parent, Registries.ENTITY_TYPE);
	}

	/**
	 * Creates and registers an {@link EntityType} with the type of a {@link LivingEntity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return A {@link RegistryHolder} containing the created {@link EntityType}.
	 */
	public <E extends LivingEntity> RegistryHolder<EntityType<?>, EntityType<E>> createLivingEntity(String name, EntityType.EntityFactory<E> factory, MobCategory entityClassification, float width, float height) {
		return this.wrapper.register(name, () -> createLivingEntity(factory, entityClassification, name, width, height));
	}

	/**
	 * Creates and registers an {@link EntityType} with the type of {@link Entity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return A {@link RegistryHolder} containing the created {@link EntityType}.
	 */
	public <E extends Entity> RegistryHolder<EntityType<?>, EntityType<E>> createEntity(String name, EntityType.EntityFactory<E> factory, MobCategory entityClassification, float width, float height) {
		return this.wrapper.register(name, () -> createEntity(factory, entityClassification, name, width, height));
	}

	/**
	 * Creates an {@link EntityType} with the type of {@link LivingEntity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return The created {@link EntityType}.
	 */
	public <E extends LivingEntity> EntityType<E> createLivingEntity(EntityType.EntityFactory<E> factory, MobCategory entityClassification, String name, float width, float height) {
		ResourceLocation location = this.parent.prefix(name);
		return EntityType.Builder.of(factory, entityClassification)
				.sized(width, height)
				.clientTrackingRange(64)
				.updateInterval(3)
				.build(location.toString());
	}

	/**
	 * Creates an {@link EntityType} with the type of {@link Entity}.
	 *
	 * @param name                 The entity's name.
	 * @param factory              The entity's factory.
	 * @param entityClassification The entity's classification.
	 * @param width                The width of the entity's bounding box.
	 * @param height               The height of the entity's bounding box.
	 * @return The created {@link EntityType}.
	 */
	public <E extends Entity> EntityType<E> createEntity(EntityType.EntityFactory<E> factory, MobCategory entityClassification, String name, float width, float height) {
		ResourceLocation location = this.parent.prefix(name);
		return EntityType.Builder.of(factory, entityClassification)
				.sized(width, height)
				.updateInterval(3)
				.build(location.toString());
	}

}