// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Set;
import net.minecraft.util.BlockPos;
import java.util.Collections;
import net.minecraft.server.MinecraftServer;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import java.util.Arrays;
import java.util.List;

public class CommandHelp extends CommandBase
{
    @Override
    public String getCommandName() {
        return "help";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.help.usage";
    }
    
    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("?");
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final List<ICommand> list = this.getSortedPossibleCommands(sender);
        final int i = 7;
        final int j = (list.size() - 1) / 7;
        int k = 0;
        try {
            k = ((args.length == 0) ? 0 : (CommandBase.parseInt(args[0], 1, j + 1) - 1));
        }
        catch (NumberInvalidException numberinvalidexception) {
            final Map<String, ICommand> map = this.getCommands();
            final ICommand icommand = map.get(args[0]);
            if (icommand != null) {
                throw new WrongUsageException(icommand.getCommandUsage(sender), new Object[0]);
            }
            if (MathHelper.parseIntWithDefault(args[0], -1) != -1) {
                throw numberinvalidexception;
            }
            throw new CommandNotFoundException();
        }
        final int l = Math.min((k + 1) * 7, list.size());
        final ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.help.header", new Object[] { k + 1, j + 1 });
        chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        sender.addChatMessage(chatcomponenttranslation1);
        for (int i2 = k * 7; i2 < l; ++i2) {
            final ICommand icommand2 = list.get(i2);
            final ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation(icommand2.getCommandUsage(sender), new Object[0]);
            chatcomponenttranslation2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand2.getCommandName() + " "));
            sender.addChatMessage(chatcomponenttranslation2);
        }
        if (k == 0 && sender instanceof EntityPlayer) {
            final ChatComponentTranslation chatcomponenttranslation3 = new ChatComponentTranslation("commands.help.footer", new Object[0]);
            chatcomponenttranslation3.getChatStyle().setColor(EnumChatFormatting.GREEN);
            sender.addChatMessage(chatcomponenttranslation3);
        }
    }
    
    protected List<ICommand> getSortedPossibleCommands(final ICommandSender p_71534_1_) {
        final List<ICommand> list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(p_71534_1_);
        Collections.sort(list);
        return list;
    }
    
    protected Map<String, ICommand> getCommands() {
        return MinecraftServer.getServer().getCommandManager().getCommands();
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            final Set<String> set = this.getCommands().keySet();
            return CommandBase.getListOfStringsMatchingLastWord(args, (String[])set.toArray(new String[set.size()]));
        }
        return null;
    }
}
