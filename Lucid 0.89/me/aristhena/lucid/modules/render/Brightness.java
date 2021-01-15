/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.modules.render;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.TickEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;

@Mod
public class Brightness
extends Module {
	
    @EventTarget
    private void onTick(TickEvent event) {
    	this.mc.gameSettings.gammaSetting = 100F;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    	this.mc.gameSettings.gammaSetting = 0F;
    }
}

