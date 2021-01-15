package info.spicyclient.modules.memes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.SettingChangeEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;

public class Furries extends Module {
	
	public static transient HashMap<AbstractClientPlayer, furries> playerSkins = new HashMap<AbstractClientPlayer, furries>();
	
	public Furries() {
		super("Furries", Keyboard.KEY_NONE, Category.MEMES);
	}
	
	public void onEnable() {
		playerSkins.clear();
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		if (e instanceof EventUpdate && e.isPre()) {
			
			
			if (SpicyClient.config.floofyFoxes.isEnabled())
				SpicyClient.config.floofyFoxes.toggle();
			
			if (SpicyClient.config.dougDimmadome.isEnabled())
				SpicyClient.config.dougDimmadome.toggle();
			
		}
		
	}
	
	public static ResourceLocation setSkin(AbstractClientPlayer player) {
		
		if (!playerSkins.containsKey(player)) {
			
			playerSkins.put(player, furries.values()[new Random().nextInt(furries.values().length)]);
			
		}
		
		return playerSkins.get(player).skin;
		
	}
	
	public enum furries{
		
		FOX1(new ResourceLocation("spicy/skins/fox1.png")),
		FOX2(new ResourceLocation("spicy/skins/fox2.png")),
		PANDA1(new ResourceLocation("spicy/skins/panda1.png")),
		WOLF1(new ResourceLocation("spicy/skins/wolf1.png")),
		WOLF2(new ResourceLocation("spicy/skins/wolf2.png")),
		WOLF3(new ResourceLocation("spicy/skins/wolf3.png")),
		DOG1(new ResourceLocation("spicy/skins/dog1.png"));
		
		public ResourceLocation skin;
		
		private furries(ResourceLocation skin) {
			this.skin = skin;
		}
		
	}
	
}
