/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
/*    */ import java.nio.FloatBuffer;
/*    */ import java.nio.IntBuffer;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.lwjgl.util.glu.GLU;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GLAllocation
/*    */ {
/*    */   public static synchronized int generateDisplayLists(int range)
/*    */   {
/* 18 */     int i = GL11.glGenLists(range);
/*    */     
/* 20 */     if (i == 0)
/*    */     {
/* 22 */       int j = GL11.glGetError();
/* 23 */       String s = "No error code reported";
/*    */       
/* 25 */       if (j != 0)
/*    */       {
/* 27 */         s = GLU.gluErrorString(j);
/*    */       }
/*    */       
/* 30 */       throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + range + ", GL error (" + j + "): " + s);
/*    */     }
/*    */     
/*    */ 
/* 34 */     return i;
/*    */   }
/*    */   
/*    */ 
/*    */   public static synchronized void deleteDisplayLists(int list, int range)
/*    */   {
/* 40 */     GL11.glDeleteLists(list, range);
/*    */   }
/*    */   
/*    */   public static synchronized void deleteDisplayLists(int list)
/*    */   {
/* 45 */     GL11.glDeleteLists(list, 1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized ByteBuffer createDirectByteBuffer(int capacity)
/*    */   {
/* 53 */     return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static IntBuffer createDirectIntBuffer(int capacity)
/*    */   {
/* 61 */     return createDirectByteBuffer(capacity << 2).asIntBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static FloatBuffer createDirectFloatBuffer(int capacity)
/*    */   {
/* 70 */     return createDirectByteBuffer(capacity << 2).asFloatBuffer();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\GLAllocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */