/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ArmorUtils
/*    */ {
/*    */   public static boolean isBetterArmor(int slot, int[] armorType)
/*    */   {
/*  9 */     if (net.minecraft.client.Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot] != null) {
/* 10 */       int currentIndex = 0;
/* 11 */       int invIndex = 0;
/* 12 */       int finalCurrentIndex = -1;
/* 13 */       int finalInvIndex = -1;
/*    */       int[] array;
/* 15 */       int j = (array = armorType).length;
/* 16 */       for (int i = 0; i < j; i++) {
/* 17 */         int armor = array[i];
/* 18 */         if (net.minecraft.item.Item.getIdFromItem(net.minecraft.client.Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot].getItem()) == armor) {
/* 19 */           finalCurrentIndex = currentIndex;
/* 20 */           break;
/*    */         }
/* 22 */         currentIndex++;
/*    */       }
/* 24 */       j = (array = armorType).length;
/* 25 */       for (int i = 0; i < j; i++) {
/* 26 */         int armor = array[i];
/* 27 */         if (getItem(armor) != -1) {
/* 28 */           finalInvIndex = invIndex;
/* 29 */           break;
/*    */         }
/* 31 */         invIndex++;
/*    */       }
/* 33 */       if (finalInvIndex > -1)
/* 34 */         return finalInvIndex < finalCurrentIndex;
/*    */     }
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public static int getItem(int id) {
/* 40 */     for (int i = 9; i < 45; i++) {
/* 41 */       ItemStack item = net.minecraft.client.Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
/* 42 */       if ((item != null) && (net.minecraft.item.Item.getIdFromItem(item.getItem()) == id))
/* 43 */         return i;
/*    */     }
/* 45 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\ArmorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */