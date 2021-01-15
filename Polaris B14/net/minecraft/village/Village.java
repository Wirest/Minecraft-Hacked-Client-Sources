/*     */ package net.minecraft.village;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.TreeMap;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.PlayerProfileCache;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class Village
/*     */ {
/*     */   private World worldObj;
/*  29 */   private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  35 */   private BlockPos centerHelper = BlockPos.ORIGIN;
/*     */   
/*     */ 
/*  38 */   private BlockPos center = BlockPos.ORIGIN;
/*     */   
/*     */   private int villageRadius;
/*     */   
/*     */   private int lastAddDoorTimestamp;
/*     */   private int tickCounter;
/*     */   private int numVillagers;
/*     */   private int noBreedTicks;
/*  46 */   private TreeMap<String, Integer> playerReputation = new TreeMap();
/*  47 */   private List<VillageAggressor> villageAgressors = Lists.newArrayList();
/*     */   
/*     */   private int numIronGolems;
/*     */   
/*     */ 
/*     */   public Village() {}
/*     */   
/*     */   public Village(World worldIn)
/*     */   {
/*  56 */     this.worldObj = worldIn;
/*     */   }
/*     */   
/*     */   public void setWorld(World worldIn)
/*     */   {
/*  61 */     this.worldObj = worldIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void tick(int p_75560_1_)
/*     */   {
/*  69 */     this.tickCounter = p_75560_1_;
/*  70 */     removeDeadAndOutOfRangeDoors();
/*  71 */     removeDeadAndOldAgressors();
/*     */     
/*  73 */     if (p_75560_1_ % 20 == 0)
/*     */     {
/*  75 */       updateNumVillagers();
/*     */     }
/*     */     
/*  78 */     if (p_75560_1_ % 30 == 0)
/*     */     {
/*  80 */       updateNumIronGolems();
/*     */     }
/*     */     
/*  83 */     int i = this.numVillagers / 10;
/*     */     
/*  85 */     if ((this.numIronGolems < i) && (this.villageDoorInfoList.size() > 20) && (this.worldObj.rand.nextInt(7000) == 0))
/*     */     {
/*  87 */       Vec3 vec3 = func_179862_a(this.center, 2, 4, 2);
/*     */       
/*  89 */       if (vec3 != null)
/*     */       {
/*  91 */         EntityIronGolem entityirongolem = new EntityIronGolem(this.worldObj);
/*  92 */         entityirongolem.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
/*  93 */         this.worldObj.spawnEntityInWorld(entityirongolem);
/*  94 */         this.numIronGolems += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Vec3 func_179862_a(BlockPos p_179862_1_, int p_179862_2_, int p_179862_3_, int p_179862_4_)
/*     */   {
/* 101 */     for (int i = 0; i < 10; i++)
/*     */     {
/* 103 */       BlockPos blockpos = p_179862_1_.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
/*     */       
/* 105 */       if ((func_179866_a(blockpos)) && (func_179861_a(new BlockPos(p_179862_2_, p_179862_3_, p_179862_4_), blockpos)))
/*     */       {
/* 107 */         return new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*     */       }
/*     */     }
/*     */     
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   private boolean func_179861_a(BlockPos p_179861_1_, BlockPos p_179861_2_)
/*     */   {
/* 116 */     if (!World.doesBlockHaveSolidTopSurface(this.worldObj, p_179861_2_.down()))
/*     */     {
/* 118 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 122 */     int i = p_179861_2_.getX() - p_179861_1_.getX() / 2;
/* 123 */     int j = p_179861_2_.getZ() - p_179861_1_.getZ() / 2;
/*     */     
/* 125 */     for (int k = i; k < i + p_179861_1_.getX(); k++)
/*     */     {
/* 127 */       for (int l = p_179861_2_.getY(); l < p_179861_2_.getY() + p_179861_1_.getY(); l++)
/*     */       {
/* 129 */         for (int i1 = j; i1 < j + p_179861_1_.getZ(); i1++)
/*     */         {
/* 131 */           if (this.worldObj.getBlockState(new BlockPos(k, l, i1)).getBlock().isNormalCube())
/*     */           {
/* 133 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 139 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void updateNumIronGolems()
/*     */   {
/* 145 */     List<EntityIronGolem> list = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
/* 146 */     this.numIronGolems = list.size();
/*     */   }
/*     */   
/*     */   private void updateNumVillagers()
/*     */   {
/* 151 */     List<EntityVillager> list = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
/* 152 */     this.numVillagers = list.size();
/*     */     
/* 154 */     if (this.numVillagers == 0)
/*     */     {
/* 156 */       this.playerReputation.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public BlockPos getCenter()
/*     */   {
/* 162 */     return this.center;
/*     */   }
/*     */   
/*     */   public int getVillageRadius()
/*     */   {
/* 167 */     return this.villageRadius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getNumVillageDoors()
/*     */   {
/* 176 */     return this.villageDoorInfoList.size();
/*     */   }
/*     */   
/*     */   public int getTicksSinceLastDoorAdding()
/*     */   {
/* 181 */     return this.tickCounter - this.lastAddDoorTimestamp;
/*     */   }
/*     */   
/*     */   public int getNumVillagers()
/*     */   {
/* 186 */     return this.numVillagers;
/*     */   }
/*     */   
/*     */   public boolean func_179866_a(BlockPos pos)
/*     */   {
/* 191 */     return this.center.distanceSq(pos) < this.villageRadius * this.villageRadius;
/*     */   }
/*     */   
/*     */   public List<VillageDoorInfo> getVillageDoorInfoList()
/*     */   {
/* 196 */     return this.villageDoorInfoList;
/*     */   }
/*     */   
/*     */   public VillageDoorInfo getNearestDoor(BlockPos pos)
/*     */   {
/* 201 */     VillageDoorInfo villagedoorinfo = null;
/* 202 */     int i = Integer.MAX_VALUE;
/*     */     
/* 204 */     for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList)
/*     */     {
/* 206 */       int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
/*     */       
/* 208 */       if (j < i)
/*     */       {
/* 210 */         villagedoorinfo = villagedoorinfo1;
/* 211 */         i = j;
/*     */       }
/*     */     }
/*     */     
/* 215 */     return villagedoorinfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public VillageDoorInfo getDoorInfo(BlockPos pos)
/*     */   {
/* 223 */     VillageDoorInfo villagedoorinfo = null;
/* 224 */     int i = Integer.MAX_VALUE;
/*     */     
/* 226 */     for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList)
/*     */     {
/* 228 */       int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
/*     */       
/* 230 */       if (j > 256)
/*     */       {
/* 232 */         j *= 1000;
/*     */       }
/*     */       else
/*     */       {
/* 236 */         j = villagedoorinfo1.getDoorOpeningRestrictionCounter();
/*     */       }
/*     */       
/* 239 */       if (j < i)
/*     */       {
/* 241 */         villagedoorinfo = villagedoorinfo1;
/* 242 */         i = j;
/*     */       }
/*     */     }
/*     */     
/* 246 */     return villagedoorinfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public VillageDoorInfo getExistedDoor(BlockPos doorBlock)
/*     */   {
/* 254 */     if (this.center.distanceSq(doorBlock) > this.villageRadius * this.villageRadius)
/*     */     {
/* 256 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 260 */     for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList)
/*     */     {
/* 262 */       if ((villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX()) && (villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ()) && (Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1))
/*     */       {
/* 264 */         return villagedoorinfo;
/*     */       }
/*     */     }
/*     */     
/* 268 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addVillageDoorInfo(VillageDoorInfo doorInfo)
/*     */   {
/* 274 */     this.villageDoorInfoList.add(doorInfo);
/* 275 */     this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
/* 276 */     updateVillageRadiusAndCenter();
/* 277 */     this.lastAddDoorTimestamp = doorInfo.getInsidePosY();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAnnihilated()
/*     */   {
/* 285 */     return this.villageDoorInfoList.isEmpty();
/*     */   }
/*     */   
/*     */   public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn)
/*     */   {
/* 290 */     for (VillageAggressor village$villageaggressor : this.villageAgressors)
/*     */     {
/* 292 */       if (village$villageaggressor.agressor == entitylivingbaseIn)
/*     */       {
/* 294 */         village$villageaggressor.agressionTime = this.tickCounter;
/* 295 */         return;
/*     */       }
/*     */     }
/*     */     
/* 299 */     this.villageAgressors.add(new VillageAggressor(entitylivingbaseIn, this.tickCounter));
/*     */   }
/*     */   
/*     */   public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn)
/*     */   {
/* 304 */     double d0 = Double.MAX_VALUE;
/* 305 */     VillageAggressor village$villageaggressor = null;
/*     */     
/* 307 */     for (int i = 0; i < this.villageAgressors.size(); i++)
/*     */     {
/* 309 */       VillageAggressor village$villageaggressor1 = (VillageAggressor)this.villageAgressors.get(i);
/* 310 */       double d1 = village$villageaggressor1.agressor.getDistanceSqToEntity(entitylivingbaseIn);
/*     */       
/* 312 */       if (d1 <= d0)
/*     */       {
/* 314 */         village$villageaggressor = village$villageaggressor1;
/* 315 */         d0 = d1;
/*     */       }
/*     */     }
/*     */     
/* 319 */     return village$villageaggressor != null ? village$villageaggressor.agressor : null;
/*     */   }
/*     */   
/*     */   public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender)
/*     */   {
/* 324 */     double d0 = Double.MAX_VALUE;
/* 325 */     EntityPlayer entityplayer = null;
/*     */     
/* 327 */     for (String s : this.playerReputation.keySet())
/*     */     {
/* 329 */       if (isPlayerReputationTooLow(s))
/*     */       {
/* 331 */         EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(s);
/*     */         
/* 333 */         if (entityplayer1 != null)
/*     */         {
/* 335 */           double d1 = entityplayer1.getDistanceSqToEntity(villageDefender);
/*     */           
/* 337 */           if (d1 <= d0)
/*     */           {
/* 339 */             entityplayer = entityplayer1;
/* 340 */             d0 = d1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 346 */     return entityplayer;
/*     */   }
/*     */   
/*     */   private void removeDeadAndOldAgressors()
/*     */   {
/* 351 */     Iterator<VillageAggressor> iterator = this.villageAgressors.iterator();
/*     */     
/* 353 */     while (iterator.hasNext())
/*     */     {
/* 355 */       VillageAggressor village$villageaggressor = (VillageAggressor)iterator.next();
/*     */       
/* 357 */       if ((!village$villageaggressor.agressor.isEntityAlive()) || (Math.abs(this.tickCounter - village$villageaggressor.agressionTime) > 300))
/*     */       {
/* 359 */         iterator.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeDeadAndOutOfRangeDoors()
/*     */   {
/* 366 */     boolean flag = false;
/* 367 */     boolean flag1 = this.worldObj.rand.nextInt(50) == 0;
/* 368 */     Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
/*     */     
/* 370 */     while (iterator.hasNext())
/*     */     {
/* 372 */       VillageDoorInfo villagedoorinfo = (VillageDoorInfo)iterator.next();
/*     */       
/* 374 */       if (flag1)
/*     */       {
/* 376 */         villagedoorinfo.resetDoorOpeningRestrictionCounter();
/*     */       }
/*     */       
/* 379 */       if ((!isWoodDoor(villagedoorinfo.getDoorBlockPos())) || (Math.abs(this.tickCounter - villagedoorinfo.getInsidePosY()) > 1200))
/*     */       {
/* 381 */         this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
/* 382 */         flag = true;
/* 383 */         villagedoorinfo.setIsDetachedFromVillageFlag(true);
/* 384 */         iterator.remove();
/*     */       }
/*     */     }
/*     */     
/* 388 */     if (flag)
/*     */     {
/* 390 */       updateVillageRadiusAndCenter();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isWoodDoor(BlockPos pos)
/*     */   {
/* 396 */     Block block = this.worldObj.getBlockState(pos).getBlock();
/* 397 */     return block.getMaterial() == net.minecraft.block.material.Material.wood;
/*     */   }
/*     */   
/*     */   private void updateVillageRadiusAndCenter()
/*     */   {
/* 402 */     int i = this.villageDoorInfoList.size();
/*     */     
/* 404 */     if (i == 0)
/*     */     {
/* 406 */       this.center = new BlockPos(0, 0, 0);
/* 407 */       this.villageRadius = 0;
/*     */     }
/*     */     else
/*     */     {
/* 411 */       this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
/* 412 */       int j = 0;
/*     */       
/* 414 */       for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList)
/*     */       {
/* 416 */         j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j);
/*     */       }
/*     */       
/* 419 */       this.villageRadius = Math.max(32, (int)Math.sqrt(j) + 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getReputationForPlayer(String p_82684_1_)
/*     */   {
/* 428 */     Integer integer = (Integer)this.playerReputation.get(p_82684_1_);
/* 429 */     return integer != null ? integer.intValue() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int setReputationForPlayer(String p_82688_1_, int p_82688_2_)
/*     */   {
/* 437 */     int i = getReputationForPlayer(p_82688_1_);
/* 438 */     int j = MathHelper.clamp_int(i + p_82688_2_, -30, 10);
/* 439 */     this.playerReputation.put(p_82688_1_, Integer.valueOf(j));
/* 440 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPlayerReputationTooLow(String p_82687_1_)
/*     */   {
/* 448 */     return getReputationForPlayer(p_82687_1_) <= -15;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readVillageDataFromNBT(NBTTagCompound p_82690_1_)
/*     */   {
/* 456 */     this.numVillagers = p_82690_1_.getInteger("PopSize");
/* 457 */     this.villageRadius = p_82690_1_.getInteger("Radius");
/* 458 */     this.numIronGolems = p_82690_1_.getInteger("Golems");
/* 459 */     this.lastAddDoorTimestamp = p_82690_1_.getInteger("Stable");
/* 460 */     this.tickCounter = p_82690_1_.getInteger("Tick");
/* 461 */     this.noBreedTicks = p_82690_1_.getInteger("MTick");
/* 462 */     this.center = new BlockPos(p_82690_1_.getInteger("CX"), p_82690_1_.getInteger("CY"), p_82690_1_.getInteger("CZ"));
/* 463 */     this.centerHelper = new BlockPos(p_82690_1_.getInteger("ACX"), p_82690_1_.getInteger("ACY"), p_82690_1_.getInteger("ACZ"));
/* 464 */     NBTTagList nbttaglist = p_82690_1_.getTagList("Doors", 10);
/*     */     
/* 466 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 468 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 469 */       VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
/* 470 */       this.villageDoorInfoList.add(villagedoorinfo);
/*     */     }
/*     */     
/* 473 */     NBTTagList nbttaglist1 = p_82690_1_.getTagList("Players", 10);
/*     */     
/* 475 */     for (int j = 0; j < nbttaglist1.tagCount(); j++)
/*     */     {
/* 477 */       NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);
/*     */       
/* 479 */       if (nbttagcompound1.hasKey("UUID"))
/*     */       {
/* 481 */         PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
/* 482 */         GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound1.getString("UUID")));
/*     */         
/* 484 */         if (gameprofile != null)
/*     */         {
/* 486 */           this.playerReputation.put(gameprofile.getName(), Integer.valueOf(nbttagcompound1.getInteger("S")));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 491 */         this.playerReputation.put(nbttagcompound1.getString("Name"), Integer.valueOf(nbttagcompound1.getInteger("S")));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeVillageDataToNBT(NBTTagCompound p_82689_1_)
/*     */   {
/* 501 */     p_82689_1_.setInteger("PopSize", this.numVillagers);
/* 502 */     p_82689_1_.setInteger("Radius", this.villageRadius);
/* 503 */     p_82689_1_.setInteger("Golems", this.numIronGolems);
/* 504 */     p_82689_1_.setInteger("Stable", this.lastAddDoorTimestamp);
/* 505 */     p_82689_1_.setInteger("Tick", this.tickCounter);
/* 506 */     p_82689_1_.setInteger("MTick", this.noBreedTicks);
/* 507 */     p_82689_1_.setInteger("CX", this.center.getX());
/* 508 */     p_82689_1_.setInteger("CY", this.center.getY());
/* 509 */     p_82689_1_.setInteger("CZ", this.center.getZ());
/* 510 */     p_82689_1_.setInteger("ACX", this.centerHelper.getX());
/* 511 */     p_82689_1_.setInteger("ACY", this.centerHelper.getY());
/* 512 */     p_82689_1_.setInteger("ACZ", this.centerHelper.getZ());
/* 513 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     NBTTagCompound nbttagcompound;
/* 515 */     for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList)
/*     */     {
/* 517 */       nbttagcompound = new NBTTagCompound();
/* 518 */       nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
/* 519 */       nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
/* 520 */       nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
/* 521 */       nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
/* 522 */       nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
/* 523 */       nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
/* 524 */       nbttaglist.appendTag(nbttagcompound);
/*     */     }
/*     */     
/* 527 */     p_82689_1_.setTag("Doors", nbttaglist);
/* 528 */     NBTTagList nbttaglist1 = new NBTTagList();
/*     */     
/* 530 */     for (String s : this.playerReputation.keySet())
/*     */     {
/* 532 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 533 */       PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
/* 534 */       GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);
/*     */       
/* 536 */       if (gameprofile != null)
/*     */       {
/* 538 */         nbttagcompound1.setString("UUID", gameprofile.getId().toString());
/* 539 */         nbttagcompound1.setInteger("S", ((Integer)this.playerReputation.get(s)).intValue());
/* 540 */         nbttaglist1.appendTag(nbttagcompound1);
/*     */       }
/*     */     }
/*     */     
/* 544 */     p_82689_1_.setTag("Players", nbttaglist1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endMatingSeason()
/*     */   {
/* 552 */     this.noBreedTicks = this.tickCounter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMatingSeason()
/*     */   {
/* 560 */     return (this.noBreedTicks == 0) || (this.tickCounter - this.noBreedTicks >= 3600);
/*     */   }
/*     */   
/*     */   public void setDefaultPlayerReputation(int p_82683_1_)
/*     */   {
/* 565 */     for (String s : this.playerReputation.keySet())
/*     */     {
/* 567 */       setReputationForPlayer(s, p_82683_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   class VillageAggressor
/*     */   {
/*     */     public EntityLivingBase agressor;
/*     */     public int agressionTime;
/*     */     
/*     */     VillageAggressor(EntityLivingBase p_i1674_2_, int p_i1674_3_)
/*     */     {
/* 578 */       this.agressor = p_i1674_2_;
/* 579 */       this.agressionTime = p_i1674_3_;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\village\Village.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */