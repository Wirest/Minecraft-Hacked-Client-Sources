// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.management.UserList;
import java.util.Iterator;
import net.minecraft.command.ICommand;
import java.util.Date;
import net.minecraft.server.management.IPBanEntry;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.regex.Matcher;
import net.minecraft.util.IChatComponent;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;

public class CommandBanIp extends CommandBase
{
    public static final Pattern field_147211_a;
    
    static {
        field_147211_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    }
    
    @Override
    public String getCommandName() {
        return "ban-ip";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(sender);
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.banip.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length >= 1 && args[0].length() > 1) {
            final IChatComponent ichatcomponent = (args.length >= 2) ? CommandBase.getChatComponentFromNthArg(sender, args, 1) : null;
            final Matcher matcher = CommandBanIp.field_147211_a.matcher(args[0]);
            if (matcher.matches()) {
                this.func_147210_a(sender, args[0], (ichatcomponent == null) ? null : ichatcomponent.getUnformattedText());
            }
            else {
                final EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
                if (entityplayermp == null) {
                    throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
                }
                this.func_147210_a(sender, entityplayermp.getPlayerIP(), (ichatcomponent == null) ? null : ichatcomponent.getUnformattedText());
            }
            return;
        }
        throw new WrongUsageException("commands.banip.usage", new Object[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
    
    protected void func_147210_a(final ICommandSender p_147210_1_, final String p_147210_2_, final String p_147210_3_) {
        final IPBanEntry ipbanentry = new IPBanEntry(p_147210_2_, null, p_147210_1_.getName(), null, p_147210_3_);
        ((UserList<K, IPBanEntry>)MinecraftServer.getServer().getConfigurationManager().getBannedIPs()).addEntry(ipbanentry);
        final List<EntityPlayerMP> list = MinecraftServer.getServer().getConfigurationManager().getPlayersMatchingAddress(p_147210_2_);
        final String[] astring = new String[list.size()];
        int i = 0;
        for (final EntityPlayerMP entityplayermp : list) {
            entityplayermp.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
            astring[i++] = entityplayermp.getName();
        }
        if (list.isEmpty()) {
            CommandBase.notifyOperators(p_147210_1_, this, "commands.banip.success", p_147210_2_);
        }
        else {
            CommandBase.notifyOperators(p_147210_1_, this, "commands.banip.success.players", p_147210_2_, CommandBase.joinNiceString(astring));
        }
    }
}
