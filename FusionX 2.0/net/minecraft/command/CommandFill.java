package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandFill extends CommandBase
{
    private static final String __OBFID = "CL_00002342";

    public String getCommandName()
    {
        return "fill";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.fill.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 7)
        {
            throw new WrongUsageException("commands.fill.usage", new Object[0]);
        }
        else
        {
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos var3 = func_175757_a(sender, args, 0, false);
            BlockPos var4 = func_175757_a(sender, args, 3, false);
            Block var5 = CommandBase.getBlockByText(sender, args[6]);
            int var6 = 0;

            if (args.length >= 8)
            {
                var6 = parseInt(args[7], 0, 15);
            }

            BlockPos var7 = new BlockPos(Math.min(var3.getX(), var4.getX()), Math.min(var3.getY(), var4.getY()), Math.min(var3.getZ(), var4.getZ()));
            BlockPos var8 = new BlockPos(Math.max(var3.getX(), var4.getX()), Math.max(var3.getY(), var4.getY()), Math.max(var3.getZ(), var4.getZ()));
            int var9 = (var8.getX() - var7.getX() + 1) * (var8.getY() - var7.getY() + 1) * (var8.getZ() - var7.getZ() + 1);

            if (var9 > 32768)
            {
                throw new CommandException("commands.fill.tooManyBlocks", new Object[] {Integer.valueOf(var9), Integer.valueOf(32768)});
            }
            else if (var7.getY() >= 0 && var8.getY() < 256)
            {
                World var10 = sender.getEntityWorld();

                for (int var11 = var7.getZ(); var11 < var8.getZ() + 16; var11 += 16)
                {
                    for (int var12 = var7.getX(); var12 < var8.getX() + 16; var12 += 16)
                    {
                        if (!var10.isBlockLoaded(new BlockPos(var12, var8.getY() - var7.getY(), var11)))
                        {
                            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                        }
                    }
                }

                NBTTagCompound var22 = new NBTTagCompound();
                boolean var23 = false;

                if (args.length >= 10 && var5.hasTileEntity())
                {
                    String var13 = getChatComponentFromNthArg(sender, args, 9).getUnformattedText();

                    try
                    {
                        var22 = JsonToNBT.func_180713_a(var13);
                        var23 = true;
                    }
                    catch (NBTException var21)
                    {
                        throw new CommandException("commands.fill.tagError", new Object[] {var21.getMessage()});
                    }
                }

                ArrayList var24 = Lists.newArrayList();
                var9 = 0;

                for (int var14 = var7.getZ(); var14 <= var8.getZ(); ++var14)
                {
                    for (int var15 = var7.getY(); var15 <= var8.getY(); ++var15)
                    {
                        for (int var16 = var7.getX(); var16 <= var8.getX(); ++var16)
                        {
                            BlockPos var17 = new BlockPos(var16, var15, var14);
                            IBlockState var19;

                            if (args.length >= 9)
                            {
                                if (!args[8].equals("outline") && !args[8].equals("hollow"))
                                {
                                    if (args[8].equals("destroy"))
                                    {
                                        var10.destroyBlock(var17, true);
                                    }
                                    else if (args[8].equals("keep"))
                                    {
                                        if (!var10.isAirBlock(var17))
                                        {
                                            continue;
                                        }
                                    }
                                    else if (args[8].equals("replace") && !var5.hasTileEntity())
                                    {
                                        if (args.length > 9)
                                        {
                                            Block var18 = CommandBase.getBlockByText(sender, args[9]);

                                            if (var10.getBlockState(var17).getBlock() != var18)
                                            {
                                                continue;
                                            }
                                        }

                                        if (args.length > 10)
                                        {
                                            int var28 = CommandBase.parseInt(args[10]);
                                            var19 = var10.getBlockState(var17);

                                            if (var19.getBlock().getMetaFromState(var19) != var28)
                                            {
                                                continue;
                                            }
                                        }
                                    }
                                }
                                else if (var16 != var7.getX() && var16 != var8.getX() && var15 != var7.getY() && var15 != var8.getY() && var14 != var7.getZ() && var14 != var8.getZ())
                                {
                                    if (args[8].equals("hollow"))
                                    {
                                        var10.setBlockState(var17, Blocks.air.getDefaultState(), 2);
                                        var24.add(var17);
                                    }

                                    continue;
                                }
                            }

                            TileEntity var29 = var10.getTileEntity(var17);

                            if (var29 != null)
                            {
                                if (var29 instanceof IInventory)
                                {
                                    ((IInventory)var29).clearInventory();
                                }

                                var10.setBlockState(var17, Blocks.barrier.getDefaultState(), var5 == Blocks.barrier ? 2 : 4);
                            }

                            var19 = var5.getStateFromMeta(var6);

                            if (var10.setBlockState(var17, var19, 2))
                            {
                                var24.add(var17);
                                ++var9;

                                if (var23)
                                {
                                    TileEntity var20 = var10.getTileEntity(var17);

                                    if (var20 != null)
                                    {
                                        var22.setInteger("x", var17.getX());
                                        var22.setInteger("y", var17.getY());
                                        var22.setInteger("z", var17.getZ());
                                        var20.readFromNBT(var22);
                                    }
                                }
                            }
                        }
                    }
                }

                Iterator var25 = var24.iterator();

                while (var25.hasNext())
                {
                    BlockPos var26 = (BlockPos)var25.next();
                    Block var27 = var10.getBlockState(var26).getBlock();
                    var10.func_175722_b(var26, var27);
                }

                if (var9 <= 0)
                {
                    throw new CommandException("commands.fill.failed", new Object[0]);
                }
                else
                {
                    sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, var9);
                    notifyOperators(sender, this, "commands.fill.success", new Object[] {Integer.valueOf(var9)});
                }
            }
            else
            {
                throw new CommandException("commands.fill.outOfWorld", new Object[0]);
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length > 3 && args.length <= 6 ? func_175771_a(args, 3, pos) : (args.length == 7 ? func_175762_a(args, Block.blockRegistry.getKeys()) : (args.length == 9 ? getListOfStringsMatchingLastWord(args, new String[] {"replace", "destroy", "keep", "hollow", "outline"}): null)));
    }
}
