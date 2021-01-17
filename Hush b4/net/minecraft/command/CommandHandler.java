// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.util.Set;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class CommandHandler implements ICommandManager
{
    private static final Logger logger;
    private final Map<String, ICommand> commandMap;
    private final Set<ICommand> commandSet;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public CommandHandler() {
        this.commandMap = (Map<String, ICommand>)Maps.newHashMap();
        this.commandSet = (Set<ICommand>)Sets.newHashSet();
    }
    
    @Override
    public int executeCommand(final ICommandSender sender, String rawCommand) {
        rawCommand = rawCommand.trim();
        if (rawCommand.startsWith("/")) {
            rawCommand = rawCommand.substring(1);
        }
        String[] astring = rawCommand.split(" ");
        final String s = astring[0];
        astring = dropFirstString(astring);
        final ICommand icommand = this.commandMap.get(s);
        final int i = this.getUsernameIndex(icommand, astring);
        int j = 0;
        if (icommand == null) {
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation);
        }
        else if (icommand.canCommandSenderUseCommand(sender)) {
            if (i > -1) {
                final List<Entity> list = PlayerSelector.matchEntities(sender, astring[i], (Class<? extends Entity>)Entity.class);
                final String s2 = astring[i];
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                for (final Entity entity : list) {
                    astring[i] = entity.getUniqueID().toString();
                    if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                        ++j;
                    }
                }
                astring[i] = s2;
            }
            else {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                    ++j;
                }
            }
        }
        else {
            final ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
            chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation2);
        }
        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
        return j;
    }
    
    protected boolean tryExecute(final ICommandSender sender, final String[] args, final ICommand command, final String input) {
        try {
            command.processCommand(sender, args);
            return true;
        }
        catch (WrongUsageException wrongusageexception) {
            final ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.usage", new Object[] { new ChatComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects()) });
            chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation2);
        }
        catch (CommandException commandexception) {
            final ChatComponentTranslation chatcomponenttranslation3 = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatcomponenttranslation3.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation3);
        }
        catch (Throwable var9) {
            final ChatComponentTranslation chatcomponenttranslation4 = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            chatcomponenttranslation4.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation4);
            CommandHandler.logger.warn("Couldn't process command: '" + input + "'");
        }
        return false;
    }
    
    public ICommand registerCommand(final ICommand command) {
        this.commandMap.put(command.getCommandName(), command);
        this.commandSet.add(command);
        for (final String s : command.getCommandAliases()) {
            final ICommand icommand = this.commandMap.get(s);
            if (icommand == null || !icommand.getCommandName().equals(s)) {
                this.commandMap.put(s, command);
            }
        }
        return command;
    }
    
    private static String[] dropFirstString(final String[] input) {
        final String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }
    
    @Override
    public List<String> getTabCompletionOptions(final ICommandSender sender, final String input, final BlockPos pos) {
        final String[] astring = input.split(" ", -1);
        final String s = astring[0];
        if (astring.length == 1) {
            final List<String> list = (List<String>)Lists.newArrayList();
            for (final Map.Entry<String, ICommand> entry : this.commandMap.entrySet()) {
                if (CommandBase.doesStringStartWith(s, entry.getKey()) && entry.getValue().canCommandSenderUseCommand(sender)) {
                    list.add(entry.getKey());
                }
            }
            return list;
        }
        if (astring.length > 1) {
            final ICommand icommand = this.commandMap.get(s);
            if (icommand != null && icommand.canCommandSenderUseCommand(sender)) {
                return icommand.addTabCompletionOptions(sender, dropFirstString(astring), pos);
            }
        }
        return null;
    }
    
    @Override
    public List<ICommand> getPossibleCommands(final ICommandSender sender) {
        final List<ICommand> list = (List<ICommand>)Lists.newArrayList();
        for (final ICommand icommand : this.commandSet) {
            if (icommand.canCommandSenderUseCommand(sender)) {
                list.add(icommand);
            }
        }
        return list;
    }
    
    @Override
    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }
    
    private int getUsernameIndex(final ICommand command, final String[] args) {
        if (command == null) {
            return -1;
        }
        for (int i = 0; i < args.length; ++i) {
            if (command.isUsernameIndex(args, i) && PlayerSelector.matchesMultiplePlayers(args[i])) {
                return i;
            }
        }
        return -1;
    }
}
