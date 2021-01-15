/*    */ package optfine;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockGrass;
/*    */ import net.minecraft.block.BlockMycelium;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.resources.model.IBakedModel;
/*    */ import net.minecraft.client.resources.model.SimpleBakedModel;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ 
/*    */ 
/*    */ public class BetterGrass
/*    */ {
/* 19 */   private static IBakedModel modelEmpty = new SimpleBakedModel(new ArrayList(), new ArrayList(), false, false, null, null);
/* 20 */   private static IBakedModel modelCubeMycelium = null;
/* 21 */   private static IBakedModel modelCubeGrassSnowy = null;
/* 22 */   private static IBakedModel modelCubeGrass = null;
/*    */   
/*    */   public static void update()
/*    */   {
/* 26 */     modelCubeGrass = BlockModelUtils.makeModelCube("minecraft:blocks/grass_top", 0);
/* 27 */     modelCubeGrassSnowy = BlockModelUtils.makeModelCube("minecraft:blocks/snow", -1);
/* 28 */     modelCubeMycelium = BlockModelUtils.makeModelCube("minecraft:blocks/mycelium_top", -1);
/*    */   }
/*    */   
/*    */   public static List getFaceQuads(IBlockAccess p_getFaceQuads_0_, Block p_getFaceQuads_1_, BlockPos p_getFaceQuads_2_, EnumFacing p_getFaceQuads_3_, List p_getFaceQuads_4_)
/*    */   {
/* 33 */     if ((p_getFaceQuads_3_ != EnumFacing.UP) && (p_getFaceQuads_3_ != EnumFacing.DOWN))
/*    */     {
/* 35 */       if ((p_getFaceQuads_1_ instanceof BlockMycelium))
/*    */       {
/* 37 */         return Config.isBetterGrassFancy() ? p_getFaceQuads_4_ : getBlockAt(p_getFaceQuads_2_.down(), p_getFaceQuads_3_, p_getFaceQuads_0_) == Blocks.mycelium ? modelCubeMycelium.getFaceQuads(p_getFaceQuads_3_) : modelCubeMycelium.getFaceQuads(p_getFaceQuads_3_);
/*    */       }
/*    */       
/*    */ 
/* 41 */       if ((p_getFaceQuads_1_ instanceof BlockGrass))
/*    */       {
/* 43 */         Block block = p_getFaceQuads_0_.getBlockState(p_getFaceQuads_2_.up()).getBlock();
/* 44 */         boolean flag = (block == Blocks.snow) || (block == Blocks.snow_layer);
/*    */         
/* 46 */         if (!Config.isBetterGrassFancy())
/*    */         {
/* 48 */           if (flag)
/*    */           {
/* 50 */             return modelCubeGrassSnowy.getFaceQuads(p_getFaceQuads_3_);
/*    */           }
/*    */           
/* 53 */           return modelCubeGrass.getFaceQuads(p_getFaceQuads_3_);
/*    */         }
/*    */         
/* 56 */         if (flag)
/*    */         {
/* 58 */           if (getBlockAt(p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_0_) == Blocks.snow_layer)
/*    */           {
/* 60 */             return modelCubeGrassSnowy.getFaceQuads(p_getFaceQuads_3_);
/*    */           }
/*    */         }
/* 63 */         else if (getBlockAt(p_getFaceQuads_2_.down(), p_getFaceQuads_3_, p_getFaceQuads_0_) == Blocks.grass)
/*    */         {
/* 65 */           return modelCubeGrass.getFaceQuads(p_getFaceQuads_3_);
/*    */         }
/*    */       }
/*    */       
/* 69 */       return p_getFaceQuads_4_;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 74 */     return p_getFaceQuads_4_;
/*    */   }
/*    */   
/*    */ 
/*    */   private static Block getBlockAt(BlockPos p_getBlockAt_0_, EnumFacing p_getBlockAt_1_, IBlockAccess p_getBlockAt_2_)
/*    */   {
/* 80 */     BlockPos blockpos = p_getBlockAt_0_.offset(p_getBlockAt_1_);
/* 81 */     Block block = p_getBlockAt_2_.getBlockState(blockpos).getBlock();
/* 82 */     return block;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\BetterGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */