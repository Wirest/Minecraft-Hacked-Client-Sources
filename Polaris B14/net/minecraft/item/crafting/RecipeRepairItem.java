/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecipeRepairItem
/*     */   implements IRecipe
/*     */ {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn)
/*     */   {
/*  17 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  19 */     for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */     {
/*  21 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/*  23 */       if (itemstack != null)
/*     */       {
/*  25 */         list.add(itemstack);
/*     */         
/*  27 */         if (list.size() > 1)
/*     */         {
/*  29 */           ItemStack itemstack1 = (ItemStack)list.get(0);
/*     */           
/*  31 */           if ((itemstack.getItem() != itemstack1.getItem()) || (itemstack1.stackSize != 1) || (itemstack.stackSize != 1) || (!itemstack1.getItem().isDamageable()))
/*     */           {
/*  33 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  39 */     return list.size() == 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */   {
/*  47 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  49 */     for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */     {
/*  51 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/*  53 */       if (itemstack != null)
/*     */       {
/*  55 */         list.add(itemstack);
/*     */         
/*  57 */         if (list.size() > 1)
/*     */         {
/*  59 */           ItemStack itemstack1 = (ItemStack)list.get(0);
/*     */           
/*  61 */           if ((itemstack.getItem() != itemstack1.getItem()) || (itemstack1.stackSize != 1) || (itemstack.stackSize != 1) || (!itemstack1.getItem().isDamageable()))
/*     */           {
/*  63 */             return null;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  69 */     if (list.size() == 2)
/*     */     {
/*  71 */       ItemStack itemstack2 = (ItemStack)list.get(0);
/*  72 */       ItemStack itemstack3 = (ItemStack)list.get(1);
/*     */       
/*  74 */       if ((itemstack2.getItem() == itemstack3.getItem()) && (itemstack2.stackSize == 1) && (itemstack3.stackSize == 1) && (itemstack2.getItem().isDamageable()))
/*     */       {
/*  76 */         Item item = itemstack2.getItem();
/*  77 */         int j = item.getMaxDamage() - itemstack2.getItemDamage();
/*  78 */         int k = item.getMaxDamage() - itemstack3.getItemDamage();
/*  79 */         int l = j + k + item.getMaxDamage() * 5 / 100;
/*  80 */         int i1 = item.getMaxDamage() - l;
/*     */         
/*  82 */         if (i1 < 0)
/*     */         {
/*  84 */           i1 = 0;
/*     */         }
/*     */         
/*  87 */         return new ItemStack(itemstack2.getItem(), 1, i1);
/*     */       }
/*     */     }
/*     */     
/*  91 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRecipeSize()
/*     */   {
/*  99 */     return 4;
/*     */   }
/*     */   
/*     */   public ItemStack getRecipeOutput()
/*     */   {
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */   {
/* 109 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 111 */     for (int i = 0; i < aitemstack.length; i++)
/*     */     {
/* 113 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 115 */       if ((itemstack != null) && (itemstack.getItem().hasContainerItem()))
/*     */       {
/* 117 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     }
/*     */     
/* 121 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipeRepairItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */