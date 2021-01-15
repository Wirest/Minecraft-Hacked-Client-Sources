package info.spicyclient.files;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.EventType;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventRender3D;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.player.ChatBypass;
import info.spicyclient.modules.player.HideName;
import info.spicyclient.networking.NetworkManager;
import info.spicyclient.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.util.ChatComponentText;

public class Account {
	
	public static transient HashMap<String, String> usernames = new HashMap<String, String>();
	public static transient Timer timer1 = new Timer(), timer2 = new Timer(), timer3 = new Timer();
	
	public String username = "", session = "";
	
	public boolean loggedIn = false;
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public void onEvent(Event e) {
		
		if (!SpicyClient.account.loggedIn) {
			return;
		}
		
		if (e instanceof EventPacket)
			onPacket((EventPacket) e);
		
		if (e instanceof EventUpdate)
			onUpdate((EventUpdate) e);
		
	}
	
	public void render3DStuff(EventRender3D e) {
		
		
		
	}
	
	public void onUpdate(EventUpdate e) {
		
		if (e.isPre() && timer2.hasTimeElapsed(60000, true)) {
			
		}
		
		int players = 20;
		
		if (mc.theWorld.playerEntities.size() <= 10) {
			
			players = 20;
			
		}else {
			
			players = mc.theWorld.playerEntities.size();
			
		}
		
		try {
			if (e.isPre() && timer1.hasTimeElapsed(2000 * players, true)) {
				
				for (EntityPlayer player : mc.theWorld.playerEntities) {
					
					new Thread() {
						
						public void run() {
							
							JSONObject json;
							try {
								
								//Command.sendPrivateChatMessage(player.getName());
								if (usernames.containsKey(player.getName())) {
									return;
								}
								
								json = new JSONObject(NetworkManager.getNetworkManager().sendPost(new HttpPost("http://SpicyClient.info/api/accountApi.php"), new BasicNameValuePair("type", "checkIfUserIsUsingSpicyClient"), new BasicNameValuePair("username", player.getName())));
								
								if (json.getBoolean("alt")) {
									String realUsername = json.getString("username");
									usernames.put(player.getName(), realUsername);
								}else {
									//usernames.put(player.getName(), " - - - - ");
								}
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						
					}.start();
					
				}
				
			}
		} catch (ConcurrentModificationException e2) {
			// TODO: handle exception
		}
		
	}
	
	public void onPacket(EventPacket e) {
		
		if (e instanceof EventPacket && e.isPre()) {
			
			if (SpicyClient.config.killSults.isEnabled()) {
				EventType temp = e.getType();
				e.setType(EventType.BEFOREPRE);
				SpicyClient.config.killSults.onEvent(e);
				e.setType(temp);
			}
			
			EventPacket packetEvent = (EventPacket) e;
			if (packetEvent.packet instanceof S02PacketChat) {
				
				S02PacketChat packet = (S02PacketChat) packetEvent.packet;
				
				/*
				mc.thePlayer.setCustomNameTag(mc.getSession().getUsername() + " §7{§c" + ChatBypass.insertPeriodically(SpicyClient.account.username, "⛍⛗⛌⛗⛘⛉⛡⛍⛗⛉⛍⛘⛜⛍⛠⛘⛟⛏⛡⛏⛗⛏⛍⛉⛋׼", 1) + "§7} ");
				if (packet.getChatComponent().getUnformattedText().replaceAll("׼", "").contains(mc.getSession().getUsername())) {
					packet.chatComponent = new ChatComponentText(packet.getChatComponent().getFormattedText().replaceAll("׼", "").replaceAll(mc.getSession().getUsername(), mc.getSession().getUsername() + " §7{§a" + ChatBypass.insertPeriodically(SpicyClient.account.username, "⛍⛗⛌⛗⛘⛉⛡⛍⛗⛉⛍⛘⛜⛍⛠⛘⛟⛏⛡⛏⛗⛏⛍⛉⛋׼", 1) + "§7} "));
				}
				*/
				
				for (String username : usernames.keySet()) {
					
					if (packet.getChatComponent().getUnformattedText().replaceAll("׼", "").contains(username) && usernames.get(username) != " - - - - ") {
						packet.chatComponent = new ChatComponentText(packet.getChatComponent().getFormattedText().replaceAll("׼", "").replaceAll(username, username + " §7{§a" + ChatBypass.insertPeriodically(usernames.get(username), "⛍⛗⛌⛗⛘⛉⛡⛍⛗⛉⛍⛘⛜⛍⛠⛘⛟⛏⛡⛏⛗⛏⛍⛉⛋׼", 1) + "§7} "));
					}
					
				}
				
			}
			else if (packetEvent.packet instanceof S3CPacketUpdateScore) {
				S3CPacketUpdateScore packet = (S3CPacketUpdateScore) packetEvent.packet;
				
				if (packet.getObjectiveName().replaceAll("׼", "").contains(mc.getSession().getUsername())){
					packet.setObjective(packet.getObjectiveName().replaceAll("׼", "").replaceAll(mc.getSession().getUsername(), mc.getSession().getUsername() + " §f(§a" + SpicyClient.account.username + "§f)"));
				}
				
				if (packet.getName().replaceAll("׼", "").contains(mc.getSession().getUsername())){
					packet.setName(packet.getName().replaceAll("׼", "").replaceAll(mc.getSession().getUsername(), mc.getSession().getUsername() + " §f(§a" + SpicyClient.account.username + "§f)"));
				}
				
			}
			
		}
		
	}
	
}
