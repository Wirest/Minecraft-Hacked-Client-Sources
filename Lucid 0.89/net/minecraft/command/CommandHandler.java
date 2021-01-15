package net.minecraft.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class CommandHandler implements ICommandManager
{
    private static final Logger logger = LogManager.getLogger();

    /** Map of Strings to the ICommand objects they represent */
    private final Map commandMap = Maps.newHashMap();

    /** The set of ICommand objects currently loaded. */
    private final Set commandSet = Sets.newHashSet();

    @Override
	public int executeCommand(ICommandSender sender, String rawCommand)
    {
        rawCommand = rawCommand.trim();

        if (rawCommand.startsWith("/"))
        {
            rawCommand = rawCommand.substring(1);
        }

        String[] var3 = rawCommand.split(" ");
        String var4 = var3[0];
        var3 = dropFirstString(var3);
        ICommand var5 = (ICommand)this.commandMap.get(var4);
        int var6 = this.getUsernameIndex(var5, var3);
        int var7 = 0;
        ChatComponentTranslation var8;

        if (var5 == null)
        {
            var8 = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
            var8.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var8);
        }
        else if (var5.canCommandSenderUseCommand(sender))
        {
            if (var6 > -1)
            {
                List var12 = PlayerSelector.matchEntities(sender, var3[var6], Entity.class);
                String var9 = var3[var6];
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, var12.size());
                Iterator var10 = var12.iterator();

                while (var10.hasNext())
                {
                    Entity var11 = (Entity)var10.next();
                    var3[var6] = var11.getUniqueID().toString();

                    if (this.tryExecute(sender, var3, var5, rawCommand))
                    {
                        ++var7;
                    }
                }

                var3[var6] = var9;
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);

                if (this.tryExecute(sender, var3, var5, rawCommand))
                {
                    ++var7;
                }
            }
        }
        else
        {
            var8 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
            var8.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var8);
        }

        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, var7);
        return var7;
    }

    protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input)
    {
        ChatComponentTranslation var6;

        try
        {
            command.processCommand(sender, args);
            return true;
        }
        catch (WrongUsageException var7)
        {
            var6 = new ChatComponentTranslation("commands.generic.usage", new Object[] {new ChatComponentTranslation(var7.getMessage(), var7.getErrorObjects())});
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var6);
        }
        catch (CommandException var8)
        {
            var6 = new ChatComponentTranslation(var8.getMessage(), var8.getErrorObjects());
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var6);
        }
        catch (Throwable var9)
        {
            var6 = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var6);
            logger.error("Couldn\'t process command: \'" + input + "\'", var9);
        }

        return false;
    }

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public ICommand registerCommand(ICommand command)
    {
        this.commandMap.put(command.getCommandName(), command);
        this.commandSet.add(command);
        Iterator var2 = command.getCommandAliases().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            ICommand var4 = (ICommand)this.commandMap.get(var3);

            if (var4 == null || !var4.getCommandName().equals(var3))
            {
                this.commandMap.put(var3, command);
            }
        }

        return command;
    }

    /**
     * creates a new array and sets elements 0..n-2 to be 0..n-1 of the input (n elements)
     */
    private static String[] dropFirstString(String[] input)
    {
        String[] var1 = new String[input.length - 1];
        System.arraycopy(input, 1, var1, 0, input.length - 1);
        return var1;
    }

    @Override
	public List getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos)
    {
        String[] var4 = input.split(" ", -1);
        String var5 = var4[0];

        if (var4.length == 1)
        {
            ArrayList var9 = Lists.newArrayList();
            Iterator var7 = this.commandMap.entrySet().iterator();

            while (var7.hasNext())
            {
                Entry var8 = (Entry)var7.next();

                if (CommandBase.doesStringStartWith(var5, (String)var8.getKey()) && ((ICommand)var8.getValue()).canCommandSenderUseCommand(sender))
                {
                    var9.add(var8.getKey());
                }
            }

            return var9;
        }
        else
        {
            if (var4.length > 1)
            {
                ICommand var6 = (ICommand)this.commandMap.get(var5);

                if (var6 != null && var6.canCommandSenderUseCommand(sender))
                {
                    return var6.addTabCompletionOptions(sender, dropFirstString(var4), pos);
                }
            }

            return null;
        }
    }

    /**
     * returns all commands that the commandSender can use
     */
    @Override
	public List getPossibleCommands(ICommandSender sender)
    {
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = this.commandSet.iterator();

        while (var3.hasNext())
        {
            ICommand var4 = (ICommand)var3.next();

            if (var4.canCommandSenderUseCommand(sender))
            {
                var2.add(var4);
            }
        }

        return var2;
    }

    /**
     * returns a map of string to commads. All commands are returned, not just ones which someone has permission to use.
     */
    @Override
	public Map getCommands()
    {
        return this.commandMap;
    }

    /**
     * Return a command's first parameter index containing a valid username.
     */
    private int getUsernameIndex(ICommand command, String[] args)
    {
        if (command == null)
        {
            return -1;
        }
        else
        {
            for (int var3 = 0; var3 < args.length; ++var3)
            {
                if (command.isUsernameIndex(args, var3) && PlayerSelector.matchesMultiplePlayers(args[var3]))
                {
                    return var3;
                }
            }

            return -1;
        }
    }
}
