package cheatware.module.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;

public class SelfDestruct extends Module {
    public SelfDestruct() {
        super("SelfDestruct", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @Override
    public void onEnable() {
    	Cheatware.instance.selfDestruct();
    }
}
