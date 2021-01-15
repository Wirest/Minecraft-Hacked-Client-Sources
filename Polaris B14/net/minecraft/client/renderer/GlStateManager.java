/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.nio.FloatBuffer;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class GlStateManager
/*     */ {
/*   8 */   private static AlphaState alphaState = new AlphaState(null);
/*   9 */   private static BooleanState lightingState = new BooleanState(2896);
/*  10 */   private static BooleanState[] lightState = new BooleanState[8];
/*  11 */   private static ColorMaterialState colorMaterialState = new ColorMaterialState(null);
/*  12 */   private static BlendState blendState = new BlendState(null);
/*  13 */   private static DepthState depthState = new DepthState(null);
/*  14 */   private static FogState fogState = new FogState(null);
/*  15 */   private static CullState cullState = new CullState(null);
/*  16 */   private static PolygonOffsetState polygonOffsetState = new PolygonOffsetState(null);
/*  17 */   private static ColorLogicState colorLogicState = new ColorLogicState(null);
/*  18 */   private static TexGenState texGenState = new TexGenState(null);
/*  19 */   private static ClearState clearState = new ClearState(null);
/*  20 */   private static StencilState stencilState = new StencilState(null);
/*  21 */   private static BooleanState normalizeState = new BooleanState(2977);
/*  22 */   private static int activeTextureUnit = 0;
/*  23 */   private static TextureState[] textureState = new TextureState[8];
/*  24 */   private static int activeShadeModel = 7425;
/*  25 */   private static BooleanState rescaleNormalState = new BooleanState(32826);
/*  26 */   private static ColorMask colorMaskState = new ColorMask(null);
/*  27 */   private static Color colorState = new Color();
/*     */   private static final String __OBFID = "CL_00002558";
/*  29 */   public static boolean clearEnabled = true;
/*     */   
/*     */   public static void pushAttrib()
/*     */   {
/*  33 */     GL11.glPushAttrib(8256);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void disableAlpha()
/*     */   {
/*  43 */     alphaState.field_179208_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void enableAlpha()
/*     */   {
/*  48 */     alphaState.field_179208_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void alphaFunc(int func, float ref)
/*     */   {
/*  53 */     if ((func != alphaState.func) || (ref != alphaState.ref))
/*     */     {
/*  55 */       alphaState.func = func;
/*  56 */       alphaState.ref = ref;
/*  57 */       GL11.glAlphaFunc(func, ref);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableLighting()
/*     */   {
/*  63 */     lightingState.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableLighting()
/*     */   {
/*  68 */     lightingState.setDisabled();
/*     */   }
/*     */   
/*     */   public static void enableLight(int light)
/*     */   {
/*  73 */     lightState[light].setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableLight(int light)
/*     */   {
/*  78 */     lightState[light].setDisabled();
/*     */   }
/*     */   
/*     */   public static void enableColorMaterial()
/*     */   {
/*  83 */     colorMaterialState.field_179191_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableColorMaterial()
/*     */   {
/*  88 */     colorMaterialState.field_179191_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void colorMaterial(int face, int mode)
/*     */   {
/*  93 */     if ((face != colorMaterialState.field_179189_b) || (mode != colorMaterialState.field_179190_c))
/*     */     {
/*  95 */       colorMaterialState.field_179189_b = face;
/*  96 */       colorMaterialState.field_179190_c = mode;
/*  97 */       GL11.glColorMaterial(face, mode);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void disableDepth()
/*     */   {
/* 103 */     depthState.depthTest.setDisabled();
/*     */   }
/*     */   
/*     */   public static void enableDepth()
/*     */   {
/* 108 */     depthState.depthTest.setEnabled();
/*     */   }
/*     */   
/*     */   public static void depthFunc(int depthFunc)
/*     */   {
/* 113 */     if (depthFunc != depthState.depthFunc)
/*     */     {
/* 115 */       depthState.depthFunc = depthFunc;
/* 116 */       GL11.glDepthFunc(depthFunc);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void depthMask(boolean flagIn)
/*     */   {
/* 122 */     if (flagIn != depthState.maskEnabled)
/*     */     {
/* 124 */       depthState.maskEnabled = flagIn;
/* 125 */       GL11.glDepthMask(flagIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void disableBlend()
/*     */   {
/* 131 */     blendState.field_179213_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void enableBlend()
/*     */   {
/* 136 */     blendState.field_179213_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void blendFunc(int srcFactor, int dstFactor)
/*     */   {
/* 141 */     if ((srcFactor != blendState.srcFactor) || (dstFactor != blendState.dstFactor))
/*     */     {
/* 143 */       blendState.srcFactor = srcFactor;
/* 144 */       blendState.dstFactor = dstFactor;
/* 145 */       GL11.glBlendFunc(srcFactor, dstFactor);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha)
/*     */   {
/* 151 */     if ((srcFactor != blendState.srcFactor) || (dstFactor != blendState.dstFactor) || (srcFactorAlpha != blendState.srcFactorAlpha) || (dstFactorAlpha != blendState.dstFactorAlpha))
/*     */     {
/* 153 */       blendState.srcFactor = srcFactor;
/* 154 */       blendState.dstFactor = dstFactor;
/* 155 */       blendState.srcFactorAlpha = srcFactorAlpha;
/* 156 */       blendState.dstFactorAlpha = dstFactorAlpha;
/* 157 */       OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableFog()
/*     */   {
/* 163 */     fogState.field_179049_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableFog()
/*     */   {
/* 168 */     fogState.field_179049_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void setFog(int param)
/*     */   {
/* 173 */     if (param != fogState.field_179047_b)
/*     */     {
/* 175 */       fogState.field_179047_b = param;
/* 176 */       GL11.glFogi(2917, param);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setFogDensity(float param)
/*     */   {
/* 182 */     if (param != fogState.field_179048_c)
/*     */     {
/* 184 */       fogState.field_179048_c = param;
/* 185 */       GL11.glFogf(2914, param);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setFogStart(float param)
/*     */   {
/* 191 */     if (param != fogState.field_179045_d)
/*     */     {
/* 193 */       fogState.field_179045_d = param;
/* 194 */       GL11.glFogf(2915, param);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setFogEnd(float param)
/*     */   {
/* 200 */     if (param != fogState.field_179046_e)
/*     */     {
/* 202 */       fogState.field_179046_e = param;
/* 203 */       GL11.glFogf(2916, param);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableCull()
/*     */   {
/* 209 */     cullState.field_179054_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableCull()
/*     */   {
/* 214 */     cullState.field_179054_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void cullFace(int mode)
/*     */   {
/* 219 */     if (mode != cullState.field_179053_b)
/*     */     {
/* 221 */       cullState.field_179053_b = mode;
/* 222 */       GL11.glCullFace(mode);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enablePolygonOffset()
/*     */   {
/* 228 */     polygonOffsetState.field_179044_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disablePolygonOffset()
/*     */   {
/* 233 */     polygonOffsetState.field_179044_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void doPolygonOffset(float factor, float units)
/*     */   {
/* 238 */     if ((factor != polygonOffsetState.field_179043_c) || (units != polygonOffsetState.field_179041_d))
/*     */     {
/* 240 */       polygonOffsetState.field_179043_c = factor;
/* 241 */       polygonOffsetState.field_179041_d = units;
/* 242 */       GL11.glPolygonOffset(factor, units);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableColorLogic()
/*     */   {
/* 248 */     colorLogicState.field_179197_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableColorLogic()
/*     */   {
/* 253 */     colorLogicState.field_179197_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void colorLogicOp(int opcode)
/*     */   {
/* 258 */     if (opcode != colorLogicState.field_179196_b)
/*     */     {
/* 260 */       colorLogicState.field_179196_b = opcode;
/* 261 */       GL11.glLogicOp(opcode);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableTexGenCoord(TexGen p_179087_0_)
/*     */   {
/* 267 */     texGenCoord(p_179087_0_).field_179067_a.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableTexGenCoord(TexGen p_179100_0_)
/*     */   {
/* 272 */     texGenCoord(p_179100_0_).field_179067_a.setDisabled();
/*     */   }
/*     */   
/*     */   public static void texGen(TexGen p_179149_0_, int p_179149_1_)
/*     */   {
/* 277 */     TexGenCoord glstatemanager$texgencoord = texGenCoord(p_179149_0_);
/*     */     
/* 279 */     if (p_179149_1_ != glstatemanager$texgencoord.field_179066_c)
/*     */     {
/* 281 */       glstatemanager$texgencoord.field_179066_c = p_179149_1_;
/* 282 */       GL11.glTexGeni(glstatemanager$texgencoord.field_179065_b, 9472, p_179149_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void func_179105_a(TexGen p_179105_0_, int pname, FloatBuffer params)
/*     */   {
/* 288 */     GL11.glTexGen(texGenCoord(p_179105_0_).field_179065_b, pname, params);
/*     */   }
/*     */   
/*     */   private static TexGenCoord texGenCoord(TexGen p_179125_0_)
/*     */   {
/* 293 */     switch (GlStateManager.1.field_179175_a[p_179125_0_.ordinal()])
/*     */     {
/*     */     case 1: 
/* 296 */       return texGenState.field_179064_a;
/*     */     
/*     */     case 2: 
/* 299 */       return texGenState.field_179062_b;
/*     */     
/*     */     case 3: 
/* 302 */       return texGenState.field_179063_c;
/*     */     
/*     */     case 4: 
/* 305 */       return texGenState.field_179061_d;
/*     */     }
/*     */     
/* 308 */     return texGenState.field_179064_a;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void setActiveTexture(int texture)
/*     */   {
/* 314 */     if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit)
/*     */     {
/* 316 */       activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
/* 317 */       OpenGlHelper.setActiveTexture(texture);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableTexture2D()
/*     */   {
/* 323 */     textureState[activeTextureUnit].texture2DState.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableTexture2D()
/*     */   {
/* 328 */     textureState[activeTextureUnit].texture2DState.setDisabled();
/*     */   }
/*     */   
/*     */   public static int generateTexture()
/*     */   {
/* 333 */     return GL11.glGenTextures();
/*     */   }
/*     */   
/*     */   public static void deleteTexture(int texture)
/*     */   {
/* 338 */     GL11.glDeleteTextures(texture);
/*     */     TextureState[] arrayOfTextureState;
/* 340 */     int j = (arrayOfTextureState = textureState).length; for (int i = 0; i < j; i++) { TextureState glstatemanager$texturestate = arrayOfTextureState[i];
/*     */       
/* 342 */       if (glstatemanager$texturestate.textureName == texture)
/*     */       {
/* 344 */         glstatemanager$texturestate.textureName = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bindTexture(int texture)
/*     */   {
/* 351 */     if (texture != textureState[activeTextureUnit].textureName)
/*     */     {
/* 353 */       textureState[activeTextureUnit].textureName = texture;
/* 354 */       GL11.glBindTexture(3553, texture);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bindCurrentTexture()
/*     */   {
/* 360 */     GL11.glBindTexture(3553, textureState[activeTextureUnit].textureName);
/*     */   }
/*     */   
/*     */   public static void enableNormalize()
/*     */   {
/* 365 */     normalizeState.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableNormalize()
/*     */   {
/* 370 */     normalizeState.setDisabled();
/*     */   }
/*     */   
/*     */   public static void shadeModel(int mode)
/*     */   {
/* 375 */     if (mode != activeShadeModel)
/*     */     {
/* 377 */       activeShadeModel = mode;
/* 378 */       GL11.glShadeModel(mode);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableRescaleNormal()
/*     */   {
/* 384 */     rescaleNormalState.setEnabled();
/*     */   }
/*     */   
/*     */   public static void disableRescaleNormal()
/*     */   {
/* 389 */     rescaleNormalState.setDisabled();
/*     */   }
/*     */   
/*     */   public static void viewport(int x, int y, int width, int height)
/*     */   {
/* 394 */     GL11.glViewport(x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha)
/*     */   {
/* 399 */     if ((red != colorMaskState.red) || (green != colorMaskState.green) || (blue != colorMaskState.blue) || (alpha != colorMaskState.alpha))
/*     */     {
/* 401 */       colorMaskState.red = red;
/* 402 */       colorMaskState.green = green;
/* 403 */       colorMaskState.blue = blue;
/* 404 */       colorMaskState.alpha = alpha;
/* 405 */       GL11.glColorMask(red, green, blue, alpha);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clearDepth(double depth)
/*     */   {
/* 411 */     if (depth != clearState.field_179205_a)
/*     */     {
/* 413 */       clearState.field_179205_a = depth;
/* 414 */       GL11.glClearDepth(depth);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clearColor(float red, float green, float blue, float alpha)
/*     */   {
/* 420 */     if ((red != clearState.field_179203_b.red) || (green != clearState.field_179203_b.green) || (blue != clearState.field_179203_b.blue) || (alpha != clearState.field_179203_b.alpha))
/*     */     {
/* 422 */       clearState.field_179203_b.red = red;
/* 423 */       clearState.field_179203_b.green = green;
/* 424 */       clearState.field_179203_b.blue = blue;
/* 425 */       clearState.field_179203_b.alpha = alpha;
/* 426 */       GL11.glClearColor(red, green, blue, alpha);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clear(int mask)
/*     */   {
/* 432 */     if (clearEnabled)
/*     */     {
/* 434 */       GL11.glClear(mask);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void matrixMode(int mode)
/*     */   {
/* 440 */     GL11.glMatrixMode(mode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void getFloat(int pname, FloatBuffer params)
/*     */   {
/* 460 */     GL11.glGetFloat(pname, params);
/*     */   }
/*     */   
/*     */   public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar)
/*     */   {
/* 465 */     GL11.glOrtho(left, right, bottom, top, zNear, zFar);
/*     */   }
/*     */   
/*     */   public static void rotate(float angle, float x, float y, float z)
/*     */   {
/* 470 */     GL11.glRotatef(angle, x, y, z);
/*     */   }
/*     */   
/*     */   public static void scale(float x, float y, float z)
/*     */   {
/* 475 */     GL11.glScalef(x, y, z);
/*     */   }
/*     */   
/*     */   public static void scale(double x, double y, double z)
/*     */   {
/* 480 */     GL11.glScaled(x, y, z);
/*     */   }
/*     */   
/*     */   public static void translate(float x, float y, float z)
/*     */   {
/* 485 */     GL11.glTranslatef(x, y, z);
/*     */   }
/*     */   
/*     */   public static void translate(double x, double y, double z)
/*     */   {
/* 490 */     GL11.glTranslated(x, y, z);
/*     */   }
/*     */   
/*     */   public static void multMatrix(FloatBuffer matrix)
/*     */   {
/* 495 */     GL11.glMultMatrix(matrix);
/*     */   }
/*     */   
/*     */   public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha)
/*     */   {
/* 500 */     if ((colorRed != colorState.red) || (colorGreen != colorState.green) || (colorBlue != colorState.blue) || (colorAlpha != colorState.alpha))
/*     */     {
/* 502 */       colorState.red = colorRed;
/* 503 */       colorState.green = colorGreen;
/* 504 */       colorState.blue = colorBlue;
/* 505 */       colorState.alpha = colorAlpha;
/* 506 */       GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void color(float colorRed, float colorGreen, float colorBlue)
/*     */   {
/* 512 */     color(colorRed, colorGreen, colorBlue, 1.0F);
/*     */   }
/*     */   
/*     */   public static void resetColor()
/*     */   {
/* 517 */     colorState.red = (colorState.green = colorState.blue = colorState.alpha = -1.0F);
/*     */   }
/*     */   
/*     */   public static void callList(int list)
/*     */   {
/* 522 */     GL11.glCallList(list);
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 527 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 529 */       lightState[i] = new BooleanState(16384 + i);
/*     */     }
/*     */     
/* 532 */     for (int j = 0; j < 8; j++)
/*     */     {
/* 534 */       textureState[j] = new TextureState(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void popAttrib() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void loadIdentity() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void pushMatrix() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void popMatrix() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class AlphaState
/*     */   {
/*     */     public GlStateManager.BooleanState field_179208_a;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int func;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public float ref;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private static final String __OBFID = "CL_00002556";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private AlphaState()
/*     */     {
/* 592 */       this.field_179208_a = new GlStateManager.BooleanState(3008);
/* 593 */       this.func = 519;
/* 594 */       this.ref = -1.0F;
/*     */     }
/*     */     
/*     */     AlphaState(GlStateManager.GlStateManager.1 p_i46489_1_)
/*     */     {
/* 599 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class BlendState
/*     */   {
/*     */     public GlStateManager.BooleanState field_179213_a;
/*     */     public int srcFactor;
/*     */     public int dstFactor;
/*     */     public int srcFactorAlpha;
/*     */     public int dstFactorAlpha;
/*     */     private static final String __OBFID = "CL_00002555";
/*     */     
/*     */     private BlendState()
/*     */     {
/* 614 */       this.field_179213_a = new GlStateManager.BooleanState(3042);
/* 615 */       this.srcFactor = 1;
/* 616 */       this.dstFactor = 0;
/* 617 */       this.srcFactorAlpha = 1;
/* 618 */       this.dstFactorAlpha = 0;
/*     */     }
/*     */     
/*     */     BlendState(GlStateManager.GlStateManager.1 p_i46488_1_)
/*     */     {
/* 623 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class BooleanState
/*     */   {
/*     */     private final int capability;
/* 630 */     private boolean currentState = false;
/*     */     private static final String __OBFID = "CL_00002554";
/*     */     
/*     */     public BooleanState(int capabilityIn)
/*     */     {
/* 635 */       this.capability = capabilityIn;
/*     */     }
/*     */     
/*     */     public void setDisabled()
/*     */     {
/* 640 */       setState(false);
/*     */     }
/*     */     
/*     */     public void setEnabled()
/*     */     {
/* 645 */       setState(true);
/*     */     }
/*     */     
/*     */     public void setState(boolean state)
/*     */     {
/* 650 */       if (state != this.currentState)
/*     */       {
/* 652 */         this.currentState = state;
/*     */         
/* 654 */         if (state)
/*     */         {
/* 656 */           GL11.glEnable(this.capability);
/*     */         }
/*     */         else
/*     */         {
/* 660 */           GL11.glDisable(this.capability);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class ClearState
/*     */   {
/*     */     public double field_179205_a;
/*     */     public GlStateManager.Color field_179203_b;
/*     */     public int field_179204_c;
/*     */     private static final String __OBFID = "CL_00002553";
/*     */     
/*     */     private ClearState()
/*     */     {
/* 675 */       this.field_179205_a = 1.0D;
/* 676 */       this.field_179203_b = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
/* 677 */       this.field_179204_c = 0;
/*     */     }
/*     */     
/*     */     ClearState(GlStateManager.GlStateManager.1 p_i46487_1_)
/*     */     {
/* 682 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class Color
/*     */   {
/* 688 */     public float red = 1.0F;
/* 689 */     public float green = 1.0F;
/* 690 */     public float blue = 1.0F;
/* 691 */     public float alpha = 1.0F;
/*     */     
/*     */     private static final String __OBFID = "CL_00002552";
/*     */     
/*     */ 
/*     */     public Color() {}
/*     */     
/*     */     public Color(float redIn, float greenIn, float blueIn, float alphaIn)
/*     */     {
/* 700 */       this.red = redIn;
/* 701 */       this.green = greenIn;
/* 702 */       this.blue = blueIn;
/* 703 */       this.alpha = alphaIn;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ColorLogicState
/*     */   {
/*     */     public GlStateManager.BooleanState field_179197_a;
/*     */     public int field_179196_b;
/*     */     private static final String __OBFID = "CL_00002551";
/*     */     
/*     */     private ColorLogicState()
/*     */     {
/* 715 */       this.field_179197_a = new GlStateManager.BooleanState(3058);
/* 716 */       this.field_179196_b = 5379;
/*     */     }
/*     */     
/*     */     ColorLogicState(GlStateManager.GlStateManager.1 p_i46486_1_)
/*     */     {
/* 721 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ColorMask
/*     */   {
/*     */     public boolean red;
/*     */     public boolean green;
/*     */     public boolean blue;
/*     */     public boolean alpha;
/*     */     private static final String __OBFID = "CL_00002550";
/*     */     
/*     */     private ColorMask()
/*     */     {
/* 735 */       this.red = true;
/* 736 */       this.green = true;
/* 737 */       this.blue = true;
/* 738 */       this.alpha = true;
/*     */     }
/*     */     
/*     */     ColorMask(GlStateManager.GlStateManager.1 p_i46485_1_)
/*     */     {
/* 743 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ColorMaterialState
/*     */   {
/*     */     public GlStateManager.BooleanState field_179191_a;
/*     */     public int field_179189_b;
/*     */     public int field_179190_c;
/*     */     private static final String __OBFID = "CL_00002549";
/*     */     
/*     */     private ColorMaterialState()
/*     */     {
/* 756 */       this.field_179191_a = new GlStateManager.BooleanState(2903);
/* 757 */       this.field_179189_b = 1032;
/* 758 */       this.field_179190_c = 5634;
/*     */     }
/*     */     
/*     */     ColorMaterialState(GlStateManager.GlStateManager.1 p_i46484_1_)
/*     */     {
/* 763 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class CullState
/*     */   {
/*     */     public GlStateManager.BooleanState field_179054_a;
/*     */     public int field_179053_b;
/*     */     private static final String __OBFID = "CL_00002548";
/*     */     
/*     */     private CullState()
/*     */     {
/* 775 */       this.field_179054_a = new GlStateManager.BooleanState(2884);
/* 776 */       this.field_179053_b = 1029;
/*     */     }
/*     */     
/*     */     CullState(GlStateManager.GlStateManager.1 p_i46483_1_)
/*     */     {
/* 781 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class DepthState
/*     */   {
/*     */     public GlStateManager.BooleanState depthTest;
/*     */     public boolean maskEnabled;
/*     */     public int depthFunc;
/*     */     private static final String __OBFID = "CL_00002547";
/*     */     
/*     */     private DepthState()
/*     */     {
/* 794 */       this.depthTest = new GlStateManager.BooleanState(2929);
/* 795 */       this.maskEnabled = true;
/* 796 */       this.depthFunc = 513;
/*     */     }
/*     */     
/*     */     DepthState(GlStateManager.GlStateManager.1 p_i46482_1_)
/*     */     {
/* 801 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class FogState
/*     */   {
/*     */     public GlStateManager.BooleanState field_179049_a;
/*     */     public int field_179047_b;
/*     */     public float field_179048_c;
/*     */     public float field_179045_d;
/*     */     public float field_179046_e;
/*     */     private static final String __OBFID = "CL_00002546";
/*     */     
/*     */     private FogState()
/*     */     {
/* 816 */       this.field_179049_a = new GlStateManager.BooleanState(2912);
/* 817 */       this.field_179047_b = 2048;
/* 818 */       this.field_179048_c = 1.0F;
/* 819 */       this.field_179045_d = 0.0F;
/* 820 */       this.field_179046_e = 1.0F;
/*     */     }
/*     */     
/*     */     FogState(GlStateManager.GlStateManager.1 p_i46481_1_)
/*     */     {
/* 825 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class PolygonOffsetState
/*     */   {
/*     */     public GlStateManager.BooleanState field_179044_a;
/*     */     public GlStateManager.BooleanState field_179042_b;
/*     */     public float field_179043_c;
/*     */     public float field_179041_d;
/*     */     private static final String __OBFID = "CL_00002545";
/*     */     
/*     */     private PolygonOffsetState()
/*     */     {
/* 839 */       this.field_179044_a = new GlStateManager.BooleanState(32823);
/* 840 */       this.field_179042_b = new GlStateManager.BooleanState(10754);
/* 841 */       this.field_179043_c = 0.0F;
/* 842 */       this.field_179041_d = 0.0F;
/*     */     }
/*     */     
/*     */     PolygonOffsetState(GlStateManager.GlStateManager.1 p_i46480_1_)
/*     */     {
/* 847 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class StencilFunc
/*     */   {
/*     */     public int field_179081_a;
/*     */     public int field_179079_b;
/*     */     public int field_179080_c;
/*     */     private static final String __OBFID = "CL_00002544";
/*     */     
/*     */     private StencilFunc()
/*     */     {
/* 860 */       this.field_179081_a = 519;
/* 861 */       this.field_179079_b = 0;
/* 862 */       this.field_179080_c = -1;
/*     */     }
/*     */     
/*     */     StencilFunc(GlStateManager.GlStateManager.1 p_i46479_1_)
/*     */     {
/* 867 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class StencilState
/*     */   {
/*     */     public GlStateManager.StencilFunc field_179078_a;
/*     */     public int field_179076_b;
/*     */     public int field_179077_c;
/*     */     public int field_179074_d;
/*     */     public int field_179075_e;
/*     */     private static final String __OBFID = "CL_00002543";
/*     */     
/*     */     private StencilState()
/*     */     {
/* 882 */       this.field_179078_a = new GlStateManager.StencilFunc(null);
/* 883 */       this.field_179076_b = -1;
/* 884 */       this.field_179077_c = 7680;
/* 885 */       this.field_179074_d = 7680;
/* 886 */       this.field_179075_e = 7680;
/*     */     }
/*     */     
/*     */     StencilState(GlStateManager.GlStateManager.1 p_i46478_1_)
/*     */     {
/* 891 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum TexGen
/*     */   {
/* 897 */     S("S", 0), 
/* 898 */     T("T", 1), 
/* 899 */     R("R", 2), 
/* 900 */     Q("Q", 3);
/*     */     
/* 902 */     private static final TexGen[] $VALUES = { S, T, R, Q };
/*     */     
/*     */     private static final String __OBFID = "CL_00002542";
/*     */     
/*     */ 
/*     */     private TexGen(String p_i3_3_, int p_i3_4_) {}
/*     */   }
/*     */   
/*     */   static class TexGenCoord
/*     */   {
/*     */     public GlStateManager.BooleanState field_179067_a;
/*     */     public int field_179065_b;
/* 914 */     public int field_179066_c = -1;
/*     */     private static final String __OBFID = "CL_00002541";
/*     */     
/*     */     public TexGenCoord(int p_i46254_1_, int p_i46254_2_)
/*     */     {
/* 919 */       this.field_179065_b = p_i46254_1_;
/* 920 */       this.field_179067_a = new GlStateManager.BooleanState(p_i46254_2_);
/*     */     }
/*     */   }
/*     */   
/*     */   static class TexGenState
/*     */   {
/*     */     public GlStateManager.TexGenCoord field_179064_a;
/*     */     public GlStateManager.TexGenCoord field_179062_b;
/*     */     public GlStateManager.TexGenCoord field_179063_c;
/*     */     public GlStateManager.TexGenCoord field_179061_d;
/*     */     private static final String __OBFID = "CL_00002540";
/*     */     
/*     */     private TexGenState()
/*     */     {
/* 934 */       this.field_179064_a = new GlStateManager.TexGenCoord(8192, 3168);
/* 935 */       this.field_179062_b = new GlStateManager.TexGenCoord(8193, 3169);
/* 936 */       this.field_179063_c = new GlStateManager.TexGenCoord(8194, 3170);
/* 937 */       this.field_179061_d = new GlStateManager.TexGenCoord(8195, 3171);
/*     */     }
/*     */     
/*     */     TexGenState(GlStateManager.GlStateManager.1 p_i46477_1_)
/*     */     {
/* 942 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   static class TextureState
/*     */   {
/*     */     public GlStateManager.BooleanState texture2DState;
/*     */     public int textureName;
/*     */     private static final String __OBFID = "CL_00002539";
/*     */     
/*     */     private TextureState()
/*     */     {
/* 954 */       this.texture2DState = new GlStateManager.BooleanState(3553);
/* 955 */       this.textureName = 0;
/*     */     }
/*     */     
/*     */     TextureState(GlStateManager.GlStateManager.1 p_i46476_1_)
/*     */     {
/* 960 */       this();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\GlStateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */