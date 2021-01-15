/*     */ package net.minecraft.client;
/*     */ 
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import net.minecraft.util.MinecraftError;
/*     */ 
/*     */ public class LoadingScreenRenderer implements net.minecraft.util.IProgressUpdate
/*     */ {
/*  16 */   private String message = "";
/*     */   
/*     */ 
/*     */ 
/*     */   private Minecraft mc;
/*     */   
/*     */ 
/*     */ 
/*  24 */   private String currentlyDisplayedText = "";
/*     */   
/*     */ 
/*  27 */   private long systemTime = Minecraft.getSystemTime();
/*     */   private boolean field_73724_e;
/*     */   private ScaledResolution scaledResolution;
/*     */   private Framebuffer framebuffer;
/*     */   
/*     */   public LoadingScreenRenderer(Minecraft mcIn)
/*     */   {
/*  34 */     this.mc = mcIn;
/*  35 */     this.scaledResolution = new ScaledResolution(mcIn, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  36 */     this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
/*  37 */     this.framebuffer.setFramebufferFilter(9728);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetProgressAndMessage(String message)
/*     */   {
/*  46 */     this.field_73724_e = false;
/*  47 */     displayString(message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void displaySavingString(String message)
/*     */   {
/*  55 */     this.field_73724_e = true;
/*  56 */     displayString(message);
/*     */   }
/*     */   
/*     */   private void displayString(String message)
/*     */   {
/*  61 */     this.currentlyDisplayedText = message;
/*     */     
/*  63 */     if (!this.mc.running)
/*     */     {
/*  65 */       if (!this.field_73724_e)
/*     */       {
/*  67 */         throw new MinecraftError();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  72 */       GlStateManager.clear(256);
/*  73 */       GlStateManager.matrixMode(5889);
/*  74 */       GlStateManager.loadIdentity();
/*     */       
/*  76 */       if (OpenGlHelper.isFramebufferEnabled())
/*     */       {
/*  78 */         int i = this.scaledResolution.getScaleFactor();
/*  79 */         GlStateManager.ortho(0.0D, ScaledResolution.getScaledWidth() * i, ScaledResolution.getScaledHeight() * i, 0.0D, 100.0D, 300.0D);
/*     */       }
/*     */       else
/*     */       {
/*  83 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  84 */         GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
/*     */       }
/*     */       
/*  87 */       GlStateManager.matrixMode(5888);
/*  88 */       GlStateManager.loadIdentity();
/*  89 */       GlStateManager.translate(0.0F, 0.0F, -200.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void displayLoadingString(String message)
/*     */   {
/*  98 */     if (!this.mc.running)
/*     */     {
/* 100 */       if (!this.field_73724_e)
/*     */       {
/* 102 */         throw new MinecraftError();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 107 */       this.systemTime = 0L;
/* 108 */       this.message = message;
/* 109 */       setLoadingProgress(-1);
/* 110 */       this.systemTime = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLoadingProgress(int progress)
/*     */   {
/* 119 */     if (!this.mc.running)
/*     */     {
/* 121 */       if (!this.field_73724_e)
/*     */       {
/* 123 */         throw new MinecraftError();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 128 */       long i = Minecraft.getSystemTime();
/*     */       
/* 130 */       if (i - this.systemTime >= 100L)
/*     */       {
/* 132 */         this.systemTime = i;
/* 133 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/* 134 */         int j = scaledresolution.getScaleFactor();
/* 135 */         int k = ScaledResolution.getScaledWidth();
/* 136 */         int l = ScaledResolution.getScaledHeight();
/*     */         
/* 138 */         if (OpenGlHelper.isFramebufferEnabled())
/*     */         {
/* 140 */           this.framebuffer.framebufferClear();
/*     */         }
/*     */         else
/*     */         {
/* 144 */           GlStateManager.clear(256);
/*     */         }
/*     */         
/* 147 */         this.framebuffer.bindFramebuffer(false);
/* 148 */         GlStateManager.matrixMode(5889);
/* 149 */         GlStateManager.loadIdentity();
/* 150 */         GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
/* 151 */         GlStateManager.matrixMode(5888);
/* 152 */         GlStateManager.loadIdentity();
/* 153 */         GlStateManager.translate(0.0F, 0.0F, -200.0F);
/*     */         
/* 155 */         if (!OpenGlHelper.isFramebufferEnabled())
/*     */         {
/* 157 */           GlStateManager.clear(16640);
/*     */         }
/*     */         
/* 160 */         Tessellator tessellator = Tessellator.getInstance();
/* 161 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 162 */         this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 163 */         float f = 32.0F;
/* 164 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 165 */         worldrenderer.pos(0.0D, l, 0.0D).tex(0.0D, l / f).color(64, 64, 64, 255).endVertex();
/* 166 */         worldrenderer.pos(k, l, 0.0D).tex(k / f, l / f).color(64, 64, 64, 255).endVertex();
/* 167 */         worldrenderer.pos(k, 0.0D, 0.0D).tex(k / f, 0.0D).color(64, 64, 64, 255).endVertex();
/* 168 */         worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
/* 169 */         tessellator.draw();
/*     */         
/* 171 */         if (progress >= 0)
/*     */         {
/* 173 */           int i1 = 100;
/* 174 */           int j1 = 2;
/* 175 */           int k1 = k / 2 - i1 / 2;
/* 176 */           int l1 = l / 2 + 16;
/* 177 */           GlStateManager.disableTexture2D();
/* 178 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 179 */           worldrenderer.pos(k1, l1, 0.0D).color(128, 128, 128, 255).endVertex();
/* 180 */           worldrenderer.pos(k1, l1 + j1, 0.0D).color(128, 128, 128, 255).endVertex();
/* 181 */           worldrenderer.pos(k1 + i1, l1 + j1, 0.0D).color(128, 128, 128, 255).endVertex();
/* 182 */           worldrenderer.pos(k1 + i1, l1, 0.0D).color(128, 128, 128, 255).endVertex();
/* 183 */           worldrenderer.pos(k1, l1, 0.0D).color(128, 255, 128, 255).endVertex();
/* 184 */           worldrenderer.pos(k1, l1 + j1, 0.0D).color(128, 255, 128, 255).endVertex();
/* 185 */           worldrenderer.pos(k1 + progress, l1 + j1, 0.0D).color(128, 255, 128, 255).endVertex();
/* 186 */           worldrenderer.pos(k1 + progress, l1, 0.0D).color(128, 255, 128, 255).endVertex();
/* 187 */           tessellator.draw();
/* 188 */           GlStateManager.enableTexture2D();
/*     */         }
/*     */         
/* 191 */         GlStateManager.enableBlend();
/* 192 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 193 */         this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (k - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2, l / 2 - 4 - 16, 16777215);
/* 194 */         this.mc.fontRendererObj.drawStringWithShadow(this.message, (k - this.mc.fontRendererObj.getStringWidth(this.message)) / 2, l / 2 - 4 + 8, 16777215);
/* 195 */         this.framebuffer.unbindFramebuffer();
/*     */         
/* 197 */         if (OpenGlHelper.isFramebufferEnabled())
/*     */         {
/* 199 */           this.framebuffer.framebufferRender(k * j, l * j);
/*     */         }
/*     */         
/* 202 */         this.mc.updateDisplay();
/*     */         
/*     */         try
/*     */         {
/* 206 */           Thread.yield();
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDoneWorking() {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\LoadingScreenRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */