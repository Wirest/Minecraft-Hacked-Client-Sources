package de.iotacb.client.command.commands;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import de.iotacb.client.file.files.ClientConfigFile;
import de.iotacb.client.utilities.misc.Printer;

@CommandInfo(names = {"NameProtect", "NP"}, description = "Change your name when using NameProtect", usage = "#nameprotect NAME")
public class NameProtectCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		if (args.length > 1) {
			final String oldName = Client.INSTANCE.getNameProtectName();
			Client.INSTANCE.setNameProtectName(args[1]);
			((ClientConfigFile) Client.INSTANCE.getFileManager().getFileByClass(ClientConfigFile.class)).saveConfig();
			Client.PRINTER.printMessage("Changed NameProtect name from '".concat(Client.INSTANCE.getClientColorCode()).concat(oldName).concat("§f' to '").concat(Client.INSTANCE.getClientColorCode()).concat(args[1]).concat("§f'!"));
		} else {
			sendHelp();
		}
	}

}
