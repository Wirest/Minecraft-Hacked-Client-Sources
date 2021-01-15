package saint.modstuff.modules;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.Module;
import saint.utilities.TimeHelper;

public class AutoArmor extends Module {
   private final int[] boots = new int[]{313, 309, 317, 305, 301};
   private final int[] chestplate = new int[]{311, 307, 315, 303, 299};
   private final int[] helmet = new int[]{310, 306, 314, 302, 298};
   private final int[] leggings = new int[]{312, 308, 316, 304, 300};
   private final TimeHelper time = new TimeHelper();

   public AutoArmor() {
      super("AutoArmor");
      this.setEnabled(true);
   }

   public boolean armourIsBetter(int slot, int[] armourtype) {
      if (mc.thePlayer.inventory.armorInventory[slot] != null) {
         int currentIndex = 0;
         int finalCurrentIndex = -1;
         int invIndex = 0;
         int finalInvIndex = -1;
         int[] arrayOfInt = armourtype;
         int j = armourtype.length;

         int i;
         int armour;
         for(i = 0; i < j; ++i) {
            armour = arrayOfInt[i];
            if (Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[slot].getItem()) == armour) {
               finalCurrentIndex = currentIndex;
               break;
            }

            ++currentIndex;
         }

         arrayOfInt = armourtype;
         j = armourtype.length;

         for(i = 0; i < j; ++i) {
            armour = arrayOfInt[i];
            if (this.findItem(armour) != -1) {
               finalInvIndex = invIndex;
               break;
            }

            ++invIndex;
         }

         if (finalInvIndex > -1) {
            if (finalInvIndex < finalCurrentIndex) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   private int findItem(int id) {
      for(int index = 9; index < 45; ++index) {
         ItemStack item = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (item != null && Item.getIdFromItem(item.getItem()) == id) {
            return index;
         }
      }

      return -1;
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (!this.time.hasReached(65L)) {
            return;
         }

         if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0) {
            return;
         }

         int item = -1;
         int[] arrayOfInt;
         int j;
         int i;
         int id;
         if (mc.thePlayer.inventory.armorInventory[0] == null) {
            j = (arrayOfInt = this.boots).length;

            for(i = 0; i < j; ++i) {
               id = arrayOfInt[i];
               if (this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if (this.armourIsBetter(0, this.boots)) {
            item = 8;
         }

         if (mc.thePlayer.inventory.armorInventory[3] == null) {
            j = (arrayOfInt = this.helmet).length;

            for(i = 0; i < j; ++i) {
               id = arrayOfInt[i];
               if (this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if (this.armourIsBetter(3, this.helmet)) {
            item = 5;
         }

         if (mc.thePlayer.inventory.armorInventory[1] == null) {
            j = (arrayOfInt = this.leggings).length;

            for(i = 0; i < j; ++i) {
               id = arrayOfInt[i];
               if (this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if (this.armourIsBetter(1, this.leggings)) {
            item = 7;
         }

         if (mc.thePlayer.inventory.armorInventory[2] == null) {
            j = (arrayOfInt = this.chestplate).length;

            for(i = 0; i < j; ++i) {
               id = arrayOfInt[i];
               if (this.findItem(id) != -1) {
                  item = this.findItem(id);
                  break;
               }
            }
         }

         if (this.armourIsBetter(2, this.chestplate)) {
            item = 6;
         }

         if (item != -1) {
            mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
            mc.playerController.updateController();
            this.time.setLastMS(this.time.getCurrentMS());
            return;
         }
      }

   }
}
