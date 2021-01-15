/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAILookAtVillager;
/*     */ import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.village.Village;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityIronGolem extends EntityGolem
/*     */ {
/*     */   private int homeCheckTimer;
/*     */   Village villageObj;
/*     */   private int attackTimer;
/*     */   private int holdRoseTick;
/*     */   
/*     */   public EntityIronGolem(World worldIn)
/*     */   {
/*  47 */     super(worldIn);
/*  48 */     setSize(1.4F, 2.9F);
/*  49 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  50 */     this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
/*  51 */     this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
/*  52 */     this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
/*  53 */     this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
/*  54 */     this.tasks.addTask(5, new EntityAILookAtVillager(this));
/*  55 */     this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
/*  56 */     this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
/*  57 */     this.tasks.addTask(8, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  58 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIDefendVillage(this));
/*  59 */     this.targetTasks.addTask(2, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, false, new Class[0]));
/*  60 */     this.targetTasks.addTask(3, new AINearestAttackableTargetNonCreeper(this, EntityLiving.class, 10, false, true, IMob.VISIBLE_MOB_SELECTOR));
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  65 */     super.entityInit();
/*  66 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   protected void updateAITasks()
/*     */   {
/*  71 */     if (--this.homeCheckTimer <= 0)
/*     */     {
/*  73 */       this.homeCheckTimer = (70 + this.rand.nextInt(50));
/*  74 */       this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this), 32);
/*     */       
/*  76 */       if (this.villageObj == null)
/*     */       {
/*  78 */         detachHome();
/*     */       }
/*     */       else
/*     */       {
/*  82 */         BlockPos blockpos = this.villageObj.getCenter();
/*  83 */         setHomePosAndDistance(blockpos, (int)(this.villageObj.getVillageRadius() * 0.6F));
/*     */       }
/*     */     }
/*     */     
/*  87 */     super.updateAITasks();
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  92 */     super.applyEntityAttributes();
/*  93 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
/*  94 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int decreaseAirSupply(int p_70682_1_)
/*     */   {
/* 102 */     return p_70682_1_;
/*     */   }
/*     */   
/*     */   protected void collideWithEntity(Entity p_82167_1_)
/*     */   {
/* 107 */     if (((p_82167_1_ instanceof IMob)) && (!(p_82167_1_ instanceof EntityCreeper)) && (getRNG().nextInt(20) == 0))
/*     */     {
/* 109 */       setAttackTarget((EntityLivingBase)p_82167_1_);
/*     */     }
/*     */     
/* 112 */     super.collideWithEntity(p_82167_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 121 */     super.onLivingUpdate();
/*     */     
/* 123 */     if (this.attackTimer > 0)
/*     */     {
/* 125 */       this.attackTimer -= 1;
/*     */     }
/*     */     
/* 128 */     if (this.holdRoseTick > 0)
/*     */     {
/* 130 */       this.holdRoseTick -= 1;
/*     */     }
/*     */     
/* 133 */     if ((this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D) && (this.rand.nextInt(5) == 0))
/*     */     {
/* 135 */       int i = MathHelper.floor_double(this.posX);
/* 136 */       int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/* 137 */       int k = MathHelper.floor_double(this.posZ);
/* 138 */       IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
/* 139 */       Block block = iblockstate.getBlock();
/*     */       
/* 141 */       if (block.getMaterial() != Material.air)
/*     */       {
/* 143 */         this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(iblockstate) });
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 161 */     super.writeEntityToNBT(tagCompound);
/* 162 */     tagCompound.setBoolean("PlayerCreated", isPlayerCreated());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 170 */     super.readEntityFromNBT(tagCompund);
/* 171 */     setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 176 */     this.attackTimer = 10;
/* 177 */     this.worldObj.setEntityState(this, (byte)4);
/* 178 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));
/*     */     
/* 180 */     if (flag)
/*     */     {
/* 182 */       entityIn.motionY += 0.4000000059604645D;
/* 183 */       applyEnchantments(this, entityIn);
/*     */     }
/*     */     
/* 186 */     playSound("mob.irongolem.throw", 1.0F, 1.0F);
/* 187 */     return flag;
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 192 */     if (id == 4)
/*     */     {
/* 194 */       this.attackTimer = 10;
/* 195 */       playSound("mob.irongolem.throw", 1.0F, 1.0F);
/*     */     }
/* 197 */     else if (id == 11)
/*     */     {
/* 199 */       this.holdRoseTick = 400;
/*     */     }
/*     */     else
/*     */     {
/* 203 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */   public Village getVillage()
/*     */   {
/* 209 */     return this.villageObj;
/*     */   }
/*     */   
/*     */   public int getAttackTimer()
/*     */   {
/* 214 */     return this.attackTimer;
/*     */   }
/*     */   
/*     */   public void setHoldingRose(boolean p_70851_1_)
/*     */   {
/* 219 */     this.holdRoseTick = (p_70851_1_ ? 400 : 0);
/* 220 */     this.worldObj.setEntityState(this, (byte)11);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 228 */     return "mob.irongolem.hit";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 236 */     return "mob.irongolem.death";
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 241 */     playSound("mob.irongolem.walk", 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 249 */     int i = this.rand.nextInt(3);
/*     */     
/* 251 */     for (int j = 0; j < i; j++)
/*     */     {
/* 253 */       dropItemWithOffset(Item.getItemFromBlock(net.minecraft.init.Blocks.red_flower), 1, BlockFlower.EnumFlowerType.POPPY.getMeta());
/*     */     }
/*     */     
/* 256 */     int l = 3 + this.rand.nextInt(3);
/*     */     
/* 258 */     for (int k = 0; k < l; k++)
/*     */     {
/* 260 */       dropItem(Items.iron_ingot, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getHoldRoseTick()
/*     */   {
/* 266 */     return this.holdRoseTick;
/*     */   }
/*     */   
/*     */   public boolean isPlayerCreated()
/*     */   {
/* 271 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
/*     */   }
/*     */   
/*     */   public void setPlayerCreated(boolean p_70849_1_)
/*     */   {
/* 276 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 278 */     if (p_70849_1_)
/*     */     {
/* 280 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else
/*     */     {
/* 284 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDeath(DamageSource cause)
/*     */   {
/* 293 */     if ((!isPlayerCreated()) && (this.attackingPlayer != null) && (this.villageObj != null))
/*     */     {
/* 295 */       this.villageObj.setReputationForPlayer(this.attackingPlayer.getName(), -5);
/*     */     }
/*     */     
/* 298 */     super.onDeath(cause);
/*     */   }
/*     */   
/*     */   static class AINearestAttackableTargetNonCreeper<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
/*     */   {
/*     */     public AINearestAttackableTargetNonCreeper(final EntityCreature creature, Class<T> classTarget, int chance, boolean p_i45858_4_, boolean p_i45858_5_, final Predicate<? super T> p_i45858_6_)
/*     */     {
/* 305 */       super(classTarget, chance, p_i45858_4_, p_i45858_5_, p_i45858_6_);
/* 306 */       this.targetEntitySelector = new Predicate()
/*     */       {
/*     */         public boolean apply(T p_apply_1_)
/*     */         {
/* 310 */           if ((p_i45858_6_ != null) && (!p_i45858_6_.apply(p_apply_1_)))
/*     */           {
/* 312 */             return false;
/*     */           }
/* 314 */           if ((p_apply_1_ instanceof EntityCreeper))
/*     */           {
/* 316 */             return false;
/*     */           }
/*     */           
/*     */ 
/* 320 */           if ((p_apply_1_ instanceof EntityPlayer))
/*     */           {
/* 322 */             double d0 = EntityIronGolem.AINearestAttackableTargetNonCreeper.this.getTargetDistance();
/*     */             
/* 324 */             if (p_apply_1_.isSneaking())
/*     */             {
/* 326 */               d0 *= 0.800000011920929D;
/*     */             }
/*     */             
/* 329 */             if (p_apply_1_.isInvisible())
/*     */             {
/* 331 */               float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
/*     */               
/* 333 */               if (f < 0.1F)
/*     */               {
/* 335 */                 f = 0.1F;
/*     */               }
/*     */               
/* 338 */               d0 *= 0.7F * f;
/*     */             }
/*     */             
/* 341 */             if (p_apply_1_.getDistanceToEntity(creature) > d0)
/*     */             {
/* 343 */               return false;
/*     */             }
/*     */           }
/*     */           
/* 347 */           return EntityIronGolem.AINearestAttackableTargetNonCreeper.this.isSuitableTarget(p_apply_1_, false);
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityIronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */