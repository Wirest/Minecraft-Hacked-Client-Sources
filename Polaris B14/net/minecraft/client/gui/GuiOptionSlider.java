/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.client.settings.GameSettings.Options;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class GuiOptionSlider extends GuiButton
/*    */ {
/*    */   private float sliderValue;
/*    */   public boolean dragging;
/*    */   private GameSettings.Options options;
/*    */   private final float field_146132_r;
/*    */   private final float field_146131_s;
/*    */   
/*    */   public GuiOptionSlider(int p_i45016_1_, int p_i45016_2_, int p_i45016_3_, GameSettings.Options p_i45016_4_)
/*    */   {
/* 18 */     this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public GuiOptionSlider(int p_i45017_1_, int p_i45017_2_, int p_i45017_3_, GameSettings.Options p_i45017_4_, float p_i45017_5_, float p_i45017_6_)
/*    */   {
/* 23 */     super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
/* 24 */     this.sliderValue = 1.0F;
/* 25 */     this.options = p_i45017_4_;
/* 26 */     this.field_146132_r = p_i45017_5_;
/* 27 */     this.field_146131_s = p_i45017_6_;
/* 28 */     Minecraft minecraft = Minecraft.getMinecraft();
/* 29 */     this.sliderValue = p_i45017_4_.normalizeValue(minecraft.gameSettings.getOptionFloatValue(p_i45017_4_));
/* 30 */     this.displayString = minecraft.gameSettings.getKeyBinding(p_i45017_4_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int getHoverState(boolean mouseOver)
/*    */   {
/* 39 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
/*    */   {
/* 47 */     if (this.visible)
/*    */     {
/* 49 */       if (this.dragging)
/*    */       {
/* 51 */         this.sliderValue = ((mouseX - (this.xPosition + 4)) / (this.width - 8));
/* 52 */         this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
/* 53 */         float f = this.options.denormalizeValue(this.sliderValue);
/* 54 */         mc.gameSettings.setOptionFloatValue(this.options, f);
/* 55 */         this.sliderValue = this.options.normalizeValue(f);
/* 56 */         this.displayString = mc.gameSettings.getKeyBinding(this.options);
/*    */       }
/*    */       
/* 59 */       mc.getTextureManager().bindTexture(buttonTextures);
/* 60 */       net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 61 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
/* 62 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
/*    */   {
/* 72 */     if (super.mousePressed(mc, mouseX, mouseY))
/*    */     {
/* 74 */       this.sliderValue = ((mouseX - (this.xPosition + 4)) / (this.width - 8));
/* 75 */       this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
/* 76 */       mc.gameSettings.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
/* 77 */       this.displayString = mc.gameSettings.getKeyBinding(this.options);
/* 78 */       this.dragging = true;
/* 79 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 83 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void mouseReleased(int mouseX, int mouseY)
/*    */   {
/* 92 */     this.dragging = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiOptionSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */