// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CommandKill extends CommandBase
{
    @Override
    public String getCommandName() {
        return "kill";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.kill.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length == 0) {
            final EntityPlayer entityplayer = CommandBase.getCommandSenderAsPlayer(sender);
            entityplayer.onKillCommand();
            CommandBase.notifyOperators(sender, this, "commands.kill.successful", entityplayer.getDisplayName());
        }
        else {
            final Entity entity = CommandBase.func_175768_b(sender, args[0]);
            entity.onKillCommand();
            CommandBase.notifyOperators(sender, this, "commands.kill.successful", entity.getDisplayName());
        }
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
