package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemSword extends Item {
   private float field_150934_a;
   private final Item.ToolMaterial repairMaterial;
   private static final String __OBFID = "CL_00000072";

   public ItemSword(Item.ToolMaterial p_i45356_1_) {
      this.repairMaterial = p_i45356_1_;
      this.maxStackSize = 1;
      this.setMaxDamage(p_i45356_1_.getMaxUses());
      this.setCreativeTab(CreativeTabs.tabCombat);
      this.field_150934_a = 4.0F + p_i45356_1_.getDamageVsEntity();
   }

   public float func_150931_i() {
      return this.repairMaterial.getDamageVsEntity();
   }

   public float getStrVsBlock(ItemStack stack, Block p_150893_2_) {
      if (p_150893_2_ == Blocks.web) {
         return 15.0F;
      } else {
         Material var3 = p_150893_2_.getMaterial();
         return var3 != Material.plants && var3 != Material.vine && var3 != Material.coral && var3 != Material.leaves && var3 != Material.gourd ? 1.0F : 1.5F;
      }
   }

   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
      stack.damageItem(1, attacker);
      return true;
   }

   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
      if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
         stack.damageItem(2, playerIn);
      }

      return true;
   }

   public boolean isFull3D() {
      return true;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.BLOCK;
   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return 72000;
   }

   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
      playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
      return itemStackIn;
   }

   public boolean canHarvestBlock(Block blockIn) {
      return blockIn == Blocks.web;
   }

   public int getItemEnchantability() {
      return this.repairMaterial.getEnchantability();
   }

   public String getToolMaterialName() {
      return this.repairMaterial.toString();
   }

   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
      return this.repairMaterial.getBaseItemForRepair() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
   }

   public Multimap getItemAttributeModifiers() {
      Multimap var1 = super.getItemAttributeModifiers();
      var1.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.field_150934_a, 0));
      return var1;
   }
}
