package me.onlyeli.ice.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvUtils {
	public static void swapShift(int slot) {
		Minecraft.getMinecraft().playerController.windowClick(
				Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1,
				Minecraft.getMinecraft().thePlayer);
	}

	public static void swap(int slot, int hotbarNum) {
		Minecraft.getMinecraft().playerController.windowClick(
				Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				Minecraft.getMinecraft().thePlayer);
	}

	public static int getPotFromInventory() {
		int pot = -1;
		for (int i = 1; i < 45; ++i) {
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
				final Item item = is.getItem();
				if (item instanceof ItemPotion) {
					final ItemPotion potion = (ItemPotion) item;
					if (potion.getEffects(is) != null) {
						for (final Object o : potion.getEffects(is)) {
							final PotionEffect effect = (PotionEffect) o;
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
		for (int i = 1; i < 45; ++i) {
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
				final Item item = is.getItem();
				if (item instanceof ItemPotion) {
					final ItemPotion potion = (ItemPotion) item;
					if (potion.getEffects(is) != null) {
						for (final Object o : potion.getEffects(is)) {
							final PotionEffect effect = (PotionEffect) o;
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
}