package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase {
   public String getCommandName() {
      return "save-all";
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.save.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      MinecraftServer minecraftserver = MinecraftServer.getServer();
      sender.addChatMessage(new ChatComponentTranslation("commands.save.start", new Object[0]));
      if (minecraftserver.getConfigurationManager() != null) {
         minecraftserver.getConfigurationManager().saveAllPlayerData();
      }

      try {
         int j;
         WorldServer worldserver1;
         boolean flag1;
         for(j = 0; j < minecraftserver.worldServers.length; ++j) {
            if (minecraftserver.worldServers[j] != null) {
               worldserver1 = minecraftserver.worldServers[j];
               flag1 = worldserver1.disableLevelSaving;
               worldserver1.disableLevelSaving = false;
               worldserver1.saveAllChunks(true, (IProgressUpdate)null);
               worldserver1.disableLevelSaving = flag1;
            }
         }

         if (args.length > 0 && "flush".equals(args[0])) {
            sender.addChatMessage(new ChatComponentTranslation("commands.save.flushStart", new Object[0]));

            for(j = 0; j < minecraftserver.worldServers.length; ++j) {
               if (minecraftserver.worldServers[j] != null) {
                  worldserver1 = minecraftserver.worldServers[j];
                  flag1 = worldserver1.disableLevelSaving;
                  worldserver1.disableLevelSaving = false;
                  worldserver1.saveChunkData();
                  worldserver1.disableLevelSaving = flag1;
               }
            }

            sender.addChatMessage(new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
         }
      } catch (MinecraftException var7) {
         notifyOperators(sender, this, "commands.save.failed", new Object[]{var7.getMessage()});
         return;
      }

      notifyOperators(sender, this, "commands.save.success", new Object[0]);
   }
}
