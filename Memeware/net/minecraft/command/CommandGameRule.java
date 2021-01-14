package net.minecraft.command;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase {
    private static final String __OBFID = "CL_00000475";

    public String getCommandName() {
        return "gamerule";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.gamerule.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GameRules var3 = this.getGameRules();
        String var4 = args.length > 0 ? args[0] : "";
        String var5 = args.length > 1 ? func_180529_a(args, 1) : "";

        switch (args.length) {
            case 0:
                sender.addChatMessage(new ChatComponentText(joinNiceString(var3.getRules())));
                break;

            case 1:
                if (!var3.hasRule(var4)) {
                    throw new CommandException("commands.gamerule.norule", new Object[]{var4});
                }

                String var6 = var3.getGameRuleStringValue(var4);
                sender.addChatMessage((new ChatComponentText(var4)).appendText(" = ").appendText(var6));
                sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3.getInt(var4));
                break;

            default:
                if (var3.areSameType(var4, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(var5) && !"false".equals(var5)) {
                    throw new CommandException("commands.generic.boolean.invalid", new Object[]{var5});
                }

                var3.setOrCreateGameRule(var4, var5);
                func_175773_a(var3, var4);
                notifyOperators(sender, this, "commands.gamerule.success", new Object[0]);
        }
    }

    public static void func_175773_a(GameRules p_175773_0_, String p_175773_1_) {
        if ("reducedDebugInfo".equals(p_175773_1_)) {
            int var2 = p_175773_0_.getGameRuleBooleanValue(p_175773_1_) ? 22 : 23;
            Iterator var3 = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

            while (var3.hasNext()) {
                EntityPlayerMP var4 = (EntityPlayerMP) var3.next();
                var4.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(var4, (byte) var2));
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, this.getGameRules().getRules());
        } else {
            if (args.length == 2) {
                GameRules var4 = this.getGameRules();

                if (var4.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE)) {
                    return getListOfStringsMatchingLastWord(args, new String[]{"true", "false"});
                }
            }

            return null;
        }
    }

    /**
     * Return the game rule set this command should be able to manipulate.
     */
    private GameRules getGameRules() {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
