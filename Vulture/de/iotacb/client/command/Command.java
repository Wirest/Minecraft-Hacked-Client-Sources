package de.iotacb.client.command;

import de.iotacb.client.Client;
import de.iotacb.client.utilities.misc.Printer;

public abstract class Command {
	
	private final String[] names;
	private final String description;
	private final String usage;
	
	public Command() {
		final CommandInfo info = getClass().getAnnotation(CommandInfo.class);
		
		this.names = info.names();
		this.description = info.description();
		this.usage = info.usage();
		
		init();
	}
	
	public void init() {}
	
	public final void sendHelp() {
		Client.PRINTER.printMessage(names[0].concat(": \nDescription: ").concat(Client.INSTANCE.getClientColorCode()).concat(description).concat("\n§fUsage: ").concat(Client.INSTANCE.getClientColorCode()).concat(usage));
	}
	
	public abstract void fireCommand(String[] args);
	
	public boolean equalsInvoke(String text) {
		for (String string : names) {
			if (string.equalsIgnoreCase(text)) {
				return true;
			}
		}
		return false;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public final String getUsage() {
		return usage;
	}
	
}
