package com.teamabnormals.blueprint.core.mixin.fabric;

import com.teamabnormals.blueprint.core.events.lifecycle.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(method = "initServer", at = @At("TAIL"))
    public void starting(CallbackInfoReturnable<Boolean> cir) {
        ServerLifecycleEvents.STARTING.getInvoker().event((MinecraftServer) (Object) this);
    }
}