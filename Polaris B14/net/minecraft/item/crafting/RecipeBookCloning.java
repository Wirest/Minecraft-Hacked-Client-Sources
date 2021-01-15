/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.ItemEditableBook;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecipeBookCloning
/*     */   implements IRecipe
/*     */ {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn)
/*     */   {
/*  17 */     int i = 0;
/*  18 */     ItemStack itemstack = null;
/*     */     
/*  20 */     for (int j = 0; j < inv.getSizeInventory(); j++)
/*     */     {
/*  22 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  24 */       if (itemstack1 != null)
/*     */       {
/*  26 */         if (itemstack1.getItem() == Items.written_book)
/*     */         {
/*  28 */           if (itemstack != null)
/*     */           {
/*  30 */             return false;
/*     */           }
/*     */           
/*  33 */           itemstack = itemstack1;
/*     */         }
/*     */         else
/*     */         {
/*  37 */           if (itemstack1.getItem() != Items.writable_book)
/*     */           {
/*  39 */             return false;
/*     */           }
/*     */           
/*  42 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  47 */     return (itemstack != null) && (i > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */   {
/*  55 */     int i = 0;
/*  56 */     ItemStack itemstack = null;
/*     */     
/*  58 */     for (int j = 0; j < inv.getSizeInventory(); j++)
/*     */     {
/*  60 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  62 */       if (itemstack1 != null)
/*     */       {
/*  64 */         if (itemstack1.getItem() == Items.written_book)
/*     */         {
/*  66 */           if (itemstack != null)
/*     */           {
/*  68 */             return null;
/*     */           }
/*     */           
/*  71 */           itemstack = itemstack1;
/*     */         }
/*     */         else
/*     */         {
/*  75 */           if (itemstack1.getItem() != Items.writable_book)
/*     */           {
/*  77 */             return null;
/*     */           }
/*     */           
/*  80 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  85 */     if ((itemstack != null) && (i >= 1) && (ItemEditableBook.getGeneration(itemstack) < 2))
/*     */     {
/*  87 */       ItemStack itemstack2 = new ItemStack(Items.written_book, i);
/*  88 */       itemstack2.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
/*  89 */       itemstack2.getTagCompound().setInteger("generation", ItemEditableBook.getGeneration(itemstack) + 1);
/*     */       
/*  91 */       if (itemstack.hasDisplayName())
/*     */       {
/*  93 */         itemstack2.setStackDisplayName(itemstack.getDisplayName());
/*     */       }
/*     */       
/*  96 */       return itemstack2;
/*     */     }
/*     */     
/*     */ 
/* 100 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRecipeSize()
/*     */   {
/* 109 */     return 9;
/*     */   }
/*     */   
/*     */   public ItemStack getRecipeOutput()
/*     */   {
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */   {
/* 119 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 121 */     for (int i = 0; i < aitemstack.length; i++)
/*     */     {
/* 123 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 125 */       if ((itemstack != null) && ((itemstack.getItem() instanceof ItemEditableBook)))
/*     */       {
/* 127 */         aitemstack[i] = itemstack;
/* 128 */         break;
/*     */       }
/*     */     }
/*     */     
/* 132 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipeBookCloning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */