package com.teamabnormals.blueprint.common.item;

import com.teamabnormals.blueprint.core.util.PlatformUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * A {@link BlockItem} extension that supports lazily loaded custom item stack renderers.
 * <p>The registration of the BEWLR on each platform is handled by Blueprint automatically.</p>
 *
 * @author SmellyModder (Luke Tonon)
 * @see BEWLRBlockItem.LazyBEWLR
 */
public class BEWLRBlockItem extends BlockItem {
	@Nullable
	private final Supplier<LazyBEWLR> bewlr;

	public BEWLRBlockItem(Block block, Properties properties, Supplier<Supplier<LazyBEWLR>> bewlr) {
		super(block, properties);
		LazyBEWLR lazyBEWLR = PlatformUtil.getSide().isClient() ? bewlr.get().get() : null;
		this.bewlr = lazyBEWLR == null ? null : () -> lazyBEWLR;
	}

	@Environment(EnvType.CLIENT)
	public Supplier<LazyBEWLR> getBewlr() {
		return this.bewlr;
	}

	/**
	 * A caching class for custom {@link BlockEntityWithoutLevelRenderer}s used on items.
	 * <p>Without caching, a new {@link BlockEntityRenderDispatcher} instance would have to get created every time the item is rendered.</p>
	 *
	 * @author SmellyModder (Luke Tonon)
	 */
	public static final class LazyBEWLR {
		private final BiFunction<BlockEntityRenderDispatcher, EntityModelSet, BlockEntityWithoutLevelRenderer> cacheFunction;
		public BlockEntityWithoutLevelRenderer value;

		public LazyBEWLR(BiFunction<BlockEntityRenderDispatcher, EntityModelSet, BlockEntityWithoutLevelRenderer> cacheFunction) {
			this.cacheFunction = cacheFunction;
		}

		public BlockEntityWithoutLevelRenderer cache(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
			return this.value = this.cacheFunction.apply(dispatcher, modelSet);
		}
	}
}