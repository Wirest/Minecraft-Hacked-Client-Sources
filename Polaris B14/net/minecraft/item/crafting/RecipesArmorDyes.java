/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class RecipesArmorDyes
/*     */   implements IRecipe
/*     */ {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn)
/*     */   {
/*  20 */     ItemStack itemstack = null;
/*  21 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  23 */     for (int i = 0; i < inv.getSizeInventory(); i++)
/*     */     {
/*  25 */       ItemStack itemstack1 = inv.getStackInSlot(i);
/*     */       
/*  27 */       if (itemstack1 != null)
/*     */       {
/*  29 */         if ((itemstack1.getItem() instanceof ItemArmor))
/*     */         {
/*  31 */           ItemArmor itemarmor = (ItemArmor)itemstack1.getItem();
/*     */           
/*  33 */           if ((itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) || (itemstack != null))
/*     */           {
/*  35 */             return false;
/*     */           }
/*     */           
/*  38 */           itemstack = itemstack1;
/*     */         }
/*     */         else
/*     */         {
/*  42 */           if (itemstack1.getItem() != Items.dye)
/*     */           {
/*  44 */             return false;
/*     */           }
/*     */           
/*  47 */           list.add(itemstack1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  52 */     return (itemstack != null) && (!list.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */   {
/*  60 */     ItemStack itemstack = null;
/*  61 */     int[] aint = new int[3];
/*  62 */     int i = 0;
/*  63 */     int j = 0;
/*  64 */     ItemArmor itemarmor = null;
/*     */     
/*  66 */     for (int k = 0; k < inv.getSizeInventory(); k++)
/*     */     {
/*  68 */       ItemStack itemstack1 = inv.getStackInSlot(k);
/*     */       
/*  70 */       if (itemstack1 != null)
/*     */       {
/*  72 */         if ((itemstack1.getItem() instanceof ItemArmor))
/*     */         {
/*  74 */           itemarmor = (ItemArmor)itemstack1.getItem();
/*     */           
/*  76 */           if ((itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) || (itemstack != null))
/*     */           {
/*  78 */             return null;
/*     */           }
/*     */           
/*  81 */           itemstack = itemstack1.copy();
/*  82 */           itemstack.stackSize = 1;
/*     */           
/*  84 */           if (itemarmor.hasColor(itemstack1))
/*     */           {
/*  86 */             int l = itemarmor.getColor(itemstack);
/*  87 */             float f = (l >> 16 & 0xFF) / 255.0F;
/*  88 */             float f1 = (l >> 8 & 0xFF) / 255.0F;
/*  89 */             float f2 = (l & 0xFF) / 255.0F;
/*  90 */             i = (int)(i + Math.max(f, Math.max(f1, f2)) * 255.0F);
/*  91 */             aint[0] = ((int)(aint[0] + f * 255.0F));
/*  92 */             aint[1] = ((int)(aint[1] + f1 * 255.0F));
/*  93 */             aint[2] = ((int)(aint[2] + f2 * 255.0F));
/*  94 */             j++;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  99 */           if (itemstack1.getItem() != Items.dye)
/*     */           {
/* 101 */             return null;
/*     */           }
/*     */           
/* 104 */           float[] afloat = EntitySheep.func_175513_a(EnumDyeColor.byDyeDamage(itemstack1.getMetadata()));
/* 105 */           int l1 = (int)(afloat[0] * 255.0F);
/* 106 */           int i2 = (int)(afloat[1] * 255.0F);
/* 107 */           int j2 = (int)(afloat[2] * 255.0F);
/* 108 */           i += Math.max(l1, Math.max(i2, j2));
/* 109 */           aint[0] += l1;
/* 110 */           aint[1] += i2;
/* 111 */           aint[2] += j2;
/* 112 */           j++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 117 */     if (itemarmor == null)
/*     */     {
/* 119 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 123 */     int i1 = aint[0] / j;
/* 124 */     int j1 = aint[1] / j;
/* 125 */     int k1 = aint[2] / j;
/* 126 */     float f3 = i / j;
/* 127 */     float f4 = Math.max(i1, Math.max(j1, k1));
/* 128 */     i1 = (int)(i1 * f3 / f4);
/* 129 */     j1 = (int)(j1 * f3 / f4);
/* 130 */     k1 = (int)(k1 * f3 / f4);
/* 131 */     int lvt_12_3_ = (i1 << 8) + j1;
/* 132 */     lvt_12_3_ = (lvt_12_3_ << 8) + k1;
/* 133 */     itemarmor.setColor(itemstack, lvt_12_3_);
/* 134 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRecipeSize()
/*     */   {
/* 143 */     return 10;
/*     */   }
/*     */   
/*     */   public ItemStack getRecipeOutput()
/*     */   {
/* 148 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */   {
/* 153 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 155 */     for (int i = 0; i < aitemstack.length; i++)
/*     */     {
/* 157 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 159 */       if ((itemstack != null) && (itemstack.getItem().hasContainerItem()))
/*     */       {
/* 161 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     }
/*     */     
/* 165 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesArmorDyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */