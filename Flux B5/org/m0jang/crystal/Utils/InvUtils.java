package org.m0jang.crystal.Utils;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvUtils {
   public static void swapShift(int slot) {
      PlayerControllerMP var10000 = Minecraft.getMinecraft().playerController;
      Minecraft.getMinecraft();
      int var10001 = Minecraft.thePlayer.inventoryContainer.windowId;
      Minecraft.getMinecraft();
      var10000.windowClick(var10001, slot, 0, 1, Minecraft.thePlayer);
   }

   public static void swap(int slot, int hotbarNum) {
      PlayerControllerMP var10000 = Minecraft.getMinecraft().playerController;
      Minecraft.getMinecraft();
      int var10001 = Minecraft.thePlayer.inventoryContainer.windowId;
      Minecraft.getMinecraft();
      var10000.windowClick(var10001, slot, hotbarNum, 2, Minecraft.thePlayer);
   }

   public static int getPotFromInventory() {
      int pot = -1;

      for(int i = 1; i < 45; ++i) {
         Minecraft.getMinecraft();
         if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            Minecraft.getMinecraft();
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (item instanceof ItemPotion) {
               ItemPotion potion = (ItemPotion)item;
               if (potion.getEffects(is) != null) {
                  Iterator var6 = potion.getEffects(is).iterator();

                  while(var6.hasNext()) {
                     Object o = var6.next();
                     PotionEffect effect = (PotionEffect)o;
                     if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
                        pot = i;
                     }
                  }
               }
            }
         }
      }

      return pot;
   }

   public static int getPotsInInventory() {
      int counter = 0;

      for(int i = 1; i < 45; ++i) {
         Minecraft.getMinecraft();
         if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            Minecraft.getMinecraft();
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (item instanceof ItemPotion) {
               ItemPotion potion = (ItemPotion)item;
               if (potion.getEffects(is) != null) {
                  Iterator var6 = potion.getEffects(is).iterator();

                  while(var6.hasNext()) {
                     Object o = var6.next();
                     PotionEffect effect = (PotionEffect)o;
                     if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
                        ++counter;
                     }
                  }
               }
            }
         }
      }

      return counter;
   }

   public static int getSoupInInventory() {
      int counter = 0;

      for(int i = 1; i < 45; ++i) {
         Minecraft.getMinecraft();
         if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            Minecraft.getMinecraft();
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (item instanceof ItemSoup && item instanceof ItemSoup) {
               ++counter;
            }
         }
      }

      return counter;
   }

   public static boolean isFullInv() {
      for(int i = 9; i < 45; ++i) {
         ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (itemStack == null || itemStack.getUnlocalizedName().contains("air")) {
            return false;
         }
      }

      return true;
   }
}
