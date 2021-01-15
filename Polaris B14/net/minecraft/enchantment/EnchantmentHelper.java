/*     */ package net.minecraft.enchantment;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemEnchantedBook;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ 
/*     */ public class EnchantmentHelper
/*     */ {
/*  24 */   private static final Random enchantmentRand = new Random();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  29 */   private static final ModifierDamage enchantmentModifierDamage = new ModifierDamage(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  34 */   private static final ModifierLiving enchantmentModifierLiving = new ModifierLiving(null);
/*  35 */   private static final HurtIterator ENCHANTMENT_ITERATOR_HURT = new HurtIterator(null);
/*  36 */   private static final DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new DamageIterator(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getEnchantmentLevel(int enchID, ItemStack stack)
/*     */   {
/*  43 */     if (stack == null)
/*     */     {
/*  45 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  49 */     NBTTagList nbttaglist = stack.getEnchantmentTagList();
/*     */     
/*  51 */     if (nbttaglist == null)
/*     */     {
/*  53 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  57 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/*  59 */       int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/*  60 */       int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*     */       
/*  62 */       if (j == enchID)
/*     */       {
/*  64 */         return k;
/*     */       }
/*     */     }
/*     */     
/*  68 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Map<Integer, Integer> getEnchantments(ItemStack stack)
/*     */   {
/*  75 */     Map<Integer, Integer> map = Maps.newLinkedHashMap();
/*  76 */     NBTTagList nbttaglist = stack.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();
/*     */     
/*  78 */     if (nbttaglist != null)
/*     */     {
/*  80 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/*  82 */         int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/*  83 */         int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*  84 */         map.put(Integer.valueOf(j), Integer.valueOf(k));
/*     */       }
/*     */     }
/*     */     
/*  88 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setEnchantments(Map<Integer, Integer> enchMap, ItemStack stack)
/*     */   {
/*  96 */     NBTTagList nbttaglist = new NBTTagList();
/*  97 */     Iterator iterator = enchMap.keySet().iterator();
/*     */     
/*  99 */     while (iterator.hasNext())
/*     */     {
/* 101 */       int i = ((Integer)iterator.next()).intValue();
/* 102 */       Enchantment enchantment = Enchantment.getEnchantmentById(i);
/*     */       
/* 104 */       if (enchantment != null)
/*     */       {
/* 106 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 107 */         nbttagcompound.setShort("id", (short)i);
/* 108 */         nbttagcompound.setShort("lvl", (short)((Integer)enchMap.get(Integer.valueOf(i))).intValue());
/* 109 */         nbttaglist.appendTag(nbttagcompound);
/*     */         
/* 111 */         if (stack.getItem() == Items.enchanted_book)
/*     */         {
/* 113 */           Items.enchanted_book.addEnchantment(stack, new EnchantmentData(enchantment, ((Integer)enchMap.get(Integer.valueOf(i))).intValue()));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 118 */     if (nbttaglist.tagCount() > 0)
/*     */     {
/* 120 */       if (stack.getItem() != Items.enchanted_book)
/*     */       {
/* 122 */         stack.setTagInfo("ench", nbttaglist);
/*     */       }
/*     */     }
/* 125 */     else if (stack.hasTagCompound())
/*     */     {
/* 127 */       stack.getTagCompound().removeTag("ench");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks)
/*     */   {
/* 136 */     if (stacks == null)
/*     */     {
/* 138 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 142 */     int i = 0;
/*     */     ItemStack[] arrayOfItemStack;
/* 144 */     int j = (arrayOfItemStack = stacks).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*     */       
/* 146 */       int j = getEnchantmentLevel(enchID, itemstack);
/*     */       
/* 148 */       if (j > i)
/*     */       {
/* 150 */         i = j;
/*     */       }
/*     */     }
/*     */     
/* 154 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void applyEnchantmentModifier(IModifier modifier, ItemStack stack)
/*     */   {
/* 163 */     if (stack != null)
/*     */     {
/* 165 */       NBTTagList nbttaglist = stack.getEnchantmentTagList();
/*     */       
/* 167 */       if (nbttaglist != null)
/*     */       {
/* 169 */         for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */         {
/* 171 */           int j = nbttaglist.getCompoundTagAt(i).getShort("id");
/* 172 */           int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
/*     */           
/* 174 */           if (Enchantment.getEnchantmentById(j) != null)
/*     */           {
/* 176 */             modifier.calculateModifier(Enchantment.getEnchantmentById(j), k);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void applyEnchantmentModifierArray(IModifier modifier, ItemStack[] stacks)
/*     */   {
/*     */     ItemStack[] arrayOfItemStack;
/*     */     
/* 188 */     int j = (arrayOfItemStack = stacks).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*     */       
/* 190 */       applyEnchantmentModifier(modifier, itemstack);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source)
/*     */   {
/* 199 */     enchantmentModifierDamage.damageModifier = 0;
/* 200 */     enchantmentModifierDamage.source = source;
/* 201 */     applyEnchantmentModifierArray(enchantmentModifierDamage, stacks);
/*     */     
/* 203 */     if (enchantmentModifierDamage.damageModifier > 25)
/*     */     {
/* 205 */       enchantmentModifierDamage.damageModifier = 25;
/*     */     }
/* 207 */     else if (enchantmentModifierDamage.damageModifier < 0)
/*     */     {
/* 209 */       enchantmentModifierDamage.damageModifier = 0;
/*     */     }
/*     */     
/* 212 */     return (enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
/*     */   }
/*     */   
/*     */   public static float func_152377_a(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_)
/*     */   {
/* 217 */     enchantmentModifierLiving.livingModifier = 0.0F;
/* 218 */     enchantmentModifierLiving.entityLiving = p_152377_1_;
/* 219 */     applyEnchantmentModifier(enchantmentModifierLiving, p_152377_0_);
/* 220 */     return enchantmentModifierLiving.livingModifier;
/*     */   }
/*     */   
/*     */   public static void applyThornEnchantments(EntityLivingBase p_151384_0_, Entity p_151384_1_)
/*     */   {
/* 225 */     ENCHANTMENT_ITERATOR_HURT.attacker = p_151384_1_;
/* 226 */     ENCHANTMENT_ITERATOR_HURT.user = p_151384_0_;
/*     */     
/* 228 */     if (p_151384_0_ != null)
/*     */     {
/* 230 */       applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getInventory());
/*     */     }
/*     */     
/* 233 */     if ((p_151384_1_ instanceof EntityPlayer))
/*     */     {
/* 235 */       applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItem());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void applyArthropodEnchantments(EntityLivingBase p_151385_0_, Entity p_151385_1_)
/*     */   {
/* 241 */     ENCHANTMENT_ITERATOR_DAMAGE.user = p_151385_0_;
/* 242 */     ENCHANTMENT_ITERATOR_DAMAGE.target = p_151385_1_;
/*     */     
/* 244 */     if (p_151385_0_ != null)
/*     */     {
/* 246 */       applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getInventory());
/*     */     }
/*     */     
/* 249 */     if ((p_151385_0_ instanceof EntityPlayer))
/*     */     {
/* 251 */       applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItem());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getKnockbackModifier(EntityLivingBase player)
/*     */   {
/* 260 */     return getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getFireAspectModifier(EntityLivingBase player)
/*     */   {
/* 268 */     return getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getRespiration(Entity player)
/*     */   {
/* 276 */     return getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getDepthStriderModifier(Entity player)
/*     */   {
/* 284 */     return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getEfficiencyModifier(EntityLivingBase player)
/*     */   {
/* 292 */     return getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean getSilkTouchModifier(EntityLivingBase player)
/*     */   {
/* 300 */     return getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getFortuneModifier(EntityLivingBase player)
/*     */   {
/* 308 */     return getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getLuckOfSeaModifier(EntityLivingBase player)
/*     */   {
/* 316 */     return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getLureModifier(EntityLivingBase player)
/*     */   {
/* 324 */     return getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getLootingModifier(EntityLivingBase player)
/*     */   {
/* 332 */     return getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean getAquaAffinityModifier(EntityLivingBase player)
/*     */   {
/* 340 */     return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0;
/*     */   }
/*     */   
/*     */   public static ItemStack getEnchantedItem(Enchantment p_92099_0_, EntityLivingBase p_92099_1_) {
/*     */     ItemStack[] arrayOfItemStack;
/* 345 */     int j = (arrayOfItemStack = p_92099_1_.getInventory()).length; for (int i = 0; i < j; i++) { ItemStack itemstack = arrayOfItemStack[i];
/*     */       
/* 347 */       if ((itemstack != null) && (getEnchantmentLevel(p_92099_0_.effectId, itemstack) > 0))
/*     */       {
/* 349 */         return itemstack;
/*     */       }
/*     */     }
/*     */     
/* 353 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calcItemStackEnchantability(Random p_77514_0_, int p_77514_1_, int p_77514_2_, ItemStack p_77514_3_)
/*     */   {
/* 362 */     Item item = p_77514_3_.getItem();
/* 363 */     int i = item.getItemEnchantability();
/*     */     
/* 365 */     if (i <= 0)
/*     */     {
/* 367 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 371 */     if (p_77514_2_ > 15)
/*     */     {
/* 373 */       p_77514_2_ = 15;
/*     */     }
/*     */     
/* 376 */     int j = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
/* 377 */     return p_77514_1_ == 1 ? j * 2 / 3 + 1 : p_77514_1_ == 0 ? Math.max(j / 3, 1) : Math.max(j, p_77514_2_ * 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_)
/*     */   {
/* 386 */     List<EnchantmentData> list = buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
/* 387 */     boolean flag = p_77504_1_.getItem() == Items.book;
/*     */     
/* 389 */     if (flag)
/*     */     {
/* 391 */       p_77504_1_.setItem(Items.enchanted_book);
/*     */     }
/*     */     
/* 394 */     if (list != null)
/*     */     {
/* 396 */       for (EnchantmentData enchantmentdata : list)
/*     */       {
/* 398 */         if (flag)
/*     */         {
/* 400 */           Items.enchanted_book.addEnchantment(p_77504_1_, enchantmentdata);
/*     */         }
/*     */         else
/*     */         {
/* 404 */           p_77504_1_.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 409 */     return p_77504_1_;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentData> buildEnchantmentList(Random randomIn, ItemStack itemStackIn, int p_77513_2_)
/*     */   {
/* 414 */     Item item = itemStackIn.getItem();
/* 415 */     int i = item.getItemEnchantability();
/*     */     
/* 417 */     if (i <= 0)
/*     */     {
/* 419 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 423 */     i /= 2;
/* 424 */     i = 1 + randomIn.nextInt((i >> 1) + 1) + randomIn.nextInt((i >> 1) + 1);
/* 425 */     int j = i + p_77513_2_;
/* 426 */     float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
/* 427 */     int k = (int)(j * (1.0F + f) + 0.5F);
/*     */     
/* 429 */     if (k < 1)
/*     */     {
/* 431 */       k = 1;
/*     */     }
/*     */     
/* 434 */     List<EnchantmentData> list = null;
/* 435 */     Map<Integer, EnchantmentData> map = mapEnchantmentData(k, itemStackIn);
/*     */     
/* 437 */     if ((map != null) && (!map.isEmpty()))
/*     */     {
/* 439 */       EnchantmentData enchantmentdata = (EnchantmentData)WeightedRandom.getRandomItem(randomIn, map.values());
/*     */       
/* 441 */       if (enchantmentdata != null)
/*     */       {
/* 443 */         list = com.google.common.collect.Lists.newArrayList();
/* 444 */         list.add(enchantmentdata);
/*     */         
/* 446 */         for (int l = k; randomIn.nextInt(50) <= l; l >>= 1)
/*     */         {
/* 448 */           Iterator<Integer> iterator = map.keySet().iterator();
/*     */           
/* 450 */           while (iterator.hasNext())
/*     */           {
/* 452 */             Integer integer = (Integer)iterator.next();
/* 453 */             boolean flag = true;
/*     */             
/* 455 */             for (EnchantmentData enchantmentdata1 : list)
/*     */             {
/* 457 */               if (!enchantmentdata1.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(integer.intValue())))
/*     */               {
/* 459 */                 flag = false;
/* 460 */                 break;
/*     */               }
/*     */             }
/*     */             
/* 464 */             if (!flag)
/*     */             {
/* 466 */               iterator.remove();
/*     */             }
/*     */           }
/*     */           
/* 470 */           if (!map.isEmpty())
/*     */           {
/* 472 */             EnchantmentData enchantmentdata2 = (EnchantmentData)WeightedRandom.getRandomItem(randomIn, map.values());
/* 473 */             list.add(enchantmentdata2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 479 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Map<Integer, EnchantmentData> mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_)
/*     */   {
/* 485 */     Item item = p_77505_1_.getItem();
/* 486 */     Map<Integer, EnchantmentData> map = null;
/* 487 */     boolean flag = p_77505_1_.getItem() == Items.book;
/*     */     Enchantment[] arrayOfEnchantment;
/* 489 */     int j = (arrayOfEnchantment = Enchantment.enchantmentsBookList).length; for (int i = 0; i < j; i++) { Enchantment enchantment = arrayOfEnchantment[i];
/*     */       
/* 491 */       if ((enchantment != null) && ((enchantment.type.canEnchantItem(item)) || (flag)))
/*     */       {
/* 493 */         for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++)
/*     */         {
/* 495 */           if ((p_77505_0_ >= enchantment.getMinEnchantability(i)) && (p_77505_0_ <= enchantment.getMaxEnchantability(i)))
/*     */           {
/* 497 */             if (map == null)
/*     */             {
/* 499 */               map = Maps.newHashMap();
/*     */             }
/*     */             
/* 502 */             map.put(Integer.valueOf(enchantment.effectId), new EnchantmentData(enchantment, i));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 508 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */   static final class DamageIterator
/*     */     implements EnchantmentHelper.IModifier
/*     */   {
/*     */     public EntityLivingBase user;
/*     */     
/*     */     public Entity target;
/*     */     
/*     */ 
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel)
/*     */     {
/* 522 */       enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class HurtIterator
/*     */     implements EnchantmentHelper.IModifier
/*     */   {
/*     */     public EntityLivingBase user;
/*     */     
/*     */     public Entity attacker;
/*     */     
/*     */ 
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel)
/*     */     {
/* 537 */       enchantmentIn.onUserHurt(this.user, this.attacker, enchantmentLevel);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static abstract interface IModifier
/*     */   {
/*     */     public abstract void calculateModifier(Enchantment paramEnchantment, int paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */   static final class ModifierDamage
/*     */     implements EnchantmentHelper.IModifier
/*     */   {
/*     */     public int damageModifier;
/*     */     
/*     */     public DamageSource source;
/*     */     
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel)
/*     */     {
/* 557 */       this.damageModifier += enchantmentIn.calcModifierDamage(enchantmentLevel, this.source);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class ModifierLiving
/*     */     implements EnchantmentHelper.IModifier
/*     */   {
/*     */     public float livingModifier;
/*     */     
/*     */     public EnumCreatureAttribute entityLiving;
/*     */     
/*     */ 
/*     */     public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel)
/*     */     {
/* 572 */       this.livingModifier += enchantmentIn.calcDamageByCreature(enchantmentLevel, this.entityLiving);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */