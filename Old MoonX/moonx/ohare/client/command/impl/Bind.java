package moonx.ohare.client.command.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import org.lwjgl.input.Keyboard;

import java.util.Objects;


public class Bind extends Command {

	public Bind() {
		super("Bind",new String[]{"bind","b"});
	}

	@Override
	public void onRun(final String[] args) {
		if (args.length == 2) {
			if (args[1].toLowerCase().equals("resetall")) {
				Moonx.INSTANCE.getModuleManager().getModuleMap().values().forEach(m -> m.setKeybind(0));
				Moonx.INSTANCE.getNotificationManager().addNotification("Reset all keybinds.", 2000);
				return;
			}
		}
		if (args.length == 3) {
			String moduleName = args[1];
			Module module = Moonx.INSTANCE.getModuleManager().getModule(moduleName);
			if (module != null) {
				int keyCode = Keyboard.getKeyIndex(args[2].toUpperCase());
				if (keyCode != -1) {
					module.setKeybind(keyCode);
					Printer.print(module.getLabel() + " is now bound to \"" + Keyboard.getKeyName(keyCode) + "\".");
					Moonx.INSTANCE.getNotificationManager().addNotification((Objects.nonNull(module.getRenderLabel()) ? module.getRenderLabel():module.getLabel()) + " is now bound to \"" + Keyboard.getKeyName(keyCode) + "\".", 2000);
				} else {
					Printer.print("That is not a valid key code.");
				}
			} else {
				Moonx.INSTANCE.getNotificationManager().addNotification("That module does not exist.", 2000);
                Printer.print("That module does not exist.");
				Printer.print("Type \"modules\" for a list of all modules.");
			}
		} else {
			Printer.print("Invalid arguments.");
			Printer.print("Usage: \"bind [module] [key]\"");
		}
	}
}
