/*     */ package net.minecraft.world.pathfinder;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockRailBase;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WalkNodeProcessor extends NodeProcessor
/*     */ {
/*     */   private boolean canEnterDoors;
/*     */   private boolean canBreakDoors;
/*     */   private boolean avoidsWater;
/*     */   private boolean canSwim;
/*     */   private boolean shouldAvoidWater;
/*     */   
/*     */   public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn)
/*     */   {
/*  27 */     super.initProcessor(iblockaccessIn, entityIn);
/*  28 */     this.shouldAvoidWater = this.avoidsWater;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void postProcess()
/*     */   {
/*  38 */     super.postProcess();
/*  39 */     this.avoidsWater = this.shouldAvoidWater;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PathPoint getPathPointTo(Entity entityIn)
/*     */   {
/*     */     int i;
/*     */     
/*     */ 
/*  49 */     if ((this.canSwim) && (entityIn.isInWater()))
/*     */     {
/*  51 */       int i = (int)entityIn.getEntityBoundingBox().minY;
/*  52 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
/*     */       
/*  54 */       for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); (block == Blocks.flowing_water) || (block == Blocks.water); block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock())
/*     */       {
/*  56 */         i++;
/*  57 */         blockpos$mutableblockpos.func_181079_c(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
/*     */       }
/*     */       
/*  60 */       this.avoidsWater = false;
/*     */     }
/*     */     else
/*     */     {
/*  64 */       i = MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5D);
/*     */     }
/*     */     
/*  67 */     return openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), i, MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target)
/*     */   {
/*  75 */     return openPoint(MathHelper.floor_double(x - entityIn.width / 2.0F), MathHelper.floor_double(y), MathHelper.floor_double(target - entityIn.width / 2.0F));
/*     */   }
/*     */   
/*     */   public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
/*     */   {
/*  80 */     int i = 0;
/*  81 */     int j = 0;
/*     */     
/*  83 */     if (getVerticalOffset(entityIn, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord) == 1)
/*     */     {
/*  85 */       j = 1;
/*     */     }
/*     */     
/*  88 */     PathPoint pathpoint = getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j);
/*  89 */     PathPoint pathpoint1 = getSafePoint(entityIn, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j);
/*  90 */     PathPoint pathpoint2 = getSafePoint(entityIn, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j);
/*  91 */     PathPoint pathpoint3 = getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j);
/*     */     
/*  93 */     if ((pathpoint != null) && (!pathpoint.visited) && (pathpoint.distanceTo(targetPoint) < maxDistance))
/*     */     {
/*  95 */       pathOptions[(i++)] = pathpoint;
/*     */     }
/*     */     
/*  98 */     if ((pathpoint1 != null) && (!pathpoint1.visited) && (pathpoint1.distanceTo(targetPoint) < maxDistance))
/*     */     {
/* 100 */       pathOptions[(i++)] = pathpoint1;
/*     */     }
/*     */     
/* 103 */     if ((pathpoint2 != null) && (!pathpoint2.visited) && (pathpoint2.distanceTo(targetPoint) < maxDistance))
/*     */     {
/* 105 */       pathOptions[(i++)] = pathpoint2;
/*     */     }
/*     */     
/* 108 */     if ((pathpoint3 != null) && (!pathpoint3.visited) && (pathpoint3.distanceTo(targetPoint) < maxDistance))
/*     */     {
/* 110 */       pathOptions[(i++)] = pathpoint3;
/*     */     }
/*     */     
/* 113 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private PathPoint getSafePoint(Entity entityIn, int x, int y, int z, int p_176171_5_)
/*     */   {
/* 121 */     PathPoint pathpoint = null;
/* 122 */     int i = getVerticalOffset(entityIn, x, y, z);
/*     */     
/* 124 */     if (i == 2)
/*     */     {
/* 126 */       return openPoint(x, y, z);
/*     */     }
/*     */     
/*     */ 
/* 130 */     if (i == 1)
/*     */     {
/* 132 */       pathpoint = openPoint(x, y, z);
/*     */     }
/*     */     
/* 135 */     if ((pathpoint == null) && (p_176171_5_ > 0) && (i != -3) && (i != -4) && (getVerticalOffset(entityIn, x, y + p_176171_5_, z) == 1))
/*     */     {
/* 137 */       pathpoint = openPoint(x, y + p_176171_5_, z);
/* 138 */       y += p_176171_5_;
/*     */     }
/*     */     
/* 141 */     if (pathpoint != null)
/*     */     {
/* 143 */       int j = 0;
/*     */       
/*     */ 
/* 146 */       for (int k = 0; y > 0; pathpoint = openPoint(x, y, z))
/*     */       {
/* 148 */         k = getVerticalOffset(entityIn, x, y - 1, z);
/*     */         
/* 150 */         if ((this.avoidsWater) && (k == -1))
/*     */         {
/* 152 */           return null;
/*     */         }
/*     */         
/* 155 */         if (k != 1) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 160 */         if (j++ >= entityIn.getMaxFallHeight())
/*     */         {
/* 162 */           return null;
/*     */         }
/*     */         
/* 165 */         y--;
/*     */         
/* 167 */         if (y <= 0)
/*     */         {
/* 169 */           return null;
/*     */         }
/*     */       }
/*     */       
/* 173 */       if (k == -2)
/*     */       {
/* 175 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 179 */     return pathpoint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getVerticalOffset(Entity entityIn, int x, int y, int z)
/*     */   {
/* 191 */     return func_176170_a(this.blockaccess, entityIn, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
/*     */   }
/*     */   
/*     */   public static int func_176170_a(IBlockAccess blockaccessIn, Entity entityIn, int x, int y, int z, int sizeX, int sizeY, int sizeZ, boolean avoidWater, boolean breakDoors, boolean enterDoors)
/*     */   {
/* 196 */     boolean flag = false;
/* 197 */     BlockPos blockpos = new BlockPos(entityIn);
/* 198 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 200 */     for (int i = x; i < x + sizeX; i++)
/*     */     {
/* 202 */       for (int j = y; j < y + sizeY; j++)
/*     */       {
/* 204 */         for (int k = z; k < z + sizeZ; k++)
/*     */         {
/* 206 */           blockpos$mutableblockpos.func_181079_c(i, j, k);
/* 207 */           Block block = blockaccessIn.getBlockState(blockpos$mutableblockpos).getBlock();
/*     */           
/* 209 */           if (block.getMaterial() != Material.air)
/*     */           {
/* 211 */             if ((block != Blocks.trapdoor) && (block != Blocks.iron_trapdoor))
/*     */             {
/* 213 */               if ((block != Blocks.flowing_water) && (block != Blocks.water))
/*     */               {
/* 215 */                 if ((!enterDoors) && ((block instanceof net.minecraft.block.BlockDoor)) && (block.getMaterial() == Material.wood))
/*     */                 {
/* 217 */                   return 0;
/*     */                 }
/*     */               }
/*     */               else
/*     */               {
/* 222 */                 if (avoidWater)
/*     */                 {
/* 224 */                   return -1;
/*     */                 }
/*     */                 
/* 227 */                 flag = true;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/* 232 */               flag = true;
/*     */             }
/*     */             
/* 235 */             if ((entityIn.worldObj.getBlockState(blockpos$mutableblockpos).getBlock() instanceof BlockRailBase))
/*     */             {
/* 237 */               if ((!(entityIn.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase)) && (!(entityIn.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase)))
/*     */               {
/* 239 */                 return -3;
/*     */               }
/*     */             }
/* 242 */             else if ((!block.isPassable(blockaccessIn, blockpos$mutableblockpos)) && ((!breakDoors) || (!(block instanceof net.minecraft.block.BlockDoor)) || (block.getMaterial() != Material.wood)))
/*     */             {
/* 244 */               if (((block instanceof net.minecraft.block.BlockFence)) || ((block instanceof net.minecraft.block.BlockFenceGate)) || ((block instanceof net.minecraft.block.BlockWall)))
/*     */               {
/* 246 */                 return -3;
/*     */               }
/*     */               
/* 249 */               if ((block == Blocks.trapdoor) || (block == Blocks.iron_trapdoor))
/*     */               {
/* 251 */                 return -4;
/*     */               }
/*     */               
/* 254 */               Material material = block.getMaterial();
/*     */               
/* 256 */               if (material != Material.lava)
/*     */               {
/* 258 */                 return 0;
/*     */               }
/*     */               
/* 261 */               if (!entityIn.isInLava())
/*     */               {
/* 263 */                 return -2;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 271 */     return flag ? 2 : 1;
/*     */   }
/*     */   
/*     */   public void setEnterDoors(boolean canEnterDoorsIn)
/*     */   {
/* 276 */     this.canEnterDoors = canEnterDoorsIn;
/*     */   }
/*     */   
/*     */   public void setBreakDoors(boolean canBreakDoorsIn)
/*     */   {
/* 281 */     this.canBreakDoors = canBreakDoorsIn;
/*     */   }
/*     */   
/*     */   public void setAvoidsWater(boolean avoidsWaterIn)
/*     */   {
/* 286 */     this.avoidsWater = avoidsWaterIn;
/*     */   }
/*     */   
/*     */   public void setCanSwim(boolean canSwimIn)
/*     */   {
/* 291 */     this.canSwim = canSwimIn;
/*     */   }
/*     */   
/*     */   public boolean getEnterDoors()
/*     */   {
/* 296 */     return this.canEnterDoors;
/*     */   }
/*     */   
/*     */   public boolean getCanSwim()
/*     */   {
/* 301 */     return this.canSwim;
/*     */   }
/*     */   
/*     */   public boolean getAvoidsWater()
/*     */   {
/* 306 */     return this.avoidsWater;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\pathfinder\WalkNodeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */