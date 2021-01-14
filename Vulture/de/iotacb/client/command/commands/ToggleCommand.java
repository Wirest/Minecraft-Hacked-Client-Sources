package de.iotacb.client.command.commands;

import org.lwjgl.input.Keyboard;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import de.iotacb.client.module.Module;
import de.iotacb.client.utilities.misc.Printer;

@CommandInfo(names = {"Toggle", "T"}, description = "Toggle a module", usage = "#toggle MODULENAME [on/off]")
public class ToggleCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		if (args.length == 3) {
			if (args[1].equalsIgnoreCase("all")) {
				Client.INSTANCE.getModuleManager().getModules().forEach(module -> module.setEnabled(false));
				Client.PRINTER.printMessage("Toggled all modules!");
			}
			for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
				if (module.getName().equalsIgnoreCase(args[1])) {
					module.setEnabled(args[2].equalsIgnoreCase("on") ? true : args[2].equalsIgnoreCase("off") ? false : false);
					Client.PRINTER.printMessage(String.format("Toggled module §l§6§n%s§f to §l§6§n%s", module.getName(), args[2]));
				}
			}
		} else if (args.length == 2) {
			for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
				if (module.getName().equalsIgnoreCase(args[1])) {
					module.toggle();
					Client.PRINTER.printMessage(String.format("Toggled module §l§6§n%s", module.getName()));
				}
			}
		} else {
			sendHelp();
		}
	}

}
