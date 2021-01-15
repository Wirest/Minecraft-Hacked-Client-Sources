/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFurnace;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerFurnace;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.ISidedInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemHoe;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory
/*     */ {
/*  30 */   private static final int[] slotsTop = new int[1];
/*  31 */   private static final int[] slotsBottom = { 2, 1 };
/*  32 */   private static final int[] slotsSides = { 1 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  37 */   private ItemStack[] furnaceItemStacks = new ItemStack[3];
/*     */   
/*     */ 
/*     */   private int furnaceBurnTime;
/*     */   
/*     */ 
/*     */   private int currentItemBurnTime;
/*     */   
/*     */ 
/*     */   private int cookTime;
/*     */   
/*     */   private int totalCookTime;
/*     */   
/*     */   private String furnaceCustomName;
/*     */   
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  55 */     return this.furnaceItemStacks.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/*  63 */     return this.furnaceItemStacks[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/*  71 */     if (this.furnaceItemStacks[index] != null)
/*     */     {
/*  73 */       if (this.furnaceItemStacks[index].stackSize <= count)
/*     */       {
/*  75 */         ItemStack itemstack1 = this.furnaceItemStacks[index];
/*  76 */         this.furnaceItemStacks[index] = null;
/*  77 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/*  81 */       ItemStack itemstack = this.furnaceItemStacks[index].splitStack(count);
/*     */       
/*  83 */       if (this.furnaceItemStacks[index].stackSize == 0)
/*     */       {
/*  85 */         this.furnaceItemStacks[index] = null;
/*     */       }
/*     */       
/*  88 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  93 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 102 */     if (this.furnaceItemStacks[index] != null)
/*     */     {
/* 104 */       ItemStack itemstack = this.furnaceItemStacks[index];
/* 105 */       this.furnaceItemStacks[index] = null;
/* 106 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 110 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 119 */     boolean flag = (stack != null) && (stack.isItemEqual(this.furnaceItemStacks[index])) && (ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks[index]));
/* 120 */     this.furnaceItemStacks[index] = stack;
/*     */     
/* 122 */     if ((stack != null) && (stack.stackSize > getInventoryStackLimit()))
/*     */     {
/* 124 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 127 */     if ((index == 0) && (!flag))
/*     */     {
/* 129 */       this.totalCookTime = getCookTime(stack);
/* 130 */       this.cookTime = 0;
/* 131 */       markDirty();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 140 */     return hasCustomName() ? this.furnaceCustomName : "container.furnace";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 148 */     return (this.furnaceCustomName != null) && (this.furnaceCustomName.length() > 0);
/*     */   }
/*     */   
/*     */   public void setCustomInventoryName(String p_145951_1_)
/*     */   {
/* 153 */     this.furnaceCustomName = p_145951_1_;
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/* 158 */     super.readFromNBT(compound);
/* 159 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 160 */     this.furnaceItemStacks = new ItemStack[getSizeInventory()];
/*     */     
/* 162 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 164 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 165 */       int j = nbttagcompound.getByte("Slot");
/*     */       
/* 167 */       if ((j >= 0) && (j < this.furnaceItemStacks.length))
/*     */       {
/* 169 */         this.furnaceItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 173 */     this.furnaceBurnTime = compound.getShort("BurnTime");
/* 174 */     this.cookTime = compound.getShort("CookTime");
/* 175 */     this.totalCookTime = compound.getShort("CookTimeTotal");
/* 176 */     this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
/*     */     
/* 178 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 180 */       this.furnaceCustomName = compound.getString("CustomName");
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/* 186 */     super.writeToNBT(compound);
/* 187 */     compound.setShort("BurnTime", (short)this.furnaceBurnTime);
/* 188 */     compound.setShort("CookTime", (short)this.cookTime);
/* 189 */     compound.setShort("CookTimeTotal", (short)this.totalCookTime);
/* 190 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 192 */     for (int i = 0; i < this.furnaceItemStacks.length; i++)
/*     */     {
/* 194 */       if (this.furnaceItemStacks[i] != null)
/*     */       {
/* 196 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 197 */         nbttagcompound.setByte("Slot", (byte)i);
/* 198 */         this.furnaceItemStacks[i].writeToNBT(nbttagcompound);
/* 199 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 203 */     compound.setTag("Items", nbttaglist);
/*     */     
/* 205 */     if (hasCustomName())
/*     */     {
/* 207 */       compound.setString("CustomName", this.furnaceCustomName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 216 */     return 64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBurning()
/*     */   {
/* 224 */     return this.furnaceBurnTime > 0;
/*     */   }
/*     */   
/*     */   public static boolean isBurning(IInventory p_174903_0_)
/*     */   {
/* 229 */     return p_174903_0_.getField(0) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/* 237 */     boolean flag = isBurning();
/* 238 */     boolean flag1 = false;
/*     */     
/* 240 */     if (isBurning())
/*     */     {
/* 242 */       this.furnaceBurnTime -= 1;
/*     */     }
/*     */     
/* 245 */     if (!this.worldObj.isRemote)
/*     */     {
/* 247 */       if ((isBurning()) || ((this.furnaceItemStacks[1] != null) && (this.furnaceItemStacks[0] != null)))
/*     */       {
/* 249 */         if ((!isBurning()) && (canSmelt()))
/*     */         {
/* 251 */           this.currentItemBurnTime = (this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]));
/*     */           
/* 253 */           if (isBurning())
/*     */           {
/* 255 */             flag1 = true;
/*     */             
/* 257 */             if (this.furnaceItemStacks[1] != null)
/*     */             {
/* 259 */               this.furnaceItemStacks[1].stackSize -= 1;
/*     */               
/* 261 */               if (this.furnaceItemStacks[1].stackSize == 0)
/*     */               {
/* 263 */                 Item item = this.furnaceItemStacks[1].getItem().getContainerItem();
/* 264 */                 this.furnaceItemStacks[1] = (item != null ? new ItemStack(item) : null);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 270 */         if ((isBurning()) && (canSmelt()))
/*     */         {
/* 272 */           this.cookTime += 1;
/*     */           
/* 274 */           if (this.cookTime == this.totalCookTime)
/*     */           {
/* 276 */             this.cookTime = 0;
/* 277 */             this.totalCookTime = getCookTime(this.furnaceItemStacks[0]);
/* 278 */             smeltItem();
/* 279 */             flag1 = true;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 284 */           this.cookTime = 0;
/*     */         }
/*     */       }
/* 287 */       else if ((!isBurning()) && (this.cookTime > 0))
/*     */       {
/* 289 */         this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
/*     */       }
/*     */       
/* 292 */       if (flag != isBurning())
/*     */       {
/* 294 */         flag1 = true;
/* 295 */         BlockFurnace.setState(isBurning(), this.worldObj, this.pos);
/*     */       }
/*     */     }
/*     */     
/* 299 */     if (flag1)
/*     */     {
/* 301 */       markDirty();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getCookTime(ItemStack stack)
/*     */   {
/* 307 */     return 200;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean canSmelt()
/*     */   {
/* 315 */     if (this.furnaceItemStacks[0] == null)
/*     */     {
/* 317 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 321 */     ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
/* 322 */     return itemstack != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void smeltItem()
/*     */   {
/* 331 */     if (canSmelt())
/*     */     {
/* 333 */       ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
/*     */       
/* 335 */       if (this.furnaceItemStacks[2] == null)
/*     */       {
/* 337 */         this.furnaceItemStacks[2] = itemstack.copy();
/*     */       }
/* 339 */       else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem())
/*     */       {
/* 341 */         this.furnaceItemStacks[2].stackSize += 1;
/*     */       }
/*     */       
/* 344 */       if ((this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge)) && (this.furnaceItemStacks[0].getMetadata() == 1) && (this.furnaceItemStacks[1] != null) && (this.furnaceItemStacks[1].getItem() == Items.bucket))
/*     */       {
/* 346 */         this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
/*     */       }
/*     */       
/* 349 */       this.furnaceItemStacks[0].stackSize -= 1;
/*     */       
/* 351 */       if (this.furnaceItemStacks[0].stackSize <= 0)
/*     */       {
/* 353 */         this.furnaceItemStacks[0] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getItemBurnTime(ItemStack p_145952_0_)
/*     */   {
/* 364 */     if (p_145952_0_ == null)
/*     */     {
/* 366 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 370 */     Item item = p_145952_0_.getItem();
/*     */     
/* 372 */     if (((item instanceof ItemBlock)) && (Block.getBlockFromItem(item) != Blocks.air))
/*     */     {
/* 374 */       Block block = Block.getBlockFromItem(item);
/*     */       
/* 376 */       if (block == Blocks.wooden_slab)
/*     */       {
/* 378 */         return 150;
/*     */       }
/*     */       
/* 381 */       if (block.getMaterial() == Material.wood)
/*     */       {
/* 383 */         return 300;
/*     */       }
/*     */       
/* 386 */       if (block == Blocks.coal_block)
/*     */       {
/* 388 */         return 16000;
/*     */       }
/*     */     }
/*     */     
/* 392 */     return item == Items.blaze_rod ? 2400 : item == Item.getItemFromBlock(Blocks.sapling) ? 100 : item == Items.lava_bucket ? 20000 : item == Items.coal ? 1600 : item == Items.stick ? 100 : ((item instanceof ItemHoe)) && (((ItemHoe)item).getMaterialName().equals("WOOD")) ? 200 : ((item instanceof ItemSword)) && (((ItemSword)item).getToolMaterialName().equals("WOOD")) ? 200 : ((item instanceof ItemTool)) && (((ItemTool)item).getToolMaterialName().equals("WOOD")) ? 200 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isItemFuel(ItemStack p_145954_0_)
/*     */   {
/* 398 */     return getItemBurnTime(p_145954_0_) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 406 */     return this.worldObj.getTileEntity(this.pos) == this;
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
/* 422 */     return index != 2;
/*     */   }
/*     */   
/*     */   public int[] getSlotsForFace(EnumFacing side)
/*     */   {
/* 427 */     return side == EnumFacing.UP ? slotsTop : side == EnumFacing.DOWN ? slotsBottom : slotsSides;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
/*     */   {
/* 436 */     return isItemValidForSlot(index, itemStackIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
/*     */   {
/* 445 */     if ((direction == EnumFacing.DOWN) && (index == 1))
/*     */     {
/* 447 */       Item item = stack.getItem();
/*     */       
/* 449 */       if ((item != Items.water_bucket) && (item != Items.bucket))
/*     */       {
/* 451 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 455 */     return true;
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 460 */     return "minecraft:furnace";
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 465 */     return new ContainerFurnace(playerInventory, this);
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 470 */     switch (id)
/*     */     {
/*     */     case 0: 
/* 473 */       return this.furnaceBurnTime;
/*     */     
/*     */     case 1: 
/* 476 */       return this.currentItemBurnTime;
/*     */     
/*     */     case 2: 
/* 479 */       return this.cookTime;
/*     */     
/*     */     case 3: 
/* 482 */       return this.totalCookTime;
/*     */     }
/*     */     
/* 485 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value)
/*     */   {
/* 491 */     switch (id)
/*     */     {
/*     */     case 0: 
/* 494 */       this.furnaceBurnTime = value;
/* 495 */       break;
/*     */     
/*     */     case 1: 
/* 498 */       this.currentItemBurnTime = value;
/* 499 */       break;
/*     */     
/*     */     case 2: 
/* 502 */       this.cookTime = value;
/* 503 */       break;
/*     */     
/*     */     case 3: 
/* 506 */       this.totalCookTime = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getFieldCount()
/*     */   {
/* 512 */     return 4;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 517 */     for (int i = 0; i < this.furnaceItemStacks.length; i++)
/*     */     {
/* 519 */       this.furnaceItemStacks[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */