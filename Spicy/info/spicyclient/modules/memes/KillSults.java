package info.spicyclient.modules.memes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.SettingChangeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;

public class KillSults extends Module {
	
	public ModeSetting messageMode = new ModeSetting("Message Type", "Furry", "Furry", "Retarded Furry", "Annoying", "SpicyClient Ads", "SpicyFacts");
	private ModeSetting serverMode = new ModeSetting("Server Mode", "PvpLands", "PvpLands", "Hypixel", "Test");
	
	public BooleanSetting hypixelShout = new BooleanSetting("/Shout", false);
	
	public BooleanSetting pvplandsPayback = new BooleanSetting("Payback", false);
	
	public KillSults() {
		super("KillSults", Keyboard.KEY_NONE, Category.MEMES);
		setupMessageLists();
		resetSettings();
		this.additionalInformation = messageMode.getMode();
	}
	
	private static ArrayList<String> furryKillsults = new ArrayList<String>();
	private static ArrayList<String> retardedFurryKillsults = new ArrayList<String>();
	private static ArrayList<String> annoyingKillsults = new ArrayList<String>();
	private static ArrayList<String> spicyClientAdsKillsults = new ArrayList<String>();
	private static ArrayList<String> spicyFactsKillsults = new ArrayList<String>();
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(messageMode, serverMode, pvplandsPayback, hypixelShout);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		
		if (e != null && e.setting != null) {
			
			if (e.setting.equals(serverMode)) {
				
				if (this.settings.contains(hypixelShout)) {
					this.settings.remove(hypixelShout);
				}
				
				if (serverMode.is("PvpLands")) {
					
					if (!this.settings.contains(pvplandsPayback)) {
						
						settings.add(pvplandsPayback);
						this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						
					}
					
				}
				else if (serverMode.is("Test")) {
					
					if (this.settings.contains(pvplandsPayback)) {
						
						settings.remove(pvplandsPayback);
						this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						
					}
					
				}
				else if (serverMode.is("Hypixel")) {
					
					if (!this.settings.contains(hypixelShout)) {
						this.settings.add(hypixelShout);
					}
					
					if (this.settings.contains(pvplandsPayback)) {
						
						settings.remove(pvplandsPayback);
						this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
						
					}
					
					reorderSettings();
					
				}
				
			}
			
		}
		
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				this.additionalInformation = messageMode.getMode();
				
			}
			
		}
		
		if (e instanceof EventPacket) {
			
			if (e.isBeforePre()) {
				
				if (e.isIncoming()) {
					
					EventPacket event = (EventPacket) e;
					
					if (event.packet instanceof S02PacketChat) {
						
						S02PacketChat packet = (S02PacketChat) event.packet;
						
						String playerName = null;
						boolean OwOifier = false;
						boolean sendMessage = false;
						
						ArrayList<String> killsults = new ArrayList<String>();
						
						if (messageMode.getMode().toLowerCase().contains("furry".toLowerCase())) {
							
							OwOifier = true;
							
						}
						
						if (messageMode.is("Furry")) {
							
							killsults = this.furryKillsults;
							
						}
						else if (messageMode.is("Retarded Furry")) {
							
							killsults = this.retardedFurryKillsults;
							
						}
						else if (messageMode.is("Annoying")) {
							
							killsults = this.annoyingKillsults;
							
						}
						else if (messageMode.is("SpicyClient Ads")) {
							
							killsults = this.spicyClientAdsKillsults;
							
						}
						else if (messageMode.is("SpicyFacts")) {
							
							killsults = this.spicyFactsKillsults;
							
						}
						
						if (serverMode.is("PvpLands") || serverMode.getMode() == "PvpLands") {
							
							sendMessage = true;
							
							if (packet.getChatComponent().getFormattedText().toLowerCase().contains("§r§ayou've killed") && packet.getChatComponent().getFormattedText().contains(" received ")) {
								
								String[] strings = packet.getChatComponent().getFormattedText().split(" ");
								
								playerName = strings[2];
								String moneyBack = strings[5];
								moneyBack = moneyBack.replace("§", "");
								if (pvplandsPayback.enabled) {
									mc.thePlayer.sendChatMessage("/pay " + playerName + " " + moneyBack);
								}
								
							}else {
								return;
							}
							
						}
						else if (serverMode.is("Hypixel") || serverMode.getMode() == "Hypixel") {
							
							sendMessage = false;
							String message = packet.getChatComponent().getUnformattedText();
							
							if (message == null) {
								return;
							}
							
							String[] strings = packet.getChatComponent().getFormattedText().split(" ");
							
							if (strings == null) {
								return;
							}
							
				        	//if((message.toLowerCase().contains("you won! want to play again? click here!") || message.toLowerCase().contains("coins! (win)")) || message.toLowerCase().contains("experience! (win)")){
							if((message.toLowerCase().contains("queued! use the bed to return to lobby!")) || message.toLowerCase().contains("coins! (win)") || message.toLowerCase().contains("experience! (win)") || message.toLowerCase().contains("you won! want to play again? click here!") || message.toLowerCase().contains("you died! want to play again? click here!")){
				        		
								if (messageMode.is("Annoying") || messageMode.getMode() == "Annoying") {
									Command.sendPublicChatMessage("E׼Z");
								}else {
									Command.sendPublicChatMessage("GG");
								}
								
				        		return;
				        	}
							if(message.toLowerCase().contains("was killed by " + mc.thePlayer.getName().toLowerCase()) || message.toLowerCase().contains("was thrown into the void by " + mc.thePlayer.getName().toLowerCase()) || (message.toLowerCase().contains("was knocked into the void by") && message.toLowerCase().contains(mc.thePlayer.getName().toLowerCase())) || message.toLowerCase().contains("was thrown off a cliff by " + mc.thePlayer.getName().toLowerCase()) || packet.getChatComponent().getUnformattedText().toLowerCase().contains("§a§lkill!")){
								
								// Counts the amount of players in the game
								int players = 0;
								
								ArrayList<EntityPlayer> playersLeft = new ArrayList<EntityPlayer>();
								
								try{
			        				for(Object o : mc.theWorld.loadedEntityList){
			            				Entity t = (Entity)o;
			            				if(t instanceof EntityPlayer){
			            					EntityPlayer p = (EntityPlayer)t;
			            					
			            					if(p != null && !p.isInvisible()){
			            						players++;
			            						playersLeft.add(p);
			            					}
			            				}
			            			}
			        			}catch(ConcurrentModificationException y){
			        				
			        			}
								
			        			if(players != 0 && players != 1 && players != 2 && playersLeft != null && strings != null){
			        				
			        				Random random = new Random();
									message = killsults.get(random.nextInt(killsults.size()));
									
									playerName = "";
									
									for (String s : strings) {
										
										for (EntityPlayer p : playersLeft) {
											
											if (p != null && p != mc.thePlayer && s != null && s.toLowerCase().contains((p.getName()).toLowerCase())) {
												
												playerName = p.getName();
												
											}
											
										}
										
									}
									
									if (playerName == "") {
										return;
									}
									
									message = message.replaceAll("<PlayerName>", playerName);
									if (messageMode.is("Retarded Furry") || messageMode.getMode() == "Retarded Furry" || messageMode.is("Annoying") || messageMode.getMode() == "Annoying" || messageMode.is("SpicyClient Ads") || messageMode.getMode() == "SpicyClient Ads") {
										mc.thePlayer.sendChatMessage((this.hypixelShout.enabled ? "/shout " : "") + message);
									}else {
										Command.sendPublicChatMessage((this.hypixelShout.enabled ? "/shout " : "") + message);
									}
									return;
			        				
			        			}else {
			        				return;
			        			}
								
							}else {
								return;
							}
							
						}
						else if (serverMode.is("Test") || serverMode.getMode() == "Test") {
							
						}else {
							
							return;
							
						}
						
						Random random = new Random();
						
						String message = killsults.get(random.nextInt(killsults.size()));
						
						if (message == null) {
							return;
						}
						message = message.replaceAll("<PlayerName>", playerName);
						
						if (OwOifier) {
							
							//message = message.replace("l", "w").replace("L", "W").replace("r", "w").replace("R", "W").replace("o", "u").replace("O", "U");
							
						}
						
						if (sendMessage) {
							
							mc.thePlayer.sendChatMessage(message);
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public void setupMessageLists() {
		
		// Use <PlayerName> in those caps and with the <> to print out the player that you killed
		
		// For the furry killsults list
		furryKillsults.add("<PlayerName> Just got killed by a furry");
		furryKillsults.add("<PlayerName> OwO");
		furryKillsults.add("<PlayerName> UwU");
		furryKillsults.add("<PlayerName> Awoo");
		furryKillsults.add("<PlayerName> OwO?");
		furryKillsults.add("<PlayerName> UwU?");
		furryKillsults.add("<PlayerName> Awoo?");
		furryKillsults.add("OwO <PlayerName>");
		furryKillsults.add("UwU <PlayerName>");
		furryKillsults.add("Hello <PlayerName> would you like to OwO with me?");
		furryKillsults.add("Hello <PlayerName> would you like to UwU with me?");
		furryKillsults.add("Hello <PlayerName> would you like to Awoo with me?");
		furryKillsults.add("<PlayerName> Should legalize awoo");
		furryKillsults.add("<PlayerName> Help me legalize awoo");
		furryKillsults.add("<PlayerName> #LegalizeAwoo");
		furryKillsults.add("<PlayerName> is a furry confirmed?!?!?!?!!");
		furryKillsults.add("<PlayerName> should visit ht׼tp:׼//׼spi׼cyc׼lient׼.in׼fo/fu׼rry1.g׼if");
		furryKillsults.add("<PlayerName> really likes this meme h׼tt׼p:/׼/׼spicy׼clie׼nt.i׼nfo׼/furry׼2.׼jpg");
		furryKillsults.add("<PlayerName> Should check out ht׼tp׼:/׼/spicy׼client׼.i׼nfo/fur׼ry3׼.׼png׼");
		furryKillsults.add("<PlayerName> browses furaffinity");
		furryKillsults.add("<PlayerName> joined r/furryirl");
		// Removed the youtube channel names because they probably don't want to me mentioned in this client
		furryKillsults.add("<PlayerName> Probably watches furry youtubers");
		
		// WARNING THE RETARDED FURRY KILLSULTS ARE NSFW
		// For the retarded furry killsults list
		// Writing these made me cringe so hard and I refuse to make more
		retardedFurryKillsults.add("It's <PlayerName>s time to E621 and cry");
		retardedFurryKillsults.add("Hey <PlayerName> want to yiff with me?");
		retardedFurryKillsults.add("Hey everyone, did you know that <PlayerName> is a bottom");
		retardedFurryKillsults.add("<PlayerName> is a bottom");
		retardedFurryKillsults.add("<PlayerName> likes horse cock");
		retardedFurryKillsults.add("<PlayerName> browses E621 in their free time");
		retardedFurryKillsults.add("<PlayerName> Did you know that there is no cock like horse cock");
		retardedFurryKillsults.add("<PlayerName> bought a bad dragon dildo");
		retardedFurryKillsults.add("waww x3 nuzzwes pounces on <PlayerName> uwu <PlayerName> so wawm>");
		// Ok so maybe I made more...
		retardedFurryKillsults.add("hey <PlayerName> Want to browse e621 with me?");
		retardedFurryKillsults.add("<PlayerName> Do you also enjoy gay furry vore?");
		retardedFurryKillsults.add("Sorry <PlayerName>, but I only like the kinky furry shit");
		retardedFurryKillsults.add("Wait, <PlayerName> wants to mursuit with me?!?!?!?");
		retardedFurryKillsults.add("<PlayerName> Enjoys e621 confirmed?!?!?!?!?!?!?!");
		
		// For the annoying killsults list
		annoyingKillsults.add("<PlayerName> L");
		annoyingKillsults.add("<PlayerName> EZ");
		annoyingKillsults.add("<PlayerName> bad");
		annoyingKillsults.add("<PlayerName> get good noob");
		annoyingKillsults.add("<PlayerName> LLL");
		annoyingKillsults.add("<PlayerName> LLLL");
		annoyingKillsults.add("<PlayerName> uninstall the game");
		annoyingKillsults.add("<PlayerName> noob");
		annoyingKillsults.add("<PlayerName> is bad");
		annoyingKillsults.add("<PlayerName> EZ EZ EZ");
		annoyingKillsults.add("<PlayerName> bad lol");
		annoyingKillsults.add("<PlayerName> git gud");
		
		// For the spicyclient ads killsults list
		spicyClientAdsKillsults.add("<PlayerName> SpicyClient.info is your new home");
		spicyClientAdsKillsults.add("<PlayerName> Download SpicyClient at SpicyClient.info");
		spicyClientAdsKillsults.add("<PlayerName> SpicyClient is the easy way to get kills");
		spicyClientAdsKillsults.add("<PlayerName> Should install SpicyClient");
		spicyClientAdsKillsults.add("<PlayerName> SpicyClient increases your skill");
		spicyClientAdsKillsults.add("<PlayerName> Doesn't use SpicyClient");
		spicyClientAdsKillsults.add("<PlayerName> Download SpicyClient today to show those nons whos boss");
		spicyClientAdsKillsults.add("<PlayerName> SpicyClient is free and open source, download today at SpicyClient.info");
		spicyClientAdsKillsults.add("<PlayerName> Is bad because they dont use SpicyClient");
		spicyClientAdsKillsults.add("<PlayerName> Get SpicyClient noob");
		
		// for the spicy facts killsults list
		spicyFactsKillsults.add("<PlayerName> [FACT] SpicyClient is open source");
		spicyFactsKillsults.add("<PlayerName> [FACT] SpicyClient is free");
		spicyFactsKillsults.add("<PlayerName> [FACT] SpicyClient is the best free hypixel client");
		spicyFactsKillsults.add("<PlayerName> [FACT] You should download SpicyClient");
		spicyFactsKillsults.add("<PlayerName> [FACT] SpicyClient uses the MIT license");
		spicyFactsKillsults.add("<PlayerName> [FACT] SpicyClient is better than sigma");
		spicyFactsKillsults.add("<PlayerName> [FACT] SpicyClient > Sigma");
		spicyFactsKillsults.add("<PlayerName> [FACT] SpicyClient has tons of Hypixel bypasses");
		spicyFactsKillsults.add("<PlayerName> [FACT] Downloading SpicyClient makes you 200% more pog");
		
	}
	
}
