/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public enum EnumParticleTypes
/*     */ {
/*  10 */   EXPLOSION_NORMAL("explode", 0, true), 
/*  11 */   EXPLOSION_LARGE("largeexplode", 1, true), 
/*  12 */   EXPLOSION_HUGE("hugeexplosion", 2, true), 
/*  13 */   FIREWORKS_SPARK("fireworksSpark", 3, false), 
/*  14 */   WATER_BUBBLE("bubble", 4, false), 
/*  15 */   WATER_SPLASH("splash", 5, false), 
/*  16 */   WATER_WAKE("wake", 6, false), 
/*  17 */   SUSPENDED("suspended", 7, false), 
/*  18 */   SUSPENDED_DEPTH("depthsuspend", 8, false), 
/*  19 */   CRIT("crit", 9, false), 
/*  20 */   CRIT_MAGIC("magicCrit", 10, false), 
/*  21 */   SMOKE_NORMAL("smoke", 11, false), 
/*  22 */   SMOKE_LARGE("largesmoke", 12, false), 
/*  23 */   SPELL("spell", 13, false), 
/*  24 */   SPELL_INSTANT("instantSpell", 14, false), 
/*  25 */   SPELL_MOB("mobSpell", 15, false), 
/*  26 */   SPELL_MOB_AMBIENT("mobSpellAmbient", 16, false), 
/*  27 */   SPELL_WITCH("witchMagic", 17, false), 
/*  28 */   DRIP_WATER("dripWater", 18, false), 
/*  29 */   DRIP_LAVA("dripLava", 19, false), 
/*  30 */   VILLAGER_ANGRY("angryVillager", 20, false), 
/*  31 */   VILLAGER_HAPPY("happyVillager", 21, false), 
/*  32 */   TOWN_AURA("townaura", 22, false), 
/*  33 */   NOTE("note", 23, false), 
/*  34 */   PORTAL("portal", 24, false), 
/*  35 */   ENCHANTMENT_TABLE("enchantmenttable", 25, false), 
/*  36 */   FLAME("flame", 26, false), 
/*  37 */   LAVA("lava", 27, false), 
/*  38 */   FOOTSTEP("footstep", 28, false), 
/*  39 */   CLOUD("cloud", 29, false), 
/*  40 */   REDSTONE("reddust", 30, false), 
/*  41 */   SNOWBALL("snowballpoof", 31, false), 
/*  42 */   SNOW_SHOVEL("snowshovel", 32, false), 
/*  43 */   SLIME("slime", 33, false), 
/*  44 */   HEART("heart", 34, false), 
/*  45 */   BARRIER("barrier", 35, false), 
/*  46 */   ITEM_CRACK("iconcrack_", 36, false, 2), 
/*  47 */   BLOCK_CRACK("blockcrack_", 37, false, 1), 
/*  48 */   BLOCK_DUST("blockdust_", 38, false, 1), 
/*  49 */   WATER_DROP("droplet", 39, false), 
/*  50 */   ITEM_TAKE("take", 40, false), 
/*  51 */   MOB_APPEARANCE("mobappearance", 41, true);
/*     */   
/*     */   private final String particleName;
/*     */   private final int particleID;
/*     */   private final boolean shouldIgnoreRange;
/*     */   private final int argumentCount;
/*     */   private static final Map<Integer, EnumParticleTypes> PARTICLES;
/*     */   private static final String[] PARTICLE_NAMES;
/*     */   
/*     */   private EnumParticleTypes(String particleNameIn, int particleIDIn, boolean p_i46011_5_, int argumentCountIn)
/*     */   {
/*  62 */     this.particleName = particleNameIn;
/*  63 */     this.particleID = particleIDIn;
/*  64 */     this.shouldIgnoreRange = p_i46011_5_;
/*  65 */     this.argumentCount = argumentCountIn;
/*     */   }
/*     */   
/*     */   private EnumParticleTypes(String particleNameIn, int particleIDIn, boolean p_i46012_5_)
/*     */   {
/*  70 */     this(particleNameIn, particleIDIn, p_i46012_5_, 0);
/*     */   }
/*     */   
/*     */   public static String[] getParticleNames()
/*     */   {
/*  75 */     return PARTICLE_NAMES;
/*     */   }
/*     */   
/*     */   public String getParticleName()
/*     */   {
/*  80 */     return this.particleName;
/*     */   }
/*     */   
/*     */   public int getParticleID()
/*     */   {
/*  85 */     return this.particleID;
/*     */   }
/*     */   
/*     */   public int getArgumentCount()
/*     */   {
/*  90 */     return this.argumentCount;
/*     */   }
/*     */   
/*     */   public boolean getShouldIgnoreRange()
/*     */   {
/*  95 */     return this.shouldIgnoreRange;
/*     */   }
/*     */   
/*     */   public boolean hasArguments()
/*     */   {
/* 100 */     return this.argumentCount > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumParticleTypes getParticleFromId(int particleId)
/*     */   {
/* 108 */     return (EnumParticleTypes)PARTICLES.get(Integer.valueOf(particleId));
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  57 */     PARTICLES = Maps.newHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */     List<String> list = Lists.newArrayList();
/*     */     EnumParticleTypes[] arrayOfEnumParticleTypes;
/* 114 */     int j = (arrayOfEnumParticleTypes = values()).length; for (int i = 0; i < j; i++) { EnumParticleTypes enumparticletypes = arrayOfEnumParticleTypes[i];
/*     */       
/* 116 */       PARTICLES.put(Integer.valueOf(enumparticletypes.getParticleID()), enumparticletypes);
/*     */       
/* 118 */       if (!enumparticletypes.getParticleName().endsWith("_"))
/*     */       {
/* 120 */         list.add(enumparticletypes.getParticleName());
/*     */       }
/*     */     }
/*     */     
/* 124 */     PARTICLE_NAMES = (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\EnumParticleTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */