package com.teamabnormals.blueprint.core.mixin.fabric.client;

import com.teamabnormals.blueprint.core.events.lifecycle.ServerLifecycleEvents;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(method = "initServer", at = @At("TAIL"))
    public void starting(CallbackInfoReturnable<Boolean> cir) {
        ServerLifecycleEvents.STARTING.getInvoker().event((MinecraftServer) (Object) this);
    }
}