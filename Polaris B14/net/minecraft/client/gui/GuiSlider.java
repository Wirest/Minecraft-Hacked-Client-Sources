/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ 
/*     */ public class GuiSlider extends GuiButton
/*     */ {
/*   8 */   private float sliderPosition = 1.0F;
/*     */   public boolean isMouseDown;
/*     */   private String name;
/*     */   private final float min;
/*     */   private final float max;
/*     */   private final GuiPageButtonList.GuiResponder responder;
/*     */   private FormatHelper formatHelper;
/*     */   
/*     */   public GuiSlider(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String name, float min, float max, float defaultValue, FormatHelper formatter) {
/*  17 */     super(idIn, x, y, 150, 20, "");
/*  18 */     this.name = name;
/*  19 */     this.min = min;
/*  20 */     this.max = max;
/*  21 */     this.sliderPosition = ((defaultValue - min) / (max - min));
/*  22 */     this.formatHelper = formatter;
/*  23 */     this.responder = guiResponder;
/*  24 */     this.displayString = getDisplayString();
/*     */   }
/*     */   
/*     */   public float func_175220_c() {
/*  28 */     return this.min + (this.max - this.min) * this.sliderPosition;
/*     */   }
/*     */   
/*     */   public void func_175218_a(float p_175218_1_, boolean p_175218_2_) {
/*  32 */     this.sliderPosition = ((p_175218_1_ - this.min) / (this.max - this.min));
/*  33 */     this.displayString = getDisplayString();
/*  34 */     if (p_175218_2_) {
/*  35 */       this.responder.onTick(this.id, func_175220_c());
/*     */     }
/*     */   }
/*     */   
/*     */   public float func_175217_d() {
/*  40 */     return this.sliderPosition;
/*     */   }
/*     */   
/*     */   private String getDisplayString() {
/*  44 */     return this.formatHelper == null ? I18n.format(this.name, new Object[0]) + ": " + func_175220_c() : this.formatHelper.getText(this.id, I18n.format(this.name, new Object[0]), func_175220_c());
/*     */   }
/*     */   
/*     */   protected int getHoverState(boolean mouseOver) {
/*  48 */     return 0;
/*     */   }
/*     */   
/*     */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
/*  52 */     if (this.visible) {
/*  53 */       if (this.isMouseDown) {
/*  54 */         this.sliderPosition = ((mouseX - (this.xPosition + 4)) / (this.width - 8));
/*  55 */         if (this.sliderPosition < 0.0F) {
/*  56 */           this.sliderPosition = 0.0F;
/*     */         }
/*     */         
/*  59 */         if (this.sliderPosition > 1.0F) {
/*  60 */           this.sliderPosition = 1.0F;
/*     */         }
/*     */         
/*  63 */         this.displayString = getDisplayString();
/*  64 */         this.responder.onTick(this.id, func_175220_c());
/*     */       }
/*     */       
/*  67 */       net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  68 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
/*  69 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_175219_a(float p_175219_1_) {
/*  74 */     this.sliderPosition = p_175219_1_;
/*  75 */     this.displayString = getDisplayString();
/*  76 */     this.responder.onTick(this.id, func_175220_c());
/*     */   }
/*     */   
/*     */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/*  80 */     if (super.mousePressed(mc, mouseX, mouseY)) {
/*  81 */       this.sliderPosition = ((mouseX - (this.xPosition + 4)) / (this.width - 8));
/*  82 */       if (this.sliderPosition < 0.0F) {
/*  83 */         this.sliderPosition = 0.0F;
/*     */       }
/*     */       
/*  86 */       if (this.sliderPosition > 1.0F) {
/*  87 */         this.sliderPosition = 1.0F;
/*     */       }
/*     */       
/*  90 */       this.displayString = getDisplayString();
/*  91 */       this.responder.onTick(this.id, func_175220_c());
/*  92 */       this.isMouseDown = true;
/*  93 */       return true;
/*     */     }
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY)
/*     */   {
/* 100 */     this.isMouseDown = false;
/*     */   }
/*     */   
/*     */   public static abstract interface FormatHelper
/*     */   {
/*     */     public abstract String getText(int paramInt, String paramString, float paramFloat);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */