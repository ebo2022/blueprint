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
     * Fired at the same time as {@link #COMMON_SETUP} in server environments only.
     * <p>On Fabric, this fires after all mod initializers have loaded.</p>
     * <p>On Forge, this is equivalent to <code>FMLClientSetupEvent</code>.</p>
     */
    SimpleEvent<ParallelDispatchEvent> CLIENT_SETUP = EventUtil.createVoid(ParallelDispatchEvent.class);

    /**
     * Fired after registries have been initialized.
     * <p>On Fabric, this fires after all mod initializers have loaded.</p>
     * <p>On Forge, this is equivalent to <code>FMLCommonSetupEvent</code>.</p>
     */
    SimpleEvent<ParallelDispatchEvent> COMMON_SETUP = EventUtil.createVoid(ParallelDispatchEvent.class);

    /**
     * Fired at the same time as {@link #COMMON_SETUP} in server environments only.
     * <p>On Fabric, this fires after all mod initializers have loaded.</p>
     * <p>On Forge, this is equivalent to <code>FMLDedicatedServerSetupEvent</code>.</p>
     */
    SimpleEvent<ParallelDispatchEvent> DEDICATED_SERVER_SETUP = EventUtil.createVoid(ParallelDispatchEvent.class);

    /**
     * Fired when modloading is fully complete.
     * <p>On Fabric, this fires after all mod initializers have loaded.</p>
     * <p>On Forge, this is equivalent to <code>FMLLoadCompleteEvent</code>.</p>
     */
    SimpleEvent<ParallelDispatchEvent> LOAD_COMPLETE = EventUtil.createVoid(ParallelDispatchEvent.class);

    @FunctionalInterface
    interface ParallelDispatchEvent {
        void event(IParallelDispatcher dispatcher);
    }
}
