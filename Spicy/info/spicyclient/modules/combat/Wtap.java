package info.spicyclient.modules.combat;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventGetBlockReach;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Wtap extends Module {
	
	public Wtap() {
		super("WTap", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public Timer timer = new Timer();
	
	public void onEvent(Event e) {
		
		if (e instanceof EventSendPacket && e.isPre()) {
			Packet packet = ((EventSendPacket)e).packet;
			
			if (packet instanceof C02PacketUseEntity) {
				
				C02PacketUseEntity attack = (C02PacketUseEntity) packet;
				
				if (attack.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
					
					boolean continueSprint = mc.thePlayer.isSprinting();
					
					//mc.thePlayer.setSprinting(false);
		            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
		            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
		            mc.thePlayer.setSprinting(continueSprint);
					
				}
				
			}
			
		}
		
	}
	
}
