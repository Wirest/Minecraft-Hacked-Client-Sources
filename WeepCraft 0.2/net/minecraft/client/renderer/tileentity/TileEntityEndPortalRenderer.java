package net.minecraft.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import shadersmod.client.ShadersRender;

public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal>
{
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    public void func_192841_a(TileEntityEndPortal p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        if (!Config.isShaders() || !ShadersRender.renderEndPortal(p_192841_1_, p_192841_2_, p_192841_4_, p_192841_6_, p_192841_8_, p_192841_9_, this.func_191287_c()))
        {
            GlStateManager.disableLighting();
            RANDOM.setSeed(31100L);
            GlStateManager.getFloat(2982, MODELVIEW);
            GlStateManager.getFloat(2983, PROJECTION);
            double d0 = p_192841_2_ * p_192841_2_ + p_192841_4_ * p_192841_4_ + p_192841_6_ * p_192841_6_;
            int i = this.func_191286_a(d0);
            float f = this.func_191287_c();
            boolean flag = false;

            for (int j = 0; j < i; ++j)
            {
                GlStateManager.pushMatrix();
                float f1 = 2.0F / (float)(18 - j);

                if (j == 0)
                {
                    this.bindTexture(END_SKY_TEXTURE);
                    f1 = 0.15F;
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                }

                if (j >= 1)
                {
                    this.bindTexture(END_PORTAL_TEXTURE);
                    flag = true;
                    Minecraft.getMinecraft().entityRenderer.func_191514_d(true);
                }

                if (j == 1)
                {
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                }

                GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
                GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.5F, 0.5F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 1.0F);
                float f2 = (float)(j + 1);
                GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
                GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);
                GlStateManager.multMatrix(PROJECTION);
                GlStateManager.multMatrix(MODELVIEW);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                float f3 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f1;
                float f4 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f1;
                float f5 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f1;

                if (p_192841_1_.shouldRenderFace(EnumFacing.SOUTH))
                {
                    bufferbuilder.pos(p_192841_2_, p_192841_4_, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                }

                if (p_192841_1_.shouldRenderFace(EnumFacing.NORTH))
                {
                    bufferbuilder.pos(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_, p_192841_4_, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                }

                if (p_192841_1_.shouldRenderFace(EnumFacing.EAST))
                {
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                }

                if (p_192841_1_.shouldRenderFace(EnumFacing.WEST))
                {
                    bufferbuilder.pos(p_192841_2_, p_192841_4_, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_, p_192841_4_, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                }

                if (p_192841_1_.shouldRenderFace(EnumFacing.DOWN))
                {
                    bufferbuilder.pos(p_192841_2_, p_192841_4_, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_, p_192841_4_, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                }

                if (p_192841_1_.shouldRenderFace(EnumFacing.UP))
                {
                    bufferbuilder.pos(p_192841_2_, p_192841_4_ + (double)f, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_ + (double)f, p_192841_6_ + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_ + 1.0D, p_192841_4_ + (double)f, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                    bufferbuilder.pos(p_192841_2_, p_192841_4_ + (double)f, p_192841_6_).color(f3, f4, f5, 1.0F).endVertex();
                }

                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                this.bindTexture(END_SKY_TEXTURE);
            }

            GlStateManager.disableBlend();
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableLighting();

            if (flag)
            {
                Minecraft.getMinecraft().entityRenderer.func_191514_d(false);
            }
        }
    }

    protected int func_191286_a(double p_191286_1_)
    {
        int i;

        if (p_191286_1_ > 36864.0D)
        {
            i = 1;
        }
        else if (p_191286_1_ > 25600.0D)
        {
            i = 3;
        }
        else if (p_191286_1_ > 16384.0D)
        {
            i = 5;
        }
        else if (p_191286_1_ > 9216.0D)
        {
            i = 7;
        }
        else if (p_191286_1_ > 4096.0D)
        {
            i = 9;
        }
        else if (p_191286_1_ > 1024.0D)
        {
            i = 11;
        }
        else if (p_191286_1_ > 576.0D)
        {
            i = 13;
        }
        else if (p_191286_1_ > 256.0D)
        {
            i = 14;
        }
        else
        {
            i = 15;
        }

        return i;
    }

    protected float func_191287_c()
    {
        return 0.75F;
    }

    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }
}
