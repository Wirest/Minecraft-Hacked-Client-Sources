// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandOp extends CommandBase
{
    @Override
    public String getCommandName() {
        return "op";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.op.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        final GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[0]);
        if (gameprofile == null) {
            throw new CommandException("commands.op.failed", new Object[] { args[0] });
        }
        minecraftserver.getConfigurationManager().addOp(gameprofile);
        CommandBase.notifyOperators(sender, this, "commands.op.success", args[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            final String s = args[args.length - 1];
            final List<String> list = (List<String>)Lists.newArrayList();
            GameProfile[] gameProfiles;
            for (int length = (gameProfiles = MinecraftServer.getServer().getGameProfiles()).length, i = 0; i < length; ++i) {
                final GameProfile gameprofile = gameProfiles[i];
                if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(gameprofile) && CommandBase.doesStringStartWith(s, gameprofile.getName())) {
                    list.add(gameprofile.getName());
                }
            }
            return list;
        }
        return null;
    }
}
