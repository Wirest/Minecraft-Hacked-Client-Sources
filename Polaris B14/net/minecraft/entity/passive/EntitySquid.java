/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntitySquid
/*     */   extends EntityWaterMob
/*     */ {
/*     */   public float squidPitch;
/*     */   public float prevSquidPitch;
/*     */   public float squidYaw;
/*     */   public float prevSquidYaw;
/*     */   public float squidRotation;
/*     */   public float prevSquidRotation;
/*     */   public float tentacleAngle;
/*     */   public float lastTentacleAngle;
/*     */   private float randomMotionSpeed;
/*     */   private float rotationVelocity;
/*     */   private float field_70871_bB;
/*     */   private float randomMotionVecX;
/*     */   private float randomMotionVecY;
/*     */   private float randomMotionVecZ;
/*     */   
/*     */   public EntitySquid(World worldIn)
/*     */   {
/*  44 */     super(worldIn);
/*  45 */     setSize(0.95F, 0.95F);
/*  46 */     this.rand.setSeed(1 + getEntityId());
/*  47 */     this.rotationVelocity = (1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F);
/*  48 */     this.tasks.addTask(0, new AIMoveRandom(this));
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/*  53 */     super.applyEntityAttributes();
/*  54 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/*  59 */     return this.height * 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  67 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  75 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/*  83 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSoundVolume()
/*     */   {
/*  91 */     return 0.4F;
/*     */   }
/*     */   
/*     */   protected Item getDropItem()
/*     */   {
/*  96 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/* 105 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 113 */     int i = this.rand.nextInt(3 + p_70628_2_) + 1;
/*     */     
/* 115 */     for (int j = 0; j < i; j++)
/*     */     {
/* 117 */       entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInWater()
/*     */   {
/* 127 */     return this.worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 136 */     super.onLivingUpdate();
/* 137 */     this.prevSquidPitch = this.squidPitch;
/* 138 */     this.prevSquidYaw = this.squidYaw;
/* 139 */     this.prevSquidRotation = this.squidRotation;
/* 140 */     this.lastTentacleAngle = this.tentacleAngle;
/* 141 */     this.squidRotation += this.rotationVelocity;
/*     */     
/* 143 */     if (this.squidRotation > 6.283185307179586D)
/*     */     {
/* 145 */       if (this.worldObj.isRemote)
/*     */       {
/* 147 */         this.squidRotation = 6.2831855F;
/*     */       }
/*     */       else
/*     */       {
/* 151 */         this.squidRotation = ((float)(this.squidRotation - 6.283185307179586D));
/*     */         
/* 153 */         if (this.rand.nextInt(10) == 0)
/*     */         {
/* 155 */           this.rotationVelocity = (1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F);
/*     */         }
/*     */         
/* 158 */         this.worldObj.setEntityState(this, (byte)19);
/*     */       }
/*     */     }
/*     */     
/* 162 */     if (this.inWater)
/*     */     {
/* 164 */       if (this.squidRotation < 3.1415927F)
/*     */       {
/* 166 */         float f = this.squidRotation / 3.1415927F;
/* 167 */         this.tentacleAngle = (MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F);
/*     */         
/* 169 */         if (f > 0.75D)
/*     */         {
/* 171 */           this.randomMotionSpeed = 1.0F;
/* 172 */           this.field_70871_bB = 1.0F;
/*     */         }
/*     */         else
/*     */         {
/* 176 */           this.field_70871_bB *= 0.8F;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 181 */         this.tentacleAngle = 0.0F;
/* 182 */         this.randomMotionSpeed *= 0.9F;
/* 183 */         this.field_70871_bB *= 0.99F;
/*     */       }
/*     */       
/* 186 */       if (!this.worldObj.isRemote)
/*     */       {
/* 188 */         this.motionX = (this.randomMotionVecX * this.randomMotionSpeed);
/* 189 */         this.motionY = (this.randomMotionVecY * this.randomMotionSpeed);
/* 190 */         this.motionZ = (this.randomMotionVecZ * this.randomMotionSpeed);
/*     */       }
/*     */       
/* 193 */       float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 194 */       this.renderYawOffset += (-(float)MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0F / 3.1415927F - this.renderYawOffset) * 0.1F;
/* 195 */       this.rotationYaw = this.renderYawOffset;
/* 196 */       this.squidYaw = ((float)(this.squidYaw + 3.141592653589793D * this.field_70871_bB * 1.5D));
/* 197 */       this.squidPitch += (-(float)MathHelper.func_181159_b(f1, this.motionY) * 180.0F / 3.1415927F - this.squidPitch) * 0.1F;
/*     */     }
/*     */     else
/*     */     {
/* 201 */       this.tentacleAngle = (MathHelper.abs(MathHelper.sin(this.squidRotation)) * 3.1415927F * 0.25F);
/*     */       
/* 203 */       if (!this.worldObj.isRemote)
/*     */       {
/* 205 */         this.motionX = 0.0D;
/* 206 */         this.motionY -= 0.08D;
/* 207 */         this.motionY *= 0.9800000190734863D;
/* 208 */         this.motionZ = 0.0D;
/*     */       }
/*     */       
/* 211 */       this.squidPitch = ((float)(this.squidPitch + (-90.0F - this.squidPitch) * 0.02D));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveEntityWithHeading(float strafe, float forward)
/*     */   {
/* 220 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanSpawnHere()
/*     */   {
/* 228 */     return (this.posY > 45.0D) && (this.posY < this.worldObj.func_181545_F()) && (super.getCanSpawnHere());
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 233 */     if (id == 19)
/*     */     {
/* 235 */       this.squidRotation = 0.0F;
/*     */     }
/*     */     else
/*     */     {
/* 239 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_175568_b(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn)
/*     */   {
/* 245 */     this.randomMotionVecX = randomMotionVecXIn;
/* 246 */     this.randomMotionVecY = randomMotionVecYIn;
/* 247 */     this.randomMotionVecZ = randomMotionVecZIn;
/*     */   }
/*     */   
/*     */   public boolean func_175567_n()
/*     */   {
/* 252 */     return (this.randomMotionVecX != 0.0F) || (this.randomMotionVecY != 0.0F) || (this.randomMotionVecZ != 0.0F);
/*     */   }
/*     */   
/*     */   static class AIMoveRandom extends EntityAIBase
/*     */   {
/*     */     private EntitySquid squid;
/*     */     
/*     */     public AIMoveRandom(EntitySquid p_i45859_1_)
/*     */     {
/* 261 */       this.squid = p_i45859_1_;
/*     */     }
/*     */     
/*     */     public boolean shouldExecute()
/*     */     {
/* 266 */       return true;
/*     */     }
/*     */     
/*     */     public void updateTask()
/*     */     {
/* 271 */       int i = this.squid.getAge();
/*     */       
/* 273 */       if (i > 100)
/*     */       {
/* 275 */         this.squid.func_175568_b(0.0F, 0.0F, 0.0F);
/*     */       }
/* 277 */       else if ((this.squid.getRNG().nextInt(50) == 0) || (!this.squid.inWater) || (!this.squid.func_175567_n()))
/*     */       {
/* 279 */         float f = this.squid.getRNG().nextFloat() * 3.1415927F * 2.0F;
/* 280 */         float f1 = MathHelper.cos(f) * 0.2F;
/* 281 */         float f2 = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;
/* 282 */         float f3 = MathHelper.sin(f) * 0.2F;
/* 283 */         this.squid.func_175568_b(f1, f2, f3);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntitySquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */