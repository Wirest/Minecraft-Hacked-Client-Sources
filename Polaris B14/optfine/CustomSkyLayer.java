/*     */ package optfine;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ 
/*     */ public class CustomSkyLayer
/*     */ {
/*  11 */   public String source = null;
/*  12 */   private int startFadeIn = -1;
/*  13 */   private int endFadeIn = -1;
/*  14 */   private int startFadeOut = -1;
/*  15 */   private int endFadeOut = -1;
/*  16 */   private int blend = 1;
/*  17 */   private boolean rotate = false;
/*  18 */   private float speed = 1.0F;
/*     */   private float[] axis;
/*     */   public int textureId;
/*  21 */   public static final float[] DEFAULT_AXIS = { 1.0F, 0.0F, 0.0F };
/*     */   
/*     */   public CustomSkyLayer(Properties p_i31_1_, String p_i31_2_)
/*     */   {
/*  25 */     this.axis = DEFAULT_AXIS;
/*  26 */     this.textureId = -1;
/*  27 */     this.source = p_i31_1_.getProperty("source", p_i31_2_);
/*  28 */     this.startFadeIn = parseTime(p_i31_1_.getProperty("startFadeIn"));
/*  29 */     this.endFadeIn = parseTime(p_i31_1_.getProperty("endFadeIn"));
/*  30 */     this.startFadeOut = parseTime(p_i31_1_.getProperty("startFadeOut"));
/*  31 */     this.endFadeOut = parseTime(p_i31_1_.getProperty("endFadeOut"));
/*  32 */     this.blend = Blender.parseBlend(p_i31_1_.getProperty("blend"));
/*  33 */     this.rotate = parseBoolean(p_i31_1_.getProperty("rotate"), true);
/*  34 */     this.speed = parseFloat(p_i31_1_.getProperty("speed"), 1.0F);
/*  35 */     this.axis = parseAxis(p_i31_1_.getProperty("axis"), DEFAULT_AXIS);
/*     */   }
/*     */   
/*     */   private int parseTime(String p_parseTime_1_)
/*     */   {
/*  40 */     if (p_parseTime_1_ == null)
/*     */     {
/*  42 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*  46 */     String[] astring = Config.tokenize(p_parseTime_1_, ":");
/*     */     
/*  48 */     if (astring.length != 2)
/*     */     {
/*  50 */       Config.warn("Invalid time: " + p_parseTime_1_);
/*  51 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*  55 */     String s = astring[0];
/*  56 */     String s1 = astring[1];
/*  57 */     int i = Config.parseInt(s, -1);
/*  58 */     int j = Config.parseInt(s1, -1);
/*     */     
/*  60 */     if ((i >= 0) && (i <= 23) && (j >= 0) && (j <= 59))
/*     */     {
/*  62 */       i -= 6;
/*     */       
/*  64 */       if (i < 0)
/*     */       {
/*  66 */         i += 24;
/*     */       }
/*     */       
/*  69 */       int k = i * 1000 + (int)(j / 60.0D * 1000.0D);
/*  70 */       return k;
/*     */     }
/*     */     
/*     */ 
/*  74 */     Config.warn("Invalid time: " + p_parseTime_1_);
/*  75 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean parseBoolean(String p_parseBoolean_1_, boolean p_parseBoolean_2_)
/*     */   {
/*  83 */     if (p_parseBoolean_1_ == null)
/*     */     {
/*  85 */       return p_parseBoolean_2_;
/*     */     }
/*  87 */     if (p_parseBoolean_1_.toLowerCase().equals("true"))
/*     */     {
/*  89 */       return true;
/*     */     }
/*  91 */     if (p_parseBoolean_1_.toLowerCase().equals("false"))
/*     */     {
/*  93 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  97 */     Config.warn("Unknown boolean: " + p_parseBoolean_1_);
/*  98 */     return p_parseBoolean_2_;
/*     */   }
/*     */   
/*     */ 
/*     */   private float parseFloat(String p_parseFloat_1_, float p_parseFloat_2_)
/*     */   {
/* 104 */     if (p_parseFloat_1_ == null)
/*     */     {
/* 106 */       return p_parseFloat_2_;
/*     */     }
/*     */     
/*     */ 
/* 110 */     float f = Config.parseFloat(p_parseFloat_1_, Float.MIN_VALUE);
/*     */     
/* 112 */     if (f == Float.MIN_VALUE)
/*     */     {
/* 114 */       Config.warn("Invalid value: " + p_parseFloat_1_);
/* 115 */       return p_parseFloat_2_;
/*     */     }
/*     */     
/*     */ 
/* 119 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private float[] parseAxis(String p_parseAxis_1_, float[] p_parseAxis_2_)
/*     */   {
/* 126 */     if (p_parseAxis_1_ == null)
/*     */     {
/* 128 */       return p_parseAxis_2_;
/*     */     }
/*     */     
/*     */ 
/* 132 */     String[] astring = Config.tokenize(p_parseAxis_1_, " ");
/*     */     
/* 134 */     if (astring.length != 3)
/*     */     {
/* 136 */       Config.warn("Invalid axis: " + p_parseAxis_1_);
/* 137 */       return p_parseAxis_2_;
/*     */     }
/*     */     
/*     */ 
/* 141 */     float[] afloat = new float[3];
/*     */     
/* 143 */     for (int i = 0; i < astring.length; i++)
/*     */     {
/* 145 */       afloat[i] = Config.parseFloat(astring[i], Float.MIN_VALUE);
/*     */       
/* 147 */       if (afloat[i] == Float.MIN_VALUE)
/*     */       {
/* 149 */         Config.warn("Invalid axis: " + p_parseAxis_1_);
/* 150 */         return p_parseAxis_2_;
/*     */       }
/*     */       
/* 153 */       if ((afloat[i] < -1.0F) || (afloat[i] > 1.0F))
/*     */       {
/* 155 */         Config.warn("Invalid axis values: " + p_parseAxis_1_);
/* 156 */         return p_parseAxis_2_;
/*     */       }
/*     */     }
/*     */     
/* 160 */     float f2 = afloat[0];
/* 161 */     float f = afloat[1];
/* 162 */     float f1 = afloat[2];
/*     */     
/* 164 */     if (f2 * f2 + f * f + f1 * f1 < 1.0E-5F)
/*     */     {
/* 166 */       Config.warn("Invalid axis values: " + p_parseAxis_1_);
/* 167 */       return p_parseAxis_2_;
/*     */     }
/*     */     
/*     */ 
/* 171 */     float[] afloat1 = { f1, f, -f2 };
/* 172 */     return afloat1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isValid(String p_isValid_1_)
/*     */   {
/* 180 */     if (this.source == null)
/*     */     {
/* 182 */       Config.warn("No source texture: " + p_isValid_1_);
/* 183 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 187 */     this.source = TextureUtils.fixResourcePath(this.source, TextureUtils.getBasePath(p_isValid_1_));
/*     */     
/* 189 */     if ((this.startFadeIn >= 0) && (this.endFadeIn >= 0) && (this.endFadeOut >= 0))
/*     */     {
/* 191 */       int i = normalizeTime(this.endFadeIn - this.startFadeIn);
/*     */       
/* 193 */       if (this.startFadeOut < 0)
/*     */       {
/* 195 */         this.startFadeOut = normalizeTime(this.endFadeOut - i);
/*     */       }
/*     */       
/* 198 */       int j = normalizeTime(this.startFadeOut - this.endFadeIn);
/* 199 */       int k = normalizeTime(this.endFadeOut - this.startFadeOut);
/* 200 */       int l = normalizeTime(this.startFadeIn - this.endFadeOut);
/* 201 */       int i1 = i + j + k + l;
/*     */       
/* 203 */       if (i1 != 24000)
/*     */       {
/* 205 */         Config.warn("Invalid fadeIn/fadeOut times, sum is more than 24h: " + i1);
/* 206 */         return false;
/*     */       }
/* 208 */       if (this.speed < 0.0F)
/*     */       {
/* 210 */         Config.warn("Invalid speed: " + this.speed);
/* 211 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 215 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 220 */     Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
/* 221 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int normalizeTime(int p_normalizeTime_1_)
/*     */   {
/* 228 */     while (p_normalizeTime_1_ >= 24000)
/*     */     {
/* 230 */       p_normalizeTime_1_ -= 24000;
/*     */     }
/*     */     
/* 233 */     while (p_normalizeTime_1_ < 0)
/*     */     {
/* 235 */       p_normalizeTime_1_ += 24000;
/*     */     }
/*     */     
/* 238 */     return p_normalizeTime_1_;
/*     */   }
/*     */   
/*     */   public void render(int p_render_1_, float p_render_2_, float p_render_3_)
/*     */   {
/* 243 */     float f = p_render_3_ * getFadeBrightness(p_render_1_);
/* 244 */     f = Config.limit(f, 0.0F, 1.0F);
/*     */     
/* 246 */     if (f >= 1.0E-4F)
/*     */     {
/* 248 */       GlStateManager.bindTexture(this.textureId);
/* 249 */       Blender.setupBlend(this.blend, f);
/* 250 */       GlStateManager.pushMatrix();
/*     */       
/* 252 */       if (this.rotate)
/*     */       {
/* 254 */         GlStateManager.rotate(p_render_2_ * 360.0F * this.speed, this.axis[0], this.axis[1], this.axis[2]);
/*     */       }
/*     */       
/* 257 */       Tessellator tessellator = Tessellator.getInstance();
/* 258 */       GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/* 259 */       GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
/* 260 */       renderSide(tessellator, 4);
/* 261 */       GlStateManager.pushMatrix();
/* 262 */       GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/* 263 */       renderSide(tessellator, 1);
/* 264 */       GlStateManager.popMatrix();
/* 265 */       GlStateManager.pushMatrix();
/* 266 */       GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
/* 267 */       renderSide(tessellator, 0);
/* 268 */       GlStateManager.popMatrix();
/* 269 */       GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/* 270 */       renderSide(tessellator, 5);
/* 271 */       GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/* 272 */       renderSide(tessellator, 2);
/* 273 */       GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/* 274 */       renderSide(tessellator, 3);
/* 275 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   private float getFadeBrightness(int p_getFadeBrightness_1_)
/*     */   {
/* 281 */     if (timeBetween(p_getFadeBrightness_1_, this.startFadeIn, this.endFadeIn))
/*     */     {
/* 283 */       int k = normalizeTime(this.endFadeIn - this.startFadeIn);
/* 284 */       int l = normalizeTime(p_getFadeBrightness_1_ - this.startFadeIn);
/* 285 */       return l / k;
/*     */     }
/* 287 */     if (timeBetween(p_getFadeBrightness_1_, this.endFadeIn, this.startFadeOut))
/*     */     {
/* 289 */       return 1.0F;
/*     */     }
/* 291 */     if (timeBetween(p_getFadeBrightness_1_, this.startFadeOut, this.endFadeOut))
/*     */     {
/* 293 */       int i = normalizeTime(this.endFadeOut - this.startFadeOut);
/* 294 */       int j = normalizeTime(p_getFadeBrightness_1_ - this.startFadeOut);
/* 295 */       return 1.0F - j / i;
/*     */     }
/*     */     
/*     */ 
/* 299 */     return 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   private void renderSide(Tessellator p_renderSide_1_, int p_renderSide_2_)
/*     */   {
/* 305 */     WorldRenderer worldrenderer = p_renderSide_1_.getWorldRenderer();
/* 306 */     double d0 = p_renderSide_2_ % 3 / 3.0D;
/* 307 */     double d1 = p_renderSide_2_ / 3 / 2.0D;
/* 308 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 309 */     worldrenderer.pos(-100.0D, -100.0D, -100.0D).tex(d0, d1).endVertex();
/* 310 */     worldrenderer.pos(-100.0D, -100.0D, 100.0D).tex(d0, d1 + 0.5D).endVertex();
/* 311 */     worldrenderer.pos(100.0D, -100.0D, 100.0D).tex(d0 + 0.3333333333333333D, d1 + 0.5D).endVertex();
/* 312 */     worldrenderer.pos(100.0D, -100.0D, -100.0D).tex(d0 + 0.3333333333333333D, d1).endVertex();
/* 313 */     p_renderSide_1_.draw();
/*     */   }
/*     */   
/*     */   public boolean isActive(int p_isActive_1_)
/*     */   {
/* 318 */     return !timeBetween(p_isActive_1_, this.endFadeOut, this.startFadeIn);
/*     */   }
/*     */   
/*     */   private boolean timeBetween(int p_timeBetween_1_, int p_timeBetween_2_, int p_timeBetween_3_)
/*     */   {
/* 323 */     return (p_timeBetween_1_ >= p_timeBetween_2_) && (p_timeBetween_1_ <= p_timeBetween_3_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\CustomSkyLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */