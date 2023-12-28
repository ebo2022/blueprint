package com.teamabnormals.blueprint.fabric;

import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.fabricmc.api.ClientModInitializer;

// Client initializers are ran after the regular ones meaning registries should be complete
public class BlueprintFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModLifecycleEvents.COMMON_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.CLIENT_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.LOAD_COMPLETE.getInvoker().event(IParallelDispatcher.EMPTY);
    }
}
