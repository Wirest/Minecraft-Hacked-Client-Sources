/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagIntArray;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ public class ItemFireworkCharge extends Item
/*     */ {
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass)
/*     */   {
/*  14 */     if (renderPass != 1)
/*     */     {
/*  16 */       return super.getColorFromItemStack(stack, renderPass);
/*     */     }
/*     */     
/*     */ 
/*  20 */     NBTBase nbtbase = getExplosionTag(stack, "Colors");
/*     */     
/*  22 */     if (!(nbtbase instanceof NBTTagIntArray))
/*     */     {
/*  24 */       return 9079434;
/*     */     }
/*     */     
/*     */ 
/*  28 */     NBTTagIntArray nbttagintarray = (NBTTagIntArray)nbtbase;
/*  29 */     int[] aint = nbttagintarray.getIntArray();
/*     */     
/*  31 */     if (aint.length == 1)
/*     */     {
/*  33 */       return aint[0];
/*     */     }
/*     */     
/*     */ 
/*  37 */     int i = 0;
/*  38 */     int j = 0;
/*  39 */     int k = 0;
/*     */     int[] arrayOfInt1;
/*  41 */     int j = (arrayOfInt1 = aint).length; for (int i = 0; i < j; i++) { int l = arrayOfInt1[i];
/*     */       
/*  43 */       i += ((l & 0xFF0000) >> 16);
/*  44 */       j += ((l & 0xFF00) >> 8);
/*  45 */       k += ((l & 0xFF) >> 0);
/*     */     }
/*     */     
/*  48 */     i /= aint.length;
/*  49 */     j /= aint.length;
/*  50 */     k /= aint.length;
/*  51 */     return i << 16 | j << 8 | k;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NBTBase getExplosionTag(ItemStack stack, String key)
/*     */   {
/*  59 */     if (stack.hasTagCompound())
/*     */     {
/*  61 */       NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
/*     */       
/*  63 */       if (nbttagcompound != null)
/*     */       {
/*  65 */         return nbttagcompound.getTag(key);
/*     */       }
/*     */     }
/*     */     
/*  69 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/*  77 */     if (stack.hasTagCompound())
/*     */     {
/*  79 */       NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
/*     */       
/*  81 */       if (nbttagcompound != null)
/*     */       {
/*  83 */         addExplosionInfo(nbttagcompound, tooltip);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addExplosionInfo(NBTTagCompound nbt, List<String> tooltip)
/*     */   {
/*  90 */     byte b0 = nbt.getByte("Type");
/*     */     
/*  92 */     if ((b0 >= 0) && (b0 <= 4))
/*     */     {
/*  94 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type." + b0).trim());
/*     */     }
/*     */     else
/*     */     {
/*  98 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
/*     */     }
/*     */     
/* 101 */     int[] aint = nbt.getIntArray("Colors");
/*     */     int j;
/* 103 */     boolean flag1; if (aint.length > 0)
/*     */     {
/* 105 */       boolean flag = true;
/* 106 */       String s = "";
/*     */       int[] arrayOfInt1;
/* 108 */       j = (arrayOfInt1 = aint).length; for (int i = 0; i < j; i++) { int i = arrayOfInt1[i];
/*     */         
/* 110 */         if (!flag)
/*     */         {
/* 112 */           s = s + ", ";
/*     */         }
/*     */         
/* 115 */         flag = false;
/* 116 */         flag1 = false;
/*     */         
/* 118 */         for (int j = 0; j < ItemDye.dyeColors.length; j++)
/*     */         {
/* 120 */           if (i == ItemDye.dyeColors[j])
/*     */           {
/* 122 */             flag1 = true;
/* 123 */             s = s + StatCollector.translateToLocal(new StringBuilder("item.fireworksCharge.").append(EnumDyeColor.byDyeDamage(j).getUnlocalizedName()).toString());
/* 124 */             break;
/*     */           }
/*     */         }
/*     */         
/* 128 */         if (!flag1)
/*     */         {
/* 130 */           s = s + StatCollector.translateToLocal("item.fireworksCharge.customColor");
/*     */         }
/*     */       }
/*     */       
/* 134 */       tooltip.add(s);
/*     */     }
/*     */     
/* 137 */     int[] aint1 = nbt.getIntArray("FadeColors");
/*     */     
/* 139 */     if (aint1.length > 0)
/*     */     {
/* 141 */       boolean flag2 = true;
/* 142 */       String s1 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
/*     */       
/* 144 */       int k = (flag1 = aint1).length; for (j = 0; j < k; j++) { int l = flag1[j];
/*     */         
/* 146 */         if (!flag2)
/*     */         {
/* 148 */           s1 = s1 + ", ";
/*     */         }
/*     */         
/* 151 */         flag2 = false;
/* 152 */         boolean flag5 = false;
/*     */         
/* 154 */         for (int k = 0; k < 16; k++)
/*     */         {
/* 156 */           if (l == ItemDye.dyeColors[k])
/*     */           {
/* 158 */             flag5 = true;
/* 159 */             s1 = s1 + StatCollector.translateToLocal(new StringBuilder("item.fireworksCharge.").append(EnumDyeColor.byDyeDamage(k).getUnlocalizedName()).toString());
/* 160 */             break;
/*     */           }
/*     */         }
/*     */         
/* 164 */         if (!flag5)
/*     */         {
/* 166 */           s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
/*     */         }
/*     */       }
/*     */       
/* 170 */       tooltip.add(s1);
/*     */     }
/*     */     
/* 173 */     boolean flag3 = nbt.getBoolean("Trail");
/*     */     
/* 175 */     if (flag3)
/*     */     {
/* 177 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
/*     */     }
/*     */     
/* 180 */     boolean flag4 = nbt.getBoolean("Flicker");
/*     */     
/* 182 */     if (flag4)
/*     */     {
/* 184 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemFireworkCharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */