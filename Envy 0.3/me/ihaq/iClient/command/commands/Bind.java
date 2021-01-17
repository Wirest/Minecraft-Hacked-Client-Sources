package me.ihaq.iClient.command.commands;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.command.Command;
import me.ihaq.iClient.modules.Module;

public class Bind extends Command {
	public Bind() {
		super("Bind", "<Module> <Key>");
	}

	@Override
	public void run(final String message) {
		final Module m = Module.getModuleByString(message.split(" ")[1]);
		if (message.split(" ").length == 3) {
			if (message.split(" ")[1].equalsIgnoreCase(m.name)) {
				if (Keyboard.getKeyIndex(message.split(" ")[2].toUpperCase()) == 0) {
					m.setKeyCode(0);
					Envy.tellPlayer(
							"The Bind for " + m.name + " has been set to " + Keyboard.getKeyName(m.keyCode) + ".");
				} else {
					m.setKeyCode(Keyboard.getKeyIndex(message.split(" ")[2].toUpperCase()));
					Envy.tellPlayer(
							"The Bind for " + m.name + " has been set to " + Keyboard.getKeyName(m.keyCode) + ".");
				}
				Envy.FILE_MANAGER.saveFiles();
			}
		} else {
			Envy.tellPlayer("Incorrect Syntax! bind <Module> <Bind>");
		}
	}
}