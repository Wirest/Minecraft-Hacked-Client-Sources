package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import me.cupboard.command.argument.*;

public class CommandVclip extends Command
{
    public CommandVclip() {
        super("vclip", new String[] { "vc" });
    }
    
    @Argument
    protected String vclip(final double pos) {
        Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + pos, Minecraft.getMinecraft().thePlayer.posZ);
        return "Vclipped " + (float)pos;
    }
}
