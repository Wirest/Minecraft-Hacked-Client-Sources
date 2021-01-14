package store.shadowclient.client.management.command.commands;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.command.Command;
import store.shadowclient.client.module.Module;

public class Toggle extends Command {

	public Toggle(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "toggle";
	}

	@Override
	public String getDescription() {
		return "Allows you to toggle a module";
	}

	@Override
	public String getSyntax() {
		return ".toggle [MOD]";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		
		if(args[0].equalsIgnoreCase("")) {
			Shadow.addChatMessage(getSyntax());
		}
		
		boolean found = false;
		for(Module m: Shadow.instance.moduleManager.getModules()) {
			if(args[0].equalsIgnoreCase(m.getName())) {
				if(m.isToggled()) {
				Shadow.addChatMessage(m.getName() + " has been §cdisabled!");
			}else {
				Shadow.addChatMessage(m.getName() + " has been §aenabled!");
			}
				m.toggle();
				found = true;
			}
		}
		if(found == false) {
			Shadow.addChatMessage("§cInvalid Module!");
		}
		return line;
	}

}
