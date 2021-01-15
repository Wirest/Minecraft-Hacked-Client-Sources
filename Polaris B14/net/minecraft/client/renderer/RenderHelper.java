/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.nio.FloatBuffer;
/*    */ import net.minecraft.util.Vec3;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ public class RenderHelper
/*    */ {
/* 10 */   private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
/* 11 */   private static final Vec3 LIGHT0_POS = new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D).normalize();
/* 12 */   private static final Vec3 LIGHT1_POS = new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D).normalize();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void disableStandardItemLighting()
/*    */   {
/* 19 */     GlStateManager.disableLighting();
/* 20 */     GlStateManager.disableLight(0);
/* 21 */     GlStateManager.disableLight(1);
/* 22 */     GlStateManager.disableColorMaterial();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void enableStandardItemLighting()
/*    */   {
/* 30 */     GlStateManager.enableLighting();
/* 31 */     GlStateManager.enableLight(0);
/* 32 */     GlStateManager.enableLight(1);
/* 33 */     GlStateManager.enableColorMaterial();
/* 34 */     GlStateManager.colorMaterial(1032, 5634);
/* 35 */     float f = 0.4F;
/* 36 */     float f1 = 0.6F;
/* 37 */     float f2 = 0.0F;
/* 38 */     GL11.glLight(16384, 4611, setColorBuffer(LIGHT0_POS.xCoord, LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0D));
/* 39 */     GL11.glLight(16384, 4609, setColorBuffer(f1, f1, f1, 1.0F));
/* 40 */     GL11.glLight(16384, 4608, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
/* 41 */     GL11.glLight(16384, 4610, setColorBuffer(f2, f2, f2, 1.0F));
/* 42 */     GL11.glLight(16385, 4611, setColorBuffer(LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0D));
/* 43 */     GL11.glLight(16385, 4609, setColorBuffer(f1, f1, f1, 1.0F));
/* 44 */     GL11.glLight(16385, 4608, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
/* 45 */     GL11.glLight(16385, 4610, setColorBuffer(f2, f2, f2, 1.0F));
/* 46 */     GlStateManager.shadeModel(7424);
/* 47 */     GL11.glLightModel(2899, setColorBuffer(f, f, f, 1.0F));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_)
/*    */   {
/* 55 */     return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_)
/*    */   {
/* 63 */     colorBuffer.clear();
/* 64 */     colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
/* 65 */     colorBuffer.flip();
/* 66 */     return colorBuffer;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void enableGUIStandardItemLighting()
/*    */   {
/* 74 */     GlStateManager.pushMatrix();
/* 75 */     GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
/* 76 */     GlStateManager.rotate(165.0F, 1.0F, 0.0F, 0.0F);
/* 77 */     enableStandardItemLighting();
/* 78 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\RenderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */