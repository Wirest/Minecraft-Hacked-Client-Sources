// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentProcessor;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S45PacketTitle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle extends CommandBase
{
    private static final Logger LOGGER;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    @Override
    public String getCommandName() {
        return "title";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.title.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        if (args.length < 3) {
            if ("title".equals(args[1]) || "subtitle".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            }
            if ("times".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.times", new Object[0]);
            }
        }
        final EntityPlayerMP entityplayermp = CommandBase.getPlayer(sender, args[0]);
        final S45PacketTitle.Type s45packettitle$type = S45PacketTitle.Type.byName(args[1]);
        if (s45packettitle$type != S45PacketTitle.Type.CLEAR && s45packettitle$type != S45PacketTitle.Type.RESET) {
            if (s45packettitle$type == S45PacketTitle.Type.TIMES) {
                if (args.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                final int i = CommandBase.parseInt(args[2]);
                final int j = CommandBase.parseInt(args[3]);
                final int k = CommandBase.parseInt(args[4]);
                final S45PacketTitle s45packettitle2 = new S45PacketTitle(i, j, k);
                entityplayermp.playerNetServerHandler.sendPacket(s45packettitle2);
                CommandBase.notifyOperators(sender, this, "commands.title.success", new Object[0]);
            }
            else {
                if (args.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                final String s = CommandBase.buildString(args, 2);
                IChatComponent ichatcomponent;
                try {
                    ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                }
                catch (JsonParseException jsonparseexception) {
                    final Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
                    throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { (throwable == null) ? "" : throwable.getMessage() });
                }
                final S45PacketTitle s45packettitle3 = new S45PacketTitle(s45packettitle$type, ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayermp));
                entityplayermp.playerNetServerHandler.sendPacket(s45packettitle3);
                CommandBase.notifyOperators(sender, this, "commands.title.success", new Object[0]);
            }
        }
        else {
            if (args.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            final S45PacketTitle s45packettitle4 = new S45PacketTitle(s45packettitle$type, null);
            entityplayermp.playerNetServerHandler.sendPacket(s45packettitle4);
            CommandBase.notifyOperators(sender, this, "commands.title.success", new Object[0]);
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.getNames()) : null);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
