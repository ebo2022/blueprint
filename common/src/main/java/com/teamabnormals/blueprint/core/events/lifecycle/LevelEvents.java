package com.teamabnormals.blueprint.core.events.lifecycle;

import com.teamabnormals.blueprint.core.events.SimpleEvent;
import com.teamabnormals.blueprint.core.util.EventUtil;
import net.minecraft.world.level.LevelAccessor;

public interface LevelEvents {
    SimpleEvent<Load> LOAD = EventUtil.createVoid(Load.class);
    SimpleEvent<Unload> UNLOAD = EventUtil.createVoid(Unload.class);

    /**
     * The functional interface representing listeners for the {@link #LOAD} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface Load {
        /**
         * Called when a level is in the process of loading.
         *
         * @param level The level being loaded.
         */
        void event(LevelAccessor level);
    }

    /**
     * The functional interface representing listeners for the {@link #UNLOAD} event.
     *
     * @author ebo2022
     */
    @FunctionalInterface
    interface Unload {
        /**
         * Called when a level is in the process of unloading.
         *
         * @param level The level being unloaded.
         */
        void event(LevelAccessor level);
    }
}