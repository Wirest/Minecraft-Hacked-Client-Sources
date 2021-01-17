/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class WaterSpeed
extends Mod {
    private Value<Double> speed = new Value<Double>("WaterSpeed_Speed", 1.17, 1.05, 1.5, 0.01);
    private TimeHelper time = new TimeHelper();
    private boolean speedUp;

    public WaterSpeed() {
        super("WaterSpeed", Mod.Category.MOVEMENT, Colors.DARKBLUE.c);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.setColor(Colors.GREY.c);
        if (this.mc.thePlayer.isInWater()) {
            this.mc.thePlayer.motionX *= this.speed.getValueState().doubleValue();
            this.mc.thePlayer.motionZ *= this.speed.getValueState().doubleValue();
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("WaterSpeed Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("WaterSpeed Enable", ClientNotification.Type.SUCCESS);
    }
}

