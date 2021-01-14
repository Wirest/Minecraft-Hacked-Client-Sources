package com.mentalfrostbyte.jello.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArmorUtil {
    public static boolean IsBetterArmor(int slot, int[] armourtype) {
        if (Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot] != null) {
            int armour;
            int currentIndex = 0;
            int invIndex = 0;
            int finalCurrentIndex = -1;
            int finalInvIndex = -1;
            int[] arrn = armourtype;
            int n = arrn.length;
            int n2 = 0;
            while (n2 < n) {
                armour = arrn[n2];
                if (Item.getIdFromItem(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot].getItem()) == armour) {
                    finalCurrentIndex = currentIndex;
                    break;
                }
                ++currentIndex;
                ++n2;
            }
            arrn = armourtype;
            n = arrn.length;
            n2 = 0;
            while (n2 < n) {
                armour = arrn[n2];
                if (ArmorUtil.getItem(armour) != -1) {
                    finalInvIndex = invIndex;
                    break;
                }
                ++invIndex;
                ++n2;
            }
            if (finalInvIndex > -1) {
                if (finalInvIndex < finalCurrentIndex) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static int getItem(int id) {
        int index = 9;
        while (index < 45) {
            ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == id) {
                return index;
            }
            ++index;
        }
        return -1;
    }
}

