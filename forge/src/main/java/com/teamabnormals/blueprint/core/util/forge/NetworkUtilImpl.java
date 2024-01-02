package com.teamabnormals.blueprint.core.util.forge;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class NetworkUtilImpl {

    public static Packet<ClientGamePacketListener> getEntitySpawningPacket(Entity entity) {
        return NetworkHooks.getEntitySpawningPacket(entity);
    }
}
