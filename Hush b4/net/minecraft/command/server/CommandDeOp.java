// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.List;
import net.minecraft.util.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandDeOp extends CommandBase
{
    @Override
    public String getCommandName() {
        return "deop";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.deop.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.deop.usage", new Object[0]);
        }
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        final GameProfile gameprofile = minecraftserver.getConfigurationManager().getOppedPlayers().getGameProfileFromName(args[0]);
        if (gameprofile == null) {
            throw new CommandException("commands.deop.failed", new Object[] { args[0] });
        }
        minecraftserver.getConfigurationManager().removeOp(gameprofile);
        CommandBase.notifyOperators(sender, this, "commands.deop.success", args[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames()) : null;
    }
}
