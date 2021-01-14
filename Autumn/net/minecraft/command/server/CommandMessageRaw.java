package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandMessageRaw extends CommandBase {
   public String getCommandName() {
      return "tellraw";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.tellraw.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 2) {
         throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
      } else {
         EntityPlayer entityplayer = getPlayer(sender, args[0]);
         String s = buildString(args, 1);

         try {
            IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
            entityplayer.addChatMessage(ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayer));
         } catch (JsonParseException var7) {
            Throwable throwable = ExceptionUtils.getRootCause(var7);
            throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[]{throwable == null ? "" : throwable.getMessage()});
         }
      }
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
   }

   public boolean isUsernameIndex(String[] args, int index) {
      return index == 0;
   }
}
