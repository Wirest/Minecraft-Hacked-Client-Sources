package net.minecraft.command;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonParseException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;

public class CommandTitle extends CommandBase
{
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "title";
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
        return "commands.title.usage";
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
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        else
        {
            if (args.length < 3)
            {
                if ("title".equals(args[1]) || "subtitle".equals(args[1]))
                {
                    throw new WrongUsageException("commands.title.usage.title", new Object[0]);
                }

                if ("times".equals(args[1]))
                {
                    throw new WrongUsageException("commands.title.usage.times", new Object[0]);
                }
            }

            EntityPlayerMP var3 = getPlayer(sender, args[0]);
            S45PacketTitle.Type var4 = S45PacketTitle.Type.byName(args[1]);

            if (var4 != S45PacketTitle.Type.CLEAR && var4 != S45PacketTitle.Type.RESET)
            {
                if (var4 == S45PacketTitle.Type.TIMES)
                {
                    if (args.length != 5)
                    {
                        throw new WrongUsageException("commands.title.usage", new Object[0]);
                    }
                    else
                    {
                        int var11 = parseInt(args[2]);
                        int var12 = parseInt(args[3]);
                        int var13 = parseInt(args[4]);
                        S45PacketTitle var14 = new S45PacketTitle(var11, var12, var13);
                        var3.playerNetServerHandler.sendPacket(var14);
                        notifyOperators(sender, this, "commands.title.success", new Object[0]);
                    }
                }
                else if (args.length < 3)
                {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                else
                {
                    String var10 = buildString(args, 2);
                    IChatComponent var6;

                    try
                    {
                        var6 = IChatComponent.Serializer.jsonToComponent(var10);
                    }
                    catch (JsonParseException var9)
                    {
                        Throwable var8 = ExceptionUtils.getRootCause(var9);
                        throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] {var8 == null ? "" : var8.getMessage()});
                    }

                    S45PacketTitle var7 = new S45PacketTitle(var4, ChatComponentProcessor.processComponent(sender, var6, var3));
                    var3.playerNetServerHandler.sendPacket(var7);
                    notifyOperators(sender, this, "commands.title.success", new Object[0]);
                }
            }
            else if (args.length != 2)
            {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            else
            {
                S45PacketTitle var5 = new S45PacketTitle(var4, (IChatComponent)null);
                var3.playerNetServerHandler.sendPacket(var5);
                notifyOperators(sender, this, "commands.title.success", new Object[0]);
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
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length == 2 ? getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.getNames()) : null);
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
