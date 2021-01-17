package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import java.util.*;

public class CommandBind extends Command
{
    public CommandBind() {
        super("bind", new String[] { "b" });
    }
    
    @Argument
    protected String bindHelp() {
        final String help = "Add <Module> <Key>, Remove <Module>";
        return help;
    }
    
    @Argument(handles = { "add", "a" })
    protected String bindAdd(final String name, final String bind) {
        if (name != "" || bind != "") {
            for (final Module mod : Client.getModules().mods) {
                if (name.equalsIgnoreCase(mod.getName())) {
                    mod.setKey(bind.toUpperCase());
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§fBound " + name + " to " + bind.toUpperCase() + "."));
                    Client.getConfig().saveBinds();
                    break;
                }
            }
        }
        return "";
    }
    
    @Argument(handles = { "remove", "r", "del", "rem", "delete" })
    protected String bindRemove(final String name) {
        if (name != "") {
            for (final Module mod : Client.getModules().mods) {
                if (name.equalsIgnoreCase(mod.getName())) {
                    mod.setKey(null);
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§fRemoved key."));
                    Client.getConfig().saveBinds();
                    break;
                }
            }
        }
        return "";
    }
}
