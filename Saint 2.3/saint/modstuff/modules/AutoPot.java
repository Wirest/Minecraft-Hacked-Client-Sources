package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class AutoPot extends Module {
   private final Value delay = new Value("autopot_delay", 500L);
   private final Value health = new Value("autopot_health", 18.0F);
   private boolean potting;
   private final TimeHelper time = new TimeHelper();

   public AutoPot() {
      super("AutoPot", -65536, ModManager.Category.COMBAT);
      this.setTag("Auto Pot");
      Saint.getCommandManager().getContentList().add(new Command("autopotdelay", "<milliseconds>", new String[]{"potdelay", "apd"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               AutoPot.this.delay.setValueState((Long)AutoPot.this.delay.getDefaultValue());
            } else {
               AutoPot.this.delay.setValueState(Long.parseLong(message.split(" ")[1]));
            }

            if ((Long)AutoPot.this.delay.getValueState() > 1000L) {
               AutoPot.this.delay.setValueState(1000L);
            } else if ((Long)AutoPot.this.delay.getValueState() < 1L) {
               AutoPot.this.delay.setValueState(1L);
            }

            Logger.writeChat("Auto Pot Delay set to: " + AutoPot.this.delay.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("autopothealth", "<health>", new String[]{"pothealth", "aph"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               AutoPot.this.health.setValueState((Float)AutoPot.this.health.getDefaultValue());
            } else {
               AutoPot.this.health.setValueState(Float.parseFloat(message.split(" ")[1]));
            }

            if ((double)(Float)AutoPot.this.health.getValueState() < 1.0D) {
               AutoPot.this.health.setValueState(1.0F);
            }

            Logger.writeChat("Auto Pot Health set to: " + AutoPot.this.health.getValueState());
         }
      });
   }

   private boolean doesHotbarHavePots() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSplashHealthPot(stack)) {
            return true;
         }
      }

      return false;
   }

   private void getPotsFromInventory() {
      if (!(mc.currentScreen instanceof GuiChest)) {
         for(int index = 9; index < 36; ++index) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
               mc.playerController.windowClick(0, index, 0, 1, mc.thePlayer);
               break;
            }
         }

      }
   }

   public boolean isPotting() {
      return this.potting;
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

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (this.updateCounter() == 0) {
            return;
         }

         PreMotion pre = (PreMotion)event;
         if (mc.thePlayer.getHealth() <= (Float)this.health.getValueState() && this.time.hasReached((Long)this.delay.getValueState()) && this.doesHotbarHavePots()) {
            this.potting = true;
            pre.setPitch(90.0F);
         }
      } else if (event instanceof PostMotion) {
         if (this.updateCounter() == 0) {
            return;
         }

         if (mc.thePlayer.getHealth() <= (Float)this.health.getValueState() && this.time.hasReached((Long)this.delay.getValueState())) {
            if (this.doesHotbarHavePots()) {
               if (!BlockHelper.isOnLiquid()) {
                  this.splashPot();
               }

               this.potting = false;
            } else {
               this.getPotsFromInventory();
            }

            this.time.reset();
         }
      }

   }

   private void splashPot() {
      for(int index = 36; index < 45; ++index) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack != null && this.isStackSplashHealthPot(stack)) {
            int oldslot = mc.thePlayer.inventory.currentItem;
            this.potting = true;
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90.0F, mc.thePlayer.onGround));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index - 36));
            mc.playerController.updateController();
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, stack, 0.0F, 0.0F, 0.0F));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldslot));
            this.potting = false;
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
            break;
         }
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

      this.setTag("Auto Pot§f §7" + counter);
      return counter;
   }
}
