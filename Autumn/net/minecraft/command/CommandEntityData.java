package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class CommandEntityData extends CommandBase {
   public String getCommandName() {
      return "entitydata";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.entitydata.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 2) {
         throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
      } else {
         Entity entity = func_175768_b(sender, args[0]);
         if (entity instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", new Object[]{entity.getDisplayName()});
         } else {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            entity.writeToNBT(nbttagcompound);
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();

            NBTTagCompound nbttagcompound2;
            try {
               nbttagcompound2 = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
            } catch (NBTException var8) {
               throw new CommandException("commands.entitydata.tagError", new Object[]{var8.getMessage()});
            }

            nbttagcompound2.removeTag("UUIDMost");
            nbttagcompound2.removeTag("UUIDLeast");
            nbttagcompound.merge(nbttagcompound2);
            if (nbttagcompound.equals(nbttagcompound1)) {
               throw new CommandException("commands.entitydata.failed", new Object[]{nbttagcompound.toString()});
            } else {
               entity.readFromNBT(nbttagcompound);
               notifyOperators(sender, this, "commands.entitydata.success", new Object[]{nbttagcompound.toString()});
            }
         }
      }
   }

   public boolean isUsernameIndex(String[] args, int index) {
      return index == 0;
   }
}
