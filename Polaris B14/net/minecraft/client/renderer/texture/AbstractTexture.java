/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public abstract class AbstractTexture implements ITextureObject
/*    */ {
/*  7 */   protected int glTextureId = -1;
/*    */   protected boolean blur;
/*    */   protected boolean mipmap;
/*    */   protected boolean blurLast;
/*    */   protected boolean mipmapLast;
/*    */   
/*    */   public void setBlurMipmapDirect(boolean p_174937_1_, boolean p_174937_2_)
/*    */   {
/* 15 */     this.blur = p_174937_1_;
/* 16 */     this.mipmap = p_174937_2_;
/* 17 */     int i = -1;
/* 18 */     int j = -1;
/*    */     
/* 20 */     if (p_174937_1_)
/*    */     {
/* 22 */       i = p_174937_2_ ? 9987 : 9729;
/* 23 */       j = 9729;
/*    */     }
/*    */     else
/*    */     {
/* 27 */       i = p_174937_2_ ? 9986 : 9728;
/* 28 */       j = 9728;
/*    */     }
/*    */     
/* 31 */     GL11.glTexParameteri(3553, 10241, i);
/* 32 */     GL11.glTexParameteri(3553, 10240, j);
/*    */   }
/*    */   
/*    */   public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_)
/*    */   {
/* 37 */     this.blurLast = this.blur;
/* 38 */     this.mipmapLast = this.mipmap;
/* 39 */     setBlurMipmapDirect(p_174936_1_, p_174936_2_);
/*    */   }
/*    */   
/*    */   public void restoreLastBlurMipmap()
/*    */   {
/* 44 */     setBlurMipmapDirect(this.blurLast, this.mipmapLast);
/*    */   }
/*    */   
/*    */   public int getGlTextureId()
/*    */   {
/* 49 */     if (this.glTextureId == -1)
/*    */     {
/* 51 */       this.glTextureId = TextureUtil.glGenTextures();
/*    */     }
/*    */     
/* 54 */     return this.glTextureId;
/*    */   }
/*    */   
/*    */   public void deleteGlTexture()
/*    */   {
/* 59 */     if (this.glTextureId != -1)
/*    */     {
/* 61 */       TextureUtil.deleteTexture(this.glTextureId);
/* 62 */       this.glTextureId = -1;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\AbstractTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */