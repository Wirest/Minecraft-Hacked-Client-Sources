// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLivingBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect
{
    private static final Logger LOGGER;
    private int potionID;
    private int duration;
    private int amplifier;
    private boolean isSplashPotion;
    private boolean isAmbient;
    private boolean isPotionDurationMax;
    private boolean showParticles;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public PotionEffect(final int id, final int effectDuration) {
        this(id, effectDuration, 0);
    }
    
    public PotionEffect(final int id, final int effectDuration, final int effectAmplifier) {
        this(id, effectDuration, effectAmplifier, false, true);
    }
    
    public PotionEffect(final int id, final int effectDuration, final int effectAmplifier, final boolean ambient, final boolean showParticles) {
        this.potionID = id;
        this.duration = effectDuration;
        this.amplifier = effectAmplifier;
        this.isAmbient = ambient;
        this.showParticles = showParticles;
    }
    
    public PotionEffect(final PotionEffect other) {
        this.potionID = other.potionID;
        this.duration = other.duration;
        this.amplifier = other.amplifier;
        this.isAmbient = other.isAmbient;
        this.showParticles = other.showParticles;
    }
    
    public void combine(final PotionEffect other) {
        if (this.potionID != other.potionID) {
            PotionEffect.LOGGER.warn("This method should only be called for matching effects!");
        }
        if (other.amplifier > this.amplifier) {
            this.amplifier = other.amplifier;
            this.duration = other.duration;
        }
        else if (other.amplifier == this.amplifier && this.duration < other.duration) {
            this.duration = other.duration;
        }
        else if (!other.isAmbient && this.isAmbient) {
            this.isAmbient = other.isAmbient;
        }
        this.showParticles = other.showParticles;
    }
    
    public int getPotionID() {
        return this.potionID;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public int getAmplifier() {
        return this.amplifier;
    }
    
    public void setSplashPotion(final boolean splashPotion) {
        this.isSplashPotion = splashPotion;
    }
    
    public boolean getIsAmbient() {
        return this.isAmbient;
    }
    
    public boolean getIsShowParticles() {
        return this.showParticles;
    }
    
    public boolean onUpdate(final EntityLivingBase entityIn) {
        if (this.duration > 0) {
            if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier)) {
                this.performEffect(entityIn);
            }
            this.deincrementDuration();
        }
        return this.duration > 0;
    }
    
    private int deincrementDuration() {
        return --this.duration;
    }
    
    public void performEffect(final EntityLivingBase entityIn) {
        if (this.duration > 0) {
            Potion.potionTypes[this.potionID].performEffect(entityIn, this.amplifier);
        }
    }
    
    public String getEffectName() {
        return Potion.potionTypes[this.potionID].getName();
    }
    
    @Override
    public int hashCode() {
        return this.potionID;
    }
    
    @Override
    public String toString() {
        String s = "";
        if (this.getAmplifier() > 0) {
            s = String.valueOf(this.getEffectName()) + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDuration();
        }
        else {
            s = String.valueOf(this.getEffectName()) + ", Duration: " + this.getDuration();
        }
        if (this.isSplashPotion) {
            s = String.valueOf(s) + ", Splash: true";
        }
        if (!this.showParticles) {
            s = String.valueOf(s) + ", Particles: false";
        }
        return Potion.potionTypes[this.potionID].isUsable() ? ("(" + s + ")") : s;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof PotionEffect)) {
            return false;
        }
        final PotionEffect potioneffect = (PotionEffect)p_equals_1_;
        return this.potionID == potioneffect.potionID && this.amplifier == potioneffect.amplifier && this.duration == potioneffect.duration && this.isSplashPotion == potioneffect.isSplashPotion && this.isAmbient == potioneffect.isAmbient;
    }
    
    public NBTTagCompound writeCustomPotionEffectToNBT(final NBTTagCompound nbt) {
        nbt.setByte("Id", (byte)this.getPotionID());
        nbt.setByte("Amplifier", (byte)this.getAmplifier());
        nbt.setInteger("Duration", this.getDuration());
        nbt.setBoolean("Ambient", this.getIsAmbient());
        nbt.setBoolean("ShowParticles", this.getIsShowParticles());
        return nbt;
    }
    
    public static PotionEffect readCustomPotionEffectFromNBT(final NBTTagCompound nbt) {
        final int i = nbt.getByte("Id");
        if (i >= 0 && i < Potion.potionTypes.length && Potion.potionTypes[i] != null) {
            final int j = nbt.getByte("Amplifier");
            final int k = nbt.getInteger("Duration");
            final boolean flag = nbt.getBoolean("Ambient");
            boolean flag2 = true;
            if (nbt.hasKey("ShowParticles", 1)) {
                flag2 = nbt.getBoolean("ShowParticles");
            }
            return new PotionEffect(i, k, j, flag, flag2);
        }
        return null;
    }
    
    public void setPotionDurationMax(final boolean maxDuration) {
        this.isPotionDurationMax = maxDuration;
    }
    
    public boolean getIsPotionDurationMax() {
        return this.isPotionDurationMax;
    }
}
