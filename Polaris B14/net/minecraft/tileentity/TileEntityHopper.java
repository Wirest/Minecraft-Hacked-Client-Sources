/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.BlockHopper;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.ISidedInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityHopper extends TileEntityLockable implements IHopper, ITickable
/*     */ {
/*  28 */   private ItemStack[] inventory = new ItemStack[5];
/*     */   private String customName;
/*  30 */   private int transferCooldown = -1;
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/*  34 */     super.readFromNBT(compound);
/*  35 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/*  36 */     this.inventory = new ItemStack[getSizeInventory()];
/*     */     
/*  38 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/*  40 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */     
/*  43 */     this.transferCooldown = compound.getInteger("TransferCooldown");
/*     */     
/*  45 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/*  47 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*  48 */       int j = nbttagcompound.getByte("Slot");
/*     */       
/*  50 */       if ((j >= 0) && (j < this.inventory.length))
/*     */       {
/*  52 */         this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/*  59 */     super.writeToNBT(compound);
/*  60 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/*  62 */     for (int i = 0; i < this.inventory.length; i++)
/*     */     {
/*  64 */       if (this.inventory[i] != null)
/*     */       {
/*  66 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  67 */         nbttagcompound.setByte("Slot", (byte)i);
/*  68 */         this.inventory[i].writeToNBT(nbttagcompound);
/*  69 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/*  73 */     compound.setTag("Items", nbttaglist);
/*  74 */     compound.setInteger("TransferCooldown", this.transferCooldown);
/*     */     
/*  76 */     if (hasCustomName())
/*     */     {
/*  78 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markDirty()
/*     */   {
/*  88 */     super.markDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  96 */     return this.inventory.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/* 104 */     return this.inventory[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/* 112 */     if (this.inventory[index] != null)
/*     */     {
/* 114 */       if (this.inventory[index].stackSize <= count)
/*     */       {
/* 116 */         ItemStack itemstack1 = this.inventory[index];
/* 117 */         this.inventory[index] = null;
/* 118 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/* 122 */       ItemStack itemstack = this.inventory[index].splitStack(count);
/*     */       
/* 124 */       if (this.inventory[index].stackSize == 0)
/*     */       {
/* 126 */         this.inventory[index] = null;
/*     */       }
/*     */       
/* 129 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 134 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 143 */     if (this.inventory[index] != null)
/*     */     {
/* 145 */       ItemStack itemstack = this.inventory[index];
/* 146 */       this.inventory[index] = null;
/* 147 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 151 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 160 */     this.inventory[index] = stack;
/*     */     
/* 162 */     if ((stack != null) && (stack.stackSize > getInventoryStackLimit()))
/*     */     {
/* 164 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 173 */     return hasCustomName() ? this.customName : "container.hopper";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 181 */     return (this.customName != null) && (this.customName.length() > 0);
/*     */   }
/*     */   
/*     */   public void setCustomName(String customNameIn)
/*     */   {
/* 186 */     this.customName = customNameIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 194 */     return 64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 202 */     return this.worldObj.getTileEntity(this.pos) == this;
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
/* 218 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/* 226 */     if ((this.worldObj != null) && (!this.worldObj.isRemote))
/*     */     {
/* 228 */       this.transferCooldown -= 1;
/*     */       
/* 230 */       if (!isOnTransferCooldown())
/*     */       {
/* 232 */         setTransferCooldown(0);
/* 233 */         updateHopper();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean updateHopper()
/*     */   {
/* 240 */     if ((this.worldObj != null) && (!this.worldObj.isRemote))
/*     */     {
/* 242 */       if ((!isOnTransferCooldown()) && (BlockHopper.isEnabled(getBlockMetadata())))
/*     */       {
/* 244 */         boolean flag = false;
/*     */         
/* 246 */         if (!isEmpty())
/*     */         {
/* 248 */           flag = transferItemsOut();
/*     */         }
/*     */         
/* 251 */         if (!isFull())
/*     */         {
/* 253 */           flag = (captureDroppedItems(this)) || (flag);
/*     */         }
/*     */         
/* 256 */         if (flag)
/*     */         {
/* 258 */           setTransferCooldown(8);
/* 259 */           markDirty();
/* 260 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 264 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 268 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isEmpty()
/*     */   {
/*     */     ItemStack[] arrayOfItemStack;
/* 274 */     int j = (arrayOfItemStack = this.inventory).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*     */       
/* 276 */       if (itemstack != null)
/*     */       {
/* 278 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 282 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isFull() {
/*     */     ItemStack[] arrayOfItemStack;
/* 287 */     int j = (arrayOfItemStack = this.inventory).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*     */       
/* 289 */       if ((itemstack == null) || (itemstack.stackSize != itemstack.getMaxStackSize()))
/*     */       {
/* 291 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 295 */     return true;
/*     */   }
/*     */   
/*     */   private boolean transferItemsOut()
/*     */   {
/* 300 */     IInventory iinventory = getInventoryForHopperTransfer();
/*     */     
/* 302 */     if (iinventory == null)
/*     */     {
/* 304 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 308 */     EnumFacing enumfacing = BlockHopper.getFacing(getBlockMetadata()).getOpposite();
/*     */     
/* 310 */     if (isInventoryFull(iinventory, enumfacing))
/*     */     {
/* 312 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 316 */     for (int i = 0; i < getSizeInventory(); i++)
/*     */     {
/* 318 */       if (getStackInSlot(i) != null)
/*     */       {
/* 320 */         ItemStack itemstack = getStackInSlot(i).copy();
/* 321 */         ItemStack itemstack1 = putStackInInventoryAllSlots(iinventory, decrStackSize(i, 1), enumfacing);
/*     */         
/* 323 */         if ((itemstack1 == null) || (itemstack1.stackSize == 0))
/*     */         {
/* 325 */           iinventory.markDirty();
/* 326 */           return true;
/*     */         }
/*     */         
/* 329 */         setInventorySlotContents(i, itemstack);
/*     */       }
/*     */     }
/*     */     
/* 333 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side)
/*     */   {
/* 343 */     if ((inventoryIn instanceof ISidedInventory))
/*     */     {
/* 345 */       ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
/* 346 */       int[] aint = isidedinventory.getSlotsForFace(side);
/*     */       
/* 348 */       for (int k = 0; k < aint.length; k++)
/*     */       {
/* 350 */         ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[k]);
/*     */         
/* 352 */         if ((itemstack1 == null) || (itemstack1.stackSize != itemstack1.getMaxStackSize()))
/*     */         {
/* 354 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 360 */       int i = inventoryIn.getSizeInventory();
/*     */       
/* 362 */       for (int j = 0; j < i; j++)
/*     */       {
/* 364 */         ItemStack itemstack = inventoryIn.getStackInSlot(j);
/*     */         
/* 366 */         if ((itemstack == null) || (itemstack.stackSize != itemstack.getMaxStackSize()))
/*     */         {
/* 368 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 373 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side)
/*     */   {
/* 381 */     if ((inventoryIn instanceof ISidedInventory))
/*     */     {
/* 383 */       ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
/* 384 */       int[] aint = isidedinventory.getSlotsForFace(side);
/*     */       
/* 386 */       for (int i = 0; i < aint.length; i++)
/*     */       {
/* 388 */         if (isidedinventory.getStackInSlot(aint[i]) != null)
/*     */         {
/* 390 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 396 */       int j = inventoryIn.getSizeInventory();
/*     */       
/* 398 */       for (int k = 0; k < j; k++)
/*     */       {
/* 400 */         if (inventoryIn.getStackInSlot(k) != null)
/*     */         {
/* 402 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 407 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean captureDroppedItems(IHopper p_145891_0_)
/*     */   {
/* 412 */     IInventory iinventory = getHopperInventory(p_145891_0_);
/*     */     int j;
/* 414 */     if (iinventory != null)
/*     */     {
/* 416 */       EnumFacing enumfacing = EnumFacing.DOWN;
/*     */       
/* 418 */       if (isInventoryEmpty(iinventory, enumfacing))
/*     */       {
/* 420 */         return false;
/*     */       }
/*     */       
/* 423 */       if ((iinventory instanceof ISidedInventory))
/*     */       {
/* 425 */         ISidedInventory isidedinventory = (ISidedInventory)iinventory;
/* 426 */         int[] aint = isidedinventory.getSlotsForFace(enumfacing);
/*     */         
/* 428 */         for (int i = 0; i < aint.length; i++)
/*     */         {
/* 430 */           if (pullItemFromSlot(p_145891_0_, iinventory, aint[i], enumfacing))
/*     */           {
/* 432 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 438 */         j = iinventory.getSizeInventory();
/*     */         
/* 440 */         for (int k = 0; k < j; k++)
/*     */         {
/* 442 */           if (pullItemFromSlot(p_145891_0_, iinventory, k, enumfacing))
/*     */           {
/* 444 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 451 */       for (EntityItem entityitem : func_181556_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0D, p_145891_0_.getZPos()))
/*     */       {
/* 453 */         if (putDropInInventoryAllSlots(p_145891_0_, entityitem))
/*     */         {
/* 455 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 460 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction)
/*     */   {
/* 469 */     ItemStack itemstack = inventoryIn.getStackInSlot(index);
/*     */     
/* 471 */     if ((itemstack != null) && (canExtractItemFromSlot(inventoryIn, itemstack, index, direction)))
/*     */     {
/* 473 */       ItemStack itemstack1 = itemstack.copy();
/* 474 */       ItemStack itemstack2 = putStackInInventoryAllSlots(hopper, inventoryIn.decrStackSize(index, 1), null);
/*     */       
/* 476 */       if ((itemstack2 == null) || (itemstack2.stackSize == 0))
/*     */       {
/* 478 */         inventoryIn.markDirty();
/* 479 */         return true;
/*     */       }
/*     */       
/* 482 */       inventoryIn.setInventorySlotContents(index, itemstack1);
/*     */     }
/*     */     
/* 485 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean putDropInInventoryAllSlots(IInventory p_145898_0_, EntityItem itemIn)
/*     */   {
/* 494 */     boolean flag = false;
/*     */     
/* 496 */     if (itemIn == null)
/*     */     {
/* 498 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 502 */     ItemStack itemstack = itemIn.getEntityItem().copy();
/* 503 */     ItemStack itemstack1 = putStackInInventoryAllSlots(p_145898_0_, itemstack, null);
/*     */     
/* 505 */     if ((itemstack1 != null) && (itemstack1.stackSize != 0))
/*     */     {
/* 507 */       itemIn.setEntityItemStack(itemstack1);
/*     */     }
/*     */     else
/*     */     {
/* 511 */       flag = true;
/* 512 */       itemIn.setDead();
/*     */     }
/*     */     
/* 515 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, EnumFacing side)
/*     */   {
/* 524 */     if (((inventoryIn instanceof ISidedInventory)) && (side != null))
/*     */     {
/* 526 */       ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
/* 527 */       int[] aint = isidedinventory.getSlotsForFace(side);
/*     */       
/* 529 */       int k = 0;
/*     */       do {
/* 531 */         stack = insertStack(inventoryIn, stack, aint[k], side);k++;
/* 529 */         if ((k >= aint.length) || (stack == null)) break; } while (stack.stackSize > 0);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 536 */       int i = inventoryIn.getSizeInventory();
/*     */       
/* 538 */       for (int j = 0; (j < i) && (stack != null) && (stack.stackSize > 0); j++)
/*     */       {
/* 540 */         stack = insertStack(inventoryIn, stack, j, side);
/*     */       }
/*     */     }
/*     */     
/* 544 */     if ((stack != null) && (stack.stackSize == 0))
/*     */     {
/* 546 */       stack = null;
/*     */     }
/*     */     
/* 549 */     return stack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
/*     */   {
/* 557 */     return inventoryIn.isItemValidForSlot(index, stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
/*     */   {
/* 565 */     return (!(inventoryIn instanceof ISidedInventory)) || (((ISidedInventory)inventoryIn).canExtractItem(index, stack, side));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
/*     */   {
/* 573 */     ItemStack itemstack = inventoryIn.getStackInSlot(index);
/*     */     
/* 575 */     if (canInsertItemInSlot(inventoryIn, stack, index, side))
/*     */     {
/* 577 */       boolean flag = false;
/*     */       
/* 579 */       if (itemstack == null)
/*     */       {
/* 581 */         inventoryIn.setInventorySlotContents(index, stack);
/* 582 */         stack = null;
/* 583 */         flag = true;
/*     */       }
/* 585 */       else if (canCombine(itemstack, stack))
/*     */       {
/* 587 */         int i = stack.getMaxStackSize() - itemstack.stackSize;
/* 588 */         int j = Math.min(stack.stackSize, i);
/* 589 */         stack.stackSize -= j;
/* 590 */         itemstack.stackSize += j;
/* 591 */         flag = j > 0;
/*     */       }
/*     */       
/* 594 */       if (flag)
/*     */       {
/* 596 */         if ((inventoryIn instanceof TileEntityHopper))
/*     */         {
/* 598 */           TileEntityHopper tileentityhopper = (TileEntityHopper)inventoryIn;
/*     */           
/* 600 */           if (tileentityhopper.mayTransfer())
/*     */           {
/* 602 */             tileentityhopper.setTransferCooldown(8);
/*     */           }
/*     */           
/* 605 */           inventoryIn.markDirty();
/*     */         }
/*     */         
/* 608 */         inventoryIn.markDirty();
/*     */       }
/*     */     }
/*     */     
/* 612 */     return stack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private IInventory getInventoryForHopperTransfer()
/*     */   {
/* 620 */     EnumFacing enumfacing = BlockHopper.getFacing(getBlockMetadata());
/* 621 */     return getInventoryAtPosition(getWorld(), this.pos.getX() + enumfacing.getFrontOffsetX(), this.pos.getY() + enumfacing.getFrontOffsetY(), this.pos.getZ() + enumfacing.getFrontOffsetZ());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IInventory getHopperInventory(IHopper hopper)
/*     */   {
/* 629 */     return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
/*     */   }
/*     */   
/*     */   public static List<EntityItem> func_181556_a(World p_181556_0_, double p_181556_1_, double p_181556_3_, double p_181556_5_)
/*     */   {
/* 634 */     return p_181556_0_.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p_181556_1_ - 0.5D, p_181556_3_ - 0.5D, p_181556_5_ - 0.5D, p_181556_1_ + 0.5D, p_181556_3_ + 0.5D, p_181556_5_ + 0.5D), EntitySelectors.selectAnything);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z)
/*     */   {
/* 642 */     IInventory iinventory = null;
/* 643 */     int i = MathHelper.floor_double(x);
/* 644 */     int j = MathHelper.floor_double(y);
/* 645 */     int k = MathHelper.floor_double(z);
/* 646 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 647 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */     
/* 649 */     if (block.hasTileEntity())
/*     */     {
/* 651 */       TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */       
/* 653 */       if ((tileentity instanceof IInventory))
/*     */       {
/* 655 */         iinventory = (IInventory)tileentity;
/*     */         
/* 657 */         if (((iinventory instanceof TileEntityChest)) && ((block instanceof BlockChest)))
/*     */         {
/* 659 */           iinventory = ((BlockChest)block).getLockableContainer(worldIn, blockpos);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 664 */     if (iinventory == null)
/*     */     {
/* 666 */       List<net.minecraft.entity.Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelectors.selectInventories);
/*     */       
/* 668 */       if (list.size() > 0)
/*     */       {
/* 670 */         iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
/*     */       }
/*     */     }
/*     */     
/* 674 */     return iinventory;
/*     */   }
/*     */   
/*     */   private static boolean canCombine(ItemStack stack1, ItemStack stack2)
/*     */   {
/* 679 */     return stack1.stackSize > stack1.getMaxStackSize() ? false : stack1.getMetadata() != stack2.getMetadata() ? false : stack1.getItem() != stack2.getItem() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getXPos()
/*     */   {
/* 687 */     return this.pos.getX() + 0.5D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getYPos()
/*     */   {
/* 695 */     return this.pos.getY() + 0.5D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getZPos()
/*     */   {
/* 703 */     return this.pos.getZ() + 0.5D;
/*     */   }
/*     */   
/*     */   public void setTransferCooldown(int ticks)
/*     */   {
/* 708 */     this.transferCooldown = ticks;
/*     */   }
/*     */   
/*     */   public boolean isOnTransferCooldown()
/*     */   {
/* 713 */     return this.transferCooldown > 0;
/*     */   }
/*     */   
/*     */   public boolean mayTransfer()
/*     */   {
/* 718 */     return this.transferCooldown <= 1;
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 723 */     return "minecraft:hopper";
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 728 */     return new net.minecraft.inventory.ContainerHopper(playerInventory, this, playerIn);
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 733 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 742 */     return 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 747 */     for (int i = 0; i < this.inventory.length; i++)
/*     */     {
/* 749 */       this.inventory[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */