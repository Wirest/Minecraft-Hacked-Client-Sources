package info.spicyclient.modules.player;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class YawAndPitchSpoof extends Module {
	
	ModeSetting pitchMode = new ModeSetting("Pitch Mode", "Down", "Down", "Up", "Middle", "No Spoof");
	ModeSetting yawMode = new ModeSetting("Yaw Mode", "Forward", "Forward", "Reversed");
	
	public YawAndPitchSpoof() {
		super("Yaw And Pitch Spoof", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(pitchMode, yawMode);
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventSendPacket && e.isBeforePre() && !SpicyClient.config.killaura.isEnabled()) {
			
			EventSendPacket event = (EventSendPacket) e;
			
			if (event.packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
				
				C03PacketPlayer.C05PacketPlayerLook packet = (C03PacketPlayer.C05PacketPlayerLook) event.packet;
				
				if (pitchMode.is("Down")) {
					packet.setPitch(90);
				}
				else if (pitchMode.is("Up")) {
					packet.setPitch(-90);
				}
				else if (pitchMode.is("Middle")) {
					packet.setPitch(0);
				}
				else if (pitchMode.is("No Spoof")) {
					
				}
				
				if (yawMode.is("Forward")) {
					
				}
				else if (yawMode.is("Reversed")) {
					packet.setYaw(mc.thePlayer.rotationYaw + 180);
				}
				else if (yawMode.is("No Spoof")) {
					
				}
				
			}
			else if (event.packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
				
				C03PacketPlayer.C06PacketPlayerPosLook packet = (C03PacketPlayer.C06PacketPlayerPosLook) event.packet;
				
				if (pitchMode.is("Down")) {
					packet.setPitch(90);
				}
				else if (pitchMode.is("Up")) {
					packet.setPitch(-90);
				}
				else if (pitchMode.is("Middle")) {
					packet.setPitch(0);
				}
				else if (pitchMode.is("No Spoof")) {
					
				}
				
				if (yawMode.is("Forward")) {
					
				}
				else if (yawMode.is("Reversed")) {
					packet.setYaw(mc.thePlayer.rotationYaw + 180);
				}
				else if (yawMode.is("No Spoof")) {
					
				}
				
			}
			
			if (event.packet instanceof C08PacketPlayerBlockPlacement || event.packet instanceof C02PacketUseEntity || event.packet instanceof C0APacketAnimation || event.packet instanceof C07PacketPlayerDigging || event.packet instanceof C0EPacketClickWindow) {
				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
			}
			
		}
		
	}
	
}
