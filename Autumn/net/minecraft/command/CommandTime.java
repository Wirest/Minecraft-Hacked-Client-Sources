package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase {
   public String getCommandName() {
      return "time";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.time.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length > 1) {
         int i;
         if (args[0].equals("set")) {
            if (args[1].equals("day")) {
               i = 1000;
            } else if (args[1].equals("night")) {
               i = 13000;
            } else {
               i = parseInt(args[1], 0);
            }

            this.setTime(sender, i);
            notifyOperators(sender, this, "commands.time.set", new Object[]{i});
            return;
         }

         if (args[0].equals("add")) {
            i = parseInt(args[1], 0);
            this.addTime(sender, i);
            notifyOperators(sender, this, "commands.time.added", new Object[]{i});
            return;
         }

         if (args[0].equals("query")) {
            if (args[1].equals("daytime")) {
               i = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
               sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
               notifyOperators(sender, this, "commands.time.query", new Object[]{i});
               return;
            }

            if (args[1].equals("gametime")) {
               i = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
               sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
               notifyOperators(sender, this, "commands.time.query", new Object[]{i});
               return;
            }
         }
      }

      throw new WrongUsageException("commands.time.usage", new Object[0]);
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[]{"set", "add", "query"}) : (args.length == 2 && args[0].equals("set") ? getListOfStringsMatchingLastWord(args, new String[]{"day", "night"}) : (args.length == 2 && args[0].equals("query") ? getListOfStringsMatchingLastWord(args, new String[]{"daytime", "gametime"}) : null));
   }

   protected void setTime(ICommandSender p_71552_1_, int p_71552_2_) {
      for(int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
         MinecraftServer.getServer().worldServers[i].setWorldTime((long)p_71552_2_);
      }

   }

   protected void addTime(ICommandSender p_71553_1_, int p_71553_2_) {
      for(int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
         WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
         worldserver.setWorldTime(worldserver.getWorldTime() + (long)p_71553_2_);
      }

   }
}
