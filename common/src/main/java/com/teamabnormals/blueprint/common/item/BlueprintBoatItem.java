package com.teamabnormals.blueprint.common.item;

import com.teamabnormals.blueprint.common.entity.BlueprintBoat;
import com.teamabnormals.blueprint.common.entity.BlueprintChestBoat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

/**
 * An {@link Item} extension used for Blueprint's boats.
 * <p>This {@link Item} will also fill itself after the latest vanilla boat item.</p>
 *
 * @author SmellyModder (Luke Tonon)
 */
public class BlueprintBoatItem extends Item {
	private static final Predicate<Entity> COLLISION_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
	private final boolean hasChest;
	private final ResourceLocation type;

	public BlueprintBoatItem(boolean hasChest, ResourceLocation type, Item.Properties properties) {
		super(properties);
		this.hasChest = hasChest;
		this.type = type;
		DispenserBlock.registerBehavior(this, new DispenserBoatBehavior(hasChest, type));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult hitResult = getPlayerPOVHitResult(level, playerIn, ClipContext.Fluid.ANY);
		if (hitResult.getType() == HitResult.Type.MISS) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else {
			Vec3 vec3d = playerIn.getViewVector(1.0F);
			List<Entity> list = level.getEntities(playerIn, playerIn.getBoundingBox().expandTowards(vec3d.scale(5.0D)).inflate(1.0D), COLLISION_PREDICATE);
			if (!list.isEmpty()) {
				Vec3 vec3d1 = playerIn.getEyePosition();

				for (Entity entity : list) {
					AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(vec3d1)) {
						return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
					}
				}
			}

			if (hitResult.getType() == HitResult.Type.BLOCK) {
				Boat boat = this.hasChest ? new BlueprintChestBoat(level, this.type, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z) : new BlueprintBoat(level, this.type, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
				boat.setYRot(playerIn.getYRot());
				if (!level.noCollision(boat, boat.getBoundingBox().inflate(-0.1D))) {
					return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
				} else {
					if (!level.isClientSide) {
						level.addFreshEntity(boat);
					}

					if (!playerIn.getAbilities().instabuild) {
						itemstack.shrink(1);
					}

					playerIn.awardStat(Stats.ITEM_USED.get(this));
					return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
				}
			} else {
				return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
			}
		}
	}

	static class DispenserBoatBehavior extends DefaultDispenseItemBehavior {
		private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
		private final boolean hasChest;
		private final ResourceLocation type;

		public DispenserBoatBehavior(boolean hasChest, ResourceLocation type) {
			this.hasChest = hasChest;
			this.type = type;
		}

		@Override
		public ItemStack execute(BlockSource source, ItemStack stack) {
			Direction direction = source.state().getValue(DispenserBlock.FACING);
			Level level = source.level();
			double x = source.pos().getX() + (double) ((float) direction.getStepX() * 1.125f);
			double y = source.pos().getY() + (double) ((float) direction.getStepY() * 1.125f);
			double z = source.pos().getZ() + (double) ((float) direction.getStepZ() * 1.125f);
			BlockPos pos = source.pos().relative(direction);
			double adjustY;
			if (level.getFluidState(pos).is(FluidTags.WATER)) {
				adjustY = 1d;
			} else {
				if (!level.getBlockState(pos).isAir() || !level.getFluidState(pos.below()).is(FluidTags.WATER)) {
					return this.defaultDispenseItemBehavior.dispense(source, stack);
				}
				adjustY = 0d;
			}
			Boat boat = this.hasChest ? new BlueprintChestBoat(level, this.type, x, y + adjustY, z) : new BlueprintBoat(level, this.type, x, y + adjustY, z);
			boat.setYRot(direction.toYRot());
			level.addFreshEntity(boat);
			stack.shrink(1);
			return stack;
		}

		@Override
		protected void playSound(BlockSource source) {
			source.level().levelEvent(1000, source.pos(), 0);
		}
	}
}