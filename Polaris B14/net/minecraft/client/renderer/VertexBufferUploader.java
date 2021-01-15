/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.client.renderer.vertex.VertexBuffer;
/*    */ 
/*    */ public class VertexBufferUploader extends WorldVertexBufferUploader
/*    */ {
/*  7 */   private VertexBuffer vertexBuffer = null;
/*    */   
/*    */   public void func_181679_a(WorldRenderer p_181679_1_)
/*    */   {
/* 11 */     p_181679_1_.reset();
/* 12 */     this.vertexBuffer.func_181722_a(p_181679_1_.getByteBuffer());
/*    */   }
/*    */   
/*    */   public void setVertexBuffer(VertexBuffer vertexBufferIn)
/*    */   {
/* 17 */     this.vertexBuffer = vertexBufferIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\VertexBufferUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */