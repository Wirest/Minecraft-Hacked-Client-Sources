package info.spicyclient.modules.memes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;

public class OwOifier extends Module {

	public OwOifier() {
		super("OwOifier", Keyboard.KEY_NONE, Category.MEMES);
		this.additionalInformation = "OwO UwU Whats This";
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	public void onEvent(Event e) {
		
		if (e instanceof EventChatmessage) {
			
			if (e.isPre()) {
				
				EventChatmessage chat = (EventChatmessage) e;
				if (!chat.getMessage().startsWith("/")) {
					chat.setMessage(chat.getMessage().replace("l", "w").replace("L", "W").replace("r", "w").replace("R", "W").replace("o", "u").replace("O", "U"));
				}
				
			}
			
		}
		
	}
	
}
