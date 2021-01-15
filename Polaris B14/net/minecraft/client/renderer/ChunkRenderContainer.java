/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ 
/*    */ public abstract class ChunkRenderContainer
/*    */ {
/*    */   private double viewEntityX;
/*    */   private double viewEntityY;
/*    */   private double viewEntityZ;
/* 14 */   protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
/*    */   protected boolean initialized;
/*    */   
/*    */   public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn)
/*    */   {
/* 19 */     this.initialized = true;
/* 20 */     this.renderChunks.clear();
/* 21 */     this.viewEntityX = viewEntityXIn;
/* 22 */     this.viewEntityY = viewEntityYIn;
/* 23 */     this.viewEntityZ = viewEntityZIn;
/*    */   }
/*    */   
/*    */   public void preRenderChunk(RenderChunk renderChunkIn)
/*    */   {
/* 28 */     BlockPos blockpos = renderChunkIn.getPosition();
/* 29 */     GlStateManager.translate((float)(blockpos.getX() - this.viewEntityX), (float)(blockpos.getY() - this.viewEntityY), (float)(blockpos.getZ() - this.viewEntityZ));
/*    */   }
/*    */   
/*    */   public void addRenderChunk(RenderChunk renderChunkIn, EnumWorldBlockLayer layer)
/*    */   {
/* 34 */     this.renderChunks.add(renderChunkIn);
/*    */   }
/*    */   
/*    */   public abstract void renderChunkLayer(EnumWorldBlockLayer paramEnumWorldBlockLayer);
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\ChunkRenderContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */