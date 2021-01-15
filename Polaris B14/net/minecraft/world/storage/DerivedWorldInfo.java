/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ import net.minecraft.world.WorldType;
/*     */ 
/*     */ public class DerivedWorldInfo
/*     */   extends WorldInfo
/*     */ {
/*     */   private final WorldInfo theWorldInfo;
/*     */   
/*     */   public DerivedWorldInfo(WorldInfo p_i2145_1_)
/*     */   {
/*  17 */     this.theWorldInfo = p_i2145_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getNBTTagCompound()
/*     */   {
/*  25 */     return this.theWorldInfo.getNBTTagCompound();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound cloneNBTCompound(NBTTagCompound nbt)
/*     */   {
/*  33 */     return this.theWorldInfo.cloneNBTCompound(nbt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSeed()
/*     */   {
/*  41 */     return this.theWorldInfo.getSeed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSpawnX()
/*     */   {
/*  49 */     return this.theWorldInfo.getSpawnX();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSpawnY()
/*     */   {
/*  57 */     return this.theWorldInfo.getSpawnY();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSpawnZ()
/*     */   {
/*  65 */     return this.theWorldInfo.getSpawnZ();
/*     */   }
/*     */   
/*     */   public long getWorldTotalTime()
/*     */   {
/*  70 */     return this.theWorldInfo.getWorldTotalTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getWorldTime()
/*     */   {
/*  78 */     return this.theWorldInfo.getWorldTime();
/*     */   }
/*     */   
/*     */   public long getSizeOnDisk()
/*     */   {
/*  83 */     return this.theWorldInfo.getSizeOnDisk();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getPlayerNBTTagCompound()
/*     */   {
/*  91 */     return this.theWorldInfo.getPlayerNBTTagCompound();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getWorldName()
/*     */   {
/*  99 */     return this.theWorldInfo.getWorldName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSaveVersion()
/*     */   {
/* 107 */     return this.theWorldInfo.getSaveVersion();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLastTimePlayed()
/*     */   {
/* 115 */     return this.theWorldInfo.getLastTimePlayed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isThundering()
/*     */   {
/* 123 */     return this.theWorldInfo.isThundering();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getThunderTime()
/*     */   {
/* 131 */     return this.theWorldInfo.getThunderTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRaining()
/*     */   {
/* 139 */     return this.theWorldInfo.isRaining();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRainTime()
/*     */   {
/* 147 */     return this.theWorldInfo.getRainTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WorldSettings.GameType getGameType()
/*     */   {
/* 155 */     return this.theWorldInfo.getGameType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawnX(int x) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawnY(int y) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawnZ(int z) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWorldTotalTime(long time) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWorldTime(long time) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpawn(BlockPos spawnPoint) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWorldName(String worldName) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSaveVersion(int version) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThundering(boolean thunderingIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThunderTime(int time) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRaining(boolean isRaining) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRainTime(int time) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMapFeaturesEnabled()
/*     */   {
/* 238 */     return this.theWorldInfo.isMapFeaturesEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHardcoreModeEnabled()
/*     */   {
/* 246 */     return this.theWorldInfo.isHardcoreModeEnabled();
/*     */   }
/*     */   
/*     */   public WorldType getTerrainType()
/*     */   {
/* 251 */     return this.theWorldInfo.getTerrainType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTerrainType(WorldType type) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean areCommandsAllowed()
/*     */   {
/* 263 */     return this.theWorldInfo.areCommandsAllowed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowCommands(boolean allow) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isInitialized()
/*     */   {
/* 275 */     return this.theWorldInfo.isInitialized();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServerInitialized(boolean initializedIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameRules getGameRulesInstance()
/*     */   {
/* 290 */     return this.theWorldInfo.getGameRulesInstance();
/*     */   }
/*     */   
/*     */   public EnumDifficulty getDifficulty()
/*     */   {
/* 295 */     return this.theWorldInfo.getDifficulty();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setDifficulty(EnumDifficulty newDifficulty) {}
/*     */   
/*     */ 
/*     */   public boolean isDifficultyLocked()
/*     */   {
/* 304 */     return this.theWorldInfo.isDifficultyLocked();
/*     */   }
/*     */   
/*     */   public void setDifficultyLocked(boolean locked) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\DerivedWorldInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */