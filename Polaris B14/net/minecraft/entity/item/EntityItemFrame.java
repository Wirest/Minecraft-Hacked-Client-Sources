/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityHanging;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemMap;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ 
/*     */ public class EntityItemFrame extends EntityHanging
/*     */ {
/*  20 */   private float itemDropChance = 1.0F;
/*     */   
/*     */   public EntityItemFrame(World worldIn)
/*     */   {
/*  24 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public EntityItemFrame(World worldIn, net.minecraft.util.BlockPos p_i45852_2_, EnumFacing p_i45852_3_)
/*     */   {
/*  29 */     super(worldIn, p_i45852_2_);
/*  30 */     updateFacingWithBoundingBox(p_i45852_3_);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  35 */     getDataWatcher().addObjectByDataType(8, 5);
/*  36 */     getDataWatcher().addObject(9, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public float getCollisionBorderSize()
/*     */   {
/*  41 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  49 */     if (isEntityInvulnerable(source))
/*     */     {
/*  51 */       return false;
/*     */     }
/*  53 */     if ((!source.isExplosion()) && (getDisplayedItem() != null))
/*     */     {
/*  55 */       if (!this.worldObj.isRemote)
/*     */       {
/*  57 */         dropItemOrSelf(source.getEntity(), false);
/*  58 */         setDisplayedItem(null);
/*     */       }
/*     */       
/*  61 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  65 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getWidthPixels()
/*     */   {
/*  71 */     return 12;
/*     */   }
/*     */   
/*     */   public int getHeightPixels()
/*     */   {
/*  76 */     return 12;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInRangeToRenderDist(double distance)
/*     */   {
/*  85 */     double d0 = 16.0D;
/*  86 */     d0 = d0 * 64.0D * this.renderDistanceWeight;
/*  87 */     return distance < d0 * d0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBroken(Entity brokenEntity)
/*     */   {
/*  95 */     dropItemOrSelf(brokenEntity, true);
/*     */   }
/*     */   
/*     */   public void dropItemOrSelf(Entity p_146065_1_, boolean p_146065_2_)
/*     */   {
/* 100 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */     {
/* 102 */       ItemStack itemstack = getDisplayedItem();
/*     */       
/* 104 */       if ((p_146065_1_ instanceof EntityPlayer))
/*     */       {
/* 106 */         EntityPlayer entityplayer = (EntityPlayer)p_146065_1_;
/*     */         
/* 108 */         if (entityplayer.capabilities.isCreativeMode)
/*     */         {
/* 110 */           removeFrameFromMap(itemstack);
/* 111 */           return;
/*     */         }
/*     */       }
/*     */       
/* 115 */       if (p_146065_2_)
/*     */       {
/* 117 */         entityDropItem(new ItemStack(Items.item_frame), 0.0F);
/*     */       }
/*     */       
/* 120 */       if ((itemstack != null) && (this.rand.nextFloat() < this.itemDropChance))
/*     */       {
/* 122 */         itemstack = itemstack.copy();
/* 123 */         removeFrameFromMap(itemstack);
/* 124 */         entityDropItem(itemstack, 0.0F);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeFrameFromMap(ItemStack p_110131_1_)
/*     */   {
/* 134 */     if (p_110131_1_ != null)
/*     */     {
/* 136 */       if (p_110131_1_.getItem() == Items.filled_map)
/*     */       {
/* 138 */         MapData mapdata = ((ItemMap)p_110131_1_.getItem()).getMapData(p_110131_1_, this.worldObj);
/* 139 */         mapdata.mapDecorations.remove("frame-" + getEntityId());
/*     */       }
/*     */       
/* 142 */       p_110131_1_.setItemFrame(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemStack getDisplayedItem()
/*     */   {
/* 148 */     return getDataWatcher().getWatchableObjectItemStack(8);
/*     */   }
/*     */   
/*     */   public void setDisplayedItem(ItemStack p_82334_1_)
/*     */   {
/* 153 */     setDisplayedItemWithUpdate(p_82334_1_, true);
/*     */   }
/*     */   
/*     */   private void setDisplayedItemWithUpdate(ItemStack p_174864_1_, boolean p_174864_2_)
/*     */   {
/* 158 */     if (p_174864_1_ != null)
/*     */     {
/* 160 */       p_174864_1_ = p_174864_1_.copy();
/* 161 */       p_174864_1_.stackSize = 1;
/* 162 */       p_174864_1_.setItemFrame(this);
/*     */     }
/*     */     
/* 165 */     getDataWatcher().updateObject(8, p_174864_1_);
/* 166 */     getDataWatcher().setObjectWatched(8);
/*     */     
/* 168 */     if ((p_174864_2_) && (this.hangingPosition != null))
/*     */     {
/* 170 */       this.worldObj.updateComparatorOutputLevel(this.hangingPosition, net.minecraft.init.Blocks.air);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRotation()
/*     */   {
/* 179 */     return getDataWatcher().getWatchableObjectByte(9);
/*     */   }
/*     */   
/*     */   public void setItemRotation(int p_82336_1_)
/*     */   {
/* 184 */     func_174865_a(p_82336_1_, true);
/*     */   }
/*     */   
/*     */   private void func_174865_a(int p_174865_1_, boolean p_174865_2_)
/*     */   {
/* 189 */     getDataWatcher().updateObject(9, Byte.valueOf((byte)(p_174865_1_ % 8)));
/*     */     
/* 191 */     if ((p_174865_2_) && (this.hangingPosition != null))
/*     */     {
/* 193 */       this.worldObj.updateComparatorOutputLevel(this.hangingPosition, net.minecraft.init.Blocks.air);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 202 */     if (getDisplayedItem() != null)
/*     */     {
/* 204 */       tagCompound.setTag("Item", getDisplayedItem().writeToNBT(new NBTTagCompound()));
/* 205 */       tagCompound.setByte("ItemRotation", (byte)getRotation());
/* 206 */       tagCompound.setFloat("ItemDropChance", this.itemDropChance);
/*     */     }
/*     */     
/* 209 */     super.writeEntityToNBT(tagCompound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 217 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Item");
/*     */     
/* 219 */     if ((nbttagcompound != null) && (!nbttagcompound.hasNoTags()))
/*     */     {
/* 221 */       setDisplayedItemWithUpdate(ItemStack.loadItemStackFromNBT(nbttagcompound), false);
/* 222 */       func_174865_a(tagCompund.getByte("ItemRotation"), false);
/*     */       
/* 224 */       if (tagCompund.hasKey("ItemDropChance", 99))
/*     */       {
/* 226 */         this.itemDropChance = tagCompund.getFloat("ItemDropChance");
/*     */       }
/*     */       
/* 229 */       if (tagCompund.hasKey("Direction"))
/*     */       {
/* 231 */         func_174865_a(getRotation() * 2, false);
/*     */       }
/*     */     }
/*     */     
/* 235 */     super.readEntityFromNBT(tagCompund);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interactFirst(EntityPlayer playerIn)
/*     */   {
/* 243 */     if (getDisplayedItem() == null)
/*     */     {
/* 245 */       ItemStack itemstack = playerIn.getHeldItem();
/*     */       
/* 247 */       if ((itemstack != null) && (!this.worldObj.isRemote))
/*     */       {
/* 249 */         setDisplayedItem(itemstack);
/*     */         
/* 251 */         if (!playerIn.capabilities.isCreativeMode) if (--itemstack.stackSize <= 0)
/*     */           {
/* 253 */             playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
/*     */           }
/*     */       }
/*     */     }
/* 257 */     else if (!this.worldObj.isRemote)
/*     */     {
/* 259 */       setItemRotation(getRotation() + 1);
/*     */     }
/*     */     
/* 262 */     return true;
/*     */   }
/*     */   
/*     */   public int func_174866_q()
/*     */   {
/* 267 */     return getDisplayedItem() == null ? 0 : getRotation() % 8 + 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */