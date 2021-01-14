package de.iotacb.client.command.commands;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import de.iotacb.client.module.modules.misc.IRC;
import de.iotacb.cu.core.string.StringUtil;

@CommandInfo(names = {"IRC"}, description = "Send messages to the client chat", usage = ".irc MESSAGE")
public class IRCCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		if (!Client.INSTANCE.getModuleManager().getModuleByClass(IRC.class).isEnabled()) {
			Client.PRINTER.printMessage("The client IRC is disabled!");
			return;
		}
		if (args.length > 1) {
			if (Client.INSTANCE.getIrc().isConnected()) {
				final String message = StringUtil.joinArray(args, " ", 1, args.length);
				Client.INSTANCE.getIrc().getReceiver().getSender().addMessage(message);
			} else {
				Client.PRINTER.printMessage("You are not connected to the IRC!");
			}
		} else {
			sendHelp();
		}
	}
}

