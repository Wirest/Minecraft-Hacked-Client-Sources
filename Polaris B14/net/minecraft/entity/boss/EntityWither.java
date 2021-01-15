/*     */ package net.minecraft.entity.boss;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIArrowAttack;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.monster.EntityMob;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityWitherSkull;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob
/*     */ {
/*  42 */   private float[] field_82220_d = new float[2];
/*  43 */   private float[] field_82221_e = new float[2];
/*  44 */   private float[] field_82217_f = new float[2];
/*  45 */   private float[] field_82218_g = new float[2];
/*  46 */   private int[] field_82223_h = new int[2];
/*  47 */   private int[] field_82224_i = new int[2];
/*     */   
/*     */   private int blockBreakCounter;
/*     */   
/*  51 */   private static final Predicate<Entity> attackEntitySelector = new Predicate()
/*     */   {
/*     */     public boolean apply(Entity p_apply_1_)
/*     */     {
/*  55 */       return ((p_apply_1_ instanceof EntityLivingBase)) && (((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD);
/*     */     }
/*     */   };
/*     */   
/*     */   public EntityWither(World worldIn)
/*     */   {
/*  61 */     super(worldIn);
/*  62 */     setHealth(getMaxHealth());
/*  63 */     setSize(0.9F, 3.5F);
/*  64 */     this.isImmuneToFire = true;
/*  65 */     ((PathNavigateGround)getNavigator()).setCanSwim(true);
/*  66 */     this.tasks.addTask(0, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  67 */     this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
/*  68 */     this.tasks.addTask(5, new net.minecraft.entity.ai.EntityAIWander(this, 1.0D));
/*  69 */     this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  70 */     this.tasks.addTask(7, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  71 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, false, new Class[0]));
/*  72 */     this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
/*  73 */     this.experienceValue = 50;
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  78 */     super.entityInit();
/*  79 */     this.dataWatcher.addObject(17, new Integer(0));
/*  80 */     this.dataWatcher.addObject(18, new Integer(0));
/*  81 */     this.dataWatcher.addObject(19, new Integer(0));
/*  82 */     this.dataWatcher.addObject(20, new Integer(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  90 */     super.writeEntityToNBT(tagCompound);
/*  91 */     tagCompound.setInteger("Invul", getInvulTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/*  99 */     super.readEntityFromNBT(tagCompund);
/* 100 */     setInvulTime(tagCompund.getInteger("Invul"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 108 */     return "mob.wither.idle";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 116 */     return "mob.wither.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 124 */     return "mob.wither.death";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 133 */     this.motionY *= 0.6000000238418579D;
/*     */     
/* 135 */     if ((!this.worldObj.isRemote) && (getWatchedTargetId(0) > 0))
/*     */     {
/* 137 */       Entity entity = this.worldObj.getEntityByID(getWatchedTargetId(0));
/*     */       
/* 139 */       if (entity != null)
/*     */       {
/* 141 */         if ((this.posY < entity.posY) || ((!isArmored()) && (this.posY < entity.posY + 5.0D)))
/*     */         {
/* 143 */           if (this.motionY < 0.0D)
/*     */           {
/* 145 */             this.motionY = 0.0D;
/*     */           }
/*     */           
/* 148 */           this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
/*     */         }
/*     */         
/* 151 */         double d0 = entity.posX - this.posX;
/* 152 */         double d1 = entity.posZ - this.posZ;
/* 153 */         double d3 = d0 * d0 + d1 * d1;
/*     */         
/* 155 */         if (d3 > 9.0D)
/*     */         {
/* 157 */           double d5 = MathHelper.sqrt_double(d3);
/* 158 */           this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
/* 159 */           this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 164 */     if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D)
/*     */     {
/* 166 */       this.rotationYaw = ((float)MathHelper.func_181159_b(this.motionZ, this.motionX) * 57.295776F - 90.0F);
/*     */     }
/*     */     
/* 169 */     super.onLivingUpdate();
/*     */     
/* 171 */     for (int i = 0; i < 2; i++)
/*     */     {
/* 173 */       this.field_82218_g[i] = this.field_82221_e[i];
/* 174 */       this.field_82217_f[i] = this.field_82220_d[i];
/*     */     }
/*     */     
/* 177 */     for (int j = 0; j < 2; j++)
/*     */     {
/* 179 */       int k = getWatchedTargetId(j + 1);
/* 180 */       Entity entity1 = null;
/*     */       
/* 182 */       if (k > 0)
/*     */       {
/* 184 */         entity1 = this.worldObj.getEntityByID(k);
/*     */       }
/*     */       
/* 187 */       if (entity1 != null)
/*     */       {
/* 189 */         double d11 = func_82214_u(j + 1);
/* 190 */         double d12 = func_82208_v(j + 1);
/* 191 */         double d13 = func_82213_w(j + 1);
/* 192 */         double d6 = entity1.posX - d11;
/* 193 */         double d7 = entity1.posY + entity1.getEyeHeight() - d12;
/* 194 */         double d8 = entity1.posZ - d13;
/* 195 */         double d9 = MathHelper.sqrt_double(d6 * d6 + d8 * d8);
/* 196 */         float f = (float)(MathHelper.func_181159_b(d8, d6) * 180.0D / 3.141592653589793D) - 90.0F;
/* 197 */         float f1 = (float)-(MathHelper.func_181159_b(d7, d9) * 180.0D / 3.141592653589793D);
/* 198 */         this.field_82220_d[j] = func_82204_b(this.field_82220_d[j], f1, 40.0F);
/* 199 */         this.field_82221_e[j] = func_82204_b(this.field_82221_e[j], f, 10.0F);
/*     */       }
/*     */       else
/*     */       {
/* 203 */         this.field_82221_e[j] = func_82204_b(this.field_82221_e[j], this.renderYawOffset, 10.0F);
/*     */       }
/*     */     }
/*     */     
/* 207 */     boolean flag = isArmored();
/*     */     
/* 209 */     for (int l = 0; l < 3; l++)
/*     */     {
/* 211 */       double d10 = func_82214_u(l);
/* 212 */       double d2 = func_82208_v(l);
/* 213 */       double d4 = func_82213_w(l);
/* 214 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       
/* 216 */       if ((flag) && (this.worldObj.rand.nextInt(4) == 0))
/*     */       {
/* 218 */         this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
/*     */       }
/*     */     }
/*     */     
/* 222 */     if (getInvulTime() > 0)
/*     */     {
/* 224 */       for (int i1 = 0; i1 < 3; i1++)
/*     */       {
/* 226 */         this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + this.rand.nextFloat() * 3.3F, this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/* 233 */     if (getInvulTime() > 0)
/*     */     {
/* 235 */       int j1 = getInvulTime() - 1;
/*     */       
/* 237 */       if (j1 <= 0)
/*     */       {
/* 239 */         this.worldObj.newExplosion(this, this.posX, this.posY + getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
/* 240 */         this.worldObj.playBroadcastSound(1013, new BlockPos(this), 0);
/*     */       }
/*     */       
/* 243 */       setInvulTime(j1);
/*     */       
/* 245 */       if (this.ticksExisted % 10 == 0)
/*     */       {
/* 247 */         heal(10.0F);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 252 */       super.updateAITasks();
/*     */       
/* 254 */       for (int i = 1; i < 3; i++)
/*     */       {
/* 256 */         if (this.ticksExisted >= this.field_82223_h[(i - 1)])
/*     */         {
/* 258 */           this.field_82223_h[(i - 1)] = (this.ticksExisted + 10 + this.rand.nextInt(10));
/*     */           
/* 260 */           if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) || (this.worldObj.getDifficulty() == EnumDifficulty.HARD))
/*     */           {
/* 262 */             int j3 = i - 1;
/* 263 */             int k3 = this.field_82224_i[(i - 1)];
/* 264 */             this.field_82224_i[j3] = (this.field_82224_i[(i - 1)] + 1);
/*     */             
/* 266 */             if (k3 > 15)
/*     */             {
/* 268 */               float f = 10.0F;
/* 269 */               float f1 = 5.0F;
/* 270 */               double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - f, this.posX + f);
/* 271 */               double d1 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - f1, this.posY + f1);
/* 272 */               double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - f, this.posZ + f);
/* 273 */               launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
/* 274 */               this.field_82224_i[(i - 1)] = 0;
/*     */             }
/*     */           }
/*     */           
/* 278 */           int k1 = getWatchedTargetId(i);
/*     */           
/* 280 */           if (k1 > 0)
/*     */           {
/* 282 */             Entity entity = this.worldObj.getEntityByID(k1);
/*     */             
/* 284 */             if ((entity != null) && (entity.isEntityAlive()) && (getDistanceSqToEntity(entity) <= 900.0D) && (canEntityBeSeen(entity)))
/*     */             {
/* 286 */               if (((entity instanceof EntityPlayer)) && (((EntityPlayer)entity).capabilities.disableDamage))
/*     */               {
/* 288 */                 updateWatchedTargetId(i, 0);
/*     */               }
/*     */               else
/*     */               {
/* 292 */                 launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
/* 293 */                 this.field_82223_h[(i - 1)] = (this.ticksExisted + 40 + this.rand.nextInt(20));
/* 294 */                 this.field_82224_i[(i - 1)] = 0;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/* 299 */               updateWatchedTargetId(i, 0);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 304 */             List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.and(attackEntitySelector, net.minecraft.util.EntitySelectors.NOT_SPECTATING));
/*     */             
/* 306 */             for (int j2 = 0; (j2 < 10) && (!list.isEmpty()); j2++)
/*     */             {
/* 308 */               EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
/*     */               
/* 310 */               if ((entitylivingbase != this) && (entitylivingbase.isEntityAlive()) && (canEntityBeSeen(entitylivingbase)))
/*     */               {
/* 312 */                 if ((entitylivingbase instanceof EntityPlayer))
/*     */                 {
/* 314 */                   if (((EntityPlayer)entitylivingbase).capabilities.disableDamage)
/*     */                     break;
/* 316 */                   updateWatchedTargetId(i, entitylivingbase.getEntityId());
/*     */                   
/* 318 */                   break;
/*     */                 }
/*     */                 
/* 321 */                 updateWatchedTargetId(i, entitylivingbase.getEntityId());
/*     */                 
/*     */ 
/* 324 */                 break;
/*     */               }
/*     */               
/* 327 */               list.remove(entitylivingbase);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 333 */       if (getAttackTarget() != null)
/*     */       {
/* 335 */         updateWatchedTargetId(0, getAttackTarget().getEntityId());
/*     */       }
/*     */       else
/*     */       {
/* 339 */         updateWatchedTargetId(0, 0);
/*     */       }
/*     */       
/* 342 */       if (this.blockBreakCounter > 0)
/*     */       {
/* 344 */         this.blockBreakCounter -= 1;
/*     */         
/* 346 */         if ((this.blockBreakCounter == 0) && (this.worldObj.getGameRules().getBoolean("mobGriefing")))
/*     */         {
/* 348 */           int i1 = MathHelper.floor_double(this.posY);
/* 349 */           int l1 = MathHelper.floor_double(this.posX);
/* 350 */           int i2 = MathHelper.floor_double(this.posZ);
/* 351 */           boolean flag = false;
/*     */           
/* 353 */           for (int k2 = -1; k2 <= 1; k2++)
/*     */           {
/* 355 */             for (int l2 = -1; l2 <= 1; l2++)
/*     */             {
/* 357 */               for (int j = 0; j <= 3; j++)
/*     */               {
/* 359 */                 int i3 = l1 + k2;
/* 360 */                 int k = i1 + j;
/* 361 */                 int l = i2 + l2;
/* 362 */                 BlockPos blockpos = new BlockPos(i3, k, l);
/* 363 */                 Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */                 
/* 365 */                 if ((block.getMaterial() != Material.air) && (func_181033_a(block)))
/*     */                 {
/* 367 */                   flag = (this.worldObj.destroyBlock(blockpos, true)) || (flag);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 373 */           if (flag)
/*     */           {
/* 375 */             this.worldObj.playAuxSFXAtEntity(null, 1012, new BlockPos(this), 0);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 380 */       if (this.ticksExisted % 20 == 0)
/*     */       {
/* 382 */         heal(1.0F);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean func_181033_a(Block p_181033_0_)
/*     */   {
/* 389 */     return (p_181033_0_ != Blocks.bedrock) && (p_181033_0_ != Blocks.end_portal) && (p_181033_0_ != Blocks.end_portal_frame) && (p_181033_0_ != Blocks.command_block) && (p_181033_0_ != Blocks.barrier);
/*     */   }
/*     */   
/*     */   public void func_82206_m()
/*     */   {
/* 394 */     setInvulTime(220);
/* 395 */     setHealth(getMaxHealth() / 3.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInWeb() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTotalArmorValue()
/*     */   {
/* 410 */     return 4;
/*     */   }
/*     */   
/*     */   private double func_82214_u(int p_82214_1_)
/*     */   {
/* 415 */     if (p_82214_1_ <= 0)
/*     */     {
/* 417 */       return this.posX;
/*     */     }
/*     */     
/*     */ 
/* 421 */     float f = (this.renderYawOffset + 180 * (p_82214_1_ - 1)) / 180.0F * 3.1415927F;
/* 422 */     float f1 = MathHelper.cos(f);
/* 423 */     return this.posX + f1 * 1.3D;
/*     */   }
/*     */   
/*     */ 
/*     */   private double func_82208_v(int p_82208_1_)
/*     */   {
/* 429 */     return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
/*     */   }
/*     */   
/*     */   private double func_82213_w(int p_82213_1_)
/*     */   {
/* 434 */     if (p_82213_1_ <= 0)
/*     */     {
/* 436 */       return this.posZ;
/*     */     }
/*     */     
/*     */ 
/* 440 */     float f = (this.renderYawOffset + 180 * (p_82213_1_ - 1)) / 180.0F * 3.1415927F;
/* 441 */     float f1 = MathHelper.sin(f);
/* 442 */     return this.posZ + f1 * 1.3D;
/*     */   }
/*     */   
/*     */ 
/*     */   private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_)
/*     */   {
/* 448 */     float f = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);
/*     */     
/* 450 */     if (f > p_82204_3_)
/*     */     {
/* 452 */       f = p_82204_3_;
/*     */     }
/*     */     
/* 455 */     if (f < -p_82204_3_)
/*     */     {
/* 457 */       f = -p_82204_3_;
/*     */     }
/*     */     
/* 460 */     return p_82204_1_ + f;
/*     */   }
/*     */   
/*     */   private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_)
/*     */   {
/* 465 */     launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, (p_82216_1_ == 0) && (this.rand.nextFloat() < 0.001F));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void launchWitherSkullToCoords(int p_82209_1_, double x, double y, double z, boolean invulnerable)
/*     */   {
/* 473 */     this.worldObj.playAuxSFXAtEntity(null, 1014, new BlockPos(this), 0);
/* 474 */     double d0 = func_82214_u(p_82209_1_);
/* 475 */     double d1 = func_82208_v(p_82209_1_);
/* 476 */     double d2 = func_82213_w(p_82209_1_);
/* 477 */     double d3 = x - d0;
/* 478 */     double d4 = y - d1;
/* 479 */     double d5 = z - d2;
/* 480 */     EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, this, d3, d4, d5);
/*     */     
/* 482 */     if (invulnerable)
/*     */     {
/* 484 */       entitywitherskull.setInvulnerable(true);
/*     */     }
/*     */     
/* 487 */     entitywitherskull.posY = d1;
/* 488 */     entitywitherskull.posX = d0;
/* 489 */     entitywitherskull.posZ = d2;
/* 490 */     this.worldObj.spawnEntityInWorld(entitywitherskull);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
/*     */   {
/* 498 */     launchWitherSkullToEntity(0, p_82196_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 506 */     if (isEntityInvulnerable(source))
/*     */     {
/* 508 */       return false;
/*     */     }
/* 510 */     if ((source != DamageSource.drown) && (!(source.getEntity() instanceof EntityWither)))
/*     */     {
/* 512 */       if ((getInvulTime() > 0) && (source != DamageSource.outOfWorld))
/*     */       {
/* 514 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 518 */       if (isArmored())
/*     */       {
/* 520 */         Entity entity = source.getSourceOfDamage();
/*     */         
/* 522 */         if ((entity instanceof EntityArrow))
/*     */         {
/* 524 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 528 */       Entity entity1 = source.getEntity();
/*     */       
/* 530 */       if ((entity1 != null) && (!(entity1 instanceof EntityPlayer)) && ((entity1 instanceof EntityLivingBase)) && (((EntityLivingBase)entity1).getCreatureAttribute() == getCreatureAttribute()))
/*     */       {
/* 532 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 536 */       if (this.blockBreakCounter <= 0)
/*     */       {
/* 538 */         this.blockBreakCounter = 20;
/*     */       }
/*     */       
/* 541 */       for (int i = 0; i < this.field_82224_i.length; i++)
/*     */       {
/* 543 */         this.field_82224_i[i] += 3;
/*     */       }
/*     */       
/* 546 */       return super.attackEntityFrom(source, amount);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 552 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 561 */     EntityItem entityitem = dropItem(net.minecraft.init.Items.nether_star, 1);
/*     */     
/* 563 */     if (entityitem != null)
/*     */     {
/* 565 */       entityitem.setNoDespawn();
/*     */     }
/*     */     
/* 568 */     if (!this.worldObj.isRemote)
/*     */     {
/* 570 */       for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)))
/*     */       {
/* 572 */         entityplayer.triggerAchievement(net.minecraft.stats.AchievementList.killWither);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void despawnEntity()
/*     */   {
/* 582 */     this.entityAge = 0;
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/* 587 */     return 15728880;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void addPotionEffect(PotionEffect potioneffectIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void applyEntityAttributes()
/*     */   {
/* 603 */     super.applyEntityAttributes();
/* 604 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
/* 605 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
/* 606 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
/*     */   }
/*     */   
/*     */   public float func_82207_a(int p_82207_1_)
/*     */   {
/* 611 */     return this.field_82221_e[p_82207_1_];
/*     */   }
/*     */   
/*     */   public float func_82210_r(int p_82210_1_)
/*     */   {
/* 616 */     return this.field_82220_d[p_82210_1_];
/*     */   }
/*     */   
/*     */   public int getInvulTime()
/*     */   {
/* 621 */     return this.dataWatcher.getWatchableObjectInt(20);
/*     */   }
/*     */   
/*     */   public void setInvulTime(int p_82215_1_)
/*     */   {
/* 626 */     this.dataWatcher.updateObject(20, Integer.valueOf(p_82215_1_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getWatchedTargetId(int p_82203_1_)
/*     */   {
/* 634 */     return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateWatchedTargetId(int targetOffset, int newId)
/*     */   {
/* 642 */     this.dataWatcher.updateObject(17 + targetOffset, Integer.valueOf(newId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isArmored()
/*     */   {
/* 651 */     return getHealth() <= getMaxHealth() / 2.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumCreatureAttribute getCreatureAttribute()
/*     */   {
/* 659 */     return EnumCreatureAttribute.UNDEAD;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mountEntity(Entity entityIn)
/*     */   {
/* 667 */     this.ridingEntity = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\boss\EntityWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */