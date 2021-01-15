/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import net.minecraft.client.renderer.RenderGlobal;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ListChunkFactory implements IRenderChunkFactory
/*    */ {
/*    */   public RenderChunk makeRenderChunk(World worldIn, RenderGlobal globalRenderer, BlockPos pos, int index)
/*    */   {
/* 11 */     return new ListedRenderChunk(worldIn, globalRenderer, pos, index);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\ListChunkFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */