// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.util.BlockPos;
import java.util.List;

public interface ICommand extends Comparable<ICommand>
{
    String getCommandName();
    
    String getCommandUsage(final ICommandSender p0);
    
    List<String> getCommandAliases();
    
    void processCommand(final ICommandSender p0, final String[] p1) throws CommandException;
    
    boolean canCommandSenderUseCommand(final ICommandSender p0);
    
    List<String> addTabCompletionOptions(final ICommandSender p0, final String[] p1, final BlockPos p2);
    
    boolean isUsernameIndex(final String[] p0, final int p1);
}
