/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemPotion;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class EntityPotion
/*     */   extends EntityThrowable
/*     */ {
/*     */   private ItemStack potionDamage;
/*     */   
/*     */   public EntityPotion(World worldIn)
/*     */   {
/*  24 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public EntityPotion(World worldIn, EntityLivingBase throwerIn, int meta)
/*     */   {
/*  29 */     this(worldIn, throwerIn, new ItemStack(Items.potionitem, 1, meta));
/*     */   }
/*     */   
/*     */   public EntityPotion(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn)
/*     */   {
/*  34 */     super(worldIn, throwerIn);
/*  35 */     this.potionDamage = potionDamageIn;
/*     */   }
/*     */   
/*     */   public EntityPotion(World worldIn, double x, double y, double z, int p_i1791_8_)
/*     */   {
/*  40 */     this(worldIn, x, y, z, new ItemStack(Items.potionitem, 1, p_i1791_8_));
/*     */   }
/*     */   
/*     */   public EntityPotion(World worldIn, double x, double y, double z, ItemStack potionDamageIn)
/*     */   {
/*  45 */     super(worldIn, x, y, z);
/*  46 */     this.potionDamage = potionDamageIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getGravityVelocity()
/*     */   {
/*  54 */     return 0.05F;
/*     */   }
/*     */   
/*     */   protected float getVelocity()
/*     */   {
/*  59 */     return 0.5F;
/*     */   }
/*     */   
/*     */   protected float getInaccuracy()
/*     */   {
/*  64 */     return -20.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPotionDamage(int potionId)
/*     */   {
/*  72 */     if (this.potionDamage == null)
/*     */     {
/*  74 */       this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
/*     */     }
/*     */     
/*  77 */     this.potionDamage.setItemDamage(potionId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPotionDamage()
/*     */   {
/*  85 */     if (this.potionDamage == null)
/*     */     {
/*  87 */       this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
/*     */     }
/*     */     
/*  90 */     return this.potionDamage.getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onImpact(MovingObjectPosition p_70184_1_)
/*     */   {
/*  98 */     if (!this.worldObj.isRemote)
/*     */     {
/* 100 */       List<PotionEffect> list = Items.potionitem.getEffects(this.potionDamage);
/*     */       
/* 102 */       if ((list != null) && (!list.isEmpty()))
/*     */       {
/* 104 */         AxisAlignedBB axisalignedbb = getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
/* 105 */         List<EntityLivingBase> list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
/*     */         
/* 107 */         if (!list1.isEmpty())
/*     */         {
/* 109 */           for (EntityLivingBase entitylivingbase : list1)
/*     */           {
/* 111 */             double d0 = getDistanceSqToEntity(entitylivingbase);
/*     */             
/* 113 */             if (d0 < 16.0D)
/*     */             {
/* 115 */               double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
/*     */               
/* 117 */               if (entitylivingbase == p_70184_1_.entityHit)
/*     */               {
/* 119 */                 d1 = 1.0D;
/*     */               }
/*     */               
/* 122 */               for (PotionEffect potioneffect : list)
/*     */               {
/* 124 */                 int i = potioneffect.getPotionID();
/*     */                 
/* 126 */                 if (Potion.potionTypes[i].isInstant())
/*     */                 {
/* 128 */                   Potion.potionTypes[i].affectEntity(this, getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
/*     */                 }
/*     */                 else
/*     */                 {
/* 132 */                   int j = (int)(d1 * potioneffect.getDuration() + 0.5D);
/*     */                   
/* 134 */                   if (j > 20)
/*     */                   {
/* 136 */                     entitylivingbase.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 145 */       this.worldObj.playAuxSFX(2002, new BlockPos(this), getPotionDamage());
/* 146 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 155 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 157 */     if (tagCompund.hasKey("Potion", 10))
/*     */     {
/* 159 */       this.potionDamage = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("Potion"));
/*     */     }
/*     */     else
/*     */     {
/* 163 */       setPotionDamage(tagCompund.getInteger("potionValue"));
/*     */     }
/*     */     
/* 166 */     if (this.potionDamage == null)
/*     */     {
/* 168 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 177 */     super.writeEntityToNBT(tagCompound);
/*     */     
/* 179 */     if (this.potionDamage != null)
/*     */     {
/* 181 */       tagCompound.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntityPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */