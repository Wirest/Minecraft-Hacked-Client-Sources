/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*    */ import net.minecraft.client.renderer.vertex.VertexBuffer;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class VboRenderList extends ChunkRenderContainer
/*    */ {
/*    */   public void renderChunkLayer(EnumWorldBlockLayer layer)
/*    */   {
/* 12 */     if (this.initialized)
/*    */     {
/* 14 */       for (RenderChunk renderchunk : this.renderChunks)
/*    */       {
/* 16 */         VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(layer.ordinal());
/* 17 */         GlStateManager.pushMatrix();
/* 18 */         preRenderChunk(renderchunk);
/* 19 */         renderchunk.multModelviewMatrix();
/* 20 */         vertexbuffer.bindBuffer();
/* 21 */         setupArrayPointers();
/* 22 */         vertexbuffer.drawArrays(7);
/* 23 */         GlStateManager.popMatrix();
/*    */       }
/*    */       
/* 26 */       OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
/* 27 */       GlStateManager.resetColor();
/* 28 */       this.renderChunks.clear();
/*    */     }
/*    */   }
/*    */   
/*    */   private void setupArrayPointers()
/*    */   {
/* 34 */     GL11.glVertexPointer(3, 5126, 28, 0L);
/* 35 */     GL11.glColorPointer(4, 5121, 28, 12L);
/* 36 */     GL11.glTexCoordPointer(2, 5126, 28, 16L);
/* 37 */     OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 38 */     GL11.glTexCoordPointer(2, 5122, 28, 24L);
/* 39 */     OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\VboRenderList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */