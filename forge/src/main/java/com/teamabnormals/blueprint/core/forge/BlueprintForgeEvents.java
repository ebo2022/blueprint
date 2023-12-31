package com.teamabnormals.blueprint.core.forge;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.events.lifecycle.ServerLifecycleEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.*;

@Mod.EventBusSubscriber(modid = Blueprint.MOD_ID)
public class BlueprintForgeEvents {

    @SubscribeEvent
    public static void event(ServerAboutToStartEvent event) {
        ServerLifecycleEvents.ABOUT_TO_START.getInvoker().event(event.getServer());
    }

    @SubscribeEvent
    public static void event(ServerStartingEvent event) {
        ServerLifecycleEvents.STARTING.getInvoker().event(event.getServer());
    }

    @SubscribeEvent
    public static void event(ServerStartedEvent event) {
        ServerLifecycleEvents.STARTED.getInvoker().event(event.getServer());
    }

    @SubscribeEvent
    public static void event(ServerStoppingEvent event) {
        ServerLifecycleEvents.STOPPING.getInvoker().event(event.getServer());
    }

    @SubscribeEvent
    public static void event(ServerStoppedEvent event) {
        ServerLifecycleEvents.STOPPED.getInvoker().event(event.getServer());
    }
}
