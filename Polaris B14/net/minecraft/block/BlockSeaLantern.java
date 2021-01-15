/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class BlockSeaLantern extends Block
/*    */ {
/*    */   public BlockSeaLantern(Material materialIn)
/*    */   {
/* 16 */     super(materialIn);
/* 17 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int quantityDropped(Random random)
/*    */   {
/* 25 */     return 2 + random.nextInt(2);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int quantityDroppedWithBonus(int fortune, Random random)
/*    */   {
/* 33 */     return MathHelper.clamp_int(quantityDropped(random) + random.nextInt(fortune + 1), 1, 5);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*    */   {
/* 41 */     return Items.prismarine_crystals;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public MapColor getMapColor(IBlockState state)
/*    */   {
/* 49 */     return MapColor.quartzColor;
/*    */   }
/*    */   
/*    */   protected boolean canSilkHarvest()
/*    */   {
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockSeaLantern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */