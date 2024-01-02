package com.teamabnormals.blueprint.core.fabric;

import com.teamabnormals.blueprint.core.events.lifecycle.ServerLifecycleEvents;
import com.teamabnormals.blueprint.core.events.lifecycle.TickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

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
        ServerTickEvents.START_SERVER_TICK.register(__ -> TickEvents.SERVER.getInvoker().tick(TickEvents.Phase.START));
        ServerTickEvents.END_SERVER_TICK.register(__ -> TickEvents.SERVER.getInvoker().tick(TickEvents.Phase.END));
        ServerTickEvents.START_WORLD_TICK.register(__ -> TickEvents.LEVEL.getInvoker().tick(TickEvents.Phase.START));
        ServerTickEvents.END_WORLD_TICK.register(__ -> TickEvents.LEVEL.getInvoker().tick(TickEvents.Phase.END));
    }

    public static void registerClient() {
        ClientTickEvents.START_CLIENT_TICK.register(__ -> TickEvents.CLIENT.getInvoker().tick(TickEvents.Phase.START));
        ClientTickEvents.END_CLIENT_TICK.register(__ -> TickEvents.CLIENT.getInvoker().tick(TickEvents.Phase.END));
        ClientTickEvents.START_WORLD_TICK.register(__ -> TickEvents.LEVEL.getInvoker().tick(TickEvents.Phase.START));
        ClientTickEvents.END_WORLD_TICK.register(__ -> TickEvents.LEVEL.getInvoker().tick(TickEvents.Phase.END));
    }
}
