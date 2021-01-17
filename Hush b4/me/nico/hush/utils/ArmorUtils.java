// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;

public class ArmorUtils
{
    public static boolean isBetterArmor(final int slot, final int[] armorType) {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.inventory.armorInventory[slot] != null) {
            int currentIndex = 0;
            int invIndex = 0;
            int finalCurrentIndex = -1;
            int finalInvIndex = -1;
            int[] array = armorType;
            for (int j = armorType.length, i = 0; i < j; ++i) {
                final int armor = array[i];
                Minecraft.getMinecraft();
                if (Item.getIdFromItem(Minecraft.thePlayer.inventory.armorInventory[slot].getItem()) == armor) {
                    finalCurrentIndex = currentIndex;
                    break;
                }
                ++currentIndex;
            }
            array = armorType;
            for (int j = armorType.length, i = 0; i < j; ++i) {
                final int armor = array[i];
                if (getItem(armor) != -1) {
                    finalInvIndex = invIndex;
                    break;
                }
                ++invIndex;
            }
            if (finalInvIndex > -1) {
                return finalInvIndex < finalCurrentIndex;
            }
        }
        return false;
    }
    
    public static int getItem(final int id) {
        for (int i = 9; i < 45; ++i) {
            Minecraft.getMinecraft();
            final ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == id) {
                return i;
            }
        }
        return -1;
    }
}
