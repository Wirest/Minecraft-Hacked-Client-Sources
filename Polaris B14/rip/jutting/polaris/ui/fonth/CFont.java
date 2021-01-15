/*     */ package rip.jutting.polaris.ui.fonth;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CFont
/*     */ {
/*  11 */   private final float imgSize = 512.0F;
/*  12 */   protected CharData[] charData = new CharData['Ā'];
/*     */   protected Font font;
/*     */   protected boolean antiAlias;
/*     */   protected boolean fractionalMetrics;
/*  16 */   protected int fontHeight = -1;
/*  17 */   protected int charOffset = 0;
/*     */   protected net.minecraft.client.renderer.texture.DynamicTexture tex;
/*     */   
/*     */   public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  21 */     this.font = font;
/*  22 */     this.antiAlias = antiAlias;
/*  23 */     this.fractionalMetrics = fractionalMetrics;
/*  24 */     this.tex = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected net.minecraft.client.renderer.texture.DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  28 */     BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);
/*     */     try {
/*  30 */       return new net.minecraft.client.renderer.texture.DynamicTexture(img);
/*     */     } catch (Exception e) {
/*  32 */       e.printStackTrace();
/*     */     }
/*  34 */     return null;
/*     */   }
/*     */   
/*     */   protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  38 */     int imgSize = 512;
/*  39 */     BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
/*  40 */     Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
/*  41 */     g.setFont(font);
/*  42 */     g.setColor(new java.awt.Color(255, 255, 255, 0));
/*  43 */     g.fillRect(0, 0, imgSize, imgSize);
/*  44 */     g.setColor(java.awt.Color.WHITE);
/*  45 */     g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
/*  46 */     g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*  47 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*  48 */     java.awt.FontMetrics fontMetrics = g.getFontMetrics();
/*  49 */     int charHeight = 0;
/*  50 */     int positionX = 0;
/*  51 */     int positionY = 1;
/*  52 */     for (int i = 0; i < chars.length; i++) {
/*  53 */       char ch = (char)i;
/*  54 */       CharData charData = new CharData();
/*  55 */       java.awt.geom.Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
/*  56 */       charData.width = (dimensions.getBounds().width + 8);
/*  57 */       charData.height = dimensions.getBounds().height;
/*  58 */       if (positionX + charData.width >= imgSize) {
/*  59 */         positionX = 0;
/*  60 */         positionY += charHeight;
/*  61 */         charHeight = 0;
/*     */       }
/*  63 */       if (charData.height > charHeight) {
/*  64 */         charHeight = charData.height;
/*     */       }
/*  66 */       charData.storedX = positionX;
/*  67 */       charData.storedY = positionY;
/*  68 */       if (charData.height > this.fontHeight) {
/*  69 */         this.fontHeight = charData.height;
/*     */       }
/*  71 */       chars[i] = charData;
/*  72 */       g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
/*  73 */       positionX += charData.width;
/*     */     }
/*  75 */     return bufferedImage;
/*     */   }
/*     */   
/*     */   public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException {
/*     */     try {
/*  80 */       drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
/*     */     } catch (Exception e) {
/*  82 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
/*  87 */     float renderSRCX = srcX / 512.0F;
/*  88 */     float renderSRCY = srcY / 512.0F;
/*  89 */     float renderSRCWidth = srcWidth / 512.0F;
/*  90 */     float renderSRCHeight = srcHeight / 512.0F;
/*  91 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/*  92 */     GL11.glVertex2d(x + width, y);
/*  93 */     GL11.glTexCoord2f(renderSRCX, renderSRCY);
/*  94 */     GL11.glVertex2d(x, y);
/*  95 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/*  96 */     GL11.glVertex2d(x, y + height);
/*  97 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/*  98 */     GL11.glVertex2d(x, y + height);
/*  99 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
/* 100 */     GL11.glVertex2d(x + width, y + height);
/* 101 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 102 */     GL11.glVertex2d(x + width, y);
/*     */   }
/*     */   
/*     */   public int getStringHeight(String text) {
/* 106 */     return getHeight();
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 110 */     return (this.fontHeight - 8) / 2;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/* 114 */     int width = 0;
/* 115 */     char[] arrayOfChar; int j = (arrayOfChar = text.toCharArray()).length; for (int i = 0; i < j; i++) { char c = arrayOfChar[i];
/* 116 */       if ((c < this.charData.length) && (c >= 0)) width += this.charData[c].width - 8 + this.charOffset;
/*     */     }
/* 118 */     return width / 2;
/*     */   }
/*     */   
/*     */   public boolean isAntiAlias() {
/* 122 */     return this.antiAlias;
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 126 */     if (this.antiAlias != antiAlias) {
/* 127 */       this.antiAlias = antiAlias;
/* 128 */       this.tex = setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFractionalMetrics() {
/* 133 */     return this.fractionalMetrics;
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 137 */     if (this.fractionalMetrics != fractionalMetrics) {
/* 138 */       this.fractionalMetrics = fractionalMetrics;
/* 139 */       this.tex = setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
/*     */     }
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 144 */     return this.font;
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 148 */     this.font = font;
/* 149 */     this.tex = setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected class CharData
/*     */   {
/*     */     public int width;
/*     */     public int height;
/*     */     public int storedX;
/*     */     public int storedY;
/*     */     
/*     */     protected CharData() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\fonth\CFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */