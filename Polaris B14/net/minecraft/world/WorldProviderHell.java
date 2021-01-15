/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.ChunkProviderHell;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ 
/*     */ public class WorldProviderHell
/*     */   extends WorldProvider
/*     */ {
/*     */   public void registerWorldChunkManager()
/*     */   {
/*  17 */     this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
/*  18 */     this.isHellWorld = true;
/*  19 */     this.hasNoSky = true;
/*  20 */     this.dimensionId = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
/*     */   {
/*  28 */     return new Vec3(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void generateLightBrightnessTable()
/*     */   {
/*  36 */     float f = 0.1F;
/*     */     
/*  38 */     for (int i = 0; i <= 15; i++)
/*     */     {
/*  40 */       float f1 = 1.0F - i / 15.0F;
/*  41 */       this.lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChunkProvider createChunkGenerator()
/*     */   {
/*  50 */     return new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSurfaceWorld()
/*     */   {
/*  58 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCoordinateBeSpawn(int x, int z)
/*     */   {
/*  66 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_)
/*     */   {
/*  74 */     return 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canRespawnHere()
/*     */   {
/*  82 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesXZShowFog(int x, int z)
/*     */   {
/*  90 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDimensionName()
/*     */   {
/*  98 */     return "Nether";
/*     */   }
/*     */   
/*     */   public String getInternalNameSuffix()
/*     */   {
/* 103 */     return "_nether";
/*     */   }
/*     */   
/*     */   public WorldBorder getWorldBorder()
/*     */   {
/* 108 */     new WorldBorder()
/*     */     {
/*     */       public double getCenterX()
/*     */       {
/* 112 */         return super.getCenterX() / 8.0D;
/*     */       }
/*     */       
/*     */       public double getCenterZ() {
/* 116 */         return super.getCenterZ() / 8.0D;
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldProviderHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */