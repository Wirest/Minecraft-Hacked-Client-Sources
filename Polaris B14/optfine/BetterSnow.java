/*     */ package optfine;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoublePlant;
/*     */ import net.minecraft.block.BlockFence;
/*     */ import net.minecraft.block.BlockFenceGate;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockFlowerPot;
/*     */ import net.minecraft.block.BlockLever;
/*     */ import net.minecraft.block.BlockLever.EnumOrientation;
/*     */ import net.minecraft.block.BlockPane;
/*     */ import net.minecraft.block.BlockRedstoneTorch;
/*     */ import net.minecraft.block.BlockSapling;
/*     */ import net.minecraft.block.BlockSnow;
/*     */ import net.minecraft.block.BlockTallGrass;
/*     */ import net.minecraft.block.BlockTorch;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.BlockModelShapes;
/*     */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*     */ import net.minecraft.client.resources.model.IBakedModel;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ 
/*     */ public class BetterSnow
/*     */ {
/*  29 */   private static IBakedModel modelSnowLayer = null;
/*     */   
/*     */   public static void update()
/*     */   {
/*  33 */     modelSnowLayer = Config.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.snow_layer.getDefaultState());
/*     */   }
/*     */   
/*     */   public static IBakedModel getModelSnowLayer()
/*     */   {
/*  38 */     return modelSnowLayer;
/*     */   }
/*     */   
/*     */   public static IBlockState getStateSnowLayer()
/*     */   {
/*  43 */     return Blocks.snow_layer.getDefaultState();
/*     */   }
/*     */   
/*     */   public static boolean shouldRender(IBlockAccess p_shouldRender_0_, Block p_shouldRender_1_, IBlockState p_shouldRender_2_, BlockPos p_shouldRender_3_)
/*     */   {
/*  48 */     return !checkBlock(p_shouldRender_1_, p_shouldRender_2_) ? false : hasSnowNeighbours(p_shouldRender_0_, p_shouldRender_3_);
/*     */   }
/*     */   
/*     */   private static boolean hasSnowNeighbours(IBlockAccess p_hasSnowNeighbours_0_, BlockPos p_hasSnowNeighbours_1_)
/*     */   {
/*  53 */     Block block = Blocks.snow_layer;
/*  54 */     return (p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.north()).getBlock() != block) && (p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.south()).getBlock() != block) && (p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.west()).getBlock() != block) && (p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.east()).getBlock() != block) ? false : p_hasSnowNeighbours_0_.getBlockState(p_hasSnowNeighbours_1_.down()).getBlock().isOpaqueCube();
/*     */   }
/*     */   
/*     */   private static boolean checkBlock(Block p_checkBlock_0_, IBlockState p_checkBlock_1_)
/*     */   {
/*  59 */     if (p_checkBlock_0_.isFullCube())
/*     */     {
/*  61 */       return false;
/*     */     }
/*  63 */     if (p_checkBlock_0_.isOpaqueCube())
/*     */     {
/*  65 */       return false;
/*     */     }
/*  67 */     if ((p_checkBlock_0_ instanceof BlockSnow))
/*     */     {
/*  69 */       return false;
/*     */     }
/*  71 */     if ((!(p_checkBlock_0_ instanceof net.minecraft.block.BlockBush)) || ((!(p_checkBlock_0_ instanceof BlockDoublePlant)) && (!(p_checkBlock_0_ instanceof BlockFlower)) && (!(p_checkBlock_0_ instanceof net.minecraft.block.BlockMushroom)) && (!(p_checkBlock_0_ instanceof BlockSapling)) && (!(p_checkBlock_0_ instanceof BlockTallGrass))))
/*     */     {
/*  73 */       if ((!(p_checkBlock_0_ instanceof BlockFence)) && (!(p_checkBlock_0_ instanceof BlockFenceGate)) && (!(p_checkBlock_0_ instanceof BlockFlowerPot)) && (!(p_checkBlock_0_ instanceof BlockPane)) && (!(p_checkBlock_0_ instanceof net.minecraft.block.BlockReed)) && (!(p_checkBlock_0_ instanceof net.minecraft.block.BlockWall)))
/*     */       {
/*  75 */         if (((p_checkBlock_0_ instanceof BlockRedstoneTorch)) && (p_checkBlock_1_.getValue(BlockTorch.FACING) == EnumFacing.UP))
/*     */         {
/*  77 */           return true;
/*     */         }
/*     */         
/*     */ 
/*  81 */         if ((p_checkBlock_0_ instanceof BlockLever))
/*     */         {
/*  83 */           Object object = p_checkBlock_1_.getValue(BlockLever.FACING);
/*     */           
/*  85 */           if ((object == BlockLever.EnumOrientation.UP_X) || (object == BlockLever.EnumOrientation.UP_Z))
/*     */           {
/*  87 */             return true;
/*     */           }
/*     */         }
/*     */         
/*  91 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  96 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 101 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\BetterSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */