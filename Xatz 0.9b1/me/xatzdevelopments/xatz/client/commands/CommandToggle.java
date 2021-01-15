package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.module.Module;

public class CommandToggle extends Command {

	@Override
	public void run(String[] commands) {
		String name = "";
		for (int i = 0; i < commands.length; i++) {
			if (i == 0) {
				continue;
			}
			name += commands[i];
			name += " ";
		}
		name = name.trim();
		Module module = Xatz.getModuleByName(name);
		if (module == null) {
			Xatz.getNotificationManager()
			.addNotification(new Notification(Level.ERROR, "Could not find module \"" + name + "\"!"));
			return;
		}
		module.toggle();

		if (module.isToggled()) {
			if (!(ClientSettings.notificationModulesEnable && Xatz.getModuleByName("Notifications").isToggled())) {
				Xatz.getNotificationManager()
						.addNotification(new Notification(Level.INFO, "Module " + module.getName() + " was enabled!"));
			}

		} else {
			if (!(ClientSettings.notificationModulesDisable && Xatz.getModuleByName("Notifications").isToggled())) {
				Xatz.getNotificationManager()
						.addNotification(new Notification(Level.INFO, "Module " + module.getName() + " was disabled!"));
			}
		}
	}

	@Override
	public String getActivator() {
		return ".t";
	}

	@Override
	public String getSyntax() {
		return ".t <module>";
	}

	@Override
	public String getDesc() {
		return "Toggles a module";
	}
}
