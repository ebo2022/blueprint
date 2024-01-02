package com.teamabnormals.blueprint.core.util.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.client.BlueprintChestMaterials;
import com.teamabnormals.blueprint.client.renderer.block.ChestBlockEntityWithoutLevelRenderer;
import com.teamabnormals.blueprint.common.block.chest.BlueprintChestBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintTrappedChestBlock;
import com.teamabnormals.blueprint.common.block.entity.BlueprintChestBlockEntity;
import com.teamabnormals.blueprint.common.block.entity.BlueprintTrappedChestBlockEntity;
import com.teamabnormals.blueprint.common.block.sign.BlueprintCeilingHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintStandingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallSignBlock;
import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.blueprint.common.item.BEWLRFuelBlockItem;
import com.teamabnormals.blueprint.common.item.FuelBlockItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * A basic {@link AbstractSubRegistryHelper} for blocks. This contains some useful registering methods for blocks.
 *
 * @author SmellyModder (Luke Tonon)
 * @see AbstractSubRegistryHelper
 * @author ebo2022
 */
public class BlockSubRegistryHelper extends AbstractSubRegistryHelper<Block> {
	protected final RegistryWrapper<Item> itemWrapper;

	public BlockSubRegistryHelper(RegistryHelper parent) {
		super(parent, Registries.BLOCK);
		this.itemWrapper = parent.getSubHelper(Registries.ITEM).getWrapper();
	}

	@Environment(EnvType.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR chestBEWLR(boolean trapped) {
		return trapped ? new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> {
			return new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BlueprintTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState()));
		}) : new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> {
			return new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BlueprintChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState()));
		});
	}

	/**
	 * Creates and registers a {@link Block} with a {@link BlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 */
	public <B extends Block> RegistryHolder<Block, B> createBlock(String name, Supplier<? extends B> supplier) {
		RegistryHolder<Block, B> block = this.wrapper.register(name, supplier);
		this.itemWrapper.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a {@link BlockItem} with custom {@link Item.Properties}.
	 *
	 * @param name       The block's name.
	 * @param supplier   The supplied {@link Block}.
	 * @param properties The {@link Item.Properties} for the {@link BlockItem}.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 */
	public <B extends Block> RegistryHolder<Block, B> createBlock(String name, Supplier<? extends B> supplier, Item.Properties properties) {
		RegistryHolder<Block, B> block = this.wrapper.register(name, supplier);
		this.itemWrapper.register(name, () -> new BlockItem(block.get(), properties));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a specified {@link BlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @param item     The {@link BlockItem} for this {@link Block}.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 */
	public <B extends Block> RegistryHolder<Block, B> createBlockWithItem(String name, Supplier<? extends B> supplier, Supplier<BlockItem> item) {
		this.itemWrapper.register(name, item);
		return this.wrapper.register(name, supplier);
	}

	/**
	 * Creates and registers a {@link Block} with no {@link BlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied Block.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 */
	public <B extends Block> RegistryHolder<Block, B> createBlockNoItem(String name, Supplier<? extends B> supplier) {
		return this.wrapper.register(name, supplier);
	}

	/**
	 * Creates and registers a {@link Block} with its {@link BlockItem} that can be used as fuel.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @param burnTime How long the item will burn (measured in ticks).
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 */
	public <B extends Block> RegistryHolder<Block, B> createFuelBlock(String name, Supplier<? extends B> supplier, int burnTime) {
		RegistryHolder<Block, B> block = this.wrapper.register(name, supplier);
		this.itemWrapper.register(name, () -> new FuelBlockItem(block.get(), burnTime, new Item.Properties()));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a {@link BlockItem} with a {@link BlockEntityWithoutLevelRenderer}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @param bewlr    A supplier for getting the {@link BlockEntityWithoutLevelRenderer} for the {@link BlockItem}.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 */
	public <B extends Block> RegistryHolder<Block, B> createBlockWithBEWLR(String name, Supplier<? extends B> supplier, Supplier<Supplier<BEWLRBlockItem.LazyBEWLR>> bewlr) {
		RegistryHolder<Block, B> block = this.wrapper.register(name, supplier);
		this.itemWrapper.register(name, () -> new BEWLRBlockItem(block.get(), new Item.Properties(), bewlr));
		return block;
	}

	/**
	 * Creates and registers a {@link Block} with a {@link DoubleHighBlockItem}.
	 *
	 * @param name     The block's name.
	 * @param supplier The supplied {@link Block}.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 * @see DoubleHighBlockItem
	 */
	public <B extends Block> RegistryHolder<Block, B> createDoubleHighBlock(String name, Supplier<? extends B> supplier) {
		RegistryHolder<Block, B> block = this.wrapper.register(name, supplier);
		this.itemWrapper.register(name, () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
		return block;
	}

	/**
	 * Creates and registers {@link Block} with a {@link StandingAndWallBlockItem}.
	 *
	 * @param name         The block's name.
	 * @param supplier     The supplied floor {@link Block}.
	 * @param wallSupplier The supplied wall {@link Block}.
	 * @param direction    The attachment {@link Direction}.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 * @see StandingAndWallBlockItem
	 */
	public <B extends Block> RegistryHolder<Block, B> createStandingAndWallBlock(String name, Supplier<? extends B> supplier, Supplier<? extends B> wallSupplier, Direction direction) {
		RegistryHolder<Block, B> block = this.wrapper.register(name, supplier);
		this.itemWrapper.register(name, () -> new StandingAndWallBlockItem(block.get(), wallSupplier.get(), new Item.Properties(), direction));
		return block;
	}

	/**
	 * Creates and registers {@link Block} with a {@link BlockItem} that has {@link Rarity}.
	 *
	 * @param name   The block's name.
	 * @param rarity The {@link Rarity} of the {@link BlockItem}.
	 * @return A {@link RegistryHolder} containing the created {@link Block}.
	 */
	public <B extends Block> RegistryHolder<Block, B> createRareBlock(String name, Supplier<? extends B> supplier, Rarity rarity) {
		RegistryHolder<Block, B> block = this.wrapper.register(name, supplier);
		this.itemWrapper.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity)));
		return block;
	}

	/**
	 * Creates and registers {@link BlueprintChestBlock} with a {@link BEWLRFuelBlockItem}.
	 *
	 * @param name       The name for this {@link BlueprintChestBlock}.
	 * @param properties The properties for this {@link BlueprintChestBlock}.
	 * @return A {@link RegistryHolder} containing the created {@link BlueprintChestBlock}.
	 */
	public RegistryHolder<Block, BlueprintChestBlock> createChestBlock(String name, Block.Properties properties) {
		String modId = this.parent.getModId();
		String chestMaterialsName = BlueprintChestMaterials.registerMaterials(modId, name, false);
		RegistryHolder<Block, BlueprintChestBlock> block = this.wrapper.register(name + "_chest", () -> new BlueprintChestBlock(chestMaterialsName, properties));
		this.itemWrapper.register(name + "_chest", () -> new BEWLRFuelBlockItem(block.get(), new Item.Properties(), () -> () -> chestBEWLR(false), 300));
		return block;
	}

	/**
	 * Creates and registers {@link BlueprintTrappedChestBlock} with a {@link BEWLRFuelBlockItem}.
	 *
	 * @param name       The name for this {@link BlueprintTrappedChestBlock}.
	 * @param properties The properties for this {@link BlueprintTrappedChestBlock}.
	 * @return A {@link RegistryHolder} containing the created {@link BlueprintTrappedChestBlock}.
	 */
	public RegistryHolder<Block, BlueprintTrappedChestBlock> createTrappedChestBlock(String name, Block.Properties properties) {
		String modId = this.parent.getModId();
		RegistryHolder<Block, BlueprintTrappedChestBlock> block = this.wrapper.register(name + "_trapped_chest", () -> new BlueprintTrappedChestBlock(modId + ":" + name + "_trapped", properties));
		String chestMaterialsName = BlueprintChestMaterials.registerMaterials(modId, name, true);
		this.itemWrapper.register(name + "_trapped_chest", () -> new BEWLRFuelBlockItem(block.get(), new Item.Properties(), () -> () -> chestBEWLR(true), 300));
		return block;
	}

	/**
	 * Creates and registers a {@link BlueprintStandingSignBlock} and a {@link BlueprintWallSignBlock} with a {@link SignItem}.
	 *
	 * @param name     The name for the sign blocks.
	 * @param woodType The {@link WoodType} for the sign blocks. <b>Also call {@link com.teamabnormals.blueprint.core.api.WoodTypeRegistryHelper#registerWoodType(WoodType)} on this</b>!
	 * @param properties The {@link Block.Properties} to use for the sign blocks.
	 * @return A {@link Pair} containing {@link RegistryHolder}s of the {@link BlueprintStandingSignBlock} and the {@link BlueprintWallSignBlock}.
	 */
	public Pair<RegistryHolder<Block, BlueprintStandingSignBlock>, RegistryHolder<Block, BlueprintWallSignBlock>> createSignBlock(String name, WoodType woodType, Block.Properties properties) {
		RegistryHolder<Block, BlueprintStandingSignBlock> standing = this.wrapper.register(name + "_sign", () -> new BlueprintStandingSignBlock(woodType, properties));
		RegistryHolder<Block, BlueprintWallSignBlock> wall = this.wrapper.register(name + "_wall_sign", () -> new BlueprintWallSignBlock(woodType, properties.dropsLike(standing.get())));
		this.itemWrapper.register(name + "_sign", () -> new SignItem(new Item.Properties().stacksTo(16), standing.get(), wall.get()));
		return Pair.of(standing, wall);
	}

	/**
	 * Creates and registers a {@link BlueprintCeilingHangingSignBlock} and a {@link BlueprintWallHangingSignBlock} with a {@link HangingSignItem}.
	 *
	 * @param name     The name for the sign blocks.
	 * @param woodType The {@link WoodType} for the sign blocks. <b>Also call {@link com.teamabnormals.blueprint.core.api.WoodTypeRegistryHelper#registerWoodType(WoodType)} on this</b>!
	 * @param properties The {@link Block.Properties} to use for the sign blocks.
	 * @return A {@link Pair} containing {@link RegistryHolder}s of the {@link BlueprintCeilingHangingSignBlock} and the {@link BlueprintWallHangingSignBlock}.
	 */
	public Pair<RegistryHolder<Block, BlueprintCeilingHangingSignBlock>, RegistryHolder<Block, BlueprintWallHangingSignBlock>> createHangingSignBlock(String name, WoodType woodType, Block.Properties properties) {
		RegistryHolder<Block, BlueprintCeilingHangingSignBlock> ceiling = this.wrapper.register(name + "_hanging_sign", () -> new BlueprintCeilingHangingSignBlock(woodType, properties));
		RegistryHolder<Block, BlueprintWallHangingSignBlock> wall = this.wrapper.register(name + "_wall_hanging_sign", () -> new BlueprintWallHangingSignBlock(woodType, properties.dropsLike(ceiling.get())));
		this.itemWrapper.register(name + "_hanging_sign", () -> new HangingSignItem(ceiling.get(), wall.get(), new Item.Properties().stacksTo(16)));
		return Pair.of(ceiling, wall);
	}
}