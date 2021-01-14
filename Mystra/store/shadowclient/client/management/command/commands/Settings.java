package store.shadowclient.client.management.command.commands;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.command.Command;

public class Settings extends Command {

	public Settings(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "settings";
	}

	@Override
	public String getDescription() {
		return "Auto setting modules.";
	}

	@Override
	public String getSyntax() {
		return ".settings [Setting] | .settings list";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		if(args[0].equalsIgnoreCase("")) {
			Shadow.addChatMessage(getSyntax());
			
		} else if(args[0].equalsIgnoreCase("list")) {
			Shadow.addChatMessage("Setting List: Hypixel, TheHive, Mooncraft, Redesky");
			
		} else if(args[0].equalsIgnoreCase("Mooncraft")) {
			
			//DISABLE ALL MODULES BEFORE ENABLE
			Shadow.instance.moduleManager.getModules();
			
			
			//COMBAT
			Shadow.instance.moduleManager.getModuleByName("AutoArmor").toggle();
			Shadow.instance.moduleManager.getModuleByName("AutoSword").toggle();
			Shadow.instance.moduleManager.getModuleByName("AimAssist").toggle();
			Shadow.instance.moduleManager.getModuleByName("AutoSoup").toggle();
			Shadow.instance.moduleManager.getModuleByName("AntiBot").toggle();
			Shadow.instance.moduleManager.getModuleByName("Reach").toggle();
			//RENDER
			Shadow.instance.moduleManager.getModuleByName("Fullbright").toggle();
			Shadow.instance.moduleManager.getModuleByName("AntiFire").toggle();
			Shadow.instance.moduleManager.getModuleByName("Tracers").toggle();
			Shadow.instance.moduleManager.getModuleByName("Chams").toggle();
			Shadow.instance.moduleManager.getModuleByName("ESP").toggle();
			//PLAYER
			Shadow.instance.moduleManager.getModuleByName("ChestStealer").toggle();
			Shadow.instance.moduleManager.getModuleByName("AutoRespawn").toggle();
			Shadow.instance.moduleManager.getModuleByName("FastPlace").toggle();
			//MOVEMENT
			Shadow.instance.moduleManager.getModuleByName("Velocity").toggle();
			Shadow.instance.moduleManager.getModuleByName("InvMove").toggle();
			Shadow.instance.moduleManager.getModuleByName("Sprint").toggle();
			//MISC
			Shadow.instance.moduleManager.getModuleByName("SwordAnimation").toggle();
			Shadow.instance.moduleManager.getModuleByName("Cosmetics").toggle();
			Shadow.instance.moduleManager.getModuleByName("HUD").toggle();
			
			//VALUES
		}
		return line;
	}

}
