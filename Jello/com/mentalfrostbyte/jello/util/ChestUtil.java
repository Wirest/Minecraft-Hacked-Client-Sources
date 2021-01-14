package com.mentalfrostbyte.jello.util;

import java.util.ArrayList;

import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.modules.ChestStealer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ChestUtil {
    public ArrayList<Integer> chestSlots = new ArrayList();

    public void findChestSlots(GuiChest chest) {
        int i = 0;
        while (i < chest.lowerChestInventory.getSizeInventory()) {
            ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);
            if (stack != null && (this.stackIsImportant(stack))) {
                this.chestSlots.add(i);
            }
            ++i;
        }
    }

    public boolean chestIsEmpty(GuiChest chest) {
        if (Jello.core.player() == null) {
            return false;
        }
        int i = 0;
        while (i < chest.lowerChestInventory.getSizeInventory()) {
            ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);
            if (stack != null && ((false) ? this.stackIsImportant(stack):true)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public boolean stackIsImportant(ItemStack stack) {
        boolean valid = true;
        if (stack.getItem() instanceof ItemMonsterPlacer) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemSword) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemAxe) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemArmor) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemPotion) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemPickaxe) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemFood) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemBow) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemFlintAndSteel) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemEnderPearl) {
            valid = true;
        }
        if (stack.getItem() instanceof ItemBlock) {
            valid = true;
        }
        stack.getItem();
        if (Item.getIdFromItem(stack.getItem()) == 346) {
            valid = true;
        }
        stack.getItem();
        if (Item.getIdFromItem(stack.getItem()) == 327) {
            valid = true;
        }
        stack.getItem();
        if (Item.getIdFromItem(stack.getItem()) == 326) {
            valid = true;
        }
        stack.getItem();
        if (Item.getIdFromItem(stack.getItem()) == 262) {
            valid = true;
        }
        return valid;
    }
}
