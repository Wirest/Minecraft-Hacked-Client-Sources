package net.minecraft.command;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase {
    private static final String __OBFID = "CL_00002346";

    public String getCommandName() {
        return "testforblocks";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.compare.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        } else {
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos var3 = func_175757_a(sender, args, 0, false);
            BlockPos var4 = func_175757_a(sender, args, 3, false);
            BlockPos var5 = func_175757_a(sender, args, 6, false);
            StructureBoundingBox var6 = new StructureBoundingBox(var3, var4);
            StructureBoundingBox var7 = new StructureBoundingBox(var5, var5.add(var6.func_175896_b()));
            int var8 = var6.getXSize() * var6.getYSize() * var6.getZSize();

            if (var8 > 524288) {
                throw new CommandException("commands.compare.tooManyBlocks", new Object[]{Integer.valueOf(var8), Integer.valueOf(524288)});
            } else if (var6.minY >= 0 && var6.maxY < 256 && var7.minY >= 0 && var7.maxY < 256) {
                World var9 = sender.getEntityWorld();

                if (var9.isAreaLoaded(var6) && var9.isAreaLoaded(var7)) {
                    boolean var10 = false;

                    if (args.length > 9 && args[9].equals("masked")) {
                        var10 = true;
                    }

                    var8 = 0;
                    BlockPos var11 = new BlockPos(var7.minX - var6.minX, var7.minY - var6.minY, var7.minZ - var6.minZ);

                    for (int var12 = var6.minZ; var12 <= var6.maxZ; ++var12) {
                        for (int var13 = var6.minY; var13 <= var6.maxY; ++var13) {
                            for (int var14 = var6.minX; var14 <= var6.maxX; ++var14) {
                                BlockPos var15 = new BlockPos(var14, var13, var12);
                                BlockPos var16 = var15.add(var11);
                                boolean var17 = false;
                                IBlockState var18 = var9.getBlockState(var15);

                                if (!var10 || var18.getBlock() != Blocks.air) {
                                    if (var18 == var9.getBlockState(var16)) {
                                        TileEntity var19 = var9.getTileEntity(var15);
                                        TileEntity var20 = var9.getTileEntity(var16);

                                        if (var19 != null && var20 != null) {
                                            NBTTagCompound var21 = new NBTTagCompound();
                                            var19.writeToNBT(var21);
                                            var21.removeTag("x");
                                            var21.removeTag("y");
                                            var21.removeTag("z");
                                            NBTTagCompound var22 = new NBTTagCompound();
                                            var20.writeToNBT(var22);
                                            var22.removeTag("x");
                                            var22.removeTag("y");
                                            var22.removeTag("z");

                                            if (!var21.equals(var22)) {
                                                var17 = true;
                                            }
                                        } else if (var19 != null) {
                                            var17 = true;
                                        }
                                    } else {
                                        var17 = true;
                                    }

                                    ++var8;

                                    if (var17) {
                                        throw new CommandException("commands.compare.failed", new Object[0]);
                                    }
                                }
                            }
                        }
                    }

                    sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, var8);
                    notifyOperators(sender, this, "commands.compare.success", new Object[]{Integer.valueOf(var8)});
                } else {
                    throw new CommandException("commands.compare.outOfWorld", new Object[0]);
                }
            } else {
                throw new CommandException("commands.compare.outOfWorld", new Object[0]);
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length > 3 && args.length <= 6 ? func_175771_a(args, 3, pos) : (args.length > 6 && args.length <= 9 ? func_175771_a(args, 6, pos) : (args.length == 10 ? getListOfStringsMatchingLastWord(args, new String[]{"masked", "all"}) : null)));
    }
}
