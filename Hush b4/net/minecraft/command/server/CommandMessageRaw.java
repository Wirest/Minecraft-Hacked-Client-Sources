// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import com.google.gson.JsonParseException;
import net.minecraft.command.SyntaxErrorException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandMessageRaw extends CommandBase
{
    @Override
    public String getCommandName() {
        return "tellraw";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.tellraw.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        final EntityPlayer entityplayer = CommandBase.getPlayer(sender, args[0]);
        final String s = CommandBase.buildString(args, 1);
        try {
            final IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
            entityplayer.addChatMessage(ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayer));
        }
        catch (JsonParseException jsonparseexception) {
            final Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
            throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { (throwable == null) ? "" : throwable.getMessage() });
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
