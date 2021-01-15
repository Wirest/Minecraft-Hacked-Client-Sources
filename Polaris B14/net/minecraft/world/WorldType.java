/*     */ package net.minecraft.world;
/*     */ 
/*     */ 
/*     */ public class WorldType
/*     */ {
/*   6 */   public static final WorldType[] worldTypes = new WorldType[16];
/*     */   
/*     */ 
/*   9 */   public static final WorldType DEFAULT = new WorldType(0, "default", 1).setVersioned();
/*     */   
/*     */ 
/*  12 */   public static final WorldType FLAT = new WorldType(1, "flat");
/*     */   
/*     */ 
/*  15 */   public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
/*     */   
/*     */ 
/*  18 */   public static final WorldType AMPLIFIED = new WorldType(3, "amplified").setNotificationData();
/*  19 */   public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
/*  20 */   public static final WorldType DEBUG_WORLD = new WorldType(5, "debug_all_block_states");
/*     */   
/*     */ 
/*  23 */   public static final WorldType DEFAULT_1_1 = new WorldType(8, "default_1_1", 0).setCanBeCreated(false);
/*     */   
/*     */ 
/*     */   private final int worldTypeId;
/*     */   
/*     */ 
/*     */   private final String worldType;
/*     */   
/*     */ 
/*     */   private final int generatorVersion;
/*     */   
/*     */   private boolean canBeCreated;
/*     */   
/*     */   private boolean isWorldTypeVersioned;
/*     */   
/*     */   private boolean hasNotificationData;
/*     */   
/*     */ 
/*     */   private WorldType(int id, String name)
/*     */   {
/*  43 */     this(id, name, 0);
/*     */   }
/*     */   
/*     */   private WorldType(int id, String name, int version)
/*     */   {
/*  48 */     this.worldType = name;
/*  49 */     this.generatorVersion = version;
/*  50 */     this.canBeCreated = true;
/*  51 */     this.worldTypeId = id;
/*  52 */     worldTypes[id] = this;
/*     */   }
/*     */   
/*     */   public String getWorldTypeName()
/*     */   {
/*  57 */     return this.worldType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTranslateName()
/*     */   {
/*  65 */     return "generator." + this.worldType;
/*     */   }
/*     */   
/*     */   public String func_151359_c()
/*     */   {
/*  70 */     return getTranslateName() + ".info";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getGeneratorVersion()
/*     */   {
/*  78 */     return this.generatorVersion;
/*     */   }
/*     */   
/*     */   public WorldType getWorldTypeForGeneratorVersion(int version)
/*     */   {
/*  83 */     return (this == DEFAULT) && (version == 0) ? DEFAULT_1_1 : this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private WorldType setCanBeCreated(boolean enable)
/*     */   {
/*  91 */     this.canBeCreated = enable;
/*  92 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanBeCreated()
/*     */   {
/* 100 */     return this.canBeCreated;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private WorldType setVersioned()
/*     */   {
/* 108 */     this.isWorldTypeVersioned = true;
/* 109 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVersioned()
/*     */   {
/* 117 */     return this.isWorldTypeVersioned;
/*     */   }
/*     */   
/*     */   public static WorldType parseWorldType(String type)
/*     */   {
/* 122 */     for (int i = 0; i < worldTypes.length; i++)
/*     */     {
/* 124 */       if ((worldTypes[i] != null) && (worldTypes[i].worldType.equalsIgnoreCase(type)))
/*     */       {
/* 126 */         return worldTypes[i];
/*     */       }
/*     */     }
/*     */     
/* 130 */     return null;
/*     */   }
/*     */   
/*     */   public int getWorldTypeID()
/*     */   {
/* 135 */     return this.worldTypeId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean showWorldInfoNotice()
/*     */   {
/* 144 */     return this.hasNotificationData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private WorldType setNotificationData()
/*     */   {
/* 152 */     this.hasNotificationData = true;
/* 153 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */