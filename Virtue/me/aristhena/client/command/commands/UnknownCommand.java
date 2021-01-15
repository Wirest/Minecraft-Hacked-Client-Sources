// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.command.commands;

import me.aristhena.utils.ClientUtils;
import me.aristhena.client.command.Com;
import me.aristhena.client.command.Command;

@Com(names = { "" })
public class UnknownCommand extends Command
{
    @Override
    public void runCommand(final String[] args) {
        ClientUtils.sendMessage("Unknown Command. Type \"help\" for a list of commands.");
    }
}
