package me.Corbis.Execution.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;

public class ItemUtils {
    static Minecraft mc = Minecraft.getMinecraft();
    public static void dropstart(int slot, ItemStack item) {
        if (mc.thePlayer == null) {
            return;
        }

        boolean hotbar = false;
        for (int k = 0; k < 9; k++) {
            ItemStack itemK = mc.thePlayer.inventory.getStackInSlot(k);
            if (itemK != null && itemK == item) {
                hotbar = true;
            }
        }
        if (hotbar) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
            C07PacketPlayerDigging.Action c07packetplayerdigging$action = C07PacketPlayerDigging.Action.DROP_ALL_ITEMS;
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
        } else {
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
        }
    }

    public static int getSwordSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemSword) {
                        ItemSword is = (ItemSword) item.getItem();
                        float damage = is.getDamageVsEntity();
                        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, item) * 1.26F +
                                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, item) * 0.01f;
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestSword = i;
                        }
                    }
                }
            }
        }
        return bestSword;
    }

    public static int getPickaxeSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemPickaxe) {
                        ItemPickaxe is = (ItemPickaxe) item.getItem();
                        float damage = is.getStrVsBlock(item, Block.getBlockById(4));
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestSword = i;
                        }
                    }
                }
            }
        }
        return bestSword;
    }

    public static int getAxeSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemAxe) {
                        ItemAxe is = (ItemAxe) item.getItem();
                        float damage = is.getStrVsBlock(item, Block.getBlockById(17));
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestSword = i;
                        }
                    }
                }
            }
        }
        return bestSword;
    }

    public static int getShovelSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemTool) {
                        ItemTool is = (ItemTool) item.getItem();
                        if (isShovel(is)) {
                            float damage = is.getStrVsBlock(item, Block.getBlockById(3));
                            if (damage > bestDamage) {
                                bestDamage = damage;
                                bestSword = i;
                            }
                        }
                    }
                }
            }
        }
        return bestSword;
    }

    public static boolean isShovel(Item is) {
        return Item.getItemById(256) == is || Item.getItemById(269) == is || Item.getItemById(273) == is || Item.getItemById(277) == is || Item.getItemById(284) == is;
    }

    public static boolean isHotbarFull() {
        int count = 0;
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null) {
                count++;
            }
        }
        return count == 8;
    }

    public static int getEmptyHotbarSlot() {
        for (int k = 0; k < 9; ++k) {
            if (mc.thePlayer.inventory.mainInventory[k] == null) {
                return k;
            }
        }
        return -1;
    }

    public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

    public static void shiftClick(int slot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer);
    }

    public static boolean isBad(final ItemStack item) {
        return !(item.getItem() instanceof ItemArmor || item.getItem() instanceof ItemTool || item.getItem() instanceof ItemBlock || item.getItem() instanceof ItemSword || item.getItem() instanceof ItemEnderPearl || item.getItem() instanceof ItemFood || (item.getItem() instanceof ItemPotion && !isBadPotion(item))) && !item.getDisplayName().toLowerCase().contains(EnumChatFormatting.GRAY +"(right click)");
        //return item.getItem().getUnlocalizedName().contains("tnt") || item.getItem().getUnlocalizedName().contains("stick") || item.getItem().getUnlocalizedName().equalsIgnoreCase("egg") || item.getItem().getUnlocalizedName().contains("string") || item.getItem().getUnlocalizedName().contains("flint") || item.getItem().getUnlocalizedName().contains("feather") || item.getItem().getUnlocalizedName().contains("bucket") || (item.getItem().getUnlocalizedName().equalsIgnoreCase("chest") && !item.getDisplayName().toLowerCase().contains("collect")) || item.getItem().getUnlocalizedName().contains("snow") || item.getItem().getUnlocalizedName().contains("enchant") || item.getItem().getUnlocalizedName().contains("exp") || item.getItem().getUnlocalizedName().contains("shears") || item.getItem().getUnlocalizedName().contains("arrow") || item.getItem().getUnlocalizedName().contains("anvil") || item.getItem().getUnlocalizedName().contains("torch") || item.getItem().getUnlocalizedName().contains("skull") || item.getItem().getUnlocalizedName().contains("seeds") || item.getItem().getUnlocalizedName().contains("leather") || item.getItem().getUnlocalizedName().contains("boat") || item.getItem().getUnlocalizedName().contains("fishing") || item.getItem().getUnlocalizedName().contains("wheat") || item.getItem().getUnlocalizedName().contains("flower") || item.getItem().getUnlocalizedName().contains("record") || item.getItem().getUnlocalizedName().contains("note") || item.getItem().getUnlocalizedName().contains("sugar") || item.getItem().getUnlocalizedName().contains("wire") || item.getItem().getUnlocalizedName().contains("trip") || item.getItem().getUnlocalizedName().contains("slime") || item.getItem().getUnlocalizedName().contains("web") || item.getItem() instanceof ItemGlassBottle || item.getItem().getUnlocalizedName().contains("piston") || (item.getItem().getUnlocalizedName().contains("potion") && isBadPotion(item));
    }

    public static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
