/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ import net.minecraft.world.WorldType;
/*     */ 
/*     */ public class WorldInfo
/*     */ {
/*  15 */   public static final EnumDifficulty DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
/*     */   
/*     */   private long randomSeed;
/*     */   
/*  19 */   private WorldType terrainType = WorldType.DEFAULT;
/*  20 */   private String generatorOptions = "";
/*     */   
/*     */ 
/*     */   private int spawnX;
/*     */   
/*     */ 
/*     */   private int spawnY;
/*     */   
/*     */ 
/*     */   private int spawnZ;
/*     */   
/*     */ 
/*     */   private long totalTime;
/*     */   
/*     */ 
/*     */   private long worldTime;
/*     */   
/*     */ 
/*     */   private long lastTimePlayed;
/*     */   
/*     */ 
/*     */   private long sizeOnDisk;
/*     */   
/*     */ 
/*     */   private NBTTagCompound playerTag;
/*     */   
/*     */ 
/*     */   private int dimension;
/*     */   
/*     */ 
/*     */   private String levelName;
/*     */   
/*     */   private int saveVersion;
/*     */   
/*     */   private int cleanWeatherTime;
/*     */   
/*     */   private boolean raining;
/*     */   
/*     */   private int rainTime;
/*     */   
/*     */   private boolean thundering;
/*     */   
/*     */   private int thunderTime;
/*     */   
/*     */   private WorldSettings.GameType theGameType;
/*     */   
/*     */   private boolean mapFeaturesEnabled;
/*     */   
/*     */   private boolean hardcore;
/*     */   
/*     */   private boolean allowCommands;
/*     */   
/*     */   private boolean initialized;
/*     */   
/*     */   private EnumDifficulty difficulty;
/*     */   
/*     */   private boolean difficultyLocked;
/*     */   
/*  78 */   private double borderCenterX = 0.0D;
/*  79 */   private double borderCenterZ = 0.0D;
/*  80 */   private double borderSize = 6.0E7D;
/*  81 */   private long borderSizeLerpTime = 0L;
/*  82 */   private double borderSizeLerpTarget = 0.0D;
/*  83 */   private double borderSafeZone = 5.0D;
/*  84 */   private double borderDamagePerBlock = 0.2D;
/*  85 */   private int borderWarningDistance = 5;
/*  86 */   private int borderWarningTime = 15;
/*  87 */   private GameRules theGameRules = new GameRules();
/*     */   
/*     */ 
/*     */   protected WorldInfo() {}
/*     */   
/*     */ 
/*     */   public WorldInfo(NBTTagCompound nbt)
/*     */   {
/*  95 */     this.randomSeed = nbt.getLong("RandomSeed");
/*     */     
/*  97 */     if (nbt.hasKey("generatorName", 8))
/*     */     {
/*  99 */       String s = nbt.getString("generatorName");
/* 100 */       this.terrainType = WorldType.parseWorldType(s);
/*     */       
/* 102 */       if (this.terrainType == null)
/*     */       {
/* 104 */         this.terrainType = WorldType.DEFAULT;
/*     */       }
/* 106 */       else if (this.terrainType.isVersioned())
/*     */       {
/* 108 */         int i = 0;
/*     */         
/* 110 */         if (nbt.hasKey("generatorVersion", 99))
/*     */         {
/* 112 */           i = nbt.getInteger("generatorVersion");
/*     */         }
/*     */         
/* 115 */         this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(i);
/*     */       }
/*     */       
/* 118 */       if (nbt.hasKey("generatorOptions", 8))
/*     */       {
/* 120 */         this.generatorOptions = nbt.getString("generatorOptions");
/*     */       }
/*     */     }
/*     */     
/* 124 */     this.theGameType = WorldSettings.GameType.getByID(nbt.getInteger("GameType"));
/*     */     
/* 126 */     if (nbt.hasKey("MapFeatures", 99))
/*     */     {
/* 128 */       this.mapFeaturesEnabled = nbt.getBoolean("MapFeatures");
/*     */     }
/*     */     else
/*     */     {
/* 132 */       this.mapFeaturesEnabled = true;
/*     */     }
/*     */     
/* 135 */     this.spawnX = nbt.getInteger("SpawnX");
/* 136 */     this.spawnY = nbt.getInteger("SpawnY");
/* 137 */     this.spawnZ = nbt.getInteger("SpawnZ");
/* 138 */     this.totalTime = nbt.getLong("Time");
/*     */     
/* 140 */     if (nbt.hasKey("DayTime", 99))
/*     */     {
/* 142 */       this.worldTime = nbt.getLong("DayTime");
/*     */     }
/*     */     else
/*     */     {
/* 146 */       this.worldTime = this.totalTime;
/*     */     }
/*     */     
/* 149 */     this.lastTimePlayed = nbt.getLong("LastPlayed");
/* 150 */     this.sizeOnDisk = nbt.getLong("SizeOnDisk");
/* 151 */     this.levelName = nbt.getString("LevelName");
/* 152 */     this.saveVersion = nbt.getInteger("version");
/* 153 */     this.cleanWeatherTime = nbt.getInteger("clearWeatherTime");
/* 154 */     this.rainTime = nbt.getInteger("rainTime");
/* 155 */     this.raining = nbt.getBoolean("raining");
/* 156 */     this.thunderTime = nbt.getInteger("thunderTime");
/* 157 */     this.thundering = nbt.getBoolean("thundering");
/* 158 */     this.hardcore = nbt.getBoolean("hardcore");
/*     */     
/* 160 */     if (nbt.hasKey("initialized", 99))
/*     */     {
/* 162 */       this.initialized = nbt.getBoolean("initialized");
/*     */     }
/*     */     else
/*     */     {
/* 166 */       this.initialized = true;
/*     */     }
/*     */     
/* 169 */     if (nbt.hasKey("allowCommands", 99))
/*     */     {
/* 171 */       this.allowCommands = nbt.getBoolean("allowCommands");
/*     */     }
/*     */     else
/*     */     {
/* 175 */       this.allowCommands = (this.theGameType == WorldSettings.GameType.CREATIVE);
/*     */     }
/*     */     
/* 178 */     if (nbt.hasKey("Player", 10))
/*     */     {
/* 180 */       this.playerTag = nbt.getCompoundTag("Player");
/* 181 */       this.dimension = this.playerTag.getInteger("Dimension");
/*     */     }
/*     */     
/* 184 */     if (nbt.hasKey("GameRules", 10))
/*     */     {
/* 186 */       this.theGameRules.readFromNBT(nbt.getCompoundTag("GameRules"));
/*     */     }
/*     */     
/* 189 */     if (nbt.hasKey("Difficulty", 99))
/*     */     {
/* 191 */       this.difficulty = EnumDifficulty.getDifficultyEnum(nbt.getByte("Difficulty"));
/*     */     }
/*     */     
/* 194 */     if (nbt.hasKey("DifficultyLocked", 1))
/*     */     {
/* 196 */       this.difficultyLocked = nbt.getBoolean("DifficultyLocked");
/*     */     }
/*     */     
/* 199 */     if (nbt.hasKey("BorderCenterX", 99))
/*     */     {
/* 201 */       this.borderCenterX = nbt.getDouble("BorderCenterX");
/*     */     }
/*     */     
/* 204 */     if (nbt.hasKey("BorderCenterZ", 99))
/*     */     {
/* 206 */       this.borderCenterZ = nbt.getDouble("BorderCenterZ");
/*     */     }
/*     */     
/* 209 */     if (nbt.hasKey("BorderSize", 99))
/*     */     {
/* 211 */       this.borderSize = nbt.getDouble("BorderSize");
/*     */     }
/*     */     
/* 214 */     if (nbt.hasKey("BorderSizeLerpTime", 99))
/*     */     {
/* 216 */       this.borderSizeLerpTime = nbt.getLong("BorderSizeLerpTime");
/*     */     }
/*     */     
/* 219 */     if (nbt.hasKey("BorderSizeLerpTarget", 99))
/*     */     {
/* 221 */       this.borderSizeLerpTarget = nbt.getDouble("BorderSizeLerpTarget");
/*     */     }
/*     */     
/* 224 */     if (nbt.hasKey("BorderSafeZone", 99))
/*     */     {
/* 226 */       this.borderSafeZone = nbt.getDouble("BorderSafeZone");
/*     */     }
/*     */     
/* 229 */     if (nbt.hasKey("BorderDamagePerBlock", 99))
/*     */     {
/* 231 */       this.borderDamagePerBlock = nbt.getDouble("BorderDamagePerBlock");
/*     */     }
/*     */     
/* 234 */     if (nbt.hasKey("BorderWarningBlocks", 99))
/*     */     {
/* 236 */       this.borderWarningDistance = nbt.getInteger("BorderWarningBlocks");
/*     */     }
/*     */     
/* 239 */     if (nbt.hasKey("BorderWarningTime", 99))
/*     */     {
/* 241 */       this.borderWarningTime = nbt.getInteger("BorderWarningTime");
/*     */     }
/*     */   }
/*     */   
/*     */   public WorldInfo(WorldSettings settings, String name)
/*     */   {
/* 247 */     populateFromWorldSettings(settings);
/* 248 */     this.levelName = name;
/* 249 */     this.difficulty = DEFAULT_DIFFICULTY;
/* 250 */     this.initialized = false;
/*     */   }
/*     */   
/*     */   public void populateFromWorldSettings(WorldSettings settings)
/*     */   {
/* 255 */     this.randomSeed = settings.getSeed();
/* 256 */     this.theGameType = settings.getGameType();
/* 257 */     this.mapFeaturesEnabled = settings.isMapFeaturesEnabled();
/* 258 */     this.hardcore = settings.getHardcoreEnabled();
/* 259 */     this.terrainType = settings.getTerrainType();
/* 260 */     this.generatorOptions = settings.getWorldName();
/* 261 */     this.allowCommands = settings.areCommandsAllowed();
/*     */   }
/*     */   
/*     */   public WorldInfo(WorldInfo worldInformation)
/*     */   {
/* 266 */     this.randomSeed = worldInformation.randomSeed;
/* 267 */     this.terrainType = worldInformation.terrainType;
/* 268 */     this.generatorOptions = worldInformation.generatorOptions;
/* 269 */     this.theGameType = worldInformation.theGameType;
/* 270 */     this.mapFeaturesEnabled = worldInformation.mapFeaturesEnabled;
/* 271 */     this.spawnX = worldInformation.spawnX;
/* 272 */     this.spawnY = worldInformation.spawnY;
/* 273 */     this.spawnZ = worldInformation.spawnZ;
/* 274 */     this.totalTime = worldInformation.totalTime;
/* 275 */     this.worldTime = worldInformation.worldTime;
/* 276 */     this.lastTimePlayed = worldInformation.lastTimePlayed;
/* 277 */     this.sizeOnDisk = worldInformation.sizeOnDisk;
/* 278 */     this.playerTag = worldInformation.playerTag;
/* 279 */     this.dimension = worldInformation.dimension;
/* 280 */     this.levelName = worldInformation.levelName;
/* 281 */     this.saveVersion = worldInformation.saveVersion;
/* 282 */     this.rainTime = worldInformation.rainTime;
/* 283 */     this.raining = worldInformation.raining;
/* 284 */     this.thunderTime = worldInformation.thunderTime;
/* 285 */     this.thundering = worldInformation.thundering;
/* 286 */     this.hardcore = worldInformation.hardcore;
/* 287 */     this.allowCommands = worldInformation.allowCommands;
/* 288 */     this.initialized = worldInformation.initialized;
/* 289 */     this.theGameRules = worldInformation.theGameRules;
/* 290 */     this.difficulty = worldInformation.difficulty;
/* 291 */     this.difficultyLocked = worldInformation.difficultyLocked;
/* 292 */     this.borderCenterX = worldInformation.borderCenterX;
/* 293 */     this.borderCenterZ = worldInformation.borderCenterZ;
/* 294 */     this.borderSize = worldInformation.borderSize;
/* 295 */     this.borderSizeLerpTime = worldInformation.borderSizeLerpTime;
/* 296 */     this.borderSizeLerpTarget = worldInformation.borderSizeLerpTarget;
/* 297 */     this.borderSafeZone = worldInformation.borderSafeZone;
/* 298 */     this.borderDamagePerBlock = worldInformation.borderDamagePerBlock;
/* 299 */     this.borderWarningTime = worldInformation.borderWarningTime;
/* 300 */     this.borderWarningDistance = worldInformation.borderWarningDistance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getNBTTagCompound()
/*     */   {
/* 308 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 309 */     updateTagCompound(nbttagcompound, this.playerTag);
/* 310 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound cloneNBTCompound(NBTTagCompound nbt)
/*     */   {
/* 318 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 319 */     updateTagCompound(nbttagcompound, nbt);
/* 320 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */   private void updateTagCompound(NBTTagCompound nbt, NBTTagCompound playerNbt)
/*     */   {
/* 325 */     nbt.setLong("RandomSeed", this.randomSeed);
/* 326 */     nbt.setString("generatorName", this.terrainType.getWorldTypeName());
/* 327 */     nbt.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
/* 328 */     nbt.setString("generatorOptions", this.generatorOptions);
/* 329 */     nbt.setInteger("GameType", this.theGameType.getID());
/* 330 */     nbt.setBoolean("MapFeatures", this.mapFeaturesEnabled);
/* 331 */     nbt.setInteger("SpawnX", this.spawnX);
/* 332 */     nbt.setInteger("SpawnY", this.spawnY);
/* 333 */     nbt.setInteger("SpawnZ", this.spawnZ);
/* 334 */     nbt.setLong("Time", this.totalTime);
/* 335 */     nbt.setLong("DayTime", this.worldTime);
/* 336 */     nbt.setLong("SizeOnDisk", this.sizeOnDisk);
/* 337 */     nbt.setLong("LastPlayed", net.minecraft.server.MinecraftServer.getCurrentTimeMillis());
/* 338 */     nbt.setString("LevelName", this.levelName);
/* 339 */     nbt.setInteger("version", this.saveVersion);
/* 340 */     nbt.setInteger("clearWeatherTime", this.cleanWeatherTime);
/* 341 */     nbt.setInteger("rainTime", this.rainTime);
/* 342 */     nbt.setBoolean("raining", this.raining);
/* 343 */     nbt.setInteger("thunderTime", this.thunderTime);
/* 344 */     nbt.setBoolean("thundering", this.thundering);
/* 345 */     nbt.setBoolean("hardcore", this.hardcore);
/* 346 */     nbt.setBoolean("allowCommands", this.allowCommands);
/* 347 */     nbt.setBoolean("initialized", this.initialized);
/* 348 */     nbt.setDouble("BorderCenterX", this.borderCenterX);
/* 349 */     nbt.setDouble("BorderCenterZ", this.borderCenterZ);
/* 350 */     nbt.setDouble("BorderSize", this.borderSize);
/* 351 */     nbt.setLong("BorderSizeLerpTime", this.borderSizeLerpTime);
/* 352 */     nbt.setDouble("BorderSafeZone", this.borderSafeZone);
/* 353 */     nbt.setDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
/* 354 */     nbt.setDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
/* 355 */     nbt.setDouble("BorderWarningBlocks", this.borderWarningDistance);
/* 356 */     nbt.setDouble("BorderWarningTime", this.borderWarningTime);
/*     */     
/* 358 */     if (this.difficulty != null)
/*     */     {
/* 360 */       nbt.setByte("Difficulty", (byte)this.difficulty.getDifficultyId());
/*     */     }
/*     */     
/* 363 */     nbt.setBoolean("DifficultyLocked", this.difficultyLocked);
/* 364 */     nbt.setTag("GameRules", this.theGameRules.writeToNBT());
/*     */     
/* 366 */     if (playerNbt != null)
/*     */     {
/* 368 */       nbt.setTag("Player", playerNbt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSeed()
/*     */   {
/* 377 */     return this.randomSeed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSpawnX()
/*     */   {
/* 385 */     return this.spawnX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSpawnY()
/*     */   {
/* 393 */     return this.spawnY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSpawnZ()
/*     */   {
/* 401 */     return this.spawnZ;
/*     */   }
/*     */   
/*     */   public long getWorldTotalTime()
/*     */   {
/* 406 */     return this.totalTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getWorldTime()
/*     */   {
/* 414 */     return this.worldTime;
/*     */   }
/*     */   
/*     */   public long getSizeOnDisk()
/*     */   {
/* 419 */     return this.sizeOnDisk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getPlayerNBTTagCompound()
/*     */   {
/* 427 */     return this.playerTag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawnX(int x)
/*     */   {
/* 435 */     this.spawnX = x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawnY(int y)
/*     */   {
/* 443 */     this.spawnY = y;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawnZ(int z)
/*     */   {
/* 451 */     this.spawnZ = z;
/*     */   }
/*     */   
/*     */   public void setWorldTotalTime(long time)
/*     */   {
/* 456 */     this.totalTime = time;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWorldTime(long time)
/*     */   {
/* 464 */     this.worldTime = time;
/*     */   }
/*     */   
/*     */   public void setSpawn(BlockPos spawnPoint)
/*     */   {
/* 469 */     this.spawnX = spawnPoint.getX();
/* 470 */     this.spawnY = spawnPoint.getY();
/* 471 */     this.spawnZ = spawnPoint.getZ();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getWorldName()
/*     */   {
/* 479 */     return this.levelName;
/*     */   }
/*     */   
/*     */   public void setWorldName(String worldName)
/*     */   {
/* 484 */     this.levelName = worldName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSaveVersion()
/*     */   {
/* 492 */     return this.saveVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSaveVersion(int version)
/*     */   {
/* 500 */     this.saveVersion = version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLastTimePlayed()
/*     */   {
/* 508 */     return this.lastTimePlayed;
/*     */   }
/*     */   
/*     */   public int getCleanWeatherTime()
/*     */   {
/* 513 */     return this.cleanWeatherTime;
/*     */   }
/*     */   
/*     */   public void setCleanWeatherTime(int cleanWeatherTimeIn)
/*     */   {
/* 518 */     this.cleanWeatherTime = cleanWeatherTimeIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isThundering()
/*     */   {
/* 526 */     return this.thundering;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThundering(boolean thunderingIn)
/*     */   {
/* 534 */     this.thundering = thunderingIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getThunderTime()
/*     */   {
/* 542 */     return this.thunderTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThunderTime(int time)
/*     */   {
/* 550 */     this.thunderTime = time;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRaining()
/*     */   {
/* 558 */     return this.raining;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRaining(boolean isRaining)
/*     */   {
/* 566 */     this.raining = isRaining;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRainTime()
/*     */   {
/* 574 */     return this.rainTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRainTime(int time)
/*     */   {
/* 582 */     this.rainTime = time;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WorldSettings.GameType getGameType()
/*     */   {
/* 590 */     return this.theGameType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMapFeaturesEnabled()
/*     */   {
/* 598 */     return this.mapFeaturesEnabled;
/*     */   }
/*     */   
/*     */   public void setMapFeaturesEnabled(boolean enabled)
/*     */   {
/* 603 */     this.mapFeaturesEnabled = enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGameType(WorldSettings.GameType type)
/*     */   {
/* 611 */     this.theGameType = type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHardcoreModeEnabled()
/*     */   {
/* 619 */     return this.hardcore;
/*     */   }
/*     */   
/*     */   public void setHardcore(boolean hardcoreIn)
/*     */   {
/* 624 */     this.hardcore = hardcoreIn;
/*     */   }
/*     */   
/*     */   public WorldType getTerrainType()
/*     */   {
/* 629 */     return this.terrainType;
/*     */   }
/*     */   
/*     */   public void setTerrainType(WorldType type)
/*     */   {
/* 634 */     this.terrainType = type;
/*     */   }
/*     */   
/*     */   public String getGeneratorOptions()
/*     */   {
/* 639 */     return this.generatorOptions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean areCommandsAllowed()
/*     */   {
/* 647 */     return this.allowCommands;
/*     */   }
/*     */   
/*     */   public void setAllowCommands(boolean allow)
/*     */   {
/* 652 */     this.allowCommands = allow;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInitialized()
/*     */   {
/* 660 */     return this.initialized;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServerInitialized(boolean initializedIn)
/*     */   {
/* 668 */     this.initialized = initializedIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameRules getGameRulesInstance()
/*     */   {
/* 676 */     return this.theGameRules;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getBorderCenterX()
/*     */   {
/* 684 */     return this.borderCenterX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getBorderCenterZ()
/*     */   {
/* 692 */     return this.borderCenterZ;
/*     */   }
/*     */   
/*     */   public double getBorderSize()
/*     */   {
/* 697 */     return this.borderSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderSize(double size)
/*     */   {
/* 705 */     this.borderSize = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getBorderLerpTime()
/*     */   {
/* 713 */     return this.borderSizeLerpTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderLerpTime(long time)
/*     */   {
/* 721 */     this.borderSizeLerpTime = time;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getBorderLerpTarget()
/*     */   {
/* 729 */     return this.borderSizeLerpTarget;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderLerpTarget(double lerpSize)
/*     */   {
/* 737 */     this.borderSizeLerpTarget = lerpSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getBorderCenterZ(double posZ)
/*     */   {
/* 745 */     this.borderCenterZ = posZ;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getBorderCenterX(double posX)
/*     */   {
/* 753 */     this.borderCenterX = posX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getBorderSafeZone()
/*     */   {
/* 761 */     return this.borderSafeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderSafeZone(double amount)
/*     */   {
/* 769 */     this.borderSafeZone = amount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getBorderDamagePerBlock()
/*     */   {
/* 777 */     return this.borderDamagePerBlock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderDamagePerBlock(double damage)
/*     */   {
/* 785 */     this.borderDamagePerBlock = damage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBorderWarningDistance()
/*     */   {
/* 793 */     return this.borderWarningDistance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBorderWarningTime()
/*     */   {
/* 801 */     return this.borderWarningTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderWarningDistance(int amountOfBlocks)
/*     */   {
/* 809 */     this.borderWarningDistance = amountOfBlocks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderWarningTime(int ticks)
/*     */   {
/* 817 */     this.borderWarningTime = ticks;
/*     */   }
/*     */   
/*     */   public EnumDifficulty getDifficulty()
/*     */   {
/* 822 */     return this.difficulty;
/*     */   }
/*     */   
/*     */   public void setDifficulty(EnumDifficulty newDifficulty)
/*     */   {
/* 827 */     this.difficulty = newDifficulty;
/*     */   }
/*     */   
/*     */   public boolean isDifficultyLocked()
/*     */   {
/* 832 */     return this.difficultyLocked;
/*     */   }
/*     */   
/*     */   public void setDifficultyLocked(boolean locked)
/*     */   {
/* 837 */     this.difficultyLocked = locked;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addToCrashReport(CrashReportCategory category)
/*     */   {
/* 845 */     category.addCrashSectionCallable("Level seed", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 849 */         return String.valueOf(WorldInfo.this.getSeed());
/*     */       }
/* 851 */     });
/* 852 */     category.addCrashSectionCallable("Level generator", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 856 */         return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[] { Integer.valueOf(WorldInfo.this.terrainType.getWorldTypeID()), WorldInfo.this.terrainType.getWorldTypeName(), Integer.valueOf(WorldInfo.this.terrainType.getGeneratorVersion()), Boolean.valueOf(WorldInfo.this.mapFeaturesEnabled) });
/*     */       }
/* 858 */     });
/* 859 */     category.addCrashSectionCallable("Level generator options", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 863 */         return WorldInfo.this.generatorOptions;
/*     */       }
/* 865 */     });
/* 866 */     category.addCrashSectionCallable("Level spawn location", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 870 */         return CrashReportCategory.getCoordinateInfo(WorldInfo.this.spawnX, WorldInfo.this.spawnY, WorldInfo.this.spawnZ);
/*     */       }
/* 872 */     });
/* 873 */     category.addCrashSectionCallable("Level time", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 877 */         return String.format("%d game time, %d day time", new Object[] { Long.valueOf(WorldInfo.this.totalTime), Long.valueOf(WorldInfo.this.worldTime) });
/*     */       }
/* 879 */     });
/* 880 */     category.addCrashSectionCallable("Level dimension", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 884 */         return String.valueOf(WorldInfo.this.dimension);
/*     */       }
/* 886 */     });
/* 887 */     category.addCrashSectionCallable("Level storage version", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 891 */         String s = "Unknown?";
/*     */         
/*     */         try
/*     */         {
/* 895 */           switch (WorldInfo.this.saveVersion)
/*     */           {
/*     */           case 19132: 
/* 898 */             s = "McRegion";
/* 899 */             break;
/*     */           
/*     */           case 19133: 
/* 902 */             s = "Anvil";
/*     */           }
/*     */           
/*     */         }
/*     */         catch (Throwable localThrowable) {}
/*     */         
/*     */ 
/*     */ 
/* 910 */         return String.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.this.saveVersion), s });
/*     */       }
/* 912 */     });
/* 913 */     category.addCrashSectionCallable("Level weather", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 917 */         return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { Integer.valueOf(WorldInfo.this.rainTime), Boolean.valueOf(WorldInfo.this.raining), Integer.valueOf(WorldInfo.this.thunderTime), Boolean.valueOf(WorldInfo.this.thundering) });
/*     */       }
/* 919 */     });
/* 920 */     category.addCrashSectionCallable("Level game mode", new Callable()
/*     */     {
/*     */       public String call() throws Exception
/*     */       {
/* 924 */         return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] { WorldInfo.this.theGameType.getName(), Integer.valueOf(WorldInfo.this.theGameType.getID()), Boolean.valueOf(WorldInfo.this.hardcore), Boolean.valueOf(WorldInfo.this.allowCommands) });
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\WorldInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */