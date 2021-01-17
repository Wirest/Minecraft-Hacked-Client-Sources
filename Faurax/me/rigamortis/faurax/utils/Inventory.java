package me.rigamortis.faurax.utils;

import net.minecraft.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import java.util.*;

public class Inventory
{
    public static Minecraft mc;
    
    static {
        Inventory.mc = Minecraft.getMinecraft();
    }
    
    public static void updateInventory() {
        for (int index = 0; index < 44; ++index) {
            try {
                final int offset = (index < 9) ? 36 : 0;
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(index + offset, Minecraft.getMinecraft().thePlayer.inventory.mainInventory[index]));
            }
            catch (Exception ex) {}
        }
    }
    
    public static void clickSlot(final int slot, final int mouseButton, final boolean shiftClick) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, Minecraft.getMinecraft().thePlayer);
    }
    
    public static int findHotbarItem(final int itemId, final int mode) {
        if (mode == 0) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemId) {
                    return slot;
                }
            }
        }
        else if (mode == 1) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemId) {
                    return slot - 36;
                }
            }
        }
        return -1;
    }
    
    public static int findPots(final int itemId, final int mode) {
        if (mode == 0) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemId && isStackSplashHealthPot(item)) {
                    return slot;
                }
            }
        }
        else if (mode == 1) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemId && isStackSplashHealthPot(item)) {
                    return slot - 36;
                }
            }
        }
        return -1;
    }
    
    public static int findInventoryPots(final int itemID) {
        for (int o = 9; o <= 35; ++o) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                final ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemID && isStackSplashHealthPot(item)) {
                    return o;
                }
            }
        }
        return -1;
    }
    
    public static boolean isStackSplashHealthPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion itempotion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object object : itempotion.getEffects(stack)) {
                    final PotionEffect potioneffect = (PotionEffect)object;
                    if (potioneffect.getPotionID() == Potion.heal.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static int findEmptyHotbarItem(final int mode) {
        if (mode == 0) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
            }
        }
        else if (mode == 1) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
            }
        }
        return -1;
    }
    
    public static int findInventoryItem(final int itemID) {
        for (int o = 9; o <= 35; ++o) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                final ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemID) {
                    return o;
                }
            }
        }
        return -1;
    }
    
    public static int findAvailableSlotInventory(final int mode, final int... itemIds) {
        if (mode == 0) {
            for (int slot = 9; slot <= 35; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
                for (final int id : itemIds) {
                    if (Item.getIdFromItem(item.getItem()) == id) {
                        return slot;
                    }
                }
            }
        }
        else if (mode == 1) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = Inventory.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
            }
        }
        return -1;
    }
}
