package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderGuardian extends RenderLiving
{
    private static final ResourceLocation field_177114_e = new ResourceLocation("textures/entity/guardian.png");
    private static final ResourceLocation field_177116_j = new ResourceLocation("textures/entity/guardian_elder.png");
    private static final ResourceLocation field_177117_k = new ResourceLocation("textures/entity/guardian_beam.png");
    int field_177115_a;
    private static final String __OBFID = "CL_00002443";

    public RenderGuardian(RenderManager p_i46171_1_)
    {
        super(p_i46171_1_, new ModelGuardian(), 0.5F);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }

    public boolean func_177113_a(EntityGuardian p_177113_1_, ICamera p_177113_2_, double p_177113_3_, double p_177113_5_, double p_177113_7_)
    {
        if (super.func_177104_a(p_177113_1_, p_177113_2_, p_177113_3_, p_177113_5_, p_177113_7_))
        {
            return true;
        }
        else
        {
            if (p_177113_1_.func_175474_cn())
            {
                EntityLivingBase var9 = p_177113_1_.func_175466_co();

                if (var9 != null)
                {
                    Vec3 var10 = this.func_177110_a(var9, (double)var9.height * 0.5D, 1.0F);
                    Vec3 var11 = this.func_177110_a(p_177113_1_, (double)p_177113_1_.getEyeHeight(), 1.0F);

                    if (p_177113_2_.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(var11.xCoord, var11.yCoord, var11.zCoord, var10.xCoord, var10.yCoord, var10.zCoord)))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private Vec3 func_177110_a(EntityLivingBase p_177110_1_, double p_177110_2_, float p_177110_4_)
    {
        double var5 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * (double)p_177110_4_;
        double var7 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * (double)p_177110_4_;
        double var9 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3(var5, var7, var9);
    }

    public void func_177109_a(EntityGuardian p_177109_1_, double p_177109_2_, double p_177109_4_, double p_177109_6_, float p_177109_8_, float p_177109_9_)
    {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a())
        {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }

        super.doRender((EntityLiving)p_177109_1_, p_177109_2_, p_177109_4_, p_177109_6_, p_177109_8_, p_177109_9_);
        EntityLivingBase var10 = p_177109_1_.func_175466_co();

        if (var10 != null)
        {
            float var11 = p_177109_1_.func_175477_p(p_177109_9_);
            Tessellator var12 = Tessellator.getInstance();
            WorldRenderer var13 = var12.getWorldRenderer();
            this.bindTexture(field_177117_k);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float var14 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var14, var14);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float var15 = (float)p_177109_1_.worldObj.getTotalWorldTime() + p_177109_9_;
            float var16 = var15 * 0.5F % 1.0F;
            float var17 = p_177109_1_.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_177109_2_, (float)p_177109_4_ + var17, (float)p_177109_6_);
            Vec3 var18 = this.func_177110_a(var10, (double)var10.height * 0.5D, p_177109_9_);
            Vec3 var19 = this.func_177110_a(p_177109_1_, (double)var17, p_177109_9_);
            Vec3 var20 = var18.subtract(var19);
            double var21 = var20.lengthVector() + 1.0D;
            var20 = var20.normalize();
            float var23 = (float)Math.acos(var20.yCoord);
            float var24 = (float)Math.atan2(var20.zCoord, var20.xCoord);
            GlStateManager.rotate((((float)Math.PI / 2F) + -var24) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var23 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            byte var25 = 1;
            double var26 = (double)var15 * 0.05D * (1.0D - (double)(var25 & 1) * 2.5D);
            var13.startDrawingQuads();
            float var28 = var11 * var11;
            var13.func_178961_b(64 + (int)(var28 * 240.0F), 32 + (int)(var28 * 192.0F), 128 - (int)(var28 * 64.0F), 255);
            double var29 = (double)var25 * 0.2D;
            double var31 = var29 * 1.41D;
            double var33 = 0.0D + Math.cos(var26 + 2.356194490192345D) * var31;
            double var35 = 0.0D + Math.sin(var26 + 2.356194490192345D) * var31;
            double var37 = 0.0D + Math.cos(var26 + (Math.PI / 4D)) * var31;
            double var39 = 0.0D + Math.sin(var26 + (Math.PI / 4D)) * var31;
            double var41 = 0.0D + Math.cos(var26 + 3.9269908169872414D) * var31;
            double var43 = 0.0D + Math.sin(var26 + 3.9269908169872414D) * var31;
            double var45 = 0.0D + Math.cos(var26 + 5.497787143782138D) * var31;
            double var47 = 0.0D + Math.sin(var26 + 5.497787143782138D) * var31;
            double var49 = 0.0D + Math.cos(var26 + Math.PI) * var29;
            double var51 = 0.0D + Math.sin(var26 + Math.PI) * var29;
            double var53 = 0.0D + Math.cos(var26 + 0.0D) * var29;
            double var55 = 0.0D + Math.sin(var26 + 0.0D) * var29;
            double var57 = 0.0D + Math.cos(var26 + (Math.PI / 2D)) * var29;
            double var59 = 0.0D + Math.sin(var26 + (Math.PI / 2D)) * var29;
            double var61 = 0.0D + Math.cos(var26 + (Math.PI * 3D / 2D)) * var29;
            double var63 = 0.0D + Math.sin(var26 + (Math.PI * 3D / 2D)) * var29;
            double var67 = 0.0D;
            double var69 = 0.4999D;
            double var71 = (double)(-1.0F + var16);
            double var73 = var21 * (0.5D / var29) + var71;
            var13.addVertexWithUV(var49, var21, var51, var69, var73);
            var13.addVertexWithUV(var49, 0.0D, var51, var69, var71);
            var13.addVertexWithUV(var53, 0.0D, var55, var67, var71);
            var13.addVertexWithUV(var53, var21, var55, var67, var73);
            var13.addVertexWithUV(var57, var21, var59, var69, var73);
            var13.addVertexWithUV(var57, 0.0D, var59, var69, var71);
            var13.addVertexWithUV(var61, 0.0D, var63, var67, var71);
            var13.addVertexWithUV(var61, var21, var63, var67, var73);
            double var75 = 0.0D;

            if (p_177109_1_.ticksExisted % 2 == 0)
            {
                var75 = 0.5D;
            }

            var13.addVertexWithUV(var33, var21, var35, 0.5D, var75 + 0.5D);
            var13.addVertexWithUV(var37, var21, var39, 1.0D, var75 + 0.5D);
            var13.addVertexWithUV(var45, var21, var47, 1.0D, var75);
            var13.addVertexWithUV(var41, var21, var43, 0.5D, var75);
            var12.draw();
            GlStateManager.popMatrix();
        }
    }

    protected void func_177112_a(EntityGuardian p_177112_1_, float p_177112_2_)
    {
        if (p_177112_1_.func_175461_cl())
        {
            GlStateManager.scale(2.35F, 2.35F, 2.35F);
        }
    }

    protected ResourceLocation func_177111_a(EntityGuardian p_177111_1_)
    {
        return p_177111_1_.func_175461_cl() ? field_177116_j : field_177114_e;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public boolean func_177104_a(EntityLiving p_177104_1_, ICamera p_177104_2_, double p_177104_3_, double p_177104_5_, double p_177104_7_)
    {
        return this.func_177113_a((EntityGuardian)p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.func_177112_a((EntityGuardian)p_77041_1_, p_77041_2_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.func_177111_a((EntityGuardian)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.func_177109_a((EntityGuardian)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public boolean func_177071_a(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_)
    {
        return this.func_177113_a((EntityGuardian)p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }
}
