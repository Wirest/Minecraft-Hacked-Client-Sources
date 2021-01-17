package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.world.*;
import me.rigamortis.faurax.*;

public class CommandAutoFarm extends Command
{
    public CommandAutoFarm() {
        super("AutoFarm", new String[] { "af" });
    }
    
    @Argument
    protected String autoFarmHelp() {
        final String help = "Type <0 - 3>";
        return help;
    }
    
    @Argument(handles = { "type", "t" })
    protected String type(final int type) {
        if (type > 3 || type < 0) {
            return "Error, value can only be 0 - 3";
        }
        String name = "";
        if (type == 0) {
            name = "Seeds";
        }
        if (type == 1) {
            name = "Tilling";
        }
        if (type == 2) {
            name = "NetherWart";
        }
        if (type == 3) {
            name = "Cactus";
        }
        AutoFarm.mode.setStringValue(name);
        Client.getConfig().saveConfig();
        return "AutoFarm §bType§f set to §6" + name + "§f";
    }
}
