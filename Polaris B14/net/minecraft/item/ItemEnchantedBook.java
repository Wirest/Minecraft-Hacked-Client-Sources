/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentData;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.WeightedRandomChestContent;
/*     */ 
/*     */ public class ItemEnchantedBook extends Item
/*     */ {
/*     */   public boolean hasEffect(ItemStack stack)
/*     */   {
/*  18 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isItemTool(ItemStack stack)
/*     */   {
/*  26 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumRarity getRarity(ItemStack stack)
/*     */   {
/*  34 */     return getEnchantments(stack).tagCount() > 0 ? EnumRarity.UNCOMMON : super.getRarity(stack);
/*     */   }
/*     */   
/*     */   public NBTTagList getEnchantments(ItemStack stack)
/*     */   {
/*  39 */     NBTTagCompound nbttagcompound = stack.getTagCompound();
/*  40 */     return (nbttagcompound != null) && (nbttagcompound.hasKey("StoredEnchantments", 9)) ? (NBTTagList)nbttagcompound.getTag("StoredEnchantments") : new NBTTagList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/*  48 */     super.addInformation(stack, playerIn, tooltip, advanced);
/*  49 */     NBTTagList nbttaglist = getEnchantments(stack);
/*     */     
/*  51 */     if (nbttaglist != null)
/*     */     {
/*  53 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/*  55 */         int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/*  56 */         int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*     */         
/*  58 */         if (Enchantment.getEnchantmentById(j) != null)
/*     */         {
/*  60 */           tooltip.add(Enchantment.getEnchantmentById(j).getTranslatedName(k));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEnchantment(ItemStack stack, EnchantmentData enchantment)
/*     */   {
/*  71 */     NBTTagList nbttaglist = getEnchantments(stack);
/*  72 */     boolean flag = true;
/*     */     
/*  74 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/*  76 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/*     */       
/*  78 */       if (nbttagcompound.getShort("id") == enchantment.enchantmentobj.effectId)
/*     */       {
/*  80 */         if (nbttagcompound.getShort("lvl") < enchantment.enchantmentLevel)
/*     */         {
/*  82 */           nbttagcompound.setShort("lvl", (short)enchantment.enchantmentLevel);
/*     */         }
/*     */         
/*  85 */         flag = false;
/*  86 */         break;
/*     */       }
/*     */     }
/*     */     
/*  90 */     if (flag)
/*     */     {
/*  92 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  93 */       nbttagcompound1.setShort("id", (short)enchantment.enchantmentobj.effectId);
/*  94 */       nbttagcompound1.setShort("lvl", (short)enchantment.enchantmentLevel);
/*  95 */       nbttaglist.appendTag(nbttagcompound1);
/*     */     }
/*     */     
/*  98 */     if (!stack.hasTagCompound())
/*     */     {
/* 100 */       stack.setTagCompound(new NBTTagCompound());
/*     */     }
/*     */     
/* 103 */     stack.getTagCompound().setTag("StoredEnchantments", nbttaglist);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getEnchantedItemStack(EnchantmentData data)
/*     */   {
/* 111 */     ItemStack itemstack = new ItemStack(this);
/* 112 */     addEnchantment(itemstack, data);
/* 113 */     return itemstack;
/*     */   }
/*     */   
/*     */   public void getAll(Enchantment enchantment, List<ItemStack> list)
/*     */   {
/* 118 */     for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++)
/*     */     {
/* 120 */       list.add(getEnchantedItemStack(new EnchantmentData(enchantment, i)));
/*     */     }
/*     */   }
/*     */   
/*     */   public WeightedRandomChestContent getRandom(Random rand)
/*     */   {
/* 126 */     return getRandom(rand, 1, 1, 1);
/*     */   }
/*     */   
/*     */   public WeightedRandomChestContent getRandom(Random rand, int minChance, int maxChance, int weight)
/*     */   {
/* 131 */     ItemStack itemstack = new ItemStack(Items.book, 1, 0);
/* 132 */     EnchantmentHelper.addRandomEnchantment(rand, itemstack, 30);
/* 133 */     return new WeightedRandomChestContent(itemstack, minChance, maxChance, weight);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemEnchantedBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */