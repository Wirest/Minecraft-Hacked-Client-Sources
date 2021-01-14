package cn.kody.debug.mod.mods.COMBAT;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.time.TimeHelper;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword
extends Mod {
    TimeHelper time = new TimeHelper();

    public AutoSword() {
        super("AutoSword", "Auto Sword", Category.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!this.time.isDelayComplete(100L) || this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        int best = -1;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            float swordD;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemSword) || (swordD = this.getSharpnessLevel(is)) <= swordDamage) continue;
            swordDamage = swordD;
            best = i;
        }
        ItemStack current = Minecraft.thePlayer.inventoryContainer.getSlot(36).getStack();
        if (!(best == -1 || current != null && current.getItem() instanceof ItemSword && swordDamage <= this.getSharpnessLevel(current))) {
            Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, best, 0, 2, Minecraft.thePlayer);
            this.time.reset();
        }
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = this.getDamage(stack);
        for (int i = 9; i < 36; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || this.getDamage(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) <= damage || !(is.getItem() instanceof ItemSword)) continue;
            return false;
        }
        if (stack.getItem() instanceof ItemSword) {
            return true;
        }
        return false;
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword)item;
            damage += sword.getDamageVsEntity();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 36; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestWeapon(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) || this.getDamage(is) <= 0.0f || !(is.getItem() instanceof ItemSword)) continue;
            this.swap(i, slot - 36);
            break;
        }
    }

    protected void swap(int slot, int hotbarNum) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.thePlayer);
    }

    private float getSharpnessLevel(ItemStack stack) {
        float damage = ((ItemSword)stack.getItem()).getDamageVsEntity();
        damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

