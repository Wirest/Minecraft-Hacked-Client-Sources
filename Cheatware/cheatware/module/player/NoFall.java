package cheatware.module.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", Keyboard.KEY_B, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if(mc.thePlayer.fallDistance > 2F)
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    }
}
