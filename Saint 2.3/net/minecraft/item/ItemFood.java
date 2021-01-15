package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemFood extends Item {
   public final int itemUseDuration;
   private final int healAmount;
   private final float saturationModifier;
   private final boolean isWolfsFavoriteMeat;
   private boolean alwaysEdible;
   private int potionId;
   private int potionDuration;
   private int potionAmplifier;
   private float potionEffectProbability;
   private static final String __OBFID = "CL_00000036";

   public ItemFood(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_) {
      this.itemUseDuration = 32;
      this.healAmount = p_i45339_1_;
      this.isWolfsFavoriteMeat = p_i45339_3_;
      this.saturationModifier = p_i45339_2_;
      this.setCreativeTab(CreativeTabs.tabFood);
   }

   public ItemFood(int p_i45340_1_, boolean p_i45340_2_) {
      this(p_i45340_1_, 0.6F, p_i45340_2_);
   }

   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
      --stack.stackSize;
      playerIn.getFoodStats().addStats(this, stack);
      worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
      this.onFoodEaten(stack, worldIn, playerIn);
      playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
      return stack;
   }

   protected void onFoodEaten(ItemStack p_77849_1_, World worldIn, EntityPlayer p_77849_3_) {
      if (!worldIn.isRemote && this.potionId > 0 && worldIn.rand.nextFloat() < this.potionEffectProbability) {
         p_77849_3_.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
      }

   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return 32;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.EAT;
   }

   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
      if (playerIn.canEat(this.alwaysEdible)) {
         playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
      }

      return itemStackIn;
   }

   public int getHealAmount(ItemStack itemStackIn) {
      return this.healAmount;
   }

   public float getSaturationModifier(ItemStack itemStackIn) {
      return this.saturationModifier;
   }

   public boolean isWolfsFavoriteMeat() {
      return this.isWolfsFavoriteMeat;
   }

   public ItemFood setPotionEffect(int p_77844_1_, int duration, int amplifier, float probability) {
      this.potionId = p_77844_1_;
      this.potionDuration = duration;
      this.potionAmplifier = amplifier;
      this.potionEffectProbability = probability;
      return this;
   }

   public ItemFood setAlwaysEdible() {
      this.alwaysEdible = true;
      return this;
   }
}
