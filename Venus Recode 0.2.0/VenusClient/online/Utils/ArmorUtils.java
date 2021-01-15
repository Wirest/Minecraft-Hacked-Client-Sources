package VenusClient.online.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArmorUtils {
    public static boolean isBetterArmor(int slot, int[] armorType) {
        if(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot] != null) {
            int currentIndex = 0;
            int invIndex = 0;
            int finalCurrentIndex = -1;
            int finalInvIndex = -1;
            int[] array;
            int j = (array = armorType).length;
            for(int i = 0; i < j; i++) {
                int armor = array[i];
                if(Item.getIdFromItem(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot].getItem()) == armor) {
                    finalCurrentIndex = currentIndex;
                    break;
                }
                currentIndex++;
            }
            j = (array = armorType).length;
            for(int i = 0; i < j; i++) {
                int armor = array[i];
                if(getItem(armor) != -1) {
                    finalInvIndex = invIndex;
                    break;
                }
                invIndex++;
            }
            if(finalInvIndex > -1)
                return finalInvIndex < finalCurrentIndex;
        }
        return false;
    }

    public static int getItem(int id) {
        for(int i = 9; i < 45; i++) {
            ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            if(item != null && Item.getIdFromItem(item.getItem()) == id)
                return i;
        }
        return -1;
    }
}