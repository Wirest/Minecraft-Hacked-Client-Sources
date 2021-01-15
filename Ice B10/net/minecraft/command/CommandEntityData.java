package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandEntityData extends CommandBase
{
    private static final String __OBFID = "CL_00002345";

    public String getCommandName()
    {
        return "entitydata";
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
        return "commands.entitydata.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        else
        {
            Entity var3 = func_175768_b(sender, args[0]);

            if (var3 instanceof EntityPlayer)
            {
                throw new CommandException("commands.entitydata.noPlayers", new Object[] {var3.getDisplayName()});
            }
            else
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var3.writeToNBT(var4);
                NBTTagCompound var5 = (NBTTagCompound)var4.copy();
                NBTTagCompound var6;

                try
                {
                    var6 = JsonToNBT.func_180713_a(getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
                }
                catch (NBTException var8)
                {
                    throw new CommandException("commands.entitydata.tagError", new Object[] {var8.getMessage()});
                }

                var6.removeTag("UUIDMost");
                var6.removeTag("UUIDLeast");
                var4.merge(var6);

                if (var4.equals(var5))
                {
                    throw new CommandException("commands.entitydata.failed", new Object[] {var4.toString()});
                }
                else
                {
                    var3.readFromNBT(var4);
                    notifyOperators(sender, this, "commands.entitydata.success", new Object[] {var4.toString()});
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
