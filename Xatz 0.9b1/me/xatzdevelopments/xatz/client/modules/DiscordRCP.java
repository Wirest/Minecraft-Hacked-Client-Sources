package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.client.multiplayer.ServerAddress;


public class DiscordRCP extends Module {
	
	public static String discordappid = "";
	
	

	public DiscordRCP() {
		super("DiscordRCP", Keyboard.KEY_NONE, Category.MISC, "Toggles the Discord Rich Presence(Requires disabling and reanabling after startup and after changing mode)");
	}

	@Override
	public void onDisable() {
		Xatz.getInstance().shutdown();
		super.onDisable();
	}

	@Override
	public void onEnable() {
		Xatz.getInstance().init();
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		
		if (this.currentMode.equals("Xatz")){
			discordappid = "738835303959101572";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("BLC")){
			discordappid = "739497081005408326";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("Lunar")){
			discordappid = "739501420092457040";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("PvP Lounge")){
			discordappid = "739522541583859834";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("LabyMod")){
			discordappid = "739515586161147954";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("Hyperium")){
			discordappid = "739516102249545759";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("CheatBreaker")){
			discordappid = "739516906654138448";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("Cosmic Client")){
			discordappid = "739516624217964746";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
		
		if (this.currentMode.equals("Forge")){
			discordappid = "739517851714584666";
			if(mc.isSingleplayer()) {
				Xatz.getInstance().getDiscordRP().update("Playing Singleplayer", "In Game");
			}else {
				Xatz.getInstance().getDiscordRP().update("Playing " + "MultiPlayer" , "In Game");
			}
		}
				
				
		super.onUpdate();
	}
	
	public String[] getModes() {
		return new String[] { "Xatz", "BLC", "Lunar", "PvP Lounge", "LabyMod", "Hyperium", "CheatBreaker", "Cosmic Client", "Forge" };
	}
	
	public String getModeName() {
		return "Style: ";
	}

}
