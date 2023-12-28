package com.teamabnormals.blueprint.core;

import com.teamabnormals.blueprint.core.util.IParallelDispatcher;

public class Blueprint {
    public static final String MOD_ID = "blueprint";

    public static void init() {

    }

    private static void onLoadComplete(IParallelDispatcher dispatcher) {
        dispatcher.enqueueWork(() -> {

        });
    }
}
