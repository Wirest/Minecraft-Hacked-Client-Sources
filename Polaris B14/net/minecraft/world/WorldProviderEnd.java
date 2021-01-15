/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.ChunkProviderEnd;
/*     */ 
/*     */ public class WorldProviderEnd
/*     */   extends WorldProvider
/*     */ {
/*     */   public void registerWorldChunkManager()
/*     */   {
/*  18 */     this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
/*  19 */     this.dimensionId = 1;
/*  20 */     this.hasNoSky = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChunkProvider createChunkGenerator()
/*     */   {
/*  28 */     return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_)
/*     */   {
/*  36 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
/*     */   {
/*  44 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
/*     */   {
/*  52 */     int i = 10518688;
/*  53 */     float f = MathHelper.cos(p_76562_1_ * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/*  54 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/*  55 */     float f1 = (i >> 16 & 0xFF) / 255.0F;
/*  56 */     float f2 = (i >> 8 & 0xFF) / 255.0F;
/*  57 */     float f3 = (i & 0xFF) / 255.0F;
/*  58 */     f1 *= (f * 0.0F + 0.15F);
/*  59 */     f2 *= (f * 0.0F + 0.15F);
/*  60 */     f3 *= (f * 0.0F + 0.15F);
/*  61 */     return new Vec3(f1, f2, f3);
/*     */   }
/*     */   
/*     */   public boolean isSkyColored()
/*     */   {
/*  66 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canRespawnHere()
/*     */   {
/*  74 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSurfaceWorld()
/*     */   {
/*  82 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getCloudHeight()
/*     */   {
/*  90 */     return 8.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCoordinateBeSpawn(int x, int z)
/*     */   {
/*  98 */     return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
/*     */   }
/*     */   
/*     */   public BlockPos getSpawnCoordinate()
/*     */   {
/* 103 */     return new BlockPos(100, 50, 0);
/*     */   }
/*     */   
/*     */   public int getAverageGroundLevel()
/*     */   {
/* 108 */     return 50;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesXZShowFog(int x, int z)
/*     */   {
/* 116 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDimensionName()
/*     */   {
/* 124 */     return "The End";
/*     */   }
/*     */   
/*     */   public String getInternalNameSuffix()
/*     */   {
/* 129 */     return "_end";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldProviderEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */