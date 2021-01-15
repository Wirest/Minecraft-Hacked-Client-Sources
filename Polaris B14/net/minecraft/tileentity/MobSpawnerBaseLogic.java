/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.WeightedRandom.Item;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class MobSpawnerBaseLogic
/*     */ {
/*  24 */   private int spawnDelay = 20;
/*  25 */   private String mobID = "Pig";
/*  26 */   private final List<WeightedRandomMinecart> minecartToSpawn = Lists.newArrayList();
/*     */   
/*     */   private WeightedRandomMinecart randomEntity;
/*     */   
/*     */   private double mobRotation;
/*     */   
/*     */   private double prevMobRotation;
/*     */   
/*  34 */   private int minSpawnDelay = 200;
/*  35 */   private int maxSpawnDelay = 800;
/*  36 */   private int spawnCount = 4;
/*     */   
/*     */   private Entity cachedEntity;
/*     */   
/*  40 */   private int maxNearbyEntities = 6;
/*     */   
/*     */ 
/*  43 */   private int activatingRangeFromPlayer = 16;
/*     */   
/*     */ 
/*  46 */   private int spawnRange = 4;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getEntityNameToSpawn()
/*     */   {
/*  53 */     if (getRandomEntity() == null)
/*     */     {
/*  55 */       if ((this.mobID != null) && (this.mobID.equals("Minecart")))
/*     */       {
/*  57 */         this.mobID = "MinecartRideable";
/*     */       }
/*     */       
/*  60 */       return this.mobID;
/*     */     }
/*     */     
/*     */ 
/*  64 */     return getRandomEntity().entityType;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setEntityName(String name)
/*     */   {
/*  70 */     this.mobID = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isActivated()
/*     */   {
/*  78 */     BlockPos blockpos = getSpawnerPosition();
/*  79 */     return getSpawnerWorld().isAnyPlayerWithinRangeAt(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, this.activatingRangeFromPlayer);
/*     */   }
/*     */   
/*     */   public void updateSpawner()
/*     */   {
/*  84 */     if (isActivated())
/*     */     {
/*  86 */       BlockPos blockpos = getSpawnerPosition();
/*     */       
/*  88 */       if (getSpawnerWorld().isRemote)
/*     */       {
/*  90 */         double d3 = blockpos.getX() + getSpawnerWorld().rand.nextFloat();
/*  91 */         double d4 = blockpos.getY() + getSpawnerWorld().rand.nextFloat();
/*  92 */         double d5 = blockpos.getZ() + getSpawnerWorld().rand.nextFloat();
/*  93 */         getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
/*  94 */         getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         
/*  96 */         if (this.spawnDelay > 0)
/*     */         {
/*  98 */           this.spawnDelay -= 1;
/*     */         }
/*     */         
/* 101 */         this.prevMobRotation = this.mobRotation;
/* 102 */         this.mobRotation = ((this.mobRotation + 1000.0F / (this.spawnDelay + 200.0F)) % 360.0D);
/*     */       }
/*     */       else
/*     */       {
/* 106 */         if (this.spawnDelay == -1)
/*     */         {
/* 108 */           resetTimer();
/*     */         }
/*     */         
/* 111 */         if (this.spawnDelay > 0)
/*     */         {
/* 113 */           this.spawnDelay -= 1;
/* 114 */           return;
/*     */         }
/*     */         
/* 117 */         boolean flag = false;
/*     */         
/* 119 */         for (int i = 0; i < this.spawnCount; i++)
/*     */         {
/* 121 */           Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), getSpawnerWorld());
/*     */           
/* 123 */           if (entity == null)
/*     */           {
/* 125 */             return;
/*     */           }
/*     */           
/* 128 */           int j = getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1).expand(this.spawnRange, this.spawnRange, this.spawnRange)).size();
/*     */           
/* 130 */           if (j >= this.maxNearbyEntities)
/*     */           {
/* 132 */             resetTimer();
/* 133 */             return;
/*     */           }
/*     */           
/* 136 */           double d0 = blockpos.getX() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * this.spawnRange + 0.5D;
/* 137 */           double d1 = blockpos.getY() + getSpawnerWorld().rand.nextInt(3) - 1;
/* 138 */           double d2 = blockpos.getZ() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * this.spawnRange + 0.5D;
/* 139 */           EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
/* 140 */           entity.setLocationAndAngles(d0, d1, d2, getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);
/*     */           
/* 142 */           if ((entityliving == null) || ((entityliving.getCanSpawnHere()) && (entityliving.isNotColliding())))
/*     */           {
/* 144 */             spawnNewEntity(entity, true);
/* 145 */             getSpawnerWorld().playAuxSFX(2004, blockpos, 0);
/*     */             
/* 147 */             if (entityliving != null)
/*     */             {
/* 149 */               entityliving.spawnExplosionParticle();
/*     */             }
/*     */             
/* 152 */             flag = true;
/*     */           }
/*     */         }
/*     */         
/* 156 */         if (flag)
/*     */         {
/* 158 */           resetTimer();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Entity spawnNewEntity(Entity entityIn, boolean spawn)
/*     */   {
/* 166 */     if (getRandomEntity() != null)
/*     */     {
/* 168 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 169 */       entityIn.writeToNBTOptional(nbttagcompound);
/*     */       
/* 171 */       for (String s : getRandomEntity().nbtData.getKeySet())
/*     */       {
/* 173 */         NBTBase nbtbase = getRandomEntity().nbtData.getTag(s);
/* 174 */         nbttagcompound.setTag(s, nbtbase.copy());
/*     */       }
/*     */       
/* 177 */       entityIn.readFromNBT(nbttagcompound);
/*     */       
/* 179 */       if ((entityIn.worldObj != null) && (spawn))
/*     */       {
/* 181 */         entityIn.worldObj.spawnEntityInWorld(entityIn);
/*     */       }
/*     */       
/*     */       NBTTagCompound nbttagcompound2;
/*     */       
/* 186 */       for (Entity entity = entityIn; nbttagcompound.hasKey("Riding", 10); nbttagcompound = nbttagcompound2)
/*     */       {
/* 188 */         nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
/* 189 */         Entity entity1 = EntityList.createEntityByName(nbttagcompound2.getString("id"), entityIn.worldObj);
/*     */         
/* 191 */         if (entity1 != null)
/*     */         {
/* 193 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 194 */           entity1.writeToNBTOptional(nbttagcompound1);
/*     */           
/* 196 */           for (String s1 : nbttagcompound2.getKeySet())
/*     */           {
/* 198 */             NBTBase nbtbase1 = nbttagcompound2.getTag(s1);
/* 199 */             nbttagcompound1.setTag(s1, nbtbase1.copy());
/*     */           }
/*     */           
/* 202 */           entity1.readFromNBT(nbttagcompound1);
/* 203 */           entity1.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
/*     */           
/* 205 */           if ((entityIn.worldObj != null) && (spawn))
/*     */           {
/* 207 */             entityIn.worldObj.spawnEntityInWorld(entity1);
/*     */           }
/*     */           
/* 210 */           entity.mountEntity(entity1);
/*     */         }
/*     */         
/* 213 */         entity = entity1;
/*     */       }
/*     */     }
/* 216 */     else if (((entityIn instanceof EntityLivingBase)) && (entityIn.worldObj != null) && (spawn))
/*     */     {
/* 218 */       if ((entityIn instanceof EntityLiving))
/*     */       {
/* 220 */         ((EntityLiving)entityIn).onInitialSpawn(entityIn.worldObj.getDifficultyForLocation(new BlockPos(entityIn)), null);
/*     */       }
/*     */       
/* 223 */       entityIn.worldObj.spawnEntityInWorld(entityIn);
/*     */     }
/*     */     
/* 226 */     return entityIn;
/*     */   }
/*     */   
/*     */   private void resetTimer()
/*     */   {
/* 231 */     if (this.maxSpawnDelay <= this.minSpawnDelay)
/*     */     {
/* 233 */       this.spawnDelay = this.minSpawnDelay;
/*     */     }
/*     */     else
/*     */     {
/* 237 */       int i = this.maxSpawnDelay - this.minSpawnDelay;
/* 238 */       this.spawnDelay = (this.minSpawnDelay + getSpawnerWorld().rand.nextInt(i));
/*     */     }
/*     */     
/* 241 */     if (this.minecartToSpawn.size() > 0)
/*     */     {
/* 243 */       setRandomEntity((WeightedRandomMinecart)WeightedRandom.getRandomItem(getSpawnerWorld().rand, this.minecartToSpawn));
/*     */     }
/*     */     
/* 246 */     func_98267_a(1);
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound nbt)
/*     */   {
/* 251 */     this.mobID = nbt.getString("EntityId");
/* 252 */     this.spawnDelay = nbt.getShort("Delay");
/* 253 */     this.minecartToSpawn.clear();
/*     */     
/* 255 */     if (nbt.hasKey("SpawnPotentials", 9))
/*     */     {
/* 257 */       NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);
/*     */       
/* 259 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/* 261 */         this.minecartToSpawn.add(new WeightedRandomMinecart(nbttaglist.getCompoundTagAt(i)));
/*     */       }
/*     */     }
/*     */     
/* 265 */     if (nbt.hasKey("SpawnData", 10))
/*     */     {
/* 267 */       setRandomEntity(new WeightedRandomMinecart(nbt.getCompoundTag("SpawnData"), this.mobID));
/*     */     }
/*     */     else
/*     */     {
/* 271 */       setRandomEntity(null);
/*     */     }
/*     */     
/* 274 */     if (nbt.hasKey("MinSpawnDelay", 99))
/*     */     {
/* 276 */       this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
/* 277 */       this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
/* 278 */       this.spawnCount = nbt.getShort("SpawnCount");
/*     */     }
/*     */     
/* 281 */     if (nbt.hasKey("MaxNearbyEntities", 99))
/*     */     {
/* 283 */       this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
/* 284 */       this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
/*     */     }
/*     */     
/* 287 */     if (nbt.hasKey("SpawnRange", 99))
/*     */     {
/* 289 */       this.spawnRange = nbt.getShort("SpawnRange");
/*     */     }
/*     */     
/* 292 */     if (getSpawnerWorld() != null)
/*     */     {
/* 294 */       this.cachedEntity = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound nbt)
/*     */   {
/* 300 */     String s = getEntityNameToSpawn();
/*     */     
/* 302 */     if (!StringUtils.isNullOrEmpty(s))
/*     */     {
/* 304 */       nbt.setString("EntityId", s);
/* 305 */       nbt.setShort("Delay", (short)this.spawnDelay);
/* 306 */       nbt.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
/* 307 */       nbt.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
/* 308 */       nbt.setShort("SpawnCount", (short)this.spawnCount);
/* 309 */       nbt.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
/* 310 */       nbt.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
/* 311 */       nbt.setShort("SpawnRange", (short)this.spawnRange);
/*     */       
/* 313 */       if (getRandomEntity() != null)
/*     */       {
/* 315 */         nbt.setTag("SpawnData", getRandomEntity().nbtData.copy());
/*     */       }
/*     */       
/* 318 */       if ((getRandomEntity() != null) || (this.minecartToSpawn.size() > 0))
/*     */       {
/* 320 */         NBTTagList nbttaglist = new NBTTagList();
/*     */         
/* 322 */         if (this.minecartToSpawn.size() > 0)
/*     */         {
/* 324 */           for (WeightedRandomMinecart mobspawnerbaselogic$weightedrandomminecart : this.minecartToSpawn)
/*     */           {
/* 326 */             nbttaglist.appendTag(mobspawnerbaselogic$weightedrandomminecart.toNBT());
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 331 */           nbttaglist.appendTag(getRandomEntity().toNBT());
/*     */         }
/*     */         
/* 334 */         nbt.setTag("SpawnPotentials", nbttaglist);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Entity func_180612_a(World worldIn)
/*     */   {
/* 341 */     if (this.cachedEntity == null)
/*     */     {
/* 343 */       Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), worldIn);
/*     */       
/* 345 */       if (entity != null)
/*     */       {
/* 347 */         entity = spawnNewEntity(entity, false);
/* 348 */         this.cachedEntity = entity;
/*     */       }
/*     */     }
/*     */     
/* 352 */     return this.cachedEntity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean setDelayToMin(int delay)
/*     */   {
/* 360 */     if ((delay == 1) && (getSpawnerWorld().isRemote))
/*     */     {
/* 362 */       this.spawnDelay = this.minSpawnDelay;
/* 363 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 367 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private WeightedRandomMinecart getRandomEntity()
/*     */   {
/* 373 */     return this.randomEntity;
/*     */   }
/*     */   
/*     */   public void setRandomEntity(WeightedRandomMinecart p_98277_1_)
/*     */   {
/* 378 */     this.randomEntity = p_98277_1_;
/*     */   }
/*     */   
/*     */   public abstract void func_98267_a(int paramInt);
/*     */   
/*     */   public abstract World getSpawnerWorld();
/*     */   
/*     */   public abstract BlockPos getSpawnerPosition();
/*     */   
/*     */   public double getMobRotation()
/*     */   {
/* 389 */     return this.mobRotation;
/*     */   }
/*     */   
/*     */   public double getPrevMobRotation()
/*     */   {
/* 394 */     return this.prevMobRotation;
/*     */   }
/*     */   
/*     */   public class WeightedRandomMinecart extends WeightedRandom.Item
/*     */   {
/*     */     private final NBTTagCompound nbtData;
/*     */     private final String entityType;
/*     */     
/*     */     public WeightedRandomMinecart(NBTTagCompound tagCompound)
/*     */     {
/* 404 */       this(tagCompound.getCompoundTag("Properties"), tagCompound.getString("Type"), tagCompound.getInteger("Weight"));
/*     */     }
/*     */     
/*     */     public WeightedRandomMinecart(NBTTagCompound tagCompound, String type)
/*     */     {
/* 409 */       this(tagCompound, type, 1);
/*     */     }
/*     */     
/*     */     private WeightedRandomMinecart(NBTTagCompound tagCompound, String type, int weight)
/*     */     {
/* 414 */       super();
/*     */       
/* 416 */       if (type.equals("Minecart"))
/*     */       {
/* 418 */         if (tagCompound != null)
/*     */         {
/* 420 */           type = EntityMinecart.EnumMinecartType.byNetworkID(tagCompound.getInteger("Type")).getName();
/*     */         }
/*     */         else
/*     */         {
/* 424 */           type = "MinecartRideable";
/*     */         }
/*     */       }
/*     */       
/* 428 */       this.nbtData = tagCompound;
/* 429 */       this.entityType = type;
/*     */     }
/*     */     
/*     */     public NBTTagCompound toNBT()
/*     */     {
/* 434 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 435 */       nbttagcompound.setTag("Properties", this.nbtData);
/* 436 */       nbttagcompound.setString("Type", this.entityType);
/* 437 */       nbttagcompound.setInteger("Weight", this.itemWeight);
/* 438 */       return nbttagcompound;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\MobSpawnerBaseLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */