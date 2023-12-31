package com.teamabnormals.blueprint.core.util.network;

import com.mojang.datafixers.util.Pair;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * An abstracted network channel that handles inbound and outgoing packets, based on Forge's <code>SimpleChannel</code>.
 *
 * @author ebo2022
 */
public interface BlueprintNetworkChannel {
    /**
     * Initializes a new {@link BlueprintNetworkChannel} using the given name and protocol version.
     *
     * @param channelId              The name to register the channel under.
     * @param networkProtocolVersion A supplier of the protocol version string, used on Forge to validate packet versions.
     * @param clientAcceptedVersions A predicate to check if the Forge protocol version received from the server is valid.
     * @param serverAcceptedVersions A predicate to check if the Forge protocol version received from the client is valid.
     * @return A new {@link BlueprintNetworkChannel}.
     */
    @ExpectPlatform
    static BlueprintNetworkChannel create(ResourceLocation channelId, Supplier<String> networkProtocolVersion, Predicate<String> clientAcceptedVersions, Predicate<String> serverAcceptedVersions) {
        throw new AssertionError();
    }

    /**
     * Registers a message handler to the channel.
     *
     * @param index     The int value to use when indexing messages handled by this channel.
     * @param type      The message type.
     * @param encoder   The encoder to write the message data to a {@link FriendlyByteBuf}.
     * @param decoder   The decoder to read the message from a {@link FriendlyByteBuf}.
     * @param consumer  A handler called after the message is decoded.
     * @param direction The direction the message can go in, or leave <code>null</code> to allow any direction.
     */
    <MSG> void registerMessage(int index, Class<MSG> type, MessageEncoder<MSG> encoder, MessageDecoder<MSG> decoder, MessageConsumer<MSG> consumer, @Nullable PacketDirection<?> direction);

    /**
     * Registers a bidirectional message handler to the channel.
     *
     * @param index     The int value to use when indexing messages handled by this channel.
     * @param type      The message type.
     * @param encoder   The encoder to write the message data to a {@link FriendlyByteBuf}.
     * @param decoder   The decoder to read the message from a {@link FriendlyByteBuf}.
     * @param consumer  A handler called after the message is decoded.
     */
    default <MSG> void registerMessage(int index, Class<MSG> type, MessageEncoder<MSG> encoder, MessageDecoder<MSG> decoder, MessageConsumer<MSG> consumer) {
        this.registerMessage(index, type, encoder, decoder, consumer, null);
    }

    /**
     * Creates a new {@link MessageBuilder} to create a customized message.
     *
     * @param type      The message type.
     * @param id        The int value to use when indexing messages handled by this channel.
     * @param direction The direction the message can go in, or leave <code>null</code> to allow any direction.
     * @return A new {@link MessageBuilder}.
     */
    <MSG> MessageBuilder<MSG> messageBuilder(Class<MSG> type, int id, @Nullable PacketDirection<?> direction);

    /**
     * Creates a new bidirectional {@link MessageBuilder} to create a customized message.
     *
     * @param type      The message type.
     * @param id        The int value to use when indexing messages handled by this channel.
     * @return A new bidirectional {@link MessageBuilder}.
     */
    default <MSG> MessageBuilder<MSG> messageBuilder(Class<MSG> type, int id) {
        return this.messageBuilder(type, id, null);
    }

    /**
     * Sends a message from the client to the server.
     *
     * @param message The message to send.
     */
    <MSG> void sendToServer(MSG message);

    /**
     * Sends a message from the server to the given player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    <MSG> void sendTo(ServerPlayer player, MSG message);

    /**
     * Sends a message from the server to all players in the given dimension.
     *
     * @param level   The level to send the message in.
     * @param message The message to send.
     */
    <MSG> void sendTo(ServerLevel level, MSG message);

    /**
     * Sends a message from the server to all players in a dimension within a given radius of the provided position.
     *
     * @param level   The level to send the message in.
     * @param x       The x-coordinate of the origin position of the message.
     * @param y       The y-coordinate of the origin position of the message.
     * @param z       The z-coordinate of the origin position of the message.
     * @param radius  How far away from the position the message can be sent.
     * @param message The message to send.
     */
    <MSG> void sendToNear(ServerLevel level, double x, double y, double z, double radius, MSG message);

    /**
     * Sends a message to all players on a server.
     *
     * @param server  The server to send the message from.
     * @param message The message to send.
     */
    <MSG> void sendToAll(MinecraftServer server, MSG message);

    /**
     * Sends a message to all players tracking the given entity.
     *
     * @param entity  The entity being tracked.
     * @param message The message to send.
     */
    <MSG> void sendToTracking(Entity entity, MSG message);

    /**
     * Sends a message to all players tracking the given entity, and the entity itself.
     *
     * @param entity  The entity being tracked.
     * @param message The message to send.
     */
    <MSG> void sendToTrackingAndSelf(Entity entity, MSG message);

    /**
     * Sends a message to all players tracking the given {@link BlockPos}.
     *
     * @param level   The level to send the message in.
     * @param pos     The {@link BlockPos} being tracked.
     * @param message The message to send.
     */
    <MSG> void sendToTracking(ServerLevel level, BlockPos pos, MSG message);

    /**
     * Sends a message to all players tracking the given {@link LevelChunk}.
     *
     * @param chunk   The {@link LevelChunk} being tracked.
     * @param message The message to send.
     */
    default <MSG> void sendToTracking(LevelChunk chunk, MSG message) {
        this.sendToTracking((ServerLevel) chunk.getLevel(), chunk.getPos(), message);
    }

    /**
     * Sends a message to all players tracking the given {@link ChunkPos}.
     *
     * @param level   The level to send the message in.
     * @param pos     The {@link ChunkPos} being tracked.
     * @param message The message to send.
     */
    <MSG> void sendToTracking(ServerLevel level, ChunkPos pos, MSG message);

    /**
     * A builder to create a more customized message handler.
     *
     * @param <MSG> The message type.
     */
    interface MessageBuilder<MSG> {

        /**
         * Sets the given encoder to write the message data to a {@link FriendlyByteBuf}.
         *
         * @param encoder The encoder to use.
         * @return This builder.
         */
        MessageBuilder<MSG> encoder(MessageEncoder<MSG> encoder);

        /**
         * Sets the given decoder to turn the incoming {@link FriendlyByteBuf} into a readable message.
         *
         * @param decoder The decoder to use.
         * @return This builder.
         */
        MessageBuilder<MSG> decoder(MessageDecoder<MSG> decoder);

        /**
         * Sets the login index handlers to use for a login message.
         * <p>Login messages are required to implement {@link java.util.function.IntSupplier} to use as the getter function.</p>
         *
         * @param getter The getter function to get the current login index.
         * @param setter The setter function to set the current login index.
         * @return This builder.
         */
        MessageBuilder<MSG> loginIndex(LoginIndexGetter<MSG> getter, LoginIndexSetter<MSG> setter);

        /**
         * Sets the generator of the list of login packets to send when a player joins a server.
         * <p>{@link #loginIndex(LoginIndexGetter, LoginIndexSetter)} should also be used with login messages.</p>
         *
         * @param generator The login packet generator to use.
         * @return This builder.
         */
        MessageBuilder<MSG> buildLoginPacketList(LoginPacketGenerator<MSG> generator);

        /**
         * Marks the message to be sent as a login message when a player joins a server.
         * <p>Only use this method if the message class constructor has no arguments.</p>
         * <p>{@link #loginIndex(LoginIndexGetter, LoginIndexSetter)} should also be used with login messages.</p>
         *
         * @return This builder.
         */
        MessageBuilder<MSG> markAsLoginPacket();

        /**
         * Sets the consumer to run on the network thread after the message has been decoded.
         * <p>{@link PacketContext#enqueueWork(Runnable)} can be used to run work that modifies game state on the main thread.</p>
         *
         * @param consumer The consumer to use.
         * @return This builder.
         */
        MessageBuilder<MSG> consumerNetworkThread(MessageConsumer<MSG> consumer);

        /**
         * Sets the consumer to run on the main client or server thread after the message has been decoded.
         * <p>It is safe to run work that modifies the game state in the consumer if this method is used.</p>
         *
         * @param consumer The consumer to use.
         * @return This builder.
         */
        default MessageBuilder<MSG> consumerMainThread(MessageConsumer<MSG> consumer) {
            return this.consumerNetworkThread((message, context) -> context.enqueueWork(() -> consumer.handle(message, context)));
        }

        /**
         * Registers the message handler to the channel.
         */
        void add();
    }

    /**
     * A functional interface for writing messages onto a {@link FriendlyByteBuf}.
     *
     * @param <MSG> The message type.
     */
    @FunctionalInterface
    interface MessageEncoder<MSG> {
        void encode(MSG message, FriendlyByteBuf buf);
    }

    /**
     * A functional interface for reading messages from a {@link FriendlyByteBuf}.
     *
     * @param <MSG> The message type.
     */
    @FunctionalInterface
    interface MessageDecoder<MSG> {
        MSG decode(FriendlyByteBuf buf);
    }

    /**
     * A functional interface for handling messages after they have been decoded.
     *
     * @param <MSG> The message type.
     */
    @FunctionalInterface
    interface MessageConsumer<MSG> {
        void handle(MSG message, PacketContext context);
    }

    /**
     * A functional interface for generating a list of login packets to send when a player joins a server.
     *
     * @param <MSG> The message type.
     */
    @FunctionalInterface
    interface LoginPacketGenerator<MSG> {
        List<Pair<String, MSG>> generate(boolean isLocal);
    }

    /**
     * A functional interface for retrieving the index of a login message.
     *
     * @param <MSG> The message type.
     */
    @FunctionalInterface
    interface LoginIndexGetter<MSG> {
        int getLoginIndex(MSG msg);
    }

    /**
     * A functional interface for setting the index of a login message.
     *
     * @param <MSG> The message type.
     */
    @FunctionalInterface
    interface LoginIndexSetter<MSG> {
        void setLoginIndex(MSG msg, int index);
    }
}
