// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import java.util.Set;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public abstract class Enchantment
{
    private static final Enchantment[] enchantmentsList;
    public static final Enchantment[] enchantmentsBookList;
    private static final Map<ResourceLocation, Enchantment> locationEnchantments;
    public static final Enchantment protection;
    public static final Enchantment fireProtection;
    public static final Enchantment featherFalling;
    public static final Enchantment blastProtection;
    public static final Enchantment projectileProtection;
    public static final Enchantment respiration;
    public static final Enchantment aquaAffinity;
    public static final Enchantment thorns;
    public static final Enchantment depthStrider;
    public static final Enchantment sharpness;
    public static final Enchantment smite;
    public static final Enchantment baneOfArthropods;
    public static final Enchantment knockback;
    public static final Enchantment fireAspect;
    public static final Enchantment looting;
    public static final Enchantment efficiency;
    public static final Enchantment silkTouch;
    public static final Enchantment unbreaking;
    public static final Enchantment fortune;
    public static final Enchantment power;
    public static final Enchantment punch;
    public static final Enchantment flame;
    public static final Enchantment infinity;
    public static final Enchantment luckOfTheSea;
    public static final Enchantment lure;
    public final int effectId;
    private final int weight;
    public EnumEnchantmentType type;
    protected String name;
    
    static {
        enchantmentsList = new Enchantment[256];
        locationEnchantments = Maps.newHashMap();
        protection = new EnchantmentProtection(0, new ResourceLocation("protection"), 10, 0);
        fireProtection = new EnchantmentProtection(1, new ResourceLocation("fire_protection"), 5, 1);
        featherFalling = new EnchantmentProtection(2, new ResourceLocation("feather_falling"), 5, 2);
        blastProtection = new EnchantmentProtection(3, new ResourceLocation("blast_protection"), 2, 3);
        projectileProtection = new EnchantmentProtection(4, new ResourceLocation("projectile_protection"), 5, 4);
        respiration = new EnchantmentOxygen(5, new ResourceLocation("respiration"), 2);
        aquaAffinity = new EnchantmentWaterWorker(6, new ResourceLocation("aqua_affinity"), 2);
        thorns = new EnchantmentThorns(7, new ResourceLocation("thorns"), 1);
        depthStrider = new EnchantmentWaterWalker(8, new ResourceLocation("depth_strider"), 2);
        sharpness = new EnchantmentDamage(16, new ResourceLocation("sharpness"), 10, 0);
        smite = new EnchantmentDamage(17, new ResourceLocation("smite"), 5, 1);
        baneOfArthropods = new EnchantmentDamage(18, new ResourceLocation("bane_of_arthropods"), 5, 2);
        knockback = new EnchantmentKnockback(19, new ResourceLocation("knockback"), 5);
        fireAspect = new EnchantmentFireAspect(20, new ResourceLocation("fire_aspect"), 2);
        looting = new EnchantmentLootBonus(21, new ResourceLocation("looting"), 2, EnumEnchantmentType.WEAPON);
        efficiency = new EnchantmentDigging(32, new ResourceLocation("efficiency"), 10);
        silkTouch = new EnchantmentUntouching(33, new ResourceLocation("silk_touch"), 1);
        unbreaking = new EnchantmentDurability(34, new ResourceLocation("unbreaking"), 5);
        fortune = new EnchantmentLootBonus(35, new ResourceLocation("fortune"), 2, EnumEnchantmentType.DIGGER);
        power = new EnchantmentArrowDamage(48, new ResourceLocation("power"), 10);
        punch = new EnchantmentArrowKnockback(49, new ResourceLocation("punch"), 2);
        flame = new EnchantmentArrowFire(50, new ResourceLocation("flame"), 2);
        infinity = new EnchantmentArrowInfinite(51, new ResourceLocation("infinity"), 1);
        luckOfTheSea = new EnchantmentLootBonus(61, new ResourceLocation("luck_of_the_sea"), 2, EnumEnchantmentType.FISHING_ROD);
        lure = new EnchantmentFishingSpeed(62, new ResourceLocation("lure"), 2, EnumEnchantmentType.FISHING_ROD);
        final List<Enchantment> list = (List<Enchantment>)Lists.newArrayList();
        Enchantment[] enchantmentsList2;
        for (int length = (enchantmentsList2 = Enchantment.enchantmentsList).length, i = 0; i < length; ++i) {
            final Enchantment enchantment = enchantmentsList2[i];
            if (enchantment != null) {
                list.add(enchantment);
            }
        }
        enchantmentsBookList = list.toArray(new Enchantment[list.size()]);
    }
    
    public static Enchantment getEnchantmentById(final int enchID) {
        return (enchID >= 0 && enchID < Enchantment.enchantmentsList.length) ? Enchantment.enchantmentsList[enchID] : null;
    }
    
    protected Enchantment(final int enchID, final ResourceLocation enchName, final int enchWeight, final EnumEnchantmentType enchType) {
        this.effectId = enchID;
        this.weight = enchWeight;
        this.type = enchType;
        if (Enchantment.enchantmentsList[enchID] != null) {
            throw new IllegalArgumentException("Duplicate enchantment id!");
        }
        Enchantment.enchantmentsList[enchID] = this;
        Enchantment.locationEnchantments.put(enchName, this);
    }
    
    public static Enchantment getEnchantmentByLocation(final String location) {
        return Enchantment.locationEnchantments.get(new ResourceLocation(location));
    }
    
    public static Set<ResourceLocation> func_181077_c() {
        return Enchantment.locationEnchantments.keySet();
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public int getMinLevel() {
        return 1;
    }
    
    public int getMaxLevel() {
        return 1;
    }
    
    public int getMinEnchantability(final int enchantmentLevel) {
        return 1 + enchantmentLevel * 10;
    }
    
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 5;
    }
    
    public int calcModifierDamage(final int level, final DamageSource source) {
        return 0;
    }
    
    public float calcDamageByCreature(final int level, final EnumCreatureAttribute creatureType) {
        return 0.0f;
    }
    
    public boolean canApplyTogether(final Enchantment ench) {
        return this != ench;
    }
    
    public Enchantment setName(final String enchName) {
        this.name = enchName;
        return this;
    }
    
    public String getName() {
        return "enchantment." + this.name;
    }
    
    public String getTranslatedName(final int level) {
        final String s = StatCollector.translateToLocal(this.getName());
        return String.valueOf(s) + " " + StatCollector.translateToLocal("enchantment.level." + level);
    }
    
    public boolean canApply(final ItemStack stack) {
        return this.type.canEnchantItem(stack.getItem());
    }
    
    public void onEntityDamaged(final EntityLivingBase user, final Entity target, final int level) {
    }
    
    public void onUserHurt(final EntityLivingBase user, final Entity attacker, final int level) {
    }
}
