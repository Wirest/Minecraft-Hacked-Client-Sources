/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.utils.RenderUtils.R2DUtils;
/*     */ 
/*     */ public class GuiButton extends Gui
/*     */ {
/*  19 */   protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
/*     */   
/*     */ 
/*     */   protected int width;
/*     */   
/*     */   protected int height;
/*     */   
/*     */   public int xPosition;
/*     */   
/*     */   public int yPosition;
/*     */   
/*     */   public String displayString;
/*     */   
/*     */   public int id;
/*     */   
/*     */   int num;
/*     */   
/*     */   public boolean enabled;
/*     */   
/*     */   public boolean visible;
/*     */   
/*     */   protected boolean hovered;
/*     */   
/*     */   protected float hoverTransition;
/*     */   
/*     */   private static final String __OBFID = "CL_00000668";
/*     */   
/*     */ 
/*     */   public GuiButton(int buttonId, int x, int y, String buttonText)
/*     */   {
/*  49 */     this(buttonId, x, y, 200, 20, buttonText);
/*     */   }
/*     */   
/*     */   public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
/*     */   {
/*  54 */     this.width = 200;
/*  55 */     this.height = 20;
/*  56 */     this.enabled = true;
/*  57 */     this.visible = true;
/*  58 */     this.id = buttonId;
/*  59 */     this.xPosition = x;
/*  60 */     this.yPosition = y;
/*  61 */     this.width = widthIn;
/*  62 */     this.height = heightIn;
/*  63 */     this.displayString = buttonText;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getHoverState(boolean mouseOver)
/*     */   {
/*  72 */     byte var2 = 1;
/*     */     
/*  74 */     if (!this.enabled)
/*     */     {
/*  76 */       var2 = 0;
/*     */     }
/*  78 */     else if (mouseOver)
/*     */     {
/*  80 */       var2 = 2;
/*     */     }
/*     */     
/*  83 */     return var2;
/*     */   }
/*     */   
/*     */   private int meme1() {
/*  87 */     if (this.num <= 2) {
/*  88 */       this.num += 1;
/*     */     }
/*  90 */     if (this.num > 2) {
/*  91 */       this.num = 2;
/*     */     }
/*  93 */     return this.num;
/*     */   }
/*     */   
/*     */   private int meme2() {
/*  97 */     if (this.num <= 685) {
/*  98 */       this.num += 3;
/*     */     }
/* 100 */     if (this.num > 684) {
/* 101 */       this.num = -110;
/*     */     }
/* 103 */     return this.num;
/*     */   }
/*     */   
/*     */   private int meme3() {
/* 107 */     if (this.num <= 685) {
/* 108 */       this.num += 3;
/*     */     }
/* 110 */     if (this.num > 684) {
/* 111 */       this.num = -110;
/*     */     }
/* 113 */     return this.num;
/*     */   }
/*     */   
/*     */   private int meme4() {
/* 117 */     if (this.num <= 685) {
/* 118 */       this.num += 3;
/*     */     }
/* 120 */     if (this.num > 684) {
/* 121 */       this.num = -110;
/*     */     }
/* 123 */     return this.num;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawButton(Minecraft mc, int mouseX, int mouseY)
/*     */   {
/* 131 */     if (this.visible)
/*     */     {
/* 133 */       FontRenderer var4 = mc.fontRendererObj;
/* 134 */       mc.getTextureManager().bindTexture(buttonTextures);
/* 135 */       GlStateManager.color(255.0F, 1.0F, 1.0F, 1.0F);
/* 136 */       this.hovered = ((mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height));
/* 137 */       int var5 = getHoverState(this.hovered);
/* 138 */       GlStateManager.enableBlend();
/* 139 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 140 */       GlStateManager.blendFunc(770, 771);
/* 141 */       int color = 0;
/* 142 */       if (!this.enabled) {
/* 143 */         color = -13619152;
/* 144 */       } else if (this.hovered) {
/* 145 */         color = 
/*     */         
/* 147 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).darker().darker().getRGB();
/* 148 */         this.hoverTransition += (-2.0F - this.hoverTransition) / 2.0F;
/*     */       } else {
/* 150 */         this.hoverTransition += (1.0F - this.hoverTransition) / 2.0F;
/* 151 */         color = new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 152 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 153 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB();
/*     */       }
/*     */       
/* 156 */       RenderUtils.R2DUtils.drawBorderedRect(this.xPosition + this.hoverTransition / 1.0F, this.yPosition + this.hoverTransition / 1.0F, this.xPosition + this.width - this.hoverTransition / 1.0F, this.yPosition + this.height - this.hoverTransition / 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 157 */         color);
/* 158 */       mouseDragged(mc, mouseX, mouseY);
/* 159 */       int var6 = -1;
/*     */       
/* 161 */       if (!this.enabled)
/*     */       {
/* 163 */         var6 = 10526880;
/*     */       }
/* 165 */       else if (this.hovered)
/*     */       {
/* 167 */         var6 = -5264726;
/*     */       }
/*     */       
/* 170 */       CFontRenderer font = FontLoaders.vardana12;
/* 171 */       font.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 4) / 2, var6);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseReleased(int mouseX, int mouseY) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
/*     */   {
/* 191 */     return (this.enabled) && (this.visible) && (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMouseOver()
/*     */   {
/* 199 */     return this.hovered;
/*     */   }
/*     */   
/*     */   public void drawButtonForegroundLayer(int mouseX, int mouseY) {}
/*     */   
/*     */   public void playPressSound(SoundHandler soundHandlerIn)
/*     */   {
/* 206 */     soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
/*     */   }
/*     */   
/*     */   public int getButtonWidth()
/*     */   {
/* 211 */     return this.width;
/*     */   }
/*     */   
/*     */   public void func_175211_a(int p_175211_1_)
/*     */   {
/* 216 */     this.width = p_175211_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */