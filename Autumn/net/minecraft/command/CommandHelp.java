package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

public class CommandHelp extends CommandBase {
   public String getCommandName() {
      return "help";
   }

   public int getRequiredPermissionLevel() {
      return 0;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.help.usage";
   }

   public List getCommandAliases() {
      return Arrays.asList("?");
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      List list = this.getSortedPossibleCommands(sender);
      int i = true;
      int j = (list.size() - 1) / 7;
      boolean var6 = false;

      int k;
      try {
         k = args.length == 0 ? 0 : parseInt(args[0], 1, j + 1) - 1;
      } catch (NumberInvalidException var12) {
         Map map = this.getCommands();
         ICommand icommand = (ICommand)map.get(args[0]);
         if (icommand != null) {
            throw new WrongUsageException(icommand.getCommandUsage(sender), new Object[0]);
         }

         if (MathHelper.parseIntWithDefault(args[0], -1) != -1) {
            throw var12;
         }

         throw new CommandNotFoundException();
      }

      int l = Math.min((k + 1) * 7, list.size());
      ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.help.header", new Object[]{k + 1, j + 1});
      chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
      sender.addChatMessage(chatcomponenttranslation1);

      for(int i1 = k * 7; i1 < l; ++i1) {
         ICommand icommand1 = (ICommand)list.get(i1);
         ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(icommand1.getCommandUsage(sender), new Object[0]);
         chatcomponenttranslation.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getCommandName() + " "));
         sender.addChatMessage(chatcomponenttranslation);
      }

      if (k == 0 && sender instanceof EntityPlayer) {
         ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.help.footer", new Object[0]);
         chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.GREEN);
         sender.addChatMessage(chatcomponenttranslation2);
      }

   }

   protected List getSortedPossibleCommands(ICommandSender p_71534_1_) {
      List list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(p_71534_1_);
      Collections.sort(list);
      return list;
   }

   protected Map getCommands() {
      return MinecraftServer.getServer().getCommandManager().getCommands();
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length == 1) {
         Set set = this.getCommands().keySet();
         return getListOfStringsMatchingLastWord(args, (String[])((String[])set.toArray(new String[set.size()])));
      } else {
         return null;
      }
   }
}
