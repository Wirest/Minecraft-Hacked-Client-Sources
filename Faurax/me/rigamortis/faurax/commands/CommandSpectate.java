package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import java.util.*;
import me.cupboard.command.argument.*;

public class CommandSpectate extends Command
{
    public CommandSpectate() {
        super("Spectate", new String[] { "spec" });
    }
    
    @Argument
    protected String spectate(final String name) {
        for (final Object i : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (i instanceof EntityPlayer) {
                final EntityPlayer ep = (EntityPlayer)i;
                if (!ep.getName().equalsIgnoreCase(name)) {
                    continue;
                }
                Minecraft.getMinecraft().field_175622_Z = ep;
            }
        }
        return "Now Spectating " + name + ".";
    }
}
