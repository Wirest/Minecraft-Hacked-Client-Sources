// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;

public class CommandMessage extends CommandBase
{
    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("w", "msg");
    }
    
    @Override
    public String getCommandName() {
        return "tell";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.message.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        final EntityPlayer entityplayer = CommandBase.getPlayer(sender, args[0]);
        if (entityplayer == sender) {
            throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
        }
        final IChatComponent ichatcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
        final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.message.display.incoming", new Object[] { sender.getDisplayName(), ichatcomponent.createCopy() });
        final ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.message.display.outgoing", new Object[] { entityplayer.getDisplayName(), ichatcomponent.createCopy() });
        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        entityplayer.addChatMessage(chatcomponenttranslation);
        sender.addChatMessage(chatcomponenttranslation2);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
