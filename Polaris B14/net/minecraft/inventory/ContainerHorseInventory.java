/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public class ContainerHorseInventory extends Container
/*     */ {
/*     */   private IInventory horseInventory;
/*     */   private EntityHorse theHorse;
/*     */   
/*     */   public ContainerHorseInventory(IInventory playerInventory, IInventory horseInventoryIn, final EntityHorse horse, EntityPlayer player)
/*     */   {
/*  15 */     this.horseInventory = horseInventoryIn;
/*  16 */     this.theHorse = horse;
/*  17 */     int i = 3;
/*  18 */     horseInventoryIn.openInventory(player);
/*  19 */     int j = (i - 4) * 18;
/*  20 */     addSlotToContainer(new Slot(horseInventoryIn, 0, 8, 18)
/*     */     {
/*     */       public boolean isItemValid(ItemStack stack)
/*     */       {
/*  24 */         return (super.isItemValid(stack)) && (stack.getItem() == net.minecraft.init.Items.saddle) && (!getHasStack());
/*     */       }
/*  26 */     });
/*  27 */     addSlotToContainer(new Slot(horseInventoryIn, 1, 8, 36)
/*     */     {
/*     */       public boolean isItemValid(ItemStack stack)
/*     */       {
/*  31 */         return (super.isItemValid(stack)) && (horse.canWearArmor()) && (EntityHorse.isArmorItem(stack.getItem()));
/*     */       }
/*     */       
/*     */       public boolean canBeHovered() {
/*  35 */         return horse.canWearArmor();
/*     */       }
/*     */     });
/*     */     
/*  39 */     if (horse.isChested())
/*     */     {
/*  41 */       for (int k = 0; k < i; k++)
/*     */       {
/*  43 */         for (int l = 0; l < 5; l++)
/*     */         {
/*  45 */           addSlotToContainer(new Slot(horseInventoryIn, 2 + l + k * 5, 80 + l * 18, 18 + k * 18));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  50 */     for (int i1 = 0; i1 < 3; i1++)
/*     */     {
/*  52 */       for (int k1 = 0; k1 < 9; k1++)
/*     */       {
/*  54 */         addSlotToContainer(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + j));
/*     */       }
/*     */     }
/*     */     
/*  58 */     for (int j1 = 0; j1 < 9; j1++)
/*     */     {
/*  60 */       addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 160 + j));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/*  66 */     return (this.horseInventory.isUseableByPlayer(playerIn)) && (this.theHorse.isEntityAlive()) && (this.theHorse.getDistanceToEntity(playerIn) < 8.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/*  74 */     ItemStack itemstack = null;
/*  75 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/*  77 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/*  79 */       ItemStack itemstack1 = slot.getStack();
/*  80 */       itemstack = itemstack1.copy();
/*     */       
/*  82 */       if (index < this.horseInventory.getSizeInventory())
/*     */       {
/*  84 */         if (!mergeItemStack(itemstack1, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true))
/*     */         {
/*  86 */           return null;
/*     */         }
/*     */       }
/*  89 */       else if ((getSlot(1).isItemValid(itemstack1)) && (!getSlot(1).getHasStack()))
/*     */       {
/*  91 */         if (!mergeItemStack(itemstack1, 1, 2, false))
/*     */         {
/*  93 */           return null;
/*     */         }
/*     */       }
/*  96 */       else if (getSlot(0).isItemValid(itemstack1))
/*     */       {
/*  98 */         if (!mergeItemStack(itemstack1, 0, 1, false))
/*     */         {
/* 100 */           return null;
/*     */         }
/*     */       }
/* 103 */       else if ((this.horseInventory.getSizeInventory() <= 2) || (!mergeItemStack(itemstack1, 2, this.horseInventory.getSizeInventory(), false)))
/*     */       {
/* 105 */         return null;
/*     */       }
/*     */       
/* 108 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 110 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 114 */         slot.onSlotChanged();
/*     */       }
/*     */     }
/*     */     
/* 118 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/* 126 */     super.onContainerClosed(playerIn);
/* 127 */     this.horseInventory.closeInventory(playerIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerHorseInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */