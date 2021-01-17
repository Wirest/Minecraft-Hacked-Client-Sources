/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;

public class Timer
extends Mod {
    private Value<Double> speed = new Value<Double>("Timer_Speed", 1.0, 0.1, 10.0);

    public Timer() {
        super("Timer", Mod.Category.MISCELLANEOUS, 0);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.setColor(-12799119);
        this.mc.timer.timerSpeed = this.speed.getValueState().floatValue();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
        ClientUtil.sendClientMessage("Timer Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Timer Enable", ClientNotification.Type.SUCCESS);
    }
}

