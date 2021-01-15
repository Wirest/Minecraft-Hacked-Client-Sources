package info.spicyclient.chatCommands.commands;

import java.io.File;
import java.io.IOException;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.files.FileManager;

public class Config extends Command {

	public Config() {
		super("config", "config <save/load/list> <configName>", 1);
	}
	
	@Override
	public void commandAction(String message) {
		
		String[] splitMessage = message.split(" ");
		String configName = "";;
		for (int i = 0; i < splitMessage.length; i++) {
			if (i >= 2) {
				configName += splitMessage[i] + " ";
			}
		}
		
		if (configName != "") {
			configName = configName.replaceFirst(".config ", "");
			configName = configName.substring(0, configName.length() - 1);
		}
		
		if (splitMessage[1].equalsIgnoreCase("save") && configName != "") {
			try {
				sendPrivateChatMessage("Saving the config...");
				FileManager.save_config(configName);
				sendPrivateChatMessage("Config saved");
			} catch (IOException e) {
				sendPrivateChatMessage("Failed to save config");
				e.printStackTrace();
			}
		}
		else if (splitMessage[1].equalsIgnoreCase("load") && configName != "") {
			try {
				sendPrivateChatMessage("Loading the config...");
				FileManager.load_config(configName);
				sendPrivateChatMessage("Config loaded");
			} catch (IOException e) {
				sendPrivateChatMessage("Failed to load config");
				e.printStackTrace();
			}
		}
		else if (splitMessage[1].equalsIgnoreCase("list")) {
			File[] files = FileManager.configs.listFiles();
			
			if (files == null) {
				
				sendPrivateChatMessage("You have 0 configs");
				return;
				
			}
			
			sendPrivateChatMessage("You have " + files.length + " configs");
			
			for (File file : files) {
			    if (file.isFile()) {
			    	
			    	sendPrivateChatMessage(" - " + file.getName().replace(".con", ""));
			    	
			    }
			}
			
		}else {
			incorrectParameters();
		}
		
	}
	
	@Override
	public void incorrectParameters() {
		sendPrivateChatMessage("Please use .config <save/load/list> <configName>");
	}
	
}
