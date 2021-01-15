package saint.modstuff.modules;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class AutoApple extends Module {
   private final Value health = new Value("autoapple_health", 16.0F);
   private boolean goOnce = false;
   private int prevSlot = -1;
   private boolean finished = false;

   public AutoApple() {
      super("AutoApple", -16728065, ModManager.Category.COMBAT);
      this.setTag("Auto Apple");
      Saint.getCommandManager().getContentList().add(new Command("autoapplehealth", "<health>", new String[]{"aahealth", "aah"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               AutoApple.this.health.setValueState((Float)AutoApple.this.health.getDefaultValue());
            } else {
               AutoApple.this.health.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((Float)AutoApple.this.health.getValueState() < 1.0F) {
               AutoApple.this.health.setValueState(1.0F);
            }

            Logger.writeChat("Auto Apple Health set to: " + AutoApple.this.health.getValueState());
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         KillAura aura = (KillAura)Saint.getModuleManager().getModuleUsingName("killaura");
         if (this.prevSlot != -1 && this.finished && this.goOnce) {
            mc.thePlayer.inventory.currentItem = this.prevSlot;
            this.setUseItemKeyPressed(false);
            this.goOnce = false;
         }

         if (mc.thePlayer.getHealth() < (Float)this.health.getValueState()) {
            for(int i = 0; i < 9; ++i) {
               ItemStack item = this.getItemAtSlotHotbar(i);
               if (item != null && item.getItem() instanceof ItemAppleGold) {
                  boolean wasEnabled = aura.isEnabled();
                  aura.setEnabled(false);
                  this.prevSlot = mc.thePlayer.inventory.currentItem;
                  mc.thePlayer.inventory.currentItem = i;
                  this.setUseItemKeyPressed(true);
                  if (mc.thePlayer.getHealth() > 1.0F) {
                     this.goOnce = true;
                     this.finished = true;
                     aura.setEnabled(wasEnabled);
                  }
               }
            }
         }
      } else if (event instanceof PostMotion && this.updateCounter() == 0) {
         return;
      }

   }

   private int updateCounter() {
      int counter = 0;

      for(int index = 36; index < 45; ++index) {
         ItemStack item = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (item != null && Item.getIdFromItem(item.getItem()) == 322) {
            counter += item.stackSize;
         }
      }

      this.setTag("Auto Apple§f §7" + counter);
      return counter;
   }

   public ItemStack getItemAtSlot(int slot) {
      return mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
   }

   public ItemStack getItemAtSlotHotbar(int slot) {
      return mc.thePlayer.inventory.getStackInSlot(slot);
   }

   public void setUseItemKeyPressed(boolean pressed) {
      mc.gameSettings.keyBindUseItem.pressed = pressed;
   }
}
