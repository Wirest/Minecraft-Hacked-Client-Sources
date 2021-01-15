/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ import net.minecraft.village.MerchantRecipeList;
/*     */ 
/*     */ public class InventoryMerchant implements IInventory
/*     */ {
/*     */   private final IMerchant theMerchant;
/*  15 */   private ItemStack[] theInventory = new ItemStack[3];
/*     */   private final EntityPlayer thePlayer;
/*     */   private MerchantRecipe currentRecipe;
/*     */   private int currentRecipeIndex;
/*     */   
/*     */   public InventoryMerchant(EntityPlayer thePlayerIn, IMerchant theMerchantIn)
/*     */   {
/*  22 */     this.thePlayer = thePlayerIn;
/*  23 */     this.theMerchant = theMerchantIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  31 */     return this.theInventory.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/*  39 */     return this.theInventory[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/*  47 */     if (this.theInventory[index] != null)
/*     */     {
/*  49 */       if (index == 2)
/*     */       {
/*  51 */         ItemStack itemstack2 = this.theInventory[index];
/*  52 */         this.theInventory[index] = null;
/*  53 */         return itemstack2;
/*     */       }
/*  55 */       if (this.theInventory[index].stackSize <= count)
/*     */       {
/*  57 */         ItemStack itemstack1 = this.theInventory[index];
/*  58 */         this.theInventory[index] = null;
/*     */         
/*  60 */         if (inventoryResetNeededOnSlotChange(index))
/*     */         {
/*  62 */           resetRecipeAndSlots();
/*     */         }
/*     */         
/*  65 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/*  69 */       ItemStack itemstack = this.theInventory[index].splitStack(count);
/*     */       
/*  71 */       if (this.theInventory[index].stackSize == 0)
/*     */       {
/*  73 */         this.theInventory[index] = null;
/*     */       }
/*     */       
/*  76 */       if (inventoryResetNeededOnSlotChange(index))
/*     */       {
/*  78 */         resetRecipeAndSlots();
/*     */       }
/*     */       
/*  81 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  86 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean inventoryResetNeededOnSlotChange(int p_70469_1_)
/*     */   {
/*  95 */     return (p_70469_1_ == 0) || (p_70469_1_ == 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 103 */     if (this.theInventory[index] != null)
/*     */     {
/* 105 */       ItemStack itemstack = this.theInventory[index];
/* 106 */       this.theInventory[index] = null;
/* 107 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 111 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 120 */     this.theInventory[index] = stack;
/*     */     
/* 122 */     if ((stack != null) && (stack.stackSize > getInventoryStackLimit()))
/*     */     {
/* 124 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 127 */     if (inventoryResetNeededOnSlotChange(index))
/*     */     {
/* 129 */       resetRecipeAndSlots();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 138 */     return "mob.villager";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 146 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDisplayName()
/*     */   {
/* 154 */     return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 162 */     return 64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 170 */     return this.theMerchant.getCustomer() == player;
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
/* 186 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markDirty()
/*     */   {
/* 195 */     resetRecipeAndSlots();
/*     */   }
/*     */   
/*     */   public void resetRecipeAndSlots()
/*     */   {
/* 200 */     this.currentRecipe = null;
/* 201 */     ItemStack itemstack = this.theInventory[0];
/* 202 */     ItemStack itemstack1 = this.theInventory[1];
/*     */     
/* 204 */     if (itemstack == null)
/*     */     {
/* 206 */       itemstack = itemstack1;
/* 207 */       itemstack1 = null;
/*     */     }
/*     */     
/* 210 */     if (itemstack == null)
/*     */     {
/* 212 */       setInventorySlotContents(2, null);
/*     */     }
/*     */     else
/*     */     {
/* 216 */       MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
/*     */       
/* 218 */       if (merchantrecipelist != null)
/*     */       {
/* 220 */         MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
/*     */         
/* 222 */         if ((merchantrecipe != null) && (!merchantrecipe.isRecipeDisabled()))
/*     */         {
/* 224 */           this.currentRecipe = merchantrecipe;
/* 225 */           setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
/*     */         }
/* 227 */         else if (itemstack1 != null)
/*     */         {
/* 229 */           merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
/*     */           
/* 231 */           if ((merchantrecipe != null) && (!merchantrecipe.isRecipeDisabled()))
/*     */           {
/* 233 */             this.currentRecipe = merchantrecipe;
/* 234 */             setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
/*     */           }
/*     */           else
/*     */           {
/* 238 */             setInventorySlotContents(2, null);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 243 */           setInventorySlotContents(2, null);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 248 */     this.theMerchant.verifySellingItem(getStackInSlot(2));
/*     */   }
/*     */   
/*     */   public MerchantRecipe getCurrentRecipe()
/*     */   {
/* 253 */     return this.currentRecipe;
/*     */   }
/*     */   
/*     */   public void setCurrentRecipeIndex(int currentRecipeIndexIn)
/*     */   {
/* 258 */     this.currentRecipeIndex = currentRecipeIndexIn;
/* 259 */     resetRecipeAndSlots();
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 264 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 273 */     return 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 278 */     for (int i = 0; i < this.theInventory.length; i++)
/*     */     {
/* 280 */       this.theInventory[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\InventoryMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */