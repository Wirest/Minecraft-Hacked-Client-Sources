/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecipeFireworks
/*     */   implements IRecipe
/*     */ {
/*     */   private ItemStack field_92102_a;
/*     */   
/*     */   public boolean matches(InventoryCrafting inv, World worldIn)
/*     */   {
/*  22 */     this.field_92102_a = null;
/*  23 */     int i = 0;
/*  24 */     int j = 0;
/*  25 */     int k = 0;
/*  26 */     int l = 0;
/*  27 */     int i1 = 0;
/*  28 */     int j1 = 0;
/*     */     
/*  30 */     for (int k1 = 0; k1 < inv.getSizeInventory(); k1++)
/*     */     {
/*  32 */       ItemStack itemstack = inv.getStackInSlot(k1);
/*     */       
/*  34 */       if (itemstack != null)
/*     */       {
/*  36 */         if (itemstack.getItem() == Items.gunpowder)
/*     */         {
/*  38 */           j++;
/*     */         }
/*  40 */         else if (itemstack.getItem() == Items.firework_charge)
/*     */         {
/*  42 */           l++;
/*     */         }
/*  44 */         else if (itemstack.getItem() == Items.dye)
/*     */         {
/*  46 */           k++;
/*     */         }
/*  48 */         else if (itemstack.getItem() == Items.paper)
/*     */         {
/*  50 */           i++;
/*     */         }
/*  52 */         else if (itemstack.getItem() == Items.glowstone_dust)
/*     */         {
/*  54 */           i1++;
/*     */         }
/*  56 */         else if (itemstack.getItem() == Items.diamond)
/*     */         {
/*  58 */           i1++;
/*     */         }
/*  60 */         else if (itemstack.getItem() == Items.fire_charge)
/*     */         {
/*  62 */           j1++;
/*     */         }
/*  64 */         else if (itemstack.getItem() == Items.feather)
/*     */         {
/*  66 */           j1++;
/*     */         }
/*  68 */         else if (itemstack.getItem() == Items.gold_nugget)
/*     */         {
/*  70 */           j1++;
/*     */         }
/*     */         else
/*     */         {
/*  74 */           if (itemstack.getItem() != Items.skull)
/*     */           {
/*  76 */             return false;
/*     */           }
/*     */           
/*  79 */           j1++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  84 */     i1 = i1 + k + j1;
/*     */     
/*  86 */     if ((j <= 3) && (i <= 1))
/*     */     {
/*  88 */       if ((j >= 1) && (i == 1) && (i1 == 0))
/*     */       {
/*  90 */         this.field_92102_a = new ItemStack(Items.fireworks);
/*     */         
/*  92 */         if (l > 0)
/*     */         {
/*  94 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  95 */           NBTTagCompound nbttagcompound3 = new NBTTagCompound();
/*  96 */           NBTTagList nbttaglist = new NBTTagList();
/*     */           
/*  98 */           for (int k2 = 0; k2 < inv.getSizeInventory(); k2++)
/*     */           {
/* 100 */             ItemStack itemstack3 = inv.getStackInSlot(k2);
/*     */             
/* 102 */             if ((itemstack3 != null) && (itemstack3.getItem() == Items.firework_charge) && (itemstack3.hasTagCompound()) && (itemstack3.getTagCompound().hasKey("Explosion", 10)))
/*     */             {
/* 104 */               nbttaglist.appendTag(itemstack3.getTagCompound().getCompoundTag("Explosion"));
/*     */             }
/*     */           }
/*     */           
/* 108 */           nbttagcompound3.setTag("Explosions", nbttaglist);
/* 109 */           nbttagcompound3.setByte("Flight", (byte)j);
/* 110 */           nbttagcompound1.setTag("Fireworks", nbttagcompound3);
/* 111 */           this.field_92102_a.setTagCompound(nbttagcompound1);
/*     */         }
/*     */         
/* 114 */         return true;
/*     */       }
/* 116 */       if ((j == 1) && (i == 0) && (l == 0) && (k > 0) && (j1 <= 1))
/*     */       {
/* 118 */         this.field_92102_a = new ItemStack(Items.firework_charge);
/* 119 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 120 */         NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/* 121 */         byte b0 = 0;
/* 122 */         List<Integer> list = Lists.newArrayList();
/*     */         
/* 124 */         for (int l1 = 0; l1 < inv.getSizeInventory(); l1++)
/*     */         {
/* 126 */           ItemStack itemstack2 = inv.getStackInSlot(l1);
/*     */           
/* 128 */           if (itemstack2 != null)
/*     */           {
/* 130 */             if (itemstack2.getItem() == Items.dye)
/*     */             {
/* 132 */               list.add(Integer.valueOf(net.minecraft.item.ItemDye.dyeColors[(itemstack2.getMetadata() & 0xF)]));
/*     */             }
/* 134 */             else if (itemstack2.getItem() == Items.glowstone_dust)
/*     */             {
/* 136 */               nbttagcompound2.setBoolean("Flicker", true);
/*     */             }
/* 138 */             else if (itemstack2.getItem() == Items.diamond)
/*     */             {
/* 140 */               nbttagcompound2.setBoolean("Trail", true);
/*     */             }
/* 142 */             else if (itemstack2.getItem() == Items.fire_charge)
/*     */             {
/* 144 */               b0 = 1;
/*     */             }
/* 146 */             else if (itemstack2.getItem() == Items.feather)
/*     */             {
/* 148 */               b0 = 4;
/*     */             }
/* 150 */             else if (itemstack2.getItem() == Items.gold_nugget)
/*     */             {
/* 152 */               b0 = 2;
/*     */             }
/* 154 */             else if (itemstack2.getItem() == Items.skull)
/*     */             {
/* 156 */               b0 = 3;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 161 */         int[] aint1 = new int[list.size()];
/*     */         
/* 163 */         for (int l2 = 0; l2 < aint1.length; l2++)
/*     */         {
/* 165 */           aint1[l2] = ((Integer)list.get(l2)).intValue();
/*     */         }
/*     */         
/* 168 */         nbttagcompound2.setIntArray("Colors", aint1);
/* 169 */         nbttagcompound2.setByte("Type", b0);
/* 170 */         nbttagcompound.setTag("Explosion", nbttagcompound2);
/* 171 */         this.field_92102_a.setTagCompound(nbttagcompound);
/* 172 */         return true;
/*     */       }
/* 174 */       if ((j == 0) && (i == 0) && (l == 1) && (k > 0) && (k == i1))
/*     */       {
/* 176 */         List<Integer> list1 = Lists.newArrayList();
/*     */         
/* 178 */         for (int i2 = 0; i2 < inv.getSizeInventory(); i2++)
/*     */         {
/* 180 */           ItemStack itemstack1 = inv.getStackInSlot(i2);
/*     */           
/* 182 */           if (itemstack1 != null)
/*     */           {
/* 184 */             if (itemstack1.getItem() == Items.dye)
/*     */             {
/* 186 */               list1.add(Integer.valueOf(net.minecraft.item.ItemDye.dyeColors[(itemstack1.getMetadata() & 0xF)]));
/*     */             }
/* 188 */             else if (itemstack1.getItem() == Items.firework_charge)
/*     */             {
/* 190 */               this.field_92102_a = itemstack1.copy();
/* 191 */               this.field_92102_a.stackSize = 1;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 196 */         int[] aint = new int[list1.size()];
/*     */         
/* 198 */         for (int j2 = 0; j2 < aint.length; j2++)
/*     */         {
/* 200 */           aint[j2] = ((Integer)list1.get(j2)).intValue();
/*     */         }
/*     */         
/* 203 */         if ((this.field_92102_a != null) && (this.field_92102_a.hasTagCompound()))
/*     */         {
/* 205 */           NBTTagCompound nbttagcompound4 = this.field_92102_a.getTagCompound().getCompoundTag("Explosion");
/*     */           
/* 207 */           if (nbttagcompound4 == null)
/*     */           {
/* 209 */             return false;
/*     */           }
/*     */           
/*     */ 
/* 213 */           nbttagcompound4.setIntArray("FadeColors", aint);
/* 214 */           return true;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 219 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 224 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 229 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv)
/*     */   {
/* 238 */     return this.field_92102_a.copy();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRecipeSize()
/*     */   {
/* 246 */     return 10;
/*     */   }
/*     */   
/*     */   public ItemStack getRecipeOutput()
/*     */   {
/* 251 */     return this.field_92102_a;
/*     */   }
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv)
/*     */   {
/* 256 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 258 */     for (int i = 0; i < aitemstack.length; i++)
/*     */     {
/* 260 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 262 */       if ((itemstack != null) && (itemstack.getItem().hasContainerItem()))
/*     */       {
/* 264 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     }
/*     */     
/* 268 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipeFireworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */