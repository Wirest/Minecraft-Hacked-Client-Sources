/*     */ package net.minecraft.entity.player;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ReportedException;
/*     */ 
/*     */ 
/*     */ public class InventoryPlayer
/*     */   implements IInventory
/*     */ {
/*  24 */   public ItemStack[] mainInventory = new ItemStack[36];
/*     */   
/*     */ 
/*  27 */   public ItemStack[] armorInventory = new ItemStack[4];
/*     */   
/*     */ 
/*     */   public int currentItem;
/*     */   
/*     */ 
/*     */   public EntityPlayer player;
/*     */   
/*     */ 
/*     */   private ItemStack itemStack;
/*     */   
/*     */ 
/*     */   public boolean inventoryChanged;
/*     */   
/*     */ 
/*     */   public InventoryPlayer(EntityPlayer playerIn)
/*     */   {
/*  44 */     this.player = playerIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getCurrentItem()
/*     */   {
/*  52 */     return (this.currentItem < 9) && (this.currentItem >= 0) ? this.mainInventory[this.currentItem] : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getHotbarSize()
/*     */   {
/*  60 */     return 9;
/*     */   }
/*     */   
/*     */   private int getInventorySlotContainItem(Item itemIn)
/*     */   {
/*  65 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/*  67 */       if ((this.mainInventory[i] != null) && (this.mainInventory[i].getItem() == itemIn))
/*     */       {
/*  69 */         return i;
/*     */       }
/*     */     }
/*     */     
/*  73 */     return -1;
/*     */   }
/*     */   
/*     */   private int getInventorySlotContainItemAndDamage(Item itemIn, int p_146024_2_)
/*     */   {
/*  78 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/*  80 */       if ((this.mainInventory[i] != null) && (this.mainInventory[i].getItem() == itemIn) && (this.mainInventory[i].getMetadata() == p_146024_2_))
/*     */       {
/*  82 */         return i;
/*     */       }
/*     */     }
/*     */     
/*  86 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int storeItemStack(ItemStack itemStackIn)
/*     */   {
/*  94 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/*  96 */       if ((this.mainInventory[i] != null) && (this.mainInventory[i].getItem() == itemStackIn.getItem()) && (this.mainInventory[i].isStackable()) && (this.mainInventory[i].stackSize < this.mainInventory[i].getMaxStackSize()) && (this.mainInventory[i].stackSize < getInventoryStackLimit()) && ((!this.mainInventory[i].getHasSubtypes()) || (this.mainInventory[i].getMetadata() == itemStackIn.getMetadata())) && (ItemStack.areItemStackTagsEqual(this.mainInventory[i], itemStackIn)))
/*     */       {
/*  98 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 102 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFirstEmptyStack()
/*     */   {
/* 110 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 112 */       if (this.mainInventory[i] == null)
/*     */       {
/* 114 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 118 */     return -1;
/*     */   }
/*     */   
/*     */   public void setCurrentItem(Item itemIn, int p_146030_2_, boolean p_146030_3_, boolean p_146030_4_)
/*     */   {
/* 123 */     ItemStack itemstack = getCurrentItem();
/* 124 */     int i = p_146030_3_ ? getInventorySlotContainItemAndDamage(itemIn, p_146030_2_) : getInventorySlotContainItem(itemIn);
/*     */     
/* 126 */     if ((i >= 0) && (i < 9))
/*     */     {
/* 128 */       this.currentItem = i;
/*     */     }
/* 130 */     else if ((p_146030_4_) && (itemIn != null))
/*     */     {
/* 132 */       int j = getFirstEmptyStack();
/*     */       
/* 134 */       if ((j >= 0) && (j < 9))
/*     */       {
/* 136 */         this.currentItem = j;
/*     */       }
/*     */       
/* 139 */       if ((itemstack == null) || (!itemstack.isItemEnchantable()) || (getInventorySlotContainItemAndDamage(itemstack.getItem(), itemstack.getItemDamage()) != this.currentItem))
/*     */       {
/* 141 */         int k = getInventorySlotContainItemAndDamage(itemIn, p_146030_2_);
/*     */         
/*     */         int l;
/* 144 */         if (k >= 0)
/*     */         {
/* 146 */           int l = this.mainInventory[k].stackSize;
/* 147 */           this.mainInventory[k] = this.mainInventory[this.currentItem];
/*     */         }
/*     */         else
/*     */         {
/* 151 */           l = 1;
/*     */         }
/*     */         
/* 154 */         this.mainInventory[this.currentItem] = new ItemStack(itemIn, l, p_146030_2_);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void changeCurrentItem(int p_70453_1_)
/*     */   {
/* 164 */     if (p_70453_1_ > 0)
/*     */     {
/* 166 */       p_70453_1_ = 1;
/*     */     }
/*     */     
/* 169 */     if (p_70453_1_ < 0)
/*     */     {
/* 171 */       p_70453_1_ = -1;
/*     */     }
/*     */     
/* 174 */     for (this.currentItem -= p_70453_1_; this.currentItem < 0; this.currentItem += 9) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 179 */     while (this.currentItem >= 9)
/*     */     {
/* 181 */       this.currentItem -= 9;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int clearMatchingItems(Item itemIn, int metadataIn, int removeCount, NBTTagCompound itemNBT)
/*     */   {
/* 195 */     int i = 0;
/*     */     
/* 197 */     for (int j = 0; j < this.mainInventory.length; j++)
/*     */     {
/* 199 */       ItemStack itemstack = this.mainInventory[j];
/*     */       
/* 201 */       if ((itemstack != null) && ((itemIn == null) || (itemstack.getItem() == itemIn)) && ((metadataIn <= -1) || (itemstack.getMetadata() == metadataIn)) && ((itemNBT == null) || (NBTUtil.func_181123_a(itemNBT, itemstack.getTagCompound(), true))))
/*     */       {
/* 203 */         int k = removeCount <= 0 ? itemstack.stackSize : Math.min(removeCount - i, itemstack.stackSize);
/* 204 */         i += k;
/*     */         
/* 206 */         if (removeCount != 0)
/*     */         {
/* 208 */           this.mainInventory[j].stackSize -= k;
/*     */           
/* 210 */           if (this.mainInventory[j].stackSize == 0)
/*     */           {
/* 212 */             this.mainInventory[j] = null;
/*     */           }
/*     */           
/* 215 */           if ((removeCount > 0) && (i >= removeCount))
/*     */           {
/* 217 */             return i;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 223 */     for (int l = 0; l < this.armorInventory.length; l++)
/*     */     {
/* 225 */       ItemStack itemstack1 = this.armorInventory[l];
/*     */       
/* 227 */       if ((itemstack1 != null) && ((itemIn == null) || (itemstack1.getItem() == itemIn)) && ((metadataIn <= -1) || (itemstack1.getMetadata() == metadataIn)) && ((itemNBT == null) || (NBTUtil.func_181123_a(itemNBT, itemstack1.getTagCompound(), false))))
/*     */       {
/* 229 */         int j1 = removeCount <= 0 ? itemstack1.stackSize : Math.min(removeCount - i, itemstack1.stackSize);
/* 230 */         i += j1;
/*     */         
/* 232 */         if (removeCount != 0)
/*     */         {
/* 234 */           this.armorInventory[l].stackSize -= j1;
/*     */           
/* 236 */           if (this.armorInventory[l].stackSize == 0)
/*     */           {
/* 238 */             this.armorInventory[l] = null;
/*     */           }
/*     */           
/* 241 */           if ((removeCount > 0) && (i >= removeCount))
/*     */           {
/* 243 */             return i;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 249 */     if (this.itemStack != null)
/*     */     {
/* 251 */       if ((itemIn != null) && (this.itemStack.getItem() != itemIn))
/*     */       {
/* 253 */         return i;
/*     */       }
/*     */       
/* 256 */       if ((metadataIn > -1) && (this.itemStack.getMetadata() != metadataIn))
/*     */       {
/* 258 */         return i;
/*     */       }
/*     */       
/* 261 */       if ((itemNBT != null) && (!NBTUtil.func_181123_a(itemNBT, this.itemStack.getTagCompound(), false)))
/*     */       {
/* 263 */         return i;
/*     */       }
/*     */       
/* 266 */       int i1 = removeCount <= 0 ? this.itemStack.stackSize : Math.min(removeCount - i, this.itemStack.stackSize);
/* 267 */       i += i1;
/*     */       
/* 269 */       if (removeCount != 0)
/*     */       {
/* 271 */         this.itemStack.stackSize -= i1;
/*     */         
/* 273 */         if (this.itemStack.stackSize == 0)
/*     */         {
/* 275 */           this.itemStack = null;
/*     */         }
/*     */         
/* 278 */         if ((removeCount > 0) && (i >= removeCount))
/*     */         {
/* 280 */           return i;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 285 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int storePartialItemStack(ItemStack itemStackIn)
/*     */   {
/* 294 */     Item item = itemStackIn.getItem();
/* 295 */     int i = itemStackIn.stackSize;
/* 296 */     int j = storeItemStack(itemStackIn);
/*     */     
/* 298 */     if (j < 0)
/*     */     {
/* 300 */       j = getFirstEmptyStack();
/*     */     }
/*     */     
/* 303 */     if (j < 0)
/*     */     {
/* 305 */       return i;
/*     */     }
/*     */     
/*     */ 
/* 309 */     if (this.mainInventory[j] == null)
/*     */     {
/* 311 */       this.mainInventory[j] = new ItemStack(item, 0, itemStackIn.getMetadata());
/*     */       
/* 313 */       if (itemStackIn.hasTagCompound())
/*     */       {
/* 315 */         this.mainInventory[j].setTagCompound((NBTTagCompound)itemStackIn.getTagCompound().copy());
/*     */       }
/*     */     }
/*     */     
/* 319 */     int k = i;
/*     */     
/* 321 */     if (i > this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize)
/*     */     {
/* 323 */       k = this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize;
/*     */     }
/*     */     
/* 326 */     if (k > getInventoryStackLimit() - this.mainInventory[j].stackSize)
/*     */     {
/* 328 */       k = getInventoryStackLimit() - this.mainInventory[j].stackSize;
/*     */     }
/*     */     
/* 331 */     if (k == 0)
/*     */     {
/* 333 */       return i;
/*     */     }
/*     */     
/*     */ 
/* 337 */     i -= k;
/* 338 */     this.mainInventory[j].stackSize += k;
/* 339 */     this.mainInventory[j].animationsToGo = 5;
/* 340 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void decrementAnimations()
/*     */   {
/* 351 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 353 */       if (this.mainInventory[i] != null)
/*     */       {
/* 355 */         this.mainInventory[i].updateAnimation(this.player.worldObj, this.player, i, this.currentItem == i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean consumeInventoryItem(Item itemIn)
/*     */   {
/* 365 */     int i = getInventorySlotContainItem(itemIn);
/*     */     
/* 367 */     if (i < 0)
/*     */     {
/* 369 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 373 */     if (--this.mainInventory[i].stackSize <= 0)
/*     */     {
/* 375 */       this.mainInventory[i] = null;
/*     */     }
/*     */     
/* 378 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasItem(Item itemIn)
/*     */   {
/* 387 */     int i = getInventorySlotContainItem(itemIn);
/* 388 */     return i >= 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean addItemStackToInventory(final ItemStack itemStackIn)
/*     */   {
/* 396 */     if ((itemStackIn != null) && (itemStackIn.stackSize != 0) && (itemStackIn.getItem() != null))
/*     */     {
/*     */       try
/*     */       {
/* 400 */         if (itemStackIn.isItemDamaged())
/*     */         {
/* 402 */           int j = getFirstEmptyStack();
/*     */           
/* 404 */           if (j >= 0)
/*     */           {
/* 406 */             this.mainInventory[j] = ItemStack.copyItemStack(itemStackIn);
/* 407 */             this.mainInventory[j].animationsToGo = 5;
/* 408 */             itemStackIn.stackSize = 0;
/* 409 */             return true;
/*     */           }
/* 411 */           if (this.player.capabilities.isCreativeMode)
/*     */           {
/* 413 */             itemStackIn.stackSize = 0;
/* 414 */             return true;
/*     */           }
/*     */           
/*     */ 
/* 418 */           return false;
/*     */         }
/*     */         
/*     */ 
/*     */         int i;
/*     */         
/*     */ 
/*     */         do
/*     */         {
/* 427 */           i = itemStackIn.stackSize;
/* 428 */           itemStackIn.stackSize = storePartialItemStack(itemStackIn);
/*     */         }
/* 430 */         while ((itemStackIn.stackSize > 0) && (itemStackIn.stackSize < i));
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 436 */         if ((itemStackIn.stackSize == i) && (this.player.capabilities.isCreativeMode))
/*     */         {
/* 438 */           itemStackIn.stackSize = 0;
/* 439 */           return true;
/*     */         }
/*     */         
/*     */ 
/* 443 */         return itemStackIn.stackSize < i;
/*     */ 
/*     */       }
/*     */       catch (Throwable throwable)
/*     */       {
/*     */ 
/* 449 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
/* 450 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
/* 451 */         crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(itemStackIn.getItem())));
/* 452 */         crashreportcategory.addCrashSection("Item data", Integer.valueOf(itemStackIn.getMetadata()));
/* 453 */         crashreportcategory.addCrashSectionCallable("Item name", new Callable()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 457 */             return itemStackIn.getDisplayName();
/*     */           }
/* 459 */         });
/* 460 */         throw new ReportedException(crashreport);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 465 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/* 474 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 476 */     if (index >= this.mainInventory.length)
/*     */     {
/* 478 */       aitemstack = this.armorInventory;
/* 479 */       index -= this.mainInventory.length;
/*     */     }
/*     */     
/* 482 */     if (aitemstack[index] != null)
/*     */     {
/* 484 */       if (aitemstack[index].stackSize <= count)
/*     */       {
/* 486 */         ItemStack itemstack1 = aitemstack[index];
/* 487 */         aitemstack[index] = null;
/* 488 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/* 492 */       ItemStack itemstack = aitemstack[index].splitStack(count);
/*     */       
/* 494 */       if (aitemstack[index].stackSize == 0)
/*     */       {
/* 496 */         aitemstack[index] = null;
/*     */       }
/*     */       
/* 499 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 504 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 513 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 515 */     if (index >= this.mainInventory.length)
/*     */     {
/* 517 */       aitemstack = this.armorInventory;
/* 518 */       index -= this.mainInventory.length;
/*     */     }
/*     */     
/* 521 */     if (aitemstack[index] != null)
/*     */     {
/* 523 */       ItemStack itemstack = aitemstack[index];
/* 524 */       aitemstack[index] = null;
/* 525 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 529 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 538 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 540 */     if (index >= aitemstack.length)
/*     */     {
/* 542 */       index -= aitemstack.length;
/* 543 */       aitemstack = this.armorInventory;
/*     */     }
/*     */     
/* 546 */     aitemstack[index] = stack;
/*     */   }
/*     */   
/*     */   public float getStrVsBlock(Block blockIn)
/*     */   {
/* 551 */     float f = 1.0F;
/*     */     
/* 553 */     if (this.mainInventory[this.currentItem] != null)
/*     */     {
/* 555 */       f *= this.mainInventory[this.currentItem].getStrVsBlock(blockIn);
/*     */     }
/*     */     
/* 558 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagList writeToNBT(NBTTagList p_70442_1_)
/*     */   {
/* 567 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 569 */       if (this.mainInventory[i] != null)
/*     */       {
/* 571 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 572 */         nbttagcompound.setByte("Slot", (byte)i);
/* 573 */         this.mainInventory[i].writeToNBT(nbttagcompound);
/* 574 */         p_70442_1_.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 578 */     for (int j = 0; j < this.armorInventory.length; j++)
/*     */     {
/* 580 */       if (this.armorInventory[j] != null)
/*     */       {
/* 582 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 583 */         nbttagcompound1.setByte("Slot", (byte)(j + 100));
/* 584 */         this.armorInventory[j].writeToNBT(nbttagcompound1);
/* 585 */         p_70442_1_.appendTag(nbttagcompound1);
/*     */       }
/*     */     }
/*     */     
/* 589 */     return p_70442_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readFromNBT(NBTTagList p_70443_1_)
/*     */   {
/* 597 */     this.mainInventory = new ItemStack[36];
/* 598 */     this.armorInventory = new ItemStack[4];
/*     */     
/* 600 */     for (int i = 0; i < p_70443_1_.tagCount(); i++)
/*     */     {
/* 602 */       NBTTagCompound nbttagcompound = p_70443_1_.getCompoundTagAt(i);
/* 603 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/* 604 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       
/* 606 */       if (itemstack != null)
/*     */       {
/* 608 */         if ((j >= 0) && (j < this.mainInventory.length))
/*     */         {
/* 610 */           this.mainInventory[j] = itemstack;
/*     */         }
/*     */         
/* 613 */         if ((j >= 100) && (j < this.armorInventory.length + 100))
/*     */         {
/* 615 */           this.armorInventory[(j - 100)] = itemstack;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/* 626 */     return this.mainInventory.length + 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/* 634 */     ItemStack[] aitemstack = this.mainInventory;
/*     */     
/* 636 */     if (index >= aitemstack.length)
/*     */     {
/* 638 */       index -= aitemstack.length;
/* 639 */       aitemstack = this.armorInventory;
/*     */     }
/*     */     
/* 642 */     return aitemstack[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 650 */     return "container.inventory";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 658 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDisplayName()
/*     */   {
/* 666 */     return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 674 */     return 64;
/*     */   }
/*     */   
/*     */   public boolean canHeldItemHarvest(Block blockIn)
/*     */   {
/* 679 */     if (blockIn.getMaterial().isToolNotRequired())
/*     */     {
/* 681 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 685 */     ItemStack itemstack = getStackInSlot(this.currentItem);
/* 686 */     return itemstack != null ? itemstack.canHarvestBlock(blockIn) : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack armorItemInSlot(int p_70440_1_)
/*     */   {
/* 695 */     return this.armorInventory[p_70440_1_];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTotalArmorValue()
/*     */   {
/* 703 */     int i = 0;
/*     */     
/* 705 */     for (int j = 0; j < this.armorInventory.length; j++)
/*     */     {
/* 707 */       if ((this.armorInventory[j] != null) && ((this.armorInventory[j].getItem() instanceof ItemArmor)))
/*     */       {
/* 709 */         int k = ((ItemArmor)this.armorInventory[j].getItem()).damageReduceAmount;
/* 710 */         i += k;
/*     */       }
/*     */     }
/*     */     
/* 714 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void damageArmor(float damage)
/*     */   {
/* 722 */     damage /= 4.0F;
/*     */     
/* 724 */     if (damage < 1.0F)
/*     */     {
/* 726 */       damage = 1.0F;
/*     */     }
/*     */     
/* 729 */     for (int i = 0; i < this.armorInventory.length; i++)
/*     */     {
/* 731 */       if ((this.armorInventory[i] != null) && ((this.armorInventory[i].getItem() instanceof ItemArmor)))
/*     */       {
/* 733 */         this.armorInventory[i].damageItem((int)damage, this.player);
/*     */         
/* 735 */         if (this.armorInventory[i].stackSize == 0)
/*     */         {
/* 737 */           this.armorInventory[i] = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropAllItems()
/*     */   {
/* 748 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 750 */       if (this.mainInventory[i] != null)
/*     */       {
/* 752 */         this.player.dropItem(this.mainInventory[i], true, false);
/* 753 */         this.mainInventory[i] = null;
/*     */       }
/*     */     }
/*     */     
/* 757 */     for (int j = 0; j < this.armorInventory.length; j++)
/*     */     {
/* 759 */       if (this.armorInventory[j] != null)
/*     */       {
/* 761 */         this.player.dropItem(this.armorInventory[j], true, false);
/* 762 */         this.armorInventory[j] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markDirty()
/*     */   {
/* 773 */     this.inventoryChanged = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItemStack(ItemStack itemStackIn)
/*     */   {
/* 781 */     this.itemStack = itemStackIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getItemStack()
/*     */   {
/* 789 */     return this.itemStack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 797 */     return !this.player.isDead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasItemStack(ItemStack itemStackIn)
/*     */   {
/* 805 */     for (int i = 0; i < this.armorInventory.length; i++)
/*     */     {
/* 807 */       if ((this.armorInventory[i] != null) && (this.armorInventory[i].isItemEqual(itemStackIn)))
/*     */       {
/* 809 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 813 */     for (int j = 0; j < this.mainInventory.length; j++)
/*     */     {
/* 815 */       if ((this.mainInventory[j] != null) && (this.mainInventory[j].isItemEqual(itemStackIn)))
/*     */       {
/* 817 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 821 */     return false;
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
/* 837 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void copyInventory(InventoryPlayer playerInventory)
/*     */   {
/* 845 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 847 */       this.mainInventory[i] = ItemStack.copyItemStack(playerInventory.mainInventory[i]);
/*     */     }
/*     */     
/* 850 */     for (int j = 0; j < this.armorInventory.length; j++)
/*     */     {
/* 852 */       this.armorInventory[j] = ItemStack.copyItemStack(playerInventory.armorInventory[j]);
/*     */     }
/*     */     
/* 855 */     this.currentItem = playerInventory.currentItem;
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 860 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 869 */     return 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 874 */     for (int i = 0; i < this.mainInventory.length; i++)
/*     */     {
/* 876 */       this.mainInventory[i] = null;
/*     */     }
/*     */     
/* 879 */     for (int j = 0; j < this.armorInventory.length; j++)
/*     */     {
/* 881 */       this.armorInventory[j] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\player\InventoryPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */