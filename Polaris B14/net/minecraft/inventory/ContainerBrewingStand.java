/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ 
/*     */ public class ContainerBrewingStand extends Container
/*     */ {
/*     */   private IInventory tileBrewingStand;
/*     */   private final Slot theSlot;
/*     */   private int brewTime;
/*     */   
/*     */   public ContainerBrewingStand(InventoryPlayer playerInventory, IInventory tileBrewingStandIn)
/*     */   {
/*  19 */     this.tileBrewingStand = tileBrewingStandIn;
/*  20 */     addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 0, 56, 46));
/*  21 */     addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 1, 79, 53));
/*  22 */     addSlotToContainer(new Potion(playerInventory.player, tileBrewingStandIn, 2, 102, 46));
/*  23 */     this.theSlot = addSlotToContainer(new Ingredient(tileBrewingStandIn, 3, 79, 17));
/*     */     
/*  25 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  27 */       for (int j = 0; j < 9; j++)
/*     */       {
/*  29 */         addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     }
/*     */     
/*  33 */     for (int k = 0; k < 9; k++)
/*     */     {
/*  35 */       addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener)
/*     */   {
/*  41 */     super.onCraftGuiOpened(listener);
/*  42 */     listener.func_175173_a(this, this.tileBrewingStand);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void detectAndSendChanges()
/*     */   {
/*  50 */     super.detectAndSendChanges();
/*     */     
/*  52 */     for (int i = 0; i < this.crafters.size(); i++)
/*     */     {
/*  54 */       ICrafting icrafting = (ICrafting)this.crafters.get(i);
/*     */       
/*  56 */       if (this.brewTime != this.tileBrewingStand.getField(0))
/*     */       {
/*  58 */         icrafting.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
/*     */       }
/*     */     }
/*     */     
/*  62 */     this.brewTime = this.tileBrewingStand.getField(0);
/*     */   }
/*     */   
/*     */   public void updateProgressBar(int id, int data)
/*     */   {
/*  67 */     this.tileBrewingStand.setField(id, data);
/*     */   }
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/*  72 */     return this.tileBrewingStand.isUseableByPlayer(playerIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/*  80 */     ItemStack itemstack = null;
/*  81 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/*  83 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/*  85 */       ItemStack itemstack1 = slot.getStack();
/*  86 */       itemstack = itemstack1.copy();
/*     */       
/*  88 */       if (((index < 0) || (index > 2)) && (index != 3))
/*     */       {
/*  90 */         if ((!this.theSlot.getHasStack()) && (this.theSlot.isItemValid(itemstack1)))
/*     */         {
/*  92 */           if (!mergeItemStack(itemstack1, 3, 4, false))
/*     */           {
/*  94 */             return null;
/*     */           }
/*     */         }
/*  97 */         else if (Potion.canHoldPotion(itemstack))
/*     */         {
/*  99 */           if (!mergeItemStack(itemstack1, 0, 3, false))
/*     */           {
/* 101 */             return null;
/*     */           }
/*     */         }
/* 104 */         else if ((index >= 4) && (index < 31))
/*     */         {
/* 106 */           if (!mergeItemStack(itemstack1, 31, 40, false))
/*     */           {
/* 108 */             return null;
/*     */           }
/*     */         }
/* 111 */         else if ((index >= 31) && (index < 40))
/*     */         {
/* 113 */           if (!mergeItemStack(itemstack1, 4, 31, false))
/*     */           {
/* 115 */             return null;
/*     */           }
/*     */         }
/* 118 */         else if (!mergeItemStack(itemstack1, 4, 40, false))
/*     */         {
/* 120 */           return null;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 125 */         if (!mergeItemStack(itemstack1, 4, 40, true))
/*     */         {
/* 127 */           return null;
/*     */         }
/*     */         
/* 130 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/*     */       
/* 133 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 135 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 139 */         slot.onSlotChanged();
/*     */       }
/*     */       
/* 142 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 144 */         return null;
/*     */       }
/*     */       
/* 147 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     }
/*     */     
/* 150 */     return itemstack;
/*     */   }
/*     */   
/*     */   class Ingredient extends Slot
/*     */   {
/*     */     public Ingredient(IInventory inventoryIn, int index, int xPosition, int yPosition)
/*     */     {
/* 157 */       super(index, xPosition, yPosition);
/*     */     }
/*     */     
/*     */     public boolean isItemValid(ItemStack stack)
/*     */     {
/* 162 */       return stack != null ? stack.getItem().isPotionIngredient(stack) : false;
/*     */     }
/*     */     
/*     */     public int getSlotStackLimit()
/*     */     {
/* 167 */       return 64;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Potion extends Slot
/*     */   {
/*     */     private EntityPlayer player;
/*     */     
/*     */     public Potion(EntityPlayer playerIn, IInventory inventoryIn, int index, int xPosition, int yPosition)
/*     */     {
/* 177 */       super(index, xPosition, yPosition);
/* 178 */       this.player = playerIn;
/*     */     }
/*     */     
/*     */     public boolean isItemValid(ItemStack stack)
/*     */     {
/* 183 */       return canHoldPotion(stack);
/*     */     }
/*     */     
/*     */     public int getSlotStackLimit()
/*     */     {
/* 188 */       return 1;
/*     */     }
/*     */     
/*     */     public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
/*     */     {
/* 193 */       if ((stack.getItem() == Items.potionitem) && (stack.getMetadata() > 0))
/*     */       {
/* 195 */         this.player.triggerAchievement(AchievementList.potion);
/*     */       }
/*     */       
/* 198 */       super.onPickupFromSlot(playerIn, stack);
/*     */     }
/*     */     
/*     */     public static boolean canHoldPotion(ItemStack stack)
/*     */     {
/* 203 */       return (stack != null) && ((stack.getItem() == Items.potionitem) || (stack.getItem() == Items.glass_bottle));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerBrewingStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */