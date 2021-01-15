package net.minecraft.command.server;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandSetBlock extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "setblock";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
	public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    @Override
	public String getCommandUsage(ICommandSender sender)
    {
        return "commands.setblock.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    @Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 4)
        {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        }
        else
        {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos var3 = parseBlockPos(sender, args, 0, false);
            Block var4 = CommandBase.getBlockByText(sender, args[3]);
            int var5 = 0;

            if (args.length >= 5)
            {
                var5 = parseInt(args[4], 0, 15);
            }

            World var6 = sender.getEntityWorld();

            if (!var6.isBlockLoaded(var3))
            {
                throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
            }
            else
            {
                NBTTagCompound var7 = new NBTTagCompound();
                boolean var8 = false;

                if (args.length >= 7 && var4.hasTileEntity())
                {
                    String var9 = getChatComponentFromNthArg(sender, args, 6).getUnformattedText();

                    try
                    {
                        var7 = JsonToNBT.getTagFromJson(var9);
                        var8 = true;
                    }
                    catch (NBTException var12)
                    {
                        throw new CommandException("commands.setblock.tagError", new Object[] {var12.getMessage()});
                    }
                }

                if (args.length >= 6)
                {
                    if (args[5].equals("destroy"))
                    {
                        var6.destroyBlock(var3, true);

                        if (var4 == Blocks.air)
                        {
                            notifyOperators(sender, this, "commands.setblock.success", new Object[0]);
                            return;
                        }
                    }
                    else if (args[5].equals("keep") && !var6.isAirBlock(var3))
                    {
                        throw new CommandException("commands.setblock.noChange", new Object[0]);
                    }
                }

                TileEntity var13 = var6.getTileEntity(var3);

                if (var13 != null)
                {
                    if (var13 instanceof IInventory)
                    {
                        ((IInventory)var13).clear();
                    }

                    var6.setBlockState(var3, Blocks.air.getDefaultState(), var4 == Blocks.air ? 2 : 4);
                }

                IBlockState var10 = var4.getStateFromMeta(var5);

                if (!var6.setBlockState(var3, var10, 2))
                {
                    throw new CommandException("commands.setblock.noChange", new Object[0]);
                }
                else
                {
                    if (var8)
                    {
                        TileEntity var11 = var6.getTileEntity(var3);

                        if (var11 != null)
                        {
                            var7.setInteger("x", var3.getX());
                            var7.setInteger("y", var3.getY());
                            var7.setInteger("z", var3.getZ());
                            var11.readFromNBT(var7);
                        }
                    }

                    var6.notifyNeighborsRespectDebug(var3, var10.getBlock());
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                    notifyOperators(sender, this, "commands.setblock.success", new Object[0]);
                }
            }
        }
    }

    /**
     * Return a list of options when the user types TAB
     *  
     * @param sender The {@link ICommandSender sender} who pressed TAB
     * @param args The arguments that were present when TAB was pressed
     * @param pos The block that the player is targeting, <b>May be {@code null}</b>
     */
    @Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length == 4 ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : (args.length == 6 ? getListOfStringsMatchingLastWord(args, new String[] {"replace", "destroy", "keep"}): null));
    }
}
