// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityCommandBlock;

public class CommandStats extends CommandBase
{
    @Override
    public String getCommandName() {
        return "stats";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.stats.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        boolean flag;
        if (args[0].equals("entity")) {
            flag = false;
        }
        else {
            if (!args[0].equals("block")) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            flag = true;
        }
        int i;
        if (flag) {
            if (args.length < 5) {
                throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
            }
            i = 4;
        }
        else {
            if (args.length < 3) {
                throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
            }
            i = 2;
        }
        final String s = args[i++];
        if ("set".equals(s)) {
            if (args.length < i + 3) {
                if (i == 5) {
                    throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
        }
        else {
            if (!"clear".equals(s)) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            if (args.length < i + 1) {
                if (i == 5) {
                    throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
            }
        }
        final CommandResultStats.Type commandresultstats$type = CommandResultStats.Type.getTypeByName(args[i++]);
        if (commandresultstats$type == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        CommandResultStats commandresultstats;
        if (flag) {
            final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 1, false);
            final TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null) {
                throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
            if (tileentity instanceof TileEntityCommandBlock) {
                commandresultstats = ((TileEntityCommandBlock)tileentity).getCommandResultStats();
            }
            else {
                if (!(tileentity instanceof TileEntitySign)) {
                    throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
                }
                commandresultstats = ((TileEntitySign)tileentity).getStats();
            }
        }
        else {
            final Entity entity = CommandBase.func_175768_b(sender, args[1]);
            commandresultstats = entity.getCommandStats();
        }
        if ("set".equals(s)) {
            final String s2 = args[i++];
            final String s3 = args[i];
            if (s2.length() == 0 || s3.length() == 0) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            CommandResultStats.func_179667_a(commandresultstats, commandresultstats$type, s2, s3);
            CommandBase.notifyOperators(sender, this, "commands.stats.success", commandresultstats$type.getTypeName(), s3, s2);
        }
        else if ("clear".equals(s)) {
            CommandResultStats.func_179667_a(commandresultstats, commandresultstats$type, null, null);
            CommandBase.notifyOperators(sender, this, "commands.stats.cleared", commandresultstats$type.getTypeName());
        }
        if (flag) {
            final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 1, false);
            final TileEntity tileentity2 = world.getTileEntity(blockpos2);
            tileentity2.markDirty();
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "entity", "block") : ((args.length == 2 && args[0].equals("entity")) ? CommandBase.getListOfStringsMatchingLastWord(args, this.func_175776_d()) : ((args.length >= 2 && args.length <= 4 && args[0].equals("block")) ? CommandBase.func_175771_a(args, 1, pos) : (((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block"))) ? (((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block"))) ? (((args.length != 6 || !args[0].equals("entity")) && (args.length != 8 || !args[0].equals("block"))) ? null : CommandBase.getListOfStringsMatchingLastWord(args, this.func_175777_e())) : CommandBase.getListOfStringsMatchingLastWord(args, CommandResultStats.Type.getTypeNames())) : CommandBase.getListOfStringsMatchingLastWord(args, "set", "clear"))));
    }
    
    protected String[] func_175776_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    protected List<String> func_175777_e() {
        final Collection<ScoreObjective> collection = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final ScoreObjective scoreobjective : collection) {
            if (!scoreobjective.getCriteria().isReadOnly()) {
                list.add(scoreobjective.getName());
            }
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }
}
