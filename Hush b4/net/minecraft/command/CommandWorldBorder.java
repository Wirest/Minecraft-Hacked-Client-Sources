// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;

public class CommandWorldBorder extends CommandBase
{
    @Override
    public String getCommandName() {
        return "worldborder";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.worldborder.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        }
        final WorldBorder worldborder = this.getWorldBorder();
        if (args[0].equals("set")) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }
            final double d0 = worldborder.getTargetSize();
            final double d2 = CommandBase.parseDouble(args[1], 1.0, 6.0E7);
            final long i = (args.length > 2) ? (CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L;
            if (i > 0L) {
                worldborder.setTransition(d0, d2, i);
                if (d0 > d2) {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L));
                }
                else {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L));
                }
            }
            else {
                worldborder.setTransition(d2);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.set.success", String.format("%.1f", d2), String.format("%.1f", d0));
            }
        }
        else if (args[0].equals("add")) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }
            final double d3 = worldborder.getDiameter();
            final double d4 = d3 + CommandBase.parseDouble(args[1], -d3, 6.0E7 - d3);
            final long i2 = worldborder.getTimeUntilTarget() + ((args.length > 2) ? (CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L);
            if (i2 > 0L) {
                worldborder.setTransition(d3, d4, i2);
                if (d3 > d4) {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d4), String.format("%.1f", d3), Long.toString(i2 / 1000L));
                }
                else {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d4), String.format("%.1f", d3), Long.toString(i2 / 1000L));
                }
            }
            else {
                worldborder.setTransition(d4);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.set.success", String.format("%.1f", d4), String.format("%.1f", d3));
            }
        }
        else if (args[0].equals("center")) {
            if (args.length != 3) {
                throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }
            final BlockPos blockpos = sender.getPosition();
            final double d5 = CommandBase.parseDouble(blockpos.getX() + 0.5, args[1], true);
            final double d6 = CommandBase.parseDouble(blockpos.getZ() + 0.5, args[2], true);
            worldborder.setCenter(d5, d6);
            CommandBase.notifyOperators(sender, this, "commands.worldborder.center.success", d5, d6);
        }
        else if (args[0].equals("damage")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }
            if (args[1].equals("buffer")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                }
                final double d7 = CommandBase.parseDouble(args[2], 0.0);
                final double d8 = worldborder.getDamageBuffer();
                worldborder.setDamageBuffer(d7);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.damage.buffer.success", String.format("%.1f", d7), String.format("%.1f", d8));
            }
            else if (args[1].equals("amount")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                }
                final double d9 = CommandBase.parseDouble(args[2], 0.0);
                final double d10 = worldborder.getDamageAmount();
                worldborder.setDamageAmount(d9);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.damage.amount.success", String.format("%.2f", d9), String.format("%.2f", d10));
            }
        }
        else if (args[0].equals("warning")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }
            final int j = CommandBase.parseInt(args[2], 0);
            if (args[1].equals("time")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                }
                final int k = worldborder.getWarningTime();
                worldborder.setWarningTime(j);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.warning.time.success", j, k);
            }
            else if (args[1].equals("distance")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                }
                final int l = worldborder.getWarningDistance();
                worldborder.setWarningDistance(j);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.warning.distance.success", j, l);
            }
        }
        else {
            if (!args[0].equals("get")) {
                throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
            }
            final double d11 = worldborder.getDiameter();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(d11 + 0.5));
            sender.addChatMessage(new ChatComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", d11) }));
        }
    }
    
    protected WorldBorder getWorldBorder() {
        return MinecraftServer.getServer().worldServers[0].getWorldBorder();
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "set", "center", "damage", "warning", "add", "get") : ((args.length == 2 && args[0].equals("damage")) ? CommandBase.getListOfStringsMatchingLastWord(args, "buffer", "amount") : ((args.length >= 2 && args.length <= 3 && args[0].equals("center")) ? CommandBase.func_181043_b(args, 1, pos) : ((args.length == 2 && args[0].equals("warning")) ? CommandBase.getListOfStringsMatchingLastWord(args, "time", "distance") : null)));
    }
}
