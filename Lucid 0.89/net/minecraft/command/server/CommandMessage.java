package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandMessage extends CommandBase
{

    /**
     * Gets a list of aliases for this command
     */
    @Override
	public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"w", "msg"});
    }

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "tell";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
	public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    @Override
	public String getCommandUsage(ICommandSender sender)
    {
        return "commands.message.usage";
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
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);

            if (var3 == sender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
            }
            else
            {
                IChatComponent var4 = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
                ChatComponentTranslation var5 = new ChatComponentTranslation("commands.message.display.incoming", new Object[] {sender.getDisplayName(), var4.createCopy()});
                ChatComponentTranslation var6 = new ChatComponentTranslation("commands.message.display.outgoing", new Object[] {var3.getDisplayName(), var4.createCopy()});
                var5.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
                var6.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
                var3.addChatMessage(var5);
                sender.addChatMessage(var6);
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
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
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
