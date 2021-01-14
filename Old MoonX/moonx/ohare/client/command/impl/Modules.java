package moonx.ohare.client.command.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.utils.Printer;

public class Modules extends Command {

    public Modules() {
        super("Modules", new String[]{"modules","mods","m"});
    }

    @Override
    public void onRun(final String[] s) {
        StringBuilder mods = new StringBuilder("Modules (" + Moonx.INSTANCE.getModuleManager().getModuleMap().values().size() + "): ");
        Moonx.INSTANCE.getModuleManager().getModuleMap().values()
                .forEach(mod -> mods.append(mod.isEnabled() ? "\247a" : "\247c").append(mod.getLabel()).append("\247r, "));
        Printer.print(mods.toString().substring(0, mods.length() - 2));
    }
}