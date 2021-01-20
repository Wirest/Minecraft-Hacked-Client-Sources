package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class NoFall extends Module {
    float niggerfalldistance;

    public NoFall() {
        super("NoFall", Keyboard.KEY_N, Category.PLAYER);
    }

    public void onEvent(Event e) { //TODO add a mode to manipulate packets rather than spam new ones
        if (e instanceof EventMotion && e.isPre()) {
            if(mc.thePlayer.fallDistance >= 3){
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
            }
        }
    }
}
	