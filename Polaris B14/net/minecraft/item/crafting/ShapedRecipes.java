/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShapedRecipes
/*     */   implements IRecipe
/*     */ {
/*     */   private final int recipeWidth;
/*     */   private final int recipeHeight;
/*     */   private final ItemStack[] recipeItems;
/*     */   private final ItemStack recipeOutput;
/*     */   private boolean copyIngredientNBT;
/*     */   
/*     */   public ShapedRecipes(int width, int height, ItemStack[] p_i1917_3_, ItemStack output)
/*     */   {
/*  25 */     this.recipeWidth = width;
/*  26 */     this.recipeHeight = height;
/*  27 */     this.recipeItems = p_i1917_3_;
/*  28 */     this.recipeOutput = output;
/*     */   }
/*     */   
/*     */   public ItemStack getRecipeOutput()
/*     */   {
/*  33 */     return this.recipeOutput;
/*     */   }
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */   {
/*  38 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/*  40 */     for (int i = 0; i < aitemstack.length; i++)
/*     */     {
/*  42 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/*  44 */       if ((itemstack != null) && (itemstack.getItem().hasContainerItem()))
/*     */       {
/*  46 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     }
/*     */     
/*  50 */     return aitemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matches(InventoryCrafting inv, World worldIn)
/*     */   {
/*  58 */     for (int i = 0; i <= 3 - this.recipeWidth; i++)
/*     */     {
/*  60 */       for (int j = 0; j <= 3 - this.recipeHeight; j++)
/*     */       {
/*  62 */         if (checkMatch(inv, i, j, true))
/*     */         {
/*  64 */           return true;
/*     */         }
/*     */         
/*  67 */         if (checkMatch(inv, i, j, false))
/*     */         {
/*  69 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  74 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean checkMatch(InventoryCrafting p_77573_1_, int p_77573_2_, int p_77573_3_, boolean p_77573_4_)
/*     */   {
/*  82 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  84 */       for (int j = 0; j < 3; j++)
/*     */       {
/*  86 */         int k = i - p_77573_2_;
/*  87 */         int l = j - p_77573_3_;
/*  88 */         ItemStack itemstack = null;
/*     */         
/*  90 */         if ((k >= 0) && (l >= 0) && (k < this.recipeWidth) && (l < this.recipeHeight))
/*     */         {
/*  92 */           if (p_77573_4_)
/*     */           {
/*  94 */             itemstack = this.recipeItems[(this.recipeWidth - k - 1 + l * this.recipeWidth)];
/*     */           }
/*     */           else
/*     */           {
/*  98 */             itemstack = this.recipeItems[(k + l * this.recipeWidth)];
/*     */           }
/*     */         }
/*     */         
/* 102 */         ItemStack itemstack1 = p_77573_1_.getStackInRowAndColumn(i, j);
/*     */         
/* 104 */         if ((itemstack1 != null) || (itemstack != null))
/*     */         {
/* 106 */           if (((itemstack1 == null) && (itemstack != null)) || ((itemstack1 != null) && (itemstack == null)))
/*     */           {
/* 108 */             return false;
/*     */           }
/*     */           
/* 111 */           if (itemstack.getItem() != itemstack1.getItem())
/*     */           {
/* 113 */             return false;
/*     */           }
/*     */           
/* 116 */           if ((itemstack.getMetadata() != 32767) && (itemstack.getMetadata() != itemstack1.getMetadata()))
/*     */           {
/* 118 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 124 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */   {
/* 132 */     ItemStack itemstack = getRecipeOutput().copy();
/*     */     
/* 134 */     if (this.copyIngredientNBT)
/*     */     {
/* 136 */       for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */       {
/* 138 */         ItemStack itemstack1 = inv.getStackInSlot(i);
/*     */         
/* 140 */         if ((itemstack1 != null) && (itemstack1.hasTagCompound()))
/*     */         {
/* 142 */           itemstack.setTagCompound((NBTTagCompound)itemstack1.getTagCompound().copy());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 147 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRecipeSize()
/*     */   {
/* 155 */     return this.recipeWidth * this.recipeHeight;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\ShapedRecipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */