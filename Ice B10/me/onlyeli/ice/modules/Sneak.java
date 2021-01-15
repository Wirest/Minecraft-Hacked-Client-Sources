package me.onlyeli.ice.modules;

import java.awt.Color;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventListener;
import me.onlyeli.ice.events.EventType;
import me.onlyeli.ice.events.EventMotion;
import me.onlyeli.ice.events.ModData;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.network.play.client.C0BPacketEntityAction;

import org.lwjgl.input.Keyboard;

public class Sneak extends Module {

	public Sneak() {
		super("Sneak", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onEnable() {
		Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
	}
	
	public void onDisable() {
		Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
	}
	
	@EventListener
	public void onMotion(EventMotion event){
		if(event.getType() == EventType.PRE){
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}else{
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
	}
	
}
