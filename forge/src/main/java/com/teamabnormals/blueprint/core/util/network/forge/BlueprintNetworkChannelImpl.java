package com.teamabnormals.blueprint.core.util.network.forge;

import com.teamabnormals.blueprint.core.util.network.BlueprintNetworkChannel;
import com.teamabnormals.blueprint.core.util.network.PacketContext;
import com.teamabnormals.blueprint.core.util.network.PacketDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.simple.MessageFunctions;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ApiStatus.Internal
public class BlueprintNetworkChannelImpl implements BlueprintNetworkChannel {

    private final SimpleChannel channel;

    private BlueprintNetworkChannelImpl(SimpleChannel channel) {
        this.channel = channel;
    }

    public static BlueprintNetworkChannel create(ResourceLocation name, Supplier<String> networkProtocolVersion, Predicate<String> clientAcceptedVersions, Predicate<String> serverAcceptedVersions) {
        return new BlueprintNetworkChannelImpl(NetworkRegistry.newSimpleChannel(name, networkProtocolVersion, clientAcceptedVersions, serverAcceptedVersions));
    }

    @Override
    public <MSG> void registerMessage(int index, Class<MSG> type, MessageEncoder<MSG> encoder, MessageDecoder<MSG> decoder, MessageConsumer<MSG> consumer, @Nullable PacketDirection<?> direction) {
        this.channel.registerMessage(index, type, encoder::encode, decoder::decode, (msg, context) -> {
            consumer.handle(msg, new PacketContextImpl(this.channel, context));
            context.setPacketHandled(true);
        }, Optional.ofNullable(getForgeDirection(direction)));
    }

    @Override
    public <MSG> MessageBuilder<MSG> messageBuilder(Class<MSG> type, int id, @Nullable PacketDirection<?> direction) {
        return new MessageBuilderImpl<>(this.channel, this.channel.messageBuilder(type, id, getForgeDirection(direction)));
    }

    @Override
    public <MSG> void sendToServer(MSG message) {
        this.channel.sendToServer(message);
    }

    @Override
    public <MSG> void sendTo(ServerPlayer player, MSG message) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    @Override
    public <MSG> void sendTo(ServerLevel level, MSG message) {
        this.channel.send(PacketDistributor.DIMENSION.with(level::dimension), message);
    }

    @Override
    public <MSG> void sendToNear(ServerLevel level, double x, double y, double z, double radius, MSG message) {
        this.channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, radius * radius, level.dimension())), message);
    }

    @Override
    public <MSG> void sendToAll(MinecraftServer server, MSG message) {
        this.channel.send(PacketDistributor.ALL.noArg(), message);
    }

    @Override
    public <MSG> void sendToTracking(Entity entity, MSG message) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    @Override
    public <MSG> void sendToTracking(ServerLevel level, BlockPos pos, MSG message) {
        this.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), message);
    }

    @Override
    public <MSG> void sendToTracking(ServerLevel level, ChunkPos pos, MSG message) {
        this.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunk(pos.x, pos.z)), message);
    }

    @Override
    public <MSG> void sendToTrackingAndSelf(Entity entity, MSG message) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    @Nullable
    private static INetworkDirection<?> getForgeDirection(PacketDirection<?> direction) {
        if (direction == null) return null;
        if (direction instanceof PacketDirection.Play playDirection) {
            return switch (playDirection) {
                case CLIENTBOUND -> PlayNetworkDirection.PLAY_TO_CLIENT;
                case SERVERBOUND -> PlayNetworkDirection.PLAY_TO_SERVER;
            };
        } else if (direction instanceof PacketDirection.Login loginDirection) {
            return switch (loginDirection) {
                case CLIENTBOUND -> LoginNetworkDirection.LOGIN_TO_CLIENT;
                case SERVERBOUND -> LoginNetworkDirection.LOGIN_TO_SERVER;
            };
        } else {
            throw new IllegalStateException("Unknown network direction");
        }
    }

    private record MessageBuilderImpl<MSG>(SimpleChannel channel, SimpleChannel.MessageBuilder<MSG> builder) implements MessageBuilder<MSG> {

        @Override
        public MessageBuilder<MSG> encoder(MessageEncoder<MSG> encoder) {
            this.builder.encoder(encoder::encode);
            return this;
        }

        @Override
        public MessageBuilder<MSG> decoder(MessageDecoder<MSG> decoder) {
            this.builder.decoder(decoder::decode);
            return this;
        }

        @Override
        public MessageBuilder<MSG> loginIndex(LoginIndexGetter<MSG> getter, LoginIndexSetter<MSG> setter) {
            this.builder.loginIndex(getter::getLoginIndex, setter::setLoginIndex);
            return this;
        }

        @Override
        public MessageBuilder<MSG> buildLoginPacketList(LoginPacketGenerator<MSG> generator) {
            this.builder.buildLoginPacketList(local -> generator.generate(local).stream().map(pair -> new MessageFunctions.LoginPacket<>(pair.getFirst(), pair.getSecond())).toList());
            return this;
        }

        @Override
        public MessageBuilder<MSG> markAsLoginPacket() {
            this.builder.markAsLoginPacket();
            return this;
        }

        @Override
        public MessageBuilder<MSG> consumerNetworkThread(MessageConsumer<MSG> consumer) {
            this.builder.consumerNetworkThread((msg, context) -> consumer.handle(msg, new PacketContextImpl(this.channel, context)));
            return this;
        }

        @Override
        public void add() {
            this.builder.add();
        }
    }

    private record PacketContextImpl(SimpleChannel channel, NetworkEvent.Context context) implements PacketContext {

        @Override
        public CompletableFuture<Void> enqueueWork(Runnable work) {
            return this.context.enqueueWork(work);
        }

        @Override
        public PacketDirection<?> getDirection() {
            INetworkDirection<?> direction = this.context.getDirection();
            if (direction instanceof PlayNetworkDirection playDirection) {
                return switch (playDirection) {
                    case PLAY_TO_CLIENT -> PacketDirection.Play.CLIENTBOUND;
                    case PLAY_TO_SERVER -> PacketDirection.Play.SERVERBOUND;
                };
            } else if (direction instanceof LoginNetworkDirection loginDirection) {
                return switch (loginDirection) {
                    case LOGIN_TO_CLIENT -> PacketDirection.Login.CLIENTBOUND;
                    case LOGIN_TO_SERVER -> PacketDirection.Login.SERVERBOUND;
                };
            } else {
                throw new IllegalStateException("Unknown network direction");
            }
        }

        @Override
        public Connection getNetworkManager() {
            return this.context.getNetworkManager();
        }

        @Override
        public <MSG> void reply(MSG message) {
            this.channel.reply(message, this.context);
        }
    }
}
