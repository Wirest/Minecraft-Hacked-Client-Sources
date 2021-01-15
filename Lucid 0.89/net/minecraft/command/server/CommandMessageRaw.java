package net.minecraft.command.server;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonParseException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;

public class CommandMessageRaw extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "tellraw";
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
        return "commands.tellraw.usage";
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
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);
            String var4 = buildString(args, 1);

            try
            {
                IChatComponent var5 = IChatComponent.Serializer.jsonToComponent(var4);
                var3.addChatMessage(ChatComponentProcessor.processComponent(sender, var5, var3));
            }
            catch (JsonParseException var7)
            {
                Throwable var6 = ExceptionUtils.getRootCause(var7);
                throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] {var6 == null ? "" : var6.getMessage()});
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
