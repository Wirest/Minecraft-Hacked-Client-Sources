// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.List;
import net.minecraft.util.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandWhitelist extends CommandBase
{
    @Override
    public String getCommandName() {
        return "whitelist";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.whitelist.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        }
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (args[0].equals("on")) {
            minecraftserver.getConfigurationManager().setWhiteListEnabled(true);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.enabled", new Object[0]);
        }
        else if (args[0].equals("off")) {
            minecraftserver.getConfigurationManager().setWhiteListEnabled(false);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.disabled", new Object[0]);
        }
        else if (args[0].equals("list")) {
            sender.addChatMessage(new ChatComponentTranslation("commands.whitelist.list", new Object[] { minecraftserver.getConfigurationManager().getWhitelistedPlayerNames().length, minecraftserver.getConfigurationManager().getAvailablePlayerDat().length }));
            final String[] astring = minecraftserver.getConfigurationManager().getWhitelistedPlayerNames();
            sender.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(astring)));
        }
        else if (args[0].equals("add")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
            }
            final GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[1]);
            if (gameprofile == null) {
                throw new CommandException("commands.whitelist.add.failed", new Object[] { args[1] });
            }
            minecraftserver.getConfigurationManager().addWhitelistedPlayer(gameprofile);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.add.success", args[1]);
        }
        else if (args[0].equals("remove")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
            }
            final GameProfile gameprofile2 = minecraftserver.getConfigurationManager().getWhitelistedPlayers().func_152706_a(args[1]);
            if (gameprofile2 == null) {
                throw new CommandException("commands.whitelist.remove.failed", new Object[] { args[1] });
            }
            minecraftserver.getConfigurationManager().removePlayerFromWhitelist(gameprofile2);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.remove.success", args[1]);
        }
        else if (args[0].equals("reload")) {
            minecraftserver.getConfigurationManager().loadWhiteList();
            CommandBase.notifyOperators(sender, this, "commands.whitelist.reloaded", new Object[0]);
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "on", "off", "list", "add", "remove", "reload");
        }
        if (args.length == 2) {
            if (args[0].equals("remove")) {
                return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
            }
            if (args[0].equals("add")) {
                return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getPlayerProfileCache().getUsernames());
            }
        }
        return null;
    }
}
