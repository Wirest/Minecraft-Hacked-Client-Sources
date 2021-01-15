/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityAgeable extends EntityCreature
/*     */ {
/*     */   protected int growingAge;
/*     */   protected int field_175502_b;
/*     */   protected int field_175503_c;
/*  16 */   private float ageWidth = -1.0F;
/*     */   private float ageHeight;
/*     */   
/*     */   public EntityAgeable(World worldIn)
/*     */   {
/*  21 */     super(worldIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract EntityAgeable createChild(EntityAgeable paramEntityAgeable);
/*     */   
/*     */ 
/*     */   public boolean interact(EntityPlayer player)
/*     */   {
/*  31 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/*  33 */     if ((itemstack != null) && (itemstack.getItem() == net.minecraft.init.Items.spawn_egg))
/*     */     {
/*  35 */       if (!this.worldObj.isRemote)
/*     */       {
/*  37 */         Class<? extends Entity> oclass = EntityList.getClassFromID(itemstack.getMetadata());
/*     */         
/*  39 */         if ((oclass != null) && (getClass() == oclass))
/*     */         {
/*  41 */           EntityAgeable entityageable = createChild(this);
/*     */           
/*  43 */           if (entityageable != null)
/*     */           {
/*  45 */             entityageable.setGrowingAge(41536);
/*  46 */             entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
/*  47 */             this.worldObj.spawnEntityInWorld(entityageable);
/*     */             
/*  49 */             if (itemstack.hasDisplayName())
/*     */             {
/*  51 */               entityageable.setCustomNameTag(itemstack.getDisplayName());
/*     */             }
/*     */             
/*  54 */             if (!player.capabilities.isCreativeMode)
/*     */             {
/*  56 */               itemstack.stackSize -= 1;
/*     */               
/*  58 */               if (itemstack.stackSize <= 0)
/*     */               {
/*  60 */                 player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  67 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  71 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void entityInit()
/*     */   {
/*  77 */     super.entityInit();
/*  78 */     this.dataWatcher.addObject(12, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getGrowingAge()
/*     */   {
/*  88 */     return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.growingAge;
/*     */   }
/*     */   
/*     */   public void func_175501_a(int p_175501_1_, boolean p_175501_2_)
/*     */   {
/*  93 */     int i = getGrowingAge();
/*  94 */     int j = i;
/*  95 */     i += p_175501_1_ * 20;
/*     */     
/*  97 */     if (i > 0)
/*     */     {
/*  99 */       i = 0;
/*     */       
/* 101 */       if (j < 0)
/*     */       {
/* 103 */         onGrowingAdult();
/*     */       }
/*     */     }
/*     */     
/* 107 */     int k = i - j;
/* 108 */     setGrowingAge(i);
/*     */     
/* 110 */     if (p_175501_2_)
/*     */     {
/* 112 */       this.field_175502_b += k;
/*     */       
/* 114 */       if (this.field_175503_c == 0)
/*     */       {
/* 116 */         this.field_175503_c = 40;
/*     */       }
/*     */     }
/*     */     
/* 120 */     if (getGrowingAge() == 0)
/*     */     {
/* 122 */       setGrowingAge(this.field_175502_b);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addGrowth(int growth)
/*     */   {
/* 132 */     func_175501_a(growth, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGrowingAge(int age)
/*     */   {
/* 141 */     this.dataWatcher.updateObject(12, Byte.valueOf((byte)net.minecraft.util.MathHelper.clamp_int(age, -1, 1)));
/* 142 */     this.growingAge = age;
/* 143 */     setScaleForAge(isChild());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 151 */     super.writeEntityToNBT(tagCompound);
/* 152 */     tagCompound.setInteger("Age", getGrowingAge());
/* 153 */     tagCompound.setInteger("ForcedAge", this.field_175502_b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 161 */     super.readEntityFromNBT(tagCompund);
/* 162 */     setGrowingAge(tagCompund.getInteger("Age"));
/* 163 */     this.field_175502_b = tagCompund.getInteger("ForcedAge");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/* 172 */     super.onLivingUpdate();
/*     */     
/* 174 */     if (this.worldObj.isRemote)
/*     */     {
/* 176 */       if (this.field_175503_c > 0)
/*     */       {
/* 178 */         if (this.field_175503_c % 4 == 0)
/*     */         {
/* 180 */           this.worldObj.spawnParticle(net.minecraft.util.EnumParticleTypes.VILLAGER_HAPPY, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */         
/* 183 */         this.field_175503_c -= 1;
/*     */       }
/*     */       
/* 186 */       setScaleForAge(isChild());
/*     */     }
/*     */     else
/*     */     {
/* 190 */       int i = getGrowingAge();
/*     */       
/* 192 */       if (i < 0)
/*     */       {
/* 194 */         i++;
/* 195 */         setGrowingAge(i);
/*     */         
/* 197 */         if (i == 0)
/*     */         {
/* 199 */           onGrowingAdult();
/*     */         }
/*     */       }
/* 202 */       else if (i > 0)
/*     */       {
/* 204 */         i--;
/* 205 */         setGrowingAge(i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onGrowingAdult() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isChild()
/*     */   {
/* 223 */     return getGrowingAge() < 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScaleForAge(boolean p_98054_1_)
/*     */   {
/* 231 */     setScale(p_98054_1_ ? 0.5F : 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void setSize(float width, float height)
/*     */   {
/* 239 */     boolean flag = this.ageWidth > 0.0F;
/* 240 */     this.ageWidth = width;
/* 241 */     this.ageHeight = height;
/*     */     
/* 243 */     if (!flag)
/*     */     {
/* 245 */       setScale(1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void setScale(float scale)
/*     */   {
/* 251 */     super.setSize(this.ageWidth * scale, this.ageHeight * scale);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityAgeable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */