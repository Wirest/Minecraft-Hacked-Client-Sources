/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ContainerWorkbench extends Container
/*     */ {
/*  14 */   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
/*  15 */   public IInventory craftResult = new InventoryCraftResult();
/*     */   
/*     */   private World worldObj;
/*     */   
/*     */   private BlockPos pos;
/*     */   
/*     */   public ContainerWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn)
/*     */   {
/*  23 */     this.worldObj = worldIn;
/*  24 */     this.pos = posIn;
/*  25 */     addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
/*     */     
/*  27 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  29 */       for (int j = 0; j < 3; j++)
/*     */       {
/*  31 */         addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
/*     */       }
/*     */     }
/*     */     
/*  35 */     for (int k = 0; k < 3; k++)
/*     */     {
/*  37 */       for (int i1 = 0; i1 < 9; i1++)
/*     */       {
/*  39 */         addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
/*     */       }
/*     */     }
/*     */     
/*  43 */     for (int l = 0; l < 9; l++)
/*     */     {
/*  45 */       addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
/*     */     }
/*     */     
/*  48 */     onCraftMatrixChanged(this.craftMatrix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn)
/*     */   {
/*  56 */     this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/*  64 */     super.onContainerClosed(playerIn);
/*     */     
/*  66 */     if (!this.worldObj.isRemote)
/*     */     {
/*  68 */       for (int i = 0; i < 9; i++)
/*     */       {
/*  70 */         ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
/*     */         
/*  72 */         if (itemstack != null)
/*     */         {
/*  74 */           playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/*  82 */     return this.worldObj.getBlockState(this.pos).getBlock() == net.minecraft.init.Blocks.crafting_table;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/*  90 */     ItemStack itemstack = null;
/*  91 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/*  93 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/*  95 */       ItemStack itemstack1 = slot.getStack();
/*  96 */       itemstack = itemstack1.copy();
/*     */       
/*  98 */       if (index == 0)
/*     */       {
/* 100 */         if (!mergeItemStack(itemstack1, 10, 46, true))
/*     */         {
/* 102 */           return null;
/*     */         }
/*     */         
/* 105 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/* 107 */       else if ((index >= 10) && (index < 37))
/*     */       {
/* 109 */         if (!mergeItemStack(itemstack1, 37, 46, false))
/*     */         {
/* 111 */           return null;
/*     */         }
/*     */       }
/* 114 */       else if ((index >= 37) && (index < 46))
/*     */       {
/* 116 */         if (!mergeItemStack(itemstack1, 10, 37, false))
/*     */         {
/* 118 */           return null;
/*     */         }
/*     */       }
/* 121 */       else if (!mergeItemStack(itemstack1, 10, 46, false))
/*     */       {
/* 123 */         return null;
/*     */       }
/*     */       
/* 126 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 128 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 132 */         slot.onSlotChanged();
/*     */       }
/*     */       
/* 135 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 137 */         return null;
/*     */       }
/*     */       
/* 140 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     }
/*     */     
/* 143 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_)
/*     */   {
/* 152 */     return (p_94530_2_.inventory != this.craftResult) && (super.canMergeSlot(stack, p_94530_2_));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerWorkbench.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */