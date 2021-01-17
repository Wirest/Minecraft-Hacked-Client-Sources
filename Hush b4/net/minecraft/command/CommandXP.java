// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;

public class CommandXP extends CommandBase
{
    @Override
    public String getCommandName() {
        return "xp";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.xp.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
        String s = args[0];
        final boolean flag = s.endsWith("l") || s.endsWith("L");
        if (flag && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        int i = CommandBase.parseInt(s);
        final boolean flag2 = i < 0;
        if (flag2) {
            i *= -1;
        }
        final EntityPlayer entityplayer = (args.length > 1) ? CommandBase.getPlayer(sender, args[1]) : CommandBase.getCommandSenderAsPlayer(sender);
        if (flag) {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);
            if (flag2) {
                entityplayer.addExperienceLevel(-i);
                CommandBase.notifyOperators(sender, this, "commands.xp.success.negative.levels", i, entityplayer.getName());
            }
            else {
                entityplayer.addExperienceLevel(i);
                CommandBase.notifyOperators(sender, this, "commands.xp.success.levels", i, entityplayer.getName());
            }
        }
        else {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);
            if (flag2) {
                throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }
            entityplayer.addExperience(i);
            CommandBase.notifyOperators(sender, this, "commands.xp.success", i, entityplayer.getName());
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : null;
    }
    
    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 1;
    }
}
