package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class CommandSay extends Command {

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
		Xatz.sendChatMessageFinal(toSay);
	}

	@Override
	public String getActivator() {
		return ".say";
	}

	@Override
	public String getSyntax() {
		return ".say <message>";
	}

	@Override
	public String getDesc() {
		return "Enables you to type \".legit\" or anything in chat: \".say .legit\"";
	}
}
