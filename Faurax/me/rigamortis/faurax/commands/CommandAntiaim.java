package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.misc.*;
import me.rigamortis.faurax.*;

public class CommandAntiaim extends Command
{
    public CommandAntiaim() {
        super("AntiAim", new String[] { "aa" });
    }
    
    @Argument
    protected String antiAimHelp() {
        return "Sneak <0 - 1>, Head <0 - 1>, Rotate <0 - 1>";
    }
    
    @Argument(handles = { "sneak", "s" })
    protected String antiAimSneak(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 - 1";
        }
        AntiAim.sneak.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "AntiAim §bSneak§f set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "head", "h" })
    protected String antiAimHead(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 - 1";
        }
        AntiAim.noHead.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "AntiAim §bNo Head§f set to §6" + mode + "§f";
    }
    
    @Argument(handles = { "rotate", "r" })
    protected String antiAimRotations(final int mode) {
        if (mode > 1 || mode < 0) {
            return "Error, value can only be 0 - 1";
        }
        AntiAim.rotation.setBooleanValue(mode == 1);
        Client.getConfig().saveConfig();
        return "AntiAim §bRotation§f set to §6" + mode + "§f";
    }
}
