// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.world.WorldServer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSaveOn extends CommandBase
{
    @Override
    public String getCommandName() {
        return "save-on";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.save-on.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        boolean flag = false;
        for (int i = 0; i < minecraftserver.worldServers.length; ++i) {
            if (minecraftserver.worldServers[i] != null) {
                final WorldServer worldserver = minecraftserver.worldServers[i];
                if (worldserver.disableLevelSaving) {
                    worldserver.disableLevelSaving = false;
                    flag = true;
                }
            }
        }
        if (flag) {
            CommandBase.notifyOperators(sender, this, "commands.save.enabled", new Object[0]);
            return;
        }
        throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
    }
}
