/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InventoryCrafting
/*     */   implements IInventory
/*     */ {
/*     */   private final ItemStack[] stackList;
/*     */   private final int inventoryWidth;
/*     */   private final int inventoryHeight;
/*     */   private final Container eventHandler;
/*     */   
/*     */   public InventoryCrafting(Container eventHandlerIn, int width, int height)
/*     */   {
/*  25 */     int i = width * height;
/*  26 */     this.stackList = new ItemStack[i];
/*  27 */     this.eventHandler = eventHandlerIn;
/*  28 */     this.inventoryWidth = width;
/*  29 */     this.inventoryHeight = height;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  37 */     return this.stackList.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/*  45 */     return index >= getSizeInventory() ? null : this.stackList[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInRowAndColumn(int row, int column)
/*     */   {
/*  53 */     return (row >= 0) && (row < this.inventoryWidth) && (column >= 0) && (column <= this.inventoryHeight) ? getStackInSlot(row + column * this.inventoryWidth) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  61 */     return "container.crafting";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/*  69 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDisplayName()
/*     */   {
/*  77 */     return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/*  85 */     if (this.stackList[index] != null)
/*     */     {
/*  87 */       ItemStack itemstack = this.stackList[index];
/*  88 */       this.stackList[index] = null;
/*  89 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*  93 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/* 102 */     if (this.stackList[index] != null)
/*     */     {
/* 104 */       if (this.stackList[index].stackSize <= count)
/*     */       {
/* 106 */         ItemStack itemstack1 = this.stackList[index];
/* 107 */         this.stackList[index] = null;
/* 108 */         this.eventHandler.onCraftMatrixChanged(this);
/* 109 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/* 113 */       ItemStack itemstack = this.stackList[index].splitStack(count);
/*     */       
/* 115 */       if (this.stackList[index].stackSize == 0)
/*     */       {
/* 117 */         this.stackList[index] = null;
/*     */       }
/*     */       
/* 120 */       this.eventHandler.onCraftMatrixChanged(this);
/* 121 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 126 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 135 */     this.stackList[index] = stack;
/* 136 */     this.eventHandler.onCraftMatrixChanged(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 144 */     return 64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markDirty() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 160 */     return true;
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
/* 176 */     return true;
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 181 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 190 */     return 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 195 */     for (int i = 0; i < this.stackList.length; i++)
/*     */     {
/* 197 */       this.stackList[i] = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getHeight()
/*     */   {
/* 203 */     return this.inventoryHeight;
/*     */   }
/*     */   
/*     */   public int getWidth()
/*     */   {
/* 208 */     return this.inventoryWidth;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\InventoryCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */