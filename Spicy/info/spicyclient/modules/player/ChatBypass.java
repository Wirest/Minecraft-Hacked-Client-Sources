package info.spicyclient.modules.player;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ChatBypass extends Module {
	
	private ModeSetting mode = new ModeSetting("Mode", "Russian", "Russian", "Roblox", "Hypixel", "Test3");
	
	public ChatBypass() {
		super("Chat Bypass", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(mode);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				this.additionalInformation = mode.getMode();
				
			}
			
		}
		
		if (e instanceof EventChatmessage) {
			
			if (e.isPre()) {
				
				EventChatmessage chat = (EventChatmessage) e;
				
				if (!chat.getMessage().startsWith("/") || chat.getMessage().startsWith("/shout")) {
					
					boolean shout = chat.getMessage().startsWith("/shout");
					
					if (shout) {
						chat.setMessage(chat.getMessage().replaceFirst("/shout", ""));
					}
					
					if (mode.getMode() == "Russian" || mode.is("Russian")) {
						chat.setMessage(StringUtils.replaceChars(chat.getMessage(), "ABESZIKMHOPCTXWVYaekmotb3hpcyx", "АВЕЅZІКМНОРСТХШѴУаекмотвзнрсух"));
					}
					if (mode.getMode() == "Roblox" || mode.is("Roblox")) {
						// https://instafonts.io/font/roblox-swear-bypass
						chat.setMessage(StringUtils.replaceChars(chat.getMessage(), "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM", "Ɋ山乇尺ㄒㄚㄩ丨ㄖ卩卂丂ᗪ千Ꮆ卄ﾌҜㄥ乙乂匚ᐯ乃几爪1234567890Ɋ山乇尺ㄒㄚㄩ丨ㄖ卩卂丂ᗪ千Ꮆ卄ﾌҜㄥ乙乂匚ᐯ乃几爪"));
					}
					if (mode.getMode() == "Hypixel" || mode.is("Hypixel")) {
						//  ⛍⛗⛌⛗⛘⛉⛡⛍⛗⛉⛍⛘⛜⛍⛠⛘⛟⛏⛡⛏⛗⛏⛍⛉⛋׼
						chat.setMessage(insertPeriodically(chat.getMessage(), "⛍⛗⛌⛗⛘⛉⛡⛍⛗⛉⛍⛘⛜⛍⛠⛘⛟⛏⛡⛏⛗⛏⛍⛉⛋׼", 1));
					}
					if (mode.getMode() == "Test3" || mode.is("Test3")) {
						
						String text = chat.getMessage();
					    StringBuilder builder = new StringBuilder(text.length() + ".".length() * (text.length()/1)+1);

						    int index = 0;
						    String prefix = "";
						    while (index < text.length())
						    {
						        // Don't put the insert in the very first iteration.
						        // This is easier than appending it *after* each substring
						        builder.append(prefix);
						        
						        Random random = new Random();
						        
						        String bypass = ".,';`\"";
						        prefix = Character.toString(bypass.charAt(random.nextInt(bypass.length())));
						        
						        builder.append(text.substring(index, 
						            Math.min(index + 1, text.length())));
						        index += 1;
						    }
						    chat.message = builder.toString();
						
					}
					
					if (shout) {
						chat.setMessage("/shout " + chat.getMessage());
					}
					
				}
				
			}
			
		}
		
	}
	
	public static String insertPeriodically(String text, String insert, int period) {
		    StringBuilder builder = new StringBuilder(
		         text.length() + insert.length() * (text.length()/period)+1);

		    int index = 0;
		    String prefix = "";
		    while (index < text.length())
		    {
		        // Don't put the insert in the very first iteration.
		        // This is easier than appending it *after* each substring
		        builder.append(prefix);
		        
		        Random random = new Random();
		        
		        //String bypass = "⛍⛗⛌⛗⛘⛉⛡⛍⛗⛉⛍⛘⛜⛍⛠⛘⛟⛏⛡⛏⛗⛏⛍⛉⛋׼";
		        prefix = Character.toString(insert.charAt(random.nextInt(insert.length())));
		        
		        builder.append(text.substring(index, 
		            Math.min(index + period, text.length())));
		        index += period;
		    }
		    return builder.toString();
		}
	
}
