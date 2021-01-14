package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import optfine.Config;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import rip.autumn.core.Autumn;
import rip.autumn.module.impl.visuals.StreamerMod;

public class FontRenderer implements IResourceManagerReloadListener {
   private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
   private static final String __OBFID = "CL_00000660";
   private final TextureManager renderEngine;
   private static final Minecraft mc = Minecraft.getMinecraft();
   public static final int FONT_HEIGHT = 9;
   public Random fontRandom = new Random();
   public GameSettings gameSettings;
   public ResourceLocation locationFontTextureBase;
   public boolean enabled = true;
   public float scaleFactor = 1.0F;
   private float[] charWidth = new float[256];
   private byte[] glyphWidth = new byte[65536];
   private int[] colorCode = new int[32];
   private ResourceLocation locationFontTexture;
   protected float posX;
   protected float posY;
   private boolean unicodeFlag;
   protected boolean bidiFlag;
   protected float red;
   protected float blue;
   protected float green;
   protected float alpha;
   private int textColor;
   private boolean randomStyle;
   private boolean boldStyle;
   private boolean italicStyle;
   private boolean underlineStyle;
   private boolean strikethroughStyle;

   public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
      this.gameSettings = gameSettingsIn;
      this.locationFontTextureBase = location;
      this.locationFontTexture = location;
      this.renderEngine = textureManagerIn;
      this.unicodeFlag = unicode;
      this.locationFontTexture = getHdFontLocation(this.locationFontTextureBase);
      textureManagerIn.bindTexture(this.locationFontTexture);

      for(int i = 0; i < 32; ++i) {
         int j = (i >> 3 & 1) * 85;
         int k = (i >> 2 & 1) * 170 + j;
         int l = (i >> 1 & 1) * 170 + j;
         int i1 = (i >> 0 & 1) * 170 + j;
         if (i == 6) {
            k += 85;
         }

         if (gameSettingsIn.anaglyph) {
            int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
            int k1 = (k * 30 + l * 70) / 100;
            int l1 = (k * 30 + i1 * 70) / 100;
            k = j1;
            l = k1;
            i1 = l1;
         }

         if (i >= 16) {
            k /= 4;
            l /= 4;
            i1 /= 4;
         }

         this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
      }

      this.readGlyphSizes();
   }

   private static boolean isFormatColor(char colorChar) {
      return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
   }

   private static boolean isFormatSpecial(char formatChar) {
      return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
   }

   public static String getFormatFromString(String text) {
      String s = "";
      int i = -1;
      int j = text.length();

      while((i = text.indexOf(167, i + 1)) != -1) {
         if (i < j - 1) {
            char c0 = text.charAt(i + 1);
            if (isFormatColor(c0)) {
               s = "§" + c0;
            } else if (isFormatSpecial(c0)) {
               s = s + "§" + c0;
            }
         }
      }

      return s;
   }

   private static ResourceLocation getHdFontLocation(ResourceLocation p_getHdFontLocation_0_) {
      if (!Config.isCustomFonts()) {
         return p_getHdFontLocation_0_;
      } else if (p_getHdFontLocation_0_ == null) {
         return p_getHdFontLocation_0_;
      } else {
         String s = p_getHdFontLocation_0_.getResourcePath();
         String s1 = "textures/";
         String s2 = "mcpatcher/";
         if (!s.startsWith(s1)) {
            return p_getHdFontLocation_0_;
         } else {
            s = s.substring(s1.length());
            s = s2 + s;
            ResourceLocation resourcelocation = new ResourceLocation(p_getHdFontLocation_0_.getResourceDomain(), s);
            return Config.hasResource(Config.getResourceManager(), resourcelocation) ? resourcelocation : p_getHdFontLocation_0_;
         }
      }
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      this.locationFontTexture = getHdFontLocation(this.locationFontTextureBase);

      for(int i = 0; i < unicodePageLocations.length; ++i) {
         unicodePageLocations[i] = null;
      }

      this.readFontTexture();
   }

   private void readFontTexture() {
      BufferedImage bufferedimage;
      try {
         bufferedimage = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
      } catch (IOException var18) {
         throw new RuntimeException(var18);
      }

      int i = bufferedimage.getWidth();
      int j = bufferedimage.getHeight();
      int k = i / 16;
      int l = j / 16;
      float f = (float)i / 128.0F;
      this.scaleFactor = f;
      int[] aint = new int[i * j];
      bufferedimage.getRGB(0, 0, i, j, aint, 0, i);

      for(int i1 = 0; i1 < 256; ++i1) {
         int j1 = i1 % 16;
         int k1 = i1 / 16;
         int l1 = false;

         int l1;
         for(l1 = k - 1; l1 >= 0; --l1) {
            int i2 = j1 * k + l1;
            boolean flag = true;

            for(int j2 = 0; j2 < l && flag; ++j2) {
               int k2 = (k1 * l + j2) * i;
               int l2 = aint[i2 + k2];
               int i3 = l2 >> 24 & 255;
               if (i3 > 16) {
                  flag = false;
               }
            }

            if (!flag) {
               break;
            }
         }

         if (i1 == 65) {
            i1 = i1;
         }

         if (i1 == 32) {
            if (k <= 8) {
               l1 = (int)(2.0F * f);
            } else {
               l1 = (int)(1.5F * f);
            }
         }

         this.charWidth[i1] = (float)(l1 + 1) / f + 1.0F;
      }

      this.readCustomCharWidths();
   }

   private void readGlyphSizes() {
      InputStream inputstream = null;

      try {
         inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
         inputstream.read(this.glyphWidth);
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      } finally {
         IOUtils.closeQuietly(inputstream);
      }

   }

   private float func_181559_a(char p_181559_1_, boolean p_181559_2_) {
      if (p_181559_1_ == ' ') {
         return this.charWidth[p_181559_1_];
      } else {
         int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(p_181559_1_);
         return i != -1 && !this.unicodeFlag ? this.renderDefaultChar(i, p_181559_2_) : this.renderUnicodeChar(p_181559_1_, p_181559_2_);
      }
   }

   private float renderDefaultChar(int p_78266_1_, boolean p_78266_2_) {
      int i = p_78266_1_ % 16 * 8;
      int j = p_78266_1_ / 16 * 8;
      int k = p_78266_2_ ? 1 : 0;
      this.renderEngine.bindTexture(this.locationFontTexture);
      float f = this.charWidth[p_78266_1_];
      float f1 = 7.99F;
      GL11.glBegin(5);
      GL11.glTexCoord2f((float)i / 128.0F, (float)j / 128.0F);
      GL11.glVertex3f(this.posX + (float)k, this.posY, 0.0F);
      GL11.glTexCoord2f((float)i / 128.0F, ((float)j + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX - (float)k, this.posY + 7.99F, 0.0F);
      GL11.glTexCoord2f(((float)i + f1 - 1.0F) / 128.0F, (float)j / 128.0F);
      GL11.glVertex3f(this.posX + f1 - 1.0F + (float)k, this.posY, 0.0F);
      GL11.glTexCoord2f(((float)i + f1 - 1.0F) / 128.0F, ((float)j + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX + f1 - 1.0F - (float)k, this.posY + 7.99F, 0.0F);
      GL11.glEnd();
      return f;
   }

   private ResourceLocation getUnicodePageLocation(int p_111271_1_) {
      if (unicodePageLocations[p_111271_1_] == null) {
         unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", p_111271_1_));
         unicodePageLocations[p_111271_1_] = getHdFontLocation(unicodePageLocations[p_111271_1_]);
      }

      return unicodePageLocations[p_111271_1_];
   }

   private void loadGlyphTexture(int p_78257_1_) {
      this.renderEngine.bindTexture(this.getUnicodePageLocation(p_78257_1_));
   }

   private float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_) {
      if (this.glyphWidth[p_78277_1_] == 0) {
         return 0.0F;
      } else {
         int i = p_78277_1_ / 256;
         this.loadGlyphTexture(i);
         int j = this.glyphWidth[p_78277_1_] >>> 4;
         int k = this.glyphWidth[p_78277_1_] & 15;
         float f = (float)j;
         float f1 = (float)(k + 1);
         float f2 = (float)(p_78277_1_ % 16 * 16) + f;
         float f3 = (float)((p_78277_1_ & 255) / 16 * 16);
         float f4 = f1 - f - 0.02F;
         float f5 = p_78277_2_ ? 1.0F : 0.0F;
         GL11.glBegin(5);
         GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
         GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
         GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
         GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
         GL11.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
         GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
         GL11.glEnd();
         return (f1 - f) / 2.0F + 1.0F;
      }
   }

   public int drawStringWithShadow(String text, float x, float y, int color) {
      return this.drawString(text, x, y, color, true);
   }

   public void drawCenteredString(String s, int x, int y, int colour) {
      x -= this.getStringWidth(s) / 2;
      this.drawStringWithShadow(s, (float)x, (float)y, colour);
   }

   public int drawString(String text, int x, int y, int color) {
      return !this.enabled ? 0 : this.drawString(text, (float)x, (float)y, color, false);
   }

   public int drawString(String text, float x, float y, int color, boolean dropShadow) {
      GlStateManager.enableAlpha();
      this.resetStyles();
      int i;
      if (dropShadow) {
         i = this.renderString(text, x + 1.0F, y + 1.0F, color, true);
         i = Math.max(i, this.renderString(text, x, y, color, false));
      } else {
         i = this.renderString(text, x, y, color, false);
      }

      return i;
   }

   protected String bidiReorder(String p_147647_1_) {
      try {
         Bidi bidi = new Bidi((new ArabicShaping(8)).shape(p_147647_1_), 127);
         bidi.setReorderingMode(0);
         return bidi.writeReordered(2);
      } catch (ArabicShapingException var3) {
         return p_147647_1_;
      }
   }

   private void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_) {
      for(int i = 0; i < p_78255_1_.length(); ++i) {
         char c0 = p_78255_1_.charAt(i);
         int i1;
         int j1;
         if (c0 == 167 && i + 1 < p_78255_1_.length()) {
            i1 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i + 1));
            if (i1 < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (i1 < 0 || i1 > 15) {
                  i1 = 15;
               }

               if (p_78255_2_) {
                  i1 += 16;
               }

               j1 = this.colorCode[i1];
               this.textColor = j1;
               GlStateManager.color((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
            } else if (i1 == 16) {
               this.randomStyle = true;
            } else if (i1 == 17) {
               this.boldStyle = true;
            } else if (i1 == 18) {
               this.strikethroughStyle = true;
            } else if (i1 == 19) {
               this.underlineStyle = true;
            } else if (i1 == 20) {
               this.italicStyle = true;
            } else if (i1 == 21) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               GlStateManager.color(this.red, this.blue, this.green, this.alpha);
            }

            ++i;
         } else {
            i1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
            if (this.randomStyle && i1 != -1) {
               j1 = this.getCharWidth(c0);

               char c1;
               do {
                  i1 = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".length());
                  c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".charAt(i1);
               } while(j1 != this.getCharWidth(c1));

               c0 = c1;
            }

            float f1 = this.unicodeFlag ? 0.5F : 1.0F / this.scaleFactor;
            boolean flag = (c0 == 0 || i1 == -1 || this.unicodeFlag) && p_78255_2_;
            if (flag) {
               this.posX -= f1;
               this.posY -= f1;
            }

            float f = this.func_181559_a(c0, this.italicStyle);
            if (flag) {
               this.posX += f1;
               this.posY += f1;
            }

            if (this.boldStyle) {
               this.posX += f1;
               if (flag) {
                  this.posX -= f1;
                  this.posY -= f1;
               }

               this.func_181559_a(c0, this.italicStyle);
               this.posX -= f1;
               if (flag) {
                  this.posX += f1;
                  this.posY += f1;
               }

               f += f1;
            }

            Tessellator tessellator1;
            WorldRenderer worldrenderer1;
            if (this.strikethroughStyle) {
               tessellator1 = Tessellator.getInstance();
               worldrenderer1 = tessellator1.getWorldRenderer();
               GlStateManager.disableTexture2D();
               worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
               worldrenderer1.pos((double)this.posX, (double)(this.posY + (float)(9 / 2)), 0.0D).endVertex();
               worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)(9 / 2)), 0.0D).endVertex();
               worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)(9 / 2) - 1.0F), 0.0D).endVertex();
               worldrenderer1.pos((double)this.posX, (double)(this.posY + (float)(9 / 2) - 1.0F), 0.0D).endVertex();
               tessellator1.draw();
               GlStateManager.enableTexture2D();
            }

            if (this.underlineStyle) {
               tessellator1 = Tessellator.getInstance();
               worldrenderer1 = tessellator1.getWorldRenderer();
               GlStateManager.disableTexture2D();
               worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
               int l = this.underlineStyle ? -1 : 0;
               worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + 9.0F), 0.0D).endVertex();
               worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + 9.0F), 0.0D).endVertex();
               worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + 9.0F - 1.0F), 0.0D).endVertex();
               worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + 9.0F - 1.0F), 0.0D).endVertex();
               tessellator1.draw();
               GlStateManager.enableTexture2D();
            }

            this.posX += f;
         }
      }

   }

   private int renderStringAligned(String text, int x, int y, int p_78274_4_, int color, boolean dropShadow) {
      if (this.bidiFlag) {
         int i = this.getStringWidth(this.bidiReorder(text));
         x = x + p_78274_4_ - i;
      }

      return this.renderString(text, (float)x, (float)y, color, dropShadow);
   }

   protected int renderString(String text, float x, float y, int color, boolean dropShadow) {
      if (mc.theWorld != null && ((StreamerMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(StreamerMod.class)).isEnabled()) {
         List playerEntities = mc.theWorld.playerEntities;
         Iterator var7 = playerEntities.iterator();

         while(var7.hasNext()) {
            EntityPlayer player = (EntityPlayer)var7.next();
            if (text.equals(player.getName())) {
               text = player instanceof EntityPlayerSP ? "You" : "Enemy";
            }
         }
      }

      if (text == null) {
         return 0;
      } else {
         if (this.bidiFlag) {
            text = this.bidiReorder(text);
         }

         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (dropShadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         this.red = (float)(color >> 16 & 255) / 255.0F;
         this.blue = (float)(color >> 8 & 255) / 255.0F;
         this.green = (float)(color & 255) / 255.0F;
         this.alpha = (float)(color >> 24 & 255) / 255.0F;
         GlStateManager.color(this.red, this.blue, this.green, this.alpha);
         this.posX = x;
         this.posY = y;
         this.renderStringAtPos(text, dropShadow);
         return (int)this.posX;
      }
   }

   public int getStringWidth(String text) {
      if (text == null) {
         return 0;
      } else {
         float f = 0.0F;
         boolean flag = false;

         for(int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            float f1 = this.getCharWidthFloat(c0);
            if (f1 < 0.0F && i < text.length() - 1) {
               ++i;
               c0 = text.charAt(i);
               if (c0 != 'l' && c0 != 'L') {
                  if (c0 == 'r' || c0 == 'R') {
                     flag = false;
                  }
               } else {
                  flag = true;
               }

               f1 = 0.0F;
            }

            f += f1;
            if (flag && f1 > 0.0F) {
               ++f;
            }
         }

         return (int)f;
      }
   }

   public int getCharWidth(char character) {
      return Math.round(this.getCharWidthFloat(character));
   }

   private float getCharWidthFloat(char p_getCharWidthFloat_1_) {
      if (p_getCharWidthFloat_1_ == 167) {
         return -1.0F;
      } else if (p_getCharWidthFloat_1_ == ' ') {
         return this.charWidth[32];
      } else {
         int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(p_getCharWidthFloat_1_);
         if (p_getCharWidthFloat_1_ > 0 && i != -1 && !this.unicodeFlag) {
            return this.charWidth[i];
         } else if (this.glyphWidth[p_getCharWidthFloat_1_] != 0) {
            int j = this.glyphWidth[p_getCharWidthFloat_1_] >>> 4;
            int k = this.glyphWidth[p_getCharWidthFloat_1_] & 15;
            if (k > 7) {
               k = 15;
               j = 0;
            }

            ++k;
            return (float)((k - j) / 2 + 1);
         } else {
            return 0.0F;
         }
      }
   }

   public String trimStringToWidth(String text, int width) {
      return this.trimStringToWidth(text, width, false);
   }

   public String trimStringToWidth(String text, int width, boolean reverse) {
      StringBuilder stringbuilder = new StringBuilder();
      float f = 0.0F;
      int i = reverse ? text.length() - 1 : 0;
      int j = reverse ? -1 : 1;
      boolean flag = false;
      boolean flag1 = false;

      for(int k = i; k >= 0 && k < text.length() && f < (float)width; k += j) {
         char c0 = text.charAt(k);
         float f1 = this.getCharWidthFloat(c0);
         if (flag) {
            flag = false;
            if (c0 != 'l' && c0 != 'L') {
               if (c0 == 'r' || c0 == 'R') {
                  flag1 = false;
               }
            } else {
               flag1 = true;
            }
         } else if (f1 < 0.0F) {
            flag = true;
         } else {
            f += f1;
            if (flag1) {
               ++f;
            }
         }

         if (f > (float)width) {
            break;
         }

         if (reverse) {
            stringbuilder.insert(0, c0);
         } else {
            stringbuilder.append(c0);
         }
      }

      return stringbuilder.toString();
   }

   private String trimStringNewline(String text) {
      while(text != null && text.endsWith("\n")) {
         text = text.substring(0, text.length() - 1);
      }

      return text;
   }

   public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
      this.resetStyles();
      this.textColor = textColor;
      str = this.trimStringNewline(str);
      this.renderSplitString(str, x, y, wrapWidth, false);
   }

   private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
      for(Iterator var6 = this.listFormattedStringToWidth(str, wrapWidth).iterator(); var6.hasNext(); y += 9) {
         Object s = var6.next();
         this.renderStringAligned((String)s, x, y, wrapWidth, this.textColor, addShadow);
      }

   }

   public int splitStringWidth(String p_78267_1_, int p_78267_2_) {
      return 9 * this.listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
   }

   public boolean getUnicodeFlag() {
      return this.unicodeFlag;
   }

   public void setUnicodeFlag(boolean unicodeFlagIn) {
      this.unicodeFlag = unicodeFlagIn;
   }

   public List listFormattedStringToWidth(String str, int wrapWidth) {
      return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
   }

   String wrapFormattedStringToWidth(String str, int wrapWidth) {
      int i = this.sizeStringToWidth(str, wrapWidth);
      if (str.length() <= i) {
         return str;
      } else {
         String s = str.substring(0, i);
         char c0 = str.charAt(i);
         boolean flag = c0 == ' ' || c0 == '\n';
         String s1 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
         return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
      }
   }

   private int sizeStringToWidth(String str, int wrapWidth) {
      int i = str.length();
      float f = 0.0F;
      int j = 0;
      int k = -1;

      for(boolean flag = false; j < i; ++j) {
         char c0 = str.charAt(j);
         switch(c0) {
         case '\n':
            --j;
            break;
         case ' ':
            k = j;
         default:
            f += this.getCharWidthFloat(c0);
            if (flag) {
               ++f;
            }
            break;
         case '§':
            if (j < i - 1) {
               ++j;
               char c1 = str.charAt(j);
               if (c1 != 'l' && c1 != 'L') {
                  if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
                     flag = false;
                  }
               } else {
                  flag = true;
               }
            }
         }

         if (c0 == '\n') {
            ++j;
            k = j;
            break;
         }

         if (f > (float)wrapWidth) {
            break;
         }
      }

      return j != i && k != -1 && k < j ? k : j;
   }

   public boolean getBidiFlag() {
      return this.bidiFlag;
   }

   public void setBidiFlag(boolean bidiFlagIn) {
      this.bidiFlag = bidiFlagIn;
   }

   public int getColorCode(char character) {
      int i = "0123456789abcdef".indexOf(character);
      return i >= 0 && i < this.colorCode.length ? this.colorCode[i] : 16777215;
   }

   private void readCustomCharWidths() {
      String s = this.locationFontTexture.getResourcePath();
      String s1 = ".png";
      if (s.endsWith(s1)) {
         String s2 = s.substring(0, s.length() - s1.length()) + ".properties";

         try {
            ResourceLocation resourcelocation = new ResourceLocation(this.locationFontTexture.getResourceDomain(), s2);
            InputStream inputstream = Config.getResourceStream(Config.getResourceManager(), resourcelocation);
            if (inputstream == null) {
               return;
            }

            Config.log("Loading " + s2);
            Properties properties = new Properties();
            properties.load(inputstream);
            Iterator var7 = properties.keySet().iterator();

            while(var7.hasNext()) {
               Object s30 = var7.next();
               String s3 = (String)s30;
               String s4 = "width.";
               if (s3.startsWith(s4)) {
                  String s5 = s3.substring(s4.length());
                  int i = Config.parseInt(s5, -1);
                  if (i >= 0 && i < this.charWidth.length) {
                     String s6 = properties.getProperty(s3);
                     float f = Config.parseFloat(s6, -1.0F);
                     if (f >= 0.0F) {
                        this.charWidth[i] = f;
                     }
                  }
               }
            }
         } catch (FileNotFoundException var15) {
         } catch (IOException var16) {
            var16.printStackTrace();
         }
      }

   }
}
