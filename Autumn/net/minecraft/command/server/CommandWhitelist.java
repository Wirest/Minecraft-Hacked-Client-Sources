package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandWhitelist extends CommandBase {
   public String getCommandName() {
      return "whitelist";
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.whitelist.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
      } else {
         MinecraftServer minecraftserver = MinecraftServer.getServer();
         if (args[0].equals("on")) {
            minecraftserver.getConfigurationManager().setWhiteListEnabled(true);
            notifyOperators(sender, this, "commands.whitelist.enabled", new Object[0]);
         } else if (args[0].equals("off")) {
            minecraftserver.getConfigurationManager().setWhiteListEnabled(false);
            notifyOperators(sender, this, "commands.whitelist.disabled", new Object[0]);
         } else if (args[0].equals("list")) {
            sender.addChatMessage(new ChatComponentTranslation("commands.whitelist.list", new Object[]{minecraftserver.getConfigurationManager().getWhitelistedPlayerNames().length, minecraftserver.getConfigurationManager().getAvailablePlayerDat().length}));
            String[] astring = minecraftserver.getConfigurationManager().getWhitelistedPlayerNames();
            sender.addChatMessage(new ChatComponentText(joinNiceString(astring)));
         } else {
            GameProfile gameprofile1;
            if (args[0].equals("add")) {
               if (args.length < 2) {
                  throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
               }

               gameprofile1 = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[1]);
               if (gameprofile1 == null) {
                  throw new CommandException("commands.whitelist.add.failed", new Object[]{args[1]});
               }

               minecraftserver.getConfigurationManager().addWhitelistedPlayer(gameprofile1);
               notifyOperators(sender, this, "commands.whitelist.add.success", new Object[]{args[1]});
            } else if (args[0].equals("remove")) {
               if (args.length < 2) {
                  throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
               }

               gameprofile1 = minecraftserver.getConfigurationManager().getWhitelistedPlayers().func_152706_a(args[1]);
               if (gameprofile1 == null) {
                  throw new CommandException("commands.whitelist.remove.failed", new Object[]{args[1]});
               }

               minecraftserver.getConfigurationManager().removePlayerFromWhitelist(gameprofile1);
               notifyOperators(sender, this, "commands.whitelist.remove.success", new Object[]{args[1]});
            } else if (args[0].equals("reload")) {
               minecraftserver.getConfigurationManager().loadWhiteList();
               notifyOperators(sender, this, "commands.whitelist.reloaded", new Object[0]);
            }
         }

      }
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length == 1) {
         return getListOfStringsMatchingLastWord(args, new String[]{"on", "off", "list", "add", "remove", "reload"});
      } else {
         if (args.length == 2) {
            if (args[0].equals("remove")) {
               return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
            }

            if (args[0].equals("add")) {
               return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getPlayerProfileCache().getUsernames());
            }
         }

         return null;
      }
   }
}
