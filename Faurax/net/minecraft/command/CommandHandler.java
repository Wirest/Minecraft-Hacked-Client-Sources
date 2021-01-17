package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler implements ICommandManager
{
    private static final Logger logger = LogManager.getLogger();

    /** Map of Strings to the ICommand objects they represent */
    private final Map commandMap = Maps.newHashMap();

    /** The set of ICommand objects currently loaded. */
    private final Set commandSet = Sets.newHashSet();
    private static final String __OBFID = "CL_00001765";

    public int executeCommand(ICommandSender sender, String command)
    {
        command = command.trim();

        if (command.startsWith("/"))
        {
            command = command.substring(1);
        }

        String[] var3 = command.split(" ");
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
                List var12 = PlayerSelector.func_179656_b(sender, var3[var6], Entity.class);
                String var9 = var3[var6];
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var12.size());
                Iterator var10 = var12.iterator();

                while (var10.hasNext())
                {
                    Entity var11 = (Entity)var10.next();
                    var3[var6] = var11.getUniqueID().toString();

                    if (this.func_175786_a(sender, var3, var5, command))
                    {
                        ++var7;
                    }
                }

                var3[var6] = var9;
            }
            else
            {
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, 1);

                if (this.func_175786_a(sender, var3, var5, command))
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

        sender.func_174794_a(CommandResultStats.Type.SUCCESS_COUNT, var7);
        return var7;
    }

    protected boolean func_175786_a(ICommandSender p_175786_1_, String[] p_175786_2_, ICommand p_175786_3_, String p_175786_4_)
    {
        ChatComponentTranslation var6;

        try
        {
            p_175786_3_.processCommand(p_175786_1_, p_175786_2_);
            return true;
        }
        catch (WrongUsageException var7)
        {
            var6 = new ChatComponentTranslation("commands.generic.usage", new Object[] {new ChatComponentTranslation(var7.getMessage(), var7.getErrorOjbects())});
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
        }
        catch (CommandException var8)
        {
            var6 = new ChatComponentTranslation(var8.getMessage(), var8.getErrorOjbects());
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
        }
        catch (Throwable var9)
        {
            var6 = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
            logger.error("Couldn\'t process command: \'" + p_175786_4_ + "\'", var9);
        }

        return false;
    }

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public ICommand registerCommand(ICommand p_71560_1_)
    {
        this.commandMap.put(p_71560_1_.getCommandName(), p_71560_1_);
        this.commandSet.add(p_71560_1_);
        Iterator var2 = p_71560_1_.getCommandAliases().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            ICommand var4 = (ICommand)this.commandMap.get(var3);

            if (var4 == null || !var4.getCommandName().equals(var3))
            {
                this.commandMap.put(var3, p_71560_1_);
            }
        }

        return p_71560_1_;
    }

    /**
     * creates a new array and sets elements 0..n-2 to be 0..n-1 of the input (n elements)
     */
    private static String[] dropFirstString(String[] p_71559_0_)
    {
        String[] var1 = new String[p_71559_0_.length - 1];
        System.arraycopy(p_71559_0_, 1, var1, 0, p_71559_0_.length - 1);
        return var1;
    }

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
    public Map getCommands()
    {
        return this.commandMap;
    }

    /**
     * Return a command's first parameter index containing a valid username.
     */
    private int getUsernameIndex(ICommand p_82370_1_, String[] p_82370_2_)
    {
        if (p_82370_1_ == null)
        {
            return -1;
        }
        else
        {
            for (int var3 = 0; var3 < p_82370_2_.length; ++var3)
            {
                if (p_82370_1_.isUsernameIndex(p_82370_2_, var3) && PlayerSelector.matchesMultiplePlayers(p_82370_2_[var3]))
                {
                    return var3;
                }
            }

            return -1;
        }
    }
}
