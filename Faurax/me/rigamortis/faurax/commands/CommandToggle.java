package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.module.*;
import java.util.*;
import me.cupboard.command.argument.*;

public class CommandToggle extends Command
{
    public CommandToggle() {
        super("toggle", new String[] { "t" });
    }
    
    @Argument
    protected String toggle(final String name) {
        if (name != null) {
            for (final Module mod : Client.getModules().mods) {
                if (mod.getName().equalsIgnoreCase(name)) {
                    String state = "";
                    if (!mod.isToggled()) {
                        state = "on";
                    }
                    else {
                        state = "off";
                    }
                    mod.toggle();
                    Client.getConfig().saveMods();
                    return "Toggled §b" + name + " §f" + state + "§f";
                }
            }
        }
        return "";
    }
}
