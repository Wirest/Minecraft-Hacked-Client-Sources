// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collection;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.inventory.IInventory;
import net.minecraft.command.ICommand;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSetBlock extends CommandBase
{
    @Override
    public String getCommandName() {
        return "setblock";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.setblock.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final Block block = CommandBase.getBlockByText(sender, args[3]);
        int i = 0;
        if (args.length >= 5) {
            i = CommandBase.parseInt(args[4], 0, 15);
        }
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 7 && block.hasTileEntity()) {
            final String s = CommandBase.getChatComponentFromNthArg(sender, args, 6).getUnformattedText();
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (args.length >= 6) {
            if (args[5].equals("destroy")) {
                world.destroyBlock(blockpos, true);
                if (block == Blocks.air) {
                    CommandBase.notifyOperators(sender, this, "commands.setblock.success", new Object[0]);
                    return;
                }
            }
            else if (args[5].equals("keep") && !world.isAirBlock(blockpos)) {
                throw new CommandException("commands.setblock.noChange", new Object[0]);
            }
        }
        final TileEntity tileentity1 = world.getTileEntity(blockpos);
        if (tileentity1 != null) {
            if (tileentity1 instanceof IInventory) {
                ((IInventory)tileentity1).clear();
            }
            world.setBlockState(blockpos, Blocks.air.getDefaultState(), (block == Blocks.air) ? 2 : 4);
        }
        final IBlockState iblockstate = block.getStateFromMeta(i);
        if (!world.setBlockState(blockpos, iblockstate, 2)) {
            throw new CommandException("commands.setblock.noChange", new Object[0]);
        }
        if (flag) {
            final TileEntity tileentity2 = world.getTileEntity(blockpos);
            if (tileentity2 != null) {
                nbttagcompound.setInteger("x", blockpos.getX());
                nbttagcompound.setInteger("y", blockpos.getY());
                nbttagcompound.setInteger("z", blockpos.getZ());
                tileentity2.readFromNBT(nbttagcompound);
            }
        }
        world.notifyNeighborsRespectDebug(blockpos, iblockstate.getBlock());
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBase.notifyOperators(sender, this, "commands.setblock.success", new Object[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length == 4) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : ((args.length == 6) ? CommandBase.getListOfStringsMatchingLastWord(args, "replace", "destroy", "keep") : null));
    }
}
