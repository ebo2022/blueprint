package com.teamabnormals.blueprint.core.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;

import java.util.Optional;

/**
 * Utility methods for accessing platform-specific APIs.
 *
 * @author ebo2022
 */
public class PlatformUtil {


    /**
     * @return An optional of the currently running Minecraft server, or {@link Optional#empty()} if none is present.
     */
    @ExpectPlatform
    public static Optional<MinecraftServer> getCurrentServer() {
        throw new AssertionError();
    }

    /***
     * @return The currently running game executor; this is either the minecraft client or server instance.
     */
    @ExpectPlatform
    public static BlockableEventLoop<?> getGameExecutor() {
        throw new AssertionError();
    }
}
