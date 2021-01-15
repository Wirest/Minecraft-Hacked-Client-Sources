package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventUpdate;
import me.onlyeli.ice.utils.ChatUtils;
import me.onlyeli.ice.utils.NetUtils;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class ServerLagger extends Module {

	public ServerLagger() {
		super("ServerLagger", Keyboard.KEY_P, Category.MISC);
	}

	public void onEnable() {
		if (Wrapper.mc.isSingleplayer()) {
			ChatUtils.sendMessageToPlayer("Multiplayer Only!");
			if(!this.isToggled())
			return;
		}
		EventManager.register(this);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		for (int i = 0; i < 2000; i++) {
			NetUtils.sendPacket(new C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY,
					Wrapper.mc.thePlayer.posZ, true));
			NetUtils.sendPacket(new C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY,
					Wrapper.mc.thePlayer.posZ, false));
		}
	}

	public void onDisable() {
		EventManager.unregister(this);
	}
}