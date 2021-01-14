package de.iotacb.client.command.commands;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import de.iotacb.client.utilities.misc.ClipboardUtil;
import de.iotacb.client.utilities.misc.Printer;
import net.minecraft.client.Minecraft;

@CommandInfo(names = {"Name"}, description = "Copy your ingame name to the clipboard", usage = "#name")
public class NameCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		final String name = Minecraft.getMinecraft().thePlayer.getName();
		Client.PRINTER.printMessage("'".concat(Client.INSTANCE.getClientColorCode()).concat(name).concat("§f' has been copied to the clipboard."));
		ClipboardUtil.copyToClipboard(name);
	}

}
