/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockBrewingStand;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerBrewingStand;
/*     */ import net.minecraft.inventory.ISidedInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemPotion;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.potion.PotionHelper;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory
/*     */ {
/*  26 */   private static final int[] inputSlots = { 3 };
/*     */   
/*     */ 
/*  29 */   private static final int[] outputSlots = { 0, 1, 2 };
/*     */   
/*     */ 
/*  32 */   private ItemStack[] brewingItemStacks = new ItemStack[4];
/*     */   
/*     */ 
/*     */ 
/*     */   private int brewTime;
/*     */   
/*     */ 
/*     */   private boolean[] filledSlots;
/*     */   
/*     */ 
/*     */   private Item ingredientID;
/*     */   
/*     */ 
/*     */   private String customName;
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  51 */     return hasCustomName() ? this.customName : "container.brewing";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/*  59 */     return (this.customName != null) && (this.customName.length() > 0);
/*     */   }
/*     */   
/*     */   public void setName(String name)
/*     */   {
/*  64 */     this.customName = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  72 */     return this.brewingItemStacks.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/*  80 */     if (this.brewTime > 0)
/*     */     {
/*  82 */       this.brewTime -= 1;
/*     */       
/*  84 */       if (this.brewTime == 0)
/*     */       {
/*  86 */         brewPotions();
/*  87 */         markDirty();
/*     */       }
/*  89 */       else if (!canBrew())
/*     */       {
/*  91 */         this.brewTime = 0;
/*  92 */         markDirty();
/*     */       }
/*  94 */       else if (this.ingredientID != this.brewingItemStacks[3].getItem())
/*     */       {
/*  96 */         this.brewTime = 0;
/*  97 */         markDirty();
/*     */       }
/*     */     }
/* 100 */     else if (canBrew())
/*     */     {
/* 102 */       this.brewTime = 400;
/* 103 */       this.ingredientID = this.brewingItemStacks[3].getItem();
/*     */     }
/*     */     
/* 106 */     if (!this.worldObj.isRemote)
/*     */     {
/* 108 */       boolean[] aboolean = func_174902_m();
/*     */       
/* 110 */       if (!Arrays.equals(aboolean, this.filledSlots))
/*     */       {
/* 112 */         this.filledSlots = aboolean;
/* 113 */         IBlockState iblockstate = this.worldObj.getBlockState(getPos());
/*     */         
/* 115 */         if (!(iblockstate.getBlock() instanceof BlockBrewingStand))
/*     */         {
/* 117 */           return;
/*     */         }
/*     */         
/* 120 */         for (int i = 0; i < BlockBrewingStand.HAS_BOTTLE.length; i++)
/*     */         {
/* 122 */           iblockstate = iblockstate.withProperty(BlockBrewingStand.HAS_BOTTLE[i], Boolean.valueOf(aboolean[i]));
/*     */         }
/*     */         
/* 125 */         this.worldObj.setBlockState(this.pos, iblockstate, 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canBrew()
/*     */   {
/* 132 */     if ((this.brewingItemStacks[3] != null) && (this.brewingItemStacks[3].stackSize > 0))
/*     */     {
/* 134 */       ItemStack itemstack = this.brewingItemStacks[3];
/*     */       
/* 136 */       if (!itemstack.getItem().isPotionIngredient(itemstack))
/*     */       {
/* 138 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 142 */       boolean flag = false;
/*     */       
/* 144 */       for (int i = 0; i < 3; i++)
/*     */       {
/* 146 */         if ((this.brewingItemStacks[i] != null) && (this.brewingItemStacks[i].getItem() == Items.potionitem))
/*     */         {
/* 148 */           int j = this.brewingItemStacks[i].getMetadata();
/* 149 */           int k = getPotionResult(j, itemstack);
/*     */           
/* 151 */           if ((!ItemPotion.isSplash(j)) && (ItemPotion.isSplash(k)))
/*     */           {
/* 153 */             flag = true;
/* 154 */             break;
/*     */           }
/*     */           
/* 157 */           List<PotionEffect> list = Items.potionitem.getEffects(j);
/* 158 */           List<PotionEffect> list1 = Items.potionitem.getEffects(k);
/*     */           
/* 160 */           if (((j <= 0) || (list != list1)) && ((list == null) || ((!list.equals(list1)) && (list1 != null))) && (j != k))
/*     */           {
/* 162 */             flag = true;
/* 163 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 168 */       return flag;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 173 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private void brewPotions()
/*     */   {
/* 179 */     if (canBrew())
/*     */     {
/* 181 */       ItemStack itemstack = this.brewingItemStacks[3];
/*     */       
/* 183 */       for (int i = 0; i < 3; i++)
/*     */       {
/* 185 */         if ((this.brewingItemStacks[i] != null) && (this.brewingItemStacks[i].getItem() == Items.potionitem))
/*     */         {
/* 187 */           int j = this.brewingItemStacks[i].getMetadata();
/* 188 */           int k = getPotionResult(j, itemstack);
/* 189 */           List<PotionEffect> list = Items.potionitem.getEffects(j);
/* 190 */           List<PotionEffect> list1 = Items.potionitem.getEffects(k);
/*     */           
/* 192 */           if (((j > 0) && (list == list1)) || ((list != null) && ((list.equals(list1)) || (list1 == null))))
/*     */           {
/* 194 */             if ((!ItemPotion.isSplash(j)) && (ItemPotion.isSplash(k)))
/*     */             {
/* 196 */               this.brewingItemStacks[i].setItemDamage(k);
/*     */             }
/*     */           }
/* 199 */           else if (j != k)
/*     */           {
/* 201 */             this.brewingItemStacks[i].setItemDamage(k);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 206 */       if (itemstack.getItem().hasContainerItem())
/*     */       {
/* 208 */         this.brewingItemStacks[3] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */       else
/*     */       {
/* 212 */         this.brewingItemStacks[3].stackSize -= 1;
/*     */         
/* 214 */         if (this.brewingItemStacks[3].stackSize <= 0)
/*     */         {
/* 216 */           this.brewingItemStacks[3] = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getPotionResult(int meta, ItemStack stack)
/*     */   {
/* 227 */     return stack.getItem().isPotionIngredient(stack) ? PotionHelper.applyIngredient(meta, stack.getItem().getPotionEffect(stack)) : stack == null ? meta : meta;
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/* 232 */     super.readFromNBT(compound);
/* 233 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 234 */     this.brewingItemStacks = new ItemStack[getSizeInventory()];
/*     */     
/* 236 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 238 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 239 */       int j = nbttagcompound.getByte("Slot");
/*     */       
/* 241 */       if ((j >= 0) && (j < this.brewingItemStacks.length))
/*     */       {
/* 243 */         this.brewingItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 247 */     this.brewTime = compound.getShort("BrewTime");
/*     */     
/* 249 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 251 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/* 257 */     super.writeToNBT(compound);
/* 258 */     compound.setShort("BrewTime", (short)this.brewTime);
/* 259 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 261 */     for (int i = 0; i < this.brewingItemStacks.length; i++)
/*     */     {
/* 263 */       if (this.brewingItemStacks[i] != null)
/*     */       {
/* 265 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 266 */         nbttagcompound.setByte("Slot", (byte)i);
/* 267 */         this.brewingItemStacks[i].writeToNBT(nbttagcompound);
/* 268 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 272 */     compound.setTag("Items", nbttaglist);
/*     */     
/* 274 */     if (hasCustomName())
/*     */     {
/* 276 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/* 285 */     return (index >= 0) && (index < this.brewingItemStacks.length) ? this.brewingItemStacks[index] : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/* 293 */     if ((index >= 0) && (index < this.brewingItemStacks.length))
/*     */     {
/* 295 */       ItemStack itemstack = this.brewingItemStacks[index];
/* 296 */       this.brewingItemStacks[index] = null;
/* 297 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 301 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 310 */     if ((index >= 0) && (index < this.brewingItemStacks.length))
/*     */     {
/* 312 */       ItemStack itemstack = this.brewingItemStacks[index];
/* 313 */       this.brewingItemStacks[index] = null;
/* 314 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 318 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 327 */     if ((index >= 0) && (index < this.brewingItemStacks.length))
/*     */     {
/* 329 */       this.brewingItemStacks[index] = stack;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 338 */     return 64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 346 */     return this.worldObj.getTileEntity(this.pos) == this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void openInventory(EntityPlayer player) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void closeInventory(EntityPlayer player) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack)
/*     */   {
/* 362 */     return (stack.getItem() != Items.potionitem) && (stack.getItem() != Items.glass_bottle) ? false : index == 3 ? stack.getItem().isPotionIngredient(stack) : true;
/*     */   }
/*     */   
/*     */   public boolean[] func_174902_m()
/*     */   {
/* 367 */     boolean[] aboolean = new boolean[3];
/*     */     
/* 369 */     for (int i = 0; i < 3; i++)
/*     */     {
/* 371 */       if (this.brewingItemStacks[i] != null)
/*     */       {
/* 373 */         aboolean[i] = true;
/*     */       }
/*     */     }
/*     */     
/* 377 */     return aboolean;
/*     */   }
/*     */   
/*     */   public int[] getSlotsForFace(EnumFacing side)
/*     */   {
/* 382 */     return side == EnumFacing.UP ? inputSlots : outputSlots;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
/*     */   {
/* 391 */     return isItemValidForSlot(index, itemStackIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
/*     */   {
/* 400 */     return true;
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 405 */     return "minecraft:brewing_stand";
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 410 */     return new ContainerBrewingStand(playerInventory, this);
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 415 */     switch (id)
/*     */     {
/*     */     case 0: 
/* 418 */       return this.brewTime;
/*     */     }
/*     */     
/* 421 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value)
/*     */   {
/* 427 */     switch (id)
/*     */     {
/*     */     case 0: 
/* 430 */       this.brewTime = value;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 438 */     return 1;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 443 */     for (int i = 0; i < this.brewingItemStacks.length; i++)
/*     */     {
/* 445 */       this.brewingItemStacks[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityBrewingStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */