// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase
{
    @Override
    public String getCommandName() {
        return "setidletimeout";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.setidletimeout.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        }
        final int i = CommandBase.parseInt(args[0], 0);
        MinecraftServer.getServer().setPlayerIdleTimeout(i);
        CommandBase.notifyOperators(sender, this, "commands.setidletimeout.success", i);
    }
}
