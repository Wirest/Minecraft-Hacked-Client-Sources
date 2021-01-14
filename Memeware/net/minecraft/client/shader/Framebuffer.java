package net.minecraft.client.shader;

import java.nio.ByteBuffer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public class Framebuffer {
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
    private static final String __OBFID = "CL_00000959";

    public Framebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_) {
        this.useDepth = p_i45078_3_;
        this.framebufferObject = -1;
        this.framebufferTexture = -1;
        this.depthBuffer = -1;
        this.framebufferColor = new float[4];
        this.framebufferColor[0] = 1.0F;
        this.framebufferColor[1] = 1.0F;
        this.framebufferColor[2] = 1.0F;
        this.framebufferColor[3] = 0.0F;
        this.createBindFramebuffer(p_i45078_1_, p_i45078_2_);
    }

    public void createBindFramebuffer(int p_147613_1_, int p_147613_2_) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferWidth = p_147613_1_;
            this.framebufferHeight = p_147613_2_;
        } else {
            GlStateManager.enableDepth();

            if (this.framebufferObject >= 0) {
                this.deleteFramebuffer();
            }

            this.createFramebuffer(p_147613_1_, p_147613_2_);
            this.checkFramebufferComplete();
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
        }
    }

    public void deleteFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.unbindFramebufferTexture();
            this.unbindFramebuffer();

            if (this.depthBuffer > -1) {
                OpenGlHelper.func_153184_g(this.depthBuffer);
                this.depthBuffer = -1;
            }

            if (this.framebufferTexture > -1) {
                TextureUtil.deleteTexture(this.framebufferTexture);
                this.framebufferTexture = -1;
            }

            if (this.framebufferObject > -1) {
                OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
                OpenGlHelper.func_153174_h(this.framebufferObject);
                this.framebufferObject = -1;
            }
        }
    }

    public void createFramebuffer(int p_147605_1_, int p_147605_2_) {
        this.framebufferWidth = p_147605_1_;
        this.framebufferHeight = p_147605_2_;
        this.framebufferTextureWidth = p_147605_1_;
        this.framebufferTextureHeight = p_147605_2_;

        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferClear();
        } else {
            this.framebufferObject = OpenGlHelper.func_153165_e();
            this.framebufferTexture = TextureUtil.glGenTextures();

            if (this.useDepth) {
                this.depthBuffer = OpenGlHelper.func_153185_f();
            }

            this.setFramebufferFilter(9728);
            GlStateManager.func_179144_i(this.framebufferTexture);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);
            OpenGlHelper.func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553, this.framebufferTexture, 0);

            if (this.useDepth) {
                OpenGlHelper.func_153176_h(OpenGlHelper.field_153199_f, this.depthBuffer);
                OpenGlHelper.func_153186_a(OpenGlHelper.field_153199_f, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
                OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e, OpenGlHelper.field_153201_h, OpenGlHelper.field_153199_f, this.depthBuffer);
            }

            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }

    public void setFramebufferFilter(int p_147607_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferFilter = p_147607_1_;
            GlStateManager.func_179144_i(this.framebufferTexture);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, (float) p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, (float) p_147607_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0F);
            GlStateManager.func_179144_i(0);
        }
    }

    public void checkFramebufferComplete() {
        int var1 = OpenGlHelper.func_153167_i(OpenGlHelper.field_153198_e);

        if (var1 != OpenGlHelper.field_153202_i) {
            if (var1 == OpenGlHelper.field_153203_j) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            } else if (var1 == OpenGlHelper.field_153204_k) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            } else if (var1 == OpenGlHelper.field_153205_l) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            } else if (var1 == OpenGlHelper.field_153206_m) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            } else {
                throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + var1);
            }
        }
    }

    public void bindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.func_179144_i(this.framebufferTexture);
        }
    }

    public void unbindFramebufferTexture() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.func_179144_i(0);
        }
    }

    public void bindFramebuffer(boolean p_147610_1_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);

            if (p_147610_1_) {
                GlStateManager.viewport(0, 0, this.framebufferWidth, this.framebufferHeight);
            }
        }
    }

    public void unbindFramebuffer() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
        }
    }

    public void setFramebufferColor(float p_147604_1_, float p_147604_2_, float p_147604_3_, float p_147604_4_) {
        this.framebufferColor[0] = p_147604_1_;
        this.framebufferColor[1] = p_147604_2_;
        this.framebufferColor[2] = p_147604_3_;
        this.framebufferColor[3] = p_147604_4_;
    }

    public void framebufferRender(int p_147615_1_, int p_147615_2_) {
        this.func_178038_a(p_147615_1_, p_147615_2_, true);
    }

    public void func_178038_a(int p_178038_1_, int p_178038_2_, boolean p_178038_3_) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.colorMask(true, true, true, false);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, (double) p_178038_1_, (double) p_178038_2_, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.viewport(0, 0, p_178038_1_, p_178038_2_);
            GlStateManager.func_179098_w();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();

            if (p_178038_3_) {
                GlStateManager.disableBlend();
                GlStateManager.enableColorMaterial();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindFramebufferTexture();
            float var4 = (float) p_178038_1_;
            float var5 = (float) p_178038_2_;
            float var6 = (float) this.framebufferWidth / (float) this.framebufferTextureWidth;
            float var7 = (float) this.framebufferHeight / (float) this.framebufferTextureHeight;
            Tessellator var8 = Tessellator.getInstance();
            WorldRenderer var9 = var8.getWorldRenderer();
            var9.startDrawingQuads();
            var9.func_178991_c(-1);
            var9.addVertexWithUV(0.0D, (double) var5, 0.0D, 0.0D, 0.0D);
            var9.addVertexWithUV((double) var4, (double) var5, 0.0D, (double) var6, 0.0D);
            var9.addVertexWithUV((double) var4, 0.0D, 0.0D, (double) var6, (double) var7);
            var9.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double) var7);
            var8.draw();
            this.unbindFramebufferTexture();
            GlStateManager.depthMask(true);
            GlStateManager.colorMask(true, true, true, true);
        }
    }

    public void framebufferClear() {
        this.bindFramebuffer(true);
        GlStateManager.clearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
        int var1 = 16384;

        if (this.useDepth) {
            GlStateManager.clearDepth(1.0D);
            var1 |= 256;
        }

        GlStateManager.clear(var1);
        this.unbindFramebuffer();
    }
}
