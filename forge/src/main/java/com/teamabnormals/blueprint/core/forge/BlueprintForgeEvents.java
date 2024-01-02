package com.teamabnormals.blueprint.core.forge;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.events.lifecycle.ServerLifecycleEvents;
import com.teamabnormals.blueprint.core.events.lifecycle.TickEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
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

    @SubscribeEvent
    public static void event(TickEvent.ClientTickEvent event) {
        TickEvents.CLIENT.getInvoker().tick(getPhase(event.phase));
    }

    @SubscribeEvent
    public static void event(TickEvent.ServerTickEvent event) {
        TickEvents.SERVER.getInvoker().tick(getPhase(event.phase));
    }

    @SubscribeEvent
    public static void event(TickEvent.LevelTickEvent event) {
        TickEvents.LEVEL.getInvoker().tick(getPhase(event.phase));
    }

    @SubscribeEvent
    private static TickEvents.Phase getPhase(TickEvent.Phase phase) {
        return switch (phase) {
            case START -> TickEvents.Phase.START;
            case END -> TickEvents.Phase.END;
        };
    }
}
