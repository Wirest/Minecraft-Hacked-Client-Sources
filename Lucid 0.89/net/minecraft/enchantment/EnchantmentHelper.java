package net.minecraft.enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom;

public class EnchantmentHelper
{
    /** Is the random seed of enchantment effects. */
    private static final Random enchantmentRand = new Random();

    /**
     * Used to calculate the extra armor of enchantments on armors equipped on player.
     */
    private static final EnchantmentHelper.ModifierDamage enchantmentModifierDamage = new EnchantmentHelper.ModifierDamage(null);

    /**
     * Used to calculate the (magic) extra damage done by enchantments on current equipped item of player.
     */
    private static final EnchantmentHelper.ModifierLiving enchantmentModifierLiving = new EnchantmentHelper.ModifierLiving(null);
    private static final EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT = new EnchantmentHelper.HurtIterator(null);
    private static final EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new EnchantmentHelper.DamageIterator(null);

    /**
     * Returns the level of enchantment on the ItemStack passed.
     *  
     * @param enchID The ID for the enchantment you are looking for.
     * @param stack The ItemStack being searched.
     */
    public static int getEnchantmentLevel(int enchID, ItemStack stack)
    {
        if (stack == null)
        {
            return 0;
        }
        else
        {
            NBTTagList var2 = stack.getEnchantmentTagList();

            if (var2 == null)
            {
                return 0;
            }
            else
            {
                for (int var3 = 0; var3 < var2.tagCount(); ++var3)
                {
                    short var4 = var2.getCompoundTagAt(var3).getShort("id");
                    short var5 = var2.getCompoundTagAt(var3).getShort("lvl");

                    if (var4 == enchID)
                    {
                        return var5;
                    }
                }

                return 0;
            }
        }
    }

    /**
     * Return the enchantments for the specified stack.
     *  
     * @param stack The stack being searched for enchantments.
     */
    public static Map getEnchantments(ItemStack stack)
    {
        LinkedHashMap var1 = Maps.newLinkedHashMap();
        NBTTagList var2 = stack.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();

        if (var2 != null)
        {
            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                short var4 = var2.getCompoundTagAt(var3).getShort("id");
                short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
                var1.put(Integer.valueOf(var4), Integer.valueOf(var5));
            }
        }

        return var1;
    }

    /**
     * Set the enchantments for the specified stack.
     *  
     * @param enchMap A map containing all the enchantments you wish to add. Enchantments stored with the ID as the key,
     * and the level as the value.
     * @param stack The stack to have enchantments applied to.
     */
    public static void setEnchantments(Map enchMap, ItemStack stack)
    {
        NBTTagList var2 = new NBTTagList();
        Iterator var3 = enchMap.keySet().iterator();

        while (var3.hasNext())
        {
            int var4 = ((Integer)var3.next()).intValue();
            Enchantment var5 = Enchantment.getEnchantmentById(var4);

            if (var5 != null)
            {
                NBTTagCompound var6 = new NBTTagCompound();
                var6.setShort("id", (short)var4);
                var6.setShort("lvl", (short)((Integer)enchMap.get(Integer.valueOf(var4))).intValue());
                var2.appendTag(var6);

                if (stack.getItem() == Items.enchanted_book)
                {
                    Items.enchanted_book.addEnchantment(stack, new EnchantmentData(var5, ((Integer)enchMap.get(Integer.valueOf(var4))).intValue()));
                }
            }
        }

        if (var2.tagCount() > 0)
        {
            if (stack.getItem() != Items.enchanted_book)
            {
                stack.setTagInfo("ench", var2);
            }
        }
        else if (stack.hasTagCompound())
        {
            stack.getTagCompound().removeTag("ench");
        }
    }

    /**
     * Returns the biggest level of the enchantment on the array of ItemStack passed.
     *  
     * @param enchID The ID of the enchantment being searched for.
     * @param stacks The array of stacks being searched.
     */
    public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks)
    {
        if (stacks == null)
        {
            return 0;
        }
        else
        {
            int var2 = 0;
            ItemStack[] var3 = stacks;
            int var4 = stacks.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                ItemStack var6 = var3[var5];
                int var7 = getEnchantmentLevel(enchID, var6);

                if (var7 > var2)
                {
                    var2 = var7;
                }
            }

            return var2;
        }
    }

    /**
     * Executes the enchantment modifier on the ItemStack passed.
     *  
     * @param modifier The modifier being applied.
     * @param stack The ItemStack having a modifier applied to.
     */
    private static void applyEnchantmentModifier(EnchantmentHelper.IModifier modifier, ItemStack stack)
    {
        if (stack != null)
        {
            NBTTagList var2 = stack.getEnchantmentTagList();

            if (var2 != null)
            {
                for (int var3 = 0; var3 < var2.tagCount(); ++var3)
                {
                    short var4 = var2.getCompoundTagAt(var3).getShort("id");
                    short var5 = var2.getCompoundTagAt(var3).getShort("lvl");

                    if (Enchantment.getEnchantmentById(var4) != null)
                    {
                        modifier.calculateModifier(Enchantment.getEnchantmentById(var4), var5);
                    }
                }
            }
        }
    }

    /**
     * Executes the enchantment modifier on the array of ItemStack passed.
     *  
     * @param modifier The modifier being applied.
     * @param stacks An array of ItemStacks that will have the modifier applied to them.
     */
    private static void applyEnchantmentModifierArray(EnchantmentHelper.IModifier modifier, ItemStack[] stacks)
    {
        ItemStack[] var2 = stacks;
        int var3 = stacks.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ItemStack var5 = var2[var4];
            applyEnchantmentModifier(modifier, var5);
        }
    }

    /**
     * Returns the modifier of protection enchantments on armors equipped on player.
     *  
     * @param stacks An array of ItemStacks being checked.
     * @param source The source of the damage.
     */
    public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source)
    {
        enchantmentModifierDamage.damageModifier = 0;
        enchantmentModifierDamage.source = source;
        applyEnchantmentModifierArray(enchantmentModifierDamage, stacks);

        if (enchantmentModifierDamage.damageModifier > 25)
        {
            enchantmentModifierDamage.damageModifier = 25;
        }

        return (enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
    }

    public static float func_152377_a(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_)
    {
        enchantmentModifierLiving.livingModifier = 0.0F;
        enchantmentModifierLiving.entityLiving = p_152377_1_;
        applyEnchantmentModifier(enchantmentModifierLiving, p_152377_0_);
        return enchantmentModifierLiving.livingModifier;
    }

    public static void func_151384_a(EntityLivingBase p_151384_0_, Entity p_151384_1_)
    {
        ENCHANTMENT_ITERATOR_HURT.field_151363_b = p_151384_1_;
        ENCHANTMENT_ITERATOR_HURT.field_151364_a = p_151384_0_;

        if (p_151384_0_ != null)
        {
            applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getInventory());
        }

        if (p_151384_1_ instanceof EntityPlayer)
        {
            applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItem());
        }
    }

    public static void func_151385_b(EntityLivingBase p_151385_0_, Entity p_151385_1_)
    {
        ENCHANTMENT_ITERATOR_DAMAGE.field_151366_a = p_151385_0_;
        ENCHANTMENT_ITERATOR_DAMAGE.field_151365_b = p_151385_1_;

        if (p_151385_0_ != null)
        {
            applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getInventory());
        }

        if (p_151385_0_ instanceof EntityPlayer)
        {
            applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItem());
        }
    }

    /**
     * Returns the Knockback modifier of the enchantment on the players held item.
     *  
     * @param player The player being checked.
     */
    public static int getKnockbackModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
    }

    /**
     * Returns the fire aspect modifier of the players held item.
     *  
     * @param player The player being checked.
     */
    public static int getFireAspectModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
    }

    /**
     * Returns the 'Water Breathing' modifier of enchantments on player equipped armors.
     *  
     * @param player The player being checked.
     */
    public static int getRespiration(Entity player)
    {
        return getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
    }

    /**
     * Returns the level of the Depth Strider enchantment.
     *  
     * @param player The player being checked.
     */
    public static int getDepthStriderModifier(Entity player)
    {
        return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
    }

    /**
     * Return the extra efficiency of tools based on enchantments on equipped player item.
     *  
     * @param player The player being checked.
     */
    public static int getEfficiencyModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
    }

    /**
     * Returns the silk touch status of enchantments on current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static boolean getSilkTouchModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0;
    }

    /**
     * Returns the fortune enchantment modifier of the current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static int getFortuneModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
    }

    /**
     * Returns the level of the 'Luck Of The Sea' enchantment.
     *  
     * @param player The player being checked.
     */
    public static int getLuckOfSeaModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
    }

    /**
     * Returns the level of the 'Lure' enchantment on the players held item.
     *  
     * @param player The player being checked.
     */
    public static int getLureModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
    }

    /**
     * Returns the looting enchantment modifier of the current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static int getLootingModifier(EntityLivingBase player)
    {
        return getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
    }

    /**
     * Returns the aqua affinity status of enchantments on current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static boolean getAquaAffinityModifier(EntityLivingBase player)
    {
        return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0;
    }

    public static ItemStack getEnchantedItem(Enchantment p_92099_0_, EntityLivingBase p_92099_1_)
    {
        ItemStack[] var2 = p_92099_1_.getInventory();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ItemStack var5 = var2[var4];

            if (var5 != null && getEnchantmentLevel(p_92099_0_.effectId, var5) > 0)
            {
                return var5;
            }
        }

        return null;
    }

    /**
     * Returns the enchantability of itemstack, it's uses a singular formula for each index (2nd parameter: 0, 1 and 2),
     * cutting to the max enchantability power of the table (3rd parameter)
     */
    public static int calcItemStackEnchantability(Random p_77514_0_, int p_77514_1_, int p_77514_2_, ItemStack p_77514_3_)
    {
        Item var4 = p_77514_3_.getItem();
        int var5 = var4.getItemEnchantability();

        if (var5 <= 0)
        {
            return 0;
        }
        else
        {
            if (p_77514_2_ > 15)
            {
                p_77514_2_ = 15;
            }

            int var6 = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
            return p_77514_1_ == 0 ? Math.max(var6 / 3, 1) : (p_77514_1_ == 1 ? var6 * 2 / 3 + 1 : Math.max(var6, p_77514_2_ * 2));
        }
    }

    /**
     * Adds a random enchantment to the specified item. Args: random, itemStack, enchantabilityLevel
     */
    public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_)
    {
        List var3 = buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
        boolean var4 = p_77504_1_.getItem() == Items.book;

        if (var4)
        {
            p_77504_1_.setItem(Items.enchanted_book);
        }

        if (var3 != null)
        {
            Iterator var5 = var3.iterator();

            while (var5.hasNext())
            {
                EnchantmentData var6 = (EnchantmentData)var5.next();

                if (var4)
                {
                    Items.enchanted_book.addEnchantment(p_77504_1_, var6);
                }
                else
                {
                    p_77504_1_.addEnchantment(var6.enchantmentobj, var6.enchantmentLevel);
                }
            }
        }

        return p_77504_1_;
    }

    /**
     * Create a list of random EnchantmentData (enchantments) that can be added together to the ItemStack, the 3rd
     * parameter is the total enchantability level.
     */
    public static List buildEnchantmentList(Random p_77513_0_, ItemStack p_77513_1_, int p_77513_2_)
    {
        Item var3 = p_77513_1_.getItem();
        int var4 = var3.getItemEnchantability();

        if (var4 <= 0)
        {
            return null;
        }
        else
        {
            var4 /= 2;
            var4 = 1 + p_77513_0_.nextInt((var4 >> 1) + 1) + p_77513_0_.nextInt((var4 >> 1) + 1);
            int var5 = var4 + p_77513_2_;
            float var6 = (p_77513_0_.nextFloat() + p_77513_0_.nextFloat() - 1.0F) * 0.15F;
            int var7 = (int)(var5 * (1.0F + var6) + 0.5F);

            if (var7 < 1)
            {
                var7 = 1;
            }

            ArrayList var8 = null;
            Map var9 = mapEnchantmentData(var7, p_77513_1_);

            if (var9 != null && !var9.isEmpty())
            {
                EnchantmentData var10 = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, var9.values());

                if (var10 != null)
                {
                    var8 = Lists.newArrayList();
                    var8.add(var10);

                    for (int var11 = var7; p_77513_0_.nextInt(50) <= var11; var11 >>= 1)
                    {
                        Iterator var12 = var9.keySet().iterator();

                        while (var12.hasNext())
                        {
                            Integer var13 = (Integer)var12.next();
                            boolean var14 = true;
                            Iterator var15 = var8.iterator();

                            while (true)
                            {
                                if (var15.hasNext())
                                {
                                    EnchantmentData var16 = (EnchantmentData)var15.next();

                                    if (var16.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(var13.intValue())))
                                    {
                                        continue;
                                    }

                                    var14 = false;
                                }

                                if (!var14)
                                {
                                    var12.remove();
                                }

                                break;
                            }
                        }

                        if (!var9.isEmpty())
                        {
                            EnchantmentData var17 = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, var9.values());
                            var8.add(var17);
                        }
                    }
                }
            }

            return var8;
        }
    }

    /**
     * Creates a 'Map' of EnchantmentData (enchantments) possible to add on the ItemStack and the enchantability level
     * passed.
     */
    public static Map mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_)
    {
        Item var2 = p_77505_1_.getItem();
        HashMap var3 = null;
        boolean var4 = p_77505_1_.getItem() == Items.book;
        Enchantment[] var5 = Enchantment.enchantmentsBookList;
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            Enchantment var8 = var5[var7];

            if (var8 != null && (var8.type.canEnchantItem(var2) || var4))
            {
                for (int var9 = var8.getMinLevel(); var9 <= var8.getMaxLevel(); ++var9)
                {
                    if (p_77505_0_ >= var8.getMinEnchantability(var9) && p_77505_0_ <= var8.getMaxEnchantability(var9))
                    {
                        if (var3 == null)
                        {
                            var3 = Maps.newHashMap();
                        }

                        var3.put(Integer.valueOf(var8.effectId), new EnchantmentData(var8, var9));
                    }
                }
            }
        }

        return var3;
    }

    static final class DamageIterator implements EnchantmentHelper.IModifier
    {
        public EntityLivingBase field_151366_a;
        public Entity field_151365_b;

        private DamageIterator() {}

        @Override
		public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
        {
            p_77493_1_.onEntityDamaged(this.field_151366_a, this.field_151365_b, p_77493_2_);
        }

        DamageIterator(Object p_i45359_1_)
        {
            this();
        }
    }

    static final class HurtIterator implements EnchantmentHelper.IModifier
    {
        public EntityLivingBase field_151364_a;
        public Entity field_151363_b;

        private HurtIterator() {}

        @Override
		public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
        {
            p_77493_1_.onUserHurt(this.field_151364_a, this.field_151363_b, p_77493_2_);
        }

        HurtIterator(Object p_i45360_1_)
        {
            this();
        }
    }

    interface IModifier
    {
        void calculateModifier(Enchantment var1, int var2);
    }

    static final class ModifierDamage implements EnchantmentHelper.IModifier
    {
        public int damageModifier;
        public DamageSource source;

        private ModifierDamage() {}

        @Override
		public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
        {
            this.damageModifier += p_77493_1_.calcModifierDamage(p_77493_2_, this.source);
        }

        ModifierDamage(Object p_i1929_1_)
        {
            this();
        }
    }

    static final class ModifierLiving implements EnchantmentHelper.IModifier
    {
        public float livingModifier;
        public EnumCreatureAttribute entityLiving;

        private ModifierLiving() {}

        @Override
		public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
        {
            this.livingModifier += p_77493_1_.calcDamageByCreature(p_77493_2_, this.entityLiving);
        }

        ModifierLiving(Object p_i1928_1_)
        {
            this();
        }
    }
}
