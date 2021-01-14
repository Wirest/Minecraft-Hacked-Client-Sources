package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class AntiKB extends Module {
    public AntiKB() {
        super("AntiKB", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public void onEvent(Event e) {
        if (e instanceof EventPacket && e.isPre() && e.isIncoming()) {

            EventPacket event = (EventPacket) e;
            Packet<?> packet = event.getPacket();
            if (packet instanceof S12PacketEntityVelocity) {

                S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) packet;

                if (s12.getEntityID() == mc.thePlayer.getEntityId())
                    e.setCancelled(true);
            }

            if (packet instanceof S27PacketExplosion) {
                e.setCancelled(true);
            }
        }
    }
}