package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase {
   private static final String __OBFID = "CL_00001053";

   public boolean canCommandSenderUseCommand(ICommandSender sender) {
      return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(sender);
   }

   public String getCommandName() {
      return "seed";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.seed.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      Object var3 = sender instanceof EntityPlayer ? ((EntityPlayer)sender).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
      sender.addChatMessage(new ChatComponentTranslation("commands.seed.success", new Object[]{((World)var3).getSeed()}));
   }
}
