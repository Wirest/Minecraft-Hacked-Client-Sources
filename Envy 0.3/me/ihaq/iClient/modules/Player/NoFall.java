package me.ihaq.iClient.modules.Player;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", Keyboard.KEY_NONE, Category.PLAYER, "");
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (!this.isToggled()) {
			return;
		}
		if (mc.thePlayer.fallDistance > 2.0F && !mc.thePlayer.onGround) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		}

	}

}