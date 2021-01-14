package net.minecraft.command;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase {
   private static final Map SHORTCUTS = Maps.newHashMap();

   public String getCommandName() {
      return "replaceitem";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.replaceitem.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
      } else {
         boolean flag;
         if (args[0].equals("entity")) {
            flag = false;
         } else {
            if (!args[0].equals("block")) {
               throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
            }

            flag = true;
         }

         byte i;
         if (flag) {
            if (args.length < 6) {
               throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
            }

            i = 4;
         } else {
            if (args.length < 4) {
               throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
            }

            i = 2;
         }

         int i = i + 1;
         int j = this.getSlotForShortcut(args[i]);

         Item item;
         try {
            item = getItemByText(sender, args[i]);
         } catch (NumberInvalidException var15) {
            if (Block.getBlockFromName(args[i]) != Blocks.air) {
               throw var15;
            }

            item = null;
         }

         ++i;
         int k = args.length > i ? parseInt(args[i++], 1, 64) : 1;
         int l = args.length > i ? parseInt(args[i++]) : 0;
         ItemStack itemstack = new ItemStack(item, k, l);
         if (args.length > i) {
            String s = getChatComponentFromNthArg(sender, args, i).getUnformattedText();

            try {
               itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
            } catch (NBTException var14) {
               throw new CommandException("commands.replaceitem.tagError", new Object[]{var14.getMessage()});
            }
         }

         if (itemstack.getItem() == null) {
            itemstack = null;
         }

         if (flag) {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            BlockPos blockpos = parseBlockPos(sender, args, 1, false);
            World world = sender.getEntityWorld();
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null || !(tileentity instanceof IInventory)) {
               throw new CommandException("commands.replaceitem.noContainer", new Object[]{blockpos.getX(), blockpos.getY(), blockpos.getZ()});
            }

            IInventory iinventory = (IInventory)tileentity;
            if (j >= 0 && j < iinventory.getSizeInventory()) {
               iinventory.setInventorySlotContents(j, itemstack);
            }
         } else {
            Entity entity = func_175768_b(sender, args[1]);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (entity instanceof EntityPlayer) {
               ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }

            if (!entity.replaceItemInInventory(j, itemstack)) {
               throw new CommandException("commands.replaceitem.failed", new Object[]{j, k, itemstack == null ? "Air" : itemstack.getChatComponent()});
            }

            if (entity instanceof EntityPlayer) {
               ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }
         }

         sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
         notifyOperators(sender, this, "commands.replaceitem.success", new Object[]{j, k, itemstack == null ? "Air" : itemstack.getChatComponent()});
      }
   }

   private int getSlotForShortcut(String shortcut) throws CommandException {
      if (!SHORTCUTS.containsKey(shortcut)) {
         throw new CommandException("commands.generic.parameter.invalid", new Object[]{shortcut});
      } else {
         return (Integer)SHORTCUTS.get(shortcut);
      }
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[]{"entity", "block"}) : (args.length == 2 && args[0].equals("entity") ? getListOfStringsMatchingLastWord(args, this.getUsernames()) : (args.length >= 2 && args.length <= 4 && args[0].equals("block") ? func_175771_a(args, 1, pos) : (args.length == 3 && args[0].equals("entity") || args.length == 5 && args[0].equals("block") ? getListOfStringsMatchingLastWord(args, SHORTCUTS.keySet()) : (args.length == 4 && args[0].equals("entity") || args.length == 6 && args[0].equals("block") ? getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : null))));
   }

   protected String[] getUsernames() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   public boolean isUsernameIndex(String[] args, int index) {
      return args.length > 0 && args[0].equals("entity") && index == 1;
   }

   static {
      int j1;
      for(j1 = 0; j1 < 54; ++j1) {
         SHORTCUTS.put("slot.container." + j1, j1);
      }

      for(j1 = 0; j1 < 9; ++j1) {
         SHORTCUTS.put("slot.hotbar." + j1, j1);
      }

      for(j1 = 0; j1 < 27; ++j1) {
         SHORTCUTS.put("slot.inventory." + j1, 9 + j1);
      }

      for(j1 = 0; j1 < 27; ++j1) {
         SHORTCUTS.put("slot.enderchest." + j1, 200 + j1);
      }

      for(j1 = 0; j1 < 8; ++j1) {
         SHORTCUTS.put("slot.villager." + j1, 300 + j1);
      }

      for(j1 = 0; j1 < 15; ++j1) {
         SHORTCUTS.put("slot.horse." + j1, 500 + j1);
      }

      SHORTCUTS.put("slot.weapon", 99);
      SHORTCUTS.put("slot.armor.head", 103);
      SHORTCUTS.put("slot.armor.chest", 102);
      SHORTCUTS.put("slot.armor.legs", 101);
      SHORTCUTS.put("slot.armor.feet", 100);
      SHORTCUTS.put("slot.horse.saddle", 400);
      SHORTCUTS.put("slot.horse.armor", 401);
      SHORTCUTS.put("slot.horse.chest", 499);
   }
}
