package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.render.*;
import me.rigamortis.faurax.*;

public class CommandChams extends Command
{
    public CommandChams() {
        super("Chams", new String[] { "chameleon" });
    }
    
    @Argument
    protected String chamsHelp() {
        final String help = "Mobs <0 - 1>, Animals <0 - 1>, Players <0 - 1>, Items <0 - 1>, Chests <0 - 1>";
        return help;
    }
    
    @Argument(handles = { "players", "p" })
    protected String chamsPlayers(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        Chams.players.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Chams §bPlayers §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "animals", "a" })
    protected String chamsAnimals(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        Chams.animals.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Chams §bAnimals §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "mobs", "m" })
    protected String chamsMobs(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        Chams.mobs.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Chams §bMobs §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "items", "i" })
    protected String chamsItems(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        Chams.items.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Chams §bItems §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "chests", "c" })
    protected String chamsChests(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        Chams.chests.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "Chams §bChests §fset to §6" + mode + "§f";
    }
}
