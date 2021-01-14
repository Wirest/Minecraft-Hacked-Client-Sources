package com.etb.client.module.modules.player;

import com.etb.client.Client;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.gui.notification.Notification;
import com.etb.client.module.Module;
import com.etb.client.utils.Printer;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

/**
 * made by oHare for oHareWare
 *
 * @since 7/14/2019
 **/
public class AntiDeath extends Module {
    private boolean teleport, teleported;

    public AntiDeath() {
        super("AntiDeath", Category.PLAYER, new Color(0x4DAE99).getRGB());
    }

    @Override
    public void onEnable() {
        teleport = false;
        teleported = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.getHealth() <= 3 && !teleport) {
            teleport = true;
        }
        if (teleport && !teleported) {
            mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(0, -999, 0);
            Client.INSTANCE.getNotificationManager().sendClientMessage("You were about to die so we teleported you to spawn, you're welcome!", Notification.Type.SUCCESS);
            Printer.print("You were about to die so we teleported you to spawn, you're welcome!");
            teleported = true;
        }
        if (teleport && mc.thePlayer.getHealth() > 3) {
            teleport = false;
            teleported = false;
        }
    }
}
