package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPlayerMotion;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueBoolean;
import net.minecraft.network.play.client.C03PacketPlayer;

@EventHandler(events = {EventPlayerMotion.class})
public class Phase extends Module {

    private boolean hasPhased = false;

    public Phase(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("sneak", new Option("Sneak", "Phase only when sneaking", new ValueBoolean(true), Option.Type.BOOLEAN));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        boolean sneak = Option.get(options, "sneak").BOOLEAN();
        if (event instanceof EventPlayerMotion) {
            EventPlayerMotion e = (EventPlayerMotion) event;
            if (e.type == Event.Type.POST) {
                if (Helper.player().isCollidedHorizontally && Helper.player().isCollidedVertically && (sneak && Helper.player().isSneaking())) {
                    double val = (Helper.player().rotationYaw + 90 + (Helper.player().moveForward > 0 ? 0 + (Helper.player().moveStrafing > 0 ? -45 : Helper.player().moveStrafing < 0 ? 45 : 0) : Helper.player().moveForward < 0 ? 180 + (Helper.player().moveStrafing > 0 ? 45 : Helper.player().moveStrafing < 0 ? -45 : 0) : 0 + (Helper.player().moveStrafing > 0 ? -90 : Helper.player().moveStrafing < 0 ? 90 : 0))) * Math.PI / 180;

                    double x = Math.cos(val) * 0.3;
                    double z = Math.sin(val) * 0.3;

                    for (int i = 0; i < 10; i++) {
                        Helper.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Helper.player().posX, Helper.player().posY - 0.01, Helper.player().posZ, true));
                        Helper.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Helper.player().posX + (x * i), Helper.player().posY, Helper.player().posZ + (z * i), true));
                    }
                }
            }
        }
    }

}
