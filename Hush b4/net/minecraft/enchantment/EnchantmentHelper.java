// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import net.minecraft.util.WeightedRandom;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.Random;

public class EnchantmentHelper
{
    private static final Random enchantmentRand;
    private static final ModifierDamage enchantmentModifierDamage;
    private static final ModifierLiving enchantmentModifierLiving;
    private static final HurtIterator ENCHANTMENT_ITERATOR_HURT;
    private static final DamageIterator ENCHANTMENT_ITERATOR_DAMAGE;
    
    static {
        enchantmentRand = new Random();
        enchantmentModifierDamage = new ModifierDamage(null);
        enchantmentModifierLiving = new ModifierLiving(null);
        ENCHANTMENT_ITERATOR_HURT = new HurtIterator(null);
        ENCHANTMENT_ITERATOR_DAMAGE = new DamageIterator(null);
    }
    
    public static int getEnchantmentLevel(final int enchID, final ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        final NBTTagList nbttaglist = stack.getEnchantmentTagList();
        if (nbttaglist == null) {
            return 0;
        }
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final int j = nbttaglist.getCompoundTagAt(i).getShort("id");
            final int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
            if (j == enchID) {
                return k;
            }
        }
        return 0;
    }
    
    public static Map<Integer, Integer> getEnchantments(final ItemStack stack) {
        final Map<Integer, Integer> map = (Map<Integer, Integer>)Maps.newLinkedHashMap();
        final NBTTagList nbttaglist = (stack.getItem() == Items.enchanted_book) ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();
        if (nbttaglist != null) {
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final int j = nbttaglist.getCompoundTagAt(i).getShort("id");
                final int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                map.put(j, k);
            }
        }
        return map;
    }
    
    public static void setEnchantments(final Map<Integer, Integer> enchMap, final ItemStack stack) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final int i : enchMap.keySet()) {
            final Enchantment enchantment = Enchantment.getEnchantmentById(i);
            if (enchantment != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setShort("id", (short)i);
                nbttagcompound.setShort("lvl", (short)(int)enchMap.get(i));
                nbttaglist.appendTag(nbttagcompound);
                if (stack.getItem() != Items.enchanted_book) {
                    continue;
                }
                Items.enchanted_book.addEnchantment(stack, new EnchantmentData(enchantment, enchMap.get(i)));
            }
        }
        if (nbttaglist.tagCount() > 0) {
            if (stack.getItem() != Items.enchanted_book) {
                stack.setTagInfo("ench", nbttaglist);
            }
        }
        else if (stack.hasTagCompound()) {
            stack.getTagCompound().removeTag("ench");
        }
    }
    
    public static int getMaxEnchantmentLevel(final int enchID, final ItemStack[] stacks) {
        if (stacks == null) {
            return 0;
        }
        int i = 0;
        for (final ItemStack itemstack : stacks) {
            final int j = getEnchantmentLevel(enchID, itemstack);
            if (j > i) {
                i = j;
            }
        }
        return i;
    }
    
    private static void applyEnchantmentModifier(final IModifier modifier, final ItemStack stack) {
        if (stack != null) {
            final NBTTagList nbttaglist = stack.getEnchantmentTagList();
            if (nbttaglist != null) {
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final int j = nbttaglist.getCompoundTagAt(i).getShort("id");
                    final int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                    if (Enchantment.getEnchantmentById(j) != null) {
                        modifier.calculateModifier(Enchantment.getEnchantmentById(j), k);
                    }
                }
            }
        }
    }
    
    private static void applyEnchantmentModifierArray(final IModifier modifier, final ItemStack[] stacks) {
        for (final ItemStack itemstack : stacks) {
            applyEnchantmentModifier(modifier, itemstack);
        }
    }
    
    public static int getEnchantmentModifierDamage(final ItemStack[] stacks, final DamageSource source) {
        EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        EnchantmentHelper.enchantmentModifierDamage.source = source;
        applyEnchantmentModifierArray(EnchantmentHelper.enchantmentModifierDamage, stacks);
        if (EnchantmentHelper.enchantmentModifierDamage.damageModifier > 25) {
            EnchantmentHelper.enchantmentModifierDamage.damageModifier = 25;
        }
        else if (EnchantmentHelper.enchantmentModifierDamage.damageModifier < 0) {
            EnchantmentHelper.enchantmentModifierDamage.damageModifier = 0;
        }
        return (EnchantmentHelper.enchantmentModifierDamage.damageModifier + 1 >> 1) + EnchantmentHelper.enchantmentRand.nextInt((EnchantmentHelper.enchantmentModifierDamage.damageModifier >> 1) + 1);
    }
    
    public static float func_152377_a(final ItemStack p_152377_0_, final EnumCreatureAttribute p_152377_1_) {
        EnchantmentHelper.enchantmentModifierLiving.livingModifier = 0.0f;
        EnchantmentHelper.enchantmentModifierLiving.entityLiving = p_152377_1_;
        applyEnchantmentModifier(EnchantmentHelper.enchantmentModifierLiving, p_152377_0_);
        return EnchantmentHelper.enchantmentModifierLiving.livingModifier;
    }
    
    public static void applyThornEnchantments(final EntityLivingBase p_151384_0_, final Entity p_151384_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.attacker = p_151384_1_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.user = p_151384_0_;
        if (p_151384_0_ != null) {
            applyEnchantmentModifierArray(EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getInventory());
        }
        if (p_151384_1_ instanceof EntityPlayer) {
            applyEnchantmentModifier(EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItem());
        }
    }
    
    public static void applyArthropodEnchantments(final EntityLivingBase p_151385_0_, final Entity p_151385_1_) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.user = p_151385_0_;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.target = p_151385_1_;
        if (p_151385_0_ != null) {
            applyEnchantmentModifierArray(EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getInventory());
        }
        if (p_151385_0_ instanceof EntityPlayer) {
            applyEnchantmentModifier(EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItem());
        }
    }
    
    public static int getKnockbackModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
    }
    
    public static int getFireAspectModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
    }
    
    public static int getRespiration(final Entity player) {
        return getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
    }
    
    public static int getDepthStriderModifier(final Entity player) {
        return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
    }
    
    public static int getEfficiencyModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
    }
    
    public static boolean getSilkTouchModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0;
    }
    
    public static int getFortuneModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
    }
    
    public static int getLuckOfSeaModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
    }
    
    public static int getLureModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
    }
    
    public static int getLootingModifier(final EntityLivingBase player) {
        return getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
    }
    
    public static boolean getAquaAffinityModifier(final EntityLivingBase player) {
        return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0;
    }
    
    public static ItemStack getEnchantedItem(final Enchantment p_92099_0_, final EntityLivingBase p_92099_1_) {
        ItemStack[] inventory;
        for (int length = (inventory = p_92099_1_.getInventory()).length, i = 0; i < length; ++i) {
            final ItemStack itemstack = inventory[i];
            if (itemstack != null && getEnchantmentLevel(p_92099_0_.effectId, itemstack) > 0) {
                return itemstack;
            }
        }
        return null;
    }
    
    public static int calcItemStackEnchantability(final Random p_77514_0_, final int p_77514_1_, int p_77514_2_, final ItemStack p_77514_3_) {
        final Item item = p_77514_3_.getItem();
        final int i = item.getItemEnchantability();
        if (i <= 0) {
            return 0;
        }
        if (p_77514_2_ > 15) {
            p_77514_2_ = 15;
        }
        final int j = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
        return (p_77514_1_ == 0) ? Math.max(j / 3, 1) : ((p_77514_1_ == 1) ? (j * 2 / 3 + 1) : Math.max(j, p_77514_2_ * 2));
    }
    
    public static ItemStack addRandomEnchantment(final Random p_77504_0_, final ItemStack p_77504_1_, final int p_77504_2_) {
        final List<EnchantmentData> list = buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
        final boolean flag = p_77504_1_.getItem() == Items.book;
        if (flag) {
            p_77504_1_.setItem(Items.enchanted_book);
        }
        if (list != null) {
            for (final EnchantmentData enchantmentdata : list) {
                if (flag) {
                    Items.enchanted_book.addEnchantment(p_77504_1_, enchantmentdata);
                }
                else {
                    p_77504_1_.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
                }
            }
        }
        return p_77504_1_;
    }
    
    public static List<EnchantmentData> buildEnchantmentList(final Random randomIn, final ItemStack itemStackIn, final int p_77513_2_) {
        final Item item = itemStackIn.getItem();
        int i = item.getItemEnchantability();
        if (i <= 0) {
            return null;
        }
        i /= 2;
        i = 1 + randomIn.nextInt((i >> 1) + 1) + randomIn.nextInt((i >> 1) + 1);
        final int j = i + p_77513_2_;
        final float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0f) * 0.15f;
        int k = (int)(j * (1.0f + f) + 0.5f);
        if (k < 1) {
            k = 1;
        }
        List<EnchantmentData> list = null;
        final Map<Integer, EnchantmentData> map = mapEnchantmentData(k, itemStackIn);
        if (map != null && !map.isEmpty()) {
            final EnchantmentData enchantmentdata = WeightedRandom.getRandomItem(randomIn, map.values());
            if (enchantmentdata != null) {
                list = (List<EnchantmentData>)Lists.newArrayList();
                list.add(enchantmentdata);
                for (int l = k; randomIn.nextInt(50) <= l; l >>= 1) {
                    final Iterator<Integer> iterator = map.keySet().iterator();
                    while (iterator.hasNext()) {
                        final Integer integer = iterator.next();
                        boolean flag = true;
                        for (final EnchantmentData enchantmentdata2 : list) {
                            if (!enchantmentdata2.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(integer))) {
                                flag = false;
                                break;
                            }
                        }
                        if (!flag) {
                            iterator.remove();
                        }
                    }
                    if (!map.isEmpty()) {
                        final EnchantmentData enchantmentdata3 = WeightedRandom.getRandomItem(randomIn, map.values());
                        list.add(enchantmentdata3);
                    }
                }
            }
        }
        return list;
    }
    
    public static Map<Integer, EnchantmentData> mapEnchantmentData(final int p_77505_0_, final ItemStack p_77505_1_) {
        final Item item = p_77505_1_.getItem();
        Map<Integer, EnchantmentData> map = null;
        final boolean flag = p_77505_1_.getItem() == Items.book;
        Enchantment[] enchantmentsBookList;
        for (int length = (enchantmentsBookList = Enchantment.enchantmentsBookList).length, j = 0; j < length; ++j) {
            final Enchantment enchantment = enchantmentsBookList[j];
            if (enchantment != null && (enchantment.type.canEnchantItem(item) || flag)) {
                for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                    if (p_77505_0_ >= enchantment.getMinEnchantability(i) && p_77505_0_ <= enchantment.getMaxEnchantability(i)) {
                        if (map == null) {
                            map = (Map<Integer, EnchantmentData>)Maps.newHashMap();
                        }
                        map.put(enchantment.effectId, new EnchantmentData(enchantment, i));
                    }
                }
            }
        }
        return map;
    }
    
    static final class DamageIterator implements IModifier
    {
        public EntityLivingBase user;
        public Entity target;
        
        private DamageIterator() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
        }
    }
    
    static final class HurtIterator implements IModifier
    {
        public EntityLivingBase user;
        public Entity attacker;
        
        private HurtIterator() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            enchantmentIn.onUserHurt(this.user, this.attacker, enchantmentLevel);
        }
    }
    
    static final class ModifierDamage implements IModifier
    {
        public int damageModifier;
        public DamageSource source;
        
        private ModifierDamage() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            this.damageModifier += enchantmentIn.calcModifierDamage(enchantmentLevel, this.source);
        }
    }
    
    static final class ModifierLiving implements IModifier
    {
        public float livingModifier;
        public EnumCreatureAttribute entityLiving;
        
        private ModifierLiving() {
        }
        
        @Override
        public void calculateModifier(final Enchantment enchantmentIn, final int enchantmentLevel) {
            this.livingModifier += enchantmentIn.calcDamageByCreature(enchantmentLevel, this.entityLiving);
        }
    }
    
    interface IModifier
    {
        void calculateModifier(final Enchantment p0, final int p1);
    }
}
