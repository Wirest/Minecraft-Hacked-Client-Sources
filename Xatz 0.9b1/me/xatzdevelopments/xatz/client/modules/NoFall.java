package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", Keyboard.KEY_NONE, Category.PLAYER, "Disables fall damage.");
	}

	@Override
	public void onUpdate() {
		if (mc.thePlayer.fallDistance > 2.0f) {
			sendPacket(new C03PacketPlayer(true));
		}
		super.onUpdate();
	}
}
