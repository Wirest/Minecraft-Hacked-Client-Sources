/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ 
/*    */ public class BlockGlass extends BlockBreakable
/*    */ {
/*    */   public BlockGlass(Material materialIn, boolean ignoreSimilarity)
/*    */   {
/* 12 */     super(materialIn, ignoreSimilarity);
/* 13 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int quantityDropped(Random random)
/*    */   {
/* 21 */     return 0;
/*    */   }
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer()
/*    */   {
/* 26 */     return EnumWorldBlockLayer.CUTOUT;
/*    */   }
/*    */   
/*    */   public boolean isFullCube()
/*    */   {
/* 31 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean canSilkHarvest()
/*    */   {
/* 36 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockGlass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */