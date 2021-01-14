package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandMessage extends CommandBase
{
    public List<String> getCommandAliases()
    {
        return Arrays.<String>asList("w", "msg");
    }

    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "tell";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.message.usage");
        }
        else
        {
            EntityPlayer entityplayer = getPlayer(sender, args[0]);

            if (entityplayer == sender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget");
            }
            else
            {
                IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
                ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.message.display.incoming", sender.getDisplayName(), ichatcomponent.createCopy());
                ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.message.display.outgoing", entityplayer.getDisplayName(), ichatcomponent.createCopy());
                chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.TRUE);
                chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.TRUE);
                entityplayer.addChatMessage(chatcomponenttranslation);
                sender.addChatMessage(chatcomponenttranslation1);
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *  
     * @param args The arguments that were given
     * @param index The argument index that we are checking
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
