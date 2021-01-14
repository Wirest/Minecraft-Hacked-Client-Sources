package rip.autumn.utils.font;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public final class CustomFontRenderer extends CFont {
   private final int[] colorCode = new int[32];
   protected CFont.CharData[] boldChars = new CFont.CharData[256];
   protected CFont.CharData[] italicChars = new CFont.CharData[256];
   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
   protected DynamicTexture texBold;
   protected DynamicTexture texItalic;
   protected DynamicTexture texItalicBold;

   public CustomFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
      super(font, antiAlias, fractionalMetrics);
      this.setupMinecraftColorcodes();
      this.setupBoldItalicIDs();
   }

   public float drawString(String text, float x, float y, int color) {
      return this.drawString(text, (double)x, (double)y, color, false);
   }

   public float drawString(String text, double x, double y, int color) {
      return this.drawString(text, x, y, color, false);
   }

   public float drawStringWithShadow(String text, float x, float y, int color) {
      float shadowWidth = this.drawString(text, (double)x + 0.5D, (double)y + 0.5D, color, true);
      return Math.max(shadowWidth, this.drawString(text, (double)x, (double)y, color, false));
   }

   public float drawStringWithShadow(String text, double x, double y, int color) {
      float shadowWidth = this.drawString(text, x + 0.5D, y + 0.5D, color, true);
      return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
   }

   public float drawCenteredString(String text, float x, float y, int color) {
      return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0F, y, color);
   }

   public float drawCenteredString(String text, double x, double y, int color) {
      return this.drawString(text, x - (double)((float)this.getStringWidth(text) / 2.0F), y, color);
   }

   public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
      this.drawString(text, (double)(x - (float)this.getStringWidth(text) / 2.0F) + 0.45D, (double)y + 0.5D, color, true);
      return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0F, y, color);
   }

   public void drawStringWithOutline(String text, double x, double y, int color) {
      this.drawString(text, x - 0.5D, y, 0);
      this.drawString(text, x + 0.5D, y, 0);
      this.drawString(text, x, y - 0.5D, 0);
      this.drawString(text, x, y + 0.5D, 0);
      this.drawString(text, x, y, color);
   }

   public void drawCenteredStringWithOutline(String text, double x, double y, int color) {
      this.drawCenteredString(text, x - 0.5D, y, 0);
      this.drawCenteredString(text, x + 0.5D, y, 0);
      this.drawCenteredString(text, x, y - 0.5D, 0);
      this.drawCenteredString(text, x, y + 0.5D, 0);
      this.drawCenteredString(text, x, y, color);
   }

   public float drawCenteredStringWithShadow(String text, double x, double y, int color) {
      this.drawString(text, x - (double)((float)this.getStringWidth(text) / 2.0F) + 0.45D, y + 0.5D, color, true);
      return this.drawString(text, x - (double)((float)this.getStringWidth(text) / 2.0F), y, color);
   }

   public float drawString(String text, double x, double y, int color, boolean shadow) {
      --x;
      if (text == null) {
         return 0.0F;
      } else {
         if (color == 553648127) {
            color = 16777215;
         }

         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (shadow) {
            color = (color & 16579836) >> 2 | color & (new Color(20, 20, 20, 200)).getRGB();
         }

         CFont.CharData[] currentData = this.charData;
         float alpha = (float)(color >> 24 & 255) / 255.0F;
         boolean bold = false;
         boolean italic = false;
         boolean strikethrough = false;
         boolean underline = false;
         x *= 2.0D;
         y = (y - 3.0D) * 2.0D;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 0.5D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         GlStateManager.color((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, alpha);
         int size = text.length();
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(this.tex.getGlTextureId());
         GL11.glBindTexture(3553, this.tex.getGlTextureId());

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (String.valueOf(character).equals("§")) {
               int colorIndex = 21;

               try {
                  colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
               } catch (Exception var19) {
                  var19.printStackTrace();
               }

               if (colorIndex < 16) {
                  bold = false;
                  italic = false;
                  underline = false;
                  strikethrough = false;
                  GlStateManager.bindTexture(this.tex.getGlTextureId());
                  currentData = this.charData;
                  if (colorIndex < 0) {
                     colorIndex = 15;
                  }

                  if (shadow) {
                     colorIndex += 16;
                  }

                  int colorcode = this.colorCode[colorIndex];
                  GlStateManager.color((float)(colorcode >> 16 & 255) / 255.0F, (float)(colorcode >> 8 & 255) / 255.0F, (float)(colorcode & 255) / 255.0F, alpha);
               } else if (colorIndex == 17) {
                  bold = true;
                  if (italic) {
                     GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                     currentData = this.boldItalicChars;
                  } else {
                     GlStateManager.bindTexture(this.texBold.getGlTextureId());
                     currentData = this.boldChars;
                  }
               } else if (colorIndex == 18) {
                  strikethrough = true;
               } else if (colorIndex == 19) {
                  underline = true;
               } else if (colorIndex == 20) {
                  italic = true;
                  if (bold) {
                     GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                     currentData = this.boldItalicChars;
                  } else {
                     GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                     currentData = this.italicChars;
                  }
               } else if (colorIndex == 21) {
                  bold = false;
                  italic = false;
                  underline = false;
                  strikethrough = false;
                  GlStateManager.color((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, alpha);
                  GlStateManager.bindTexture(this.tex.getGlTextureId());
                  currentData = this.charData;
               }

               ++i;
            } else if (character < currentData.length) {
               GL11.glBegin(4);
               this.drawChar(currentData, character, (float)x, (float)y);
               GL11.glEnd();
               if (strikethrough) {
                  this.drawLine(x, y + (double)((float)currentData[character].height / 2.0F), x + (double)currentData[character].width - 8.0D, y + (double)((float)currentData[character].height / 2.0F), 1.0F);
               }

               if (underline) {
                  this.drawLine(x, y + (double)currentData[character].height - 2.0D, x + (double)currentData[character].width - 8.0D, y + (double)currentData[character].height - 2.0D, 1.0F);
               }

               x += (double)(currentData[character].width - 8 + this.charOffset);
            }
         }

         GL11.glPopMatrix();
         return (float)x / 2.0F;
      }
   }

   public int getStringWidth(String text) {
      if (text == null) {
         return 0;
      } else {
         int width = 0;
         CFont.CharData[] currentData = this.charData;
         boolean bold = false;
         boolean italic = false;
         int size = text.length();

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (String.valueOf(character).equals("§")) {
               int colorIndex = "0123456789abcdefklmnor".indexOf(character);
               if (colorIndex < 16) {
                  bold = false;
                  italic = false;
               } else if (colorIndex == 17) {
                  bold = true;
                  if (italic) {
                     currentData = this.boldItalicChars;
                  } else {
                     currentData = this.boldChars;
                  }
               } else if (colorIndex == 20) {
                  italic = true;
                  if (bold) {
                     currentData = this.boldItalicChars;
                  } else {
                     currentData = this.italicChars;
                  }
               } else if (colorIndex == 21) {
                  bold = false;
                  italic = false;
                  currentData = this.charData;
               }

               ++i;
            } else if (character < currentData.length) {
               width += currentData[character].width - 8 + this.charOffset;
            }
         }

         return width / 2;
      }
   }

   public int getStringWidthCust(String text) {
      if (text == null) {
         return 0;
      } else {
         int width = 0;
         CFont.CharData[] currentData = this.charData;
         boolean bold = false;
         boolean italic = false;
         int size = text.length();

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (String.valueOf(character).equals("§") && i < size) {
               int colorIndex = "0123456789abcdefklmnor".indexOf(character);
               if (colorIndex < 16) {
                  bold = false;
                  italic = false;
               } else if (colorIndex == 17) {
                  bold = true;
                  if (italic) {
                     currentData = this.boldItalicChars;
                  } else {
                     currentData = this.boldChars;
                  }
               } else if (colorIndex == 20) {
                  italic = true;
                  if (bold) {
                     currentData = this.boldItalicChars;
                  } else {
                     currentData = this.italicChars;
                  }
               } else if (colorIndex == 21) {
                  bold = false;
                  italic = false;
                  currentData = this.charData;
               }

               ++i;
            } else if (character < currentData.length && character >= 0) {
               width += currentData[character].width - 8 + this.charOffset;
            }
         }

         return (width - this.charOffset) / 2;
      }
   }

   public void setFont(Font font) {
      super.setFont(font);
      this.setupBoldItalicIDs();
   }

   public void setAntiAlias(boolean antiAlias) {
      super.setAntiAlias(antiAlias);
      this.setupBoldItalicIDs();
   }

   public void setFractionalMetrics(boolean fractionalMetrics) {
      super.setFractionalMetrics(fractionalMetrics);
      this.setupBoldItalicIDs();
   }

   private void setupBoldItalicIDs() {
      this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
      this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
      this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
   }

   private void drawLine(double x, double y, double x1, double y1, float width) {
      GL11.glDisable(3553);
      GL11.glLineWidth(width);
      GL11.glBegin(1);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x1, y1);
      GL11.glEnd();
      GL11.glEnable(3553);
   }

   public List wrapWords(String text, double width) {
      List finalWords = new ArrayList();
      if ((double)this.getStringWidth(text) > width) {
         String[] words = text.split(" ");
         String currentWord = "";
         char lastColorCode = '\uffff';
         String[] var8 = words;
         int var9 = words.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String word = var8[var10];

            for(int i = 0; i < word.toCharArray().length; ++i) {
               char c = word.toCharArray()[i];
               if (String.valueOf(c).equals("§") && i < word.toCharArray().length - 1) {
                  lastColorCode = word.toCharArray()[i + 1];
               }
            }

            if ((double)this.getStringWidth(currentWord + word + " ") < width) {
               currentWord = currentWord + word + " ";
            } else {
               finalWords.add(currentWord);
               currentWord = "" + lastColorCode + word + " ";
            }
         }

         if (currentWord.length() > 0) {
            if ((double)this.getStringWidth(currentWord) < width) {
               finalWords.add("" + lastColorCode + currentWord + " ");
               currentWord = "";
            } else {
               Iterator var14 = this.formatString(currentWord, width).iterator();

               while(var14.hasNext()) {
                  String s = (String)var14.next();
                  finalWords.add(s);
               }
            }
         }
      } else {
         finalWords.add(text);
      }

      return finalWords;
   }

   public List formatString(String string, double width) {
      List finalWords = new ArrayList();
      String currentWord = "";
      char lastColorCode = '\uffff';
      char[] chars = string.toCharArray();

      for(int i = 0; i < chars.length; ++i) {
         char c = chars[i];
         if (String.valueOf(c).equals("§") && i < chars.length - 1) {
            lastColorCode = chars[i + 1];
         }

         if ((double)this.getStringWidth(currentWord + c) < width) {
            currentWord = currentWord + c;
         } else {
            finalWords.add(currentWord);
            currentWord = "" + lastColorCode + c;
         }
      }

      if (currentWord.length() > 0) {
         finalWords.add(currentWord);
      }

      return finalWords;
   }

   private void setupMinecraftColorcodes() {
      for(int index = 0; index < 32; ++index) {
         int noClue = (index >> 3 & 1) * 85;
         int red = (index >> 2 & 1) * 170 + noClue;
         int green = (index >> 1 & 1) * 170 + noClue;
         int blue = (index >> 0 & 1) * 170 + noClue;
         if (index == 6) {
            red += 85;
         }

         if (index >= 16) {
            red /= 4;
            green /= 4;
            blue /= 4;
         }

         this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
      }

   }
}
