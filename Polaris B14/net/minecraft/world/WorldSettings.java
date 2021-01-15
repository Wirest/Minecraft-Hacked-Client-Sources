/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.world.storage.WorldInfo;
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
/*     */ public final class WorldSettings
/*     */ {
/*     */   private final long seed;
/*     */   private final GameType theGameType;
/*     */   private final boolean mapFeaturesEnabled;
/*     */   private final boolean hardcoreEnabled;
/*     */   private final WorldType terrainType;
/*     */   private boolean commandsAllowed;
/*     */   private boolean bonusChestEnabled;
/*     */   private String worldName;
/*     */   
/*     */   public WorldSettings(long seedIn, GameType gameType, boolean enableMapFeatures, boolean hardcoreMode, WorldType worldTypeIn)
/*     */   {
/*  32 */     this.worldName = "";
/*  33 */     this.seed = seedIn;
/*  34 */     this.theGameType = gameType;
/*  35 */     this.mapFeaturesEnabled = enableMapFeatures;
/*  36 */     this.hardcoreEnabled = hardcoreMode;
/*  37 */     this.terrainType = worldTypeIn;
/*     */   }
/*     */   
/*     */   public WorldSettings(WorldInfo info)
/*     */   {
/*  42 */     this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WorldSettings enableBonusChest()
/*     */   {
/*  50 */     this.bonusChestEnabled = true;
/*  51 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WorldSettings enableCommands()
/*     */   {
/*  59 */     this.commandsAllowed = true;
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public WorldSettings setWorldName(String name)
/*     */   {
/*  65 */     this.worldName = name;
/*  66 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBonusChestEnabled()
/*     */   {
/*  74 */     return this.bonusChestEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSeed()
/*     */   {
/*  82 */     return this.seed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameType getGameType()
/*     */   {
/*  90 */     return this.theGameType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getHardcoreEnabled()
/*     */   {
/*  98 */     return this.hardcoreEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMapFeaturesEnabled()
/*     */   {
/* 106 */     return this.mapFeaturesEnabled;
/*     */   }
/*     */   
/*     */   public WorldType getTerrainType()
/*     */   {
/* 111 */     return this.terrainType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean areCommandsAllowed()
/*     */   {
/* 119 */     return this.commandsAllowed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GameType getGameTypeById(int id)
/*     */   {
/* 127 */     return GameType.getByID(id);
/*     */   }
/*     */   
/*     */   public String getWorldName()
/*     */   {
/* 132 */     return this.worldName;
/*     */   }
/*     */   
/*     */   public static enum GameType
/*     */   {
/* 137 */     NOT_SET(-1, ""), 
/* 138 */     SURVIVAL(0, "survival"), 
/* 139 */     CREATIVE(1, "creative"), 
/* 140 */     ADVENTURE(2, "adventure"), 
/* 141 */     SPECTATOR(3, "spectator");
/*     */     
/*     */     int id;
/*     */     String name;
/*     */     
/*     */     private GameType(int typeId, String nameIn)
/*     */     {
/* 148 */       this.id = typeId;
/* 149 */       this.name = nameIn;
/*     */     }
/*     */     
/*     */     public int getID()
/*     */     {
/* 154 */       return this.id;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 159 */       return this.name;
/*     */     }
/*     */     
/*     */     public void configurePlayerCapabilities(PlayerCapabilities capabilities)
/*     */     {
/* 164 */       if (this == CREATIVE)
/*     */       {
/* 166 */         capabilities.allowFlying = true;
/* 167 */         capabilities.isCreativeMode = true;
/* 168 */         capabilities.disableDamage = true;
/*     */       }
/* 170 */       else if (this == SPECTATOR)
/*     */       {
/* 172 */         capabilities.allowFlying = true;
/* 173 */         capabilities.isCreativeMode = false;
/* 174 */         capabilities.disableDamage = true;
/* 175 */         capabilities.isFlying = true;
/*     */       }
/*     */       else
/*     */       {
/* 179 */         capabilities.allowFlying = false;
/* 180 */         capabilities.isCreativeMode = false;
/* 181 */         capabilities.disableDamage = false;
/* 182 */         capabilities.isFlying = false;
/*     */       }
/*     */       
/* 185 */       capabilities.allowEdit = (!isAdventure());
/*     */     }
/*     */     
/*     */     public boolean isAdventure()
/*     */     {
/* 190 */       return (this == ADVENTURE) || (this == SPECTATOR);
/*     */     }
/*     */     
/*     */     public boolean isCreative()
/*     */     {
/* 195 */       return this == CREATIVE;
/*     */     }
/*     */     
/*     */     public boolean isSurvivalOrAdventure()
/*     */     {
/* 200 */       return (this == SURVIVAL) || (this == ADVENTURE);
/*     */     }
/*     */     
/*     */     public static GameType getByID(int idIn) {
/*     */       GameType[] arrayOfGameType;
/* 205 */       int j = (arrayOfGameType = values()).length; for (int i = 0; i < j; i++) { GameType worldsettings$gametype = arrayOfGameType[i];
/*     */         
/* 207 */         if (worldsettings$gametype.id == idIn)
/*     */         {
/* 209 */           return worldsettings$gametype;
/*     */         }
/*     */       }
/*     */       
/* 213 */       return SURVIVAL;
/*     */     }
/*     */     
/*     */     public static GameType getByName(String p_77142_0_) {
/*     */       GameType[] arrayOfGameType;
/* 218 */       int j = (arrayOfGameType = values()).length; for (int i = 0; i < j; i++) { GameType worldsettings$gametype = arrayOfGameType[i];
/*     */         
/* 220 */         if (worldsettings$gametype.name.equals(p_77142_0_))
/*     */         {
/* 222 */           return worldsettings$gametype;
/*     */         }
/*     */       }
/*     */       
/* 226 */       return SURVIVAL;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */