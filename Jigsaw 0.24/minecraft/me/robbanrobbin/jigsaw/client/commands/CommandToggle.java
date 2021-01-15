package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.module.Module;

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
		Module module = Jigsaw.getModuleByName(name);
		if (module == null) {
			Jigsaw.getNotificationManager()
			.addNotification(new Notification(Level.ERROR, "Could not find module \"" + name + "\"!"));
			return;
		}
		module.toggle();

		if (module.isToggled()) {
			if (!(ClientSettings.notificationModulesEnable && Jigsaw.getModuleByName("Notifications").isToggled())) {
				Jigsaw.getNotificationManager()
						.addNotification(new Notification(Level.INFO, "Module " + module.getName() + " was enabled!"));
			}

		} else {
			if (!(ClientSettings.notificationModulesDisable && Jigsaw.getModuleByName("Notifications").isToggled())) {
				Jigsaw.getNotificationManager()
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
