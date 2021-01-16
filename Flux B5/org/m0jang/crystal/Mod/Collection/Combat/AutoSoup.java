package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class AutoSoup extends Module {
   private TimeHelper timer;
   public static Value health;
   public static Value delay;

   static {
      health = new Value("AutoSoup", Float.TYPE, "Health", 6.0F, 1.0F, 20.0F, 1.0F);
      delay = new Value("AutoSoup", Float.TYPE, "Delay", 200.0F, 0.0F, 2000.0F, 10.0F);
   }

   public AutoSoup() {
      super("AutoSoup", Category.Combat, false);
   }

   public void onEnable() {
      this.timer = new TimeHelper();
      super.onEnable();
   }

   @EventTarget
   private void onPostUpdate(EventUpdate event) {
      if (event.state == EventState.POST) {
         int soupSlot = this.getSoupFromInventory();
         if ((double)Minecraft.thePlayer.getHealth() < (double)health.getFloatValue() * 2.0D && this.timer.hasPassed((double)delay.getFloatValue()) && soupSlot != -1) {
            int prevSlot = Minecraft.thePlayer.inventory.currentItem;
            if (soupSlot < 9) {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(soupSlot));
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
               this.mc.playerController.syncCurrentPlayItem();
               Minecraft.thePlayer.inventory.currentItem = prevSlot;
            } else {
               this.swap(soupSlot, Minecraft.thePlayer.inventory.currentItem + (Minecraft.thePlayer.inventory.currentItem < 8 ? 1 : -1));
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

   private int getSoupFromInventory() {
      for(int i = 0; i < 35; ++i) {
         int counter = 0;
         if (Minecraft.thePlayer.inventory.mainInventory[i] != null) {
            ItemStack is = Minecraft.thePlayer.inventory.mainInventory[i];
            Item item = is.getItem();
            if (Item.getIdFromItem(item) == 282) {
               int var5 = counter + 1;
               return i;
            }
         }
      }

      return -1;
   }
}
