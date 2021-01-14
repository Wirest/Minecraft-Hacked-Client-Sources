package net.minecraft.command;

import java.util.Iterator;

import net.minecraft.command.common.CommandReplaceItem;
import net.minecraft.command.server.CommandAchievement;
import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class ServerCommandManager extends CommandHandler implements IAdminCommand {
    private static final String __OBFID = "CL_00000922";

    public ServerCommandManager() {
        registerCommand(new CommandTime());
        registerCommand(new CommandGameMode());
        registerCommand(new CommandDifficulty());
        registerCommand(new CommandDefaultGameMode());
        registerCommand(new CommandKill());
        registerCommand(new CommandToggleDownfall());
        registerCommand(new CommandWeather());
        registerCommand(new CommandXP());
        registerCommand(new CommandTeleport());
        registerCommand(new CommandGive());
        registerCommand(new CommandReplaceItem());
        registerCommand(new CommandStats());
        registerCommand(new CommandEffect());
        registerCommand(new CommandEnchant());
        registerCommand(new CommandParticle());
        registerCommand(new CommandEmote());
        registerCommand(new CommandShowSeed());
        registerCommand(new CommandHelp());
        registerCommand(new CommandDebug());
        registerCommand(new CommandMessage());
        registerCommand(new CommandBroadcast());
        registerCommand(new CommandSetSpawnpoint());
        registerCommand(new CommandSetDefaultSpawnpoint());
        registerCommand(new CommandGameRule());
        registerCommand(new CommandClearInventory());
        registerCommand(new CommandTestFor());
        registerCommand(new CommandSpreadPlayers());
        registerCommand(new CommandPlaySound());
        registerCommand(new CommandScoreboard());
        registerCommand(new CommandExecuteAt());
        registerCommand(new CommandTrigger());
        registerCommand(new CommandAchievement());
        registerCommand(new CommandSummon());
        registerCommand(new CommandSetBlock());
        registerCommand(new CommandFill());
        registerCommand(new CommandClone());
        registerCommand(new CommandCompare());
        registerCommand(new CommandBlockData());
        registerCommand(new CommandTestForBlock());
        registerCommand(new CommandMessageRaw());
        registerCommand(new CommandWorldBorder());
        registerCommand(new CommandTitle());
        registerCommand(new CommandEntityData());

        if (MinecraftServer.getServer().isDedicatedServer()) {
            registerCommand(new CommandOp());
            registerCommand(new CommandDeOp());
            registerCommand(new CommandStop());
            registerCommand(new CommandSaveAll());
            registerCommand(new CommandSaveOff());
            registerCommand(new CommandSaveOn());
            registerCommand(new CommandBanIp());
            registerCommand(new CommandPardonIp());
            registerCommand(new CommandBanPlayer());
            registerCommand(new CommandListBans());
            registerCommand(new CommandPardonPlayer());
            registerCommand(new CommandServerKick());
            registerCommand(new CommandListPlayers());
            registerCommand(new CommandWhitelist());
            registerCommand(new CommandSetPlayerTimeout());
        } else {
            registerCommand(new CommandPublishLocalServer());
        }

        CommandBase.setAdminCommander(this);
    }

    @Override
    public void notifyOperators(ICommandSender sender, ICommand command, int p_152372_3_, String msgFormat, Object... msgParams) {
        boolean var6 = true;
        MinecraftServer var7 = MinecraftServer.getServer();

        if (!sender.sendCommandFeedback()) {
            var6 = false;
        }

        ChatComponentTranslation var8 = new ChatComponentTranslation("chat.type.admin", new Object[]{sender.getName(), new ChatComponentTranslation(msgFormat, msgParams)});
        var8.getChatStyle().setColor(EnumChatFormatting.GRAY);
        var8.getChatStyle().setItalic(Boolean.valueOf(true));

        if (var6) {
            Iterator var9 = var7.getConfigurationManager().playerEntityList.iterator();

            while (var9.hasNext()) {
                EntityPlayer var10 = (EntityPlayer) var9.next();

                if (var10 != sender && var7.getConfigurationManager().canSendCommands(var10.getGameProfile()) && command.canCommandSenderUseCommand(sender)) {
                    var10.addChatMessage(var8);
                }
            }
        }

        if (sender != var7 && var7.worldServers[0].getGameRules().getGameRuleBooleanValue("logAdminCommands")) {
            var7.addChatMessage(var8);
        }

        boolean var11 = var7.worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");

        if (sender instanceof CommandBlockLogic) {
            var11 = ((CommandBlockLogic) sender).func_175571_m();
        }

        if ((p_152372_3_ & 1) != 1 && var11) {
            sender.addChatMessage(new ChatComponentTranslation(msgFormat, msgParams));
        }
    }
}
