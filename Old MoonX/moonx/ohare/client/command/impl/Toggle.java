package moonx.ohare.client.command.impl;


import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;

import java.util.Objects;

public class Toggle extends Command {

	public Toggle() {
		super("Toggle",new String[]{"t","toggle"});
	}

	@Override
	public void onRun(final String[] s) {
		if (s.length <= 1) {
			Printer.print("Not enough args.");
			return;
		}
			for (Module m : Moonx.INSTANCE.getModuleManager().getModuleMap().values()) {
				if (m.getLabel().toLowerCase().equals(s[1])) {
					m.toggle();
					Moonx.INSTANCE.getNotificationManager().addNotification("Toggled " + (Objects.nonNull(m.getRenderLabel()) ? m.getRenderLabel():m.getLabel()), 2000);
                    Printer.print("Toggled " + m.getLabel());
					break;
				}
			}
	}
}
