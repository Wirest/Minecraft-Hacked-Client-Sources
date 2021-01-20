package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventDestroyBlock;
import cedo.events.listeners.EventItemUse;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

public class AntiServer extends Module {

    public BooleanSetting syncBreak = new BooleanSetting("Sync Break", false),
            syncPlace = new BooleanSetting("Sync Place", false),
            antiClose = new BooleanSetting("Anti Close", true),
            noRotate = new BooleanSetting("No Rotate", true);

    public AntiServer() {
        super("AntiServer", Keyboard.KEY_NONE, Category.PLAYER);
        this.addSettings(syncBreak, syncPlace, antiClose, noRotate);
    }

    public void onEvent(Event e) {
        if ((syncBreak.isEnabled() && e instanceof EventDestroyBlock) || (syncPlace.isEnabled() && e instanceof EventItemUse)) {
            e.setCancelled(true);
        }
        if (antiClose.isEnabled() && e instanceof EventPacket && e.isPre() && e.isIncoming()) {
            if (((EventPacket) e).getPacket() instanceof S2EPacketCloseWindow) {
                e.setCancelled(true);
            }
        }
        if (noRotate.isEnabled()) {
            if (e instanceof EventPacket && e.isIncoming() && e.isPre()) {
                EventPacket event = (EventPacket) e;
                Packet packet = event.getPacket();

                if (packet instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) packet;
                    if (mc.thePlayer == null)
                        return;

                    s08.yaw = mc.thePlayer.rotationYaw;
                    s08.pitch = mc.thePlayer.rotationPitch;
                }
            }
        }
    }
}
