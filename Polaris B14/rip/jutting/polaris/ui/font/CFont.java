/*     */ package rip.jutting.polaris.ui.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CFont
/*     */ {
/*  12 */   private float imgSize = 512.0F;
/*  13 */   protected CharData[] charData = new CharData['Ā'];
/*     */   protected Font font;
/*     */   protected boolean antiAlias;
/*     */   protected boolean fractionalMetrics;
/*  17 */   protected int fontHeight = -1;
/*  18 */   protected int charOffset = 0;
/*     */   protected net.minecraft.client.renderer.texture.DynamicTexture tex;
/*     */   
/*     */   public CFont(Font font, boolean antiAlias, boolean fractionalMetrics)
/*     */   {
/*  23 */     this.font = font;
/*  24 */     this.antiAlias = antiAlias;
/*  25 */     this.fractionalMetrics = fractionalMetrics;
/*  26 */     this.tex = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected net.minecraft.client.renderer.texture.DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars)
/*     */   {
/*  31 */     BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);
/*     */     
/*     */     try
/*     */     {
/*  35 */       return new net.minecraft.client.renderer.texture.DynamicTexture(img);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  39 */       e.printStackTrace();
/*     */     }
/*     */     
/*  42 */     return null;
/*     */   }
/*     */   
/*     */   protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars)
/*     */   {
/*  47 */     int imgSize = (int)this.imgSize;
/*  48 */     BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
/*  49 */     Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
/*  50 */     g.setFont(font);
/*  51 */     g.setColor(new java.awt.Color(255, 255, 255, 0));
/*  52 */     g.fillRect(0, 0, imgSize, imgSize);
/*  53 */     g.setColor(java.awt.Color.WHITE);
/*  54 */     g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
/*  55 */     g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*  56 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*  57 */     java.awt.FontMetrics fontMetrics = g.getFontMetrics();
/*  58 */     int charHeight = 0;
/*  59 */     int positionX = 0;
/*  60 */     int positionY = 1;
/*     */     
/*  62 */     for (int i = 0; i < chars.length; i++)
/*     */     {
/*  64 */       char ch = (char)i;
/*  65 */       CharData charData = new CharData();
/*  66 */       Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
/*  67 */       charData.width = (dimensions.getBounds().width + 8);
/*  68 */       charData.height = dimensions.getBounds().height;
/*     */       
/*  70 */       if (positionX + charData.width >= imgSize)
/*     */       {
/*  72 */         positionX = 0;
/*  73 */         positionY += charHeight;
/*  74 */         charHeight = 0;
/*     */       }
/*     */       
/*  77 */       if (charData.height > charHeight)
/*     */       {
/*  79 */         charHeight = charData.height;
/*     */       }
/*     */       
/*  82 */       charData.storedX = positionX;
/*  83 */       charData.storedY = positionY;
/*     */       
/*  85 */       if (charData.height > this.fontHeight)
/*     */       {
/*  87 */         this.fontHeight = charData.height;
/*     */       }
/*     */       
/*  90 */       chars[i] = charData;
/*  91 */       g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
/*  92 */       positionX += charData.width;
/*     */     }
/*     */     
/*  95 */     return bufferedImage;
/*     */   }
/*     */   
/*     */   public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException
/*     */   {
/*     */     try
/*     */     {
/* 102 */       drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 106 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight)
/*     */   {
/* 112 */     float renderSRCX = srcX / this.imgSize;
/* 113 */     float renderSRCY = srcY / this.imgSize;
/* 114 */     float renderSRCWidth = srcWidth / this.imgSize;
/* 115 */     float renderSRCHeight = srcHeight / this.imgSize;
/* 116 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 117 */     GL11.glVertex2d(x + width, y);
/* 118 */     GL11.glTexCoord2f(renderSRCX, renderSRCY);
/* 119 */     GL11.glVertex2d(x, y);
/* 120 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 121 */     GL11.glVertex2d(x, y + height);
/* 122 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 123 */     GL11.glVertex2d(x, y + height);
/* 124 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
/* 125 */     GL11.glVertex2d(x + width, y + height);
/* 126 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 127 */     GL11.glVertex2d(x + width, y);
/*     */   }
/*     */   
/*     */   public int getStringHeight(String text)
/*     */   {
/* 132 */     return getHeight();
/*     */   }
/*     */   
/*     */   public int getHeight()
/*     */   {
/* 137 */     return (this.fontHeight - 8) / 2;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text)
/*     */   {
/* 142 */     int width = 0;
/*     */     char[] arrayOfChar;
/* 144 */     int j = (arrayOfChar = text.toCharArray()).length; for (int i = 0; i < j; i++) { char c = arrayOfChar[i];
/*     */       
/* 146 */       if ((c < this.charData.length) && (c >= 0))
/*     */       {
/* 148 */         width += this.charData[c].width - 8 + this.charOffset;
/*     */       }
/*     */     }
/*     */     
/* 152 */     return width / 2;
/*     */   }
/*     */   
/*     */   public boolean isAntiAlias()
/*     */   {
/* 157 */     return this.antiAlias;
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias)
/*     */   {
/* 162 */     if (this.antiAlias != antiAlias)
/*     */     {
/* 164 */       this.antiAlias = antiAlias;
/* 165 */       this.tex = setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFractionalMetrics()
/*     */   {
/* 171 */     return this.fractionalMetrics;
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics)
/*     */   {
/* 176 */     if (this.fractionalMetrics != fractionalMetrics)
/*     */     {
/* 178 */       this.fractionalMetrics = fractionalMetrics;
/* 179 */       this.tex = setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
/*     */     }
/*     */   }
/*     */   
/*     */   public Font getFont()
/*     */   {
/* 185 */     return this.font;
/*     */   }
/*     */   
/*     */   public void setFont(Font font)
/*     */   {
/* 190 */     this.font = font;
/* 191 */     this.tex = setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
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


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\font\CFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */