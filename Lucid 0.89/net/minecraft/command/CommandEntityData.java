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

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "entitydata";
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
        return "commands.entitydata.usage";
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
                    var6 = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
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
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *  
     * @param args The arguments that were given
     * @param index The argument index that we are checking
     */
    @Override
	public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
