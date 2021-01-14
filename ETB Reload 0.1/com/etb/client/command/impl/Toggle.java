package com.etb.client.command.impl;

import com.etb.client.command.Command;
import com.etb.client.Client;
import com.etb.client.gui.notification.Notification;
import com.etb.client.module.Module;
import com.etb.client.utils.Printer;

public class Toggle extends Command {

	public Toggle() {
		super("T",new String[]{"t","toggle"},"Toggle modules");
	}

	@Override
	public void onRun(final String[] s) {
		if (s.length <= 1) {
			Printer.print("Not enough args.");
			return;
		}
			for (Module m : Client.INSTANCE.getModuleManager().getModuleMap().values()) {
				if (m.getLabel().toLowerCase().equals(s[1])) {
					m.toggle();
                    Client.INSTANCE.getNotificationManager().sendClientMessage("Toggled " + m.getLabel(), Notification.Type.SUCCESS);

                    Printer.print("Toggled " + m.getLabel());
					break;
				}
			}
	}
}
