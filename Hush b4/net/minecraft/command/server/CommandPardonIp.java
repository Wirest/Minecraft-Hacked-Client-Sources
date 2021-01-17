// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.management.UserList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import java.util.regex.Matcher;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.ICommand;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandPardonIp extends CommandBase
{
    @Override
    public String getCommandName() {
        return "pardon-ip";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(sender);
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.unbanip.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 1) {
            throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        }
        final Matcher matcher = CommandBanIp.field_147211_a.matcher(args[0]);
        if (matcher.matches()) {
            ((UserList<String, V>)MinecraftServer.getServer().getConfigurationManager().getBannedIPs()).removeEntry(args[0]);
            CommandBase.notifyOperators(sender, this, "commands.unbanip.success", args[0]);
            return;
        }
        throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys()) : null;
    }
}
