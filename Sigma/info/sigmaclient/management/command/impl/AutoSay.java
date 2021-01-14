package info.sigmaclient.management.command.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.SettingsMap;
import info.sigmaclient.util.FileUtils;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class AutoSay extends Command {
	private static final File MESSAGE = FileUtils.getConfigFile("AutoSay");
    public AutoSay(String[] names, String description) {
        super(names, description);
        loadStatus();
    }
    public static void saveStatus() {
        List<String> fileContent = new ArrayList<>();
        fileContent.add(String.format("%s", info.sigmaclient.module.impl.other.AutoSay.message));
        FileUtils.write(MESSAGE, fileContent, true);
    }
    public static void loadStatus() {
        try {
            List<String> fileContent = FileUtils.read(MESSAGE);
            for (String line : fileContent) {
            	info.sigmaclient.module.impl.other.AutoSay.message = line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void fire(String[] args) {
        Module autosay = Client.getModuleManager().get(info.sigmaclient.module.impl.other.AutoSay.class);
        SettingsMap settings = autosay.getSettings();
    	if (args == null) {
    		printUsage();
            return;
        }
        if (args.length >= 1) {
           	try{
           		String message ="";
            	for(int i = 0; i < args.length ; i++){
            		String str = args[i];
            		message = message + str + " ";
            	}
            	ChatUtil.printChat(chatPrefix + "AutoSay message set to : §r" + message);         	
            	info.sigmaclient.module.impl.other.AutoSay.message = message;
            	saveStatus();
           	}catch(NumberFormatException e){
           		printUsage();
           		
           	}
           	return;
        }
        printUsage();
    }

    @Override
    public String getUsage() {
        return "autosay <message>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
