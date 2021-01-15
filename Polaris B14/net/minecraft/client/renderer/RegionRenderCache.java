/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.ChunkCache;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.Chunk;
/*    */ import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
/*    */ 
/*    */ public class RegionRenderCache extends ChunkCache
/*    */ {
/* 15 */   private static final IBlockState DEFAULT_STATE = net.minecraft.init.Blocks.air.getDefaultState();
/*    */   private final BlockPos position;
/*    */   private int[] combinedLights;
/*    */   private IBlockState[] blockStates;
/*    */   
/*    */   public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn)
/*    */   {
/* 22 */     super(worldIn, posFromIn, posToIn, subIn);
/* 23 */     this.position = posFromIn.subtract(new net.minecraft.util.Vec3i(subIn, subIn, subIn));
/* 24 */     int i = 8000;
/* 25 */     this.combinedLights = new int['ὀ'];
/* 26 */     Arrays.fill(this.combinedLights, -1);
/* 27 */     this.blockStates = new IBlockState['ὀ'];
/*    */   }
/*    */   
/*    */   public TileEntity getTileEntity(BlockPos pos)
/*    */   {
/* 32 */     int i = (pos.getX() >> 4) - this.chunkX;
/* 33 */     int j = (pos.getZ() >> 4) - this.chunkZ;
/* 34 */     return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.QUEUED);
/*    */   }
/*    */   
/*    */   public int getCombinedLight(BlockPos pos, int lightValue)
/*    */   {
/* 39 */     int i = getPositionIndex(pos);
/* 40 */     int j = this.combinedLights[i];
/*    */     
/* 42 */     if (j == -1)
/*    */     {
/* 44 */       j = super.getCombinedLight(pos, lightValue);
/* 45 */       this.combinedLights[i] = j;
/*    */     }
/*    */     
/* 48 */     return j;
/*    */   }
/*    */   
/*    */   public IBlockState getBlockState(BlockPos pos)
/*    */   {
/* 53 */     int i = getPositionIndex(pos);
/* 54 */     IBlockState iblockstate = this.blockStates[i];
/*    */     
/* 56 */     if (iblockstate == null)
/*    */     {
/* 58 */       iblockstate = getBlockStateRaw(pos);
/* 59 */       this.blockStates[i] = iblockstate;
/*    */     }
/*    */     
/* 62 */     return iblockstate;
/*    */   }
/*    */   
/*    */   private IBlockState getBlockStateRaw(BlockPos pos)
/*    */   {
/* 67 */     if ((pos.getY() >= 0) && (pos.getY() < 256))
/*    */     {
/* 69 */       int i = (pos.getX() >> 4) - this.chunkX;
/* 70 */       int j = (pos.getZ() >> 4) - this.chunkZ;
/* 71 */       return this.chunkArray[i][j].getBlockState(pos);
/*    */     }
/*    */     
/*    */ 
/* 75 */     return DEFAULT_STATE;
/*    */   }
/*    */   
/*    */ 
/*    */   private int getPositionIndex(BlockPos p_175630_1_)
/*    */   {
/* 81 */     int i = p_175630_1_.getX() - this.position.getX();
/* 82 */     int j = p_175630_1_.getY() - this.position.getY();
/* 83 */     int k = p_175630_1_.getZ() - this.position.getZ();
/* 84 */     return i * 400 + k * 20 + j;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\RegionRenderCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */