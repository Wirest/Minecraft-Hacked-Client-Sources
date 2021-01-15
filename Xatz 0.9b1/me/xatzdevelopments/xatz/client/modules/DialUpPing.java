package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C00PacketKeepAlive;

public class DialUpPing extends Module {

	int tries = 0;

	public DialUpPing() {
		super("Dial-Up Ping", Keyboard.KEY_NONE, Category.MISC, "This derps your ping");
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
		if (tries > 150) {
			tries = 0;
		}
		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C00PacketKeepAlive) {
			if (tries != 0) {
				packet.cancel();
			}
			tries++;
		}
		super.onPacketSent(packet);
	}

}
