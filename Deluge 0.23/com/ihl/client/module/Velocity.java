package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPacket;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueDouble;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@EventHandler(events = {EventPacket.class})
public class Velocity extends Module {

    public Velocity(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("multiplier", new Option("Multiplier", "Knockback multiplier", new ValueDouble(0, new double[]{0, 5}, 0.1), Option.Type.NUMBER));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        double multiplier = Option.get(options, "multiplier").DOUBLE();

        if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.RECEIVE) {
                if (e.packet instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.packet;
                    if (Helper.world().getEntityByID(packet.func_149412_c()) == Helper.player()) {
                        packet.x *= multiplier;
                        packet.y *= multiplier;
                        packet.z *= multiplier;
                        if (multiplier == 0) {
                            e.cancel();
                        }
                    }
                }
            }
        }
    }

}
