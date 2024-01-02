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
    SimpleEvent<AboutToStart> ABOUT_TO_START = EventUtil.createVoid(AboutToStart.class);
    SimpleEvent<Starting> STARTING = EventUtil.createVoid(Starting.class);
    SimpleEvent<Started> STARTED = EventUtil.createVoid(Started.class);
    SimpleEvent<Stopping> STOPPING = EventUtil.createVoid(Stopping.class);
    SimpleEvent<Stopped> STOPPED = EventUtil.createVoid(Stopped.class);

    /**
     * The functional interface for representing listeners of the {@link #ABOUT_TO_START} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface AboutToStart {
        /**
         * Called just the server starts.
         *
         * @param server The current server.
         */
        void event(MinecraftServer server);
    }

    /**
     * The functional interface for representing listeners of the {@link #STARTING} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface Starting {
        /**
         * Called while the server is starting.
         *
         * @param server The current server.
         */
        void event(MinecraftServer server);
    }

    /**
     * The functional interface for representing listeners of the {@link #STARTED} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface Started {
        /**
         * Called once the server has fully started.
         *
         * @param server The current server.
         */
        void event(MinecraftServer server);
    }

    /**
     * The functional interface for representing listeners of the {@link #STOPPING} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface Stopping {
        /**
         * Called when the server is going to stop.
         *
         * @param server The current server.
         */
        void event(MinecraftServer server);
    }

    /**
     * The functional interface for representing listeners of the {@link #STOPPED} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface Stopped {
        /**
         * Called once the server has stopped.
         *
         * @param server The current server.
         */
        void event(MinecraftServer server);
    }
}