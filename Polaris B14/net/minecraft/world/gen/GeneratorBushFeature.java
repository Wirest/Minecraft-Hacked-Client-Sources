/*    */ package net.minecraft.world.gen;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockBush;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.feature.WorldGenerator;
/*    */ 
/*    */ public class GeneratorBushFeature extends WorldGenerator
/*    */ {
/*    */   private BlockBush field_175908_a;
/*    */   
/*    */   public GeneratorBushFeature(BlockBush p_i45633_1_)
/*    */   {
/* 15 */     this.field_175908_a = p_i45633_1_;
/*    */   }
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 20 */     for (int i = 0; i < 64; i++)
/*    */     {
/* 22 */       BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
/*    */       
/* 24 */       if ((worldIn.isAirBlock(blockpos)) && ((!worldIn.provider.getHasNoSky()) || (blockpos.getY() < 255)) && (this.field_175908_a.canBlockStay(worldIn, blockpos, this.field_175908_a.getDefaultState())))
/*    */       {
/* 26 */         worldIn.setBlockState(blockpos, this.field_175908_a.getDefaultState(), 2);
/*    */       }
/*    */     }
/*    */     
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\GeneratorBushFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */