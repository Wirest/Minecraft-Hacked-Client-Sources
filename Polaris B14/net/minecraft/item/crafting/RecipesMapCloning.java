/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class RecipesMapCloning
/*     */   implements IRecipe
/*     */ {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn)
/*     */   {
/*  15 */     int i = 0;
/*  16 */     ItemStack itemstack = null;
/*     */     
/*  18 */     for (int j = 0; j < inv.getSizeInventory(); j++)
/*     */     {
/*  20 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  22 */       if (itemstack1 != null)
/*     */       {
/*  24 */         if (itemstack1.getItem() == Items.filled_map)
/*     */         {
/*  26 */           if (itemstack != null)
/*     */           {
/*  28 */             return false;
/*     */           }
/*     */           
/*  31 */           itemstack = itemstack1;
/*     */         }
/*     */         else
/*     */         {
/*  35 */           if (itemstack1.getItem() != Items.map)
/*     */           {
/*  37 */             return false;
/*     */           }
/*     */           
/*  40 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  45 */     return (itemstack != null) && (i > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */   {
/*  53 */     int i = 0;
/*  54 */     ItemStack itemstack = null;
/*     */     
/*  56 */     for (int j = 0; j < inv.getSizeInventory(); j++)
/*     */     {
/*  58 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  60 */       if (itemstack1 != null)
/*     */       {
/*  62 */         if (itemstack1.getItem() == Items.filled_map)
/*     */         {
/*  64 */           if (itemstack != null)
/*     */           {
/*  66 */             return null;
/*     */           }
/*     */           
/*  69 */           itemstack = itemstack1;
/*     */         }
/*     */         else
/*     */         {
/*  73 */           if (itemstack1.getItem() != Items.map)
/*     */           {
/*  75 */             return null;
/*     */           }
/*     */           
/*  78 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  83 */     if ((itemstack != null) && (i >= 1))
/*     */     {
/*  85 */       ItemStack itemstack2 = new ItemStack(Items.filled_map, i + 1, itemstack.getMetadata());
/*     */       
/*  87 */       if (itemstack.hasDisplayName())
/*     */       {
/*  89 */         itemstack2.setStackDisplayName(itemstack.getDisplayName());
/*     */       }
/*     */       
/*  92 */       return itemstack2;
/*     */     }
/*     */     
/*     */ 
/*  96 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRecipeSize()
/*     */   {
/* 105 */     return 9;
/*     */   }
/*     */   
/*     */   public ItemStack getRecipeOutput()
/*     */   {
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */   {
/* 115 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 117 */     for (int i = 0; i < aitemstack.length; i++)
/*     */     {
/* 119 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 121 */       if ((itemstack != null) && (itemstack.getItem().hasContainerItem()))
/*     */       {
/* 123 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     }
/*     */     
/* 127 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesMapCloning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */