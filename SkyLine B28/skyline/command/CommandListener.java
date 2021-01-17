package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command;

import java.util.List;

import skyline.specc.SkyLine;
import skyline.specc.extras.chat.ChatBuilder;
import skyline.specc.extras.chat.ChatColor;

public class CommandListener {

	public void onCommandRan(List<String> args){}
	
	public void addChat(String message){
		new ChatBuilder()
		.appendText("[", ChatColor.DARK_GRAY)
		.appendText(SkyLine.getVital().getClient().getName(), SkyLine.getVital().getClient().getData().getDisplayColor())
		.appendText("]", ChatColor.DARK_GRAY)
		.appendText(" " + message, ChatColor.GRAY).send();
	}
	
	public void addChat(ChatBuilder chatBuilder){
		new ChatBuilder()
		.appendText("[", ChatColor.DARK_GRAY)
		.appendText(SkyLine.getVital().getClient().getName(), SkyLine.getVital().getClient().getData().getDisplayColor())
		.appendText("]", ChatColor.DARK_GRAY)
		.appendText(" ", ChatColor.GRAY)
		.appendMessage(chatBuilder.getMessage()).send();
	}
	
	public void error(String message){
		new ChatBuilder()
		.appendText("[", ChatColor.DARK_GRAY)
		.appendText(SkyLine.getVital().getClient().getName(), SkyLine.getVital().getClient().getData().getDisplayColor())
		.appendText("]", ChatColor.DARK_GRAY)
		.appendText(" " + message, ChatColor.RED).send();
	}
	
	public void error(ChatBuilder chatBuilder){
		new ChatBuilder()
		.appendText("[", ChatColor.DARK_GRAY)
		.appendText(SkyLine.getVital().getClient().getName(), SkyLine.getVital().getClient().getData().getDisplayColor())
		.appendText("]", ChatColor.DARK_GRAY)
		.appendText(" ", ChatColor.RED)
		.appendMessage(chatBuilder.getMessage()).send();
	}
	
}
