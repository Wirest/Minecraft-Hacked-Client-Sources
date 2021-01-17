// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands.cmds;

import me.nico.hush.Client;
import me.nico.hush.commands.Command;

public class Help extends Command
{
    public Help() {
        super("help", "to see all informations.");
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 1) {
            Command.messageWithPrefix("§f§m-----------");
            Command.messageWithPrefix("§fInformations:");
            Command.messageWithPrefix(" ");
            Command.messageWithPrefix("§fZodiac Client");
            Command.messageWithPrefix("§fVersion " + Client.instance.ClientVersion);
            Command.messageWithPrefix("§fCoded by " + Client.instance.ClientCoder);
            Command.messageWithPrefix("§f§m----------------------------");
            Command.messageWithPrefix("§f.toggle - To toggle a module.");
            Command.messageWithPrefix("§f.bind - To bind a module.");
            Command.messageWithPrefix("§f.config - To load a config.");
            Command.messageWithPrefix("§f.clip - To glitch through a block.");
            Command.messageWithPrefix("§f.ign - To copy your ingamename.");
            Command.messageWithPrefix("§f§m----------------------------");
        }
        else {
            Command.messageWithPrefix("§cPlease only use §f.help§c!");
        }
    }
}
