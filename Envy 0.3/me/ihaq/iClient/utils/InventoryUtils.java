package me.ihaq.iClient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
	public boolean inventoryIsFull() {
		int i = 9;
		while (i < 45) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack == null) {
				return false;
			}
			++i;
		}
		return true;
	}
}
