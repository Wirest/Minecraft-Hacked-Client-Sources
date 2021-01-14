package net.minecraft.potion;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class Potion {
    /**
     * The array of potion types.
     */
    public static final Potion[] potionTypes = new Potion[32];
    private static final Map field_180150_I = Maps.newHashMap();
    public static final Potion field_180151_b = null;
    public static final Potion moveSpeed = (new Potion(1, new ResourceLocation("speed"), false, 8171462)).setPotionName("potion.moveSpeed").setIconIndex(0, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224D, 2);
    public static final Potion moveSlowdown = (new Potion(2, new ResourceLocation("slowness"), true, 5926017)).setPotionName("potion.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2);
    public static final Potion digSpeed = (new Potion(3, new ResourceLocation("haste"), false, 14270531)).setPotionName("potion.digSpeed").setIconIndex(2, 0).setEffectiveness(1.5D);
    public static final Potion digSlowdown = (new Potion(4, new ResourceLocation("mining_fatigue"), true, 4866583)).setPotionName("potion.digSlowDown").setIconIndex(3, 0);
    public static final Potion damageBoost = (new PotionAttackDamage(5, new ResourceLocation("strength"), false, 9643043)).setPotionName("potion.damageBoost").setIconIndex(4, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5D, 2);
    public static final Potion heal = (new PotionHealth(6, new ResourceLocation("instant_health"), false, 16262179)).setPotionName("potion.heal");
    public static final Potion harm = (new PotionHealth(7, new ResourceLocation("instant_damage"), true, 4393481)).setPotionName("potion.harm");
    public static final Potion jump = (new Potion(8, new ResourceLocation("jump_boost"), false, 2293580)).setPotionName("potion.jump").setIconIndex(2, 1);
    public static final Potion confusion = (new Potion(9, new ResourceLocation("nausea"), true, 5578058)).setPotionName("potion.confusion").setIconIndex(3, 1).setEffectiveness(0.25D);

    /**
     * The regeneration Potion object.
     */
    public static final Potion regeneration = (new Potion(10, new ResourceLocation("regeneration"), false, 13458603)).setPotionName("potion.regeneration").setIconIndex(7, 0).setEffectiveness(0.25D);
    public static final Potion resistance = (new Potion(11, new ResourceLocation("resistance"), false, 10044730)).setPotionName("potion.resistance").setIconIndex(6, 1);

    /**
     * The fire resistance Potion object.
     */
    public static final Potion fireResistance = (new Potion(12, new ResourceLocation("fire_resistance"), false, 14981690)).setPotionName("potion.fireResistance").setIconIndex(7, 1);

    /**
     * The water breathing Potion object.
     */
    public static final Potion waterBreathing = (new Potion(13, new ResourceLocation("water_breathing"), false, 3035801)).setPotionName("potion.waterBreathing").setIconIndex(0, 2);

    /**
     * The invisibility Potion object.
     */
    public static final Potion invisibility = (new Potion(14, new ResourceLocation("invisibility"), false, 8356754)).setPotionName("potion.invisibility").setIconIndex(0, 1);

    /**
     * The blindness Potion object.
     */
    public static final Potion blindness = (new Potion(15, new ResourceLocation("blindness"), true, 2039587)).setPotionName("potion.blindness").setIconIndex(5, 1).setEffectiveness(0.25D);

    /**
     * The night vision Potion object.
     */
    public static final Potion nightVision = (new Potion(16, new ResourceLocation("night_vision"), false, 2039713)).setPotionName("potion.nightVision").setIconIndex(4, 1);

    /**
     * The hunger Potion object.
     */
    public static final Potion hunger = (new Potion(17, new ResourceLocation("hunger"), true, 5797459)).setPotionName("potion.hunger").setIconIndex(1, 1);

    /**
     * The weakness Potion object.
     */
    public static final Potion weakness = (new PotionAttackDamage(18, new ResourceLocation("weakness"), true, 4738376)).setPotionName("potion.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0D, 0);

    /**
     * The poison Potion object.
     */
    public static final Potion poison = (new Potion(19, new ResourceLocation("poison"), true, 5149489)).setPotionName("potion.poison").setIconIndex(6, 0).setEffectiveness(0.25D);

    /**
     * The wither Potion object.
     */
    public static final Potion wither = (new Potion(20, new ResourceLocation("wither"), true, 3484199)).setPotionName("potion.wither").setIconIndex(1, 2).setEffectiveness(0.25D);
    public static final Potion field_180152_w = (new PotionHealthBoost(21, new ResourceLocation("health_boost"), false, 16284963)).setPotionName("potion.healthBoost").setIconIndex(2, 2).registerPotionAttributeModifier(SharedMonsterAttributes.maxHealth, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0D, 0);

    /**
     * The absorption Potion object.
     */
    public static final Potion absorption = (new PotionAbsoption(22, new ResourceLocation("absorption"), false, 2445989)).setPotionName("potion.absorption").setIconIndex(2, 2);

    /**
     * The saturation Potion object.
     */
    public static final Potion saturation = (new PotionHealth(23, new ResourceLocation("saturation"), false, 16262179)).setPotionName("potion.saturation");
    public static final Potion field_180153_z = null;
    public static final Potion field_180147_A = null;
    public static final Potion field_180148_B = null;
    public static final Potion field_180149_C = null;
    public static final Potion field_180143_D = null;
    public static final Potion field_180144_E = null;
    public static final Potion field_180145_F = null;
    public static final Potion field_180146_G = null;

    /**
     * The Id of a Potion object.
     */
    public final int id;

    /**
     * Contains a Map of the AttributeModifiers registered by potions
     */
    private final Map attributeModifierMap = Maps.newHashMap();

    /**
     * This field indicated if the effect is 'bad' - negative - for the entity.
     */
    private final boolean isBadEffect;

    /**
     * Is the color of the liquid for this potion.
     */
    private final int liquidColor;

    /**
     * The name of the Potion.
     */
    private String name = "";

    /**
     * The index for the icon displayed when the potion effect is active.
     */
    private int statusIconIndex = -1;
    private double effectiveness;
    private boolean usable;
    private static final String __OBFID = "CL_00001528";

    protected Potion(int p_i45897_1_, ResourceLocation p_i45897_2_, boolean p_i45897_3_, int p_i45897_4_) {
        this.id = p_i45897_1_;
        potionTypes[p_i45897_1_] = this;
        field_180150_I.put(p_i45897_2_, this);
        this.isBadEffect = p_i45897_3_;

        if (p_i45897_3_) {
            this.effectiveness = 0.5D;
        } else {
            this.effectiveness = 1.0D;
        }

        this.liquidColor = p_i45897_4_;
    }

    public static Potion func_180142_b(String p_180142_0_) {
        return (Potion) field_180150_I.get(new ResourceLocation(p_180142_0_));
    }

    public static String[] func_180141_c() {
        String[] var0 = new String[field_180150_I.size()];
        int var1 = 0;
        ResourceLocation var3;

        for (Iterator var2 = field_180150_I.keySet().iterator(); var2.hasNext(); var0[var1++] = var3.toString()) {
            var3 = (ResourceLocation) var2.next();
        }

        return var0;
    }

    /**
     * Sets the index for the icon displayed in the player's inventory when the status is active.
     */
    protected Potion setIconIndex(int p_76399_1_, int p_76399_2_) {
        this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }

    /**
     * returns the ID of the potion
     */
    public int getId() {
        return this.id;
    }

    public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_) {
        if (this.id == regeneration.id) {
            if (p_76394_1_.getHealth() < p_76394_1_.getMaxHealth()) {
                p_76394_1_.heal(1.0F);
            }
        } else if (this.id == poison.id) {
            if (p_76394_1_.getHealth() > 1.0F) {
                p_76394_1_.attackEntityFrom(DamageSource.magic, 1.0F);
            }
        } else if (this.id == wither.id) {
            p_76394_1_.attackEntityFrom(DamageSource.wither, 1.0F);
        } else if (this.id == hunger.id && p_76394_1_ instanceof EntityPlayer) {
            ((EntityPlayer) p_76394_1_).addExhaustion(0.025F * (float) (p_76394_2_ + 1));
        } else if (this.id == saturation.id && p_76394_1_ instanceof EntityPlayer) {
            if (!p_76394_1_.worldObj.isRemote) {
                ((EntityPlayer) p_76394_1_).getFoodStats().addStats(p_76394_2_ + 1, 1.0F);
            }
        } else if ((this.id != heal.id || p_76394_1_.isEntityUndead()) && (this.id != harm.id || !p_76394_1_.isEntityUndead())) {
            if (this.id == harm.id && !p_76394_1_.isEntityUndead() || this.id == heal.id && p_76394_1_.isEntityUndead()) {
                p_76394_1_.attackEntityFrom(DamageSource.magic, (float) (6 << p_76394_2_));
            }
        } else {
            p_76394_1_.heal((float) Math.max(4 << p_76394_2_, 0));
        }
    }

    public void func_180793_a(Entity p_180793_1_, Entity p_180793_2_, EntityLivingBase p_180793_3_, int p_180793_4_, double p_180793_5_) {
        int var7;

        if ((this.id != heal.id || p_180793_3_.isEntityUndead()) && (this.id != harm.id || !p_180793_3_.isEntityUndead())) {
            if (this.id == harm.id && !p_180793_3_.isEntityUndead() || this.id == heal.id && p_180793_3_.isEntityUndead()) {
                var7 = (int) (p_180793_5_ * (double) (6 << p_180793_4_) + 0.5D);

                if (p_180793_1_ == null) {
                    p_180793_3_.attackEntityFrom(DamageSource.magic, (float) var7);
                } else {
                    p_180793_3_.attackEntityFrom(DamageSource.causeIndirectMagicDamage(p_180793_1_, p_180793_2_), (float) var7);
                }
            }
        } else {
            var7 = (int) (p_180793_5_ * (double) (4 << p_180793_4_) + 0.5D);
            p_180793_3_.heal((float) var7);
        }
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstant() {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        int var3;

        if (this.id == regeneration.id) {
            var3 = 50 >> p_76397_2_;
            return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
        } else if (this.id == poison.id) {
            var3 = 25 >> p_76397_2_;
            return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
        } else if (this.id == wither.id) {
            var3 = 40 >> p_76397_2_;
            return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
        } else {
            return this.id == hunger.id;
        }
    }

    /**
     * Set the potion name.
     */
    public Potion setPotionName(String p_76390_1_) {
        this.name = p_76390_1_;
        return this;
    }

    /**
     * returns the name of the potion
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns true if the potion has a associated status icon to display in then inventory when active.
     */
    public boolean hasStatusIcon() {
        return this.statusIconIndex >= 0;
    }

    /**
     * Returns the index for the icon to display when the potion is active.
     */
    public int getStatusIconIndex() {
        return this.statusIconIndex;
    }

    /**
     * This method returns true if the potion effect is bad - negative - for the entity.
     */
    public boolean isBadEffect() {
        return this.isBadEffect;
    }

    public static String getDurationString(PotionEffect p_76389_0_) {
        if (p_76389_0_.getIsPotionDurationMax()) {
            return "**:**";
        } else {
            int var1 = p_76389_0_.getDuration();
            return StringUtils.ticksToElapsedTime(var1);
        }
    }

    protected Potion setEffectiveness(double p_76404_1_) {
        this.effectiveness = p_76404_1_;
        return this;
    }

    public double getEffectiveness() {
        return this.effectiveness;
    }

    public boolean isUsable() {
        return this.usable;
    }

    /**
     * Returns the color of the potion liquid.
     */
    public int getLiquidColor() {
        return this.liquidColor;
    }

    /**
     * Used by potions to register the attribute they modify.
     */
    public Potion registerPotionAttributeModifier(IAttribute p_111184_1_, String p_111184_2_, double p_111184_3_, int p_111184_5_) {
        AttributeModifier var6 = new AttributeModifier(UUID.fromString(p_111184_2_), this.getName(), p_111184_3_, p_111184_5_);
        this.attributeModifierMap.put(p_111184_1_, var6);
        return this;
    }

    public Map func_111186_k() {
        return this.attributeModifierMap;
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase p_111187_1_, BaseAttributeMap p_111187_2_, int p_111187_3_) {
        Iterator var4 = this.attributeModifierMap.entrySet().iterator();

        while (var4.hasNext()) {
            Entry var5 = (Entry) var4.next();
            IAttributeInstance var6 = p_111187_2_.getAttributeInstance((IAttribute) var5.getKey());

            if (var6 != null) {
                var6.removeModifier((AttributeModifier) var5.getValue());
            }
        }
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase p_111185_1_, BaseAttributeMap p_111185_2_, int p_111185_3_) {
        Iterator var4 = this.attributeModifierMap.entrySet().iterator();

        while (var4.hasNext()) {
            Entry var5 = (Entry) var4.next();
            IAttributeInstance var6 = p_111185_2_.getAttributeInstance((IAttribute) var5.getKey());

            if (var6 != null) {
                AttributeModifier var7 = (AttributeModifier) var5.getValue();
                var6.removeModifier(var7);
                var6.applyModifier(new AttributeModifier(var7.getID(), this.getName() + " " + p_111185_3_, this.func_111183_a(p_111185_3_, var7), var7.getOperation()));
            }
        }
    }

    public double func_111183_a(int p_111183_1_, AttributeModifier p_111183_2_) {
        return p_111183_2_.getAmount() * (double) (p_111183_1_ + 1);
    }
}
