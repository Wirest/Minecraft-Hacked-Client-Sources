// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands.cmds;

import me.nico.hush.Client;
import me.nico.hush.commands.Command;

public class Settings extends Command
{
    public Settings() {
        super("settings", "to load a script");
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            Command.messageWithPrefix(String.valueOf(Client.instance.commandManager.Chat_Prefix) + "settings save");
            return;
        }
        if (args[0].equalsIgnoreCase("save")) {
            Command.messageWithPrefix(" ");
            Command.messageWithPrefix("§aYour script was saved.");
            Command.messageWithPrefix(" ");
            Client.instance.fileManager.saveSettings();
        }
    }
}
