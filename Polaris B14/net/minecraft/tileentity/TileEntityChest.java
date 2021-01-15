/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryLargeChest;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityChest extends TileEntityLockable implements ITickable, IInventory
/*     */ {
/*  21 */   private ItemStack[] chestContents = new ItemStack[27];
/*     */   
/*     */ 
/*     */   public boolean adjacentChestChecked;
/*     */   
/*     */ 
/*     */   public TileEntityChest adjacentChestZNeg;
/*     */   
/*     */ 
/*     */   public TileEntityChest adjacentChestXPos;
/*     */   
/*     */ 
/*     */   public TileEntityChest adjacentChestXNeg;
/*     */   
/*     */ 
/*     */   public TileEntityChest adjacentChestZPos;
/*     */   
/*     */ 
/*     */   public float lidAngle;
/*     */   
/*     */   public float prevLidAngle;
/*     */   
/*     */   public int numPlayersUsing;
/*     */   
/*     */   private int ticksSinceSync;
/*     */   
/*     */   private int cachedChestType;
/*     */   
/*     */   private String customName;
/*     */   
/*     */ 
/*     */   public TileEntityChest()
/*     */   {
/*  54 */     this.cachedChestType = -1;
/*     */   }
/*     */   
/*     */   public TileEntityChest(int chestType)
/*     */   {
/*  59 */     this.cachedChestType = chestType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  67 */     return 27;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/*  75 */     return this.chestContents[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/*  83 */     if (this.chestContents[index] != null)
/*     */     {
/*  85 */       if (this.chestContents[index].stackSize <= count)
/*     */       {
/*  87 */         ItemStack itemstack1 = this.chestContents[index];
/*  88 */         this.chestContents[index] = null;
/*  89 */         markDirty();
/*  90 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/*  94 */       ItemStack itemstack = this.chestContents[index].splitStack(count);
/*     */       
/*  96 */       if (this.chestContents[index].stackSize == 0)
/*     */       {
/*  98 */         this.chestContents[index] = null;
/*     */       }
/*     */       
/* 101 */       markDirty();
/* 102 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 107 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 116 */     if (this.chestContents[index] != null)
/*     */     {
/* 118 */       ItemStack itemstack = this.chestContents[index];
/* 119 */       this.chestContents[index] = null;
/* 120 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 124 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 133 */     this.chestContents[index] = stack;
/*     */     
/* 135 */     if ((stack != null) && (stack.stackSize > getInventoryStackLimit()))
/*     */     {
/* 137 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 140 */     markDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 148 */     return hasCustomName() ? this.customName : "container.chest";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 156 */     return (this.customName != null) && (this.customName.length() > 0);
/*     */   }
/*     */   
/*     */   public void setCustomName(String name)
/*     */   {
/* 161 */     this.customName = name;
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/* 166 */     super.readFromNBT(compound);
/* 167 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 168 */     this.chestContents = new ItemStack[getSizeInventory()];
/*     */     
/* 170 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 172 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */     
/* 175 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 177 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 178 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/*     */       
/* 180 */       if ((j >= 0) && (j < this.chestContents.length))
/*     */       {
/* 182 */         this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/* 189 */     super.writeToNBT(compound);
/* 190 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 192 */     for (int i = 0; i < this.chestContents.length; i++)
/*     */     {
/* 194 */       if (this.chestContents[i] != null)
/*     */       {
/* 196 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 197 */         nbttagcompound.setByte("Slot", (byte)i);
/* 198 */         this.chestContents[i].writeToNBT(nbttagcompound);
/* 199 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 203 */     compound.setTag("Items", nbttaglist);
/*     */     
/* 205 */     if (hasCustomName())
/*     */     {
/* 207 */       compound.setString("CustomName", this.customName);
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
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 224 */     return this.worldObj.getTileEntity(this.pos) == this;
/*     */   }
/*     */   
/*     */   public void updateContainingBlockInfo()
/*     */   {
/* 229 */     super.updateContainingBlockInfo();
/* 230 */     this.adjacentChestChecked = false;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_174910_a(TileEntityChest chestTe, EnumFacing side)
/*     */   {
/* 236 */     if (chestTe.isInvalid())
/*     */     {
/* 238 */       this.adjacentChestChecked = false;
/*     */     }
/* 240 */     else if (this.adjacentChestChecked)
/*     */     {
/* 242 */       switch (side)
/*     */       {
/*     */       case NORTH: 
/* 245 */         if (this.adjacentChestZNeg != chestTe)
/*     */         {
/* 247 */           this.adjacentChestChecked = false;
/*     */         }
/*     */         
/* 250 */         break;
/*     */       
/*     */       case SOUTH: 
/* 253 */         if (this.adjacentChestZPos != chestTe)
/*     */         {
/* 255 */           this.adjacentChestChecked = false;
/*     */         }
/*     */         
/* 258 */         break;
/*     */       
/*     */       case WEST: 
/* 261 */         if (this.adjacentChestXPos != chestTe)
/*     */         {
/* 263 */           this.adjacentChestChecked = false;
/*     */         }
/*     */         
/* 266 */         break;
/*     */       
/*     */       case UP: 
/* 269 */         if (this.adjacentChestXNeg != chestTe)
/*     */         {
/* 271 */           this.adjacentChestChecked = false;
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public void checkForAdjacentChests()
/*     */   {
/* 282 */     if (!this.adjacentChestChecked)
/*     */     {
/* 284 */       this.adjacentChestChecked = true;
/* 285 */       this.adjacentChestXNeg = getAdjacentChest(EnumFacing.WEST);
/* 286 */       this.adjacentChestXPos = getAdjacentChest(EnumFacing.EAST);
/* 287 */       this.adjacentChestZNeg = getAdjacentChest(EnumFacing.NORTH);
/* 288 */       this.adjacentChestZPos = getAdjacentChest(EnumFacing.SOUTH);
/*     */     }
/*     */   }
/*     */   
/*     */   protected TileEntityChest getAdjacentChest(EnumFacing side)
/*     */   {
/* 294 */     BlockPos blockpos = this.pos.offset(side);
/*     */     
/* 296 */     if (isChestAt(blockpos))
/*     */     {
/* 298 */       TileEntity tileentity = this.worldObj.getTileEntity(blockpos);
/*     */       
/* 300 */       if ((tileentity instanceof TileEntityChest))
/*     */       {
/* 302 */         TileEntityChest tileentitychest = (TileEntityChest)tileentity;
/* 303 */         tileentitychest.func_174910_a(this, side.getOpposite());
/* 304 */         return tileentitychest;
/*     */       }
/*     */     }
/*     */     
/* 308 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isChestAt(BlockPos posIn)
/*     */   {
/* 313 */     if (this.worldObj == null)
/*     */     {
/* 315 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 319 */     net.minecraft.block.Block block = this.worldObj.getBlockState(posIn).getBlock();
/* 320 */     return ((block instanceof BlockChest)) && (((BlockChest)block).chestType == getChestType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/* 329 */     checkForAdjacentChests();
/* 330 */     int i = this.pos.getX();
/* 331 */     int j = this.pos.getY();
/* 332 */     int k = this.pos.getZ();
/* 333 */     this.ticksSinceSync += 1;
/*     */     
/* 335 */     if ((!this.worldObj.isRemote) && (this.numPlayersUsing != 0) && ((this.ticksSinceSync + i + j + k) % 200 == 0))
/*     */     {
/* 337 */       this.numPlayersUsing = 0;
/* 338 */       float f = 5.0F;
/*     */       
/* 340 */       for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new net.minecraft.util.AxisAlignedBB(i - f, j - f, k - f, i + 1 + f, j + 1 + f, k + 1 + f)))
/*     */       {
/* 342 */         if ((entityplayer.openContainer instanceof ContainerChest))
/*     */         {
/* 344 */           IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();
/*     */           
/* 346 */           if ((iinventory == this) || (((iinventory instanceof InventoryLargeChest)) && (((InventoryLargeChest)iinventory).isPartOfLargeChest(this))))
/*     */           {
/* 348 */             this.numPlayersUsing += 1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 354 */     this.prevLidAngle = this.lidAngle;
/* 355 */     float f1 = 0.1F;
/*     */     
/* 357 */     if ((this.numPlayersUsing > 0) && (this.lidAngle == 0.0F) && (this.adjacentChestZNeg == null) && (this.adjacentChestXNeg == null))
/*     */     {
/* 359 */       double d1 = i + 0.5D;
/* 360 */       double d2 = k + 0.5D;
/*     */       
/* 362 */       if (this.adjacentChestZPos != null)
/*     */       {
/* 364 */         d2 += 0.5D;
/*     */       }
/*     */       
/* 367 */       if (this.adjacentChestXPos != null)
/*     */       {
/* 369 */         d1 += 0.5D;
/*     */       }
/*     */       
/* 372 */       this.worldObj.playSoundEffect(d1, j + 0.5D, d2, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */     }
/*     */     
/* 375 */     if (((this.numPlayersUsing == 0) && (this.lidAngle > 0.0F)) || ((this.numPlayersUsing > 0) && (this.lidAngle < 1.0F)))
/*     */     {
/* 377 */       float f2 = this.lidAngle;
/*     */       
/* 379 */       if (this.numPlayersUsing > 0)
/*     */       {
/* 381 */         this.lidAngle += f1;
/*     */       }
/*     */       else
/*     */       {
/* 385 */         this.lidAngle -= f1;
/*     */       }
/*     */       
/* 388 */       if (this.lidAngle > 1.0F)
/*     */       {
/* 390 */         this.lidAngle = 1.0F;
/*     */       }
/*     */       
/* 393 */       float f3 = 0.5F;
/*     */       
/* 395 */       if ((this.lidAngle < f3) && (f2 >= f3) && (this.adjacentChestZNeg == null) && (this.adjacentChestXNeg == null))
/*     */       {
/* 397 */         double d3 = i + 0.5D;
/* 398 */         double d0 = k + 0.5D;
/*     */         
/* 400 */         if (this.adjacentChestZPos != null)
/*     */         {
/* 402 */           d0 += 0.5D;
/*     */         }
/*     */         
/* 405 */         if (this.adjacentChestXPos != null)
/*     */         {
/* 407 */           d3 += 0.5D;
/*     */         }
/*     */         
/* 410 */         this.worldObj.playSoundEffect(d3, j + 0.5D, d0, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */       }
/*     */       
/* 413 */       if (this.lidAngle < 0.0F)
/*     */       {
/* 415 */         this.lidAngle = 0.0F;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean receiveClientEvent(int id, int type)
/*     */   {
/* 422 */     if (id == 1)
/*     */     {
/* 424 */       this.numPlayersUsing = type;
/* 425 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 429 */     return super.receiveClientEvent(id, type);
/*     */   }
/*     */   
/*     */ 
/*     */   public void openInventory(EntityPlayer player)
/*     */   {
/* 435 */     if (!player.isSpectator())
/*     */     {
/* 437 */       if (this.numPlayersUsing < 0)
/*     */       {
/* 439 */         this.numPlayersUsing = 0;
/*     */       }
/*     */       
/* 442 */       this.numPlayersUsing += 1;
/* 443 */       this.worldObj.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
/* 444 */       this.worldObj.notifyNeighborsOfStateChange(this.pos, getBlockType());
/* 445 */       this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), getBlockType());
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeInventory(EntityPlayer player)
/*     */   {
/* 451 */     if ((!player.isSpectator()) && ((getBlockType() instanceof BlockChest)))
/*     */     {
/* 453 */       this.numPlayersUsing -= 1;
/* 454 */       this.worldObj.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
/* 455 */       this.worldObj.notifyNeighborsOfStateChange(this.pos, getBlockType());
/* 456 */       this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), getBlockType());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack)
/*     */   {
/* 465 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 473 */     super.invalidate();
/* 474 */     updateContainingBlockInfo();
/* 475 */     checkForAdjacentChests();
/*     */   }
/*     */   
/*     */   public int getChestType()
/*     */   {
/* 480 */     if (this.cachedChestType == -1)
/*     */     {
/* 482 */       if ((this.worldObj == null) || (!(getBlockType() instanceof BlockChest)))
/*     */       {
/* 484 */         return 0;
/*     */       }
/*     */       
/* 487 */       this.cachedChestType = ((BlockChest)getBlockType()).chestType;
/*     */     }
/*     */     
/* 490 */     return this.cachedChestType;
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 495 */     return "minecraft:chest";
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 500 */     return new ContainerChest(playerInventory, this, playerIn);
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 505 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 514 */     return 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 519 */     for (int i = 0; i < this.chestContents.length; i++)
/*     */     {
/* 521 */       this.chestContents[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */