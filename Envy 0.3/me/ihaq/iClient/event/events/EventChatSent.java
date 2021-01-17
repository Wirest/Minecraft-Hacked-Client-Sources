package me.ihaq.iClient.event.events;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.command.Command;
import me.ihaq.iClient.command.CommandManager;
import me.ihaq.iClient.event.Event;

public class EventChatSent extends Event {

	private boolean cancel;
	private static String message;

	public EventChatSent(String message) {
		this.message = message;
	}

	public void checkForCommands(String message) {
		if (message.startsWith(CommandManager.prefix)) {
			for (Command command : Envy.COMMANDS.commands) {
				if (this.message.split(" ")[0]
						.equalsIgnoreCase(String.valueOf(CommandManager.prefix) + command.getCommand())) {
					try {
						command.run(message);
					} catch (Exception e) {
						Envy.tellPlayer("Incorrect syntax! " + command.getCommand() + " " + command.getArguments());
					}
					this.cancel = true;
				}
			}
			if (!this.cancel) {
				Envy.tellPlayer("Command \"" + this.message + "\" was not found!");
				this.cancel = true;
			}
		}
	}

	public static String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public boolean getCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}