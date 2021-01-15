package net.minecraft.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;

public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147529_c = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation field_147526_d = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random field_147527_e = new Random(31100L);
    FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);
    private static final String __OBFID = "CL_00002467";

    public void func_180544_a(TileEntityEndPortal p_180544_1_, double p_180544_2_, double p_180544_4_, double p_180544_6_, float p_180544_8_, int p_180544_9_)
    {
        float var10 = (float)this.rendererDispatcher.field_147560_j;
        float var11 = (float)this.rendererDispatcher.field_147561_k;
        float var12 = (float)this.rendererDispatcher.field_147558_l;
        GlStateManager.disableLighting();
        field_147527_e.setSeed(31100L);
        float var13 = 0.75F;

        for (int var14 = 0; var14 < 16; ++var14)
        {
            GlStateManager.pushMatrix();
            float var15 = (float)(16 - var14);
            float var16 = 0.0625F;
            float var17 = 1.0F / (var15 + 1.0F);

            if (var14 == 0)
            {
                this.bindTexture(field_147529_c);
                var17 = 0.1F;
                var15 = 65.0F;
                var16 = 0.125F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
            }

            if (var14 >= 1)
            {
                this.bindTexture(field_147526_d);
            }

            if (var14 == 1)
            {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(1, 1);
                var16 = 0.5F;
            }

            float var18 = (float)(-(p_180544_4_ + (double)var13));
            float var19 = var18 + (float)ActiveRenderInfo.func_178804_a().yCoord;
            float var20 = var18 + var15 + (float)ActiveRenderInfo.func_178804_a().yCoord;
            float var21 = var19 / var20;
            var21 += (float)(p_180544_4_ + (double)var13);
            GlStateManager.translate(var10, var21, var12);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
            GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9473, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9473, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9473, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
            GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9474, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GlStateManager.scale(var16, var16, var16);
            GlStateManager.translate(0.5F, 0.5F, 0.0F);
            GlStateManager.rotate((float)(var14 * var14 * 4321 + var14 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-0.5F, -0.5F, 0.0F);
            GlStateManager.translate(-var10, -var12, -var11);
            var19 = var18 + (float)ActiveRenderInfo.func_178804_a().yCoord;
            GlStateManager.translate((float)ActiveRenderInfo.func_178804_a().xCoord * var15 / var19, (float)ActiveRenderInfo.func_178804_a().zCoord * var15 / var19, -var11);
            Tessellator var25 = Tessellator.getInstance();
            WorldRenderer var26 = var25.getWorldRenderer();
            var26.startDrawingQuads();
            float var22 = field_147527_e.nextFloat() * 0.5F + 0.1F;
            float var23 = field_147527_e.nextFloat() * 0.5F + 0.4F;
            float var24 = field_147527_e.nextFloat() * 0.5F + 0.5F;

            if (var14 == 0)
            {
                var24 = 1.0F;
                var23 = 1.0F;
                var22 = 1.0F;
            }

            var26.func_178960_a(var22 * var17, var23 * var17, var24 * var17, 1.0F);
            var26.addVertex(p_180544_2_, p_180544_4_ + (double)var13, p_180544_6_);
            var26.addVertex(p_180544_2_, p_180544_4_ + (double)var13, p_180544_6_ + 1.0D);
            var26.addVertex(p_180544_2_ + 1.0D, p_180544_4_ + (double)var13, p_180544_6_ + 1.0D);
            var26.addVertex(p_180544_2_ + 1.0D, p_180544_4_ + (double)var13, p_180544_6_);
            var25.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
        GlStateManager.enableLighting();
    }

    private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.field_147528_b.clear();
        this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }

    public void renderTileEntityAt(TileEntity p_180535_1_, double p_180535_2_, double p_180535_4_, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.func_180544_a((TileEntityEndPortal)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
}
