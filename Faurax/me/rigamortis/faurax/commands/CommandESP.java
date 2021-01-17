package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.render.*;
import me.rigamortis.faurax.*;

public class CommandESP extends Command
{
    public CommandESP() {
        super("ESP", new String[0]);
    }
    
    @Argument
    protected String ESPHelp() {
        final String help = "Mobs §a<§f0 - 1§a>§f, Animals §a<§f0 - 1§a>§f, Players §a<§f0 - 1§a>§f, Mode §a<§f0 - 3§a>§f";
        return help;
    }
    
    @Argument(handles = { "players", "p" })
    protected String ESPPlayers(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        ESP.players.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "ESP §bPlayers §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "animals", "a" })
    protected String ESPAnimals(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        ESP.animals.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "ESP §bAnimals §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "mobs", "m" })
    protected String chamsMobs(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        ESP.mobs.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "ESP §bMobs §fset to §6" + mode + "§f";
    }
    
    @Argument(handles = { "mode" })
    protected String ESPMode(final int mode) {
        if (mode > 3 || mode < 0) {
            return "Error, value can only be 0 - 3";
        }
        String type = "";
        if (mode == 0) {
            type = "Outline";
        }
        if (mode == 1) {
            type = "2D";
        }
        if (mode == 2) {
            type = "Box";
        }
        if (mode == 3) {
            type = "2D Corner";
        }
        if (mode == 4) {
            type = "Bone";
        }
        ESP.mode.setSelectedOption(type);
        Client.getConfig().saveConfig();
        return "ESP §bMode §fset to §6" + mode + "§f";
    }
}
