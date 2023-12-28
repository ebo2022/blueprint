package com.teamabnormals.blueprint.fabric;

import com.teamabnormals.blueprint.core.events.lifecycle.CommonLifecycleEvents;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class BlueprintFabric implements ModInitializer, DedicatedServerModInitializer {

    @Override
    public void onInitialize() {
    }

    // Server initializers are ran after the regular ones meaning registries should be complete
    @Override
    public void onInitializeServer() {
        CommonLifecycleEvents.COMMON_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        CommonLifecycleEvents.DEDICATED_SERVER_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        CommonLifecycleEvents.LOAD_COMPLETE.getInvoker().event(IParallelDispatcher.EMPTY);
    }
}
