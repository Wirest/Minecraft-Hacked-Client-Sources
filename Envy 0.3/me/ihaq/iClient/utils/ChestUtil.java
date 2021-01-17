package me.ihaq.iClient.utils;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ChestUtil {
	public ArrayList<Integer> chestSlots = new ArrayList();

	public void findChestSlots(GuiChest chest) {
		int i = 0;
		while (i < chest.lowerChestInventory.getSizeInventory()) {
			ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);

			if (stack != null) {
				this.chestSlots.add(i);
			}
			++i;
		}
	}

	public static boolean isChestEmpty(ContainerChest chest) {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return false;
		}
		int i = 0;
		while (i < chest.lowerChestInventory.getSizeInventory()) {
			ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);
			if (stack != null) {
				return false;
			}
			++i;
		}
		return true;
	}

}
