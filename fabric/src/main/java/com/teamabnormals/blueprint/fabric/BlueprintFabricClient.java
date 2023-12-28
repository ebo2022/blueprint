package com.teamabnormals.blueprint.fabric;

import com.teamabnormals.blueprint.core.events.lifecycle.CommonLifecycleEvents;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.fabricmc.api.ClientModInitializer;

// Client initializers are ran after the regular ones meaning registries should be complete
public class BlueprintFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommonLifecycleEvents.COMMON_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        CommonLifecycleEvents.CLIENT_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        CommonLifecycleEvents.LOAD_COMPLETE.getInvoker().event(IParallelDispatcher.EMPTY);
    }
}
