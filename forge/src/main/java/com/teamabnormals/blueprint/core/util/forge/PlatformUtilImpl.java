package com.teamabnormals.blueprint.core.util.forge;

import com.teamabnormals.blueprint.core.util.PlatformUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.Internal
public class PlatformUtilImpl {

    public static PlatformUtil.Side getSide() {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> PlatformUtil.Side.CLIENT;
            case DEDICATED_SERVER -> PlatformUtil.Side.SERVER;
        };
    }

    public static Optional<MinecraftServer> getCurrentServer() {
        return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer());
    }

    public static BlockableEventLoop<?> getGameExecutor() {
        return LogicalSidedProvider.WORKQUEUE.get(EffectiveSide.get());
    }
}
