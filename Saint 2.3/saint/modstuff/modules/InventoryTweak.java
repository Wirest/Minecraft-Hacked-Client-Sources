package saint.modstuff.modules;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class InventoryTweak extends Module {
   private final Value id = new Value("inventorytweak_id", 1);

   public InventoryTweak() {
      super("InventoryTweak", -2252579, ModManager.Category.PLAYER);
      this.setTag("Inventory Tweak");
      Saint.getCommandManager().getContentList().add(new Command("inventorytweakid", "<id>", new String[]{"tweakid", "iti"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               InventoryTweak.this.id.setValueState((Integer)InventoryTweak.this.id.getDefaultValue());
            } else {
               InventoryTweak.this.id.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)InventoryTweak.this.id.getValueState() > 431) {
               InventoryTweak.this.id.setValueState(431);
            } else if ((Integer)InventoryTweak.this.id.getValueState() < 1) {
               InventoryTweak.this.id.setValueState(1);
            }

            Logger.writeChat("Inventory Tweak Item Id set to: " + InventoryTweak.this.id.getValueState());
         }
      });
   }

   private int findInventoryItem(int itemID) {
      for(int o = 9; o < 45; ++o) {
         if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
            ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == itemID) {
               return o;
            }
         }
      }

      return -1;
   }

   private int findHotbarItem(int itemID) {
      for(int o = 0; o < 9; ++o) {
         ItemStack item = mc.thePlayer.inventory.getStackInSlot(o);
         if (item != null && Item.getIdFromItem(item.getItem()) == itemID) {
            return o;
         }
      }

      return -1;
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion && (mc.thePlayer.inventory.getCurrentItem() == null || mc.thePlayer.inventory.getCurrentItem().getItem() == null)) {
         int slotId = this.findInventoryItem((Integer)this.id.getValueState());
         if (slotId != -1) {
            mc.playerController.windowClick(0, slotId, mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
         }
      }

   }
}
