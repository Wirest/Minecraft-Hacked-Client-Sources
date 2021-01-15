/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
/*     */ 
/*     */ 
/*     */ public class ChunkCache
/*     */   implements IBlockAccess
/*     */ {
/*     */   protected int chunkX;
/*     */   protected int chunkZ;
/*     */   protected Chunk[][] chunkArray;
/*     */   protected boolean hasExtendedLevels;
/*     */   protected World worldObj;
/*     */   
/*     */   public ChunkCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn)
/*     */   {
/*  26 */     this.worldObj = worldIn;
/*  27 */     this.chunkX = (posFromIn.getX() - subIn >> 4);
/*  28 */     this.chunkZ = (posFromIn.getZ() - subIn >> 4);
/*  29 */     int i = posToIn.getX() + subIn >> 4;
/*  30 */     int j = posToIn.getZ() + subIn >> 4;
/*  31 */     this.chunkArray = new Chunk[i - this.chunkX + 1][j - this.chunkZ + 1];
/*  32 */     this.hasExtendedLevels = true;
/*     */     
/*  34 */     for (int k = this.chunkX; k <= i; k++)
/*     */     {
/*  36 */       for (int l = this.chunkZ; l <= j; l++)
/*     */       {
/*  38 */         this.chunkArray[(k - this.chunkX)][(l - this.chunkZ)] = worldIn.getChunkFromChunkCoords(k, l);
/*     */       }
/*     */     }
/*     */     
/*  42 */     for (int i1 = posFromIn.getX() >> 4; i1 <= posToIn.getX() >> 4; i1++)
/*     */     {
/*  44 */       for (int j1 = posFromIn.getZ() >> 4; j1 <= posToIn.getZ() >> 4; j1++)
/*     */       {
/*  46 */         Chunk chunk = this.chunkArray[(i1 - this.chunkX)][(j1 - this.chunkZ)];
/*     */         
/*  48 */         if ((chunk != null) && (!chunk.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY())))
/*     */         {
/*  50 */           this.hasExtendedLevels = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean extendedLevelsInChunkCache()
/*     */   {
/*  61 */     return this.hasExtendedLevels;
/*     */   }
/*     */   
/*     */   public TileEntity getTileEntity(BlockPos pos)
/*     */   {
/*  66 */     int i = (pos.getX() >> 4) - this.chunkX;
/*  67 */     int j = (pos.getZ() >> 4) - this.chunkZ;
/*  68 */     return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
/*     */   }
/*     */   
/*     */   public int getCombinedLight(BlockPos pos, int lightValue)
/*     */   {
/*  73 */     int i = getLightForExt(EnumSkyBlock.SKY, pos);
/*  74 */     int j = getLightForExt(EnumSkyBlock.BLOCK, pos);
/*     */     
/*  76 */     if (j < lightValue)
/*     */     {
/*  78 */       j = lightValue;
/*     */     }
/*     */     
/*  81 */     return i << 20 | j << 4;
/*     */   }
/*     */   
/*     */   public IBlockState getBlockState(BlockPos pos)
/*     */   {
/*  86 */     if ((pos.getY() >= 0) && (pos.getY() < 256))
/*     */     {
/*  88 */       int i = (pos.getX() >> 4) - this.chunkX;
/*  89 */       int j = (pos.getZ() >> 4) - this.chunkZ;
/*     */       
/*  91 */       if ((i >= 0) && (i < this.chunkArray.length) && (j >= 0) && (j < this.chunkArray[i].length))
/*     */       {
/*  93 */         Chunk chunk = this.chunkArray[i][j];
/*     */         
/*  95 */         if (chunk != null)
/*     */         {
/*  97 */           return chunk.getBlockState(pos);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 102 */     return Blocks.air.getDefaultState();
/*     */   }
/*     */   
/*     */   public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
/*     */   {
/* 107 */     return this.worldObj.getBiomeGenForCoords(pos);
/*     */   }
/*     */   
/*     */   private int getLightForExt(EnumSkyBlock p_175629_1_, BlockPos pos)
/*     */   {
/* 112 */     if ((p_175629_1_ == EnumSkyBlock.SKY) && (this.worldObj.provider.getHasNoSky()))
/*     */     {
/* 114 */       return 0;
/*     */     }
/* 116 */     if ((pos.getY() >= 0) && (pos.getY() < 256))
/*     */     {
/* 118 */       if (getBlockState(pos).getBlock().getUseNeighborBrightness())
/*     */       {
/* 120 */         int l = 0;
/*     */         EnumFacing[] arrayOfEnumFacing;
/* 122 */         int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */           
/* 124 */           int k = getLightFor(p_175629_1_, pos.offset(enumfacing));
/*     */           
/* 126 */           if (k > l)
/*     */           {
/* 128 */             l = k;
/*     */           }
/*     */           
/* 131 */           if (l >= 15)
/*     */           {
/* 133 */             return l;
/*     */           }
/*     */         }
/*     */         
/* 137 */         return l;
/*     */       }
/*     */       
/*     */ 
/* 141 */       int i = (pos.getX() >> 4) - this.chunkX;
/* 142 */       int j = (pos.getZ() >> 4) - this.chunkZ;
/* 143 */       return this.chunkArray[i][j].getLightFor(p_175629_1_, pos);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 148 */     return p_175629_1_.defaultLightValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAirBlock(BlockPos pos)
/*     */   {
/* 158 */     return getBlockState(pos).getBlock().getMaterial() == Material.air;
/*     */   }
/*     */   
/*     */   public int getLightFor(EnumSkyBlock p_175628_1_, BlockPos pos)
/*     */   {
/* 163 */     if ((pos.getY() >= 0) && (pos.getY() < 256))
/*     */     {
/* 165 */       int i = (pos.getX() >> 4) - this.chunkX;
/* 166 */       int j = (pos.getZ() >> 4) - this.chunkZ;
/* 167 */       return this.chunkArray[i][j].getLightFor(p_175628_1_, pos);
/*     */     }
/*     */     
/*     */ 
/* 171 */     return p_175628_1_.defaultLightValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getStrongPower(BlockPos pos, EnumFacing direction)
/*     */   {
/* 177 */     IBlockState iblockstate = getBlockState(pos);
/* 178 */     return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
/*     */   }
/*     */   
/*     */   public WorldType getWorldType()
/*     */   {
/* 183 */     return this.worldObj.getWorldType();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\ChunkCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */