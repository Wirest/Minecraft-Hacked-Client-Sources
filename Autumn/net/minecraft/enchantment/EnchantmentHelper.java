package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom;

public class EnchantmentHelper {
   private static final Random enchantmentRand = new Random();
   private static final EnchantmentHelper.ModifierDamage enchantmentModifierDamage = new EnchantmentHelper.ModifierDamage();
   private static final EnchantmentHelper.ModifierLiving enchantmentModifierLiving = new EnchantmentHelper.ModifierLiving();
   private static final EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT = new EnchantmentHelper.HurtIterator();
   private static final EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new EnchantmentHelper.DamageIterator();

   public static int getEnchantmentLevel(int enchID, ItemStack stack) {
      if (stack == null) {
         return 0;
      } else {
         NBTTagList nbttaglist = stack.getEnchantmentTagList();
         if (nbttaglist == null) {
            return 0;
         } else {
            for(int i = 0; i < nbttaglist.tagCount(); ++i) {
               int j = nbttaglist.getCompoundTagAt(i).getShort("id");
               int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
               if (j == enchID) {
                  return k;
               }
            }

            return 0;
         }
      }
   }

   public static Map getEnchantments(ItemStack stack) {
      Map map = Maps.newLinkedHashMap();
      NBTTagList nbttaglist = stack.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();
      if (nbttaglist != null) {
         for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            int j = nbttaglist.getCompoundTagAt(i).getShort("id");
            int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
            map.put(Integer.valueOf(j), Integer.valueOf(k));
         }
      }

      return map;
   }

   public static void setEnchantments(Map enchMap, ItemStack stack) {
      NBTTagList nbttaglist = new NBTTagList();
      Iterator iterator = enchMap.keySet().iterator();

      while(iterator.hasNext()) {
         int i = (Integer)iterator.next();
         Enchantment enchantment = Enchantment.getEnchantmentById(i);
         if (enchantment != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setShort("id", (short)i);
            nbttagcompound.setShort("lvl", (short)(Integer)enchMap.get(i));
            nbttaglist.appendTag(nbttagcompound);
            if (stack.getItem() == Items.enchanted_book) {
               Items.enchanted_book.addEnchantment(stack, new EnchantmentData(enchantment, (Integer)enchMap.get(i)));
            }
         }
      }

      if (nbttaglist.tagCount() > 0) {
         if (stack.getItem() != Items.enchanted_book) {
            stack.setTagInfo("ench", nbttaglist);
         }
      } else if (stack.hasTagCompound()) {
         stack.getTagCompound().removeTag("ench");
      }

   }

   public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks) {
      if (stacks == null) {
         return 0;
      } else {
         int i = 0;
         ItemStack[] var3 = stacks;
         int var4 = stacks.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ItemStack itemstack = var3[var5];
            int j = getEnchantmentLevel(enchID, itemstack);
            if (j > i) {
               i = j;
            }
         }

         return i;
      }
   }

   private static void applyEnchantmentModifier(EnchantmentHelper.IModifier modifier, ItemStack stack) {
      if (stack != null) {
         NBTTagList nbttaglist = stack.getEnchantmentTagList();
         if (nbttaglist != null) {
            for(int i = 0; i < nbttaglist.tagCount(); ++i) {
               int j = nbttaglist.getCompoundTagAt(i).getShort("id");
               int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
               if (Enchantment.getEnchantmentById(j) != null) {
                  modifier.calculateModifier(Enchantment.getEnchantmentById(j), k);
               }
            }
         }
      }

   }

   private static void applyEnchantmentModifierArray(EnchantmentHelper.IModifier modifier, ItemStack[] stacks) {
      ItemStack[] var2 = stacks;
      int var3 = stacks.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack itemstack = var2[var4];
         applyEnchantmentModifier(modifier, itemstack);
      }

   }

   public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source) {
      enchantmentModifierDamage.damageModifier = 0;
      enchantmentModifierDamage.source = source;
      applyEnchantmentModifierArray(enchantmentModifierDamage, stacks);
      if (enchantmentModifierDamage.damageModifier > 25) {
         enchantmentModifierDamage.damageModifier = 25;
      } else if (enchantmentModifierDamage.damageModifier < 0) {
         enchantmentModifierDamage.damageModifier = 0;
      }

      return (enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
   }

   public static float func_152377_a(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_) {
      enchantmentModifierLiving.livingModifier = 0.0F;
      enchantmentModifierLiving.entityLiving = p_152377_1_;
      applyEnchantmentModifier(enchantmentModifierLiving, p_152377_0_);
      return enchantmentModifierLiving.livingModifier;
   }

   public static void applyThornEnchantments(EntityLivingBase p_151384_0_, Entity p_151384_1_) {
      ENCHANTMENT_ITERATOR_HURT.attacker = p_151384_1_;
      ENCHANTMENT_ITERATOR_HURT.user = p_151384_0_;
      if (p_151384_0_ != null) {
         applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getInventory());
      }

      if (p_151384_1_ instanceof EntityPlayer) {
         applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, p_151384_0_.getHeldItem());
      }

   }

   public static void applyArthropodEnchantments(EntityLivingBase p_151385_0_, Entity p_151385_1_) {
      ENCHANTMENT_ITERATOR_DAMAGE.user = p_151385_0_;
      ENCHANTMENT_ITERATOR_DAMAGE.target = p_151385_1_;
      if (p_151385_0_ != null) {
         applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getInventory());
      }

      if (p_151385_0_ instanceof EntityPlayer) {
         applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, p_151385_0_.getHeldItem());
      }

   }

   public static int getKnockbackModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
   }

   public static int getFireAspectModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
   }

   public static int getRespiration(Entity player) {
      return getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
   }

   public static int getDepthStriderModifier(Entity player) {
      return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
   }

   public static int getEfficiencyModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
   }

   public static boolean getSilkTouchModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0;
   }

   public static int getFortuneModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
   }

   public static int getLuckOfSeaModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
   }

   public static int getLureModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
   }

   public static int getLootingModifier(EntityLivingBase player) {
      return getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
   }

   public static boolean getAquaAffinityModifier(EntityLivingBase player) {
      return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0;
   }

   public static ItemStack getEnchantedItem(Enchantment p_92099_0_, EntityLivingBase p_92099_1_) {
      ItemStack[] var2 = p_92099_1_.getInventory();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack itemstack = var2[var4];
         if (itemstack != null && getEnchantmentLevel(p_92099_0_.effectId, itemstack) > 0) {
            return itemstack;
         }
      }

      return null;
   }

   public static int calcItemStackEnchantability(Random p_77514_0_, int p_77514_1_, int p_77514_2_, ItemStack p_77514_3_) {
      Item item = p_77514_3_.getItem();
      int i = item.getItemEnchantability();
      if (i <= 0) {
         return 0;
      } else {
         if (p_77514_2_ > 15) {
            p_77514_2_ = 15;
         }

         int j = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
         return p_77514_1_ == 0 ? Math.max(j / 3, 1) : (p_77514_1_ == 1 ? j * 2 / 3 + 1 : Math.max(j, p_77514_2_ * 2));
      }
   }

   public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_) {
      List list = buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
      boolean flag = p_77504_1_.getItem() == Items.book;
      if (flag) {
         p_77504_1_.setItem(Items.enchanted_book);
      }

      if (list != null) {
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            EnchantmentData enchantmentdata = (EnchantmentData)var5.next();
            if (flag) {
               Items.enchanted_book.addEnchantment(p_77504_1_, enchantmentdata);
            } else {
               p_77504_1_.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
            }
         }
      }

      return p_77504_1_;
   }

   public static List buildEnchantmentList(Random randomIn, ItemStack itemStackIn, int p_77513_2_) {
      Item item = itemStackIn.getItem();
      int i = item.getItemEnchantability();
      if (i <= 0) {
         return null;
      } else {
         i /= 2;
         i = 1 + randomIn.nextInt((i >> 1) + 1) + randomIn.nextInt((i >> 1) + 1);
         int j = i + p_77513_2_;
         float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
         int k = (int)((float)j * (1.0F + f) + 0.5F);
         if (k < 1) {
            k = 1;
         }

         List list = null;
         Map map = mapEnchantmentData(k, itemStackIn);
         if (map != null && !map.isEmpty()) {
            EnchantmentData enchantmentdata = (EnchantmentData)WeightedRandom.getRandomItem(randomIn, map.values());
            if (enchantmentdata != null) {
               list = Lists.newArrayList();
               list.add(enchantmentdata);

               for(int l = k; randomIn.nextInt(50) <= l; l >>= 1) {
                  Iterator iterator = map.keySet().iterator();

                  while(iterator.hasNext()) {
                     Integer integer = (Integer)iterator.next();
                     boolean flag = true;
                     Iterator var15 = list.iterator();

                     while(var15.hasNext()) {
                        EnchantmentData enchantmentdata1 = (EnchantmentData)var15.next();
                        if (!enchantmentdata1.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(integer))) {
                           flag = false;
                           break;
                        }
                     }

                     if (!flag) {
                        iterator.remove();
                     }
                  }

                  if (!map.isEmpty()) {
                     EnchantmentData enchantmentdata2 = (EnchantmentData)WeightedRandom.getRandomItem(randomIn, map.values());
                     list.add(enchantmentdata2);
                  }
               }
            }
         }

         return list;
      }
   }

   public static Map mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_) {
      Item item = p_77505_1_.getItem();
      Map map = null;
      boolean flag = p_77505_1_.getItem() == Items.book;
      Enchantment[] var5 = Enchantment.enchantmentsBookList;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Enchantment enchantment = var5[var7];
         if (enchantment != null && (enchantment.type.canEnchantItem(item) || flag)) {
            for(int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
               if (p_77505_0_ >= enchantment.getMinEnchantability(i) && p_77505_0_ <= enchantment.getMaxEnchantability(i)) {
                  if (map == null) {
                     map = Maps.newHashMap();
                  }

                  map.put(enchantment.effectId, new EnchantmentData(enchantment, i));
               }
            }
         }
      }

      return map;
   }

   static final class ModifierLiving implements EnchantmentHelper.IModifier {
      public float livingModifier;
      public EnumCreatureAttribute entityLiving;

      private ModifierLiving() {
      }

      public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
         this.livingModifier += enchantmentIn.calcDamageByCreature(enchantmentLevel, this.entityLiving);
      }
   }

   static final class ModifierDamage implements EnchantmentHelper.IModifier {
      public int damageModifier;
      public DamageSource source;

      private ModifierDamage() {
      }

      public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
         this.damageModifier += enchantmentIn.calcModifierDamage(enchantmentLevel, this.source);
      }
   }

   interface IModifier {
      void calculateModifier(Enchantment var1, int var2);
   }

   static final class HurtIterator implements EnchantmentHelper.IModifier {
      public EntityLivingBase user;
      public Entity attacker;

      private HurtIterator() {
      }

      public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
         enchantmentIn.onUserHurt(this.user, this.attacker, enchantmentLevel);
      }
   }

   static final class DamageIterator implements EnchantmentHelper.IModifier {
      public EntityLivingBase user;
      public Entity target;

      private DamageIterator() {
      }

      public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
         enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
      }
   }
}
