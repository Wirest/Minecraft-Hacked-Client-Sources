// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collection;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.CommandException;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.block.Block;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTestForBlock extends CommandBase
{
    @Override
    public String getCommandName() {
        return "testforblock";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.testforblock.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final Block block = Block.getBlockFromName(args[3]);
        if (block == null) {
            throw new NumberInvalidException("commands.setblock.notFound", new Object[] { args[3] });
        }
        int i = -1;
        if (args.length >= 5) {
            i = CommandBase.parseInt(args[4], -1, 15);
        }
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 6 && block.hasTileEntity()) {
            final String s = CommandBase.getChatComponentFromNthArg(sender, args, 5).getUnformattedText();
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final IBlockState iblockstate = world.getBlockState(blockpos);
        final Block block2 = iblockstate.getBlock();
        if (block2 != block) {
            throw new CommandException("commands.testforblock.failed.tile", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ(), block2.getLocalizedName(), block.getLocalizedName() });
        }
        if (i > -1) {
            final int j = iblockstate.getBlock().getMetaFromState(iblockstate);
            if (j != i) {
                throw new CommandException("commands.testforblock.failed.data", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ(), j, i });
            }
        }
        if (flag) {
            final TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null) {
                throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            tileentity.writeToNBT(nbttagcompound2);
            if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound2, true)) {
                throw new CommandException("commands.testforblock.failed.nbt", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBase.notifyOperators(sender, this, "commands.testforblock.success", blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length == 4) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null);
    }
}
