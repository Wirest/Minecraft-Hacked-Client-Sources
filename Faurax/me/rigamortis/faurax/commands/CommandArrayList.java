package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.hooks.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.module.*;
import java.util.*;

public class CommandArrayList extends Command
{
    public CommandArrayList() {
        super("ArrayList", new String[] { "al", "array" });
    }
    
    @Argument
    protected String arrayListHelp() {
        final String help = "Add <Module Name>, Remove <Module Name>, Color <Module Name> <Hex Color>, Name <Module Name> <New Name>, ShowInfo <0 - 1>";
        return help;
    }
    
    @Argument(handles = { "showinfo", "si" })
    protected String arrayListInfo(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 - 1";
        }
        if (mode == 1) {
            GuiIngameHook.showInfo = true;
            return "ArrayList will now show info.";
        }
        if (mode == 0) {
            GuiIngameHook.showInfo = false;
            return "ArrayList will no longer show info.";
        }
        return "";
    }
    
    @Argument(handles = { "add", "a" })
    protected String arrayListAdd(final String name) {
        for (final Module mod : Client.getModules().mods) {
            if (mod.getName().equalsIgnoreCase(name)) {
                if (mod.isVisible()) {
                    return String.valueOf(name) + " is already visible";
                }
                mod.setVisible(true);
                return String.valueOf(name) + " is now visible";
            }
        }
        return "";
    }
    
    @Argument(handles = { "remove", "rem", "r", "del", "d", "delete" })
    protected String arrayListRemove(final String name) {
        for (final Module mod : Client.getModules().mods) {
            if (mod.getName().equalsIgnoreCase(name)) {
                if (mod.isVisible()) {
                    mod.setVisible(false);
                    return String.valueOf(name) + " is no longer visible";
                }
                return String.valueOf(name) + " is not visible";
            }
        }
        return "";
    }
    
    @Argument(handles = { "color", "c" })
    protected String arrayListColor(final String name, final int color) {
        for (final Module mod : Client.getModules().mods) {
            if (mod.getName().equalsIgnoreCase(name)) {
                mod.setColor(color);
                return String.valueOf(name) + "Color has been set to " + color;
            }
        }
        return "";
    }
    
    @Argument(handles = { "name", "n" })
    protected String arrayListName(final String name, final String newName) {
        for (final Module mod : Client.getModules().mods) {
            if (mod.getName().equalsIgnoreCase(name)) {
                mod.setName(newName);
                return String.valueOf(name) + "'s Name has been set to " + newName;
            }
        }
        return "";
    }
}
