// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSword;
import net.minecraft.client.Minecraft;
import java.util.Comparator;
import java.util.Iterator;
import me.aristhena.utils.ClientUtils;
import java.util.ArrayList;
import java.util.List;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Durability Check")
public class DurabilityCheck extends Module
{
    private List<DurabilityItem> durabilityItems;
    
    public DurabilityCheck() {
        this.durabilityItems = new ArrayList<DurabilityItem>();
    }
    
    @Override
    public void enable() {
        if (ClientUtils.player() != null) {
            this.loadDurabilityItems();
            int count = 0;
            double prevDamage = 0.0;
            final int slot = -1;
            final boolean crit = false;
            int clicks = 0;
            int packets = 0;
            for (final DurabilityItem item : this.durabilityItems) {
                double armorDamage = item.damage - prevDamage;
                armorDamage /= 4.0;
                if (armorDamage < 1.0) {
                    armorDamage = 1.0;
                }
                count += (int)armorDamage;
                ClientUtils.sendMessage("xd: " + item.damage + ", " + armorDamage);
                if (item.slot != slot) {
                    ++clicks;
                }
                if (item.crit && !crit) {
                    packets += 4;
                }
                if (!item.crit && crit) {
                    ++packets;
                }
                prevDamage = item.damage;
            }
            ClientUtils.sendMessage("Clicks: " + clicks + ", Packets: " + packets + ", Dura/hit: " + count);
        }
    }
    
    private void loadDurabilityItems() {
        this.durabilityItems.clear();
        boolean pickedLowDamageItem = false;
        for (int i = 0; i < 35; ++i) {
            if (this.getDamageFromSlot(i, false) >= 5.0 || !pickedLowDamageItem) {
                if (this.getDamageFromSlot(i, false) < 5.0) {
                    pickedLowDamageItem = true;
                }
                else {
                    this.durabilityItems.add(new DurabilityItem(this.getDamageFromSlot(i, false), i, false));
                    this.durabilityItems.add(new DurabilityItem(this.getDamageFromSlot(i, true), i, true));
                }
            }
        }
        this.durabilityItems.sort(new Comparator<DurabilityItem>() {
            @Override
            public int compare(final DurabilityItem item1, final DurabilityItem item2) {
                if (item1.damage < item2.damage) {
                    return -1;
                }
                if (item1.damage > item2.damage) {
                    return 1;
                }
                return 1;
            }
        });
    }
    
    private double getDamageFromSlot(final int slot, final boolean crit) {
        double damage = 1.0;
        final ItemStack weapon = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[slot];
        if (weapon == null || weapon == null) {
            return damage;
        }
        if (weapon.getItem() instanceof ItemSword) {
            final ItemSword sword = (ItemSword)weapon.getItem();
            final int sharpLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, weapon);
            final double baseDamage;
            damage = (baseDamage = ((sword.repairMaterial.getDamageVsEntity() >= 0.0f) ? (5.0 + sword.repairMaterial.getDamageVsEntity()) : sword.repairMaterial.getDamageVsEntity()));
            damage += sharpLevel * 1.25;
        }
        if (crit) {
            damage *= 1.5;
        }
        return damage;
    }
    
    private class DurabilityItem
    {
        public double damage;
        public int slot;
        public boolean crit;
        
        public DurabilityItem(final double damage, final int slot, final boolean crit) {
            this.damage = damage;
            this.slot = slot;
            this.crit = crit;
        }
    }
}
