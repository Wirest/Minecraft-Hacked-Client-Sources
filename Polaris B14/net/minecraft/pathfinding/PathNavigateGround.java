/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.pathfinder.WalkNodeProcessor;
/*     */ 
/*     */ public class PathNavigateGround extends PathNavigate
/*     */ {
/*     */   protected WalkNodeProcessor nodeProcessor;
/*     */   private boolean shouldAvoidSun;
/*     */   
/*     */   public PathNavigateGround(EntityLiving entitylivingIn, World worldIn)
/*     */   {
/*  22 */     super(entitylivingIn, worldIn);
/*     */   }
/*     */   
/*     */   protected PathFinder getPathFinder()
/*     */   {
/*  27 */     this.nodeProcessor = new WalkNodeProcessor();
/*  28 */     this.nodeProcessor.setEnterDoors(true);
/*  29 */     return new PathFinder(this.nodeProcessor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canNavigate()
/*     */   {
/*  37 */     return (this.theEntity.onGround) || ((getCanSwim()) && (isInLiquid())) || ((this.theEntity.isRiding()) && ((this.theEntity instanceof net.minecraft.entity.monster.EntityZombie)) && ((this.theEntity.ridingEntity instanceof net.minecraft.entity.passive.EntityChicken)));
/*     */   }
/*     */   
/*     */   protected Vec3 getEntityPosition()
/*     */   {
/*  42 */     return new Vec3(this.theEntity.posX, getPathablePosY(), this.theEntity.posZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getPathablePosY()
/*     */   {
/*  50 */     if ((this.theEntity.isInWater()) && (getCanSwim()))
/*     */     {
/*  52 */       int i = (int)this.theEntity.getEntityBoundingBox().minY;
/*  53 */       Block block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
/*  54 */       int j = 0;
/*     */       
/*  56 */       while ((block == Blocks.flowing_water) || (block == Blocks.water))
/*     */       {
/*  58 */         i++;
/*  59 */         block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
/*  60 */         j++;
/*     */         
/*  62 */         if (j > 16)
/*     */         {
/*  64 */           return (int)this.theEntity.getEntityBoundingBox().minY;
/*     */         }
/*     */       }
/*     */       
/*  68 */       return i;
/*     */     }
/*     */     
/*     */ 
/*  72 */     return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeSunnyPath()
/*     */   {
/*  81 */     super.removeSunnyPath();
/*     */     
/*  83 */     if (this.shouldAvoidSun)
/*     */     {
/*  85 */       if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ))))
/*     */       {
/*  87 */         return;
/*     */       }
/*     */       
/*  90 */       for (int i = 0; i < this.currentPath.getCurrentPathLength(); i++)
/*     */       {
/*  92 */         PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
/*     */         
/*  94 */         if (this.worldObj.canSeeSky(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord)))
/*     */         {
/*  96 */           this.currentPath.setCurrentPathLength(i - 1);
/*  97 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ)
/*     */   {
/* 109 */     int i = MathHelper.floor_double(posVec31.xCoord);
/* 110 */     int j = MathHelper.floor_double(posVec31.zCoord);
/* 111 */     double d0 = posVec32.xCoord - posVec31.xCoord;
/* 112 */     double d1 = posVec32.zCoord - posVec31.zCoord;
/* 113 */     double d2 = d0 * d0 + d1 * d1;
/*     */     
/* 115 */     if (d2 < 1.0E-8D)
/*     */     {
/* 117 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 121 */     double d3 = 1.0D / Math.sqrt(d2);
/* 122 */     d0 *= d3;
/* 123 */     d1 *= d3;
/* 124 */     sizeX += 2;
/* 125 */     sizeZ += 2;
/*     */     
/* 127 */     if (!isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1))
/*     */     {
/* 129 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 133 */     sizeX -= 2;
/* 134 */     sizeZ -= 2;
/* 135 */     double d4 = 1.0D / Math.abs(d0);
/* 136 */     double d5 = 1.0D / Math.abs(d1);
/* 137 */     double d6 = i * 1 - posVec31.xCoord;
/* 138 */     double d7 = j * 1 - posVec31.zCoord;
/*     */     
/* 140 */     if (d0 >= 0.0D)
/*     */     {
/* 142 */       d6 += 1.0D;
/*     */     }
/*     */     
/* 145 */     if (d1 >= 0.0D)
/*     */     {
/* 147 */       d7 += 1.0D;
/*     */     }
/*     */     
/* 150 */     d6 /= d0;
/* 151 */     d7 /= d1;
/* 152 */     int k = d0 < 0.0D ? -1 : 1;
/* 153 */     int l = d1 < 0.0D ? -1 : 1;
/* 154 */     int i1 = MathHelper.floor_double(posVec32.xCoord);
/* 155 */     int j1 = MathHelper.floor_double(posVec32.zCoord);
/* 156 */     int k1 = i1 - i;
/* 157 */     int l1 = j1 - j;
/*     */     
/* 159 */     while ((k1 * k > 0) || (l1 * l > 0))
/*     */     {
/* 161 */       if (d6 < d7)
/*     */       {
/* 163 */         d6 += d4;
/* 164 */         i += k;
/* 165 */         k1 = i1 - i;
/*     */       }
/*     */       else
/*     */       {
/* 169 */         d7 += d5;
/* 170 */         j += l;
/* 171 */         l1 = j1 - j;
/*     */       }
/*     */       
/* 174 */       if (!isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1))
/*     */       {
/* 176 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 180 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3 vec31, double p_179683_8_, double p_179683_10_)
/*     */   {
/* 190 */     int i = x - sizeX / 2;
/* 191 */     int j = z - sizeZ / 2;
/*     */     
/* 193 */     if (!isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_))
/*     */     {
/* 195 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 199 */     for (int k = i; k < i + sizeX; k++)
/*     */     {
/* 201 */       for (int l = j; l < j + sizeZ; l++)
/*     */       {
/* 203 */         double d0 = k + 0.5D - vec31.xCoord;
/* 204 */         double d1 = l + 0.5D - vec31.zCoord;
/*     */         
/* 206 */         if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D)
/*     */         {
/* 208 */           Block block = this.worldObj.getBlockState(new BlockPos(k, y - 1, l)).getBlock();
/* 209 */           Material material = block.getMaterial();
/*     */           
/* 211 */           if (material == Material.air)
/*     */           {
/* 213 */             return false;
/*     */           }
/*     */           
/* 216 */           if ((material == Material.water) && (!this.theEntity.isInWater()))
/*     */           {
/* 218 */             return false;
/*     */           }
/*     */           
/* 221 */           if (material == Material.lava)
/*     */           {
/* 223 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 229 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isPositionClear(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_)
/*     */   {
/* 238 */     for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)))
/*     */     {
/* 240 */       double d0 = blockpos.getX() + 0.5D - p_179692_7_.xCoord;
/* 241 */       double d1 = blockpos.getZ() + 0.5D - p_179692_7_.zCoord;
/*     */       
/* 243 */       if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D)
/*     */       {
/* 245 */         Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */         
/* 247 */         if (!block.isPassable(this.worldObj, blockpos))
/*     */         {
/* 249 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 254 */     return true;
/*     */   }
/*     */   
/*     */   public void setAvoidsWater(boolean avoidsWater)
/*     */   {
/* 259 */     this.nodeProcessor.setAvoidsWater(avoidsWater);
/*     */   }
/*     */   
/*     */   public boolean getAvoidsWater()
/*     */   {
/* 264 */     return this.nodeProcessor.getAvoidsWater();
/*     */   }
/*     */   
/*     */   public void setBreakDoors(boolean canBreakDoors)
/*     */   {
/* 269 */     this.nodeProcessor.setBreakDoors(canBreakDoors);
/*     */   }
/*     */   
/*     */   public void setEnterDoors(boolean par1)
/*     */   {
/* 274 */     this.nodeProcessor.setEnterDoors(par1);
/*     */   }
/*     */   
/*     */   public boolean getEnterDoors()
/*     */   {
/* 279 */     return this.nodeProcessor.getEnterDoors();
/*     */   }
/*     */   
/*     */   public void setCanSwim(boolean canSwim)
/*     */   {
/* 284 */     this.nodeProcessor.setCanSwim(canSwim);
/*     */   }
/*     */   
/*     */   public boolean getCanSwim()
/*     */   {
/* 289 */     return this.nodeProcessor.getCanSwim();
/*     */   }
/*     */   
/*     */   public void setAvoidSun(boolean par1)
/*     */   {
/* 294 */     this.shouldAvoidSun = par1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\pathfinding\PathNavigateGround.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */