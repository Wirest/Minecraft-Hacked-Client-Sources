// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase
{
    @Override
    public String getCommandName() {
        return "testforblocks";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.compare.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 3, false);
        final BlockPos blockpos3 = CommandBase.parseBlockPos(sender, args, 6, false);
        final StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos2);
        final StructureBoundingBox structureboundingbox2 = new StructureBoundingBox(blockpos3, blockpos3.add(structureboundingbox.func_175896_b()));
        int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i > 524288) {
            throw new CommandException("commands.compare.tooManyBlocks", new Object[] { i, 524288 });
        }
        if (structureboundingbox.minY < 0 || structureboundingbox.maxY >= 256 || structureboundingbox2.minY < 0 || structureboundingbox2.maxY >= 256) {
            throw new CommandException("commands.compare.outOfWorld", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox2)) {
            boolean flag = false;
            if (args.length > 9 && args[9].equals("masked")) {
                flag = true;
            }
            i = 0;
            final BlockPos blockpos4 = new BlockPos(structureboundingbox2.minX - structureboundingbox.minX, structureboundingbox2.minY - structureboundingbox.minY, structureboundingbox2.minZ - structureboundingbox.minZ);
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            final BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
            for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j) {
                for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k) {
                    for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l) {
                        blockpos$mutableblockpos.func_181079_c(l, k, j);
                        blockpos$mutableblockpos2.func_181079_c(l + blockpos4.getX(), k + blockpos4.getY(), j + blockpos4.getZ());
                        boolean flag2 = false;
                        final IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);
                        if (!flag || iblockstate.getBlock() != Blocks.air) {
                            if (iblockstate == world.getBlockState(blockpos$mutableblockpos2)) {
                                final TileEntity tileentity = world.getTileEntity(blockpos$mutableblockpos);
                                final TileEntity tileentity2 = world.getTileEntity(blockpos$mutableblockpos2);
                                if (tileentity != null && tileentity2 != null) {
                                    final NBTTagCompound nbttagcompound = new NBTTagCompound();
                                    tileentity.writeToNBT(nbttagcompound);
                                    nbttagcompound.removeTag("x");
                                    nbttagcompound.removeTag("y");
                                    nbttagcompound.removeTag("z");
                                    final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                                    tileentity2.writeToNBT(nbttagcompound2);
                                    nbttagcompound2.removeTag("x");
                                    nbttagcompound2.removeTag("y");
                                    nbttagcompound2.removeTag("z");
                                    if (!nbttagcompound.equals(nbttagcompound2)) {
                                        flag2 = true;
                                    }
                                }
                                else if (tileentity != null) {
                                    flag2 = true;
                                }
                            }
                            else {
                                flag2 = true;
                            }
                            ++i;
                            if (flag2) {
                                throw new CommandException("commands.compare.failed", new Object[0]);
                            }
                        }
                    }
                }
            }
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
            CommandBase.notifyOperators(sender, this, "commands.compare.success", i);
            return;
        }
        throw new CommandException("commands.compare.outOfWorld", new Object[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? CommandBase.func_175771_a(args, 3, pos) : ((args.length > 6 && args.length <= 9) ? CommandBase.func_175771_a(args, 6, pos) : ((args.length == 10) ? CommandBase.getListOfStringsMatchingLastWord(args, "masked", "all") : null)));
    }
}
