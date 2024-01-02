package com.teamabnormals.blueprint.core.events.lifecycle;

import com.teamabnormals.blueprint.core.events.SimpleEvent;
import com.teamabnormals.blueprint.core.util.EventUtil;

/**
 * A class for events related to game ticks.
 *
 * @author ebo2022
 */
public interface TickEvents {
    /**
     * Fired at the start and end of a client tick.
     */
    SimpleEvent<TickEvent> CLIENT = EventUtil.createVoid(TickEvent.class);

    /**
     * Fired at the start and end of a server tick.
     */
    SimpleEvent<TickEvent> SERVER = EventUtil.createVoid(TickEvent.class);

    /**
     * Fired at the start and end of a level tick.
     */
    SimpleEvent<TickEvent> LEVEL = EventUtil.createVoid(TickEvent.class);


    /**
     * The functional interface representing listeners for {@link TickEvents}.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface TickEvent {
        /**
         * Called at the start or end of a tick.
         *
         * @param phase The current phase of the tick.
         */
        void tick(Phase phase);
    }

    /**
     * Represents whether a {@link TickEvent} is being fired at the start or end of the tick.
     */
    enum Phase {
        START, END;
    }
}