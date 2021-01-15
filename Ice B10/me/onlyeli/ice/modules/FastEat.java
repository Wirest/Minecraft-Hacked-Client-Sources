package me.onlyeli.ice.modules;

import java.awt.Color;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventListener;
import me.onlyeli.ice.events.EventType;
import me.onlyeli.ice.events.EventPlayerUpdate;
import me.onlyeli.ice.events.ModData;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

import org.lwjgl.input.Keyboard;

public class FastEat extends Module {

	public FastEat() {
		super("FastEat", Keyboard.KEY_NONE, Category.PLAYER);
	}

	@EventListener
	public void onUpdate(EventPlayerUpdate event){
		if(event.getType() == EventType.PRE){
			if(Wrapper.mc.thePlayer.onGround && Wrapper.mc.thePlayer.getItemInUseDuration() >= 15 && Wrapper.mc.thePlayer.getItemInUse().getItem() instanceof ItemFood){
				for (int i = 0; i <= 20; i++){
					Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
			}
		}
	}

}
