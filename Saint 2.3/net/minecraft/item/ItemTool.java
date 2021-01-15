package net.minecraft.item;

import com.google.common.collect.Multimap;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemTool extends Item {
   private Set effectiveBlocksTool;
   protected float efficiencyOnProperMaterial = 4.0F;
   private float damageVsEntity;
   protected Item.ToolMaterial toolMaterial;
   private static final String __OBFID = "CL_00000019";

   protected ItemTool(float p_i45333_1_, Item.ToolMaterial p_i45333_2_, Set p_i45333_3_) {
      this.toolMaterial = p_i45333_2_;
      this.effectiveBlocksTool = p_i45333_3_;
      this.maxStackSize = 1;
      this.setMaxDamage(p_i45333_2_.getMaxUses());
      this.efficiencyOnProperMaterial = p_i45333_2_.getEfficiencyOnProperMaterial();
      this.damageVsEntity = p_i45333_1_ + p_i45333_2_.getDamageVsEntity();
      this.setCreativeTab(CreativeTabs.tabTools);
   }

   public float getStrVsBlock(ItemStack stack, Block p_150893_2_) {
      return this.effectiveBlocksTool.contains(p_150893_2_) ? this.efficiencyOnProperMaterial : 1.0F;
   }

   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
      stack.damageItem(2, attacker);
      return true;
   }

   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
      if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
         stack.damageItem(1, playerIn);
      }

      return true;
   }

   public boolean isFull3D() {
      return true;
   }

   public Item.ToolMaterial getToolMaterial() {
      return this.toolMaterial;
   }

   public int getItemEnchantability() {
      return this.toolMaterial.getEnchantability();
   }

   public String getToolMaterialName() {
      return this.toolMaterial.toString();
   }

   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
      return this.toolMaterial.getBaseItemForRepair() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
   }

   public Multimap getItemAttributeModifiers() {
      Multimap var1 = super.getItemAttributeModifiers();
      var1.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", (double)this.damageVsEntity, 0));
      return var1;
   }
}
