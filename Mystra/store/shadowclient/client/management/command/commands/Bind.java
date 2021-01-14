package store.shadowclient.client.management.command.commands;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.command.Command;
import store.shadowclient.client.module.Module;

public class Bind extends Command {

	public Bind(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "bind";
	}

	@Override
	public String getDescription() {
		return "Allows user to change keybind of module";
	}

	@Override
	public String getSyntax() {
		return ".bind set [MOD] [Key] | .bind del [MOD] | .bind clear";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		if(args[0].equalsIgnoreCase("")) {
			Shadow.addChatMessage(getSyntax());
		}
		
		if(args[0].equalsIgnoreCase("set")) {
			args[2] = args[2].toUpperCase();
			int key = Keyboard.getKeyIndex(args[2]);
			
			for(Module m: Shadow.instance.moduleManager.getModules()) {
				if(args[1].equalsIgnoreCase(m.getName())) {
					m.setKey(Keyboard.getKeyIndex(Keyboard.getKeyName(key)));
					Shadow.addChatMessage(args[1] + " has been binded to " + key);
				}
			}
		} else if (args[0].equalsIgnoreCase("del")){
			for (Module m: Shadow.instance.moduleManager.getModules()) {
				if(m.getName().equalsIgnoreCase(args[1])) {
					m.setKey(0);
					Shadow.addChatMessage(args[1] + " has been unbinded");
				}
			}
		} else if (args[0].equalsIgnoreCase("clear")) {
			for(Module m: Shadow.instance.moduleManager.getModules()) {
				m.setKey(0);
			}
			Shadow.addChatMessage("All keys has been cleared");
		}
		return line;
	}

}
