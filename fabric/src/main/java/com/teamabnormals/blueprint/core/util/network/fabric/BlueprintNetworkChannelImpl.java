package com.teamabnormals.blueprint.core.util.network.fabric;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.mixin.fabric.ServerLoginPacketListenerImplAccessor;
import com.teamabnormals.blueprint.core.mixin.fabric.client.ClientHandshakePacketListenerImplAccessor;
import com.teamabnormals.blueprint.core.mixin.fabric.client.ClientPacketListenerAccessor;
import com.teamabnormals.blueprint.core.util.PlatformUtil;
import com.teamabnormals.blueprint.core.util.network.BlueprintNetworkChannel;
import com.teamabnormals.blueprint.core.util.network.PacketContext;
import com.teamabnormals.blueprint.core.util.network.PacketDirection;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ApiStatus.Internal
public class BlueprintNetworkChannelImpl implements BlueprintNetworkChannel {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Short2ObjectMap<MessageEntry<?>> byIndex = new Short2ObjectArrayMap<>();
    private final Object2ObjectMap<Class<?>, MessageEntry<?>> byType = new Object2ObjectArrayMap<>();
    private final List<LoginPacketGenerator<?>> loginPacketGenerators = new ArrayList<>();
    private final ResourceLocation channelId;

    private BlueprintNetworkChannelImpl(ResourceLocation channelId) {
        this.channelId = channelId;
        ServerLoginConnectionEvents.QUERY_START.register(this::sendLoginPackets);
        ServerLoginNetworking.registerGlobalReceiver(this.channelId, this::handleServerLogin);
        ServerPlayNetworking.registerGlobalReceiver(this.channelId, this::handleServerPlay);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientLoginNetworking.registerGlobalReceiver(this.channelId, this::handleClientLogin);
            ClientPlayNetworking.registerGlobalReceiver(this.channelId, this::handleClientPlay);
        }
    }

    public static BlueprintNetworkChannel create(ResourceLocation channelId, Supplier<String> networkProtocolVersion, Predicate<String> clientAcceptedVersions, Predicate<String> serverAcceptedVersions) {
        return new BlueprintNetworkChannelImpl(channelId);
    }

    @Override
    public <MSG> void registerMessage(int index, Class<MSG> type, MessageEncoder<MSG> encoder, MessageDecoder<MSG> decoder, MessageConsumer<MSG> consumer, @Nullable PacketDirection<?> direction) {
        MessageEntry<MSG> entry = new MessageEntry<>(index, type, encoder, decoder, consumer, direction);
        this.byIndex.put((short) (index & 0xff), entry);
        this.byType.put(type, entry);
    }

    @Override
    public <MSG> MessageBuilder<MSG> messageBuilder(Class<MSG> type, int id, @Nullable PacketDirection<?> direction) {
        return new MessageBuilderImpl<>(this, type, id, direction);
    }

    @Override
    public <MSG> void sendToServer(MSG message) {
        ClientPlayNetworking.send(this.channelId, this.encode(message));
    }

    @Override
    public <MSG> void sendTo(ServerPlayer player, MSG message) {
        ServerPlayNetworking.send(player, this.channelId, this.encode(message));
    }

    @Override
    public <MSG> void sendTo(ServerLevel level, MSG message) {
        FriendlyByteBuf buf = this.encode(message);
        for (ServerPlayer player : PlayerLookup.world(level))
            ServerPlayNetworking.send(player, this.channelId, buf);
    }

    @Override
    public <MSG> void sendToNear(ServerLevel level, double x, double y, double z, double radius, MSG message) {
        FriendlyByteBuf buf = this.encode(message);
        for (ServerPlayer player : PlayerLookup.around(level, new Vec3(x, y, z), radius))
            ServerPlayNetworking.send(player, this.channelId, buf);
    }

    @Override
    public <MSG> void sendToAll(MinecraftServer server, MSG message) {
        FriendlyByteBuf buf = this.encode(message);
        for (ServerPlayer player : PlayerLookup.all(server))
            ServerPlayNetworking.send(player, this.channelId, buf);
    }

    @Override
    public <MSG> void sendToTracking(Entity entity, MSG message) {
        FriendlyByteBuf buf = this.encode(message);
        for (ServerPlayer player : PlayerLookup.tracking(entity))
            ServerPlayNetworking.send(player, this.channelId, buf);
    }

    @Override
    public <MSG> void sendToTracking(ServerLevel level, BlockPos pos, MSG message) {
        FriendlyByteBuf buf = this.encode(message);
        for (ServerPlayer player : PlayerLookup.tracking(level, pos))
            ServerPlayNetworking.send(player, this.channelId, buf);
    }

    @Override
    public <MSG> void sendToTracking(ServerLevel level, ChunkPos pos, MSG message) {
        FriendlyByteBuf buf = this.encode(message);
        for (ServerPlayer player : PlayerLookup.tracking(level, pos))
            ServerPlayNetworking.send(player, this.channelId, buf);
    }

    @Override
    public <MSG> void sendToTrackingAndSelf(Entity entity, MSG message) {
        FriendlyByteBuf buf = this.encode(message);
        if (entity instanceof ServerPlayer)
            ServerPlayNetworking.send((ServerPlayer) entity, this.channelId, buf);
        for (ServerPlayer player : PlayerLookup.tracking(entity))
            ServerPlayNetworking.send(player, this.channelId, buf);
    }

    private <MSG> FriendlyByteBuf encode(MSG message) {
        @SuppressWarnings("unchecked")
        MessageEntry<MSG> entry = (MessageEntry<MSG>) this.byType.get(message.getClass());
        if (entry == null) {
            LOGGER.error("Received invalid message {} on channel {}", message.getClass().getName(), this.channelId);
            throw new IllegalArgumentException("Invalid message " + message.getClass().getName());
        }
        FriendlyByteBuf buf = PacketByteBufs.create();
        if (entry.encoder != null) {
            buf.writeByte(entry.index & 0xff);
            entry.encoder.encode(message, buf);
        }
        return buf;
    }

    private <MSG> void decode(FriendlyByteBuf payload, PacketContext context) {
        if (payload == null || !payload.isReadable()) {
            LOGGER.error("Received empty payload on channel {}", this.channelId);
            return;
        }
        PacketDirection<?> direction = context.getDirection();
        short discriminator = payload.readUnsignedByte();
        @SuppressWarnings("unchecked")
        MessageEntry<MSG> entry = (MessageEntry<MSG>) this.byIndex.get(discriminator);
        if (entry == null) {
            LOGGER.error("Received invalid discriminator byte {} on channel {}", discriminator, this.channelId);
            return;
        }
        if (entry.packetDirection != null && entry.packetDirection != direction) {
            context.getNetworkManager().disconnect(Component.literal("Illegal packet received, terminating connection"));
            throw new IllegalStateException("Invalid packet received, aborting connection");
        }
        if (entry.decoder != null) {
            MSG message = entry.decoder.decode(payload);
            entry.messageConsumer.handle(message, context);
        }
    }

    private void sendLoginPackets(ServerLoginPacketListenerImpl handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
        this.loginPacketGenerators.stream()
                .flatMap(generator -> {
                    Connection connection = ((ServerLoginPacketListenerImplAccessor) handler).getConnection();
                    return generator.generate(connection.isMemoryConnection()).stream();
                })
                .forEach(loginPacket -> {
                    sender.sendPacket(sender.createPacket(this.channelId, this.encode(loginPacket)));
                });
    }

    private void handleServerLogin(MinecraftServer server, ServerLoginPacketListenerImpl handler, boolean understood, FriendlyByteBuf buf, ServerLoginNetworking.LoginSynchronizer synchronizer, PacketSender responseSender) {
        if (!understood) throw new IllegalStateException("Client couldn't understand server packet");
        this.decode(buf, new PacketContextImpl(PacketDirection.Login.SERVERBOUND, ((ServerLoginPacketListenerImplAccessor) handler).getConnection(), null) {
            @Override
            public <MSG> void reply(MSG message) {
                throw new UnsupportedOperationException("Server cannot reply to packets during login phase");
            }
        });
    }

    private CompletableFuture<FriendlyByteBuf> handleClientLogin(Minecraft client, ClientHandshakePacketListenerImpl handler, FriendlyByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
        CompletableFuture<FriendlyByteBuf> future = new CompletableFuture<>();
        this.decode(buf, new LoginPacketContextImpl(PacketDirection.Login.CLIENTBOUND, ((ClientHandshakePacketListenerImplAccessor) handler).getConnection(), message -> {
            try {
                future.complete(this.encode(message));
            } catch (Throwable t) {
                t.printStackTrace();
                future.completeExceptionally(t);
            }
        }));
        return future;
    }

    private void handleServerPlay(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        this.decode(buf, new PacketContextImpl(PacketDirection.Play.SERVERBOUND, ((ServerLoginPacketListenerImplAccessor) handler).getConnection(), message -> {
            responseSender.sendPacket(responseSender.createPacket(this.channelId, this.encode(message)));
        }));
    }

    private void handleClientPlay(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        this.decode(buf, new PacketContextImpl(PacketDirection.Play.CLIENTBOUND, ((ClientPacketListenerAccessor) handler).getConnection(), message -> {
            responseSender.sendPacket(responseSender.createPacket(this.channelId, this.encode(message)));
        }));
    }

    private static class MessageBuilderImpl<MSG> implements MessageBuilder<MSG> {
        private final BlueprintNetworkChannelImpl channel;
        private final Class<MSG> type;
        private final int id;
        @Nullable
        private final PacketDirection<?> packetDirection;
        private MessageEncoder<MSG> encoder;
        private MessageDecoder<MSG> decoder;
        private MessageConsumer<MSG> consumer;
        private LoginPacketGenerator<MSG> loginPacketGenerators;

        private MessageBuilderImpl(BlueprintNetworkChannelImpl channel, Class<MSG> type, int id, @Nullable PacketDirection<?> packetDirection) {
            this.channel = channel;
            this.type = type;
            this.id = id;
            this.packetDirection = packetDirection;
        }

        @Override
        public MessageBuilder<MSG> encoder(MessageEncoder<MSG> encoder) {
            this.encoder = encoder;
            return this;
        }

        @Override
        public MessageBuilder<MSG> decoder(MessageDecoder<MSG> decoder) {
            this.decoder = decoder;
            return this;
        }

        @Override
        public MessageBuilder<MSG> loginIndex(LoginIndexGetter<MSG> getter, LoginIndexSetter<MSG> setter) {
            // not relevant for Fabric
            return this;
        }

        @Override
        public MessageBuilder<MSG> buildLoginPacketList(LoginPacketGenerator<MSG> generator) {
            this.loginPacketGenerators = generator;
            return this;
        }

        @Override
        public MessageBuilder<MSG> markAsLoginPacket() {
            this.loginPacketGenerators = (isLocal) -> {
                try {
                    return Collections.singletonList(Pair.of(this.type.getName(), this.type.getConstructor().newInstance()));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException("Inaccessible no-arg constructor for message " + type.getName(), e);
                }
            };
            return this;        }

        @Override
        public MessageBuilder<MSG> consumerNetworkThread(MessageConsumer<MSG> consumer) {
            this.consumer = consumer;
            return this;
        }

        @Override
        public void add() {
            Objects.requireNonNull(this.consumer, () -> "Message of type " + this.type.getName() + " is missing a handler!");
            this.channel.registerMessage(this.id, this.type, this.encoder, this.decoder, this.consumer, this.packetDirection);
            if (this.loginPacketGenerators != null)
                this.channel.loginPacketGenerators.add(this.loginPacketGenerators);
        }
    }

    private static class PacketContextImpl implements PacketContext {

        private final PacketDirection<?> direction;
        private final Connection connection;
        private final Consumer<Object> replier;

        private PacketContextImpl(PacketDirection<?> direction, Connection connection, Consumer<Object> replier) {
            this.direction = direction;
            this.connection = connection;
            this.replier = replier;
        }

        @Override
        public CompletableFuture<Void> enqueueWork(Runnable work) {
            return PlatformUtil.getGameExecutor().submit(work);
        }

        @Override
        public PacketDirection<?> getDirection() {
            return this.direction;
        }

        @Override
        public Connection getNetworkManager() {
            return this.connection;
        }

        @Override
        public <MSG> void reply(MSG message) {
            this.replier.accept(message);
        }
    }

    private static class LoginPacketContextImpl extends PacketContextImpl {
        private boolean hasReplied;

        private LoginPacketContextImpl(PacketDirection<?> direction, Connection connection, Consumer<Object> replier) {
            super(direction, connection, replier);
        }

        @Override
        public <MSG> void reply(MSG message) {
            if (this.hasReplied) throw new IllegalStateException("Already replied to packet");
            super.reply(message);
            this.hasReplied = true;
        }
    }

    private record MessageEntry<MSG>(int index, Class<MSG> messageType, @Nullable MessageEncoder<MSG> encoder,
                                     @Nullable MessageDecoder<MSG> decoder, MessageConsumer<MSG> messageConsumer,
                                     @Nullable PacketDirection<?> packetDirection) {
    }
}
