package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class Headless extends Module {

	public Headless() {
		super("Headless", Keyboard.KEY_NONE, Category.FUN, "Your head is gone for other players!");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if (Xatz.doDisablePacketSwitch()) {
			return;
		}
		event.pitch = 180f;
		super.onUpdate(event);
	}

}
