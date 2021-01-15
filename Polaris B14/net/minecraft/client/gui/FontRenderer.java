/*      */ package net.minecraft.client.gui;
/*      */ 
/*      */ import com.ibm.icu.text.ArabicShaping;
/*      */ import com.ibm.icu.text.ArabicShapingException;
/*      */ import com.ibm.icu.text.Bidi;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.Random;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureUtil;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.IResource;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import optfine.Config;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import rip.jutting.polaris.Polaris;
/*      */ import rip.jutting.polaris.friend.Friend;
/*      */ import rip.jutting.polaris.friend.FriendManager;
/*      */ import rip.jutting.polaris.module.Module;
/*      */ import rip.jutting.polaris.module.ModuleManager;
/*      */ 
/*      */ 
/*      */ public class FontRenderer
/*      */   implements IResourceManagerReloadListener
/*      */ {
/*   38 */   private static final ResourceLocation[] unicodePageLocations = new ResourceLocation['Ā'];
/*      */   
/*      */ 
/*   41 */   private float[] charWidth = new float['Ā'];
/*      */   
/*      */ 
/*   44 */   public int FONT_HEIGHT = 9;
/*   45 */   public Random fontRandom = new Random();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   50 */   private byte[] glyphWidth = new byte[65536];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   56 */   private int[] colorCode = new int[32];
/*      */   
/*      */ 
/*      */   private ResourceLocation locationFontTexture;
/*      */   
/*      */ 
/*      */   private final TextureManager renderEngine;
/*      */   
/*      */ 
/*      */   private float posX;
/*      */   
/*      */ 
/*      */   private float posY;
/*      */   
/*      */ 
/*      */   private boolean unicodeFlag;
/*      */   
/*      */ 
/*      */   private boolean bidiFlag;
/*      */   
/*      */ 
/*      */   private float red;
/*      */   
/*      */ 
/*      */   private float blue;
/*      */   
/*      */ 
/*      */   private float green;
/*      */   
/*      */ 
/*      */   private float alpha;
/*      */   
/*      */ 
/*      */   private int textColor;
/*      */   
/*      */ 
/*      */   private boolean randomStyle;
/*      */   
/*      */ 
/*      */   private boolean boldStyle;
/*      */   
/*      */ 
/*      */   private boolean italicStyle;
/*      */   
/*      */ 
/*      */   private boolean underlineStyle;
/*      */   
/*      */ 
/*      */   private boolean strikethroughStyle;
/*      */   
/*      */ 
/*      */   private static final String __OBFID = "CL_00000660";
/*      */   
/*      */ 
/*      */   public GameSettings gameSettings;
/*      */   
/*      */   public ResourceLocation locationFontTextureBase;
/*      */   
/*  114 */   public boolean enabled = true;
/*  115 */   public float scaleFactor = 1.0F;
/*      */   
/*      */   public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode)
/*      */   {
/*  119 */     this.gameSettings = gameSettingsIn;
/*  120 */     this.locationFontTextureBase = location;
/*  121 */     this.locationFontTexture = location;
/*  122 */     this.renderEngine = textureManagerIn;
/*  123 */     this.unicodeFlag = unicode;
/*  124 */     this.locationFontTexture = getHdFontLocation(this.locationFontTextureBase);
/*  125 */     textureManagerIn.bindTexture(this.locationFontTexture);
/*      */     
/*  127 */     for (int i = 0; i < 32; i++)
/*      */     {
/*  129 */       int j = (i >> 3 & 0x1) * 85;
/*  130 */       int k = (i >> 2 & 0x1) * 170 + j;
/*  131 */       int l = (i >> 1 & 0x1) * 170 + j;
/*  132 */       int i1 = (i >> 0 & 0x1) * 170 + j;
/*      */       
/*  134 */       if (i == 6)
/*      */       {
/*  136 */         k += 85;
/*      */       }
/*      */       
/*  139 */       if (gameSettingsIn.anaglyph)
/*      */       {
/*  141 */         int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
/*  142 */         int k1 = (k * 30 + l * 70) / 100;
/*  143 */         int l1 = (k * 30 + i1 * 70) / 100;
/*  144 */         k = j1;
/*  145 */         l = k1;
/*  146 */         i1 = l1;
/*      */       }
/*      */       
/*  149 */       if (i >= 16)
/*      */       {
/*  151 */         k /= 4;
/*  152 */         l /= 4;
/*  153 */         i1 /= 4;
/*      */       }
/*      */       
/*  156 */       this.colorCode[i] = ((k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF);
/*      */     }
/*      */     
/*  159 */     readGlyphSizes();
/*      */   }
/*      */   
/*      */   public void onResourceManagerReload(IResourceManager resourceManager)
/*      */   {
/*  164 */     this.locationFontTexture = getHdFontLocation(this.locationFontTextureBase);
/*      */     
/*  166 */     for (int i = 0; i < unicodePageLocations.length; i++)
/*      */     {
/*  168 */       unicodePageLocations[i] = null;
/*      */     }
/*      */     
/*  171 */     readFontTexture();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void readFontTexture()
/*      */   {
/*      */     try
/*      */     {
/*  180 */       bufferedimage = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
/*      */     }
/*      */     catch (IOException ioexception) {
/*      */       BufferedImage bufferedimage;
/*  184 */       throw new RuntimeException(ioexception);
/*      */     }
/*      */     BufferedImage bufferedimage;
/*  187 */     int i = bufferedimage.getWidth();
/*  188 */     int j = bufferedimage.getHeight();
/*  189 */     int k = i / 16;
/*  190 */     int l = j / 16;
/*  191 */     float f = i / 128.0F;
/*  192 */     this.scaleFactor = f;
/*  193 */     int[] aint = new int[i * j];
/*  194 */     bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
/*      */     
/*  196 */     for (int i1 = 0; i1 < 256; i1++)
/*      */     {
/*  198 */       int j1 = i1 % 16;
/*  199 */       int k1 = i1 / 16;
/*  200 */       int l1 = 0;
/*      */       
/*  202 */       for (l1 = k - 1; l1 >= 0; l1--)
/*      */       {
/*  204 */         int i2 = j1 * k + l1;
/*  205 */         boolean flag = true;
/*      */         
/*  207 */         for (int j2 = 0; (j2 < l) && (flag); j2++)
/*      */         {
/*  209 */           int k2 = (k1 * l + j2) * i;
/*  210 */           int l2 = aint[(i2 + k2)];
/*  211 */           int i3 = l2 >> 24 & 0xFF;
/*      */           
/*  213 */           if (i3 > 16)
/*      */           {
/*  215 */             flag = false;
/*      */           }
/*      */         }
/*      */         
/*  219 */         if (!flag) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  225 */       if (i1 == 65)
/*      */       {
/*  227 */         i1 = i1;
/*      */       }
/*      */       
/*  230 */       if (i1 == 32)
/*      */       {
/*  232 */         if (k <= 8)
/*      */         {
/*  234 */           l1 = (int)(2.0F * f);
/*      */         }
/*      */         else
/*      */         {
/*  238 */           l1 = (int)(1.5F * f);
/*      */         }
/*      */       }
/*      */       
/*  242 */       this.charWidth[i1] = ((l1 + 1) / f + 1.0F);
/*      */     }
/*      */     
/*  245 */     readCustomCharWidths();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private void readGlyphSizes()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_1
/*      */     //   2: invokestatic 133	net/minecraft/client/Minecraft:getMinecraft	()Lnet/minecraft/client/Minecraft;
/*      */     //   5: invokevirtual 137	net/minecraft/client/Minecraft:getResourceManager	()Lnet/minecraft/client/resources/IResourceManager;
/*      */     //   8: new 52	net/minecraft/util/ResourceLocation
/*      */     //   11: dup
/*      */     //   12: ldc -63
/*      */     //   14: invokespecial 196	net/minecraft/util/ResourceLocation:<init>	(Ljava/lang/String;)V
/*      */     //   17: invokeinterface 143 2 0
/*      */     //   22: invokeinterface 149 1 0
/*      */     //   27: astore_1
/*      */     //   28: aload_1
/*      */     //   29: aload_0
/*      */     //   30: getfield 70	net/minecraft/client/gui/FontRenderer:glyphWidth	[B
/*      */     //   33: invokevirtual 202	java/io/InputStream:read	([B)I
/*      */     //   36: pop
/*      */     //   37: goto +20 -> 57
/*      */     //   40: astore_2
/*      */     //   41: new 157	java/lang/RuntimeException
/*      */     //   44: dup
/*      */     //   45: aload_2
/*      */     //   46: invokespecial 160	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*      */     //   49: athrow
/*      */     //   50: astore_3
/*      */     //   51: aload_1
/*      */     //   52: invokestatic 210	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   55: aload_3
/*      */     //   56: athrow
/*      */     //   57: aload_1
/*      */     //   58: invokestatic 210	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   61: return
/*      */     // Line number table:
/*      */     //   Java source line #250	-> byte code offset #0
/*      */     //   Java source line #254	-> byte code offset #2
/*      */     //   Java source line #255	-> byte code offset #28
/*      */     //   Java source line #256	-> byte code offset #37
/*      */     //   Java source line #257	-> byte code offset #40
/*      */     //   Java source line #259	-> byte code offset #41
/*      */     //   Java source line #262	-> byte code offset #50
/*      */     //   Java source line #263	-> byte code offset #51
/*      */     //   Java source line #264	-> byte code offset #55
/*      */     //   Java source line #263	-> byte code offset #57
/*      */     //   Java source line #265	-> byte code offset #61
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	62	0	this	FontRenderer
/*      */     //   1	57	1	inputstream	InputStream
/*      */     //   40	6	2	ioexception	IOException
/*      */     //   50	6	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   2	37	40	java/io/IOException
/*      */     //   2	50	50	finally
/*      */   }
/*      */   
/*      */   private float func_181559_a(char p_181559_1_, boolean p_181559_2_)
/*      */   {
/*  269 */     if (p_181559_1_ == ' ')
/*      */     {
/*  271 */       return this.charWidth[p_181559_1_];
/*      */     }
/*      */     
/*      */ 
/*  275 */     int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(p_181559_1_);
/*  276 */     return (i != -1) && (!this.unicodeFlag) ? renderDefaultChar(i, p_181559_2_) : renderUnicodeChar(p_181559_1_, p_181559_2_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private float renderDefaultChar(int p_78266_1_, boolean p_78266_2_)
/*      */   {
/*  285 */     int i = p_78266_1_ % 16 * 8;
/*  286 */     int j = p_78266_1_ / 16 * 8;
/*  287 */     int k = p_78266_2_ ? 1 : 0;
/*  288 */     this.renderEngine.bindTexture(this.locationFontTexture);
/*  289 */     float f = this.charWidth[p_78266_1_];
/*  290 */     float f1 = 7.99F;
/*  291 */     GL11.glBegin(5);
/*  292 */     GL11.glTexCoord2f(i / 128.0F, j / 128.0F);
/*  293 */     GL11.glVertex3f(this.posX + k, this.posY, 0.0F);
/*  294 */     GL11.glTexCoord2f(i / 128.0F, (j + 7.99F) / 128.0F);
/*  295 */     GL11.glVertex3f(this.posX - k, this.posY + 7.99F, 0.0F);
/*  296 */     GL11.glTexCoord2f((i + f1 - 1.0F) / 128.0F, j / 128.0F);
/*  297 */     GL11.glVertex3f(this.posX + f1 - 1.0F + k, this.posY, 0.0F);
/*  298 */     GL11.glTexCoord2f((i + f1 - 1.0F) / 128.0F, (j + 7.99F) / 128.0F);
/*  299 */     GL11.glVertex3f(this.posX + f1 - 1.0F - k, this.posY + 7.99F, 0.0F);
/*  300 */     GL11.glEnd();
/*  301 */     return f;
/*      */   }
/*      */   
/*      */   private ResourceLocation getUnicodePageLocation(int p_111271_1_)
/*      */   {
/*  306 */     if (unicodePageLocations[p_111271_1_] == null)
/*      */     {
/*  308 */       unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] { Integer.valueOf(p_111271_1_) }));
/*  309 */       unicodePageLocations[p_111271_1_] = getHdFontLocation(unicodePageLocations[p_111271_1_]);
/*      */     }
/*      */     
/*  312 */     return unicodePageLocations[p_111271_1_];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void loadGlyphTexture(int p_78257_1_)
/*      */   {
/*  320 */     this.renderEngine.bindTexture(getUnicodePageLocation(p_78257_1_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_)
/*      */   {
/*  328 */     if (this.glyphWidth[p_78277_1_] == 0)
/*      */     {
/*  330 */       return 0.0F;
/*      */     }
/*      */     
/*      */ 
/*  334 */     int i = p_78277_1_ / 'Ā';
/*  335 */     loadGlyphTexture(i);
/*  336 */     int j = this.glyphWidth[p_78277_1_] >>> 4;
/*  337 */     int k = this.glyphWidth[p_78277_1_] & 0xF;
/*  338 */     float f = j;
/*  339 */     float f1 = k + 1;
/*  340 */     float f2 = p_78277_1_ % '\020' * 16 + f;
/*  341 */     float f3 = (p_78277_1_ & 0xFF) / '\020' * 16;
/*  342 */     float f4 = f1 - f - 0.02F;
/*  343 */     float f5 = p_78277_2_ ? 1.0F : 0.0F;
/*  344 */     GL11.glBegin(5);
/*  345 */     GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
/*  346 */     GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
/*  347 */     GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
/*  348 */     GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
/*  349 */     GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
/*  350 */     GL11.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
/*  351 */     GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
/*  352 */     GL11.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
/*  353 */     GL11.glEnd();
/*  354 */     return (f1 - f) / 2.0F + 1.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int drawStringWithShadow(String text, float x, float y, int color)
/*      */   {
/*  363 */     return drawString(text, x, y, color, true);
/*      */   }
/*      */   
/*      */   public int drawStringWithShadow(String text, double x, double y, int color)
/*      */   {
/*  368 */     return drawString(text, x, y, color, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int drawString(String text, double x, double y, int color, boolean shadow)
/*      */   {
/*  377 */     return !this.enabled ? 0 : drawString(text, (float)x, (float)y, color, shadow);
/*      */   }
/*      */   
/*      */   public int drawString(String text, double x, double y, int color)
/*      */   {
/*  382 */     return !this.enabled ? 0 : drawString(text, (float)x, (float)y, color, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int drawString(String text, float x, float y, int color, boolean dropShadow)
/*      */   {
/*  390 */     if (Polaris.instance.moduleManager.getModuleByName("Friends").isToggled()) {
/*  391 */       for (Friend friend : FriendManager.friendsList) {
/*  392 */         if (text.contains(friend.name)) {
/*  393 */           text = text.replace(friend.name, friend.alias);
/*      */         }
/*      */       }
/*      */     }
/*  397 */     if (Polaris.instance.moduleManager.getModuleByName("NoStrike").isToggled()) {
/*  398 */       if (text.contains("VeltPvP")) {
/*  399 */         text = text.replace("VeltPvP", "Polaris");
/*      */       }
/*  401 */       if (text.contains("veltpvp.com")) {
/*  402 */         text = text.replace("veltpvp.com", "polaris.rip");
/*      */       }
/*  404 */       if (text.contains("www.veltpvp.com")) {
/*  405 */         text = text.replace("veltpvp.com", "polaris.rip");
/*      */       }
/*  407 */       if (text.contains("veltpvp")) {
/*  408 */         text = text.replace("veltpvp", "polaris");
/*      */       }
/*      */       
/*  411 */       if (text.contains("Arcane")) {
/*  412 */         text = text.replace("Arcane", "Polaris");
/*      */       }
/*  414 */       if (text.contains("arcane.cc")) {
/*  415 */         text = text.replace("arcane.cc", "polaris.rip");
/*      */       }
/*  417 */       if (text.contains("www.arcane.cc")) {
/*  418 */         text = text.replace("arcane.cc", "polaris.rip");
/*      */       }
/*  420 */       if (text.contains("arcane")) {
/*  421 */         text = text.replace("arcane", "polaris");
/*      */       }
/*      */       
/*  424 */       if (text.contains("FaithfulMC")) {
/*  425 */         text = text.replace("FaithfulMC", "Polaris");
/*      */       }
/*  427 */       if (text.contains("@FaithfulNetwork")) {
/*  428 */         text = text.replace("@FaithfulNetwork", "@Polaris");
/*      */       }
/*  430 */       if (text.contains("faithfulmc.com")) {
/*  431 */         text = text.replace("faithfulmc.com", "polaris.rip");
/*      */       }
/*      */     }
/*      */     
/*  435 */     GlStateManager.enableAlpha();
/*  436 */     resetStyles();
/*      */     
/*      */     int i;
/*  439 */     if (dropShadow)
/*      */     {
/*  441 */       int i = renderString(text, x + 1.0F, y + 1.0F, color, true);
/*  442 */       i = Math.max(i, renderString(text, x, y, color, false));
/*      */     }
/*      */     else
/*      */     {
/*  446 */       i = renderString(text, x, y, color, false);
/*      */     }
/*      */     
/*  449 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String bidiReorder(String p_147647_1_)
/*      */   {
/*      */     try
/*      */     {
/*  459 */       Bidi bidi = new Bidi(new ArabicShaping(8).shape(p_147647_1_), 127);
/*  460 */       bidi.setReorderingMode(0);
/*  461 */       return bidi.writeReordered(2);
/*      */     }
/*      */     catch (ArabicShapingException var3) {}
/*      */     
/*  465 */     return p_147647_1_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resetStyles()
/*      */   {
/*  474 */     this.randomStyle = false;
/*  475 */     this.boldStyle = false;
/*  476 */     this.italicStyle = false;
/*  477 */     this.underlineStyle = false;
/*  478 */     this.strikethroughStyle = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_)
/*      */   {
/*  486 */     for (int i = 0; i < p_78255_1_.length(); i++)
/*      */     {
/*  488 */       char c0 = p_78255_1_.charAt(i);
/*      */       
/*  490 */       if ((c0 == '§') && (i + 1 < p_78255_1_.length()))
/*      */       {
/*  492 */         int i1 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i + 1));
/*      */         
/*  494 */         if (i1 < 16)
/*      */         {
/*  496 */           this.randomStyle = false;
/*  497 */           this.boldStyle = false;
/*  498 */           this.strikethroughStyle = false;
/*  499 */           this.underlineStyle = false;
/*  500 */           this.italicStyle = false;
/*      */           
/*  502 */           if ((i1 < 0) || (i1 > 15))
/*      */           {
/*  504 */             i1 = 15;
/*      */           }
/*      */           
/*  507 */           if (p_78255_2_)
/*      */           {
/*  509 */             i1 += 16;
/*      */           }
/*      */           
/*  512 */           int j1 = this.colorCode[i1];
/*  513 */           this.textColor = j1;
/*  514 */           GlStateManager.color((j1 >> 16) / 255.0F, (j1 >> 8 & 0xFF) / 255.0F, (j1 & 0xFF) / 255.0F, this.alpha);
/*      */         }
/*  516 */         else if (i1 == 16)
/*      */         {
/*  518 */           this.randomStyle = true;
/*      */         }
/*  520 */         else if (i1 == 17)
/*      */         {
/*  522 */           this.boldStyle = true;
/*      */         }
/*  524 */         else if (i1 == 18)
/*      */         {
/*  526 */           this.strikethroughStyle = true;
/*      */         }
/*  528 */         else if (i1 == 19)
/*      */         {
/*  530 */           this.underlineStyle = true;
/*      */         }
/*  532 */         else if (i1 == 20)
/*      */         {
/*  534 */           this.italicStyle = true;
/*      */         }
/*  536 */         else if (i1 == 21)
/*      */         {
/*  538 */           this.randomStyle = false;
/*  539 */           this.boldStyle = false;
/*  540 */           this.strikethroughStyle = false;
/*  541 */           this.underlineStyle = false;
/*  542 */           this.italicStyle = false;
/*  543 */           GlStateManager.color(this.red, this.blue, this.green, this.alpha);
/*      */         }
/*      */         
/*  546 */         i++;
/*      */       }
/*      */       else
/*      */       {
/*  550 */         int j = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(c0);
/*      */         
/*  552 */         if ((this.randomStyle) && (j != -1))
/*      */         {
/*  554 */           int k = getCharWidth(c0);
/*      */           
/*      */           char c1;
/*      */           do
/*      */           {
/*  559 */             j = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".length());
/*  560 */             c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".charAt(j);
/*      */           }
/*  562 */           while (k != getCharWidth(c1));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  568 */           c0 = c1;
/*      */         }
/*      */         
/*  571 */         float f1 = this.unicodeFlag ? 0.5F : 1.0F / this.scaleFactor;
/*  572 */         boolean flag = ((c0 == 0) || (j == -1) || (this.unicodeFlag)) && (p_78255_2_);
/*      */         
/*  574 */         if (flag)
/*      */         {
/*  576 */           this.posX -= f1;
/*  577 */           this.posY -= f1;
/*      */         }
/*      */         
/*  580 */         float f = func_181559_a(c0, this.italicStyle);
/*      */         
/*  582 */         if (flag)
/*      */         {
/*  584 */           this.posX += f1;
/*  585 */           this.posY += f1;
/*      */         }
/*      */         
/*  588 */         if (this.boldStyle)
/*      */         {
/*  590 */           this.posX += f1;
/*      */           
/*  592 */           if (flag)
/*      */           {
/*  594 */             this.posX -= f1;
/*  595 */             this.posY -= f1;
/*      */           }
/*      */           
/*  598 */           func_181559_a(c0, this.italicStyle);
/*  599 */           this.posX -= f1;
/*      */           
/*  601 */           if (flag)
/*      */           {
/*  603 */             this.posX += f1;
/*  604 */             this.posY += f1;
/*      */           }
/*      */           
/*  607 */           f += f1;
/*      */         }
/*      */         
/*  610 */         if (this.strikethroughStyle)
/*      */         {
/*  612 */           Tessellator tessellator = Tessellator.getInstance();
/*  613 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  614 */           GlStateManager.disableTexture2D();
/*  615 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION);
/*  616 */           worldrenderer.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0D).endVertex();
/*  617 */           worldrenderer.pos(this.posX + f, this.posY + this.FONT_HEIGHT / 2, 0.0D).endVertex();
/*  618 */           worldrenderer.pos(this.posX + f, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
/*  619 */           worldrenderer.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
/*  620 */           tessellator.draw();
/*  621 */           GlStateManager.enableTexture2D();
/*      */         }
/*      */         
/*  624 */         if (this.underlineStyle)
/*      */         {
/*  626 */           Tessellator tessellator1 = Tessellator.getInstance();
/*  627 */           WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
/*  628 */           GlStateManager.disableTexture2D();
/*  629 */           worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
/*  630 */           int l = this.underlineStyle ? -1 : 0;
/*  631 */           worldrenderer1.pos(this.posX + l, this.posY + this.FONT_HEIGHT, 0.0D).endVertex();
/*  632 */           worldrenderer1.pos(this.posX + f, this.posY + this.FONT_HEIGHT, 0.0D).endVertex();
/*  633 */           worldrenderer1.pos(this.posX + f, this.posY + this.FONT_HEIGHT - 1.0F, 0.0D).endVertex();
/*  634 */           worldrenderer1.pos(this.posX + l, this.posY + this.FONT_HEIGHT - 1.0F, 0.0D).endVertex();
/*  635 */           tessellator1.draw();
/*  636 */           GlStateManager.enableTexture2D();
/*      */         }
/*      */         
/*  639 */         this.posX += f;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int renderStringAligned(String text, int x, int y, int p_78274_4_, int color, boolean dropShadow)
/*      */   {
/*  649 */     if (this.bidiFlag)
/*      */     {
/*  651 */       int i = getStringWidth(bidiReorder(text));
/*  652 */       x = x + p_78274_4_ - i;
/*      */     }
/*      */     
/*  655 */     return renderString(text, x, y, color, dropShadow);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int renderString(String text, float x, float y, int color, boolean dropShadow)
/*      */   {
/*  663 */     if (text == null)
/*      */     {
/*  665 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  669 */     if (this.bidiFlag)
/*      */     {
/*  671 */       text = bidiReorder(text);
/*      */     }
/*      */     
/*  674 */     if ((color & 0xFC000000) == 0)
/*      */     {
/*  676 */       color |= 0xFF000000;
/*      */     }
/*      */     
/*  679 */     if (dropShadow)
/*      */     {
/*  681 */       color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
/*      */     }
/*      */     
/*  684 */     this.red = ((color >> 16 & 0xFF) / 255.0F);
/*  685 */     this.blue = ((color >> 8 & 0xFF) / 255.0F);
/*  686 */     this.green = ((color & 0xFF) / 255.0F);
/*  687 */     this.alpha = ((color >> 24 & 0xFF) / 255.0F);
/*  688 */     GlStateManager.color(this.red, this.blue, this.green, this.alpha);
/*  689 */     this.posX = x;
/*  690 */     this.posY = y;
/*  691 */     renderStringAtPos(text, dropShadow);
/*  692 */     return (int)this.posX;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getStringWidth(String text)
/*      */   {
/*  701 */     if (text == null)
/*      */     {
/*  703 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  707 */     float f = 0.0F;
/*  708 */     boolean flag = false;
/*      */     
/*  710 */     for (int i = 0; i < text.length(); i++)
/*      */     {
/*  712 */       char c0 = text.charAt(i);
/*  713 */       float f1 = getCharWidthFloat(c0);
/*      */       
/*  715 */       if ((f1 < 0.0F) && (i < text.length() - 1))
/*      */       {
/*  717 */         i++;
/*  718 */         c0 = text.charAt(i);
/*      */         
/*  720 */         if ((c0 != 'l') && (c0 != 'L'))
/*      */         {
/*  722 */           if ((c0 == 'r') || (c0 == 'R'))
/*      */           {
/*  724 */             flag = false;
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/*  729 */           flag = true;
/*      */         }
/*      */         
/*  732 */         f1 = 0.0F;
/*      */       }
/*      */       
/*  735 */       f += f1;
/*      */       
/*  737 */       if ((flag) && (f1 > 0.0F))
/*      */       {
/*  739 */         f += 1.0F;
/*      */       }
/*      */     }
/*      */     
/*  743 */     return (int)f;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCharWidth(char character)
/*      */   {
/*  752 */     return Math.round(getCharWidthFloat(character));
/*      */   }
/*      */   
/*      */   private float getCharWidthFloat(char p_getCharWidthFloat_1_)
/*      */   {
/*  757 */     if (p_getCharWidthFloat_1_ == '§')
/*      */     {
/*  759 */       return -1.0F;
/*      */     }
/*  761 */     if (p_getCharWidthFloat_1_ == ' ')
/*      */     {
/*  763 */       return this.charWidth[32];
/*      */     }
/*      */     
/*      */ 
/*  767 */     int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(p_getCharWidthFloat_1_);
/*      */     
/*  769 */     if ((p_getCharWidthFloat_1_ > 0) && (i != -1) && (!this.unicodeFlag))
/*      */     {
/*  771 */       return this.charWidth[i];
/*      */     }
/*  773 */     if (this.glyphWidth[p_getCharWidthFloat_1_] != 0)
/*      */     {
/*  775 */       int j = this.glyphWidth[p_getCharWidthFloat_1_] >>> 4;
/*  776 */       int k = this.glyphWidth[p_getCharWidthFloat_1_] & 0xF;
/*      */       
/*  778 */       if (k > 7)
/*      */       {
/*  780 */         k = 15;
/*  781 */         j = 0;
/*      */       }
/*      */       
/*  784 */       k++;
/*  785 */       return (k - j) / 2 + 1;
/*      */     }
/*      */     
/*      */ 
/*  789 */     return 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String trimStringToWidth(String text, int width)
/*      */   {
/*  799 */     return trimStringToWidth(text, width, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String trimStringToWidth(String text, int width, boolean reverse)
/*      */   {
/*  807 */     StringBuilder stringbuilder = new StringBuilder();
/*  808 */     float f = 0.0F;
/*  809 */     int i = reverse ? text.length() - 1 : 0;
/*  810 */     int j = reverse ? -1 : 1;
/*  811 */     boolean flag = false;
/*  812 */     boolean flag1 = false;
/*      */     
/*  814 */     for (int k = i; (k >= 0) && (k < text.length()) && (f < width); k += j)
/*      */     {
/*  816 */       char c0 = text.charAt(k);
/*  817 */       float f1 = getCharWidthFloat(c0);
/*      */       
/*  819 */       if (flag)
/*      */       {
/*  821 */         flag = false;
/*      */         
/*  823 */         if ((c0 != 'l') && (c0 != 'L'))
/*      */         {
/*  825 */           if ((c0 == 'r') || (c0 == 'R'))
/*      */           {
/*  827 */             flag1 = false;
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/*  832 */           flag1 = true;
/*      */         }
/*      */       }
/*  835 */       else if (f1 < 0.0F)
/*      */       {
/*  837 */         flag = true;
/*      */       }
/*      */       else
/*      */       {
/*  841 */         f += f1;
/*      */         
/*  843 */         if (flag1)
/*      */         {
/*  845 */           f += 1.0F;
/*      */         }
/*      */       }
/*      */       
/*  849 */       if (f > width) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  854 */       if (reverse)
/*      */       {
/*  856 */         stringbuilder.insert(0, c0);
/*      */       }
/*      */       else
/*      */       {
/*  860 */         stringbuilder.append(c0);
/*      */       }
/*      */     }
/*      */     
/*  864 */     return stringbuilder.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String trimStringNewline(String text)
/*      */   {
/*  872 */     while ((text != null) && (text.endsWith("\n")))
/*      */     {
/*  874 */       text = text.substring(0, text.length() - 1);
/*      */     }
/*      */     
/*  877 */     return text;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor)
/*      */   {
/*  885 */     resetStyles();
/*  886 */     this.textColor = textColor;
/*  887 */     str = trimStringNewline(str);
/*  888 */     renderSplitString(str, x, y, wrapWidth, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow)
/*      */   {
/*  897 */     for (Object s : listFormattedStringToWidth(str, wrapWidth))
/*      */     {
/*  899 */       renderStringAligned((String)s, x, y, wrapWidth, this.textColor, addShadow);
/*  900 */       y += this.FONT_HEIGHT;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int splitStringWidth(String p_78267_1_, int p_78267_2_)
/*      */   {
/*  909 */     return this.FONT_HEIGHT * listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUnicodeFlag(boolean unicodeFlagIn)
/*      */   {
/*  918 */     this.unicodeFlag = unicodeFlagIn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUnicodeFlag()
/*      */   {
/*  927 */     return this.unicodeFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBidiFlag(boolean bidiFlagIn)
/*      */   {
/*  935 */     this.bidiFlag = bidiFlagIn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List listFormattedStringToWidth(String str, int wrapWidth)
/*      */   {
/*  943 */     return Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected String wrapFormattedStringToWidth(String str, int wrapWidth)
/*      */   {
/*  950 */     int var3 = sizeStringToWidth(str, wrapWidth);
/*      */     
/*  952 */     if (str.length() <= var3) {
/*  953 */       return str;
/*      */     }
/*  955 */     String var4 = str.substring(0, var3);
/*  956 */     char var5 = str.charAt(var3);
/*  957 */     boolean var6 = (var5 == ' ') || (var5 == '\n');
/*  958 */     String var7 = getFormatFromString(var4) + str.substring(var3 + (var6 ? 1 : 0));
/*  959 */     return var4 + "\n" + wrapFormattedStringToWidth(var7, wrapWidth);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int sizeStringToWidth(String str, int wrapWidth)
/*      */   {
/*  968 */     int i = str.length();
/*  969 */     float f = 0.0F;
/*  970 */     int j = 0;
/*  971 */     int k = -1;
/*      */     
/*  973 */     for (boolean flag = false; j < i; j++)
/*      */     {
/*  975 */       char c0 = str.charAt(j);
/*      */       
/*  977 */       switch (c0)
/*      */       {
/*      */       case '\n': 
/*  980 */         j--;
/*  981 */         break;
/*      */       
/*      */       case ' ': 
/*  984 */         k = j;
/*      */       
/*      */       default: 
/*  987 */         f += getCharWidthFloat(c0);
/*      */         
/*  989 */         if (flag)
/*      */         {
/*  991 */           f += 1.0F;
/*      */         }
/*      */         
/*  994 */         break;
/*      */       
/*      */       case '§': 
/*  997 */         if (j < i - 1)
/*      */         {
/*  999 */           j++;
/* 1000 */           char c1 = str.charAt(j);
/*      */           
/* 1002 */           if ((c1 != 'l') && (c1 != 'L'))
/*      */           {
/* 1004 */             if ((c1 == 'r') || (c1 == 'R') || (isFormatColor(c1)))
/*      */             {
/* 1006 */               flag = false;
/*      */             }
/*      */             
/*      */           }
/*      */           else {
/* 1011 */             flag = true;
/*      */           }
/*      */         }
/*      */         break;
/*      */       }
/* 1016 */       if (c0 == '\n')
/*      */       {
/* 1018 */         j++;
/* 1019 */         k = j;
/*      */       }
/*      */       else
/*      */       {
/* 1023 */         if (f > wrapWidth) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1029 */     return (j != i) && (k != -1) && (k < j) ? k : j;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isFormatColor(char colorChar)
/*      */   {
/* 1037 */     return ((colorChar >= '0') && (colorChar <= '9')) || ((colorChar >= 'a') && (colorChar <= 'f')) || ((colorChar >= 'A') && (colorChar <= 'F'));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isFormatSpecial(char formatChar)
/*      */   {
/* 1045 */     return ((formatChar >= 'k') && (formatChar <= 'o')) || ((formatChar >= 'K') && (formatChar <= 'O')) || (formatChar == 'r') || (formatChar == 'R');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getFormatFromString(String text)
/*      */   {
/* 1053 */     String s = "";
/* 1054 */     int i = -1;
/* 1055 */     int j = text.length();
/*      */     
/* 1057 */     while ((i = text.indexOf('§', i + 1)) != -1)
/*      */     {
/* 1059 */       if (i < j - 1)
/*      */       {
/* 1061 */         char c0 = text.charAt(i + 1);
/*      */         
/* 1063 */         if (isFormatColor(c0))
/*      */         {
/* 1065 */           s = "§" + c0;
/*      */         }
/* 1067 */         else if (isFormatSpecial(c0))
/*      */         {
/* 1069 */           s = s + "§" + c0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1074 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getBidiFlag()
/*      */   {
/* 1082 */     return this.bidiFlag;
/*      */   }
/*      */   
/*      */   public int getColorCode(char character)
/*      */   {
/* 1087 */     int i = "0123456789abcdef".indexOf(character);
/* 1088 */     return (i >= 0) && (i < this.colorCode.length) ? this.colorCode[i] : 16777215;
/*      */   }
/*      */   
/*      */   private void readCustomCharWidths()
/*      */   {
/* 1093 */     String s = this.locationFontTexture.getResourcePath();
/* 1094 */     String s1 = ".png";
/*      */     
/* 1096 */     if (s.endsWith(s1))
/*      */     {
/* 1098 */       String s2 = s.substring(0, s.length() - s1.length()) + ".properties";
/*      */       
/*      */       try
/*      */       {
/* 1102 */         ResourceLocation resourcelocation = new ResourceLocation(this.locationFontTexture.getResourceDomain(), s2);
/* 1103 */         InputStream inputstream = Config.getResourceStream(Config.getResourceManager(), resourcelocation);
/*      */         
/* 1105 */         if (inputstream == null)
/*      */         {
/* 1107 */           return;
/*      */         }
/*      */         
/* 1110 */         Config.log("Loading " + s2);
/* 1111 */         Properties properties = new Properties();
/* 1112 */         properties.load(inputstream);
/*      */         
/* 1114 */         for (Object s30 : properties.keySet())
/*      */         {
/* 1116 */           String s3 = (String)s30;
/* 1117 */           String s4 = "width.";
/*      */           
/* 1119 */           if (s3.startsWith(s4))
/*      */           {
/* 1121 */             String s5 = s3.substring(s4.length());
/* 1122 */             int i = Config.parseInt(s5, -1);
/*      */             
/* 1124 */             if ((i >= 0) && (i < this.charWidth.length))
/*      */             {
/* 1126 */               String s6 = properties.getProperty(s3);
/* 1127 */               float f = Config.parseFloat(s6, -1.0F);
/*      */               
/* 1129 */               if (f >= 0.0F)
/*      */               {
/* 1131 */                 this.charWidth[i] = f;
/*      */               }
/*      */               
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*      */       catch (FileNotFoundException localFileNotFoundException) {}catch (IOException ioexception)
/*      */       {
/* 1143 */         ioexception.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public int getHeight() {
/* 1149 */     return this.FONT_HEIGHT;
/*      */   }
/*      */   
/*      */   private static ResourceLocation getHdFontLocation(ResourceLocation p_getHdFontLocation_0_)
/*      */   {
/* 1154 */     if (!Config.isCustomFonts())
/*      */     {
/* 1156 */       return p_getHdFontLocation_0_;
/*      */     }
/* 1158 */     if (p_getHdFontLocation_0_ == null)
/*      */     {
/* 1160 */       return p_getHdFontLocation_0_;
/*      */     }
/*      */     
/*      */ 
/* 1164 */     String s = p_getHdFontLocation_0_.getResourcePath();
/* 1165 */     String s1 = "textures/";
/* 1166 */     String s2 = "mcpatcher/";
/*      */     
/* 1168 */     if (!s.startsWith(s1))
/*      */     {
/* 1170 */       return p_getHdFontLocation_0_;
/*      */     }
/*      */     
/*      */ 
/* 1174 */     s = s.substring(s1.length());
/* 1175 */     s = s2 + s;
/* 1176 */     ResourceLocation resourcelocation = new ResourceLocation(p_getHdFontLocation_0_.getResourceDomain(), s);
/* 1177 */     return Config.hasResource(Config.getResourceManager(), resourcelocation) ? resourcelocation : p_getHdFontLocation_0_;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\FontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */