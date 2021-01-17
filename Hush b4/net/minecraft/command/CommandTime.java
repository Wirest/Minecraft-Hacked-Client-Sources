// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.world.WorldServer;
import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;

public class CommandTime extends CommandBase
{
    @Override
    public String getCommandName() {
        return "time";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.time.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 1) {
            if (args[0].equals("set")) {
                int l;
                if (args[1].equals("day")) {
                    l = 1000;
                }
                else if (args[1].equals("night")) {
                    l = 13000;
                }
                else {
                    l = CommandBase.parseInt(args[1], 0);
                }
                this.setTime(sender, l);
                CommandBase.notifyOperators(sender, this, "commands.time.set", l);
                return;
            }
            if (args[0].equals("add")) {
                final int k = CommandBase.parseInt(args[1], 0);
                this.addTime(sender, k);
                CommandBase.notifyOperators(sender, this, "commands.time.added", k);
                return;
            }
            if (args[0].equals("query")) {
                if (args[1].equals("daytime")) {
                    final int j = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
                    CommandBase.notifyOperators(sender, this, "commands.time.query", j);
                    return;
                }
                if (args[1].equals("gametime")) {
                    final int i = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
                    CommandBase.notifyOperators(sender, this, "commands.time.query", i);
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "set", "add", "query") : ((args.length == 2 && args[0].equals("set")) ? CommandBase.getListOfStringsMatchingLastWord(args, "day", "night") : ((args.length == 2 && args[0].equals("query")) ? CommandBase.getListOfStringsMatchingLastWord(args, "daytime", "gametime") : null));
    }
    
    protected void setTime(final ICommandSender p_71552_1_, final int p_71552_2_) {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
            MinecraftServer.getServer().worldServers[i].setWorldTime(p_71552_2_);
        }
    }
    
    protected void addTime(final ICommandSender p_71553_1_, final int p_71553_2_) {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
            final WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
            worldserver.setWorldTime(worldserver.getWorldTime() + p_71553_2_);
        }
    }
}
