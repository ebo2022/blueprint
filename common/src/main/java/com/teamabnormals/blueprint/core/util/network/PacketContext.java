package com.teamabnormals.blueprint.core.util.network;

import com.teamabnormals.blueprint.core.util.PlatformUtil;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Context providing information for when a packet is received on the client or server.
 *
 * @author ebo2022
 */
public interface PacketContext {

	/**
	 * Defers work to be run on the game thread instead of the network thread.
	 * <p>This should be used for any work that modifies the game state.</p>
	 *
	 * @param work The work to run.
	 * @return A {@link CompletableFuture} for when the work is done.
	 */
	CompletableFuture<Void> enqueueWork(Runnable work);

	/**
	 * @return The direction the packet is going in.
	 */
	PacketDirection<?> getDirection();

	/**
	 * @return The client-server connection the packet was received on.
	 */
	Connection getNetworkManager();

	/**
	 * Sends out a packet in response to the packet being received.
	 * <p>This is required by login packets, but optional for any others.</p>
	 *
	 * @param message
	 * @param <MSG>
	 */
	<MSG> void reply(MSG message);

	/**
	 * @return The server player the packet was sent to if being received on the server, or <code>null</code> if the packet was received on the client.
	 */
	@Nullable
	default ServerPlayer getSender() {
		PacketListener listener = this.getNetworkManager().getPacketListener();
		if (listener instanceof ServerGamePacketListenerImpl serverPacketListener)
			return serverPacketListener.player;
		return null;
	}

	/**
	 * Disconnects the current client-server connection, and provides information on the disconnection to the client if on a server.
	 *
	 * @param message The reason for disconnection.
	 */
	default void disconnect(Component message) {
		if (this.getDirection().isServerbound()) {
			this.getNetworkManager().send(new ClientboundDisconnectPacket(message), PacketSendListener.thenRun(() -> this.getNetworkManager().disconnect(message)));
			this.getNetworkManager().setReadOnly();
			PlatformUtil.getCurrentServer().ifPresent(server -> server.executeBlocking(this.getNetworkManager()::handleDisconnection));
		} else {
			this.getNetworkManager().disconnect(message);
		}
	}
}