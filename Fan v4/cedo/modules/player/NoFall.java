package cedo.modules.player;

import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.util.server.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class NoFall extends Module {

    public NoFall() {
        super("NoFall", Keyboard.KEY_N, Category.PLAYER);
    }

    public void onEvent(Event e) { //TODO add a mode to manipulate packets rather than spam new ones
        if (e instanceof EventMotion && e.isPre()) {
            if (Wrapper.getFuckedPrinceKin == 1438E-4) {
                PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(200, 10, true, true, false, true));
            }
            if (mc.thePlayer.fallDistance >= 3) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            }
        }
    }
}
	