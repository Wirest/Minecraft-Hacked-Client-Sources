/*     */ package rip.jutting.polaris.ui.fonth;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CFontRenderer
/*     */   extends CFont
/*     */ {
/*  22 */   protected CFont.CharData[] boldChars = new CFont.CharData['Ā'];
/*  23 */   protected CFont.CharData[] italicChars = new CFont.CharData['Ā'];
/*  24 */   protected CFont.CharData[] boldItalicChars = new CFont.CharData['Ā'];
/*     */   
/*  26 */   private final int[] colorCode = new int[32];
/*  27 */   private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
/*     */   protected DynamicTexture texBold;
/*     */   protected DynamicTexture texItalic;
/*     */   protected DynamicTexture texItalicBold;
/*     */   
/*     */   public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  33 */     super(font, antiAlias, fractionalMetrics);
/*  34 */     setupMinecraftColorcodes();
/*  35 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public float drawStringWithShadow(String text, double x, double y, int color) {
/*  39 */     float shadowWidth = drawString(text, x + 0.5D, y + 0.5D, color, true);
/*  40 */     return Math.max(shadowWidth, drawString(text, x, y, color, false));
/*     */   }
/*     */   
/*     */   public float drawString(String text, float x, float y, int color) {
/*  44 */     return drawString(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   public float drawCenteredString(String text, float x, float y, int color) {
/*  48 */     return drawString(text, x - getStringWidth(text) / 2, y, color);
/*     */   }
/*     */   
/*     */   public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
/*  52 */     return drawStringWithShadow(text, x - getStringWidth(text) / 2, y, color);
/*     */   }
/*     */   
/*     */   public float drawCenteredStringWithShadow(String text, double x, int y, int color) {
/*  56 */     return drawStringWithShadow(text, x - getStringWidth(text) / 2, y, color);
/*     */   }
/*     */   
/*     */   public float drawString(String text, double x, double y, int color, boolean shadow) {
/*  60 */     if (Polaris.instance.moduleManager.getModuleByName("Friends").isToggled()) {
/*  61 */       for (Friend friend : FriendManager.friendsList) {
/*  62 */         if (text.contains(friend.name)) {
/*  63 */           text = text.replace(friend.name, friend.alias);
/*     */         }
/*     */       }
/*     */     }
/*  67 */     if (Polaris.instance.moduleManager.getModuleByName("NoStrike").isToggled()) {
/*  68 */       if (text.contains("VeltPvP")) {
/*  69 */         text = text.replace("VeltPvP", "Polaris");
/*     */       }
/*  71 */       if (text.contains("veltpvp.com")) {
/*  72 */         text = text.replace("veltpvp.com", "polaris.rip");
/*     */       }
/*  74 */       if (text.contains("www.veltpvp.com")) {
/*  75 */         text = text.replace("veltpvp.com", "polaris.rip");
/*     */       }
/*  77 */       if (text.contains("veltpvp")) {
/*  78 */         text = text.replace("veltpvp", "polaris");
/*     */       }
/*     */       
/*  81 */       if (text.contains("Arcane")) {
/*  82 */         text = text.replace("Arcane", "Polaris");
/*     */       }
/*  84 */       if (text.contains("arcane.cc")) {
/*  85 */         text = text.replace("arcane.cc", "polaris.rip");
/*     */       }
/*  87 */       if (text.contains("www.arcane.cc")) {
/*  88 */         text = text.replace("arcane.cc", "polaris.rip");
/*     */       }
/*  90 */       if (text.contains("arcane")) {
/*  91 */         text = text.replace("arcane", "polaris");
/*     */       }
/*     */       
/*  94 */       if (text.contains("FaithfulMC")) {
/*  95 */         text = text.replace("FaithfulMC", "Polaris");
/*     */       }
/*  97 */       if (text.contains("@FaithfulNetwork")) {
/*  98 */         text = text.replace("@FaithfulNetwork", "@Polaris");
/*     */       }
/* 100 */       if (text.contains("faithfulmc.com")) {
/* 101 */         text = text.replace("faithfulmc.com", "polaris.rip");
/*     */       }
/*     */     }
/* 104 */     x -= 1.0D;
/* 105 */     if (text == null)
/* 106 */       return 0.0F;
/* 107 */     if (color == 553648127) {
/* 108 */       color = 16777215;
/*     */     }
/* 110 */     if ((color & 0xFC000000) == 0) {
/* 111 */       color |= 0xFF000000;
/*     */     }
/*     */     
/* 114 */     if (shadow) {
/* 115 */       color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
/*     */     }
/*     */     
/* 118 */     CFont.CharData[] currentData = this.charData;
/* 119 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 120 */     boolean randomCase = false;
/* 121 */     boolean bold = false;
/* 122 */     boolean italic = false;
/* 123 */     boolean strikethrough = false;
/* 124 */     boolean underline = false;
/* 125 */     boolean render = true;
/* 126 */     x *= 2.0D;
/* 127 */     y = (y - 3.0D) * 2.0D;
/*     */     
/* 129 */     GL11.glPushMatrix();
/* 130 */     GlStateManager.scale(0.5D, 0.5D, 0.5D);
/*     */     
/* 132 */     GL11.glEnable(3042);
/* 133 */     GlStateManager.blendFunc(770, 771);
/* 134 */     GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 135 */     int size = text.length();
/* 136 */     GlStateManager.enableTexture2D();
/* 137 */     GlStateManager.bindTexture(this.tex.getGlTextureId());
/* 138 */     GL11.glBindTexture(3553, this.tex.getGlTextureId());
/* 139 */     for (int i = 0; i < size; i++) {
/* 140 */       char character = text.charAt(i);
/* 141 */       if ((character == '§') && (i < size)) {
/* 142 */         int colorIndex = 21;
/*     */         try {
/* 144 */           colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
/*     */         } catch (Exception e) {
/* 146 */           e.printStackTrace();
/*     */         }
/* 148 */         if (colorIndex < 16) {
/* 149 */           bold = false;
/* 150 */           italic = false;
/* 151 */           randomCase = false;
/* 152 */           underline = false;
/* 153 */           strikethrough = false;
/* 154 */           GlStateManager.bindTexture(this.tex.getGlTextureId());
/*     */           
/* 156 */           currentData = this.charData;
/* 157 */           if ((colorIndex < 0) || (colorIndex > 15))
/* 158 */             colorIndex = 15;
/* 159 */           if (shadow)
/* 160 */             colorIndex += 16;
/* 161 */           int colorcode = this.colorCode[colorIndex];
/* 162 */           GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
/* 163 */         } else if (colorIndex == 16) {
/* 164 */           randomCase = true;
/* 165 */         } else if (colorIndex == 17) {
/* 166 */           bold = true;
/* 167 */           if (italic) {
/* 168 */             GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
/*     */             
/* 170 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 172 */             GlStateManager.bindTexture(this.texBold.getGlTextureId());
/*     */             
/* 174 */             currentData = this.boldChars;
/*     */           }
/* 176 */         } else if (colorIndex == 18) {
/* 177 */           strikethrough = true;
/* 178 */         } else if (colorIndex == 19) {
/* 179 */           underline = true;
/* 180 */         } else if (colorIndex == 20) {
/* 181 */           italic = true;
/* 182 */           if (bold) {
/* 183 */             GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
/*     */             
/* 185 */             currentData = this.boldItalicChars;
/*     */           } else {
/* 187 */             GlStateManager.bindTexture(this.texItalic.getGlTextureId());
/*     */             
/* 189 */             currentData = this.italicChars;
/*     */           }
/* 191 */         } else if (colorIndex == 21) {
/* 192 */           bold = false;
/* 193 */           italic = false;
/* 194 */           randomCase = false;
/* 195 */           underline = false;
/* 196 */           strikethrough = false;
/* 197 */           GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 198 */           GlStateManager.bindTexture(this.tex.getGlTextureId());
/*     */           
/* 200 */           currentData = this.charData;
/*     */         }
/* 202 */         i++;
/* 203 */       } else if ((character < currentData.length) && (character >= 0)) {
/* 204 */         GL11.glBegin(4);
/* 205 */         drawChar(currentData, character, (float)x, (float)y);
/* 206 */         GL11.glEnd();
/* 207 */         if (strikethrough)
/* 208 */           drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0D, y + currentData[character].height / 2, 1.0F);
/* 209 */         if (underline)
/* 210 */           drawLine(x, y + currentData[character].height - 2.0D, x + currentData[character].width - 8.0D, y + currentData[character].height - 2.0D, 1.0F);
/* 211 */         x += currentData[character].width - 8 + this.charOffset;
/*     */       }
/*     */     }
/* 214 */     GL11.glHint(3155, 4352);
/* 215 */     GL11.glPopMatrix();
/*     */     
/* 217 */     return (float)x * 2.0F;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/* 221 */     if (text == null)
/* 222 */       return 0;
/* 223 */     int width = 0;
/* 224 */     CFont.CharData[] currentData = this.charData;
/* 225 */     boolean bold = false;
/* 226 */     boolean italic = false;
/* 227 */     int size = text.length();
/*     */     
/* 229 */     for (int i = 0; i < size; i++) {
/* 230 */       char character = text.charAt(i);
/* 231 */       if ((character == '§') && (i < size)) {
/* 232 */         int colorIndex = "0123456789abcdefklmnor".indexOf(character);
/* 233 */         if (colorIndex < 16) {
/* 234 */           bold = false;
/* 235 */           italic = false;
/* 236 */         } else if (colorIndex == 17) {
/* 237 */           bold = true;
/* 238 */           if (italic) {
/* 239 */             currentData = this.boldItalicChars;
/*     */           } else
/* 241 */             currentData = this.boldChars;
/* 242 */         } else if (colorIndex == 20) {
/* 243 */           italic = true;
/* 244 */           if (bold) {
/* 245 */             currentData = this.boldItalicChars;
/*     */           } else
/* 247 */             currentData = this.italicChars;
/* 248 */         } else if (colorIndex == 21) {
/* 249 */           bold = false;
/* 250 */           italic = false;
/* 251 */           currentData = this.charData;
/*     */         }
/* 253 */         i++;
/* 254 */       } else if ((character < currentData.length) && (character >= 0)) {
/* 255 */         width += currentData[character].width - 8 + this.charOffset;
/*     */       }
/*     */     }
/*     */     
/* 259 */     return width / 2;
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 263 */     super.setFont(font);
/* 264 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 268 */     super.setAntiAlias(antiAlias);
/* 269 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 273 */     super.setFractionalMetrics(fractionalMetrics);
/* 274 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   private void setupBoldItalicIDs() {
/* 278 */     this.texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
/* 279 */     this.texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
/*     */   }
/*     */   
/*     */   private void drawLine(double x, double y, double x1, double y1, float width)
/*     */   {
/* 284 */     GL11.glDisable(3553);
/* 285 */     GL11.glLineWidth(width);
/* 286 */     GL11.glBegin(1);
/* 287 */     GL11.glVertex2d(x, y);
/* 288 */     GL11.glVertex2d(x1, y1);
/* 289 */     GL11.glEnd();
/* 290 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public List<String> wrapWords(String text, double width) {
/* 294 */     List finalWords = new ArrayList();
/* 295 */     if (getStringWidth(text) > width) {
/* 296 */       String[] words = text.split(" ");
/* 297 */       String currentWord = "";
/* 298 */       char lastColorCode = 65535;
/*     */       String[] arrayOfString1;
/* 300 */       int j = (arrayOfString1 = words).length; for (int i = 0; i < j; i++) { String word = arrayOfString1[i];
/* 301 */         for (int i = 0; i < word.toCharArray().length; i++) {
/* 302 */           char c = word.toCharArray()[i];
/*     */           
/* 304 */           if ((c == '§') && (i < word.toCharArray().length - 1)) {
/* 305 */             lastColorCode = word.toCharArray()[(i + 1)];
/*     */           }
/*     */         }
/* 308 */         if (getStringWidth(currentWord + word + " ") < width) {
/* 309 */           currentWord = currentWord + word + " ";
/*     */         } else {
/* 311 */           finalWords.add(currentWord);
/* 312 */           currentWord = '§' + lastColorCode + word + " ";
/*     */         }
/*     */       }
/* 315 */       if (currentWord.length() > 0)
/* 316 */         if (getStringWidth(currentWord) < width) {
/* 317 */           finalWords.add('§' + lastColorCode + currentWord + " ");
/* 318 */           currentWord = "";
/*     */         } else {
/* 320 */           for (String s : formatString(currentWord, width))
/* 321 */             finalWords.add(s);
/*     */         }
/*     */     } else {
/* 324 */       finalWords.add(text);
/*     */     }
/*     */     
/* 327 */     return finalWords;
/*     */   }
/*     */   
/*     */   public List<String> formatString(String string, double width) {
/* 331 */     List finalWords = new ArrayList();
/* 332 */     String currentWord = "";
/* 333 */     char lastColorCode = 65535;
/* 334 */     char[] chars = string.toCharArray();
/* 335 */     for (int i = 0; i < chars.length; i++) {
/* 336 */       char c = chars[i];
/*     */       
/* 338 */       if ((c == '§') && (i < chars.length - 1)) {
/* 339 */         lastColorCode = chars[(i + 1)];
/*     */       }
/*     */       
/* 342 */       if (getStringWidth(currentWord + c) < width) {
/* 343 */         currentWord = currentWord + c;
/*     */       } else {
/* 345 */         finalWords.add(currentWord);
/* 346 */         currentWord = '§' + lastColorCode + String.valueOf(c);
/*     */       }
/*     */     }
/*     */     
/* 350 */     if (currentWord.length() > 0) {
/* 351 */       finalWords.add(currentWord);
/*     */     }
/*     */     
/* 354 */     return finalWords;
/*     */   }
/*     */   
/*     */   private void setupMinecraftColorcodes() {
/* 358 */     for (int index = 0; index < 32; index++) {
/* 359 */       int noClue = (index >> 3 & 0x1) * 85;
/* 360 */       int red = (index >> 2 & 0x1) * 170 + noClue;
/* 361 */       int green = (index >> 1 & 0x1) * 170 + noClue;
/* 362 */       int blue = (index >> 0 & 0x1) * 170 + noClue;
/*     */       
/* 364 */       if (index == 6) {
/* 365 */         red += 85;
/*     */       }
/*     */       
/* 368 */       if (index >= 16) {
/* 369 */         red /= 4;
/* 370 */         green /= 4;
/* 371 */         blue /= 4;
/*     */       }
/*     */       
/* 374 */       this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\fonth\CFontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */