/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIFollowParent;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityChicken extends EntityAnimal
/*     */ {
/*     */   public float wingRotation;
/*     */   public float destPos;
/*     */   public float field_70884_g;
/*     */   public float field_70888_h;
/*  30 */   public float wingRotDelta = 1.0F;
/*     */   
/*     */   public int timeUntilNextEgg;
/*     */   
/*     */   public boolean chickenJockey;
/*     */   
/*     */   public EntityChicken(World worldIn)
/*     */   {
/*  38 */     super(worldIn);
/*  39 */     setSize(0.4F, 0.7F);
/*  40 */     this.timeUntilNextEgg = (this.rand.nextInt(6000) + 6000);
/*  41 */     this.tasks.addTask(0, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  42 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAIPanic(this, 1.4D));
/*  43 */     this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
/*  44 */     this.tasks.addTask(3, new net.minecraft.entity.ai.EntityAITempt(this, 1.0D, Items.wheat_seeds, false));
/*  45 */     this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
/*  46 */     this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
/*  47 */     this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
/*  48 */     this.tasks.addTask(7, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/*  53 */     return this.height;
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  58 */     super.applyEntityAttributes();
/*  59 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
/*  60 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/*  69 */     super.onLivingUpdate();
/*  70 */     this.field_70888_h = this.wingRotation;
/*  71 */     this.field_70884_g = this.destPos;
/*  72 */     this.destPos = ((float)(this.destPos + (this.onGround ? -1 : 4) * 0.3D));
/*  73 */     this.destPos = MathHelper.clamp_float(this.destPos, 0.0F, 1.0F);
/*     */     
/*  75 */     if ((!this.onGround) && (this.wingRotDelta < 1.0F))
/*     */     {
/*  77 */       this.wingRotDelta = 1.0F;
/*     */     }
/*     */     
/*  80 */     this.wingRotDelta = ((float)(this.wingRotDelta * 0.9D));
/*     */     
/*  82 */     if ((!this.onGround) && (this.motionY < 0.0D))
/*     */     {
/*  84 */       this.motionY *= 0.6D;
/*     */     }
/*     */     
/*  87 */     this.wingRotation += this.wingRotDelta * 2.0F;
/*     */     
/*  89 */     if ((!this.worldObj.isRemote) && (!isChild()) && (!isChickenJockey()) && (--this.timeUntilNextEgg <= 0))
/*     */     {
/*  91 */       playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*  92 */       dropItem(Items.egg, 1);
/*  93 */       this.timeUntilNextEgg = (this.rand.nextInt(6000) + 6000);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/* 106 */     return "mob.chicken.say";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/* 114 */     return "mob.chicken.hurt";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/* 122 */     return "mob.chicken.hurt";
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn)
/*     */   {
/* 127 */     playSound("mob.chicken.step", 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/* 132 */     return Items.feather;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 140 */     int i = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
/*     */     
/* 142 */     for (int j = 0; j < i; j++)
/*     */     {
/* 144 */       dropItem(Items.feather, 1);
/*     */     }
/*     */     
/* 147 */     if (isBurning())
/*     */     {
/* 149 */       dropItem(Items.cooked_chicken, 1);
/*     */     }
/*     */     else
/*     */     {
/* 153 */       dropItem(Items.chicken, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public EntityChicken createChild(EntityAgeable ageable)
/*     */   {
/* 159 */     return new EntityChicken(this.worldObj);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBreedingItem(ItemStack stack)
/*     */   {
/* 168 */     return (stack != null) && (stack.getItem() == Items.wheat_seeds);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 176 */     super.readEntityFromNBT(tagCompund);
/* 177 */     this.chickenJockey = tagCompund.getBoolean("IsChickenJockey");
/*     */     
/* 179 */     if (tagCompund.hasKey("EggLayTime"))
/*     */     {
/* 181 */       this.timeUntilNextEgg = tagCompund.getInteger("EggLayTime");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getExperiencePoints(EntityPlayer player)
/*     */   {
/* 190 */     return isChickenJockey() ? 10 : super.getExperiencePoints(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 198 */     super.writeEntityToNBT(tagCompound);
/* 199 */     tagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
/* 200 */     tagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDespawn()
/*     */   {
/* 208 */     return (isChickenJockey()) && (this.riddenByEntity == null);
/*     */   }
/*     */   
/*     */   public void updateRiderPosition()
/*     */   {
/* 213 */     super.updateRiderPosition();
/* 214 */     float f = MathHelper.sin(this.renderYawOffset * 3.1415927F / 180.0F);
/* 215 */     float f1 = MathHelper.cos(this.renderYawOffset * 3.1415927F / 180.0F);
/* 216 */     float f2 = 0.1F;
/* 217 */     float f3 = 0.0F;
/* 218 */     this.riddenByEntity.setPosition(this.posX + f2 * f, this.posY + this.height * 0.5F + this.riddenByEntity.getYOffset() + f3, this.posZ - f2 * f1);
/*     */     
/* 220 */     if ((this.riddenByEntity instanceof EntityLivingBase))
/*     */     {
/* 222 */       ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isChickenJockey()
/*     */   {
/* 231 */     return this.chickenJockey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChickenJockey(boolean jockey)
/*     */   {
/* 239 */     this.chickenJockey = jockey;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */