// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandListBans extends CommandBase
{
    @Override
    public String getCommandName() {
        return "banlist";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return (MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() || MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer()) && super.canCommandSenderUseCommand(sender);
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.banlist.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length >= 1 && args[0].equalsIgnoreCase("ips")) {
            sender.addChatMessage(new ChatComponentTranslation("commands.banlist.ips", new Object[] { MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys().length }));
            sender.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys())));
        }
        else {
            sender.addChatMessage(new ChatComponentTranslation("commands.banlist.players", new Object[] { MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys().length }));
            sender.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys())));
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "players", "ips") : null;
    }
}
