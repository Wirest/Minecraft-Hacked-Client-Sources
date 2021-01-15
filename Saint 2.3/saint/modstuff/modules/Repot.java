package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Repot extends Module {
   private final Value delay = new Value("repot_delay", 450L);
   private final TimeHelper time = new TimeHelper();

   public Repot() {
      super("Repot", -65536, ModManager.Category.COMBAT);
      Saint.getCommandManager().getContentList().add(new Command("repotdelay", "<milliseconds>", new String[]{"rpotdelay", "rpd"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Repot.this.delay.setValueState((Long)Repot.this.delay.getDefaultValue());
            } else {
               Repot.this.delay.setValueState(Long.parseLong(message.split(" ")[1]));
            }

            if ((Long)Repot.this.delay.getValueState() > 1000L) {
               Repot.this.delay.setValueState(1000L);
            } else if ((Long)Repot.this.delay.getValueState() < 0L) {
               Repot.this.delay.setValueState(0L);
            }

            Logger.writeChat("Repot Delay set to: " + Repot.this.delay.getValueState());
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

   public void onEvent(Event event) {
      if (event instanceof PostMotion) {
         if (this.updateCounter() == 0) {
            return;
         }

         if (mc.currentScreen instanceof GuiChest) {
            return;
         }

         if (this.time.hasReached((Long)this.delay.getValueState())) {
            if (!this.isHotbarFull()) {
               for(int index = 9; index < 36; ++index) {
                  ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                  if (stack != null && stack.getItem() instanceof ItemPotion) {
                     mc.playerController.windowClick(0, index, 0, 1, mc.thePlayer);
                     break;
                  }
               }
            }

            this.time.reset();
         }
      } else if (event instanceof PreMotion && this.updateCounter() == 0) {
         return;
      }

   }

   private boolean isStackSplashHealthPot(ItemStack stack) {
      if (stack == null) {
         return false;
      } else {
         if (stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
               Iterator var4 = potion.getEffects(stack).iterator();

               while(var4.hasNext()) {
                  Object o = var4.next();
                  PotionEffect effect = (PotionEffect)o;
                  if (effect.getPotionID() == Potion.heal.id) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private int updateCounter() {
      int counter = 0;

      for(int index = 9; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSplashHealthPot(stack)) {
            counter += stack.stackSize;
         }
      }

      this.setTag("Repot §7" + counter);
      return counter;
   }
}
