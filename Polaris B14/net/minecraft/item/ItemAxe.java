/*    */ package net.minecraft.item;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.init.Blocks;
/*    */ 
/*    */ public class ItemAxe extends ItemTool
/*    */ {
/* 11 */   private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder });
/*    */   
/*    */   protected ItemAxe(Item.ToolMaterial material)
/*    */   {
/* 15 */     super(3.0F, material, EFFECTIVE_ON);
/*    */   }
/*    */   
/*    */   public float getStrVsBlock(ItemStack stack, Block block)
/*    */   {
/* 20 */     return (block.getMaterial() != Material.wood) && (block.getMaterial() != Material.plants) && (block.getMaterial() != Material.vine) ? super.getStrVsBlock(stack, block) : this.efficiencyOnProperMaterial;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemAxe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */