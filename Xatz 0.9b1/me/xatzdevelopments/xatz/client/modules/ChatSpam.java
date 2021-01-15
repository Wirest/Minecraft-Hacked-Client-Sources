package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class ChatSpam extends Module {

	public ChatSpam() {
		super("ChatSpam", Keyboard.KEY_NONE, Category.MISC, "Spams the chat.");
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

		mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("Spam"));

		super.onUpdate();
	}

}
