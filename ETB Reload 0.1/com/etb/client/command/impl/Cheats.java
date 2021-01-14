package com.etb.client.command.impl;

import com.etb.client.command.Command;
import com.etb.client.Client;
import com.etb.client.utils.Printer;

public class Cheats extends Command {

    public Cheats() {
        super("Cheats",new String[]{"mods","cheats","c","modules","m"},"Lists all cheats in the client");
    }

    @Override
    public void onRun(final String[] s) {
        StringBuilder mods = new StringBuilder("Cheats (" + Client.INSTANCE.getModuleManager().getModuleMap().values().size() + "): ");
        Client.INSTANCE.getModuleManager().getModuleMap().values()
                .forEach(mod -> mods.append(mod.isEnabled() ? "\247a" : "\247c").append(mod.getLabel()).append("\247r, "));
        Printer.print(mods.toString().substring(0, mods.length() - 2));
    }
}