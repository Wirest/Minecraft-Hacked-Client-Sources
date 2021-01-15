/*    */ package net.minecraft.world.chunk;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.ObjectIntIdentityMap;
/*    */ 
/*    */ public class ChunkPrimer
/*    */ {
/*  9 */   private final short[] data = new short[65536];
/* 10 */   private final IBlockState defaultState = net.minecraft.init.Blocks.air.getDefaultState();
/*    */   
/*    */   public IBlockState getBlockState(int x, int y, int z)
/*    */   {
/* 14 */     int i = x << 12 | z << 8 | y;
/* 15 */     return getBlockState(i);
/*    */   }
/*    */   
/*    */   public IBlockState getBlockState(int index)
/*    */   {
/* 20 */     if ((index >= 0) && (index < this.data.length))
/*    */     {
/* 22 */       IBlockState iblockstate = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[index]);
/* 23 */       return iblockstate != null ? iblockstate : this.defaultState;
/*    */     }
/*    */     
/*    */ 
/* 27 */     throw new IndexOutOfBoundsException("The coordinate is out of range");
/*    */   }
/*    */   
/*    */ 
/*    */   public void setBlockState(int x, int y, int z, IBlockState state)
/*    */   {
/* 33 */     int i = x << 12 | z << 8 | y;
/* 34 */     setBlockState(i, state);
/*    */   }
/*    */   
/*    */   public void setBlockState(int index, IBlockState state)
/*    */   {
/* 39 */     if ((index >= 0) && (index < this.data.length))
/*    */     {
/* 41 */       this.data[index] = ((short)Block.BLOCK_STATE_IDS.get(state));
/*    */     }
/*    */     else
/*    */     {
/* 45 */       throw new IndexOutOfBoundsException("The coordinate is out of range");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\ChunkPrimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */