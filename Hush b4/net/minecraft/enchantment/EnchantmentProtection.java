// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EnchantmentProtection extends Enchantment
{
    private static final String[] protectionName;
    private static final int[] baseEnchantability;
    private static final int[] levelEnchantability;
    private static final int[] thresholdEnchantability;
    public final int protectionType;
    
    static {
        protectionName = new String[] { "all", "fire", "fall", "explosion", "projectile" };
        baseEnchantability = new int[] { 1, 10, 5, 5, 3 };
        levelEnchantability = new int[] { 11, 8, 6, 8, 6 };
        thresholdEnchantability = new int[] { 20, 12, 10, 12, 15 };
    }
    
    public EnchantmentProtection(final int p_i45765_1_, final ResourceLocation p_i45765_2_, final int p_i45765_3_, final int p_i45765_4_) {
        super(p_i45765_1_, p_i45765_2_, p_i45765_3_, EnumEnchantmentType.ARMOR);
        this.protectionType = p_i45765_4_;
        if (p_i45765_4_ == 2) {
            this.type = EnumEnchantmentType.ARMOR_FEET;
        }
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return EnchantmentProtection.baseEnchantability[this.protectionType] + (enchantmentLevel - 1) * EnchantmentProtection.levelEnchantability[this.protectionType];
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + EnchantmentProtection.thresholdEnchantability[this.protectionType];
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    @Override
    public int calcModifierDamage(final int level, final DamageSource source) {
        if (source.canHarmInCreative()) {
            return 0;
        }
        final float f = (6 + level * level) / 3.0f;
        return (this.protectionType == 0) ? MathHelper.floor_float(f * 0.75f) : ((this.protectionType == 1 && source.isFireDamage()) ? MathHelper.floor_float(f * 1.25f) : ((this.protectionType == 2 && source == DamageSource.fall) ? MathHelper.floor_float(f * 2.5f) : ((this.protectionType == 3 && source.isExplosion()) ? MathHelper.floor_float(f * 1.5f) : ((this.protectionType == 4 && source.isProjectile()) ? MathHelper.floor_float(f * 1.5f) : 0))));
    }
    
    @Override
    public String getName() {
        return "enchantment.protect." + EnchantmentProtection.protectionName[this.protectionType];
    }
    
    @Override
    public boolean canApplyTogether(final Enchantment ench) {
        if (ench instanceof EnchantmentProtection) {
            final EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;
            return enchantmentprotection.protectionType != this.protectionType && (this.protectionType == 2 || enchantmentprotection.protectionType == 2);
        }
        return super.canApplyTogether(ench);
    }
    
    public static int getFireTimeForEntity(final Entity p_92093_0_, int p_92093_1_) {
        final int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.fireProtection.effectId, p_92093_0_.getInventory());
        if (i > 0) {
            p_92093_1_ -= MathHelper.floor_float(p_92093_1_ * (float)i * 0.15f);
        }
        return p_92093_1_;
    }
    
    public static double func_92092_a(final Entity p_92092_0_, double p_92092_1_) {
        final int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.blastProtection.effectId, p_92092_0_.getInventory());
        if (i > 0) {
            p_92092_1_ -= MathHelper.floor_double(p_92092_1_ * (i * 0.15f));
        }
        return p_92092_1_;
    }
}
