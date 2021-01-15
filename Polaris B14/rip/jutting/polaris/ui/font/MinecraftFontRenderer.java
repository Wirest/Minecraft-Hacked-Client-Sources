/*     */ package rip.jutting.polaris.ui.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.friend.Friend;
/*     */ import rip.jutting.polaris.friend.FriendManager;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ 
/*     */ public class MinecraftFontRenderer extends CFont
/*     */ {
/*  17 */   protected CFont.CharData[] boldChars = new CFont.CharData['Ā'];
/*  18 */   protected CFont.CharData[] italicChars = new CFont.CharData['Ā'];
/*  19 */   protected CFont.CharData[] boldItalicChars = new CFont.CharData['Ā'];
/*     */   
/*  21 */   private final int[] colorCode = new int[32];
/*  22 */   private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
/*     */   protected DynamicTexture texBold;
/*     */   
/*     */   public MinecraftFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  26 */     super(font, antiAlias, fractionalMetrics);
/*  27 */     setupMinecraftColorcodes();
/*  28 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public float drawStringWithShadow(String text, double x, double y, int color)
/*     */   {
/*  33 */     float shadowWidth = drawString(text, x + 1.0D, y + 1.0D, color, true);
/*  34 */     return Math.max(shadowWidth, drawString(text, x, y, color, false));
/*     */   }
/*     */   
/*     */   public float drawString(String text, float x, float y, int color)
/*     */   {
/*  39 */     return drawString(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   public float drawCenteredString(String text, float x, float y, int color)
/*     */   {
/*  44 */     return drawString(text, x - getStringWidth(text) / 2, y, color);
/*     */   }
/*     */   
/*     */   public float drawCenteredStringWithShadow(String text, float x, float y, int color)
/*     */   {
/*  49 */     float shadowWidth = drawString(text, x - getStringWidth(text) / 2 + 1.0D, y + 1.0D, color, true);
/*  50 */     return drawString(text, x - getStringWidth(text) / 2, y, color);
/*     */   }
/*     */   
/*     */   public float drawString(String text, double x, double y, int color, boolean shadow)
/*     */   {
/*  55 */     if (Polaris.instance.moduleManager.getModuleByName("Friends").isToggled()) {
/*  56 */       for (Friend friend : FriendManager.friendsList) {
/*  57 */         if (text.contains(friend.name)) {
/*  58 */           text = text.replace(friend.name, friend.alias);
/*     */         }
/*     */       }
/*     */     }
/*  62 */     if (Polaris.instance.moduleManager.getModuleByName("NoStrike").isToggled()) {
/*  63 */       if (text.contains("VeltPvP")) {
/*  64 */         text = text.replace("VeltPvP", "Polaris");
/*     */       }
/*  66 */       if (text.contains("veltpvp.com")) {
/*  67 */         text = text.replace("veltpvp.com", "polaris.rip");
/*     */       }
/*  69 */       if (text.contains("www.veltpvp.com")) {
/*  70 */         text = text.replace("veltpvp.com", "polaris.rip");
/*     */       }
/*  72 */       if (text.contains("veltpvp")) {
/*  73 */         text = text.replace("veltpvp", "polaris");
/*     */       }
/*     */       
/*  76 */       if (text.contains("Arcane")) {
/*  77 */         text = text.replace("Arcane", "Polaris");
/*     */       }
/*  79 */       if (text.contains("arcane.cc")) {
/*  80 */         text = text.replace("arcane.cc", "polaris.rip");
/*     */       }
/*  82 */       if (text.contains("www.arcane.cc")) {
/*  83 */         text = text.replace("arcane.cc", "polaris.rip");
/*     */       }
/*  85 */       if (text.contains("arcane")) {
/*  86 */         text = text.replace("arcane", "polaris");
/*     */       }
/*     */       
/*  89 */       if (text.contains("FaithfulMC")) {
/*  90 */         text = text.replace("FaithfulMC", "Polaris");
/*     */       }
/*  92 */       if (text.contains("@FaithfulNetwork")) {
/*  93 */         text = text.replace("@FaithfulNetwork", "@Polaris");
/*     */       }
/*  95 */       if (text.contains("faithfulmc.com")) {
/*  96 */         text = text.replace("faithfulmc.com", "polaris.rip");
/*     */       }
/*     */     }
/*  99 */     x -= 1.0D;
/*     */     
/* 101 */     if (text == null)
/*     */     {
/* 103 */       return 0.0F;
/*     */     }
/*     */     
/* 106 */     if (color == 553648127)
/*     */     {
/* 108 */       color = 16777215;
/*     */     }
/*     */     
/* 111 */     if ((color & 0xFC000000) == 0)
/*     */     {
/* 113 */       color |= 0xFF000000;
/*     */     }
/*     */     
/* 116 */     if (shadow)
/*     */     {
/* 118 */       color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
/*     */     }
/*     */     
/* 121 */     CFont.CharData[] currentData = this.charData;
/* 122 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 123 */     boolean randomCase = false;
/* 124 */     boolean bold = false;
/* 125 */     boolean italic = false;
/* 126 */     boolean strikethrough = false;
/* 127 */     boolean underline = false;
/* 128 */     boolean render = true;
/* 129 */     x *= 2.0D;
/* 130 */     y = (y - 3.0D) * 2.0D;
/*     */     
/* 132 */     if (render)
/*     */     {
/* 134 */       GL11.glPushMatrix();
/* 135 */       GlStateManager.scale(0.5D, 0.5D, 0.5D);
/* 136 */       GlStateManager.enableBlend();
/* 137 */       GlStateManager.blendFunc(770, 771);
/* 138 */       GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 139 */       int size = text.length();
/* 140 */       GlStateManager.enableTexture2D();
/* 141 */       GlStateManager.bindTexture(this.tex.getGlTextureId());
/*     */       
/* 143 */       GL11.glBindTexture(3553, this.tex.getGlTextureId());
/*     */       
/* 145 */       for (int i = 0; i < size; i++)
/*     */       {
/* 147 */         char character = text.charAt(i);
/*     */         
/* 149 */         if ((character == '§') && (i < size))
/*     */         {
/* 151 */           int colorIndex = 21;
/*     */           
/*     */           try
/*     */           {
/* 155 */             colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 159 */             e.printStackTrace();
/*     */           }
/*     */           
/* 162 */           if (colorIndex < 16)
/*     */           {
/* 164 */             bold = false;
/* 165 */             italic = false;
/* 166 */             randomCase = false;
/* 167 */             underline = false;
/* 168 */             strikethrough = false;
/* 169 */             GlStateManager.bindTexture(this.tex.getGlTextureId());
/*     */             
/*     */ 
/* 172 */             currentData = this.charData;
/*     */             
/* 174 */             if ((colorIndex < 0) || (colorIndex > 15))
/*     */             {
/* 176 */               colorIndex = 15;
/*     */             }
/*     */             
/* 179 */             if (shadow)
/*     */             {
/* 181 */               colorIndex += 16;
/*     */             }
/*     */             
/* 184 */             int colorcode = this.colorCode[colorIndex];
/* 185 */             GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
/*     */           }
/* 187 */           else if (colorIndex == 16)
/*     */           {
/* 189 */             randomCase = true;
/*     */           }
/* 191 */           else if (colorIndex == 17)
/*     */           {
/* 193 */             bold = true;
/*     */             
/* 195 */             if (italic)
/*     */             {
/* 197 */               GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
/*     */               
/*     */ 
/* 200 */               currentData = this.boldItalicChars;
/*     */             }
/*     */             else
/*     */             {
/* 204 */               GlStateManager.bindTexture(this.texBold.getGlTextureId());
/*     */               
/*     */ 
/* 207 */               currentData = this.boldChars;
/*     */             }
/*     */           }
/* 210 */           else if (colorIndex == 18)
/*     */           {
/* 212 */             strikethrough = true;
/*     */           }
/* 214 */           else if (colorIndex == 19)
/*     */           {
/* 216 */             underline = true;
/*     */           }
/* 218 */           else if (colorIndex == 20)
/*     */           {
/* 220 */             italic = true;
/*     */             
/* 222 */             if (bold)
/*     */             {
/* 224 */               GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
/*     */               
/*     */ 
/* 227 */               currentData = this.boldItalicChars;
/*     */             }
/*     */             else
/*     */             {
/* 231 */               GlStateManager.bindTexture(this.texItalic.getGlTextureId());
/*     */               
/*     */ 
/* 234 */               currentData = this.italicChars;
/*     */             }
/*     */           }
/* 237 */           else if (colorIndex == 21)
/*     */           {
/* 239 */             bold = false;
/* 240 */             italic = false;
/* 241 */             randomCase = false;
/* 242 */             underline = false;
/* 243 */             strikethrough = false;
/* 244 */             GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 245 */             GlStateManager.bindTexture(this.tex.getGlTextureId());
/*     */             
/*     */ 
/* 248 */             currentData = this.charData;
/*     */           }
/*     */           
/* 251 */           i++;
/*     */         }
/* 253 */         else if ((character < currentData.length) && (character >= 0))
/*     */         {
/* 255 */           GL11.glBegin(4);
/* 256 */           drawChar(currentData, character, (float)x, (float)y);
/* 257 */           GL11.glEnd();
/*     */           
/* 259 */           if (strikethrough)
/*     */           {
/* 261 */             drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0D, y + currentData[character].height / 2, 1.0F);
/*     */           }
/*     */           
/* 264 */           if (underline)
/*     */           {
/* 266 */             drawLine(x, y + currentData[character].height - 2.0D, x + currentData[character].width - 8.0D, y + currentData[character].height - 2.0D, 1.0F);
/*     */           }
/*     */           
/* 269 */           x += currentData[character].width - 8 + this.charOffset;
/*     */         }
/*     */       }
/*     */       
/* 273 */       GL11.glHint(3155, 4352);
/* 274 */       GL11.glPopMatrix();
/*     */     }
/*     */     
/* 277 */     return (float)x / 2.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getStringWidth(String text)
/*     */   {
/* 283 */     if (text == null)
/*     */     {
/* 285 */       return 0;
/*     */     }
/*     */     
/* 288 */     int width = 0;
/* 289 */     CFont.CharData[] currentData = this.charData;
/* 290 */     boolean bold = false;
/* 291 */     boolean italic = false;
/* 292 */     int size = text.length();
/*     */     
/* 294 */     for (int i = 0; i < size; i++)
/*     */     {
/* 296 */       char character = text.charAt(i);
/*     */       
/* 298 */       if ((character == '§') && (i < size))
/*     */       {
/* 300 */         int colorIndex = "0123456789abcdefklmnor".indexOf(character);
/*     */         
/* 302 */         if (colorIndex < 16)
/*     */         {
/* 304 */           bold = false;
/* 305 */           italic = false;
/*     */         }
/* 307 */         else if (colorIndex == 17)
/*     */         {
/* 309 */           bold = true;
/*     */           
/* 311 */           if (italic)
/*     */           {
/* 313 */             currentData = this.boldItalicChars;
/*     */           }
/*     */           else
/*     */           {
/* 317 */             currentData = this.boldChars;
/*     */           }
/*     */         }
/* 320 */         else if (colorIndex == 20)
/*     */         {
/* 322 */           italic = true;
/*     */           
/* 324 */           if (bold)
/*     */           {
/* 326 */             currentData = this.boldItalicChars;
/*     */           }
/*     */           else
/*     */           {
/* 330 */             currentData = this.italicChars;
/*     */           }
/*     */         }
/* 333 */         else if (colorIndex == 21)
/*     */         {
/* 335 */           bold = false;
/* 336 */           italic = false;
/* 337 */           currentData = this.charData;
/*     */         }
/*     */         
/* 340 */         i++;
/*     */       }
/* 342 */       else if ((character < currentData.length) && (character >= 0))
/*     */       {
/* 344 */         width += currentData[character].width - 8 + this.charOffset;
/*     */       }
/*     */     }
/*     */     
/* 348 */     return width / 2;
/*     */   }
/*     */   
/*     */   public void setFont(Font font)
/*     */   {
/* 353 */     super.setFont(font);
/* 354 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias)
/*     */   {
/* 359 */     super.setAntiAlias(antiAlias);
/* 360 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics)
/*     */   {
/* 365 */     super.setFractionalMetrics(fractionalMetrics);
/* 366 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */ 
/*     */   protected DynamicTexture texItalic;
/*     */   
/*     */   protected DynamicTexture texItalicBold;
/*     */   private void setupBoldItalicIDs()
/*     */   {
/* 375 */     this.texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
/* 376 */     this.texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
/* 377 */     this.texItalicBold = setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
/*     */   }
/*     */   
/*     */   private void drawLine(double x, double y, double x1, double y1, float width)
/*     */   {
/* 382 */     GL11.glDisable(3553);
/* 383 */     GL11.glLineWidth(width);
/* 384 */     GL11.glBegin(1);
/* 385 */     GL11.glVertex2d(x, y);
/* 386 */     GL11.glVertex2d(x1, y1);
/* 387 */     GL11.glEnd();
/* 388 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public List<String> wrapWords(String text, double width)
/*     */   {
/* 393 */     List finalWords = new ArrayList();
/*     */     
/* 395 */     if (getStringWidth(text) > width)
/*     */     {
/* 397 */       String[] words = text.split(" ");
/* 398 */       String currentWord = "";
/* 399 */       char lastColorCode = 65535;
/*     */       String[] arrayOfString1;
/* 401 */       int j = (arrayOfString1 = words).length; for (int i = 0; i < j; i++) { String word = arrayOfString1[i];
/*     */         
/* 403 */         for (int i = 0; i < word.toCharArray().length; i++)
/*     */         {
/* 405 */           char c = word.toCharArray()[i];
/*     */           
/* 407 */           if ((c == '§') && (i < word.toCharArray().length - 1))
/*     */           {
/* 409 */             lastColorCode = word.toCharArray()[(i + 1)];
/*     */           }
/*     */         }
/*     */         
/* 413 */         if (getStringWidth(currentWord + word + " ") < width)
/*     */         {
/* 415 */           currentWord = currentWord + word + " ";
/*     */         }
/*     */         else
/*     */         {
/* 419 */           finalWords.add(currentWord);
/* 420 */           currentWord = "§" + lastColorCode + word + " ";
/*     */         }
/*     */       }
/*     */       
/* 424 */       if (currentWord.length() > 0) if (getStringWidth(currentWord) < width)
/*     */         {
/* 426 */           finalWords.add("§" + lastColorCode + currentWord + " ");
/* 427 */           currentWord = "";
/*     */         }
/*     */         else
/*     */         {
/* 431 */           for (String s : formatString(currentWord, width))
/*     */           {
/* 433 */             finalWords.add(s);
/*     */           }
/*     */         }
/*     */     }
/*     */     else
/*     */     {
/* 439 */       finalWords.add(text);
/*     */     }
/*     */     
/* 442 */     return finalWords;
/*     */   }
/*     */   
/*     */   public List<String> formatString(String string, double width)
/*     */   {
/* 447 */     List finalWords = new ArrayList();
/* 448 */     String currentWord = "";
/* 449 */     char lastColorCode = 65535;
/* 450 */     char[] chars = string.toCharArray();
/*     */     
/* 452 */     for (int i = 0; i < chars.length; i++)
/*     */     {
/* 454 */       char c = chars[i];
/*     */       
/* 456 */       if ((c == '§') && (i < chars.length - 1))
/*     */       {
/* 458 */         lastColorCode = chars[(i + 1)];
/*     */       }
/*     */       
/* 461 */       if (getStringWidth(currentWord + c) < width)
/*     */       {
/* 463 */         currentWord = currentWord + c;
/*     */       }
/*     */       else
/*     */       {
/* 467 */         finalWords.add(currentWord);
/* 468 */         currentWord = "§" + lastColorCode + String.valueOf(c);
/*     */       }
/*     */     }
/*     */     
/* 472 */     if (currentWord.length() > 0)
/*     */     {
/* 474 */       finalWords.add(currentWord);
/*     */     }
/*     */     
/* 477 */     return finalWords;
/*     */   }
/*     */   
/*     */   private void setupMinecraftColorcodes()
/*     */   {
/* 482 */     for (int index = 0; index < 32; index++)
/*     */     {
/* 484 */       int noClue = (index >> 3 & 0x1) * 85;
/* 485 */       int red = (index >> 2 & 0x1) * 170 + noClue;
/* 486 */       int green = (index >> 1 & 0x1) * 170 + noClue;
/* 487 */       int blue = (index >> 0 & 0x1) * 170 + noClue;
/*     */       
/* 489 */       if (index == 6)
/*     */       {
/* 491 */         red += 85;
/*     */       }
/*     */       
/* 494 */       if (index >= 16)
/*     */       {
/* 496 */         red /= 4;
/* 497 */         green /= 4;
/* 498 */         blue /= 4;
/*     */       }
/*     */       
/* 501 */       this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\font\MinecraftFontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */