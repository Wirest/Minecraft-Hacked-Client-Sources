package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.rigamortis.faurax.module.modules.world.*;
import me.rigamortis.faurax.*;
import me.cupboard.command.argument.*;
import net.minecraft.client.*;

public class CommandControl extends Command
{
    public CommandControl() {
        super("Control", new String[0]);
    }
    
    @Argument(handles = { "mode" })
    protected String mode(final int mode) {
        if (mode < 0 || mode > 1) {
            return "Error, value can only be 0 or 1";
        }
        final String type = (mode == 0) ? "Survival" : "Creative";
        Control.mode.setSelectedOption(type);
        Client.getConfig().saveConfig();
        return "Control set to " + type;
    }
    
    @Argument
    protected String positions(final String mode) {
        final String pos = String.valueOf((int)Minecraft.getMinecraft().thePlayer.posX) + ", " + (int)Minecraft.getMinecraft().thePlayer.posY + ", " + (int)Minecraft.getMinecraft().thePlayer.posZ;
        if (mode.contains("pos1")) {
            Control.pos1 = Minecraft.getMinecraft().thePlayer.getPositionVector();
            return "Position 1 set to " + pos;
        }
        if (mode.contains("pos2")) {
            Control.pos2 = Minecraft.getMinecraft().thePlayer.getPositionVector();
            return "Position 2 set to " + pos;
        }
        return "Incorrect Syntax.";
    }
}
