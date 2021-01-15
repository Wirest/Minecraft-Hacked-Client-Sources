/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.realms.RealmsButton;
/*     */ 
/*     */ public class GuiButtonRealmsProxy extends GuiButton
/*     */ {
/*     */   private RealmsButton realmsButton;
/*     */   
/*     */   public GuiButtonRealmsProxy(RealmsButton realmsButtonIn, int buttonId, int x, int y, String text)
/*     */   {
/*  12 */     super(buttonId, x, y, text);
/*  13 */     this.realmsButton = realmsButtonIn;
/*     */   }
/*     */   
/*     */   public GuiButtonRealmsProxy(RealmsButton realmsButtonIn, int buttonId, int x, int y, String text, int widthIn, int heightIn)
/*     */   {
/*  18 */     super(buttonId, x, y, widthIn, heightIn, text);
/*  19 */     this.realmsButton = realmsButtonIn;
/*     */   }
/*     */   
/*     */   public int getId()
/*     */   {
/*  24 */     return this.id;
/*     */   }
/*     */   
/*     */   public boolean getEnabled()
/*     */   {
/*  29 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean isEnabled)
/*     */   {
/*  34 */     this.enabled = isEnabled;
/*     */   }
/*     */   
/*     */   public void setText(String text)
/*     */   {
/*  39 */     this.displayString = text;
/*     */   }
/*     */   
/*     */   public int getButtonWidth()
/*     */   {
/*  44 */     return super.getButtonWidth();
/*     */   }
/*     */   
/*     */   public int getPositionY()
/*     */   {
/*  49 */     return this.yPosition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
/*     */   {
/*  58 */     if (super.mousePressed(mc, mouseX, mouseY))
/*     */     {
/*  60 */       this.realmsButton.clicked(mouseX, mouseY);
/*     */     }
/*     */     
/*  63 */     return super.mousePressed(mc, mouseX, mouseY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseReleased(int mouseX, int mouseY)
/*     */   {
/*  71 */     this.realmsButton.released(mouseX, mouseY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseDragged(Minecraft mc, int mouseX, int mouseY)
/*     */   {
/*  79 */     this.realmsButton.renderBg(mouseX, mouseY);
/*     */   }
/*     */   
/*     */   public RealmsButton getRealmsButton()
/*     */   {
/*  84 */     return this.realmsButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHoverState(boolean mouseOver)
/*     */   {
/*  93 */     return this.realmsButton.getYImage(mouseOver);
/*     */   }
/*     */   
/*     */   public int func_154312_c(boolean p_154312_1_)
/*     */   {
/*  98 */     return super.getHoverState(p_154312_1_);
/*     */   }
/*     */   
/*     */   public int func_175232_g()
/*     */   {
/* 103 */     return this.height;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiButtonRealmsProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */