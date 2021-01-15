/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAICreeperSwell;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class EntityCreeper
/*     */   extends EntityMob
/*     */ {
/*     */   private int lastActiveTime;
/*     */   private int timeSinceIgnited;
/*  36 */   private int fuseTime = 30;
/*     */   
/*     */ 
/*  39 */   private int explosionRadius = 3;
/*  40 */   private int field_175494_bm = 0;
/*     */   
/*     */   public EntityCreeper(World worldIn)
/*     */   {
/*  44 */     super(worldIn);
/*  45 */     this.tasks.addTask(1, new EntityAISwimming(this));
/*  46 */     this.tasks.addTask(2, new EntityAICreeperSwell(this));
/*  47 */     this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
/*  48 */     this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
/*  49 */     this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
/*  50 */     this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  51 */     this.tasks.addTask(6, new EntityAILookIdle(this));
/*  52 */     this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*  53 */     this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  58 */     super.applyEntityAttributes();
/*  59 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxFallHeight()
/*     */   {
/*  67 */     return getAttackTarget() == null ? 3 : 3 + (int)(getHealth() - 1.0F);
/*     */   }
/*     */   
/*     */   public void fall(float distance, float damageMultiplier)
/*     */   {
/*  72 */     super.fall(distance, damageMultiplier);
/*  73 */     this.timeSinceIgnited = ((int)(this.timeSinceIgnited + distance * 1.5F));
/*     */     
/*  75 */     if (this.timeSinceIgnited > this.fuseTime - 5)
/*     */     {
/*  77 */       this.timeSinceIgnited = (this.fuseTime - 5);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  83 */     super.entityInit();
/*  84 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)-1));
/*  85 */     this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
/*  86 */     this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  94 */     super.writeEntityToNBT(tagCompound);
/*     */     
/*  96 */     if (this.dataWatcher.getWatchableObjectByte(17) == 1)
/*     */     {
/*  98 */       tagCompound.setBoolean("powered", true);
/*     */     }
/*     */     
/* 101 */     tagCompound.setShort("Fuse", (short)this.fuseTime);
/* 102 */     tagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
/* 103 */     tagCompound.setBoolean("ignited", hasIgnited());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 111 */     super.readEntityFromNBT(tagCompund);
/* 112 */     this.dataWatcher.updateObject(17, Byte.valueOf((byte)(tagCompund.getBoolean("powered") ? 1 : 0)));
/*     */     
/* 114 */     if (tagCompund.hasKey("Fuse", 99))
/*     */     {
/* 116 */       this.fuseTime = tagCompund.getShort("Fuse");
/*     */     }
/*     */     
/* 119 */     if (tagCompund.hasKey("ExplosionRadius", 99))
/*     */     {
/* 121 */       this.explosionRadius = tagCompund.getByte("ExplosionRadius");
/*     */     }
/*     */     
/* 124 */     if (tagCompund.getBoolean("ignited"))
/*     */     {
/* 126 */       ignite();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 135 */     if (isEntityAlive())
/*     */     {
/* 137 */       this.lastActiveTime = this.timeSinceIgnited;
/*     */       
/* 139 */       if (hasIgnited())
/*     */       {
/* 141 */         setCreeperState(1);
/*     */       }
/*     */       
/* 144 */       int i = getCreeperState();
/*     */       
/* 146 */       if ((i > 0) && (this.timeSinceIgnited == 0))
/*     */       {
/* 148 */         playSound("creeper.primed", 1.0F, 0.5F);
/*     */       }
/*     */       
/* 151 */       this.timeSinceIgnited += i;
/*     */       
/* 153 */       if (this.timeSinceIgnited < 0)
/*     */       {
/* 155 */         this.timeSinceIgnited = 0;
/*     */       }
/*     */       
/* 158 */       if (this.timeSinceIgnited >= this.fuseTime)
/*     */       {
/* 160 */         this.timeSinceIgnited = this.fuseTime;
/* 161 */         explode();
/*     */       }
/*     */     }
/*     */     
/* 165 */     super.onUpdate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 173 */     return "mob.creeper.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 181 */     return "mob.creeper.death";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDeath(DamageSource cause)
/*     */   {
/* 189 */     super.onDeath(cause);
/*     */     
/* 191 */     if ((cause.getEntity() instanceof EntitySkeleton))
/*     */     {
/* 193 */       int i = Item.getIdFromItem(Items.record_13);
/* 194 */       int j = Item.getIdFromItem(Items.record_wait);
/* 195 */       int k = i + this.rand.nextInt(j - i + 1);
/* 196 */       dropItem(Item.getItemById(k), 1);
/*     */     }
/* 198 */     else if (((cause.getEntity() instanceof EntityCreeper)) && (cause.getEntity() != this) && (((EntityCreeper)cause.getEntity()).getPowered()) && (((EntityCreeper)cause.getEntity()).isAIEnabled()))
/*     */     {
/* 200 */       ((EntityCreeper)cause.getEntity()).func_175493_co();
/* 201 */       entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn)
/*     */   {
/* 207 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getPowered()
/*     */   {
/* 215 */     return this.dataWatcher.getWatchableObjectByte(17) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getCreeperFlashIntensity(float p_70831_1_)
/*     */   {
/* 223 */     return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (this.fuseTime - 2);
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 228 */     return Items.gunpowder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCreeperState()
/*     */   {
/* 236 */     return this.dataWatcher.getWatchableObjectByte(16);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCreeperState(int state)
/*     */   {
/* 244 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)state));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onStruckByLightning(EntityLightningBolt lightningBolt)
/*     */   {
/* 252 */     super.onStruckByLightning(lightningBolt);
/* 253 */     this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean interact(EntityPlayer player)
/*     */   {
/* 261 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 263 */     if ((itemstack != null) && (itemstack.getItem() == Items.flint_and_steel))
/*     */     {
/* 265 */       this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.ignite", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
/* 266 */       player.swingItem();
/*     */       
/* 268 */       if (!this.worldObj.isRemote)
/*     */       {
/* 270 */         ignite();
/* 271 */         itemstack.damageItem(1, player);
/* 272 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 276 */     return super.interact(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void explode()
/*     */   {
/* 284 */     if (!this.worldObj.isRemote)
/*     */     {
/* 286 */       boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
/* 287 */       float f = getPowered() ? 2.0F : 1.0F;
/* 288 */       this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
/* 289 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasIgnited()
/*     */   {
/* 295 */     return this.dataWatcher.getWatchableObjectByte(18) != 0;
/*     */   }
/*     */   
/*     */   public void ignite()
/*     */   {
/* 300 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAIEnabled()
/*     */   {
/* 308 */     return (this.field_175494_bm < 1) && (this.worldObj.getGameRules().getBoolean("doMobLoot"));
/*     */   }
/*     */   
/*     */   public void func_175493_co()
/*     */   {
/* 313 */     this.field_175494_bm += 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */