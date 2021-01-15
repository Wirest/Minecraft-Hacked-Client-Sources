/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.lwjgl.opengl.Display;
/*    */ 
/*    */ public class ScaleUtils
/*    */ {
/*    */   public static int getWidth()
/*    */   {
/* 11 */     return Display.getWidth() / getScaleFactor();
/*    */   }
/*    */   
/*    */   public static int getHeight() {
/* 15 */     return Display.getHeight() / getScaleFactor();
/*    */   }
/*    */   
/*    */   public static int getScaleFactor() {
/* 19 */     ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/* 20 */     return sr.getScaleFactor();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\ScaleUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */