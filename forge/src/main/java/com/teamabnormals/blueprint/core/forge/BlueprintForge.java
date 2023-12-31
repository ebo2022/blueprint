package com.teamabnormals.blueprint.core.forge;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.util.forge.ForgeUtil;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(Blueprint.MOD_ID)
public class BlueprintForge {

    public BlueprintForge() {
        Blueprint.init();
        ForgeUtil.addModBusConsumer(Blueprint.MOD_ID, bus -> {
            bus.<FMLClientSetupEvent>addListener(event -> ModLifecycleEvents.CLIENT_SETUP.getInvoker().event(ForgeUtil.createParallelDispatcher(event)));
            bus.<FMLCommonSetupEvent>addListener(event -> ModLifecycleEvents.COMMON_SETUP.getInvoker().event(ForgeUtil.createParallelDispatcher(event)));
            bus.<FMLDedicatedServerSetupEvent>addListener(event -> ModLifecycleEvents.DEDICATED_SERVER_SETUP.getInvoker().event(ForgeUtil.createParallelDispatcher(event)));
            bus.<FMLLoadCompleteEvent>addListener(event -> ModLifecycleEvents.LOAD_COMPLETE.getInvoker().event(ForgeUtil.createParallelDispatcher(event)));
        });
    }
}
