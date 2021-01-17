// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.Iterator;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import java.util.UUID;
import net.minecraft.util.StringUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityLivingBase;
import java.util.Set;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class Potion
{
    public static final Potion[] potionTypes;
    private static final Map<ResourceLocation, Potion> field_180150_I;
    public static final Potion field_180151_b;
    public static final Potion moveSpeed;
    public static final Potion moveSlowdown;
    public static final Potion digSpeed;
    public static final Potion digSlowdown;
    public static final Potion damageBoost;
    public static final Potion heal;
    public static final Potion harm;
    public static final Potion jump;
    public static final Potion confusion;
    public static final Potion regeneration;
    public static final Potion resistance;
    public static final Potion fireResistance;
    public static final Potion waterBreathing;
    public static final Potion invisibility;
    public static final Potion blindness;
    public static final Potion nightVision;
    public static final Potion hunger;
    public static final Potion weakness;
    public static final Potion poison;
    public static final Potion wither;
    public static final Potion healthBoost;
    public static final Potion absorption;
    public static final Potion saturation;
    public static final Potion field_180153_z;
    public static final Potion field_180147_A;
    public static final Potion field_180148_B;
    public static final Potion field_180149_C;
    public static final Potion field_180143_D;
    public static final Potion field_180144_E;
    public static final Potion field_180145_F;
    public static final Potion field_180146_G;
    public final int id;
    private final Map<IAttribute, AttributeModifier> attributeModifierMap;
    private final boolean isBadEffect;
    private final int liquidColor;
    private String name;
    private int statusIconIndex;
    private double effectiveness;
    private boolean usable;
    
    static {
        potionTypes = new Potion[32];
        field_180150_I = Maps.newHashMap();
        field_180151_b = null;
        moveSpeed = new Potion(1, new ResourceLocation("speed"), false, 8171462).setPotionName("potion.moveSpeed").setIconIndex(0, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224, 2);
        moveSlowdown = new Potion(2, new ResourceLocation("slowness"), true, 5926017).setPotionName("potion.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448, 2);
        digSpeed = new Potion(3, new ResourceLocation("haste"), false, 14270531).setPotionName("potion.digSpeed").setIconIndex(2, 0).setEffectiveness(1.5);
        digSlowdown = new Potion(4, new ResourceLocation("mining_fatigue"), true, 4866583).setPotionName("potion.digSlowDown").setIconIndex(3, 0);
        damageBoost = new PotionAttackDamage(5, new ResourceLocation("strength"), false, 9643043).setPotionName("potion.damageBoost").setIconIndex(4, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5, 2);
        heal = new PotionHealth(6, new ResourceLocation("instant_health"), false, 16262179).setPotionName("potion.heal");
        harm = new PotionHealth(7, new ResourceLocation("instant_damage"), true, 4393481).setPotionName("potion.harm");
        jump = new Potion(8, new ResourceLocation("jump_boost"), false, 2293580).setPotionName("potion.jump").setIconIndex(2, 1);
        confusion = new Potion(9, new ResourceLocation("nausea"), true, 5578058).setPotionName("potion.confusion").setIconIndex(3, 1).setEffectiveness(0.25);
        regeneration = new Potion(10, new ResourceLocation("regeneration"), false, 13458603).setPotionName("potion.regeneration").setIconIndex(7, 0).setEffectiveness(0.25);
        resistance = new Potion(11, new ResourceLocation("resistance"), false, 10044730).setPotionName("potion.resistance").setIconIndex(6, 1);
        fireResistance = new Potion(12, new ResourceLocation("fire_resistance"), false, 14981690).setPotionName("potion.fireResistance").setIconIndex(7, 1);
        waterBreathing = new Potion(13, new ResourceLocation("water_breathing"), false, 3035801).setPotionName("potion.waterBreathing").setIconIndex(0, 2);
        invisibility = new Potion(14, new ResourceLocation("invisibility"), false, 8356754).setPotionName("potion.invisibility").setIconIndex(0, 1);
        blindness = new Potion(15, new ResourceLocation("blindness"), true, 2039587).setPotionName("potion.blindness").setIconIndex(5, 1).setEffectiveness(0.25);
        nightVision = new Potion(16, new ResourceLocation("night_vision"), false, 2039713).setPotionName("potion.nightVision").setIconIndex(4, 1);
        hunger = new Potion(17, new ResourceLocation("hunger"), true, 5797459).setPotionName("potion.hunger").setIconIndex(1, 1);
        weakness = new PotionAttackDamage(18, new ResourceLocation("weakness"), true, 4738376).setPotionName("potion.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0, 0);
        poison = new Potion(19, new ResourceLocation("poison"), true, 5149489).setPotionName("potion.poison").setIconIndex(6, 0).setEffectiveness(0.25);
        wither = new Potion(20, new ResourceLocation("wither"), true, 3484199).setPotionName("potion.wither").setIconIndex(1, 2).setEffectiveness(0.25);
        healthBoost = new PotionHealthBoost(21, new ResourceLocation("health_boost"), false, 16284963).setPotionName("potion.healthBoost").setIconIndex(2, 2).registerPotionAttributeModifier(SharedMonsterAttributes.maxHealth, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0, 0);
        absorption = new PotionAbsorption(22, new ResourceLocation("absorption"), false, 2445989).setPotionName("potion.absorption").setIconIndex(2, 2);
        saturation = new PotionHealth(23, new ResourceLocation("saturation"), false, 16262179).setPotionName("potion.saturation");
        field_180153_z = null;
        field_180147_A = null;
        field_180148_B = null;
        field_180149_C = null;
        field_180143_D = null;
        field_180144_E = null;
        field_180145_F = null;
        field_180146_G = null;
    }
    
    protected Potion(final int potionID, final ResourceLocation location, final boolean badEffect, final int potionColor) {
        this.attributeModifierMap = (Map<IAttribute, AttributeModifier>)Maps.newHashMap();
        this.name = "";
        this.statusIconIndex = -1;
        this.id = potionID;
        Potion.potionTypes[potionID] = this;
        Potion.field_180150_I.put(location, this);
        this.isBadEffect = badEffect;
        if (badEffect) {
            this.effectiveness = 0.5;
        }
        else {
            this.effectiveness = 1.0;
        }
        this.liquidColor = potionColor;
    }
    
    public static Potion getPotionFromResourceLocation(final String location) {
        return Potion.field_180150_I.get(new ResourceLocation(location));
    }
    
    public static Set<ResourceLocation> func_181168_c() {
        return Potion.field_180150_I.keySet();
    }
    
    protected Potion setIconIndex(final int p_76399_1_, final int p_76399_2_) {
        this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void performEffect(final EntityLivingBase entityLivingBaseIn, final int p_76394_2_) {
        if (this.id == Potion.regeneration.id) {
            if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
                entityLivingBaseIn.heal(1.0f);
            }
        }
        else if (this.id == Potion.poison.id) {
            if (entityLivingBaseIn.getHealth() > 1.0f) {
                entityLivingBaseIn.attackEntityFrom(DamageSource.magic, 1.0f);
            }
        }
        else if (this.id == Potion.wither.id) {
            entityLivingBaseIn.attackEntityFrom(DamageSource.wither, 1.0f);
        }
        else if (this.id == Potion.hunger.id && entityLivingBaseIn instanceof EntityPlayer) {
            ((EntityPlayer)entityLivingBaseIn).addExhaustion(0.025f * (p_76394_2_ + 1));
        }
        else if (this.id == Potion.saturation.id && entityLivingBaseIn instanceof EntityPlayer) {
            if (!entityLivingBaseIn.worldObj.isRemote) {
                ((EntityPlayer)entityLivingBaseIn).getFoodStats().addStats(p_76394_2_ + 1, 1.0f);
            }
        }
        else if ((this.id != Potion.heal.id || entityLivingBaseIn.isEntityUndead()) && (this.id != Potion.harm.id || !entityLivingBaseIn.isEntityUndead())) {
            if ((this.id == Potion.harm.id && !entityLivingBaseIn.isEntityUndead()) || (this.id == Potion.heal.id && entityLivingBaseIn.isEntityUndead())) {
                entityLivingBaseIn.attackEntityFrom(DamageSource.magic, (float)(6 << p_76394_2_));
            }
        }
        else {
            entityLivingBaseIn.heal((float)Math.max(4 << p_76394_2_, 0));
        }
    }
    
    public void affectEntity(final Entity p_180793_1_, final Entity p_180793_2_, final EntityLivingBase entityLivingBaseIn, final int p_180793_4_, final double p_180793_5_) {
        if ((this.id != Potion.heal.id || entityLivingBaseIn.isEntityUndead()) && (this.id != Potion.harm.id || !entityLivingBaseIn.isEntityUndead())) {
            if ((this.id == Potion.harm.id && !entityLivingBaseIn.isEntityUndead()) || (this.id == Potion.heal.id && entityLivingBaseIn.isEntityUndead())) {
                final int j = (int)(p_180793_5_ * (6 << p_180793_4_) + 0.5);
                if (p_180793_1_ == null) {
                    entityLivingBaseIn.attackEntityFrom(DamageSource.magic, (float)j);
                }
                else {
                    entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(p_180793_1_, p_180793_2_), (float)j);
                }
            }
        }
        else {
            final int i = (int)(p_180793_5_ * (4 << p_180793_4_) + 0.5);
            entityLivingBaseIn.heal((float)i);
        }
    }
    
    public boolean isInstant() {
        return false;
    }
    
    public boolean isReady(final int p_76397_1_, final int p_76397_2_) {
        if (this.id == Potion.regeneration.id) {
            final int k = 50 >> p_76397_2_;
            return k <= 0 || p_76397_1_ % k == 0;
        }
        if (this.id == Potion.poison.id) {
            final int j = 25 >> p_76397_2_;
            return j <= 0 || p_76397_1_ % j == 0;
        }
        if (this.id == Potion.wither.id) {
            final int i = 40 >> p_76397_2_;
            return i <= 0 || p_76397_1_ % i == 0;
        }
        return this.id == Potion.hunger.id;
    }
    
    public Potion setPotionName(final String nameIn) {
        this.name = nameIn;
        return this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean hasStatusIcon() {
        return this.statusIconIndex >= 0;
    }
    
    public int getStatusIconIndex() {
        return this.statusIconIndex;
    }
    
    public boolean isBadEffect() {
        return this.isBadEffect;
    }
    
    public static String getDurationString(final PotionEffect effect) {
        if (effect.getIsPotionDurationMax()) {
            return "**:**";
        }
        final int i = effect.getDuration();
        return StringUtils.ticksToElapsedTime(i);
    }
    
    protected Potion setEffectiveness(final double effectivenessIn) {
        this.effectiveness = effectivenessIn;
        return this;
    }
    
    public double getEffectiveness() {
        return this.effectiveness;
    }
    
    public boolean isUsable() {
        return this.usable;
    }
    
    public int getLiquidColor() {
        return this.liquidColor;
    }
    
    public Potion registerPotionAttributeModifier(final IAttribute p_111184_1_, final String p_111184_2_, final double p_111184_3_, final int p_111184_5_) {
        final AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(p_111184_2_), this.getName(), p_111184_3_, p_111184_5_);
        this.attributeModifierMap.put(p_111184_1_, attributemodifier);
        return this;
    }
    
    public Map<IAttribute, AttributeModifier> getAttributeModifierMap() {
        return this.attributeModifierMap;
    }
    
    public void removeAttributesModifiersFromEntity(final EntityLivingBase entityLivingBaseIn, final BaseAttributeMap p_111187_2_, final int amplifier) {
        for (final Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            final IAttributeInstance iattributeinstance = p_111187_2_.getAttributeInstance(entry.getKey());
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(entry.getValue());
            }
        }
    }
    
    public void applyAttributesModifiersToEntity(final EntityLivingBase entityLivingBaseIn, final BaseAttributeMap p_111185_2_, final int amplifier) {
        for (final Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            final IAttributeInstance iattributeinstance = p_111185_2_.getAttributeInstance(entry.getKey());
            if (iattributeinstance != null) {
                final AttributeModifier attributemodifier = entry.getValue();
                iattributeinstance.removeModifier(attributemodifier);
                iattributeinstance.applyModifier(new AttributeModifier(attributemodifier.getID(), String.valueOf(this.getName()) + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributemodifier), attributemodifier.getOperation()));
            }
        }
    }
    
    public double getAttributeModifierAmount(final int p_111183_1_, final AttributeModifier modifier) {
        return modifier.getAmount() * (p_111183_1_ + 1);
    }
}
