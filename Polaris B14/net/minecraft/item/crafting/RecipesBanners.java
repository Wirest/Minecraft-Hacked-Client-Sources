/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.tileentity.TileEntityBanner.EnumBannerPattern;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class RecipesBanners
/*     */ {
/*     */   void addRecipes(CraftingManager p_179534_1_)
/*     */   {
/*     */     EnumDyeColor[] arrayOfEnumDyeColor;
/*  20 */     int j = (arrayOfEnumDyeColor = EnumDyeColor.values()).length; for (int i = 0; i < j; i++) { EnumDyeColor enumdyecolor = arrayOfEnumDyeColor[i];
/*     */       
/*  22 */       p_179534_1_.addRecipe(new ItemStack(Items.banner, 1, enumdyecolor.getDyeDamage()), new Object[] { "###", "###", " | ", Character.valueOf('#'), new ItemStack(Blocks.wool, 1, enumdyecolor.getMetadata()), Character.valueOf('|'), Items.stick });
/*     */     }
/*     */     
/*  25 */     p_179534_1_.addRecipe(new RecipeDuplicatePattern(null));
/*  26 */     p_179534_1_.addRecipe(new RecipeAddPattern(null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class RecipeAddPattern
/*     */     implements IRecipe
/*     */   {
/*     */     public boolean matches(InventoryCrafting inv, World worldIn)
/*     */     {
/*  37 */       boolean flag = false;
/*     */       
/*  39 */       for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */       {
/*  41 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/*  43 */         if ((itemstack != null) && (itemstack.getItem() == Items.banner))
/*     */         {
/*  45 */           if (flag)
/*     */           {
/*  47 */             return false;
/*     */           }
/*     */           
/*  50 */           if (TileEntityBanner.getPatterns(itemstack) >= 6)
/*     */           {
/*  52 */             return false;
/*     */           }
/*     */           
/*  55 */           flag = true;
/*     */         }
/*     */       }
/*     */       
/*  59 */       if (!flag)
/*     */       {
/*  61 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  65 */       return func_179533_c(inv) != null;
/*     */     }
/*     */     
/*     */ 
/*     */     public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */     {
/*  71 */       ItemStack itemstack = null;
/*     */       
/*  73 */       for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */       {
/*  75 */         ItemStack itemstack1 = inv.getStackInSlot(i);
/*     */         
/*  77 */         if ((itemstack1 != null) && (itemstack1.getItem() == Items.banner))
/*     */         {
/*  79 */           itemstack = itemstack1.copy();
/*  80 */           itemstack.stackSize = 1;
/*  81 */           break;
/*     */         }
/*     */       }
/*     */       
/*  85 */       TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = func_179533_c(inv);
/*     */       
/*  87 */       if (tileentitybanner$enumbannerpattern != null)
/*     */       {
/*  89 */         int k = 0;
/*     */         
/*  91 */         for (int j = 0; j < inv.getSizeInventory(); j++)
/*     */         {
/*  93 */           ItemStack itemstack2 = inv.getStackInSlot(j);
/*     */           
/*  95 */           if ((itemstack2 != null) && (itemstack2.getItem() == Items.dye))
/*     */           {
/*  97 */             k = itemstack2.getMetadata();
/*  98 */             break;
/*     */           }
/*     */         }
/*     */         
/* 102 */         NBTTagCompound nbttagcompound1 = itemstack.getSubCompound("BlockEntityTag", true);
/* 103 */         NBTTagList nbttaglist = null;
/*     */         
/* 105 */         if (nbttagcompound1.hasKey("Patterns", 9))
/*     */         {
/* 107 */           nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
/*     */         }
/*     */         else
/*     */         {
/* 111 */           nbttaglist = new NBTTagList();
/* 112 */           nbttagcompound1.setTag("Patterns", nbttaglist);
/*     */         }
/*     */         
/* 115 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 116 */         nbttagcompound.setString("Pattern", tileentitybanner$enumbannerpattern.getPatternID());
/* 117 */         nbttagcompound.setInteger("Color", k);
/* 118 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */       
/* 121 */       return itemstack;
/*     */     }
/*     */     
/*     */     public int getRecipeSize()
/*     */     {
/* 126 */       return 10;
/*     */     }
/*     */     
/*     */     public ItemStack getRecipeOutput()
/*     */     {
/* 131 */       return null;
/*     */     }
/*     */     
/*     */     public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */     {
/* 136 */       ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */       
/* 138 */       for (int i = 0; i < aitemstack.length; i++)
/*     */       {
/* 140 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/* 142 */         if ((itemstack != null) && (itemstack.getItem().hasContainerItem()))
/*     */         {
/* 144 */           aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */         }
/*     */       }
/*     */       
/* 148 */       return aitemstack;
/*     */     }
/*     */     
/*     */     private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting p_179533_1_) {
/*     */       TileEntityBanner.EnumBannerPattern[] arrayOfEnumBannerPattern;
/* 153 */       int j = (arrayOfEnumBannerPattern = TileEntityBanner.EnumBannerPattern.values()).length; for (int i = 0; i < j; i++) { TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = arrayOfEnumBannerPattern[i];
/*     */         
/* 155 */         if (tileentitybanner$enumbannerpattern.hasValidCrafting())
/*     */         {
/* 157 */           boolean flag = true;
/*     */           
/* 159 */           if (tileentitybanner$enumbannerpattern.hasCraftingStack())
/*     */           {
/* 161 */             boolean flag1 = false;
/* 162 */             boolean flag2 = false;
/*     */             
/* 164 */             for (int i = 0; (i < p_179533_1_.getSizeInventory()) && (flag); i++)
/*     */             {
/* 166 */               ItemStack itemstack = p_179533_1_.getStackInSlot(i);
/*     */               
/* 168 */               if ((itemstack != null) && (itemstack.getItem() != Items.banner))
/*     */               {
/* 170 */                 if (itemstack.getItem() == Items.dye)
/*     */                 {
/* 172 */                   if (flag2)
/*     */                   {
/* 174 */                     flag = false;
/* 175 */                     break;
/*     */                   }
/*     */                   
/* 178 */                   flag2 = true;
/*     */                 }
/*     */                 else
/*     */                 {
/* 182 */                   if ((flag1) || (!itemstack.isItemEqual(tileentitybanner$enumbannerpattern.getCraftingStack())))
/*     */                   {
/* 184 */                     flag = false;
/* 185 */                     break;
/*     */                   }
/*     */                   
/* 188 */                   flag1 = true;
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 193 */             if (!flag1)
/*     */             {
/* 195 */               flag = false;
/*     */             }
/*     */           }
/* 198 */           else if (p_179533_1_.getSizeInventory() == tileentitybanner$enumbannerpattern.getCraftingLayers().length * tileentitybanner$enumbannerpattern.getCraftingLayers()[0].length())
/*     */           {
/* 200 */             int j = -1;
/*     */             
/* 202 */             int k = 0;
/*     */             do {
/* 204 */               int l = k / 3;
/* 205 */               int i1 = k % 3;
/* 206 */               ItemStack itemstack1 = p_179533_1_.getStackInSlot(k);
/*     */               
/* 208 */               if ((itemstack1 != null) && (itemstack1.getItem() != Items.banner))
/*     */               {
/* 210 */                 if (itemstack1.getItem() != Items.dye)
/*     */                 {
/* 212 */                   flag = false;
/* 213 */                   break;
/*     */                 }
/*     */                 
/* 216 */                 if ((j != -1) && (j != itemstack1.getMetadata()))
/*     */                 {
/* 218 */                   flag = false;
/* 219 */                   break;
/*     */                 }
/*     */                 
/* 222 */                 if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l].charAt(i1) == ' ')
/*     */                 {
/* 224 */                   flag = false;
/* 225 */                   break;
/*     */                 }
/*     */                 
/* 228 */                 j = itemstack1.getMetadata();
/*     */               }
/* 230 */               else if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l].charAt(i1) != ' ')
/*     */               {
/* 232 */                 flag = false;
/* 233 */                 break;
/*     */               }
/* 202 */               k++; if (k >= p_179533_1_.getSizeInventory()) break; } while (flag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 239 */             flag = false;
/*     */           }
/*     */           
/* 242 */           if (flag)
/*     */           {
/* 244 */             return tileentitybanner$enumbannerpattern;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 249 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class RecipeDuplicatePattern
/*     */     implements IRecipe
/*     */   {
/*     */     public boolean matches(InventoryCrafting inv, World worldIn)
/*     */     {
/* 261 */       ItemStack itemstack = null;
/* 262 */       ItemStack itemstack1 = null;
/*     */       
/* 264 */       for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */       {
/* 266 */         ItemStack itemstack2 = inv.getStackInSlot(i);
/*     */         
/* 268 */         if (itemstack2 != null)
/*     */         {
/* 270 */           if (itemstack2.getItem() != Items.banner)
/*     */           {
/* 272 */             return false;
/*     */           }
/*     */           
/* 275 */           if ((itemstack != null) && (itemstack1 != null))
/*     */           {
/* 277 */             return false;
/*     */           }
/*     */           
/* 280 */           int j = TileEntityBanner.getBaseColor(itemstack2);
/* 281 */           boolean flag = TileEntityBanner.getPatterns(itemstack2) > 0;
/*     */           
/* 283 */           if (itemstack != null)
/*     */           {
/* 285 */             if (flag)
/*     */             {
/* 287 */               return false;
/*     */             }
/*     */             
/* 290 */             if (j != TileEntityBanner.getBaseColor(itemstack))
/*     */             {
/* 292 */               return false;
/*     */             }
/*     */             
/* 295 */             itemstack1 = itemstack2;
/*     */           }
/* 297 */           else if (itemstack1 != null)
/*     */           {
/* 299 */             if (!flag)
/*     */             {
/* 301 */               return false;
/*     */             }
/*     */             
/* 304 */             if (j != TileEntityBanner.getBaseColor(itemstack1))
/*     */             {
/* 306 */               return false;
/*     */             }
/*     */             
/* 309 */             itemstack = itemstack2;
/*     */           }
/* 311 */           else if (flag)
/*     */           {
/* 313 */             itemstack = itemstack2;
/*     */           }
/*     */           else
/*     */           {
/* 317 */             itemstack1 = itemstack2;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 322 */       return (itemstack != null) && (itemstack1 != null);
/*     */     }
/*     */     
/*     */     public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */     {
/* 327 */       for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */       {
/* 329 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/* 331 */         if ((itemstack != null) && (TileEntityBanner.getPatterns(itemstack) > 0))
/*     */         {
/* 333 */           ItemStack itemstack1 = itemstack.copy();
/* 334 */           itemstack1.stackSize = 1;
/* 335 */           return itemstack1;
/*     */         }
/*     */       }
/*     */       
/* 339 */       return null;
/*     */     }
/*     */     
/*     */     public int getRecipeSize()
/*     */     {
/* 344 */       return 2;
/*     */     }
/*     */     
/*     */     public ItemStack getRecipeOutput()
/*     */     {
/* 349 */       return null;
/*     */     }
/*     */     
/*     */     public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */     {
/* 354 */       ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */       
/* 356 */       for (int i = 0; i < aitemstack.length; i++)
/*     */       {
/* 358 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/* 360 */         if (itemstack != null)
/*     */         {
/* 362 */           if (itemstack.getItem().hasContainerItem())
/*     */           {
/* 364 */             aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */           }
/* 366 */           else if ((itemstack.hasTagCompound()) && (TileEntityBanner.getPatterns(itemstack) > 0))
/*     */           {
/* 368 */             aitemstack[i] = itemstack.copy();
/* 369 */             aitemstack[i].stackSize = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 374 */       return aitemstack;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesBanners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */