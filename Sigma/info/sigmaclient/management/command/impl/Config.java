package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.Client;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.FileUtils;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.misc.ChatUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
 * @author LeakedPvP
 */
public class Config extends Command {
	ArrayList<String> basicConfigs = new ArrayList<String>() {{
	    add("hypixel");
	    add("mineplex");
	    add("cubecraft");
	    add("aac");
	    add("ncp");
	}};
    public Config(String[] names, String description) {
        super(names, description);
    }
    
    @Override
    public void fire(String[] args) {
        if (args == null || args.length > 2) {
            printUsage();
            return;
        }else if(args.length == 1){
         	if(args[0].equalsIgnoreCase("load")){
         		ChatUtil.printChat(chatPrefix + "Config : .config load <ConfigName>");
         		return;
         	}else if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("save")){
         		ChatUtil.printChat(chatPrefix + "Config : .config create <ConfigName>");
         		return;
         	}else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("rem")){
         		ChatUtil.printChat(chatPrefix + "Config : .config remove <ConfigName>");
         		return;
         	}else if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("removeall")){
         		clearConfigs();
         		return;
         	}else if(args[0].equalsIgnoreCase("list")){
         		ArrayList<String> list = getConfigList();
         		int lenght = list.size();
         		String send = chatPrefix + "Config list : ";
         		for(int i = 0; i < lenght; i++){
         			if(basicConfigs.contains(list.get(i))){
         				if(i == lenght-1)
         					send = send + "\247a" + list.get(i) + "\2477.";
         				else
         					send = send + "\247a" + list.get(i) + "\2477, ";	
         			}else{
         				if(i == lenght-1)
         					send = send + "\2477" + list.get(i) + ".";
         				else
         					send = send + "\2477" + list.get(i) + ", ";
         			}
         		}
         		ChatUtil.printChat(send);
         		return;
         	}
        }else if(args.length == 2){
        	if(args[0].equalsIgnoreCase("load")){
        		String config = args[1];
        		loadConfig(config);
                return;
        	}else
        	if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("save")){
        		String config = args[1];
        		saveConfig(config);
                return;
        	}else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("rem")){
        		String config = args[1];
        		removeConfig(config);
        		return;
        	}
        }
        printUsage();
    }

    public void loadConfig(String config) {
    	String filePath = "Configs/" + config + ".config" ;
    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
    	List<String> readContent = new ArrayList<String>();
    	File configF = FileUtils.getConfig(config);
    	if(!basicConfigs.contains(config.toLowerCase())){
    		if(configF == null){
    			Notifications.getManager().post("Config","\247b" + config+ " \247rconfig does not exist !", Notifications.Type.WARNING);
    			return;
    		}
    		readContent = FileUtils.read(configF);	
    	}else{             
            if (inputStream != null) {
                try (BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line = null;
                    while ((line = bufferedInputStream.readLine()) != null) {
                        readContent.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Notifications.getManager().post("Config","Error while loading \247b"+ config + " \247rconfig.", Notifications.Type.WARNING);
                }
            }else{
            	Notifications.getManager().post("Config","\247b" + config+ " \247rconfig does not exist !", Notifications.Type.WARNING);
            }
    	}
        if (!readContent.isEmpty()) {
            for (String line : readContent) {
            	String[] split = line.split(":");
                for (Module module : Client.getModuleManager().getArray()) {              	
                	if (split[0].equalsIgnoreCase("DIS") && split[1].equalsIgnoreCase(module.getName()) && module.isEnabled()) {
                		 module.toggle();
                	}else if (split[0].equalsIgnoreCase("FEM") && split[1].equalsIgnoreCase(module.getName()) && !module.isEnabled()) {
                        module.toggle();
                    } else if (module.getName().equalsIgnoreCase(split[0])) {
                        Setting setting = Module.getSetting(module.getSettings(), split[1]);
                        String settingValue = split[2];
                        if (setting != null) {
                            if (setting.getValue() instanceof Number) {
                                Object newValue = (StringConversions.castNumber(settingValue, setting.getValue()));
                                if (newValue != null) {
                                    setting.setValue(newValue);
                                }
                            } // If the setting is supposed to be a string
                            else if (setting.getValue().getClass().equals(String.class)) {
                                String parsed = settingValue.toString().replaceAll("_", " ");
                                setting.setValue(parsed);
                            } // If the setting is supposed to be a boolean
                            else if (setting.getValue().getClass().equals(Boolean.class)) {
                                setting.setValue(Boolean.parseBoolean(settingValue));
                            } // If the setting is supposed to be an option
                            else if (setting.getValue().getClass().equals(Options.class)) {
                                Options dank = ((Options) setting.getValue());
                                for (String str : dank.getOptions()) {
                                    if (str.equalsIgnoreCase(settingValue)) {
                                        dank.setSelected(settingValue);
                                    }
                                }
                            } 
                        }
                    }
                }
            }
            Notifications.getManager().post("Config", "\247b" + config + " \247rconfig has been loaded!", Notifications.Type.INFO);
        }else{
        	Notifications.getManager().post("Config", "\247b" + config + " \247rconfig is empty !", Notifications.Type.INFO);
        }
    }
    public void saveConfig(String config) {
    	if(basicConfigs.contains(config.toLowerCase())){
    		Notifications.getManager().post("Config", "You can not change this config !", Notifications.Type.WARNING);
    		return;
    	}
    	if (!config.matches("^[a-zA-Z0-9_]+$")) {
            Notifications.getManager().post("Config","\247b" + config + "\247r is invalid !", Notifications.Type.WARNING);
        	return;
    	}
        File dir = Client.getDataDir();
        File[] directoryListing = dir.listFiles();
        boolean shouldCreate = true;
        for (File child : directoryListing) {
        	String fileName = child.getName(); 	
        	if(fileName.equalsIgnoreCase("Configs")){
        		 shouldCreate = false;
        	}
        }
        if(shouldCreate){
        	File file = new File(Client.getDataDir().toString() + "/Configs");
        	try{
        		file.mkdir();
        		directoryListing = dir.listFiles();
        	}catch (Exception e){
        		e.printStackTrace();
        	}      	
        }
        File configDir = new File(Client.getDataDir().toString() + "/Configs");
        directoryListing = configDir.listFiles();
        File configFile = FileUtils.getConfigFile("Configs/" + config);
        for(File child : directoryListing){
        	String fileName = child.getName();
        	if(fileName.endsWith(".txt")){
        		try{
        			String str[] = fileName.split(".txt");
        			fileName = str[0];
        			if(fileName.equalsIgnoreCase(config)){
                        Notifications.getManager().post("Config","\247b" + config + "\247r config already exists !", Notifications.Type.WARNING);
                		return;
                	}
        		}catch(Exception e){
        			ChatUtil.printChat("§c" + e);
        		}
        	}
        }
        List<String> fileContent = new ArrayList<>();
        for (Module module : Client.getModuleManager().getArray()) {
        	if(module.getType() == ModuleData.Type.Visuals)
        		continue;
        	String displayName = module.getName();
        	if(displayName != "")
        	if(module.isEnabled()){
        		fileContent.add(String.format("FEM:%s", displayName));
        	}else{
        		fileContent.add(String.format("DIS:%s", displayName));
        	}
            for (Setting setting : module.getSettings().values()) {
            	if (!(setting.getValue() instanceof Options)) {
                    String settingName = setting.getName();
                    String settingValue = setting.getValue().toString();
                    fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
                } else {
                    String settingName = setting.getName();
                    String settingValue = ((Options) setting.getValue()).getSelected();
                    fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
                }
            }
        }
       
        FileUtils.write(configFile, fileContent, true);
        Notifications.getManager().post("Config","\247b" + config + "\247r config has been saved !", Notifications.Type.INFO);
		
    }
    @Override
    public void onEvent(Event event) {

    }
    public ArrayList<String> getConfigList(){
    	ArrayList<String>list = new ArrayList();
 		list.addAll(basicConfigs);	
        File dir = new File(Client.getDataDir().getName() + "\\Configs");
        File[] directoryListing = dir.listFiles();
        if(!dir.exists()){
        	dir.mkdir();
        }else
        for(File child : directoryListing){
        	String fileName = child.getName().split("\\.")[0];
        	list.add(fileName);
        } 
    	return list;
    }
    void clearConfigs(){
    	File dir = new File(Client.getDataDir().getName() + "\\Configs");
        File[] directoryListing = dir.listFiles();
        if(!dir.exists() || directoryListing.length == 0){
        	Notifications.getManager().post("Config","You have no config saved !", Notifications.Type.WARNING);
        	return;
        }
        for(File child : directoryListing){
        	child.delete();
        }
        String str = directoryListing.length > 1? "\247b" + directoryListing.length + "\247r configs have been removed" : "\247b" + directoryListing.length + "\247r config has been removed" ;
        Notifications.getManager().post("Config",str, Notifications.Type.INFO);
    }  	
    
    void removeConfig(String config){
    	File dir = new File(Client.getDataDir().getName() + "\\Configs");
        File[] directoryListing = dir.listFiles();
        if(basicConfigs.contains(config.toLowerCase())){
        	Notifications.getManager().post("Config", "You can not remove this config !", Notifications.Type.WARNING);
        	return;
        }
        	
        if(!dir.exists() || directoryListing.length == 0){
        	Notifications.getManager().post("Config","\247b" + config+ " \247rconfig does not exist !", Notifications.Type.WARNING);
        	return;
        }
        for(File child : directoryListing){
        	String fileName = child.getName();
        	String[] split =fileName.split("\\.");

        	if(split[0].equalsIgnoreCase(config)){
        		child.delete();
        		Notifications.getManager().post("Config","\247b" + config + " \247rconfig has been removed", Notifications.Type.INFO);
        		return;
        	}    	
        }
        Notifications.getManager().post("Config","\247b" + config+ " \247rconfig does not exist !", Notifications.Type.WARNING);
    }
    @Override
    public String getUsage() {
        return ".config <load/save/remove/list/clear>";
    }

}
