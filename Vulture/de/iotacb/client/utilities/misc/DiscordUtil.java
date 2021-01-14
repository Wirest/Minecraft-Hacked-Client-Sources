package de.iotacb.client.utilities.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import de.iotacb.client.Client;
import de.iotacb.client.module.Module;
import de.iotacb.client.utilities.player.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiSelectWorld;

public class DiscordUtil {
	
	private final DiscordRPC RPC;
	private final DiscordEventHandlers HANDLERS;
	private final DiscordRichPresence PRESENCE;
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public DiscordUtil(String applicationId) {
		RPC = DiscordRPC.INSTANCE;
		HANDLERS = new DiscordEventHandlers();
		HANDLERS.ready = (user) -> System.out.println("Discord is ready!");
		RPC.Discord_Initialize(applicationId, HANDLERS, true, "");
		PRESENCE = new DiscordRichPresence();
        PRESENCE.largeImageKey = "discord-rpc-logo-big";
        PRESENCE.largeImageText = "Vulture Client " + Client.INSTANCE.getClientVersion() + ". By " + Client.INSTANCE.getClientAuthor() + ". MC 1.8.8";
        PRESENCE.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        RPC.Discord_UpdatePresence(PRESENCE);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
            	RPC.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                    if (getPresence() != null) {
                    	getPresence().details = generateDetails();
                    	getPresence().state = generateState();
                    	getPresence().smallImageKey = generateServerIcon();
                    	getRpc().Discord_UpdatePresence(getPresence());
                    }
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
	}
	
	public String generateDetails() {
		String details = "";
		if (MC.thePlayer == null) {
			if (MC.currentScreen instanceof GuiMainMenu) {
				details = "In main menu";
			}
		} else {
			if (MC.getCurrentServerData() == null) {
				details = "Playing in singleplayer";
			} else {
				details = "Playing on " + MC.getCurrentServerData().serverIP;
			}
		}
		
		return details;
	}
	
	public String generateState() {
		String state = "";
		if (MC.thePlayer != null) {
			state = "Ingame name: " + MC.thePlayer.getGameProfile().getName() + (!Client.MOVEMENT_UTIL.isMoving() ? " | AFK" : "");
		}
		return state;
	}
	
	public String generateServerIcon() {
		String icon = "";
		if (MC.getCurrentServerData() != null) {
			String ip = MC.getCurrentServerData().serverIP;
			if (ip.contains("gommehd")) {
				icon = "gommehd-icon";
			} else if (ip.contains("cubecraft")) {
				icon = "cubecraft-icon";
			} else if (ip.contains("hivemc")) {
				icon = "hivemc-icon";
			} else if (ip.contains("hypixel")) {
				icon = "hypixel-icon";
			} else if (ip.contains("pika")) {
				icon = "pika-icon";
			} else {
				icon = "";
			}
		} else {
			icon = "";
		}
		
		return icon;
	}
	
	public DiscordRPC getRpc() {
		return RPC;
	}
	
	public DiscordRichPresence getPresence() {
		return PRESENCE;
	}
	
	public void updatePresence() {
		RPC.Discord_UpdatePresence(getPresence());
	}
	
}
