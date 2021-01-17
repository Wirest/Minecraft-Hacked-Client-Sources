package skyline.specc.utils;

import net.minecraft.client.Mineman;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArmorUtils {

	public static boolean IsBetterArmor(final int slot, final int[] armourtype) {
		if (Mineman.thePlayer.inventory.armorInventory[slot] != null) {
			int currentIndex = 0;
			int invIndex = 0;
			int finalCurrentIndex = -1;
			int finalInvIndex = -1;
			for (final int armour : armourtype) {
				if (Item.getIdFromItem(Mineman.thePlayer.inventory.armorInventory[slot].getItem()) == armour) {
					finalCurrentIndex = currentIndex;
					break;
				}
				++currentIndex;
			}
			for (final int armour : armourtype) {
				if (getItem(armour) != -1) {
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
		for (int index = 9; index < 45; ++index) {
			final ItemStack item = Mineman.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (item != null && Item.getIdFromItem(item.getItem()) == id) {
				return index;
			}
		}
		return -1;
	}
	
}
