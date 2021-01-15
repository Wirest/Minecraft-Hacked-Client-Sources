package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {
	
	public NoFall() {
		super("NoFall", Keyboard.KEY_O, Category.MOVEMENT);
	}
	
	
	@Override
	public void onUpdate() {
		
		if(!this.isToggled())
			return;
		
		if(Wrapper.mc.thePlayer.fallDistance > 2F) {
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		}
		
		super.onUpdate();
	}
}
