package cheatware.module.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;

public class Reload extends Module {
    public Reload() {
        super("Reload - DEV", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @Override
    public void onEnable() {
    	super.onEnable();
    	Cheatware.instance.clickGui = null;
    	Cheatware.instance.eventManager = null;
    	Cheatware.instance.moduleManager = null;
    	Cheatware.instance.settingsManager = null;
    	Cheatware.instance.reloadClient();
    }
}
