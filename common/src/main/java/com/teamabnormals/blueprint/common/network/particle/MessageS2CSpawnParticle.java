package com.teamabnormals.blueprint.common.network.particle;

import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.core.util.network.PacketContext;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Message for telling the client to spawn a particle.
 *
 * @author SmellyModder(Luke Tonon)
 */
public final class MessageS2CSpawnParticle {
	public String particleName;
	public double posX, posY, posZ;
	public double motionX, motionY, motionZ;

	public MessageS2CSpawnParticle(String particleName, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		this.particleName = particleName;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(this.particleName);
		buf.writeDouble(this.posX);
		buf.writeDouble(this.posY);
		buf.writeDouble(this.posZ);
		buf.writeDouble(this.motionX);
		buf.writeDouble(this.motionY);
		buf.writeDouble(this.motionZ);
	}

	public static MessageS2CSpawnParticle decode(FriendlyByteBuf buf) {
		String particleName = buf.readUtf();
		double posX = buf.readDouble();
		double posY = buf.readDouble();
		double posZ = buf.readDouble();
		double motionX = buf.readDouble();
		double motionY = buf.readDouble();
		double motionZ = buf.readDouble();
		return new MessageS2CSpawnParticle(particleName, posX, posY, posZ, motionX, motionY, motionZ);
	}

	public static void handle(MessageS2CSpawnParticle message, PacketContext context) {
		if (context.getDirection().isClientbound()) {
			context.enqueueWork(() -> {
				Level level = ClientInfo.getClientPlayerLevel();
				SimpleParticleType particleType = (SimpleParticleType) BuiltInRegistries.PARTICLE_TYPE.get(new ResourceLocation(message.particleName));

				if (particleType != null) {
					level.addParticle(particleType, message.posX, message.posY, message.posZ, message.motionX, message.motionY, message.motionZ);
				}
			});
		}
	}
}