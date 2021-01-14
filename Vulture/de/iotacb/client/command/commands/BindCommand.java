package de.iotacb.client.command.commands;

import org.lwjgl.input.Keyboard;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import de.iotacb.client.module.Module;
import de.iotacb.client.utilities.misc.Printer;

@CommandInfo(names = {"Bind", "B"}, description = "Bind a module to a key", usage = "#bind MODULENAME KEY/,ACTIONKEY")
public class BindCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		if (args.length == 3) {
			if (args[1].equalsIgnoreCase("all") && args[2].equalsIgnoreCase("none")) {
				for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
					module.setKey(-1);
					module.setMultiBindKey(-1);
					module.setMultiBinded(false);
				}
				Client.PRINTER.printMessage("Cleared §c§n§Lall§f binds.");
			}
			boolean found = false;
			for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
				if (module.getName().equalsIgnoreCase(args[1])) {
					final String key = args[2];
					if (key.contains(",")) {
						final String[] keys = key.split(",");
						if (keys.length == 2) {
							final int keyCode = Keyboard.getKeyIndex(keys[0].toUpperCase());
							final int multiKeyCode = Keyboard.getKeyIndex(keys[1].toUpperCase());
							if (keyCode != -1 && multiKeyCode != -1) {
								module.setKey(keyCode);
								module.setMultiBindKey(multiKeyCode);
								module.setMultiBinded(true);
								found = true;
								Client.PRINTER.printMessage(String.format("Action bound module §l§6§n%s§f to key §l§6§n%s§f and key §l§6§n%s", module.getName(), keys[0].toUpperCase(), keys[1].toUpperCase()));
							} else {
								Client.PRINTER.printMessage(String.format("§l§6§n%s§f or §l§6§n%s §fis not a valid key code!", keys[0].toUpperCase(), keys[1].toUpperCase()));
							}
						}
					} else {
						final int keyCode = Keyboard.getKeyIndex(args[2].toUpperCase());
						if (keyCode != -1) {
							module.setKey(keyCode);
							module.setMultiBindKey(-1);
							module.setMultiBinded(false);
							found = true;
							Client.PRINTER.printMessage(String.format("Bound module §l§6§n%s§f to key §l§6§n%s", module.getName(), args[2].toUpperCase()));
						} else {
							Client.PRINTER.printMessage(String.format("§l§6§n%s §fis not a valid key code!", args[2].toUpperCase()));
						}
					}
				}
			}
		} else {
			sendHelp();
		}
	}

}
