package com.etb.client.module.modules.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class InvCleaner extends Module {

    private NumberValue<Integer> delay = new NumberValue("Delay", 150, 50, 300, 10);
    private TimerUtil timer = new TimerUtil();

    public InvCleaner() {
        super("InvCleaner", Category.PLAYER, new Color(255, 0, 255, 255).getRGB());
        setDescription("Be a littering fool");
        setRenderlabel("Inv Cleaner");
        addValues(delay);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && mc.thePlayer != null && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) && timer.sleep(delay.getValue())) {
            for (int i = 9; i < 45; ++i) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (isBad(is) && is != mc.thePlayer.getCurrentEquippedItem()) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
                        timer.reset();
                        break;
                    }
                }
            }
        }
    }

    private ItemStack bestSword() {
        ItemStack best = null;
        float swordDamage = 0;
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    float swordD = getItemDamage(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = is;
                    }
                }
            }
        }
        return best;
    }

    private boolean isBad(final ItemStack item) {
        return item != null && ((item.getItem().getUnlocalizedName().contains("tnt")) || (item.getItem().getUnlocalizedName().contains("stick")) || (item.getItem().getUnlocalizedName().equalsIgnoreCase("egg")) || (item.getItem().getUnlocalizedName().contains("string")) || (item.getItem().getUnlocalizedName().contains("flint")) || (item.getItem().getUnlocalizedName().contains("compass")) || (item.getItem().getUnlocalizedName().contains("feather")) || (item.getItem().getUnlocalizedName().contains("bucket")) || (item.getItem().getUnlocalizedName().equalsIgnoreCase("chest") && !item.getDisplayName().toLowerCase().contains("collect")) || (item.getItem().getUnlocalizedName().contains("snow")) || (item.getItem().getUnlocalizedName().contains("enchant")) || (item.getItem().getUnlocalizedName().contains("exp")) || (item.getItem().getUnlocalizedName().contains("shears")) || (item.getItem().getUnlocalizedName().contains("bow")) || (item.getItem().getUnlocalizedName().contains("arrow")) || (item.getItem().getUnlocalizedName().contains("anvil")) || (item.getItem().getUnlocalizedName().contains("torch")) || (item.getItem().getUnlocalizedName().contains("skull")) || (item.getItem().getUnlocalizedName().contains("seeds")) || (item.getItem().getUnlocalizedName().contains("leather")) || (item.getItem().getUnlocalizedName().contains("boat")) || (item.getItem().getUnlocalizedName().contains("fishing")) || (item.getItem().getUnlocalizedName().contains("wheat")) || (item.getItem().getUnlocalizedName().contains("flower")) || (item.getItem().getUnlocalizedName().contains("record")) || (item.getItem().getUnlocalizedName().contains("note")) || (item.getItem().getUnlocalizedName().contains("sugar")) || (item.getItem().getUnlocalizedName().contains("wire")) || (item.getItem().getUnlocalizedName().contains("trip")) || (item.getItem().getUnlocalizedName().contains("slime")) || (item.getItem().getUnlocalizedName().contains("web")) || ((item.getItem() instanceof ItemPickaxe)) || ((item.getItem() instanceof ItemGlassBottle)) || ((item.getItem() instanceof ItemTool)) || ((item.getItem() instanceof ItemArmor) && !getBest().contains(item)) || ((item.getItem() instanceof ItemSword) && item != bestSword()) || (item.getItem().getUnlocalizedName().contains("piston")) || (item.getItem().getUnlocalizedName().contains("potion") && (isBadPotion(item))) || (item.getItem() instanceof ItemBow || item.getItem().getUnlocalizedName().contains("arrow")));

    }

    private List<ItemStack> getBest() {
        List<ItemStack> best = new ArrayList();
        for (int i = 0; i < 4; ++i) {
            ItemStack armorStack = null;
            for (ItemStack itemStack : mc.thePlayer.inventory.armorInventory) {
                if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor)) continue;
                ItemArmor stackArmor = (ItemArmor) itemStack.getItem();
                if (stackArmor.armorType != i) continue;
                armorStack = itemStack;
            }
            final double reduction = armorStack == null ? -1 : getArmorStrength(armorStack);
            ItemStack slotStack = findBestArmor(i);
            if (slotStack != null && getArmorStrength(slotStack) <= reduction) {
                slotStack = armorStack;
            }
            if (slotStack == null) continue;
            best.add(slotStack);
        }
        return best;
    }

    /**
     * Finds the best armor item stack in the player's inventory.
     *
     * @param itemSlot The equipment slot of the armor.
     * @return The best armor item stack in the player's inventory.
     */
    private ItemStack findBestArmor(final int itemSlot) {
        ItemStack i = null;
        double maxReduction = 0;
        for (int slot = 0; slot < 36; ++slot) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[slot];
            if (itemStack == null) continue;
            double reduction = getArmorStrength(itemStack);
            if (reduction == -1) continue;
            ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
            if (itemArmor.armorType != itemSlot) continue;
            if (reduction < maxReduction) continue;
            maxReduction = reduction;
            i = itemStack;
        }
        return i;
    }

    /**
     * Calculates how much the armor protects you.
     *
     * @param itemStack The armor item stack.
     * @return The protection amount.
     */
    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }
}
