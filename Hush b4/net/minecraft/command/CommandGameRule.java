// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;

public class CommandGameRule extends CommandBase
{
    @Override
    public String getCommandName() {
        return "gamerule";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.gamerule.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final GameRules gamerules = this.getGameRules();
        final String s = (args.length > 0) ? args[0] : "";
        final String s2 = (args.length > 1) ? CommandBase.buildString(args, 1) : "";
        switch (args.length) {
            case 0: {
                sender.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(gamerules.getRules())));
                break;
            }
            case 1: {
                if (!gamerules.hasRule(s)) {
                    throw new CommandException("commands.gamerule.norule", new Object[] { s });
                }
                final String s3 = gamerules.getString(s);
                sender.addChatMessage(new ChatComponentText(s).appendText(" = ").appendText(s3));
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
                break;
            }
            default: {
                if (gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s2) && !"false".equals(s2)) {
                    throw new CommandException("commands.generic.boolean.invalid", new Object[] { s2 });
                }
                gamerules.setOrCreateGameRule(s, s2);
                func_175773_a(gamerules, s);
                CommandBase.notifyOperators(sender, this, "commands.gamerule.success", new Object[0]);
                break;
            }
        }
    }
    
    public static void func_175773_a(final GameRules p_175773_0_, final String p_175773_1_) {
        if ("reducedDebugInfo".equals(p_175773_1_)) {
            final byte b0 = (byte)(p_175773_0_.getBoolean(p_175773_1_) ? 22 : 23);
            for (final EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().func_181057_v()) {
                entityplayermp.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(entityplayermp, b0));
            }
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, this.getGameRules().getRules());
        }
        if (args.length == 2) {
            final GameRules gamerules = this.getGameRules();
            if (gamerules.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE)) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
            }
        }
        return null;
    }
    
    private GameRules getGameRules() {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
