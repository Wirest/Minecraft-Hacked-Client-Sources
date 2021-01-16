package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.MathUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class CreativeDrop extends Module {
   public int itemsThrown;
   private TimeHelper timer;

   public CreativeDrop() {
      super("Creative Drop", Category.World, false);
   }

   public void onEnable() {
      this.itemsThrown = 0;
      this.timer = new TimeHelper();
      super.onEnable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      Item item;
      ItemStack itemStack;
      int i;
      if (Minecraft.thePlayer.capabilities.isCreativeMode) {
         new Item();
         item = Item.getItemById(MathUtils.randInt(1, Item.itemRegistry.getKeys().size()));
         itemStack = new ItemStack(item, 64);
         itemStack.setStackDisplayName(getString(20));
         if (item != null) {
            this.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(Minecraft.thePlayer.inventory.currentItem + 36, itemStack));
         }

         if (this.timer.hasPassed(1.0D)) {
            this.timer.reset();

            for(i = 0; i < 40; ++i) {
               this.itemsThrown += 2;
               Minecraft.thePlayer.dropOneItem(true);
            }
         }
      } else {
         int k;
         if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
            for(k = 0; k < 40; ++k) {
               this.itemsThrown += 2;
               Minecraft.thePlayer.dropOneItem(true);
            }
         } else if (event.state == EventState.PRE) {
            if (Minecraft.thePlayer.capabilities.isCreativeMode) {
               item = Item.getItemById(MathUtils.randInt(1, Item.itemRegistry.getKeys().size()));
               itemStack = new ItemStack(item, 64);
               itemStack.setStackDisplayName(getString(20));
               if (item != null) {
                  this.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(Minecraft.thePlayer.inventory.currentItem + 36, itemStack));
               }

               if (this.timer.hasPassed(1.0D)) {
                  this.timer.reset();

                  for(i = 0; i < 40; ++i) {
                     Minecraft.thePlayer.dropOneItem(true);
                  }
               }
            } else if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
               for(k = 0; k < 40; ++k) {
                  Minecraft.thePlayer.dropOneItem(true);
               }
            }
         } else if (event.state == EventState.POST) {
            if (Minecraft.thePlayer.capabilities.isCreativeMode) {
               new Item();
               item = Item.getItemById(MathUtils.randInt(1, Item.itemRegistry.getKeys().size()));
               itemStack = new ItemStack(item, 64);
               itemStack.setStackDisplayName(getString(20));
               if (item != null) {
                  this.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(Minecraft.thePlayer.inventory.currentItem + 36, itemStack));
               }

               if (this.timer.hasPassed(1.0D)) {
                  this.timer.reset();
                  Minecraft.thePlayer.dropOneItem(true);
               }
            } else if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
               for(k = 0; k < 40; ++k) {
                  Minecraft.thePlayer.dropOneItem(true);
               }
            }
         }
      }

   }

   public static String getString(int length) {
      String[] colors = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o", "r"};
      String[] names = new String[]{"COCK", "fys", "get rekt", "1v1 me rust", "nice server pleb", "need to download more ram", "noice", "how dem frames doe?", "gg rekt", "lol skid", "you usin luskid?", "lol gg get rekt kid", "skid", "nub", "loOlw0t", "u w0t m8", "kys", "memess", "cOK", "gg get lagged m8", "rekt m8", "lol u laggin bro?", "u mad bro?", "lol no hard feels"};
      String string = "\247" + colors[MathUtils.randInt(0, colors.length - 1)] + " ";

      for(int l = 0; l < 1; ++l) {
         string = String.valueOf(String.valueOf(string)) + "\247" + colors[MathUtils.randInt(0, colors.length - 1)] + names[MathUtils.randInt(0, colors.length - 1)];
      }

      return string;
   }

   public void onDisable() {
      this.itemsThrown = 0;
      super.onDisable();
   }
}
