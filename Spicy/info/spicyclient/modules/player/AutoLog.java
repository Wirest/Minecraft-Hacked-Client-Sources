package info.spicyclient.modules.player;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventServerSetYawAndPitch;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.ui.NewAltManager;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

public class AutoLog extends Module {
	
	private ModeSetting mode = new ModeSetting("mode", "Vanilla", "Vanilla", "Packet");
	private NumberSetting health = new NumberSetting("health", 4, 1, 20, 1);
	
	
	public AutoLog() {
		super("AutoLog", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(health, mode);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre() && mc.thePlayer != null) {
				
				if (mode.is("Vanilla")) {
					
					if (mc.thePlayer.getHealth() <= health.getValue() && mc.thePlayer.getHealth() != 0) {
						
						mc.thePlayer.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("§6[ §f" + SpicyClient.config.clientName + SpicyClient.config.clientVersion + " §6] §f" + "Disconnected from the server so you wouldn't die, autolog has been disabled"));
						this.notifyPlayer();
						
					}
					
				}
				else if (mode.is("Packet")) {
					
					if (mc.thePlayer.getHealth() <= health.getValue() && mc.thePlayer.getHealth() != 0) {
						
						for (int i = 0; i < 1000; i++) {
							
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
							
						}
						this.notifyPlayer();
						
					}
					
				}
				
			}
			
		}
		
	}
	
	private void notifyPlayer() {
		
		//Command.sendPrivateChatMessage("Disconnected from the server so you wouldn't die, autolog has been disabled");
		NotificationManager.getNotificationManager().createNotification("Autolog was disabled", "", true, 5000, Type.INFO, Color.PINK);
		NotificationManager.getNotificationManager().createNotification("Disconnected from the server due to autolog", "", true, 5000, Type.INFO, Color.PINK);
		this.toggle();
		
	}
	
}
