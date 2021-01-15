package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

public class CommandHelp extends CommandBase
{
    private static final String __OBFID = "CL_00000529";

    public String getCommandName()
    {
        return "help";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.help.usage";
    }

    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"?"});
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        List var3 = this.getSortedPossibleCommands(sender);
        boolean var4 = true;
        int var5 = (var3.size() - 1) / 7;
        boolean var6 = false;
        int var13;

        try
        {
            var13 = args.length == 0 ? 0 : parseInt(args[0], 1, var5 + 1) - 1;
        }
        catch (NumberInvalidException var12)
        {
            Map var8 = this.getCommands();
            ICommand var9 = (ICommand)var8.get(args[0]);

            if (var9 != null)
            {
                throw new WrongUsageException(var9.getCommandUsage(sender), new Object[0]);
            }

            if (MathHelper.parseIntWithDefault(args[0], -1) != -1)
            {
                throw var12;
            }

            throw new CommandNotFoundException();
        }

        int var7 = Math.min((var13 + 1) * 7, var3.size());
        ChatComponentTranslation var14 = new ChatComponentTranslation("commands.help.header", new Object[] {Integer.valueOf(var13 + 1), Integer.valueOf(var5 + 1)});
        var14.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        sender.addChatMessage(var14);

        for (int var15 = var13 * 7; var15 < var7; ++var15)
        {
            ICommand var10 = (ICommand)var3.get(var15);
            ChatComponentTranslation var11 = new ChatComponentTranslation(var10.getCommandUsage(sender), new Object[0]);
            var11.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + var10.getCommandName() + " "));
            sender.addChatMessage(var11);
        }

        if (var13 == 0 && sender instanceof EntityPlayer)
        {
            ChatComponentTranslation var16 = new ChatComponentTranslation("commands.help.footer", new Object[0]);
            var16.getChatStyle().setColor(EnumChatFormatting.GREEN);
            sender.addChatMessage(var16);
        }
    }

    /**
     * Returns a sorted list of all possible commands for the given ICommandSender.
     */
    protected List getSortedPossibleCommands(ICommandSender p_71534_1_)
    {
        List var2 = MinecraftServer.getServer().getCommandManager().getPossibleCommands(p_71534_1_);
        Collections.sort(var2);
        return var2;
    }

    protected Map getCommands()
    {
        return MinecraftServer.getServer().getCommandManager().getCommands();
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            Set var4 = this.getCommands().keySet();
            return getListOfStringsMatchingLastWord(args, (String[])var4.toArray(new String[var4.size()]));
        }
        else
        {
            return null;
        }
    }
}
