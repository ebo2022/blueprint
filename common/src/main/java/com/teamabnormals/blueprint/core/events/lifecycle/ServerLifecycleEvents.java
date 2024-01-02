package com.teamabnormals.blueprint.core.events.lifecycle;

import com.teamabnormals.blueprint.core.events.SimpleEvent;
import com.teamabnormals.blueprint.core.util.EventUtil;
import net.minecraft.server.MinecraftServer;

/**
 * Events relating to lifecycles of dedicated and integrated servers.
 *
 * @author ebo2022
 */
public interface ServerLifecycleEvents {

    /**
     * Fired when the server is about to start.
     */
    SimpleEvent<ServerEvent> ABOUT_TO_START = EventUtil.createVoid(ServerEvent.class);

    /**
     * Fired when the server is currently starting, after loading the level and info.
     */
    SimpleEvent<ServerEvent> STARTING = EventUtil.createVoid(ServerEvent.class);

    /**
     * Fired after the server has fully started.
     */
    SimpleEvent<ServerEvent> STARTED = EventUtil.createVoid(ServerEvent.class);

    /**
     * Fired when the server starts to stop.
     */
    SimpleEvent<ServerEvent> STOPPING = EventUtil.createVoid(ServerEvent.class);

    /**
     * Fired when the server has fully stopped.
     */
    SimpleEvent<ServerEvent> STOPPED = EventUtil.createVoid(ServerEvent.class);

    /**
     * The functional interface for representing listeners of server-related events.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface ServerEvent {
        /**
         * Called when the event listener is fired during the server lifecycle.
         *
         * @param server The current server.
         */
        void event(MinecraftServer server);
    }
}