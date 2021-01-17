// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.world.WorldSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandPublishLocalServer extends CommandBase
{
    @Override
    public String getCommandName() {
        return "publish";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.publish.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final String s = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);
        if (s != null) {
            CommandBase.notifyOperators(sender, this, "commands.publish.started", s);
        }
        else {
            CommandBase.notifyOperators(sender, this, "commands.publish.failed", new Object[0]);
        }
    }
}
