package me.existdev.exist.ttf;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.existdev.exist.ttf.CustomFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CustomFontRenderer extends FontRenderer {
   // $FF: synthetic field
   public final Random fontRandom = new Random();
   // $FF: synthetic field
   private final Color[] customColorCodes = new Color[256];
   // $FF: synthetic field
   private final int[] colorCode = new int[32];
   // $FF: synthetic field
   private CustomFont font;
   // $FF: synthetic field
   private CustomFont boldFont;
   // $FF: synthetic field
   private CustomFont italicFont;
   // $FF: synthetic field
   private CustomFont boldItalicFont;
   // $FF: synthetic field
   private String colorcodeIdentifiers = "0123456789abcdefklmnor";
   // $FF: synthetic field
   private boolean bidi;

   // $FF: synthetic method
   public CustomFontRenderer(Font font, boolean antiAlias, int charOffset) {
      super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
      this.setFont(font, antiAlias, charOffset);
      this.customColorCodes[113] = new Color(0, 90, 163);
      this.colorcodeIdentifiers = this.setupColorcodeIdentifier();
      this.setupMinecraftColorcodes();
      this.FONT_HEIGHT = this.getHeight();
   }

   // $FF: synthetic method
   public int drawString(String s, float x, float y, int color) {
      return this.drawString(s, x, y, color, false);
   }

   // $FF: synthetic method
   public int drawString(String s, double x, double y, int color) {
      return this.drawString(s, x, y, color, false);
   }

   // $FF: synthetic method
   public int drawStringWithShadow(String s, float x, float y, int color) {
      return this.drawString(s, x, y, color, false);
   }

   // $FF: synthetic method
   public void drawCenteredString(String s, int x, int y, int color, boolean shadow) {
      if(shadow) {
         this.drawStringWithShadow(s, (float)(x - this.getStringWidth(s) / 2), (float)y, color);
      } else {
         this.drawString(s, x - this.getStringWidth(s) / 2, y, color);
      }

   }

   // $FF: synthetic method
   public void drawCenteredStringXY(String s, int x, int y, int color, boolean shadow) {
      this.drawCenteredString(s, x, y - this.getHeight() / 2, color, shadow);
   }

   // $FF: synthetic method
   public void drawCenteredString(String s, int x, int y, int color) {
      this.drawStringWithShadow(s, (float)(x - this.getStringWidth(s) / 2), (float)y, color);
   }

   // $FF: synthetic method
   public int drawString(String text, double x, double y, int color, boolean shadow) {
      int result = 0;
      String[] lines = text.split("\n");

      for(int i = 0; i < lines.length; ++i) {
         result = this.drawLine(lines[i], x, y + (double)(i * this.getHeight()), color, shadow);
      }

      return result;
   }

   // $FF: synthetic method
   public int drawString(String text, float x, float y, int color, boolean shadow) {
      int result = 0;
      String[] lines = text.split("\n");

      for(int i = 0; i < lines.length; ++i) {
         result = this.drawLine(lines[i], x, y + (float)(i * this.getHeight()), color, shadow);
      }

      return result;
   }

   // $FF: synthetic method
   private int drawLine(String text, float x, float y, int color, boolean shadow) {
      if(text == null) {
         return 0;
      } else {
         GL11.glPushMatrix();
         GL11.glTranslated((double)x - 1.5D, (double)y + 0.5D, 0.0D);
         boolean wasBlend = GL11.glGetBoolean(3042);
         GlStateManager.enableAlpha();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3553);
         if((color & -67108864) == 0) {
            color |= -16777216;
         }

         if(shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         float alpha = (float)(color >> 24 & 255) / 255.0F;
         Color c = new Color(red, green, blue, alpha);
         if(text.contains("§")) {
            String[] parts = text.split("§");
            Color currentColor = c;
            CustomFont currentFont = this.font;
            int width = 0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;

            for(int index = 0; index < parts.length; ++index) {
               if(parts[index].length() > 0) {
                  if(index == 0) {
                     currentFont.drawString(parts[index], (double)width, 0.0D, currentColor, shadow);
                     width += currentFont.getStringWidth(parts[index]);
                  } else {
                     String words = parts[index].substring(1);
                     char type = parts[index].charAt(0);
                     int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                     if(colorIndex != -1) {
                        if(colorIndex < 16) {
                           int u = this.colorCode[colorIndex];
                           currentColor = this.getColor(u, alpha);
                           bold = false;
                           italic = false;
                           randomCase = false;
                           underline = false;
                           strikethrough = false;
                        } else if(colorIndex == 16) {
                           randomCase = true;
                        } else if(colorIndex == 17) {
                           bold = true;
                        } else if(colorIndex == 18) {
                           strikethrough = true;
                        } else if(colorIndex == 19) {
                           underline = true;
                        } else if(colorIndex == 20) {
                           italic = true;
                        } else if(colorIndex == 21) {
                           bold = false;
                           italic = false;
                           randomCase = false;
                           underline = false;
                           strikethrough = false;
                           currentColor = c;
                        } else if(colorIndex > 21) {
                           Color var27 = this.customColorCodes[type];
                           currentColor = new Color((float)var27.getRed() / 255.0F, (float)var27.getGreen() / 255.0F, (float)var27.getBlue() / 255.0F, alpha);
                        }
                     }

                     if(bold && italic) {
                        this.boldItalicFont.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.boldItalicFont;
                     } else if(bold) {
                        this.boldFont.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.boldFont;
                     } else if(italic) {
                        this.italicFont.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.italicFont;
                     } else {
                        this.font.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.font;
                     }

                     float var28 = (float)this.font.getHeight() / 16.0F;
                     int h = currentFont.getStringHeight(words);
                     if(strikethrough) {
                        this.drawLine((double)width / 2.0D + 1.0D, (double)(h / 3), (double)(width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, (double)(h / 3), var28);
                     }

                     if(underline) {
                        this.drawLine((double)width / 2.0D + 1.0D, (double)(h / 2), (double)(width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, (double)(h / 2), var28);
                     }

                     width += currentFont.getStringWidth(words);
                  }
               }
            }
         } else {
            this.font.drawString(text, 0.0D, 0.0D, c, shadow);
         }

         if(!wasBlend) {
            GL11.glDisable(3042);
         }

         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return (int)(x + (float)this.getStringWidth(text));
      }
   }

   // $FF: synthetic method
   private int drawLine(String text, double x, double y, int color, boolean shadow) {
      if(text == null) {
         return 0;
      } else {
         GL11.glPushMatrix();
         GL11.glTranslated(x - 1.5D, y + 0.5D, 0.0D);
         boolean wasBlend = GL11.glGetBoolean(3042);
         GlStateManager.enableAlpha();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3553);
         if((color & -67108864) == 0) {
            color |= -16777216;
         }

         if(shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         float alpha = (float)(color >> 24 & 255) / 255.0F;
         Color c = new Color(red, green, blue, alpha);
         if(text.contains("§")) {
            String[] parts = text.split("§");
            Color currentColor = c;
            CustomFont currentFont = this.font;
            int width = 0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;

            for(int index = 0; index < parts.length; ++index) {
               if(parts[index].length() > 0) {
                  if(index == 0) {
                     currentFont.drawString(parts[index], (double)width, 0.0D, currentColor, shadow);
                     width += currentFont.getStringWidth(parts[index]);
                  } else {
                     String words = parts[index].substring(1);
                     char type = parts[index].charAt(0);
                     int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                     if(colorIndex != -1) {
                        if(colorIndex < 16) {
                           int u = this.colorCode[colorIndex];
                           currentColor = this.getColor(u, alpha);
                           bold = false;
                           italic = false;
                           randomCase = false;
                           underline = false;
                           strikethrough = false;
                        } else if(colorIndex == 16) {
                           randomCase = true;
                        } else if(colorIndex == 17) {
                           bold = true;
                        } else if(colorIndex == 18) {
                           strikethrough = true;
                        } else if(colorIndex == 19) {
                           underline = true;
                        } else if(colorIndex == 20) {
                           italic = true;
                        } else if(colorIndex == 21) {
                           bold = false;
                           italic = false;
                           randomCase = false;
                           underline = false;
                           strikethrough = false;
                           currentColor = c;
                        } else if(colorIndex > 21) {
                           Color var29 = this.customColorCodes[type];
                           currentColor = new Color((float)var29.getRed() / 255.0F, (float)var29.getGreen() / 255.0F, (float)var29.getBlue() / 255.0F, alpha);
                        }
                     }

                     if(bold && italic) {
                        this.boldItalicFont.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.boldItalicFont;
                     } else if(bold) {
                        this.boldFont.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.boldFont;
                     } else if(italic) {
                        this.italicFont.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.italicFont;
                     } else {
                        this.font.drawString(randomCase?this.toRandom(currentFont, words):words, (double)width, 0.0D, currentColor, shadow);
                        currentFont = this.font;
                     }

                     float var30 = (float)this.font.getHeight() / 16.0F;
                     int h = currentFont.getStringHeight(words);
                     if(strikethrough) {
                        this.drawLine((double)width / 2.0D + 1.0D, (double)(h / 3), (double)(width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, (double)(h / 3), var30);
                     }

                     if(underline) {
                        this.drawLine((double)width / 2.0D + 1.0D, (double)(h / 2), (double)(width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, (double)(h / 2), var30);
                     }

                     width += currentFont.getStringWidth(words);
                  }
               }
            }
         } else {
            this.font.drawString(text, 0.0D, 0.0D, c, shadow);
         }

         if(!wasBlend) {
            GL11.glDisable(3042);
         }

         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return (int)(x + (double)this.getStringWidth(text));
      }
   }

   // $FF: synthetic method
   private String toRandom(CustomFont font, String text) {
      String newText = "";
      String allowedCharacters = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";
      char[] var8;
      int var7 = (var8 = text.toCharArray()).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         char c = var8[var6];
         if(ChatAllowedCharacters.isAllowedCharacter(c)) {
            int index = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".length());
            newText = newText + "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".toCharArray()[index];
         }
      }

      return newText;
   }

   // $FF: synthetic method
   public int getStringHeight(String text) {
      return text == null?0:this.font.getStringHeight(text) / 2;
   }

   // $FF: synthetic method
   public int getHeight() {
      return this.font.getHeight() / 2;
   }

   // $FF: synthetic method
   public static String getFormatFromString(String p_78282_0_) {
      String var1 = "";
      int var2 = -1;
      int var3 = p_78282_0_.length();

      while((var2 = p_78282_0_.indexOf(167, var2 + 1)) != -1) {
         if(var2 < var3 - 1) {
            char var4 = p_78282_0_.charAt(var2 + 1);
            if(isFormatColor(var4)) {
               var1 = "§" + var4;
            } else if(isFormatSpecial(var4)) {
               var1 = var1 + "§" + var4;
            }
         }
      }

      return var1;
   }

   // $FF: synthetic method
   private static boolean isFormatSpecial(char formatChar) {
      return formatChar >= 107 && formatChar <= 111 || formatChar >= 75 && formatChar <= 79 || formatChar == 114 || formatChar == 82;
   }

   // $FF: synthetic method
   public int getColorCode(char p_175064_1_) {
      return this.colorCode["0123456789abcdef".indexOf(p_175064_1_)];
   }

   // $FF: synthetic method
   public void setBidiFlag(boolean state) {
      this.bidi = state;
   }

   // $FF: synthetic method
   public boolean getBidiFlag() {
      return this.bidi;
   }

   // $FF: synthetic method
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
            if(var7) {
               ++var4;
            }
            break;
         case '§':
            if(var5 < var3 - 1) {
               ++var5;
               char var9 = str.charAt(var5);
               if(var9 != 108 && var9 != 76) {
                  if(var9 == 114 || var9 == 82 || isFormatColor(var9)) {
                     var7 = false;
                  }
               } else {
                  var7 = true;
               }
            }
         }

         if(var8 == 10) {
            ++var5;
            var6 = var5;
            break;
         }

         if(var4 > wrapWidth) {
            break;
         }
      }

      return var5 != var3 && var6 != -1 && var6 < var5?var6:var5;
   }

   // $FF: synthetic method
   private static boolean isFormatColor(char colorChar) {
      return colorChar >= 48 && colorChar <= 57 || colorChar >= 97 && colorChar <= 102 || colorChar >= 65 && colorChar <= 70;
   }

   // $FF: synthetic method
   public int getCharWidth(char c) {
      return this.getStringWidth(Character.toString(c));
   }

   // $FF: synthetic method
   public int getStringWidth(String text) {
      if(text == null) {
         return 0;
      } else if(!text.contains("§")) {
         return this.font.getStringWidth(text) / 2;
      } else {
         String[] parts = text.split("§");
         CustomFont currentFont = this.font;
         int width = 0;
         boolean bold = false;
         boolean italic = false;

         for(int index = 0; index < parts.length; ++index) {
            if(parts[index].length() > 0) {
               if(index == 0) {
                  width += currentFont.getStringWidth(parts[index]);
               } else {
                  String words = parts[index].substring(1);
                  char type = parts[index].charAt(0);
                  int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                  if(colorIndex != -1) {
                     if(colorIndex < 16) {
                        bold = false;
                        italic = false;
                     } else if(colorIndex != 16) {
                        if(colorIndex == 17) {
                           bold = true;
                        } else if(colorIndex != 18 && colorIndex != 19) {
                           if(colorIndex == 20) {
                              italic = true;
                           } else if(colorIndex == 21) {
                              bold = false;
                              italic = false;
                           }
                        }
                     }
                  }

                  if(bold && italic) {
                     currentFont = this.boldItalicFont;
                  } else if(bold) {
                     currentFont = this.boldFont;
                  } else if(italic) {
                     currentFont = this.italicFont;
                  } else {
                     currentFont = this.font;
                  }

                  width += currentFont.getStringWidth(words);
               }
            }
         }

         return width / 2;
      }
   }

   // $FF: synthetic method
   public void setFont(Font font, boolean antiAlias, int charOffset) {
      synchronized(this) {
         this.font = new CustomFont(font, antiAlias, charOffset);
         this.boldFont = new CustomFont(font.deriveFont(1), antiAlias, charOffset);
         this.italicFont = new CustomFont(font.deriveFont(2), antiAlias, charOffset);
         this.boldItalicFont = new CustomFont(font.deriveFont(3), antiAlias, charOffset);
         this.FONT_HEIGHT = this.getHeight();
      }
   }

   // $FF: synthetic method
   public CustomFont getFont() {
      return this.font;
   }

   // $FF: synthetic method
   public String getFontName() {
      return this.font.getFont().getFontName();
   }

   // $FF: synthetic method
   public int getSize() {
      return this.font.getFont().getSize();
   }

   // $FF: synthetic method
   public List wrapWords(String text, double width) {
      ArrayList finalWords = new ArrayList();
      if((double)this.getStringWidth(text) > width) {
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
               if(c == 167 && i < s.toCharArray().length - 1) {
                  lastColorCode = s.toCharArray()[i + 1];
               }
            }

            if((double)this.getStringWidth(currentWord + s + " ") < width) {
               currentWord = currentWord + s + " ";
            } else {
               finalWords.add(currentWord);
               currentWord = lastColorCode == -1?s + " ":"§" + lastColorCode + s + " ";
            }
         }

         if(!currentWord.equals("")) {
            if((double)this.getStringWidth(currentWord) < width) {
               finalWords.add(lastColorCode == -1?currentWord + " ":"§" + lastColorCode + currentWord + " ");
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

   // $FF: synthetic method
   public List formatString(String s, double width) {
      ArrayList finalWords = new ArrayList();
      String currentWord = "";
      char lastColorCode = '\uffff';

      for(int i = 0; i < s.toCharArray().length; ++i) {
         char c = s.toCharArray()[i];
         if(c == 167 && i < s.toCharArray().length - 1) {
            lastColorCode = s.toCharArray()[i + 1];
         }

         if((double)this.getStringWidth(currentWord + c) < width) {
            currentWord = currentWord + c;
         } else {
            finalWords.add(currentWord);
            currentWord = lastColorCode == -1?String.valueOf(c):"§" + lastColorCode + c;
         }
      }

      if(!currentWord.equals("")) {
         finalWords.add(currentWord);
      }

      return finalWords;
   }

   // $FF: synthetic method
   private void drawLine(double x, double y, double x1, double y1, float width) {
      GL11.glDisable(3553);
      GL11.glLineWidth(width);
      GL11.glBegin(1);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x1, y1);
      GL11.glEnd();
      GL11.glEnable(3553);
   }

   // $FF: synthetic method
   public boolean isAntiAliasing() {
      return this.font.isAntiAlias() && this.boldFont.isAntiAlias() && this.italicFont.isAntiAlias() && this.boldItalicFont.isAntiAlias();
   }

   // $FF: synthetic method
   public void setAntiAliasing(boolean antiAlias) {
      this.font.setAntiAlias(antiAlias);
      this.boldFont.setAntiAlias(antiAlias);
      this.italicFont.setAntiAlias(antiAlias);
      this.boldItalicFont.setAntiAlias(antiAlias);
   }

   // $FF: synthetic method
   private void setupMinecraftColorcodes() {
      for(int index = 0; index < 32; ++index) {
         int var6 = (index >> 3 & 1) * 85;
         int var7 = (index >> 2 & 1) * 170 + var6;
         int var8 = (index >> 1 & 1) * 170 + var6;
         int var9 = (index >> 0 & 1) * 170 + var6;
         if(index == 6) {
            var7 += 85;
         }

         if(index >= 16) {
            var7 /= 4;
            var8 /= 4;
            var9 /= 4;
         }

         this.colorCode[index] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
      }

   }

   // $FF: synthetic method
   public String trimStringToWidth(String p_78269_1_, int p_78269_2_) {
      return this.trimStringToWidth(p_78269_1_, p_78269_2_, false);
   }

   // $FF: synthetic method
   public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_) {
      StringBuilder var4 = new StringBuilder();
      int var5 = 0;
      int var6 = p_78262_3_?p_78262_1_.length() - 1:0;
      int var7 = p_78262_3_?-1:1;
      boolean var8 = false;
      boolean var9 = false;

      for(int var10 = var6; var10 >= 0 && var10 < p_78262_1_.length() && var5 < p_78262_2_; var10 += var7) {
         char var11 = p_78262_1_.charAt(var10);
         int var12 = this.getStringWidth(Character.toString(var11));
         if(var8) {
            var8 = false;
            if(var11 != 108 && var11 != 76) {
               if(var11 == 114 || var11 == 82) {
                  var9 = false;
               }
            } else {
               var9 = true;
            }
         } else if(var12 < 0) {
            var8 = true;
         } else {
            var5 += var12;
            if(var9) {
               ++var5;
            }
         }

         if(var5 > p_78262_2_) {
            break;
         }

         if(p_78262_3_) {
            var4.insert(0, var11);
         } else {
            var4.append(var11);
         }
      }

      return var4.toString();
   }

   // $FF: synthetic method
   public Color getColor(int colorCode, float alpha) {
      return new Color((float)(colorCode >> 16) / 255.0F, (float)(colorCode >> 8 & 255) / 255.0F, (float)(colorCode & 255) / 255.0F, alpha);
   }

   // $FF: synthetic method
   private String setupColorcodeIdentifier() {
      String minecraftColorCodes = "0123456789abcdefklmnor";

      for(int i = 0; i < this.customColorCodes.length; ++i) {
         if(this.customColorCodes[i] != null) {
            minecraftColorCodes = minecraftColorCodes + (char)i;
         }
      }

      return minecraftColorCodes;
   }

   // $FF: synthetic method
   public void onResourceManagerReload(IResourceManager p_110549_1_) {
   }
}
