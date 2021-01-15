package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPlayerMove;
import net.minecraft.network.play.client.C03PacketPlayer;

@EventHandler(events = {EventPlayerMove.class})
public class Damage extends Module {

    public Damage(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPlayerMove) {
            EventPlayerMove e = (EventPlayerMove) event;
            if (e.type == Event.Type.PRE) {
                e.x = 0;
                e.z = 0;
                for (int i = 0; i < 40; i++) {
                    Helper.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Helper.player().posX, Helper.player().posY + 0.03, Helper.player().posZ, false));
                    Helper.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Helper.player().posX, Helper.player().posY - 0.05, Helper.player().posZ, false));
                }
                disable();
            }
        }
    }

}
