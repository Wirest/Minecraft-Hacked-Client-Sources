/*     */ package net.minecraft.potion;
/*     */ 
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PotionEffect
/*     */ {
/*  10 */   private static final Logger LOGGER = ;
/*     */   
/*     */ 
/*     */   private int potionID;
/*     */   
/*     */ 
/*     */   private int duration;
/*     */   
/*     */ 
/*     */   private int amplifier;
/*     */   
/*     */ 
/*     */   private boolean isSplashPotion;
/*     */   
/*     */   private boolean isAmbient;
/*     */   
/*     */   private boolean isPotionDurationMax;
/*     */   
/*     */   private boolean showParticles;
/*     */   
/*     */ 
/*     */   public PotionEffect(int id, int effectDuration)
/*     */   {
/*  33 */     this(id, effectDuration, 0);
/*     */   }
/*     */   
/*     */   public PotionEffect(int id, int effectDuration, int effectAmplifier)
/*     */   {
/*  38 */     this(id, effectDuration, effectAmplifier, false, true);
/*     */   }
/*     */   
/*     */   public PotionEffect(int id, int effectDuration, int effectAmplifier, boolean ambient, boolean showParticles)
/*     */   {
/*  43 */     this.potionID = id;
/*  44 */     this.duration = effectDuration;
/*  45 */     this.amplifier = effectAmplifier;
/*  46 */     this.isAmbient = ambient;
/*  47 */     this.showParticles = showParticles;
/*     */   }
/*     */   
/*     */   public PotionEffect(PotionEffect other)
/*     */   {
/*  52 */     this.potionID = other.potionID;
/*  53 */     this.duration = other.duration;
/*  54 */     this.amplifier = other.amplifier;
/*  55 */     this.isAmbient = other.isAmbient;
/*  56 */     this.showParticles = other.showParticles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void combine(PotionEffect other)
/*     */   {
/*  65 */     if (this.potionID != other.potionID)
/*     */     {
/*  67 */       LOGGER.warn("This method should only be called for matching effects!");
/*     */     }
/*     */     
/*  70 */     if (other.amplifier > this.amplifier)
/*     */     {
/*  72 */       this.amplifier = other.amplifier;
/*  73 */       this.duration = other.duration;
/*     */     }
/*  75 */     else if ((other.amplifier == this.amplifier) && (this.duration < other.duration))
/*     */     {
/*  77 */       this.duration = other.duration;
/*     */     }
/*  79 */     else if ((!other.isAmbient) && (this.isAmbient))
/*     */     {
/*  81 */       this.isAmbient = other.isAmbient;
/*     */     }
/*     */     
/*  84 */     this.showParticles = other.showParticles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPotionID()
/*     */   {
/*  92 */     return this.potionID;
/*     */   }
/*     */   
/*     */   public int getDuration()
/*     */   {
/*  97 */     return this.duration;
/*     */   }
/*     */   
/*     */   public int getAmplifier()
/*     */   {
/* 102 */     return this.amplifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSplashPotion(boolean splashPotion)
/*     */   {
/* 110 */     this.isSplashPotion = splashPotion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getIsAmbient()
/*     */   {
/* 118 */     return this.isAmbient;
/*     */   }
/*     */   
/*     */   public boolean getIsShowParticles()
/*     */   {
/* 123 */     return this.showParticles;
/*     */   }
/*     */   
/*     */   public boolean onUpdate(EntityLivingBase entityIn)
/*     */   {
/* 128 */     if (this.duration > 0)
/*     */     {
/* 130 */       if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier))
/*     */       {
/* 132 */         performEffect(entityIn);
/*     */       }
/*     */       
/* 135 */       deincrementDuration();
/*     */     }
/*     */     
/* 138 */     return this.duration > 0;
/*     */   }
/*     */   
/*     */   private int deincrementDuration()
/*     */   {
/* 143 */     return --this.duration;
/*     */   }
/*     */   
/*     */   public void performEffect(EntityLivingBase entityIn)
/*     */   {
/* 148 */     if (this.duration > 0)
/*     */     {
/* 150 */       Potion.potionTypes[this.potionID].performEffect(entityIn, this.amplifier);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getEffectName()
/*     */   {
/* 156 */     return Potion.potionTypes[this.potionID].getName();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 161 */     return this.potionID;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 166 */     String s = "";
/*     */     
/* 168 */     if (getAmplifier() > 0)
/*     */     {
/* 170 */       s = getEffectName() + " x " + (getAmplifier() + 1) + ", Duration: " + getDuration();
/*     */     }
/*     */     else
/*     */     {
/* 174 */       s = getEffectName() + ", Duration: " + getDuration();
/*     */     }
/*     */     
/* 177 */     if (this.isSplashPotion)
/*     */     {
/* 179 */       s = s + ", Splash: true";
/*     */     }
/*     */     
/* 182 */     if (!this.showParticles)
/*     */     {
/* 184 */       s = s + ", Particles: false";
/*     */     }
/*     */     
/* 187 */     return Potion.potionTypes[this.potionID].isUsable() ? "(" + s + ")" : s;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 192 */     if (!(p_equals_1_ instanceof PotionEffect))
/*     */     {
/* 194 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 198 */     PotionEffect potioneffect = (PotionEffect)p_equals_1_;
/* 199 */     return (this.potionID == potioneffect.potionID) && (this.amplifier == potioneffect.amplifier) && (this.duration == potioneffect.duration) && (this.isSplashPotion == potioneffect.isSplashPotion) && (this.isAmbient == potioneffect.isAmbient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt)
/*     */   {
/* 208 */     nbt.setByte("Id", (byte)getPotionID());
/* 209 */     nbt.setByte("Amplifier", (byte)getAmplifier());
/* 210 */     nbt.setInteger("Duration", getDuration());
/* 211 */     nbt.setBoolean("Ambient", getIsAmbient());
/* 212 */     nbt.setBoolean("ShowParticles", getIsShowParticles());
/* 213 */     return nbt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt)
/*     */   {
/* 221 */     int i = nbt.getByte("Id");
/*     */     
/* 223 */     if ((i >= 0) && (i < Potion.potionTypes.length) && (Potion.potionTypes[i] != null))
/*     */     {
/* 225 */       int j = nbt.getByte("Amplifier");
/* 226 */       int k = nbt.getInteger("Duration");
/* 227 */       boolean flag = nbt.getBoolean("Ambient");
/* 228 */       boolean flag1 = true;
/*     */       
/* 230 */       if (nbt.hasKey("ShowParticles", 1))
/*     */       {
/* 232 */         flag1 = nbt.getBoolean("ShowParticles");
/*     */       }
/*     */       
/* 235 */       return new PotionEffect(i, k, j, flag, flag1);
/*     */     }
/*     */     
/*     */ 
/* 239 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPotionDurationMax(boolean maxDuration)
/*     */   {
/* 248 */     this.isPotionDurationMax = maxDuration;
/*     */   }
/*     */   
/*     */   public boolean getIsPotionDurationMax()
/*     */   {
/* 253 */     return this.isPotionDurationMax;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\potion\PotionEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */