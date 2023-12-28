package com.teamabnormals.blueprint.fabric;

import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
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
        ModLifecycleEvents.COMMON_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.DEDICATED_SERVER_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.LOAD_COMPLETE.getInvoker().event(IParallelDispatcher.EMPTY);
    }
}
