package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemAxe extends ItemTool {
   private static final Set EFFECTIVE_ON;
   private final Item.ToolMaterial material;

   static {
      EFFECTIVE_ON = Sets.newHashSet(new Block[]{Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder});
   }

   protected ItemAxe(Item.ToolMaterial material) {
      super(3.0F, material, EFFECTIVE_ON);
      this.material = material;
   }

   public float getStrVsBlock(ItemStack stack, Block block) {
      return block.getMaterial() != Material.wood && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine?super.getStrVsBlock(stack, block):this.efficiencyOnProperMaterial;
   }

   public float getDamageVsEntity() {
      return this.material.getDamageVsEntity();
   }
}
