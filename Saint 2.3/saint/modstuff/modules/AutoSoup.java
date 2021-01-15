package saint.modstuff.modules;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class AutoSoup extends Module {
   private final Value delay = new Value("autosoup_delay", 500L);
   private final Value health = new Value("autosoup_health", 13.0F);
   private final TimeHelper time = new TimeHelper();

   public AutoSoup() {
      super("AutoSoup", -393339, ModManager.Category.PLAYER);
      Saint.getCommandManager().getContentList().add(new Command("autosoupdelay", "<milliseconds>", new String[]{"soupdelay", "asd"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               AutoSoup.this.delay.setValueState((Long)AutoSoup.this.delay.getDefaultValue());
            } else {
               AutoSoup.this.delay.setValueState(Long.parseLong(message.split(" ")[1]));
            }

            if ((Long)AutoSoup.this.delay.getValueState() > 1000L) {
               AutoSoup.this.delay.setValueState(1000L);
            } else if ((Long)AutoSoup.this.delay.getValueState() < 1L) {
               AutoSoup.this.delay.setValueState(1L);
            }

            Logger.writeChat("Auto Soup Delay set to: " + AutoSoup.this.delay.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("autosouphealth", "<health>", new String[]{"souphealth", "ash"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               AutoSoup.this.health.setValueState((Float)AutoSoup.this.health.getDefaultValue());
            } else {
               AutoSoup.this.health.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)AutoSoup.this.health.getValueState() < 1.0F) {
               AutoSoup.this.health.setValueState(1.0F);
            }

            Logger.writeChat("Auto Soup Health set to: " + AutoSoup.this.health.getValueState());
         }
      });
   }

   private boolean isHotbarFull() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack == null) {
            return false;
         }
      }

      return true;
   }

   private boolean doesHotbarHaveSoups() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSoup(stack)) {
            return true;
         }
      }

      return false;
   }

   private void eatAndDropSoup() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSoup(stack)) {
            this.stackBowls();
            int oldslot = mc.thePlayer.inventory.currentItem;
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index - 36));
            mc.playerController.updateController();
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, stack, 0.0F, 0.0F, 0.0F));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldslot));
            break;
         }
      }

   }

   private void getSoupFromInventory() {
      if (!(mc.currentScreen instanceof GuiChest)) {
         this.stackBowls();

         for(int index = 9; index < 36; ++index) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && this.isStackSoup(stack)) {
               mc.playerController.windowClick(0, index, 0, 1, mc.thePlayer);
               break;
            }
         }

      }
   }

   private boolean isStackSoup(ItemStack stack) {
      return stack == null ? false : stack.getItem() instanceof ItemSoup;
   }

   public void onEvent(Event event) {
      if (event instanceof PostMotion) {
         if (this.updateCounter() == 0) {
            return;
         }

         if (mc.thePlayer.getHealth() <= (Float)this.health.getValueState() && this.time.hasReached((Long)this.delay.getValueState())) {
            if (this.doesHotbarHaveSoups()) {
               this.eatAndDropSoup();
            } else {
               this.getSoupFromInventory();
            }

            this.time.reset();
         }
      }

   }

   private void stackBowls() {
      if (!(mc.currentScreen instanceof GuiChest)) {
         for(int index = 9; index < 45; ++index) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && stack.getItem() == Items.bowl) {
               mc.playerController.windowClick(0, index, 0, 0, mc.thePlayer);
               mc.playerController.windowClick(0, 18, 0, 0, mc.thePlayer);
            }
         }

      }
   }

   private int updateCounter() {
      int counter = 0;

      for(int index = 9; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSoup(stack)) {
            counter += stack.stackSize;
         }
      }

      this.setTag("Auto Soup §7" + counter);
      return counter;
   }
}
