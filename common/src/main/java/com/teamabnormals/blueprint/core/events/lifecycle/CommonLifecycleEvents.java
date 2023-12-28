package com.teamabnormals.blueprint.core.events.lifecycle;

import com.teamabnormals.blueprint.core.events.SimpleEvent;
import com.teamabnormals.blueprint.core.util.EventUtil;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;

/**
 * Miscellaneous lifecycle events relating to modloading and data reloads.
 *
 * @author ebo2022
 */
public interface CommonLifecycleEvents {

    /**
     * Fired when modloading is fully complete.
     * <p>On Fabric, this fires after all mod initializers have loaded.</p>
     * <p>On Forge, this is analogous to <code>FMLLoadCompleteEvent</code>.</p>
     */
    SimpleEvent<LoadComplete> LOAD_COMPLETE = EventUtil.createVoid(LoadComplete.class);

    @FunctionalInterface
    interface LoadComplete {
        void onLoadComplete(IParallelDispatcher dispatcher);
    }
}
