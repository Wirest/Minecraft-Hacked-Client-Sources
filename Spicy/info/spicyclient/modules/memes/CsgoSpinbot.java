package info.spicyclient.modules.memes;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.util.RenderUtils;
import info.spicyclient.util.Timer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;

public class CsgoSpinbot extends Module {
	
	private float yaw = 0;
	private float lastYaw = 0;
	
	ModeSetting mode = new ModeSetting("Mode", "Slow Smooth", "Slow Smooth", "Fast Smooth", "Random");
	BooleanSetting lookDown = new BooleanSetting("Look Down", true);
	
	public CsgoSpinbot() {
		super("CS:GO Spinbot", Keyboard.KEY_NONE, Category.BETA);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(mode, lookDown);
	}
	
	@Override
	public void onEnable() {
		
		yaw = mc.thePlayer.rotationYaw;
		lastYaw = mc.thePlayer.rotationYaw;
		
	}
	
	@Override
	public void onDisable() {
		RenderUtils.resetPlayerYaw();
		RenderUtils.resetPlayerPitch();
	}
	
	private static Timer timer = new Timer();
	
	public transient boolean skipRender = false;
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && !SpicyClient.config.killaura.isEnabled()) {
			
			if (e.isPre()) {
				
				lastYaw += 45 + (new Random().nextFloat() * 1000);
				
				if (skipRender) {
					RenderUtils.resetPlayerPitch();
					RenderUtils.resetPlayerYaw();
					skipRender = false;
					return;
				}
				
				RenderUtils.setCustomYaw(lastYaw);
				if (lookDown.isEnabled()) {
					RenderUtils.setCustomPitch(90);
				}else {
					RenderUtils.resetPlayerPitch();
				}
				
			}
			
		}
		else if (SpicyClient.config.killaura.isEnabled()) {
			
		}
		
		if (e instanceof EventSendPacket && !SpicyClient.config.killaura.isEnabled()) {
			
			if (e.isPre()) {
				EventSendPacket event = (EventSendPacket) e;
				if (event.packet instanceof C08PacketPlayerBlockPlacement || event.packet instanceof C02PacketUseEntity || event.packet instanceof C0APacketAnimation || event.packet instanceof C07PacketPlayerDigging) {
					timer.reset();
					skipRender = true;
					mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
				}
				
				if (!timer.hasTimeElapsed(500, false)) {
					RenderUtils.resetPlayerYaw();
					RenderUtils.resetPlayerPitch();
					return;
				}
				
				if (((EventSendPacket) e).packet instanceof C03PacketPlayer.C05PacketPlayerLook || ((EventSendPacket) e).packet instanceof C03PacketPlayer.C06PacketPlayerPosLook){
					((C03PacketPlayer)((EventSendPacket) e).packet).setYaw(lastYaw);
					
					if (lookDown.enabled) {
						((C03PacketPlayer)((EventSendPacket) e).packet).setPitch(90f);
					}
					
				}
				else if (((EventSendPacket) e).packet instanceof C03PacketPlayer.C04PacketPlayerPosition){
					C03PacketPlayer.C04PacketPlayerPosition oldPosPacket = (C03PacketPlayer.C04PacketPlayerPosition) event.packet;
					C03PacketPlayer.C05PacketPlayerLook newPosLookPacket = new C03PacketPlayer.C05PacketPlayerLook(lastYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround);
					
					if (lookDown.enabled) {
						newPosLookPacket.setPitch(90f);
					}
					
					//mc.getNetHandler().getNetworkManager().sendPacket(newPosLookPacket);
					
				}
				
			}
			
		}
		
	}
	
}
