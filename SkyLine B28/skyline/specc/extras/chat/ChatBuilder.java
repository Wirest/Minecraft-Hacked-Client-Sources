package skyline.specc.extras.chat;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import skyline.specc.utils.Wrapper;

public class ChatBuilder {

	private ChatComponentText message = new ChatComponentText("");

	public void send(){
		Wrapper.getPlayer().addChatMessage(message);
	}
	
	public ChatBuilder appendMessage(ChatComponentText message){
		message.appendSibling(message);
		return this;
	}
	
	public ChatComponentText getMessage(){
		return this.message;
	}
	
	private static ChatComponentText parseMessage(String message){
		return null;
	}
	
	public ChatBuilder appendText(String string, ChatColor... colors){
		ChatComponentText text = new ChatComponentText(string);
		ChatStyle style = new ChatStyle();
		
		for(ChatColor color : colors){
			if(color == ChatColor.BOLD){
				style.setBold(true);
			}else if(color == ChatColor.ITALIC){
				style.setItalic(true);
			}else if(color == ChatColor.STRIKETHROUGH){
				style.setStrikethrough(true);
			}else if(color == ChatColor.UNDERLINE){
				style.setUnderlined(true);
			}else if(color == ChatColor.OBFUSCATED){
				style.setObfuscated(true);
			}else if(color == ChatColor.RESET){
				style = new ChatStyle();
			}else{
				
				for(EnumChatFormatting chatColor : EnumChatFormatting.values()){
					if(chatColor.getFriendlyName().equalsIgnoreCase(color.toString())){
						style.setColor(chatColor);
					}
				}
			}
		}
		
		text.setChatStyle(style);
		
		message.appendSibling(text);
		return this;
	}
	
	public ChatBuilder appendText(String string){
		ChatComponentText text = new ChatComponentText(string);
		message.appendSibling(text);
		return this;
	}
	
	public ChatBuilder appendText(String string, ClickEvent event, ChatColor... colors){
		ChatComponentText text = new ChatComponentText(string);
		ChatStyle style = new ChatStyle();
		
		for(ChatColor color : colors){
			if(color == ChatColor.BOLD){
				style.setBold(true);
			}else if(color == ChatColor.ITALIC){
				style.setItalic(true);
			}else if(color == ChatColor.STRIKETHROUGH){
				style.setStrikethrough(true);
			}else if(color == ChatColor.UNDERLINE){
				style.setUnderlined(true);
			}else if(color == ChatColor.OBFUSCATED){
				style.setObfuscated(true);
			}else if(color == ChatColor.RESET){
				style = new ChatStyle();
			}else{
				
				for(EnumChatFormatting chatColor : EnumChatFormatting.values()){
					if(chatColor.getFriendlyName().equalsIgnoreCase(color.toString())){
						style.setColor(chatColor);
					}
				}
			}
		}
		style.setChatClickEvent(event);
		text.setChatStyle(style);
		message.appendSibling(text);
		return this;
	}
	
}
