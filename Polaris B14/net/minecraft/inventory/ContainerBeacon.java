/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContainerBeacon
/*     */   extends Container
/*     */ {
/*     */   private IInventory tileBeacon;
/*     */   private final BeaconSlot beaconSlot;
/*     */   
/*     */   public ContainerBeacon(IInventory playerInventory, IInventory tileBeaconIn)
/*     */   {
/*  18 */     this.tileBeacon = tileBeaconIn;
/*  19 */     addSlotToContainer(this.beaconSlot = new BeaconSlot(tileBeaconIn, 0, 136, 110));
/*  20 */     int i = 36;
/*  21 */     int j = 137;
/*     */     
/*  23 */     for (int k = 0; k < 3; k++)
/*     */     {
/*  25 */       for (int l = 0; l < 9; l++)
/*     */       {
/*  27 */         addSlotToContainer(new Slot(playerInventory, l + k * 9 + 9, i + l * 18, j + k * 18));
/*     */       }
/*     */     }
/*     */     
/*  31 */     for (int i1 = 0; i1 < 9; i1++)
/*     */     {
/*  33 */       addSlotToContainer(new Slot(playerInventory, i1, i + i1 * 18, 58 + j));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener)
/*     */   {
/*  39 */     super.onCraftGuiOpened(listener);
/*  40 */     listener.func_175173_a(this, this.tileBeacon);
/*     */   }
/*     */   
/*     */   public void updateProgressBar(int id, int data)
/*     */   {
/*  45 */     this.tileBeacon.setField(id, data);
/*     */   }
/*     */   
/*     */   public IInventory func_180611_e()
/*     */   {
/*  50 */     return this.tileBeacon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/*  58 */     super.onContainerClosed(playerIn);
/*     */     
/*  60 */     if ((playerIn != null) && (!playerIn.worldObj.isRemote))
/*     */     {
/*  62 */       ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());
/*     */       
/*  64 */       if (itemstack != null)
/*     */       {
/*  66 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/*  73 */     return this.tileBeacon.isUseableByPlayer(playerIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/*  81 */     ItemStack itemstack = null;
/*  82 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/*  84 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/*  86 */       ItemStack itemstack1 = slot.getStack();
/*  87 */       itemstack = itemstack1.copy();
/*     */       
/*  89 */       if (index == 0)
/*     */       {
/*  91 */         if (!mergeItemStack(itemstack1, 1, 37, true))
/*     */         {
/*  93 */           return null;
/*     */         }
/*     */         
/*  96 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/*  98 */       else if ((!this.beaconSlot.getHasStack()) && (this.beaconSlot.isItemValid(itemstack1)) && (itemstack1.stackSize == 1))
/*     */       {
/* 100 */         if (!mergeItemStack(itemstack1, 0, 1, false))
/*     */         {
/* 102 */           return null;
/*     */         }
/*     */       }
/* 105 */       else if ((index >= 1) && (index < 28))
/*     */       {
/* 107 */         if (!mergeItemStack(itemstack1, 28, 37, false))
/*     */         {
/* 109 */           return null;
/*     */         }
/*     */       }
/* 112 */       else if ((index >= 28) && (index < 37))
/*     */       {
/* 114 */         if (!mergeItemStack(itemstack1, 1, 28, false))
/*     */         {
/* 116 */           return null;
/*     */         }
/*     */       }
/* 119 */       else if (!mergeItemStack(itemstack1, 1, 37, false))
/*     */       {
/* 121 */         return null;
/*     */       }
/*     */       
/* 124 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 126 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 130 */         slot.onSlotChanged();
/*     */       }
/*     */       
/* 133 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 135 */         return null;
/*     */       }
/*     */       
/* 138 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     }
/*     */     
/* 141 */     return itemstack;
/*     */   }
/*     */   
/*     */   class BeaconSlot extends Slot
/*     */   {
/*     */     public BeaconSlot(IInventory p_i1801_2_, int p_i1801_3_, int p_i1801_4_, int p_i1801_5_)
/*     */     {
/* 148 */       super(p_i1801_3_, p_i1801_4_, p_i1801_5_);
/*     */     }
/*     */     
/*     */     public boolean isItemValid(ItemStack stack)
/*     */     {
/* 153 */       return stack != null;
/*     */     }
/*     */     
/*     */     public int getSlotStackLimit()
/*     */     {
/* 158 */       return 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerBeacon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */