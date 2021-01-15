package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandTestFor extends CommandBase
{
    private static final String __OBFID = "CL_00001182";

    public String getCommandName()
    {
        return "testfor";
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
        return "commands.testfor.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.testfor.usage", new Object[0]);
        }
        else
        {
            Entity var3 = func_175768_b(sender, args[0]);
            NBTTagCompound var4 = null;

            if (args.length >= 2)
            {
                try
                {
                    var4 = JsonToNBT.func_180713_a(func_180529_a(args, 1));
                }
                catch (NBTException var6)
                {
                    throw new CommandException("commands.testfor.tagError", new Object[] {var6.getMessage()});
                }
            }

            if (var4 != null)
            {
                NBTTagCompound var5 = new NBTTagCompound();
                var3.writeToNBT(var5);

                if (!CommandTestForBlock.func_175775_a(var4, var5, true))
                {
                    throw new CommandException("commands.testfor.failure", new Object[] {var3.getName()});
                }
            }

            notifyOperators(sender, this, "commands.testfor.success", new Object[] {var3.getName()});
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
