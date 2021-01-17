// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class CommandFill extends CommandBase
{
    @Override
    public String getCommandName() {
        return "fill";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.fill.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 7) {
            throw new WrongUsageException("commands.fill.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 3, false);
        final Block block = CommandBase.getBlockByText(sender, args[6]);
        int i = 0;
        if (args.length >= 8) {
            i = CommandBase.parseInt(args[7], 0, 15);
        }
        final BlockPos blockpos3 = new BlockPos(Math.min(blockpos.getX(), blockpos2.getX()), Math.min(blockpos.getY(), blockpos2.getY()), Math.min(blockpos.getZ(), blockpos2.getZ()));
        final BlockPos blockpos4 = new BlockPos(Math.max(blockpos.getX(), blockpos2.getX()), Math.max(blockpos.getY(), blockpos2.getY()), Math.max(blockpos.getZ(), blockpos2.getZ()));
        int j = (blockpos4.getX() - blockpos3.getX() + 1) * (blockpos4.getY() - blockpos3.getY() + 1) * (blockpos4.getZ() - blockpos3.getZ() + 1);
        if (j > 32768) {
            throw new CommandException("commands.fill.tooManyBlocks", new Object[] { j, 32768 });
        }
        if (blockpos3.getY() < 0 || blockpos4.getY() >= 256) {
            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        for (int k = blockpos3.getZ(); k < blockpos4.getZ() + 16; k += 16) {
            for (int l = blockpos3.getX(); l < blockpos4.getX() + 16; l += 16) {
                if (!world.isBlockLoaded(new BlockPos(l, blockpos4.getY() - blockpos3.getY(), k))) {
                    throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                }
            }
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 10 && block.hasTileEntity()) {
            final String s = CommandBase.getChatComponentFromNthArg(sender, args, 9).getUnformattedText();
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.fill.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final List<BlockPos> list = (List<BlockPos>)Lists.newArrayList();
        j = 0;
        for (int i2 = blockpos3.getZ(); i2 <= blockpos4.getZ(); ++i2) {
            for (int j2 = blockpos3.getY(); j2 <= blockpos4.getY(); ++j2) {
                for (int k2 = blockpos3.getX(); k2 <= blockpos4.getX(); ++k2) {
                    final BlockPos blockpos5 = new BlockPos(k2, j2, i2);
                    if (args.length >= 9) {
                        if (!args[8].equals("outline") && !args[8].equals("hollow")) {
                            if (args[8].equals("destroy")) {
                                world.destroyBlock(blockpos5, true);
                            }
                            else if (args[8].equals("keep")) {
                                if (!world.isAirBlock(blockpos5)) {
                                    continue;
                                }
                            }
                            else if (args[8].equals("replace") && !block.hasTileEntity()) {
                                if (args.length > 9) {
                                    final Block block2 = CommandBase.getBlockByText(sender, args[9]);
                                    if (world.getBlockState(blockpos5).getBlock() != block2) {
                                        continue;
                                    }
                                }
                                if (args.length > 10) {
                                    final int l2 = CommandBase.parseInt(args[10]);
                                    final IBlockState iblockstate = world.getBlockState(blockpos5);
                                    if (iblockstate.getBlock().getMetaFromState(iblockstate) != l2) {
                                        continue;
                                    }
                                }
                            }
                        }
                        else if (k2 != blockpos3.getX() && k2 != blockpos4.getX() && j2 != blockpos3.getY() && j2 != blockpos4.getY() && i2 != blockpos3.getZ() && i2 != blockpos4.getZ()) {
                            if (args[8].equals("hollow")) {
                                world.setBlockState(blockpos5, Blocks.air.getDefaultState(), 2);
                                list.add(blockpos5);
                            }
                            continue;
                        }
                    }
                    final TileEntity tileentity1 = world.getTileEntity(blockpos5);
                    if (tileentity1 != null) {
                        if (tileentity1 instanceof IInventory) {
                            ((IInventory)tileentity1).clear();
                        }
                        world.setBlockState(blockpos5, Blocks.barrier.getDefaultState(), (block == Blocks.barrier) ? 2 : 4);
                    }
                    final IBlockState iblockstate2 = block.getStateFromMeta(i);
                    if (world.setBlockState(blockpos5, iblockstate2, 2)) {
                        list.add(blockpos5);
                        ++j;
                        if (flag) {
                            final TileEntity tileentity2 = world.getTileEntity(blockpos5);
                            if (tileentity2 != null) {
                                nbttagcompound.setInteger("x", blockpos5.getX());
                                nbttagcompound.setInteger("y", blockpos5.getY());
                                nbttagcompound.setInteger("z", blockpos5.getZ());
                                tileentity2.readFromNBT(nbttagcompound);
                            }
                        }
                    }
                }
            }
        }
        for (final BlockPos blockpos6 : list) {
            final Block block3 = world.getBlockState(blockpos6).getBlock();
            world.notifyNeighborsRespectDebug(blockpos6, block3);
        }
        if (j <= 0) {
            throw new CommandException("commands.fill.failed", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, j);
        CommandBase.notifyOperators(sender, this, "commands.fill.success", j);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? CommandBase.func_175771_a(args, 3, pos) : ((args.length == 7) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : ((args.length == 9) ? CommandBase.getListOfStringsMatchingLastWord(args, "replace", "destroy", "keep", "hollow", "outline") : ((args.length == 10 && "replace".equals(args[8])) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null))));
    }
}
