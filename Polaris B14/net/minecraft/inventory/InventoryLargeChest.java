/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.LockCode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InventoryLargeChest
/*     */   implements ILockableContainer
/*     */ {
/*     */   private String name;
/*     */   private ILockableContainer upperChest;
/*     */   private ILockableContainer lowerChest;
/*     */   
/*     */   public InventoryLargeChest(String nameIn, ILockableContainer upperChestIn, ILockableContainer lowerChestIn)
/*     */   {
/*  25 */     this.name = nameIn;
/*     */     
/*  27 */     if (upperChestIn == null)
/*     */     {
/*  29 */       upperChestIn = lowerChestIn;
/*     */     }
/*     */     
/*  32 */     if (lowerChestIn == null)
/*     */     {
/*  34 */       lowerChestIn = upperChestIn;
/*     */     }
/*     */     
/*  37 */     this.upperChest = upperChestIn;
/*  38 */     this.lowerChest = lowerChestIn;
/*     */     
/*  40 */     if (upperChestIn.isLocked())
/*     */     {
/*  42 */       lowerChestIn.setLockCode(upperChestIn.getLockCode());
/*     */     }
/*  44 */     else if (lowerChestIn.isLocked())
/*     */     {
/*  46 */       upperChestIn.setLockCode(lowerChestIn.getLockCode());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  55 */     return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPartOfLargeChest(IInventory inventoryIn)
/*     */   {
/*  63 */     return (this.upperChest == inventoryIn) || (this.lowerChest == inventoryIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  71 */     return this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.upperChest.hasCustomName() ? this.upperChest.getName() : this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/*  79 */     return (this.upperChest.hasCustomName()) || (this.lowerChest.hasCustomName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDisplayName()
/*     */   {
/*  87 */     return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/*  95 */     return index >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/* 103 */     return index >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 111 */     return index >= this.upperChest.getSizeInventory() ? this.lowerChest.removeStackFromSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 119 */     if (index >= this.upperChest.getSizeInventory())
/*     */     {
/* 121 */       this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
/*     */     }
/*     */     else
/*     */     {
/* 125 */       this.upperChest.setInventorySlotContents(index, stack);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 134 */     return this.upperChest.getInventoryStackLimit();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markDirty()
/*     */   {
/* 143 */     this.upperChest.markDirty();
/* 144 */     this.lowerChest.markDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 152 */     return (this.upperChest.isUseableByPlayer(player)) && (this.lowerChest.isUseableByPlayer(player));
/*     */   }
/*     */   
/*     */   public void openInventory(EntityPlayer player)
/*     */   {
/* 157 */     this.upperChest.openInventory(player);
/* 158 */     this.lowerChest.openInventory(player);
/*     */   }
/*     */   
/*     */   public void closeInventory(EntityPlayer player)
/*     */   {
/* 163 */     this.upperChest.closeInventory(player);
/* 164 */     this.lowerChest.closeInventory(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack)
/*     */   {
/* 172 */     return true;
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 177 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 186 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isLocked()
/*     */   {
/* 191 */     return (this.upperChest.isLocked()) || (this.lowerChest.isLocked());
/*     */   }
/*     */   
/*     */   public void setLockCode(LockCode code)
/*     */   {
/* 196 */     this.upperChest.setLockCode(code);
/* 197 */     this.lowerChest.setLockCode(code);
/*     */   }
/*     */   
/*     */   public LockCode getLockCode()
/*     */   {
/* 202 */     return this.upperChest.getLockCode();
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 207 */     return this.upperChest.getGuiID();
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 212 */     return new ContainerChest(playerInventory, this, playerIn);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 217 */     this.upperChest.clear();
/* 218 */     this.lowerChest.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\InventoryLargeChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */