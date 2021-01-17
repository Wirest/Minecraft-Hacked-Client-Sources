package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.world.*;
import me.rigamortis.faurax.*;

public class CommandChestStealer extends Command
{
    public CommandChestStealer() {
        super("ChestStealer", new String[] { "cs" });
    }
    
    @Argument
    protected String chestStealerHelp() {
        final String help = "AutoClose §a<§f0 - 1§a>§f, StealDelay §a<§fFloat§a>§f, AutoDrop §a<§f0 - 1§a>§f, AutoOpen §a<§f0 - 1§a>§f, Silent §a<§f0 - 1§a>§f";
        return help;
    }
    
    @Argument(handles = { "autoclose", "ac" })
    protected String autoClose(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        ChestStealer.autoClose.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "ChestStealer §bAuto Close§f set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "autoopen", "ao" })
    protected String autoOpen(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        ChestStealer.autoOpen.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "ChestStealer §bAuto Open§f set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "silent", "s" })
    protected String silent(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        ChestStealer.silent.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "ChestStealer §bSilent§f set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "stealdelay", "sd" })
    protected String stealDelay(final int time) {
        ChestStealer.stealDelayValue.setFloatValue(time);
        Client.getConfig().saveConfig();
        return "ChestStealer §bSteal Delay§f set to §6" + time + "§f";
    }
    
    @Argument(handles = { "autodrop", "ad" })
    protected String autoDropItems(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 or 1";
        }
        ChestStealer.autoDrop.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "ChestStealer §bAuto Drop§f mode set to §6" + mode + "§f";
    }
}
