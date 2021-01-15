/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Gui
/*     */ {
/*  15 */   public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
/*  16 */   public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
/*  17 */   public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
/*     */   
/*     */ 
/*     */   protected float zLevel;
/*     */   
/*     */ 
/*     */   protected void drawHorizontalLine(int startX, int endX, int y, int color)
/*     */   {
/*  25 */     if (endX < startX)
/*     */     {
/*  27 */       int i = startX;
/*  28 */       startX = endX;
/*  29 */       endX = i;
/*     */     }
/*     */     
/*  32 */     drawRect(startX, y, endX + 1, y + 1, color);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawVerticalLine(int x, int startY, int endY, int color)
/*     */   {
/*  40 */     if (endY < startY)
/*     */     {
/*  42 */       int i = startY;
/*  43 */       startY = endY;
/*  44 */       endY = i;
/*     */     }
/*     */     
/*  47 */     drawRect(x, startY + 1, x + 1, endY, color);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void drawRect(double left, double top, double right, double bottom, int color)
/*     */   {
/*  55 */     if (left < right)
/*     */     {
/*  57 */       double i = left;
/*  58 */       left = right;
/*  59 */       right = i;
/*     */     }
/*     */     
/*  62 */     if (top < bottom)
/*     */     {
/*  64 */       double j = top;
/*  65 */       top = bottom;
/*  66 */       bottom = j;
/*     */     }
/*     */     
/*  69 */     float f3 = (color >> 24 & 0xFF) / 255.0F;
/*  70 */     float f = (color >> 16 & 0xFF) / 255.0F;
/*  71 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
/*  72 */     float f2 = (color & 0xFF) / 255.0F;
/*  73 */     Tessellator tessellator = Tessellator.getInstance();
/*  74 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  75 */     GlStateManager.enableBlend();
/*  76 */     GlStateManager.disableTexture2D();
/*  77 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  78 */     GlStateManager.color(f, f1, f2, f3);
/*  79 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION);
/*  80 */     worldrenderer.pos(left, bottom, 0.0D).endVertex();
/*  81 */     worldrenderer.pos(right, bottom, 0.0D).endVertex();
/*  82 */     worldrenderer.pos(right, top, 0.0D).endVertex();
/*  83 */     worldrenderer.pos(left, top, 0.0D).endVertex();
/*  84 */     tessellator.draw();
/*  85 */     GlStateManager.enableTexture2D();
/*  86 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
/*     */   {
/*  95 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
/*  96 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
/*  97 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
/*  98 */     float f3 = (startColor & 0xFF) / 255.0F;
/*  99 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
/* 100 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
/* 101 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
/* 102 */     float f7 = (endColor & 0xFF) / 255.0F;
/* 103 */     GlStateManager.disableTexture2D();
/* 104 */     GlStateManager.enableBlend();
/* 105 */     GlStateManager.disableAlpha();
/* 106 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 107 */     GlStateManager.shadeModel(7425);
/* 108 */     Tessellator tessellator = Tessellator.getInstance();
/* 109 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 110 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 111 */     worldrenderer.pos(right, top, this.zLevel).color(f1, f2, f3, f).endVertex();
/* 112 */     worldrenderer.pos(left, top, this.zLevel).color(f1, f2, f3, f).endVertex();
/* 113 */     worldrenderer.pos(left, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
/* 114 */     worldrenderer.pos(right, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
/* 115 */     tessellator.draw();
/* 116 */     GlStateManager.shadeModel(7424);
/* 117 */     GlStateManager.disableBlend();
/* 118 */     GlStateManager.enableAlpha();
/* 119 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
/*     */   {
/* 127 */     fontRendererIn.drawStringWithShadow(text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
/*     */   {
/* 135 */     fontRendererIn.drawStringWithShadow(text, x, y, color);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
/*     */   {
/* 143 */     float f = 0.00390625F;
/* 144 */     float f1 = 0.00390625F;
/* 145 */     Tessellator tessellator = Tessellator.getInstance();
/* 146 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 147 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 148 */     worldrenderer.pos(x + 0, y + height, this.zLevel).tex((textureX + 0) * f, (textureY + height) * f1).endVertex();
/* 149 */     worldrenderer.pos(x + width, y + height, this.zLevel).tex((textureX + width) * f, (textureY + height) * f1).endVertex();
/* 150 */     worldrenderer.pos(x + width, y + 0, this.zLevel).tex((textureX + width) * f, (textureY + 0) * f1).endVertex();
/* 151 */     worldrenderer.pos(x + 0, y + 0, this.zLevel).tex((textureX + 0) * f, (textureY + 0) * f1).endVertex();
/* 152 */     tessellator.draw();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV)
/*     */   {
/* 160 */     float f = 0.00390625F;
/* 161 */     float f1 = 0.00390625F;
/* 162 */     Tessellator tessellator = Tessellator.getInstance();
/* 163 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 164 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 165 */     worldrenderer.pos(xCoord + 0.0F, yCoord + maxV, this.zLevel).tex((minU + 0) * f, (minV + maxV) * f1).endVertex();
/* 166 */     worldrenderer.pos(xCoord + maxU, yCoord + maxV, this.zLevel).tex((minU + maxU) * f, (minV + maxV) * f1).endVertex();
/* 167 */     worldrenderer.pos(xCoord + maxU, yCoord + 0.0F, this.zLevel).tex((minU + maxU) * f, (minV + 0) * f1).endVertex();
/* 168 */     worldrenderer.pos(xCoord + 0.0F, yCoord + 0.0F, this.zLevel).tex((minU + 0) * f, (minV + 0) * f1).endVertex();
/* 169 */     tessellator.draw();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn)
/*     */   {
/* 177 */     Tessellator tessellator = Tessellator.getInstance();
/* 178 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 179 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 180 */     worldrenderer.pos(xCoord + 0, yCoord + heightIn, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
/* 181 */     worldrenderer.pos(xCoord + widthIn, yCoord + heightIn, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
/* 182 */     worldrenderer.pos(xCoord + widthIn, yCoord + 0, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
/* 183 */     worldrenderer.pos(xCoord + 0, yCoord + 0, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
/* 184 */     tessellator.draw();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
/*     */   {
/* 192 */     float f = 1.0F / textureWidth;
/* 193 */     float f1 = 1.0F / textureHeight;
/* 194 */     Tessellator tessellator = Tessellator.getInstance();
/* 195 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 196 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 197 */     worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + height) * f1).endVertex();
/* 198 */     worldrenderer.pos(x + width, y + height, 0.0D).tex((u + width) * f, (v + height) * f1).endVertex();
/* 199 */     worldrenderer.pos(x + width, y, 0.0D).tex((u + width) * f, v * f1).endVertex();
/* 200 */     worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
/* 201 */     tessellator.draw();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
/*     */   {
/* 209 */     float f = 1.0F / tileWidth;
/* 210 */     float f1 = 1.0F / tileHeight;
/* 211 */     Tessellator tessellator = Tessellator.getInstance();
/* 212 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 213 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 214 */     worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
/* 215 */     worldrenderer.pos(x + width, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
/* 216 */     worldrenderer.pos(x + width, y, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
/* 217 */     worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
/* 218 */     tessellator.draw();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\Gui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */