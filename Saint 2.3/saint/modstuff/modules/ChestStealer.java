package saint.modstuff.modules;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.S30PacketWindowItems;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.EveryTick;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class ChestStealer extends Module {
   private S30PacketWindowItems packet;
   private boolean shouldEmptyChest;
   private int delay = 0;
   private int currentSlot;
   private int[] whitelist = new int[]{54};

   public ChestStealer() {
      super("ChestStealer", -2572328, ModManager.Category.PLAYER);
      this.setTag("Chest Stealer");
   }

   private int getNextSlot(Container container) {
      int i = 0;

      for(int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27; i < slotAmount; ++i) {
         if (container.getInventory().get(i) != null) {
            return i;
         }
      }

      return -1;
   }

   public boolean isContainerEmpty(Container container) {
      boolean temp = true;
      int i = 0;

      for(int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27; i < slotAmount; ++i) {
         if (container.getSlot(i).getHasStack()) {
            temp = false;
         }
      }

      return temp;
   }

   public void onEvent(Event event) {
      if (event instanceof EveryTick) {
         try {
            if (!mc.inGameHasFocus && this.packet != null && mc.thePlayer.openContainer.windowId == this.packet.func_148911_c() && mc.currentScreen instanceof GuiChest) {
               if (!this.isContainerEmpty(mc.thePlayer.openContainer)) {
                  int slotId = this.getNextSlot(mc.thePlayer.openContainer);
                  if (this.delay >= 2) {
                     mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slotId, 0, 1, mc.thePlayer);
                     this.delay = 0;
                  }

                  ++this.delay;
               } else {
                  mc.thePlayer.closeScreen();
                  this.packet = null;
                  Saint.getNotificationManager().addInfo("Container emptied!");
               }
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      } else if (event instanceof RecievePacket) {
         RecievePacket rec = (RecievePacket)event;
         if (rec.getPacket() instanceof S30PacketWindowItems) {
            this.packet = (S30PacketWindowItems)rec.getPacket();
         }
      }

   }
}
