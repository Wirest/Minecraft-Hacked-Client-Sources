package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class AutoPot extends Module {
   private boolean jumping;
   public static Value health;
   public static Value delay;
   public static Value jump;
   private TimeHelper timer = new TimeHelper();

   static {
      health = new Value("AutoPot", Float.TYPE, "Health", 6.0F, 1.0F, 20.0F, 1.0F);
      delay = new Value("AutoPot", Float.TYPE, "Delay", 200.0F, 0.0F, 2000.0F, 10.0F);
      jump = new Value("AutoPot", Boolean.TYPE, "Jump", true);
   }

   public AutoPot() {
      super("AutoPot", Category.Combat, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onMove(EventMove event) {
      if (this.jumping) {
         event.x = 0.0D;
         event.z = 0.0D;
         if (Minecraft.thePlayer.isCollidedVertically) {
            this.jumping = false;
         }
      }

   }

   @EventTarget(3)
   private void onPreUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         int potSlot = this.getPotFromInventory();
         if (Minecraft.thePlayer.getHealth() < health.getFloatValue() && this.timer.hasPassed((double)delay.getFloatValue()) && potSlot != -1) {
            if (jump.getBooleanValue()) {
               event.pitch = -89.5F;
               if (Minecraft.thePlayer.onGround) {
                  Minecraft.thePlayer.jump();
                  this.jumping = true;
               }
            } else {
               event.pitch = 89.5F;
            }
         }

      }
   }

   @EventTarget
   private void onPostUpdate(EventUpdate event) {
      if (event.state == EventState.POST) {
         int potSlot = this.getPotFromInventory();
         if (Minecraft.thePlayer.getHealth() < health.getFloatValue() && this.timer.hasPassed((double)delay.getFloatValue()) && potSlot != -1 && Minecraft.thePlayer.isCollidedVertically) {
            int prevSlot = Minecraft.thePlayer.inventory.currentItem;
            if (potSlot < 9) {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(potSlot));
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
               this.mc.playerController.syncCurrentPlayItem();
               Minecraft.thePlayer.inventory.currentItem = prevSlot;
            } else {
               this.swap(potSlot, Minecraft.thePlayer.inventory.currentItem + (Minecraft.thePlayer.inventory.currentItem < 8 ? 1 : -1));
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem + (Minecraft.thePlayer.inventory.currentItem < 8 ? 1 : -1)));
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
            }

            this.timer.reset();
         }

      }
   }

   protected void swap(int slot, int hotbarNum) {
      this.mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.thePlayer);
   }

   private int getPotFromInventory() {
      for(int i = 0; i < 35; ++i) {
         if (Minecraft.thePlayer.inventory.mainInventory[i] != null) {
            ItemStack is = Minecraft.thePlayer.inventory.mainInventory[i];
            Item item = is.getItem();
            if (item instanceof ItemPotion) {
               List poteffs = ((ItemPotion)item).getEffects(is);
               Iterator var6 = poteffs.iterator();

               while(var6.hasNext()) {
                  PotionEffect effect = (PotionEffect)var6.next();
                  if (effect.getPotionID() == Potion.heal.id || effect.getPotionID() == Potion.regeneration.id) {
                     return i;
                  }
               }
            }
         }
      }

      return -1;
   }
}
