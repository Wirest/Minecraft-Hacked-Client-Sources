/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.InventoryCrafting;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.storage.MapData;
/*    */ 
/*    */ public class RecipesMapExtending extends ShapedRecipes
/*    */ {
/*    */   public RecipesMapExtending()
/*    */   {
/* 14 */     super(3, 3, new ItemStack[] { new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.filled_map, 0, 32767), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper) }, new ItemStack(Items.map, 0, 0));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean matches(InventoryCrafting inv, World worldIn)
/*    */   {
/* 22 */     if (!super.matches(inv, worldIn))
/*    */     {
/* 24 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 28 */     ItemStack itemstack = null;
/*    */     
/* 30 */     for (int i = 0; (i < inv.getSizeInventory()) && (itemstack == null); i++)
/*    */     {
/* 32 */       ItemStack itemstack1 = inv.getStackInSlot(i);
/*    */       
/* 34 */       if ((itemstack1 != null) && (itemstack1.getItem() == Items.filled_map))
/*    */       {
/* 36 */         itemstack = itemstack1;
/*    */       }
/*    */     }
/*    */     
/* 40 */     if (itemstack == null)
/*    */     {
/* 42 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 46 */     MapData mapdata = Items.filled_map.getMapData(itemstack, worldIn);
/* 47 */     return mapdata != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*    */   {
/* 57 */     ItemStack itemstack = null;
/*    */     
/* 59 */     for (int i = 0; (i < inv.getSizeInventory()) && (itemstack == null); i++)
/*    */     {
/* 61 */       ItemStack itemstack1 = inv.getStackInSlot(i);
/*    */       
/* 63 */       if ((itemstack1 != null) && (itemstack1.getItem() == Items.filled_map))
/*    */       {
/* 65 */         itemstack = itemstack1;
/*    */       }
/*    */     }
/*    */     
/* 69 */     itemstack = itemstack.copy();
/* 70 */     itemstack.stackSize = 1;
/*    */     
/* 72 */     if (itemstack.getTagCompound() == null)
/*    */     {
/* 74 */       itemstack.setTagCompound(new NBTTagCompound());
/*    */     }
/*    */     
/* 77 */     itemstack.getTagCompound().setBoolean("map_is_scaling", true);
/* 78 */     return itemstack;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesMapExtending.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */