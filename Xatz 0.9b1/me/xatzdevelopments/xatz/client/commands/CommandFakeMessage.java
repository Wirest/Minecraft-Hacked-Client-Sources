package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class CommandFakeMessage extends Command {

	@Override
	public void run(String[] commands) {
		String toSay = "";
		for (int i = 0; i < commands.length; i++) {
			if (i == 0) {
				continue;
			}
			toSay += commands[i];
			toSay += " ";
		}
		toSay = toSay.replaceAll("&", "§");
		Xatz.chatMessage("", toSay);
	}

	@Override
	public String getActivator() {
		return ".fakemsg";
	}

	@Override
	public String getSyntax() {
		return ".fakemsg <message>";
	}

	@Override
	public String getDesc() {
		return "Enables you to fake chatmessages to false-report people for spam and swearing for example. Tip: You can use '&' for formatting.";
	}
}
