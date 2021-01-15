/*    */ package net.minecraft.client.renderer.vertex;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class VertexBuffer
/*    */ {
/*    */   private int glBufferId;
/*    */   private final VertexFormat vertexFormat;
/*    */   private int count;
/*    */   
/*    */   public VertexBuffer(VertexFormat vertexFormatIn)
/*    */   {
/* 15 */     this.vertexFormat = vertexFormatIn;
/* 16 */     this.glBufferId = OpenGlHelper.glGenBuffers();
/*    */   }
/*    */   
/*    */   public void bindBuffer()
/*    */   {
/* 21 */     OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.glBufferId);
/*    */   }
/*    */   
/*    */   public void func_181722_a(ByteBuffer p_181722_1_)
/*    */   {
/* 26 */     bindBuffer();
/* 27 */     OpenGlHelper.glBufferData(OpenGlHelper.GL_ARRAY_BUFFER, p_181722_1_, 35044);
/* 28 */     unbindBuffer();
/* 29 */     this.count = (p_181722_1_.limit() / this.vertexFormat.getNextOffset());
/*    */   }
/*    */   
/*    */   public void drawArrays(int mode)
/*    */   {
/* 34 */     GL11.glDrawArrays(mode, 0, this.count);
/*    */   }
/*    */   
/*    */   public void unbindBuffer()
/*    */   {
/* 39 */     OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
/*    */   }
/*    */   
/*    */   public void deleteGlBuffers()
/*    */   {
/* 44 */     if (this.glBufferId >= 0)
/*    */     {
/* 46 */       OpenGlHelper.glDeleteBuffers(this.glBufferId);
/* 47 */       this.glBufferId = -1;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\vertex\VertexBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */