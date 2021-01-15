/*    */ package net.minecraft.util;
/*    */ 
/*    */ import org.lwjgl.input.Mouse;
/*    */ import org.lwjgl.opengl.Display;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MouseHelper
/*    */ {
/*    */   public int deltaX;
/*    */   public int deltaY;
/*    */   
/*    */   public void grabMouseCursor()
/*    */   {
/* 19 */     Mouse.setGrabbed(true);
/* 20 */     this.deltaX = 0;
/* 21 */     this.deltaY = 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void ungrabMouseCursor()
/*    */   {
/* 29 */     Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
/* 30 */     Mouse.setGrabbed(false);
/*    */   }
/*    */   
/*    */   public void mouseXYChange()
/*    */   {
/* 35 */     this.deltaX = Mouse.getDX();
/* 36 */     this.deltaY = Mouse.getDY();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MouseHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */