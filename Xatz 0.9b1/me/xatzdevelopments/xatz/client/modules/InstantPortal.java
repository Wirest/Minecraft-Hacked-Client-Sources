package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class InstantPortal extends Module {

	public InstantPortal() {
		super("InstaPortal", Keyboard.KEY_NONE, Category.PLAYER, "Reduces the teleport time for portals.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if(mc.thePlayer.timeInPortal > 0) {
			for(int i = 0; i < 15; i++) {
				sendPacket(new C03PacketPlayer(true));
			}
		}
		super.onUpdate();
	}

}
