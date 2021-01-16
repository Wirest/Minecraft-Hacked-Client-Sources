package org.m0jang.crystal.Font;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Wrapper;

public class FluxFontRenderer extends FontRenderer {
   public final Random fontRandom;
   private final Color[] customColorCodes;
   private final int[] colorCode;
   private FluxFont font;
   private FluxFont boldFont;
   private FluxFont italicFont;
   private FluxFont boldItalicFont;
   private String colorcodeIdentifiers;
   private boolean bidi;

   public FluxFontRenderer(Font font, boolean antiAlias, int charOffset) {
      super(Minecraft.gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
      this.fontRandom = new Random();
      this.customColorCodes = new Color[256];
      this.colorCode = new int[32];
      this.colorcodeIdentifiers = "0123456789abcdefklmnor";
      this.setFont(font, antiAlias, charOffset);
      this.customColorCodes[113] = new Color(0, 90, 163);
      this.colorcodeIdentifiers = this.setupColorcodeIdentifier();
      this.setupMinecraftColorcodes();
      this.FONT_HEIGHT = this.getHeight();
   }

   public int drawString(String s, float x, float y, int color) {
      if (!Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue()) {
         return this.drawString(s, x, y, color, false);
      } else {
         Wrapper.mc.fontRendererObj.drawString(s, x, y, color, false);
         return -1;
      }
   }

   public void drawCenteredStringXY(String s, int x, int y, int color, boolean shadow) {
      this.drawCenteredString(s, x, y - this.getHeight() / 2, color, shadow);
   }

   public int drawStringWithShadow(String s, float x, float y, int color) {
      if (!Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue()) {
         this.drawString(s, x + 1.0F, y + 1.0F, color, true);
         return this.drawString(s, x, y, color, false) + 1;
      } else {
         Wrapper.mc.fontRendererObj.drawStringWithShadow(s, x, y, color);
         return -1;
      }
   }

   public void drawCenteredString(String s, int x, int y, int color, boolean shadow) {
      if (!Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue()) {
         if (shadow) {
            this.drawStringWithShadow(s, (float)(x - this.getStringWidth(s) / 2), (float)y, color);
         } else {
            this.drawString(s, x - this.getStringWidth(s) / 2, y, color);
         }
      } else {
         Wrapper.mc.fontRendererObj.drawCenteredString(s, x, y, color);
      }

   }

   public void drawCenteredString(String s, int x, int y, int color) {
      if (Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() || !Crystal.CustomFont.getBooleanValue()) {
         Wrapper.mc.fontRendererObj.drawCenteredString(s, x, y, color);
      }

      this.drawStringWithShadow(s, (float)(x - this.getStringWidth(s) / 2), (float)y, color);
   }

   public int func_175063_a(String message, float posX, float posY, int textColor) {
      return Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() ? Wrapper.mc.fontRendererObj.drawStringWithShadow(message, (float)((int)posX), (float)((int)posY), textColor) : this.drawStringWithShadow(message, posX, posY, textColor);
   }

   public int drawString(String text, float x, float y, int color, boolean shadow) {
      if (!Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue()) {
         int result = 0;
         String[] lines = text.split("\n");

         for(int i = 0; i < lines.length; ++i) {
            result = this.drawLine(lines[i], x, y + (float)(i * this.getHeight()), color, shadow);
         }

         return result;
      } else {
         Wrapper.mc.fontRendererObj.drawString(text, x, y, color, shadow);
         return -1;
      }
   }

   private int drawLine(String text, float x, float y, int color, boolean shadow) {
      if (text == null) {
         return 0;
      } else {
         GL11.glPushMatrix();
         GL11.glTranslated((double)x - 1.5D, (double)y + 0.5D, 0.0D);
         boolean wasBlend = GL11.glGetBoolean(3042);
         GlStateManager.enableAlpha();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3553);
         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         float alpha = (float)(color >> 24 & 255) / 255.0F;
         Color c = new Color(red, green, blue, alpha);
         if (text.contains("\247")) {
            String[] parts = text.split("\247");
            Color currentColor = c;
            FluxFont currentFont = this.font;
            int width = 0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;

            for(int index = 0; index < parts.length; ++index) {
               if (parts[index].length() > 0) {
                  if (index == 0) {
                     currentFont.drawString(parts[index], (double)width, 0.0D, currentColor, shadow);
                     width += currentFont.getStringWidth(parts[index]);
                  } else {
                     String words = parts[index].substring(1);
                     char type = parts[index].charAt(0);
                     int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                     if (colorIndex != -1) {
                        if (colorIndex < 16) {
                           int colorcode = this.colorCode[colorIndex];
                           currentColor = this.getColor(colorcode, alpha);
                           bold = false;
                           italic = false;
                           randomCase = false;
                           underline = false;
                           strikethrough = false;
                        } else if (colorIndex == 16) {
                           randomCase = true;
                        } else if (colorIndex == 17) {
                           bold = true;
                        } else if (colorIndex == 18) {
                           strikethrough = true;
                        } else if (colorIndex == 19) {
                           underline = true;
                        } else if (colorIndex == 20) {
                           italic = true;
                        } else if (colorIndex == 21) {
                           bold = false;
                           italic = false;
                           randomCase = false;
                           underline = false;
                           strikethrough = false;
                           currentColor = c;
                        } else if (colorIndex > 21) {
                           Color customColor = this.customColorCodes[type];
                           currentColor = new Color((float)customColor.getRed() / 255.0F, (float)customColor.getGreen() / 255.0F, (float)customColor.getBlue() / 255.0F, alpha);
                        }
                     }

                     if (bold && italic) {
                        this.boldItalicFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.boldItalicFont;
                     } else if (bold) {
                        this.boldFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.boldFont;
                     } else if (italic) {
                        this.italicFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.italicFont;
                     } else {
                        this.font.drawString(randomCase ? this.toRandom(currentFont, words) : words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.font;
                     }

                     float u = (float)this.font.getHeight() / 16.0F;
                     int h = currentFont.getStringHeight(words);
                     if (strikethrough) {
                        this.drawLine((double)width / 2.0D + 1.0D, (double)(h / 3), (double)(width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, (double)(h / 3), u);
                     }

                     if (underline) {
                        this.drawLine((double)width / 2.0D + 1.0D, (double)(h / 2), (double)(width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, (double)(h / 2), u);
                     }

                     width += currentFont.getStringWidth(words);
                  }
               }
            }
         } else {
            this.font.drawString(text, 0.0D, 0.0D, c, shadow);
         }

         if (!wasBlend) {
            GL11.glDisable(3042);
         }

         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return (int)(x + (float)this.getStringWidth(text));
      }
   }

   private String toRandom(FluxFont font, String text) {
      String newText = "";
      String allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
      char[] var8;
      int var7 = (var8 = text.toCharArray()).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         char c = var8[var6];
         if (ChatAllowedCharacters.isAllowedCharacter(c)) {
             int index = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
             newText = String.valueOf((Object)newText) + "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".toCharArray()[index];
         }
      }
      return newText;
   }

   public int getStringHeight(String text) {
      if (text == null) {
         return 0;
      } else {
         return !Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue() ? this.font.getStringHeight(text) : Wrapper.mc.fontRendererObj.FONT_HEIGHT;
      }
   }

   public int getHeight() {
      return !Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue() ? this.font.getHeight() : Wrapper.mc.fontRendererObj.FONT_HEIGHT;
   }

   public static String getFormatFromString(String p_78282_0_) {
      String var1 = "";
      int var2 = -1;
      int var3 = p_78282_0_.length();

      while((var2 = p_78282_0_.indexOf(167, var2 + 1)) != -1) {
         if (var2 < var3 - 1) {
            char var4 = p_78282_0_.charAt(var2 + 1);
            if (isFormatColor(var4)) {
               var1 = "\247" + var4;
            } else if (isFormatSpecial(var4)) {
               var1 = var1 + "\247" + var4;
            }
         }
      }

      return var1;
   }

   private static boolean isFormatSpecial(char formatChar) {
      return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
   }

   public int getColorCode(char p_175064_1_) {
      return this.colorCode["0123456789abcdef".indexOf(p_175064_1_)];
   }

   public void setBidiFlag(boolean state) {
      this.bidi = state;
   }

   public boolean getBidiFlag() {
      return this.bidi;
   }

   private int sizeStringToWidth(String str, int wrapWidth) {
      int var3 = str.length();
      int var4 = 0;
      int var5 = 0;
      int var6 = -1;

      for(boolean var7 = false; var5 < var3; ++var5) {
         char var8 = str.charAt(var5);
         switch(var8) {
         case '\n':
            --var5;
            break;
         case ' ':
            var6 = var5;
         default:
            var4 += this.getStringWidth(Character.toString(var8));
            if (var7) {
               ++var4;
            }
            break;
         case '\247':
            if (var5 < var3 - 1) {
               ++var5;
               char var9 = str.charAt(var5);
               if (var9 != 'l' && var9 != 'L') {
                  if (var9 == 'r' || var9 == 'R' || isFormatColor(var9)) {
                     var7 = false;
                  }
               } else {
                  var7 = true;
               }
            }
         }

         if (var8 == '\n') {
            ++var5;
            var6 = var5;
            break;
         }

         if (var4 > wrapWidth) {
            break;
         }
      }

      return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
   }

   private static boolean isFormatColor(char colorChar) {
      return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
   }

   public int getCharWidth(char c) {
      return !Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue() ? this.getStringWidth(Character.toString(c)) : Wrapper.mc.fontRendererObj.getCharWidth(c);
   }

   public int getStringWidth(String text) {
      if (!Wrapper.mc.getLanguageManager().isCurrentLocaleUnicode() && Crystal.CustomFont.getBooleanValue()) {
         if (text == null) {
            return 0;
         } else if (!text.contains("\247")) {
            return this.font.getStringWidth(text);
         } else {
            String[] parts = text.split("\247");
            FluxFont currentFont = this.font;
            int width = 0;
            boolean bold = false;
            boolean italic = false;

            for(int index = 0; index < parts.length; ++index) {
               if (parts[index].length() > 0) {
                  if (index == 0) {
                     width += currentFont.getStringWidth(parts[index]);
                  } else {
                     String words = parts[index].substring(1);
                     char type = parts[index].charAt(0);
                     int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                     if (colorIndex != -1) {
                        if (colorIndex < 16) {
                           bold = false;
                           italic = false;
                        } else if (colorIndex != 16) {
                           if (colorIndex == 17) {
                              bold = true;
                           } else if (colorIndex != 18 && colorIndex != 19) {
                              if (colorIndex == 20) {
                                 italic = true;
                              } else if (colorIndex == 21) {
                                 bold = false;
                                 italic = false;
                              }
                           }
                        }
                     }

                     if (bold && italic) {
                        currentFont = this.boldItalicFont;
                     } else if (bold) {
                        currentFont = this.boldFont;
                     } else if (italic) {
                        currentFont = this.italicFont;
                     } else {
                        currentFont = this.font;
                     }

                     width += currentFont.getStringWidth(words);
                  }
               }
            }

            return width;
         }
      } else {
         return Wrapper.mc.fontRendererObj.getStringWidth(text);
      }
   }

   public void setFont(Font font, boolean antiAlias, int charOffset) {
      synchronized(this) {
         this.font = new FluxFont(font, antiAlias, charOffset);
         this.boldFont = new FluxFont(font.deriveFont(1), antiAlias, charOffset);
         this.italicFont = new FluxFont(font.deriveFont(2), antiAlias, charOffset);
         this.boldItalicFont = new FluxFont(font.deriveFont(3), antiAlias, charOffset);
         this.FONT_HEIGHT = this.getHeight();
      }
   }

   public FluxFont getFont() {
      return this.font;
   }

   public String getFontName() {
      return this.font.getFont().getFontName();
   }

   public int getSize() {
      return this.font.getFont().getSize();
   }

   public List wrapWords(String text, double width) {
      List finalWords = new ArrayList();
      if ((double)this.getStringWidth(text) > width) {
         String[] words = text.split(" ");
         String currentWord = "";
         char lastColorCode = '\uffff';
         String[] var11 = words;
         int var10 = words.length;

         String s;
         for(int var9 = 0; var9 < var10; ++var9) {
            s = var11[var9];

            for(int i = 0; i < s.toCharArray().length; ++i) {
               char c = s.toCharArray()[i];
               if (c == '\247' && i < s.toCharArray().length - 1) {
                  lastColorCode = s.toCharArray()[i + 1];
               }
            }

            if ((double)this.getStringWidth(currentWord + s + " ") < width) {
               currentWord = currentWord + s + " ";
            } else {
               finalWords.add(currentWord);
               currentWord = lastColorCode == -1 ? s + " " : "\247" + lastColorCode + s + " ";
            }
         }

         if (!currentWord.equals("")) {
            if ((double)this.getStringWidth(currentWord) < width) {
               finalWords.add(lastColorCode == -1 ? currentWord + " " : "\247" + lastColorCode + currentWord + " ");
               currentWord = "";
            } else {
               Iterator var14 = this.formatString(currentWord, width).iterator();

               while(var14.hasNext()) {
                  s = (String)var14.next();
                  finalWords.add(s);
               }
            }
         }
      } else {
         finalWords.add(text);
      }

      return finalWords;
   }

   public List formatString(String s, double width) {
      List finalWords = new ArrayList();
      String currentWord = "";
      char lastColorCode = '\uffff';

      for(int i = 0; i < s.toCharArray().length; ++i) {
         char c = s.toCharArray()[i];
         if (c == '\247' && i < s.toCharArray().length - 1) {
            lastColorCode = s.toCharArray()[i + 1];
         }

         if ((double)this.getStringWidth(currentWord + c) < width) {
            currentWord = currentWord + c;
         } else {
            finalWords.add(currentWord);
            currentWord = lastColorCode == -1 ? String.valueOf(c) : "\247" + lastColorCode + c;
         }
      }

      if (!currentWord.equals("")) {
         finalWords.add(currentWord);
      }

      return finalWords;
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

   public boolean isAntiAliasing() {
      return this.font.isAntiAlias() && this.boldFont.isAntiAlias() && this.italicFont.isAntiAlias() && this.boldItalicFont.isAntiAlias();
   }

   public void setAntiAliasing(boolean antiAlias) {
      this.font.setAntiAlias(antiAlias);
      this.boldFont.setAntiAlias(antiAlias);
      this.italicFont.setAntiAlias(antiAlias);
      this.boldItalicFont.setAntiAlias(antiAlias);
   }

   private void setupMinecraftColorcodes() {
      for(int index = 0; index < 32; ++index) {
         int var6 = (index >> 3 & 1) * 85;
         int var7 = (index >> 2 & 1) * 170 + var6;
         int var8 = (index >> 1 & 1) * 170 + var6;
         int var9 = (index >> 0 & 1) * 170 + var6;
         if (index == 6) {
            var7 += 85;
         }

         if (index >= 16) {
            var7 /= 4;
            var8 /= 4;
            var9 /= 4;
         }

         this.colorCode[index] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
      }

   }

   public String trimStringToWidth(String p_78269_1_, int p_78269_2_) {
      return this.trimStringToWidth(p_78269_1_, p_78269_2_, false);
   }

   public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_) {
      StringBuilder var4 = new StringBuilder();
      int var5 = 0;
      int var6 = p_78262_3_ ? p_78262_1_.length() - 1 : 0;
      int var7 = p_78262_3_ ? -1 : 1;
      boolean var8 = false;
      boolean var9 = false;

      for(int var10 = var6; var10 >= 0 && var10 < p_78262_1_.length() && var5 < p_78262_2_; var10 += var7) {
         char var11 = p_78262_1_.charAt(var10);
         int var12 = this.getStringWidth(Character.toString(var11));
         if (var8) {
            var8 = false;
            if (var11 != 'l' && var11 != 'L') {
               if (var11 == 'r' || var11 == 'R') {
                  var9 = false;
               }
            } else {
               var9 = true;
            }
         } else if (var12 < 0) {
            var8 = true;
         } else {
            var5 += var12;
            if (var9) {
               ++var5;
            }
         }

         if (var5 > p_78262_2_) {
            break;
         }

         if (p_78262_3_) {
            var4.insert(0, var11);
         } else {
            var4.append(var11);
         }
      }

      return var4.toString();
   }

   public List listFormattedStringToWidth(String str, int wrapWidth) {
      return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
   }

   public String wrapFormattedStringToWidth(String str, int wrapWidth) {
      int var3 = this.sizeStringToWidth(str, wrapWidth);
      if (str.length() <= var3) {
         return str;
      } else {
         String var4 = str.substring(0, var3);
         char var5 = str.charAt(var3);
         boolean var6 = var5 == ' ' || var5 == '\n';
         String var7 = getFormatFromString(var4) + str.substring(var3 + (var6 ? 1 : 0));
         return var4 + "\n" + this.wrapFormattedStringToWidth(var7, wrapWidth);
      }
   }

   public Color getColor(int colorCode, float alpha) {
      return new Color((float)(colorCode >> 16) / 255.0F, (float)(colorCode >> 8 & 255) / 255.0F, (float)(colorCode & 255) / 255.0F, alpha);
   }

   private String setupColorcodeIdentifier() {
      String minecraftColorCodes = "0123456789abcdefklmnor";

      for(int i = 0; i < this.customColorCodes.length; ++i) {
         if (this.customColorCodes[i] != null) {
            minecraftColorCodes = minecraftColorCodes + (char)i;
         }
      }

      return minecraftColorCodes;
   }

   public void onResourceManagerReload(IResourceManager p_110549_1_) {
   }
}
