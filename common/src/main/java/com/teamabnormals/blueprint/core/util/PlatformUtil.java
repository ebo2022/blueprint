package com.teamabnormals.blueprint.core.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility methods for accessing platform-specific APIs.
 *
 * @author ebo2022
 */
public class PlatformUtil {

    /**
     * @return The current side of the game.
     */
    @ExpectPlatform
    public static Side getSide() {
        throw new AssertionError();
    }

    /**
     * Runs the given side-specific code if the game is running on it.
     *
     * @param side  The side to run the code for.
     * @param toRun A {@link Runnable} for the work to run.
     */
    public static void unsafeRunWhenOn(Side side, Supplier<Runnable> toRun) {
        if (side == getSide()) {
            toRun.get().run();
        }
    }

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

    /**
     * Represents a side of the game.
     */
    public enum Side {
        CLIENT, SERVER;

        /**
         * @return Whether the side is a client.
         */
        public boolean isClient() {
            return this == CLIENT;
        }

        /**
         * @return Whether the side is a server.
         */
        public boolean isServer() {
            return this == SERVER;
        }
    }
}
