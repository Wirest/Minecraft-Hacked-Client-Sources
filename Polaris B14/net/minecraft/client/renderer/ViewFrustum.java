/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
/*     */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ViewFrustum
/*     */ {
/*     */   protected final RenderGlobal renderGlobal;
/*     */   protected final World world;
/*     */   protected int countChunksY;
/*     */   protected int countChunksX;
/*     */   protected int countChunksZ;
/*     */   public RenderChunk[] renderChunks;
/*     */   
/*     */   public ViewFrustum(World worldIn, int renderDistanceChunks, RenderGlobal p_i46246_3_, IRenderChunkFactory renderChunkFactory)
/*     */   {
/*  20 */     this.renderGlobal = p_i46246_3_;
/*  21 */     this.world = worldIn;
/*  22 */     setCountChunksXYZ(renderDistanceChunks);
/*  23 */     createRenderChunks(renderChunkFactory);
/*     */   }
/*     */   
/*     */   protected void createRenderChunks(IRenderChunkFactory renderChunkFactory)
/*     */   {
/*  28 */     int i = this.countChunksX * this.countChunksY * this.countChunksZ;
/*  29 */     this.renderChunks = new RenderChunk[i];
/*  30 */     int j = 0;
/*     */     
/*  32 */     for (int k = 0; k < this.countChunksX; k++)
/*     */     {
/*  34 */       for (int l = 0; l < this.countChunksY; l++)
/*     */       {
/*  36 */         for (int i1 = 0; i1 < this.countChunksZ; i1++)
/*     */         {
/*  38 */           int j1 = (i1 * this.countChunksY + l) * this.countChunksX + k;
/*  39 */           BlockPos blockpos = new BlockPos(k * 16, l * 16, i1 * 16);
/*  40 */           this.renderChunks[j1] = renderChunkFactory.makeRenderChunk(this.world, this.renderGlobal, blockpos, j++);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteGlResources() {
/*     */     RenderChunk[] arrayOfRenderChunk;
/*  48 */     int j = (arrayOfRenderChunk = this.renderChunks).length; for (int i = 0; i < j; i++) { RenderChunk renderchunk = arrayOfRenderChunk[i];
/*     */       
/*  50 */       renderchunk.deleteGlResources();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setCountChunksXYZ(int renderDistanceChunks)
/*     */   {
/*  56 */     int i = renderDistanceChunks * 2 + 1;
/*  57 */     this.countChunksX = i;
/*  58 */     this.countChunksY = 16;
/*  59 */     this.countChunksZ = i;
/*     */   }
/*     */   
/*     */   public void updateChunkPositions(double viewEntityX, double viewEntityZ)
/*     */   {
/*  64 */     int i = MathHelper.floor_double(viewEntityX) - 8;
/*  65 */     int j = MathHelper.floor_double(viewEntityZ) - 8;
/*  66 */     int k = this.countChunksX * 16;
/*     */     
/*  68 */     for (int l = 0; l < this.countChunksX; l++)
/*     */     {
/*  70 */       int i1 = func_178157_a(i, k, l);
/*     */       
/*  72 */       for (int j1 = 0; j1 < this.countChunksZ; j1++)
/*     */       {
/*  74 */         int k1 = func_178157_a(j, k, j1);
/*     */         
/*  76 */         for (int l1 = 0; l1 < this.countChunksY; l1++)
/*     */         {
/*  78 */           int i2 = l1 * 16;
/*  79 */           RenderChunk renderchunk = this.renderChunks[((j1 * this.countChunksY + l1) * this.countChunksX + l)];
/*  80 */           BlockPos blockpos = new BlockPos(i1, i2, k1);
/*     */           
/*  82 */           if (!blockpos.equals(renderchunk.getPosition()))
/*     */           {
/*  84 */             renderchunk.setPosition(blockpos);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int func_178157_a(int p_178157_1_, int p_178157_2_, int p_178157_3_)
/*     */   {
/*  93 */     int i = p_178157_3_ * 16;
/*  94 */     int j = i - p_178157_1_ + p_178157_2_ / 2;
/*     */     
/*  96 */     if (j < 0)
/*     */     {
/*  98 */       j -= p_178157_2_ - 1;
/*     */     }
/*     */     
/* 101 */     return i - j / p_178157_2_ * p_178157_2_;
/*     */   }
/*     */   
/*     */   public void markBlocksForUpdate(int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
/*     */   {
/* 106 */     int i = MathHelper.bucketInt(fromX, 16);
/* 107 */     int j = MathHelper.bucketInt(fromY, 16);
/* 108 */     int k = MathHelper.bucketInt(fromZ, 16);
/* 109 */     int l = MathHelper.bucketInt(toX, 16);
/* 110 */     int i1 = MathHelper.bucketInt(toY, 16);
/* 111 */     int j1 = MathHelper.bucketInt(toZ, 16);
/*     */     
/* 113 */     for (int k1 = i; k1 <= l; k1++)
/*     */     {
/* 115 */       int l1 = k1 % this.countChunksX;
/*     */       
/* 117 */       if (l1 < 0)
/*     */       {
/* 119 */         l1 += this.countChunksX;
/*     */       }
/*     */       
/* 122 */       for (int i2 = j; i2 <= i1; i2++)
/*     */       {
/* 124 */         int j2 = i2 % this.countChunksY;
/*     */         
/* 126 */         if (j2 < 0)
/*     */         {
/* 128 */           j2 += this.countChunksY;
/*     */         }
/*     */         
/* 131 */         for (int k2 = k; k2 <= j1; k2++)
/*     */         {
/* 133 */           int l2 = k2 % this.countChunksZ;
/*     */           
/* 135 */           if (l2 < 0)
/*     */           {
/* 137 */             l2 += this.countChunksZ;
/*     */           }
/*     */           
/* 140 */           int i3 = (l2 * this.countChunksY + j2) * this.countChunksX + l1;
/* 141 */           RenderChunk renderchunk = this.renderChunks[i3];
/* 142 */           renderchunk.setNeedsUpdate(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected RenderChunk getRenderChunk(BlockPos pos)
/*     */   {
/* 150 */     int i = MathHelper.bucketInt(pos.getX(), 16);
/* 151 */     int j = MathHelper.bucketInt(pos.getY(), 16);
/* 152 */     int k = MathHelper.bucketInt(pos.getZ(), 16);
/*     */     
/* 154 */     if ((j >= 0) && (j < this.countChunksY))
/*     */     {
/* 156 */       i %= this.countChunksX;
/*     */       
/* 158 */       if (i < 0)
/*     */       {
/* 160 */         i += this.countChunksX;
/*     */       }
/*     */       
/* 163 */       k %= this.countChunksZ;
/*     */       
/* 165 */       if (k < 0)
/*     */       {
/* 167 */         k += this.countChunksZ;
/*     */       }
/*     */       
/* 170 */       int l = (k * this.countChunksY + j) * this.countChunksX + i;
/* 171 */       return this.renderChunks[l];
/*     */     }
/*     */     
/*     */ 
/* 175 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\ViewFrustum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */