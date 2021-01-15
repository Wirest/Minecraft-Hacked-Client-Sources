package net.minecraft.command;

import java.util.List;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandBlockData extends CommandBase
{
    private static final String __OBFID = "CL_00002349";

    public String getCommandName()
    {
        return "blockdata";
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
        return "commands.blockdata.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 4)
        {
            throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
        }
        else
        {
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos var3 = func_175757_a(sender, args, 0, false);
            World var4 = sender.getEntityWorld();

            if (!var4.isBlockLoaded(var3))
            {
                throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
            }
            else
            {
                TileEntity var5 = var4.getTileEntity(var3);

                if (var5 == null)
                {
                    throw new CommandException("commands.blockdata.notValid", new Object[0]);
                }
                else
                {
                    NBTTagCompound var6 = new NBTTagCompound();
                    var5.writeToNBT(var6);
                    NBTTagCompound var7 = (NBTTagCompound)var6.copy();
                    NBTTagCompound var8;

                    try
                    {
                        var8 = JsonToNBT.func_180713_a(getChatComponentFromNthArg(sender, args, 3).getUnformattedText());
                    }
                    catch (NBTException var10)
                    {
                        throw new CommandException("commands.blockdata.tagError", new Object[] {var10.getMessage()});
                    }

                    var6.merge(var8);
                    var6.setInteger("x", var3.getX());
                    var6.setInteger("y", var3.getY());
                    var6.setInteger("z", var3.getZ());

                    if (var6.equals(var7))
                    {
                        throw new CommandException("commands.blockdata.failed", new Object[] {var6.toString()});
                    }
                    else
                    {
                        var5.readFromNBT(var6);
                        var5.markDirty();
                        var4.markBlockForUpdate(var3);
                        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                        notifyOperators(sender, this, "commands.blockdata.success", new Object[] {var6.toString()});
                    }
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : null;
    }
}
