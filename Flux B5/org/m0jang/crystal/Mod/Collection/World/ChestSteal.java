package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.m0jang.crystal.Events.EventRespawn;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Misc.InventoryCleaner;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.InvUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class ChestSteal extends Module {
   private TimeHelper timer = new TimeHelper();
   private int picked;
   private boolean fast;

   public ChestSteal() {
      super("ChestSteal", Category.World, false);
   }

   public void onEnable() {
      super.onEnable();
      this.picked = 0;
      this.fast = true;
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onRespawn(EventRespawn eventRespawn) {
      this.setEnabled(false);
      ChatUtils.sendMessageToPlayer("Auto Disabled \247a" + this.getName() + "\247r by Respawn.");
   }

   @EventTarget
   private void onTick(EventTick event) {
      if (Minecraft.theWorld != null && !InvUtils.isFullInv()) {
         if (!(this.mc.currentScreen instanceof GuiChest)) {
            this.picked = 0;
            this.fast = true;
         } else {
            if (this.picked >= 10) {
               this.fast = !this.fast;
               this.picked = 0;
            }

            int delay = this.fast ? 80 : 100;
            if (this.timer.hasPassed((double)delay) && !this.mc.inGameHasFocus && this.mc.currentScreen instanceof GuiChest) {
               if (!this.isEmpty(Minecraft.thePlayer.openContainer)) {
                  int index = this.getNextSlot(Minecraft.thePlayer.openContainer);
                  this.mc.playerController.windowClick(Minecraft.thePlayer.openContainer.windowId, index, 0, 1, Minecraft.thePlayer);
                  ++this.picked;
               } else {
                  Minecraft.thePlayer.closeScreen();
                  ChestAura.setClosed(true);
                  this.picked = 0;
                  this.fast = true;
               }

               this.timer.reset();
            }

         }
      }
   }

   private int getNextSlot(Container container) {
      int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;

      for(int i = 0; i < slotAmount; ++i) {
         if (container.getInventory().get(i) != null) {
            return i;
         }
      }

      return -1;
   }

   public boolean isEmpty(Container container) {
      int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;

      for(int i = 0; i < slotAmount; ++i) {
         if (container.getSlot(i).getHasStack()) {
            return false;
         }
      }

      return true;
   }

   public static boolean isShit(ItemStack itemStack) {
      if (itemStack == null) {
         return false;
      } else if (itemStack.getItem().getUnlocalizedName().contains("bow")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("arrow")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("stick")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("egg")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("stick")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("string")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("flint")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("compass")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("feather")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("bucket")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("snow")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("fish")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("enchant")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("exp")) {
         return true;
      } else if (itemStack.getItem() instanceof ItemPickaxe) {
         return true;
      } else if (itemStack.getItem() instanceof ItemTool) {
         return true;
      } else {
         return itemStack.getItem().getUnlocalizedName().contains("potion") && InventoryCleaner.isBadPotion(itemStack);
      }
   }
}
