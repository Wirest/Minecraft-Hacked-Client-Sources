package com.mentalfrostbyte.jello.util;

import com.mentalfrostbyte.jello.main.Jello;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class InventoryUtil {
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
    public boolean hotbarHasSpace() {
        int i = 0;
        while (i < 9) {
            if (Jello.core.mc.thePlayer.inventory.getStackInSlot(i) == null) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public boolean inventoryHasPotion() {
        int i = 9;
        while (i <= 35) {
            ItemStack stack = Jello.core.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemPotion) {
                return true;
            }
            ++i;
        }
        return false;
    }
    public boolean inventoryHasRod() {
        int i = 9;
        while (i <= 35) {
            ItemStack stack = Jello.core.player().inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemFishingRod) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public boolean hotbarHasFood() {
        int i = 0;
        while (i <= 9) {
            ItemStack stack = Jello.core.player().inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemFood) {
                return true;
            }
            ++i;
        }
        return false;
    }
    
    public boolean hotbarHasWater() {
        int i = 0;
        while (i <= 9) {
            ItemStack stack = Jello.core.player().inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBucket) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public int potionInventorySlot() {
        int i = 9;
        while (i <= 35) {
            ItemStack stack = Jello.core.player().inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemPotion) {
                return i;
            }
            ++i;
        }
        return -999;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int logHotbarSlot() {
        int i = 36;
        while (i <= 44) {
            ItemStack stack = Jello.core.player().inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                stack.getItem();
                if (Item.getIdFromItem(stack.getItem()) == 17) return i;
                stack.getItem();
                if (Item.getIdFromItem(stack.getItem()) == 162) {
                    return i;
                }
            }
            ++i;
        }
        return -999;
    }

    public int foodHotbarSlot() {
        int i = 0;
        while (i <= 9) {
            ItemStack stack = Jello.core.player().inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemFood) {
                return i;
            }
            ++i;
        }
        return -999;
    }
    public int waterHotbarSlot() {
        int i = 0;
        while (i <= 9) {
            ItemStack stack = Jello.core.player().inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBucket) {
                return i;
            }
            ++i;
        }
        return -999;
    }
    
    public int pearlHotbarSlot() {
        int i = 0;
        while (i <= 9) {
            ItemStack stack = Jello.core.player().inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemEnderPearl) {
                return i;
            }
            ++i;
        }
        return -999;
    }

    public void switchToSlot(int desiredSlot) {
        Jello.core.player().inventory.currentItem = desiredSlot;
    }
    public static int getBestHotbarSword() {
        float best = -1.0F;

        int index = -1;



        for(int i = 0; i < 9; ++i) {

           ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);

           if(itemStack != null && itemStack.getItem() instanceof ItemSword) {

              ItemSword sword = (ItemSword)itemStack.getItem();

              float damage = sword.getMaxDamage() + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, itemStack) * 1.25F;

              if(damage > best) {

                 best = damage;

                 index = i;

              }

           }

        }



        return index;

     }
    public static int getBestTotalSword() {
        float best = -1.0F;

        int index = -1;



        for(int i = 0; i < 45; ++i) {

           ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);

           if(itemStack != null && itemStack.getItem() instanceof ItemSword) {

              ItemSword sword = (ItemSword)itemStack.getItem();

              float damage = sword.getMaxDamage() + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, itemStack) * 1.25F;

              if(damage > best) {

                 best = damage;

                 index = i;

              }

           }

        }



        return index;

     }

     public static int getBestTool(Block block) {
        float best = -1.0F;

        int index = -1;



        for(int i = 0; i < 9; ++i) {

           ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);

           if(itemStack != null) {

              float str = itemStack.getItem().getStrVsBlock(itemStack, block);

              if(str > best) {

                 best = str;

                 index = i;

              }

           }

        }



        return index;

     }
}
