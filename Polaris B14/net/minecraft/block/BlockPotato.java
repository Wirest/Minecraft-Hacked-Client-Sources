/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockPotato extends BlockCrops
/*    */ {
/*    */   protected Item getSeed()
/*    */   {
/* 14 */     return Items.potato;
/*    */   }
/*    */   
/*    */   protected Item getCrop()
/*    */   {
/* 19 */     return Items.potato;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*    */   {
/* 27 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*    */     
/* 29 */     if (!worldIn.isRemote)
/*    */     {
/* 31 */       if ((((Integer)state.getValue(AGE)).intValue() >= 7) && (worldIn.rand.nextInt(50) == 0))
/*    */       {
/* 33 */         spawnAsEntity(worldIn, pos, new net.minecraft.item.ItemStack(Items.poisonous_potato));
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPotato.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */