// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.command.CommandException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.command.ICommand;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSaveAll extends CommandBase
{
    @Override
    public String getCommandName() {
        return "save-all";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.save.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        sender.addChatMessage(new ChatComponentTranslation("commands.save.start", new Object[0]));
        if (minecraftserver.getConfigurationManager() != null) {
            minecraftserver.getConfigurationManager().saveAllPlayerData();
        }
        try {
            for (int i = 0; i < minecraftserver.worldServers.length; ++i) {
                if (minecraftserver.worldServers[i] != null) {
                    final WorldServer worldserver = minecraftserver.worldServers[i];
                    final boolean flag = worldserver.disableLevelSaving;
                    worldserver.disableLevelSaving = false;
                    worldserver.saveAllChunks(true, null);
                    worldserver.disableLevelSaving = flag;
                }
            }
            if (args.length > 0 && "flush".equals(args[0])) {
                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushStart", new Object[0]));
                for (int j = 0; j < minecraftserver.worldServers.length; ++j) {
                    if (minecraftserver.worldServers[j] != null) {
                        final WorldServer worldserver2 = minecraftserver.worldServers[j];
                        final boolean flag2 = worldserver2.disableLevelSaving;
                        worldserver2.disableLevelSaving = false;
                        worldserver2.saveChunkData();
                        worldserver2.disableLevelSaving = flag2;
                    }
                }
                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException minecraftexception) {
            CommandBase.notifyOperators(sender, this, "commands.save.failed", minecraftexception.getMessage());
            return;
        }
        CommandBase.notifyOperators(sender, this, "commands.save.success", new Object[0]);
    }
}
