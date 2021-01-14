package com.etb.client.command.impl;

import com.etb.client.command.Command;
import org.lwjgl.input.Keyboard;

import com.etb.client.Client;
import com.etb.client.gui.notification.Notification;
import com.etb.client.module.Module;
import com.etb.client.utils.Printer;


public class Bind extends Command {

	public Bind() {
		super("Bind",new String[]{"bind","b"},"Binds cheats to keys");
	}

	@Override
	public void onRun(final String[] args) {
		if (args.length == 2) {
			if (args[1].toLowerCase().equals("resetall")) {
				Client.INSTANCE.getModuleManager().getModuleMap().values().forEach(m -> {
					m.setKeybind(0);
				});
                Client.INSTANCE.getNotificationManager().sendClientMessage("Reset all keybinds.", Notification.Type.SUCCESS);
                return;
			}
		}
		if (args.length == 3) {
			String moduleName = args[1];
			Module module = Client.INSTANCE.getModuleManager().getModule(moduleName);
			if (module != null) {
				int keyCode = Keyboard.getKeyIndex(args[2].toUpperCase());
				if (keyCode != -1) {
					module.setKeybind(keyCode);
					Printer.print(module.getLabel() + " is now bound to \"" + Keyboard.getKeyName(keyCode) + "\".");
					Client.INSTANCE.getNotificationManager().sendClientMessage(module.getLabel() + " is now bound to \"" + Keyboard.getKeyName(keyCode) + "\".", Notification.Type.SUCCESS);
				} else {
					Printer.print("That is not a valid key code.");
				}
			} else {
                Client.INSTANCE.getNotificationManager().sendClientMessage("That module does not exist.", Notification.Type.ERROR);

                Printer.print("That module does not exist.");
				Printer.print("Type \"modules\" for a list of all modules.");
			}
		} else {
			Printer.print("Invalid arguments.");
			Printer.print("Usage: \"bind [module] [key]\"");
		}
	}
}
