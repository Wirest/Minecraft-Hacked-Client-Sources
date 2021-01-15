/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.ChunkCache;
/*     */ import net.minecraft.world.World;
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
/*     */ 
/*     */ public abstract class PathNavigate
/*     */ {
/*     */   protected EntityLiving theEntity;
/*     */   protected World worldObj;
/*     */   protected PathEntity currentPath;
/*     */   protected double speed;
/*     */   private final IAttributeInstance pathSearchRange;
/*     */   private int totalTicks;
/*     */   private int ticksAtLastPos;
/*  40 */   private Vec3 lastPosCheck = new Vec3(0.0D, 0.0D, 0.0D);
/*  41 */   private float heightRequirement = 1.0F;
/*     */   private final PathFinder pathFinder;
/*     */   
/*     */   public PathNavigate(EntityLiving entitylivingIn, World worldIn)
/*     */   {
/*  46 */     this.theEntity = entitylivingIn;
/*  47 */     this.worldObj = worldIn;
/*  48 */     this.pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.followRange);
/*  49 */     this.pathFinder = getPathFinder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract PathFinder getPathFinder();
/*     */   
/*     */ 
/*     */   public void setSpeed(double speedIn)
/*     */   {
/*  59 */     this.speed = speedIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getPathSearchRange()
/*     */   {
/*  67 */     return (float)this.pathSearchRange.getAttributeValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final PathEntity getPathToXYZ(double x, double y, double z)
/*     */   {
/*  75 */     return getPathToPos(new BlockPos(MathHelper.floor_double(x), (int)y, MathHelper.floor_double(z)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathEntity getPathToPos(BlockPos pos)
/*     */   {
/*  83 */     if (!canNavigate())
/*     */     {
/*  85 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  89 */     float f = getPathSearchRange();
/*  90 */     this.worldObj.theProfiler.startSection("pathfind");
/*  91 */     BlockPos blockpos = new BlockPos(this.theEntity);
/*  92 */     int i = (int)(f + 8.0F);
/*  93 */     ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
/*  94 */     PathEntity pathentity = this.pathFinder.createEntityPathTo(chunkcache, this.theEntity, pos, f);
/*  95 */     this.worldObj.theProfiler.endSection();
/*  96 */     return pathentity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean tryMoveToXYZ(double x, double y, double z, double speedIn)
/*     */   {
/* 105 */     PathEntity pathentity = getPathToXYZ(MathHelper.floor_double(x), (int)y, MathHelper.floor_double(z));
/* 106 */     return setPath(pathentity, speedIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeightRequirement(float jumpHeight)
/*     */   {
/* 114 */     this.heightRequirement = jumpHeight;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathEntity getPathToEntityLiving(Entity entityIn)
/*     */   {
/* 122 */     if (!canNavigate())
/*     */     {
/* 124 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 128 */     float f = getPathSearchRange();
/* 129 */     this.worldObj.theProfiler.startSection("pathfind");
/* 130 */     BlockPos blockpos = new BlockPos(this.theEntity).up();
/* 131 */     int i = (int)(f + 16.0F);
/* 132 */     ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
/* 133 */     PathEntity pathentity = this.pathFinder.createEntityPathTo(chunkcache, this.theEntity, entityIn, f);
/* 134 */     this.worldObj.theProfiler.endSection();
/* 135 */     return pathentity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn)
/*     */   {
/* 144 */     PathEntity pathentity = getPathToEntityLiving(entityIn);
/* 145 */     return pathentity != null ? setPath(pathentity, speedIn) : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean setPath(PathEntity pathentityIn, double speedIn)
/*     */   {
/* 154 */     if (pathentityIn == null)
/*     */     {
/* 156 */       this.currentPath = null;
/* 157 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 161 */     if (!pathentityIn.isSamePath(this.currentPath))
/*     */     {
/* 163 */       this.currentPath = pathentityIn;
/*     */     }
/*     */     
/* 166 */     removeSunnyPath();
/*     */     
/* 168 */     if (this.currentPath.getCurrentPathLength() == 0)
/*     */     {
/* 170 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 174 */     this.speed = speedIn;
/* 175 */     Vec3 vec3 = getEntityPosition();
/* 176 */     this.ticksAtLastPos = this.totalTicks;
/* 177 */     this.lastPosCheck = vec3;
/* 178 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathEntity getPath()
/*     */   {
/* 188 */     return this.currentPath;
/*     */   }
/*     */   
/*     */   public void onUpdateNavigation()
/*     */   {
/* 193 */     this.totalTicks += 1;
/*     */     
/* 195 */     if (!noPath())
/*     */     {
/* 197 */       if (canNavigate())
/*     */       {
/* 199 */         pathFollow();
/*     */       }
/* 201 */       else if ((this.currentPath != null) && (this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()))
/*     */       {
/* 203 */         Vec3 vec3 = getEntityPosition();
/* 204 */         Vec3 vec31 = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
/*     */         
/* 206 */         if ((vec3.yCoord > vec31.yCoord) && (!this.theEntity.onGround) && (MathHelper.floor_double(vec3.xCoord) == MathHelper.floor_double(vec31.xCoord)) && (MathHelper.floor_double(vec3.zCoord) == MathHelper.floor_double(vec31.zCoord)))
/*     */         {
/* 208 */           this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
/*     */         }
/*     */       }
/*     */       
/* 212 */       if (!noPath())
/*     */       {
/* 214 */         Vec3 vec32 = this.currentPath.getPosition(this.theEntity);
/*     */         
/* 216 */         if (vec32 != null)
/*     */         {
/* 218 */           AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(vec32.xCoord, vec32.yCoord, vec32.zCoord, vec32.xCoord, vec32.yCoord, vec32.zCoord).expand(0.5D, 0.5D, 0.5D);
/* 219 */           List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this.theEntity, axisalignedbb1.addCoord(0.0D, -1.0D, 0.0D));
/* 220 */           double d0 = -1.0D;
/* 221 */           axisalignedbb1 = axisalignedbb1.offset(0.0D, 1.0D, 0.0D);
/*     */           
/* 223 */           for (AxisAlignedBB axisalignedbb : list)
/*     */           {
/* 225 */             d0 = axisalignedbb.calculateYOffset(axisalignedbb1, d0);
/*     */           }
/*     */           
/* 228 */           this.theEntity.getMoveHelper().setMoveTo(vec32.xCoord, vec32.yCoord + d0, vec32.zCoord, this.speed);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void pathFollow()
/*     */   {
/* 236 */     Vec3 vec3 = getEntityPosition();
/* 237 */     int i = this.currentPath.getCurrentPathLength();
/*     */     
/* 239 */     for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); j++)
/*     */     {
/* 241 */       if (this.currentPath.getPathPointFromIndex(j).yCoord != (int)vec3.yCoord)
/*     */       {
/* 243 */         i = j;
/* 244 */         break;
/*     */       }
/*     */     }
/*     */     
/* 248 */     float f = this.theEntity.width * this.theEntity.width * this.heightRequirement;
/*     */     
/* 250 */     for (int k = this.currentPath.getCurrentPathIndex(); k < i; k++)
/*     */     {
/* 252 */       Vec3 vec31 = this.currentPath.getVectorFromIndex(this.theEntity, k);
/*     */       
/* 254 */       if (vec3.squareDistanceTo(vec31) < f)
/*     */       {
/* 256 */         this.currentPath.setCurrentPathIndex(k + 1);
/*     */       }
/*     */     }
/*     */     
/* 260 */     int j1 = MathHelper.ceiling_float_int(this.theEntity.width);
/* 261 */     int k1 = (int)this.theEntity.height + 1;
/* 262 */     int l = j1;
/*     */     
/* 264 */     for (int i1 = i - 1; i1 >= this.currentPath.getCurrentPathIndex(); i1--)
/*     */     {
/* 266 */       if (isDirectPathBetweenPoints(vec3, this.currentPath.getVectorFromIndex(this.theEntity, i1), j1, k1, l))
/*     */       {
/* 268 */         this.currentPath.setCurrentPathIndex(i1);
/* 269 */         break;
/*     */       }
/*     */     }
/*     */     
/* 273 */     checkForStuck(vec3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void checkForStuck(Vec3 positionVec3)
/*     */   {
/* 282 */     if (this.totalTicks - this.ticksAtLastPos > 100)
/*     */     {
/* 284 */       if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25D)
/*     */       {
/* 286 */         clearPathEntity();
/*     */       }
/*     */       
/* 289 */       this.ticksAtLastPos = this.totalTicks;
/* 290 */       this.lastPosCheck = positionVec3;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean noPath()
/*     */   {
/* 299 */     return (this.currentPath == null) || (this.currentPath.isFinished());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearPathEntity()
/*     */   {
/* 307 */     this.currentPath = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract Vec3 getEntityPosition();
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract boolean canNavigate();
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isInLiquid()
/*     */   {
/* 322 */     return (this.theEntity.isInWater()) || (this.theEntity.isInLava());
/*     */   }
/*     */   
/*     */   protected void removeSunnyPath() {}
/*     */   
/*     */   protected abstract boolean isDirectPathBetweenPoints(Vec3 paramVec31, Vec3 paramVec32, int paramInt1, int paramInt2, int paramInt3);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\pathfinding\PathNavigate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */