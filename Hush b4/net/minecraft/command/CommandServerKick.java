// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandServerKick extends CommandBase
{
    @Override
    public String getCommandName() {
        return "kick";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.kick.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0 || args[0].length() <= 1) {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
        final EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
        String s = "Kicked by an operator.";
        boolean flag = false;
        if (entityplayermp == null) {
            throw new PlayerNotFoundException();
        }
        if (args.length >= 2) {
            s = CommandBase.getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
            flag = true;
        }
        entityplayermp.playerNetServerHandler.kickPlayerFromServer(s);
        if (flag) {
            CommandBase.notifyOperators(sender, this, "commands.kick.success.reason", entityplayermp.getName(), s);
        }
        else {
            CommandBase.notifyOperators(sender, this, "commands.kick.success", entityplayermp.getName());
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length >= 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
