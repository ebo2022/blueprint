package com.teamabnormals.blueprint.core.fabric;

import com.teamabnormals.blueprint.core.events.lifecycle.ServerLifecycleEvents;

public class BlueprintFabricEvents {

    public static void register() {
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            ServerLifecycleEvents.ABOUT_TO_START.getInvoker().event(server);
            BlueprintFabric.server = server;
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerLifecycleEvents.STARTED.getInvoker().event(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvents.STOPPING.getInvoker().event(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            ServerLifecycleEvents.STOPPED.getInvoker().event(server);
            BlueprintFabric.server = null;
        });
    }
}
