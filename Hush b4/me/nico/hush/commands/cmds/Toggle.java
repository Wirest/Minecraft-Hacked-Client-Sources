// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands.cmds;

import java.util.Iterator;
import me.nico.hush.modules.Module;
import me.nico.hush.Client;
import me.nico.hush.commands.Command;

public class Toggle extends Command
{
    public Toggle() {
        super("toggle", "to toggle a module.");
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            Command.messageWithPrefix(String.valueOf(Client.instance.commandManager.Chat_Prefix) + "toggle §f<Module>");
            return;
        }
        final String module = args[0];
        if (module.equalsIgnoreCase("setup")) {
            for (final Module m : Client.instance.moduleManager.getModules()) {
                m.isEnabled();
            }
            Command.messageWithPrefix("...");
            return;
        }
        try {
            Client.instance.moduleManager.getModuleName(module).toggle();
            Command.messageWithPrefix(String.valueOf(Client.instance.moduleManager.getModuleName(module).getDisplayname()) + " §fwas " + (Client.instance.moduleManager.getModuleName(module).isEnabled() ? "§aenabled" : "§cdisabled"));
        }
        catch (Exception e) {
            Command.messageWithPrefix("§cModule not found!");
        }
    }
}
