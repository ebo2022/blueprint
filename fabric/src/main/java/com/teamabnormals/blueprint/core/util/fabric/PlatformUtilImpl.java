package com.teamabnormals.blueprint.core.util.fabric;

import com.teamabnormals.blueprint.fabric.BlueprintFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.Supplier;

@ApiStatus.Internal
public class PlatformUtilImpl {
    private static final Supplier<Supplier<BlockableEventLoop<?>>> CLIENT_GAME_EXECUTOR = () -> Minecraft::getInstance;

    public static Optional<MinecraftServer> getCurrentServer() {
        return Optional.ofNullable(BlueprintFabric.getServer());
    }

    public static BlockableEventLoop<?> getGameExecutor() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? CLIENT_GAME_EXECUTOR.get().get() : getCurrentServer().orElseThrow(() -> new IllegalStateException("Expected running server, none present"));
    }
}
