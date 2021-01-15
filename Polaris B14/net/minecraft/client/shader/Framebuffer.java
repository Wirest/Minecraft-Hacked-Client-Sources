/*     */ package net.minecraft.client.shader;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ public class Framebuffer
/*     */ {
/*     */   public int framebufferTextureWidth;
/*     */   public int framebufferTextureHeight;
/*     */   public int framebufferWidth;
/*     */   public int framebufferHeight;
/*     */   public boolean useDepth;
/*     */   public int framebufferObject;
/*     */   public int framebufferTexture;
/*     */   public int depthBuffer;
/*     */   public float[] framebufferColor;
/*     */   public int framebufferFilter;
/*     */   
/*     */   public Framebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_)
/*     */   {
/*  27 */     this.useDepth = p_i45078_3_;
/*  28 */     this.framebufferObject = -1;
/*  29 */     this.framebufferTexture = -1;
/*  30 */     this.depthBuffer = -1;
/*  31 */     this.framebufferColor = new float[4];
/*  32 */     this.framebufferColor[0] = 1.0F;
/*  33 */     this.framebufferColor[1] = 1.0F;
/*  34 */     this.framebufferColor[2] = 1.0F;
/*  35 */     this.framebufferColor[3] = 0.0F;
/*  36 */     createBindFramebuffer(p_i45078_1_, p_i45078_2_);
/*     */   }
/*     */   
/*     */   public void createBindFramebuffer(int width, int height)
/*     */   {
/*  41 */     if (!OpenGlHelper.isFramebufferEnabled())
/*     */     {
/*  43 */       this.framebufferWidth = width;
/*  44 */       this.framebufferHeight = height;
/*     */     }
/*     */     else
/*     */     {
/*  48 */       GlStateManager.enableDepth();
/*     */       
/*  50 */       if (this.framebufferObject >= 0)
/*     */       {
/*  52 */         deleteFramebuffer();
/*     */       }
/*     */       
/*  55 */       createFramebuffer(width, height);
/*  56 */       checkFramebufferComplete();
/*  57 */       OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteFramebuffer()
/*     */   {
/*  63 */     if (OpenGlHelper.isFramebufferEnabled())
/*     */     {
/*  65 */       unbindFramebufferTexture();
/*  66 */       unbindFramebuffer();
/*     */       
/*  68 */       if (this.depthBuffer > -1)
/*     */       {
/*  70 */         OpenGlHelper.glDeleteRenderbuffers(this.depthBuffer);
/*  71 */         this.depthBuffer = -1;
/*     */       }
/*     */       
/*  74 */       if (this.framebufferTexture > -1)
/*     */       {
/*  76 */         TextureUtil.deleteTexture(this.framebufferTexture);
/*  77 */         this.framebufferTexture = -1;
/*     */       }
/*     */       
/*  80 */       if (this.framebufferObject > -1)
/*     */       {
/*  82 */         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
/*  83 */         OpenGlHelper.glDeleteFramebuffers(this.framebufferObject);
/*  84 */         this.framebufferObject = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void createFramebuffer(int width, int height)
/*     */   {
/*  91 */     this.framebufferWidth = width;
/*  92 */     this.framebufferHeight = height;
/*  93 */     this.framebufferTextureWidth = width;
/*  94 */     this.framebufferTextureHeight = height;
/*     */     
/*  96 */     if (!OpenGlHelper.isFramebufferEnabled())
/*     */     {
/*  98 */       framebufferClear();
/*     */     }
/*     */     else
/*     */     {
/* 102 */       this.framebufferObject = OpenGlHelper.glGenFramebuffers();
/* 103 */       this.framebufferTexture = TextureUtil.glGenTextures();
/*     */       
/* 105 */       if (this.useDepth)
/*     */       {
/* 107 */         this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
/*     */       }
/*     */       
/* 110 */       setFramebufferFilter(9728);
/* 111 */       GlStateManager.bindTexture(this.framebufferTexture);
/* 112 */       GL11.glTexImage2D(3553, 0, 32856, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, 6408, 5121, null);
/* 113 */       OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
/* 114 */       OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, this.framebufferTexture, 0);
/*     */       
/* 116 */       if (this.useDepth)
/*     */       {
/* 118 */         OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
/* 119 */         OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
/* 120 */         OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
/*     */       }
/*     */       
/* 123 */       framebufferClear();
/* 124 */       unbindFramebufferTexture();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFramebufferFilter(int p_147607_1_)
/*     */   {
/* 130 */     if (OpenGlHelper.isFramebufferEnabled())
/*     */     {
/* 132 */       this.framebufferFilter = p_147607_1_;
/* 133 */       GlStateManager.bindTexture(this.framebufferTexture);
/* 134 */       GL11.glTexParameterf(3553, 10241, p_147607_1_);
/* 135 */       GL11.glTexParameterf(3553, 10240, p_147607_1_);
/* 136 */       GL11.glTexParameterf(3553, 10242, 10496.0F);
/* 137 */       GL11.glTexParameterf(3553, 10243, 10496.0F);
/* 138 */       GlStateManager.bindTexture(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void checkFramebufferComplete()
/*     */   {
/* 144 */     int i = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);
/*     */     
/* 146 */     if (i != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE)
/*     */     {
/* 148 */       if (i == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT)
/*     */       {
/* 150 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
/*     */       }
/* 152 */       if (i == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH)
/*     */       {
/* 154 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
/*     */       }
/* 156 */       if (i == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER)
/*     */       {
/* 158 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
/*     */       }
/* 160 */       if (i == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER)
/*     */       {
/* 162 */         throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
/*     */       }
/*     */       
/*     */ 
/* 166 */       throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void bindFramebufferTexture()
/*     */   {
/* 173 */     if (OpenGlHelper.isFramebufferEnabled())
/*     */     {
/* 175 */       GlStateManager.bindTexture(this.framebufferTexture);
/*     */     }
/*     */   }
/*     */   
/*     */   public void unbindFramebufferTexture()
/*     */   {
/* 181 */     if (OpenGlHelper.isFramebufferEnabled())
/*     */     {
/* 183 */       GlStateManager.bindTexture(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void bindFramebuffer(boolean p_147610_1_)
/*     */   {
/* 189 */     if (OpenGlHelper.isFramebufferEnabled())
/*     */     {
/* 191 */       OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
/*     */       
/* 193 */       if (p_147610_1_)
/*     */       {
/* 195 */         GlStateManager.viewport(0, 0, this.framebufferWidth, this.framebufferHeight);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void unbindFramebuffer()
/*     */   {
/* 202 */     if (OpenGlHelper.isFramebufferEnabled())
/*     */     {
/* 204 */       OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFramebufferColor(float p_147604_1_, float p_147604_2_, float p_147604_3_, float p_147604_4_)
/*     */   {
/* 210 */     this.framebufferColor[0] = p_147604_1_;
/* 211 */     this.framebufferColor[1] = p_147604_2_;
/* 212 */     this.framebufferColor[2] = p_147604_3_;
/* 213 */     this.framebufferColor[3] = p_147604_4_;
/*     */   }
/*     */   
/*     */   public void framebufferRender(int p_147615_1_, int p_147615_2_)
/*     */   {
/* 218 */     framebufferRenderExt(p_147615_1_, p_147615_2_, true);
/*     */   }
/*     */   
/*     */   public void framebufferRenderExt(int p_178038_1_, int p_178038_2_, boolean p_178038_3_)
/*     */   {
/* 223 */     if (OpenGlHelper.isFramebufferEnabled())
/*     */     {
/* 225 */       GlStateManager.colorMask(true, true, true, false);
/* 226 */       GlStateManager.disableDepth();
/* 227 */       GlStateManager.depthMask(false);
/* 228 */       GlStateManager.matrixMode(5889);
/* 229 */       GlStateManager.loadIdentity();
/* 230 */       GlStateManager.ortho(0.0D, p_178038_1_, p_178038_2_, 0.0D, 1000.0D, 3000.0D);
/* 231 */       GlStateManager.matrixMode(5888);
/* 232 */       GlStateManager.loadIdentity();
/* 233 */       GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/* 234 */       GlStateManager.viewport(0, 0, p_178038_1_, p_178038_2_);
/* 235 */       GlStateManager.enableTexture2D();
/* 236 */       GlStateManager.disableLighting();
/* 237 */       GlStateManager.disableAlpha();
/*     */       
/* 239 */       if (p_178038_3_)
/*     */       {
/* 241 */         GlStateManager.disableBlend();
/* 242 */         GlStateManager.enableColorMaterial();
/*     */       }
/*     */       
/* 245 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 246 */       bindFramebufferTexture();
/* 247 */       float f = p_178038_1_;
/* 248 */       float f1 = p_178038_2_;
/* 249 */       float f2 = this.framebufferWidth / this.framebufferTextureWidth;
/* 250 */       float f3 = this.framebufferHeight / this.framebufferTextureHeight;
/* 251 */       Tessellator tessellator = Tessellator.getInstance();
/* 252 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 253 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 254 */       worldrenderer.pos(0.0D, f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
/* 255 */       worldrenderer.pos(f, f1, 0.0D).tex(f2, 0.0D).color(255, 255, 255, 255).endVertex();
/* 256 */       worldrenderer.pos(f, 0.0D, 0.0D).tex(f2, f3).color(255, 255, 255, 255).endVertex();
/* 257 */       worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, f3).color(255, 255, 255, 255).endVertex();
/* 258 */       tessellator.draw();
/* 259 */       unbindFramebufferTexture();
/* 260 */       GlStateManager.depthMask(true);
/* 261 */       GlStateManager.colorMask(true, true, true, true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void framebufferClear()
/*     */   {
/* 267 */     bindFramebuffer(true);
/* 268 */     GlStateManager.clearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
/* 269 */     int i = 16384;
/*     */     
/* 271 */     if (this.useDepth)
/*     */     {
/* 273 */       GlStateManager.clearDepth(1.0D);
/* 274 */       i |= 0x100;
/*     */     }
/*     */     
/* 277 */     GlStateManager.clear(i);
/* 278 */     unbindFramebuffer();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\shader\Framebuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */