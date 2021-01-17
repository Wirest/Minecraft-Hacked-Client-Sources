// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

public class Framebuffer
{
    public int framebufferTextureWidth;
    public int framebufferTextureHeight;
    public int framebufferWidth;
    public int framebufferHeight;
    public boolean useDepth;
    public int framebufferObject;
    public int framebufferTexture;
    public int depthBuffer;
    public float[] framebufferColor;
    public int framebufferFilter;
    
    public Framebuffer(final int p_i45078_1_, final int p_i45078_2_, final boolean p_i45078_3_) {
        this.useDepth = p_i45078_3_;
        this.framebufferObject = -1;
        this.framebufferTexture = -1;
        this.depthBuffer = -1;
        (this.framebufferColor = new float[4])[0] = 1.0f;
        this.framebufferColor[1] = 1.0f;
        this.framebufferColor[2] = 1.0f;
        this.framebufferColor[3] = 0.0f;
        this.createBindFramebuffer(p_i45078_1_, p_i45078_2_);
    }
    
    public void createBindFramebuffer(final int width, final int height) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferWidth = width;
            this.framebufferHeight = height;
        }
        else {
            GlStateManager.enableDepth();
            if (this.framebufferObject >= 0) {
                this.deleteFramebuffer();
            }
            this.createFramebuffer(width, height);
            this.checkFramebufferComplete();
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }
    
    public void deleteFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.unbindFramebufferTexture();
            this.unbindFramebuffer();
            if (this.depthBuffer > -1) {
                OpenGlHelper.glDeleteRenderbuffers(this.depthBuffer);
                this.depthBuffer = -1;
            }
            if (this.framebufferTexture > -1) {
                TextureUtil.deleteTexture(this.framebufferTexture);
                this.framebufferTexture = -1;
            }
            if (this.framebufferObject > -1) {
                OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
                OpenGlHelper.glDeleteFramebuffers(this.framebufferObject);
                this.framebufferObject = -1;
            }
        }
    }
    
    public void createFramebuffer(final int width, final int height) {
        this.framebufferWidth = width;
        this.framebufferHeight = height;
        this.framebufferTextureWidth = width;
        this.framebufferTextureHeight = height;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferClear();
        }
        else {
            this.framebufferObject = OpenGlHelper.glGenFramebuffers();
            this.framebufferTexture = TextureUtil.glGenTextures();
            if (this.useDepth) {
                this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
            }
            this.setFramebufferFilter(9728);
            GlStateManager.bindTexture(this.framebufferTexture);
            GL11.glTexImage2D(3553, 0, 32856, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, 6408, 5121, (ByteBuffer)null);
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
            OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, this.framebufferTexture, 0);
            if (this.useDepth) {
                OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
                OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
                OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
            }
            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }
    
    public void setFramebufferFilter(final int p_147607_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferFilter = p_147607_1_;
            GlStateManager.bindTexture(this.framebufferTexture);
            GL11.glTexParameterf(3553, 10241, (float)p_147607_1_);
            GL11.glTexParameterf(3553, 10240, (float)p_147607_1_);
            GL11.glTexParameterf(3553, 10242, 10496.0f);
            GL11.glTexParameterf(3553, 10243, 10496.0f);
            GlStateManager.bindTexture(0);
        }
    }
    
    public void checkFramebufferComplete() {
        final int i = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);
        if (i == OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
            return;
        }
        if (i == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
        }
        if (i == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
        }
        if (i == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
        }
        if (i == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
        }
        throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
    }
    
    public void bindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(this.framebufferTexture);
        }
    }
    
    public void unbindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(0);
        }
    }
    
    public void bindFramebuffer(final boolean p_147610_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
            if (p_147610_1_) {
                GlStateManager.viewport(0, 0, this.framebufferWidth, this.framebufferHeight);
            }
        }
    }
    
    public void unbindFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
        }
    }
    
    public void setFramebufferColor(final float p_147604_1_, final float p_147604_2_, final float p_147604_3_, final float p_147604_4_) {
        this.framebufferColor[0] = p_147604_1_;
        this.framebufferColor[1] = p_147604_2_;
        this.framebufferColor[2] = p_147604_3_;
        this.framebufferColor[3] = p_147604_4_;
    }
    
    public void framebufferRender(final int p_147615_1_, final int p_147615_2_) {
        this.framebufferRenderExt(p_147615_1_, p_147615_2_, true);
    }
    
    public void framebufferRenderExt(final int p_178038_1_, final int p_178038_2_, final boolean p_178038_3_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.colorMask(true, true, true, false);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, p_178038_1_, p_178038_2_, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GlStateManager.viewport(0, 0, p_178038_1_, p_178038_2_);
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            if (p_178038_3_) {
                GlStateManager.disableBlend();
                GlStateManager.enableColorMaterial();
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.bindFramebufferTexture();
            final float f = (float)p_178038_1_;
            final float f2 = (float)p_178038_2_;
            final float f3 = this.framebufferWidth / (float)this.framebufferTextureWidth;
            final float f4 = this.framebufferHeight / (float)this.framebufferTextureHeight;
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(0.0, f2, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(f, f2, 0.0).tex(f3, 0.0).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(f, 0.0, 0.0).tex(f3, f4).color(255, 255, 255, 255).endVertex();
            worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, f4).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            this.unbindFramebufferTexture();
            GlStateManager.depthMask(true);
            GlStateManager.colorMask(true, true, true, true);
        }
    }
    
    public void framebufferClear() {
        this.bindFramebuffer(true);
        GlStateManager.clearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
        int i = 16384;
        if (this.useDepth) {
            GlStateManager.clearDepth(1.0);
            i |= 0x100;
        }
        GlStateManager.clear(i);
        this.unbindFramebuffer();
    }
}
