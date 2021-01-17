// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDamage extends Enchantment
{
    private static final String[] protectionName;
    private static final int[] baseEnchantability;
    private static final int[] levelEnchantability;
    private static final int[] thresholdEnchantability;
    public final int damageType;
    
    static {
        protectionName = new String[] { "all", "undead", "arthropods" };
        baseEnchantability = new int[] { 1, 5, 5 };
        levelEnchantability = new int[] { 11, 8, 8 };
        thresholdEnchantability = new int[] { 20, 20, 20 };
    }
    
    public EnchantmentDamage(final int enchID, final ResourceLocation enchName, final int enchWeight, final int classification) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
        this.damageType = classification;
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return EnchantmentDamage.baseEnchantability[this.damageType] + (enchantmentLevel - 1) * EnchantmentDamage.levelEnchantability[this.damageType];
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + EnchantmentDamage.thresholdEnchantability[this.damageType];
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float calcDamageByCreature(final int level, final EnumCreatureAttribute creatureType) {
        return (this.damageType == 0) ? (level * 1.25f) : ((this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD) ? (level * 2.5f) : ((this.damageType == 2 && creatureType == EnumCreatureAttribute.ARTHROPOD) ? (level * 2.5f) : 0.0f));
    }
    
    @Override
    public String getName() {
        return "enchantment.damage." + EnchantmentDamage.protectionName[this.damageType];
    }
    
    @Override
    public boolean canApplyTogether(final Enchantment ench) {
        return !(ench instanceof EnchantmentDamage);
    }
    
    @Override
    public boolean canApply(final ItemStack stack) {
        return stack.getItem() instanceof ItemAxe || super.canApply(stack);
    }
    
    @Override
    public void onEntityDamaged(final EntityLivingBase user, final Entity target, final int level) {
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)target;
            if (this.damageType == 2 && entitylivingbase.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
                final int i = 20 + user.getRNG().nextInt(10 * level);
                entitylivingbase.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, i, 3));
            }
        }
    }
}
