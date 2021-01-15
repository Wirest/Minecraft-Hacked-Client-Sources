/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.inventory.InventoryCrafting;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ShapelessRecipes implements IRecipe
/*    */ {
/*    */   private final ItemStack recipeOutput;
/*    */   private final List<ItemStack> recipeItems;
/*    */   
/*    */   public ShapelessRecipes(ItemStack output, List<ItemStack> inputList)
/*    */   {
/* 17 */     this.recipeOutput = output;
/* 18 */     this.recipeItems = inputList;
/*    */   }
/*    */   
/*    */   public ItemStack getRecipeOutput()
/*    */   {
/* 23 */     return this.recipeOutput;
/*    */   }
/*    */   
/*    */   public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*    */   {
/* 28 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*    */     
/* 30 */     for (int i = 0; i < aitemstack.length; i++)
/*    */     {
/* 32 */       ItemStack itemstack = inv.getStackInSlot(i);
/*    */       
/* 34 */       if ((itemstack != null) && (itemstack.getItem().hasContainerItem()))
/*    */       {
/* 36 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*    */       }
/*    */     }
/*    */     
/* 40 */     return aitemstack;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean matches(InventoryCrafting inv, World worldIn)
/*    */   {
/* 48 */     List<ItemStack> list = Lists.newArrayList(this.recipeItems);
/*    */     
/* 50 */     for (int i = 0; i < inv.getHeight(); i++)
/*    */     {
/* 52 */       for (int j = 0; j < inv.getWidth(); j++)
/*    */       {
/* 54 */         ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
/*    */         
/* 56 */         if (itemstack != null)
/*    */         {
/* 58 */           boolean flag = false;
/*    */           
/* 60 */           for (ItemStack itemstack1 : list)
/*    */           {
/* 62 */             if ((itemstack.getItem() == itemstack1.getItem()) && ((itemstack1.getMetadata() == 32767) || (itemstack.getMetadata() == itemstack1.getMetadata())))
/*    */             {
/* 64 */               flag = true;
/* 65 */               list.remove(itemstack1);
/* 66 */               break;
/*    */             }
/*    */           }
/*    */           
/* 70 */           if (!flag)
/*    */           {
/* 72 */             return false;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 78 */     return list.isEmpty();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*    */   {
/* 86 */     return this.recipeOutput.copy();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRecipeSize()
/*    */   {
/* 94 */     return this.recipeItems.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\ShapelessRecipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */