package com.etb.client.module.modules.movement;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.BooleanValue;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

public class Sneak extends Module {
    public Sneak() {
        super("Sneak", Category.MOVEMENT, new Color(84, 194, 110, 255).getRGB());
        setDescription("Automatically sneaks for you");
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if ((mc.thePlayer.isSneaking()) || (mc.thePlayer.isMoving())) return;
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        } else {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }

    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }
}
