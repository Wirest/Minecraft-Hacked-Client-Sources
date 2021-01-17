// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandStop extends CommandBase
{
    @Override
    public String getCommandName() {
        return "stop";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.stop.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (MinecraftServer.getServer().worldServers != null) {
            CommandBase.notifyOperators(sender, this, "commands.stop.start", new Object[0]);
        }
        MinecraftServer.getServer().initiateShutdown();
    }
}
