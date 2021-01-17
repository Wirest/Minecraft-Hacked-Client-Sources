// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldSettings;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;

public class CommandGameMode extends CommandBase
{
    @Override
    public String getCommandName() {
        return "gamemode";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.gamemode.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        final WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(sender, args[0]);
        final EntityPlayer entityplayer = (args.length >= 2) ? CommandBase.getPlayer(sender, args[1]) : CommandBase.getCommandSenderAsPlayer(sender);
        entityplayer.setGameType(worldsettings$gametype);
        entityplayer.fallDistance = 0.0f;
        if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            entityplayer.addChatMessage(new ChatComponentTranslation("gameMode.changed", new Object[0]));
        }
        final IChatComponent ichatcomponent = new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]);
        if (entityplayer != sender) {
            CommandBase.notifyOperators(sender, this, 1, "commands.gamemode.success.other", entityplayer.getName(), ichatcomponent);
        }
        else {
            CommandBase.notifyOperators(sender, this, 1, "commands.gamemode.success.self", ichatcomponent);
        }
    }
    
    protected WorldSettings.GameType getGameModeFromCommand(final ICommandSender p_71539_1_, final String p_71539_2_) throws CommandException, NumberInvalidException {
        return (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !p_71539_2_.equalsIgnoreCase("s")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !p_71539_2_.equalsIgnoreCase("c")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !p_71539_2_.equalsIgnoreCase("a")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !p_71539_2_.equalsIgnoreCase("sp")) ? WorldSettings.getGameTypeById(CommandBase.parseInt(p_71539_2_, 0, WorldSettings.GameType.values().length - 2)) : WorldSettings.GameType.SPECTATOR) : WorldSettings.GameType.ADVENTURE) : WorldSettings.GameType.CREATIVE) : WorldSettings.GameType.SURVIVAL;
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator") : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getListOfPlayerUsernames()) : null);
    }
    
    protected String[] getListOfPlayerUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 1;
    }
}
