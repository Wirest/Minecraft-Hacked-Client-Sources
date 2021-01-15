/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ 
/*    */ public class GuiButtonLanguage extends GuiButton
/*    */ {
/*    */   public GuiButtonLanguage(int buttonID, int xPos, int yPos)
/*    */   {
/* 10 */     super(buttonID, xPos, yPos, 20, 20, "");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void drawButton(Minecraft mc, int mouseX, int mouseY)
/*    */   {
/* 18 */     if (this.visible)
/*    */     {
/* 20 */       mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
/* 21 */       net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 22 */       boolean flag = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height);
/* 23 */       int i = 106;
/*    */       
/* 25 */       if (flag)
/*    */       {
/* 27 */         i += this.height;
/*    */       }
/*    */       
/* 30 */       drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width, this.height);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiButtonLanguage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */