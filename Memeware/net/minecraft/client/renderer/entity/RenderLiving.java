package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import optifine.Config;
import shadersmod.client.Shaders;

public abstract class RenderLiving extends RendererLivingEntity {
    private static final String __OBFID = "CL_00001015";

    public RenderLiving(RenderManager p_i46153_1_, ModelBase p_i46153_2_, float p_i46153_3_) {
        super(p_i46153_1_, p_i46153_2_, p_i46153_3_);
    }

    /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLiving targetEntity) {
        return super.canRenderName(targetEntity) && (targetEntity.getAlwaysRenderNameTagForRender() || targetEntity.hasCustomName() && targetEntity == this.renderManager.field_147941_i);
    }

    public boolean func_177104_a(EntityLiving p_177104_1_, ICamera p_177104_2_, double p_177104_3_, double p_177104_5_, double p_177104_7_) {
        if (super.func_177071_a(p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_)) {
            return true;
        } else if (p_177104_1_.getLeashed() && p_177104_1_.getLeashedToEntity() != null) {
            Entity var9 = p_177104_1_.getLeashedToEntity();
            return p_177104_2_.isBoundingBoxInFrustum(var9.getEntityBoundingBox());
        } else {
            return false;
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        super.doRender((EntityLivingBase) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        this.func_110827_b(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void func_177105_a(EntityLiving p_177105_1_, float p_177105_2_) {
        int var3 = p_177105_1_.getBrightnessForRender(p_177105_2_);
        int var4 = var3 % 65536;
        int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var4 / 1.0F, (float) var5 / 1.0F);
    }

    private double func_110828_a(double start, double end, double pct) {
        return start + (end - start) * pct;
    }

    protected void func_110827_b(EntityLiving p_110827_1_, double p_110827_2_, double p_110827_4_, double p_110827_6_, float p_110827_8_, float p_110827_9_) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            Entity var10 = p_110827_1_.getLeashedToEntity();

            if (var10 != null) {
                p_110827_4_ -= (1.6D - (double) p_110827_1_.height) * 0.5D;
                Tessellator var11 = Tessellator.getInstance();
                WorldRenderer var12 = var11.getWorldRenderer();
                double var13 = this.func_110828_a((double) var10.prevRotationYaw, (double) var10.rotationYaw, (double) (p_110827_9_ * 0.5F)) * 0.01745329238474369D;
                double var15 = this.func_110828_a((double) var10.prevRotationPitch, (double) var10.rotationPitch, (double) (p_110827_9_ * 0.5F)) * 0.01745329238474369D;
                double var17 = Math.cos(var13);
                double var19 = Math.sin(var13);
                double var21 = Math.sin(var15);

                if (var10 instanceof EntityHanging) {
                    var17 = 0.0D;
                    var19 = 0.0D;
                    var21 = -1.0D;
                }

                double var23 = Math.cos(var15);
                double var25 = this.func_110828_a(var10.prevPosX, var10.posX, (double) p_110827_9_) - var17 * 0.7D - var19 * 0.5D * var23;
                double var27 = this.func_110828_a(var10.prevPosY + (double) var10.getEyeHeight() * 0.7D, var10.posY + (double) var10.getEyeHeight() * 0.7D, (double) p_110827_9_) - var21 * 0.5D - 0.25D;
                double var29 = this.func_110828_a(var10.prevPosZ, var10.posZ, (double) p_110827_9_) - var19 * 0.7D + var17 * 0.5D * var23;
                double var31 = this.func_110828_a((double) p_110827_1_.prevRenderYawOffset, (double) p_110827_1_.renderYawOffset, (double) p_110827_9_) * 0.01745329238474369D + (Math.PI / 2D);
                var17 = Math.cos(var31) * (double) p_110827_1_.width * 0.4D;
                var19 = Math.sin(var31) * (double) p_110827_1_.width * 0.4D;
                double var33 = this.func_110828_a(p_110827_1_.prevPosX, p_110827_1_.posX, (double) p_110827_9_) + var17;
                double var35 = this.func_110828_a(p_110827_1_.prevPosY, p_110827_1_.posY, (double) p_110827_9_);
                double var37 = this.func_110828_a(p_110827_1_.prevPosZ, p_110827_1_.posZ, (double) p_110827_9_) + var19;
                p_110827_2_ += var17;
                p_110827_6_ += var19;
                double var39 = (double) ((float) (var25 - var33));
                double var41 = (double) ((float) (var27 - var35));
                double var43 = (double) ((float) (var29 - var37));
                GlStateManager.func_179090_x();
                GlStateManager.disableLighting();
                GlStateManager.disableCull();

                if (Config.isShaders()) {
                    Shaders.beginLeash();
                }

                boolean var45 = true;
                double var46 = 0.025D;
                var12.startDrawing(5);
                int var48;
                float var49;

                for (var48 = 0; var48 <= 24; ++var48) {
                    if (var48 % 2 == 0) {
                        var12.func_178960_a(0.5F, 0.4F, 0.3F, 1.0F);
                    } else {
                        var12.func_178960_a(0.35F, 0.28F, 0.21000001F, 1.0F);
                    }

                    var49 = (float) var48 / 24.0F;
                    var12.addVertex(p_110827_2_ + var39 * (double) var49 + 0.0D, p_110827_4_ + var41 * (double) (var49 * var49 + var49) * 0.5D + (double) ((24.0F - (float) var48) / 18.0F + 0.125F), p_110827_6_ + var43 * (double) var49);
                    var12.addVertex(p_110827_2_ + var39 * (double) var49 + 0.025D, p_110827_4_ + var41 * (double) (var49 * var49 + var49) * 0.5D + (double) ((24.0F - (float) var48) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var43 * (double) var49);
                }

                var11.draw();
                var12.startDrawing(5);

                for (var48 = 0; var48 <= 24; ++var48) {
                    if (var48 % 2 == 0) {
                        var12.func_178960_a(0.5F, 0.4F, 0.3F, 1.0F);
                    } else {
                        var12.func_178960_a(0.35F, 0.28F, 0.21000001F, 1.0F);
                    }

                    var49 = (float) var48 / 24.0F;
                    var12.addVertex(p_110827_2_ + var39 * (double) var49 + 0.0D, p_110827_4_ + var41 * (double) (var49 * var49 + var49) * 0.5D + (double) ((24.0F - (float) var48) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var43 * (double) var49);
                    var12.addVertex(p_110827_2_ + var39 * (double) var49 + 0.025D, p_110827_4_ + var41 * (double) (var49 * var49 + var49) * 0.5D + (double) ((24.0F - (float) var48) / 18.0F + 0.125F), p_110827_6_ + var43 * (double) var49 + 0.025D);
                }

                var11.draw();

                if (Config.isShaders()) {
                    Shaders.endLeash();
                }

                GlStateManager.enableLighting();
                GlStateManager.func_179098_w();
                GlStateManager.enableCull();
            }
        }
    }

    /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLivingBase targetEntity) {
        return this.canRenderName((EntityLiving) targetEntity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected boolean func_177070_b(Entity p_177070_1_) {
        return this.canRenderName((EntityLiving) p_177070_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public boolean func_177071_a(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_) {
        return this.func_177104_a((EntityLiving) p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }
}
