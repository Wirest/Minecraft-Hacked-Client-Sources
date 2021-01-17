// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.management.UserList;
import java.util.List;
import net.minecraft.util.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandPardonPlayer extends CommandBase
{
    @Override
    public String getCommandName() {
        return "pardon";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.unban.usage";
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer() && super.canCommandSenderUseCommand(sender);
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.unban.usage", new Object[0]);
        }
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        final GameProfile gameprofile = minecraftserver.getConfigurationManager().getBannedPlayers().isUsernameBanned(args[0]);
        if (gameprofile == null) {
            throw new CommandException("commands.unban.failed", new Object[] { args[0] });
        }
        ((UserList<GameProfile, V>)minecraftserver.getConfigurationManager().getBannedPlayers()).removeEntry(gameprofile);
        CommandBase.notifyOperators(sender, this, "commands.unban.success", args[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys()) : null;
    }
}
