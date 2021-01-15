package saint.modstuff.modules;

import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.TimeHelper;

public class AutoEat extends Module {
   private boolean eating = false;
   private Long last;
   private int ticks = 0;
   private boolean eatingFood = false;
   private Long lastMeal;
   private int ticksFood = 0;
   private final TimeHelper time = new TimeHelper();

   public AutoEat() {
      super("AutoEat", -2302756, ModManager.Category.PLAYER);
      this.setTag("Auto Eat");
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (this.updateCounter() == 0) {
            return;
         }

         if (this.eatingFood) {
            this.setColor(-14634326);
            if (this.time.hasReached(5000L)) {
               Saint.getNotificationManager().addInfo("Eating food! [Auto Eat]");
               this.time.reset();
            }
         } else {
            this.setColor(-2302756);
         }

         if (this.shouldEat()) {
            for(int i = 44; i >= 9; --i) {
               ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (stack != null && i >= 36 && i <= 44 && stack.getItem() instanceof ItemFood) {
                  if (!this.eatingFood) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
                     this.lastMeal = null;
                     this.ticksFood = 32;
                     this.eatingFood = true;
                  } else if (this.lastMeal == null || Long.valueOf(System.nanoTime() / 1000000L) >= this.lastMeal + 100L) {
                     this.lastMeal = System.nanoTime() / 1000000L;
                     --this.ticksFood;
                     if (this.ticksFood > 0) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, stack, 0.0F, 0.0F, 0.0F));
                     } else if (this.ticksFood == 0) {
                        this.eatingFood = false;
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                     }
                  }

                  return;
               }
            }
         } else if (this.eating || this.eatingFood) {
            this.eating = false;
            this.last = null;
            this.ticks = 32;
            this.eatingFood = false;
            this.lastMeal = null;
            this.ticksFood = 32;
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
         }
      }

   }

   public boolean shouldEat() {
      return !mc.thePlayer.isPotionActive(Potion.regeneration) && mc.thePlayer.getFoodStats().getFoodLevel() <= 15;
   }

   private boolean shouldUse() {
      ItemStack item = mc.thePlayer.getCurrentEquippedItem();
      if (item == null) {
         return false;
      } else {
         return item.getItem() instanceof ItemFood || item.getItem() instanceof ItemPotion || item.getItem() instanceof ItemBucketMilk;
      }
   }

   private boolean isStackFood(ItemStack stack) {
      return stack == null ? false : stack.getItem() instanceof ItemFood;
   }

   private int updateCounter() {
      int counter = 0;

      for(int index = 9; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackFood(stack)) {
            counter += stack.stackSize;
         }
      }

      this.setTag("Auto Eat§f §7" + counter);
      return counter;
   }
}
