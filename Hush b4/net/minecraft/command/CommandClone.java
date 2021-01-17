// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.world.NextTickListEntry;
import java.util.Collection;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.util.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone extends CommandBase
{
    @Override
    public String getCommandName() {
        return "clone";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.clone.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 3, false);
        final BlockPos blockpos3 = CommandBase.parseBlockPos(sender, args, 6, false);
        final StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos2);
        final StructureBoundingBox structureboundingbox2 = new StructureBoundingBox(blockpos3, blockpos3.add(structureboundingbox.func_175896_b()));
        int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", new Object[] { i, 32768 });
        }
        boolean flag = false;
        Block block = null;
        int j = -1;
        if ((args.length < 11 || (!args[10].equals("force") && !args[10].equals("move"))) && structureboundingbox.intersectsWith(structureboundingbox2)) {
            throw new CommandException("commands.clone.noOverlap", new Object[0]);
        }
        if (args.length >= 11 && args[10].equals("move")) {
            flag = true;
        }
        if (structureboundingbox.minY < 0 || structureboundingbox.maxY >= 256 || structureboundingbox2.minY < 0 || structureboundingbox2.maxY >= 256) {
            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        if (!world.isAreaLoaded(structureboundingbox) || !world.isAreaLoaded(structureboundingbox2)) {
            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        }
        boolean flag2 = false;
        if (args.length >= 10) {
            if (args[9].equals("masked")) {
                flag2 = true;
            }
            else if (args[9].equals("filtered")) {
                if (args.length < 12) {
                    throw new WrongUsageException("commands.clone.usage", new Object[0]);
                }
                block = CommandBase.getBlockByText(sender, args[11]);
                if (args.length >= 13) {
                    j = CommandBase.parseInt(args[12], 0, 15);
                }
            }
        }
        final List<StaticCloneData> list = (List<StaticCloneData>)Lists.newArrayList();
        final List<StaticCloneData> list2 = (List<StaticCloneData>)Lists.newArrayList();
        final List<StaticCloneData> list3 = (List<StaticCloneData>)Lists.newArrayList();
        final LinkedList<BlockPos> linkedlist = Lists.newLinkedList();
        final BlockPos blockpos4 = new BlockPos(structureboundingbox2.minX - structureboundingbox.minX, structureboundingbox2.minY - structureboundingbox.minY, structureboundingbox2.minZ - structureboundingbox.minZ);
        for (int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; ++k) {
            for (int l = structureboundingbox.minY; l <= structureboundingbox.maxY; ++l) {
                for (int i2 = structureboundingbox.minX; i2 <= structureboundingbox.maxX; ++i2) {
                    final BlockPos blockpos5 = new BlockPos(i2, l, k);
                    final BlockPos blockpos6 = blockpos5.add(blockpos4);
                    final IBlockState iblockstate = world.getBlockState(blockpos5);
                    if ((!flag2 || iblockstate.getBlock() != Blocks.air) && (block == null || (iblockstate.getBlock() == block && (j < 0 || iblockstate.getBlock().getMetaFromState(iblockstate) == j)))) {
                        final TileEntity tileentity = world.getTileEntity(blockpos5);
                        if (tileentity != null) {
                            final NBTTagCompound nbttagcompound = new NBTTagCompound();
                            tileentity.writeToNBT(nbttagcompound);
                            list2.add(new StaticCloneData(blockpos6, iblockstate, nbttagcompound));
                            linkedlist.addLast(blockpos5);
                        }
                        else if (!iblockstate.getBlock().isFullBlock() && !iblockstate.getBlock().isFullCube()) {
                            list3.add(new StaticCloneData(blockpos6, iblockstate, null));
                            linkedlist.addFirst(blockpos5);
                        }
                        else {
                            list.add(new StaticCloneData(blockpos6, iblockstate, null));
                            linkedlist.addLast(blockpos5);
                        }
                    }
                }
            }
        }
        if (flag) {
            for (final BlockPos blockpos7 : linkedlist) {
                final TileEntity tileentity2 = world.getTileEntity(blockpos7);
                if (tileentity2 instanceof IInventory) {
                    ((IInventory)tileentity2).clear();
                }
                world.setBlockState(blockpos7, Blocks.barrier.getDefaultState(), 2);
            }
            for (final BlockPos blockpos8 : linkedlist) {
                world.setBlockState(blockpos8, Blocks.air.getDefaultState(), 3);
            }
        }
        final List<StaticCloneData> list4 = (List<StaticCloneData>)Lists.newArrayList();
        list4.addAll(list);
        list4.addAll(list2);
        list4.addAll(list3);
        final List<StaticCloneData> list5 = Lists.reverse(list4);
        for (final StaticCloneData commandclone$staticclonedata : list5) {
            final TileEntity tileentity3 = world.getTileEntity(commandclone$staticclonedata.field_179537_a);
            if (tileentity3 instanceof IInventory) {
                ((IInventory)tileentity3).clear();
            }
            world.setBlockState(commandclone$staticclonedata.field_179537_a, Blocks.barrier.getDefaultState(), 2);
        }
        i = 0;
        for (final StaticCloneData commandclone$staticclonedata2 : list4) {
            if (world.setBlockState(commandclone$staticclonedata2.field_179537_a, commandclone$staticclonedata2.blockState, 2)) {
                ++i;
            }
        }
        for (final StaticCloneData commandclone$staticclonedata3 : list2) {
            final TileEntity tileentity4 = world.getTileEntity(commandclone$staticclonedata3.field_179537_a);
            if (commandclone$staticclonedata3.field_179536_c != null && tileentity4 != null) {
                commandclone$staticclonedata3.field_179536_c.setInteger("x", commandclone$staticclonedata3.field_179537_a.getX());
                commandclone$staticclonedata3.field_179536_c.setInteger("y", commandclone$staticclonedata3.field_179537_a.getY());
                commandclone$staticclonedata3.field_179536_c.setInteger("z", commandclone$staticclonedata3.field_179537_a.getZ());
                tileentity4.readFromNBT(commandclone$staticclonedata3.field_179536_c);
                tileentity4.markDirty();
            }
            world.setBlockState(commandclone$staticclonedata3.field_179537_a, commandclone$staticclonedata3.blockState, 2);
        }
        for (final StaticCloneData commandclone$staticclonedata4 : list5) {
            world.notifyNeighborsRespectDebug(commandclone$staticclonedata4.field_179537_a, commandclone$staticclonedata4.blockState.getBlock());
        }
        final List<NextTickListEntry> list6 = world.func_175712_a(structureboundingbox, false);
        if (list6 != null) {
            for (final NextTickListEntry nextticklistentry : list6) {
                if (structureboundingbox.isVecInside(nextticklistentry.position)) {
                    final BlockPos blockpos9 = nextticklistentry.position.add(blockpos4);
                    world.scheduleBlockUpdate(blockpos9, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
                }
            }
        }
        if (i <= 0) {
            throw new CommandException("commands.clone.failed", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
        CommandBase.notifyOperators(sender, this, "commands.clone.success", i);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? CommandBase.func_175771_a(args, 3, pos) : ((args.length > 6 && args.length <= 9) ? CommandBase.func_175771_a(args, 6, pos) : ((args.length == 10) ? CommandBase.getListOfStringsMatchingLastWord(args, "replace", "masked", "filtered") : ((args.length == 11) ? CommandBase.getListOfStringsMatchingLastWord(args, "normal", "force", "move") : ((args.length == 12 && "filtered".equals(args[9])) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null)))));
    }
    
    static class StaticCloneData
    {
        public final BlockPos field_179537_a;
        public final IBlockState blockState;
        public final NBTTagCompound field_179536_c;
        
        public StaticCloneData(final BlockPos p_i46037_1_, final IBlockState p_i46037_2_, final NBTTagCompound p_i46037_3_) {
            this.field_179537_a = p_i46037_1_;
            this.blockState = p_i46037_2_;
            this.field_179536_c = p_i46037_3_;
        }
    }
}
