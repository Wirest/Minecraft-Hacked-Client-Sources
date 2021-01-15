/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIArrowAttack;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemPotion;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWitch extends EntityMob implements IRangedAttackMob
/*     */ {
/*  32 */   private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
/*  33 */   private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0).setSaved(false);
/*     */   
/*     */ 
/*  36 */   private static final Item[] witchDrops = { Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick };
/*     */   
/*     */ 
/*     */ 
/*     */   private int witchAttackTimer;
/*     */   
/*     */ 
/*     */ 
/*     */   public EntityWitch(World worldIn)
/*     */   {
/*  46 */     super(worldIn);
/*  47 */     setSize(0.6F, 1.95F);
/*  48 */     this.tasks.addTask(1, new net.minecraft.entity.ai.EntityAISwimming(this));
/*  49 */     this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
/*  50 */     this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
/*  51 */     this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/*  52 */     this.tasks.addTask(3, new net.minecraft.entity.ai.EntityAILookIdle(this));
/*  53 */     this.targetTasks.addTask(1, new net.minecraft.entity.ai.EntityAIHurtByTarget(this, false, new Class[0]));
/*  54 */     this.targetTasks.addTask(2, new net.minecraft.entity.ai.EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  59 */     super.entityInit();
/*  60 */     getDataWatcher().addObject(21, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getLivingSound()
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHurtSound()
/*     */   {
/*  76 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDeathSound()
/*     */   {
/*  84 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAggressive(boolean aggressive)
/*     */   {
/*  92 */     getDataWatcher().updateObject(21, Byte.valueOf((byte)(aggressive ? 1 : 0)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAggressive()
/*     */   {
/* 100 */     return getDataWatcher().getWatchableObjectByte(21) == 1;
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes()
/*     */   {
/* 105 */     super.applyEntityAttributes();
/* 106 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
/* 107 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 116 */     if (!this.worldObj.isRemote)
/*     */     {
/* 118 */       if (getAggressive())
/*     */       {
/* 120 */         if (this.witchAttackTimer-- <= 0)
/*     */         {
/* 122 */           setAggressive(false);
/* 123 */           ItemStack itemstack = getHeldItem();
/* 124 */           setCurrentItemOrArmor(0, null);
/*     */           
/* 126 */           if ((itemstack != null) && (itemstack.getItem() == Items.potionitem))
/*     */           {
/* 128 */             java.util.List<PotionEffect> list = Items.potionitem.getEffects(itemstack);
/*     */             
/* 130 */             if (list != null)
/*     */             {
/* 132 */               for (PotionEffect potioneffect : list)
/*     */               {
/* 134 */                 addPotionEffect(new PotionEffect(potioneffect));
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 139 */           getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(MODIFIER);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 144 */         int i = -1;
/*     */         
/* 146 */         if ((this.rand.nextFloat() < 0.15F) && (isInsideOfMaterial(Material.water)) && (!isPotionActive(Potion.waterBreathing)))
/*     */         {
/* 148 */           i = 8237;
/*     */         }
/* 150 */         else if ((this.rand.nextFloat() < 0.15F) && (isBurning()) && (!isPotionActive(Potion.fireResistance)))
/*     */         {
/* 152 */           i = 16307;
/*     */         }
/* 154 */         else if ((this.rand.nextFloat() < 0.05F) && (getHealth() < getMaxHealth()))
/*     */         {
/* 156 */           i = 16341;
/*     */         }
/* 158 */         else if ((this.rand.nextFloat() < 0.25F) && (getAttackTarget() != null) && (!isPotionActive(Potion.moveSpeed)) && (getAttackTarget().getDistanceSqToEntity(this) > 121.0D))
/*     */         {
/* 160 */           i = 16274;
/*     */         }
/* 162 */         else if ((this.rand.nextFloat() < 0.25F) && (getAttackTarget() != null) && (!isPotionActive(Potion.moveSpeed)) && (getAttackTarget().getDistanceSqToEntity(this) > 121.0D))
/*     */         {
/* 164 */           i = 16274;
/*     */         }
/*     */         
/* 167 */         if (i > -1)
/*     */         {
/* 169 */           setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, i));
/* 170 */           this.witchAttackTimer = getHeldItem().getMaxItemUseDuration();
/* 171 */           setAggressive(true);
/* 172 */           IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 173 */           iattributeinstance.removeModifier(MODIFIER);
/* 174 */           iattributeinstance.applyModifier(MODIFIER);
/*     */         }
/*     */       }
/*     */       
/* 178 */       if (this.rand.nextFloat() < 7.5E-4F)
/*     */       {
/* 180 */         this.worldObj.setEntityState(this, (byte)15);
/*     */       }
/*     */     }
/*     */     
/* 184 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 189 */     if (id == 15)
/*     */     {
/* 191 */       for (int i = 0; i < this.rand.nextInt(35) + 10; i++)
/*     */       {
/* 193 */         this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 198 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float applyPotionDamageCalculations(DamageSource source, float damage)
/*     */   {
/* 207 */     damage = super.applyPotionDamageCalculations(source, damage);
/*     */     
/* 209 */     if (source.getEntity() == this)
/*     */     {
/* 211 */       damage = 0.0F;
/*     */     }
/*     */     
/* 214 */     if (source.isMagicDamage())
/*     */     {
/* 216 */       damage = (float)(damage * 0.15D);
/*     */     }
/*     */     
/* 219 */     return damage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
/*     */   {
/* 227 */     int i = this.rand.nextInt(3) + 1;
/*     */     
/* 229 */     for (int j = 0; j < i; j++)
/*     */     {
/* 231 */       int k = this.rand.nextInt(3);
/* 232 */       Item item = witchDrops[this.rand.nextInt(witchDrops.length)];
/*     */       
/* 234 */       if (p_70628_2_ > 0)
/*     */       {
/* 236 */         k += this.rand.nextInt(p_70628_2_ + 1);
/*     */       }
/*     */       
/* 239 */       for (int l = 0; l < k; l++)
/*     */       {
/* 241 */         dropItem(item, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
/*     */   {
/* 251 */     if (!getAggressive())
/*     */     {
/* 253 */       EntityPotion entitypotion = new EntityPotion(this.worldObj, this, 32732);
/* 254 */       double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D;
/* 255 */       entitypotion.rotationPitch -= -20.0F;
/* 256 */       double d1 = p_82196_1_.posX + p_82196_1_.motionX - this.posX;
/* 257 */       double d2 = d0 - this.posY;
/* 258 */       double d3 = p_82196_1_.posZ + p_82196_1_.motionZ - this.posZ;
/* 259 */       float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
/*     */       
/* 261 */       if ((f >= 8.0F) && (!p_82196_1_.isPotionActive(Potion.moveSlowdown)))
/*     */       {
/* 263 */         entitypotion.setPotionDamage(32698);
/*     */       }
/* 265 */       else if ((p_82196_1_.getHealth() >= 8.0F) && (!p_82196_1_.isPotionActive(Potion.poison)))
/*     */       {
/* 267 */         entitypotion.setPotionDamage(32660);
/*     */       }
/* 269 */       else if ((f <= 3.0F) && (!p_82196_1_.isPotionActive(Potion.weakness)) && (this.rand.nextFloat() < 0.25F))
/*     */       {
/* 271 */         entitypotion.setPotionDamage(32696);
/*     */       }
/*     */       
/* 274 */       entitypotion.setThrowableHeading(d1, d2 + f * 0.2F, d3, 0.75F, 8.0F);
/* 275 */       this.worldObj.spawnEntityInWorld(entitypotion);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 281 */     return 1.62F;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\monster\EntityWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */