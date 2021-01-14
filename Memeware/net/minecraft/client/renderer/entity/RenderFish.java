package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderFish extends Render {
    private static final ResourceLocation field_110792_a = new ResourceLocation("textures/particle/particles.png");
    private static final String __OBFID = "CL_00000996";

    public RenderFish(RenderManager p_i46175_1_) {
        super(p_i46175_1_);
    }

    public void func_180558_a(EntityFishHook p_180558_1_, double p_180558_2_, double p_180558_4_, double p_180558_6_, float p_180558_8_, float p_180558_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) p_180558_2_, (float) p_180558_4_, (float) p_180558_6_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(p_180558_1_);
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        byte var12 = 1;
        byte var13 = 2;
        float var14 = (float) (var12 * 8 + 0) / 128.0F;
        float var15 = (float) (var12 * 8 + 8) / 128.0F;
        float var16 = (float) (var13 * 8 + 0) / 128.0F;
        float var17 = (float) (var13 * 8 + 8) / 128.0F;
        float var18 = 1.0F;
        float var19 = 0.5F;
        float var20 = 0.5F;
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var11.startDrawingQuads();
        var11.func_178980_d(0.0F, 1.0F, 0.0F);
        var11.addVertexWithUV((double) (0.0F - var19), (double) (0.0F - var20), 0.0D, (double) var14, (double) var17);
        var11.addVertexWithUV((double) (var18 - var19), (double) (0.0F - var20), 0.0D, (double) var15, (double) var17);
        var11.addVertexWithUV((double) (var18 - var19), (double) (1.0F - var20), 0.0D, (double) var15, (double) var16);
        var11.addVertexWithUV((double) (0.0F - var19), (double) (1.0F - var20), 0.0D, (double) var14, (double) var16);
        var10.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        if (p_180558_1_.angler != null) {
            float var21 = p_180558_1_.angler.getSwingProgress(p_180558_9_);
            float var22 = MathHelper.sin(MathHelper.sqrt_float(var21) * (float) Math.PI);
            Vec3 var23 = new Vec3(-0.36D, 0.03D, 0.35D);
            var23 = var23.rotatePitch(-(p_180558_1_.angler.prevRotationPitch + (p_180558_1_.angler.rotationPitch - p_180558_1_.angler.prevRotationPitch) * p_180558_9_) * (float) Math.PI / 180.0F);
            var23 = var23.rotateYaw(-(p_180558_1_.angler.prevRotationYaw + (p_180558_1_.angler.rotationYaw - p_180558_1_.angler.prevRotationYaw) * p_180558_9_) * (float) Math.PI / 180.0F);
            var23 = var23.rotateYaw(var22 * 0.5F);
            var23 = var23.rotatePitch(-var22 * 0.7F);
            double var24 = p_180558_1_.angler.prevPosX + (p_180558_1_.angler.posX - p_180558_1_.angler.prevPosX) * (double) p_180558_9_ + var23.xCoord;
            double var26 = p_180558_1_.angler.prevPosY + (p_180558_1_.angler.posY - p_180558_1_.angler.prevPosY) * (double) p_180558_9_ + var23.yCoord;
            double var28 = p_180558_1_.angler.prevPosZ + (p_180558_1_.angler.posZ - p_180558_1_.angler.prevPosZ) * (double) p_180558_9_ + var23.zCoord;
            double var30 = (double) p_180558_1_.angler.getEyeHeight();

            if (this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0 || p_180558_1_.angler != Minecraft.getMinecraft().thePlayer) {
                float var32 = (p_180558_1_.angler.prevRenderYawOffset + (p_180558_1_.angler.renderYawOffset - p_180558_1_.angler.prevRenderYawOffset) * p_180558_9_) * (float) Math.PI / 180.0F;
                double var33 = (double) MathHelper.sin(var32);
                double var35 = (double) MathHelper.cos(var32);
                double var37 = 0.35D;
                double var39 = 0.8D;
                var24 = p_180558_1_.angler.prevPosX + (p_180558_1_.angler.posX - p_180558_1_.angler.prevPosX) * (double) p_180558_9_ - var35 * 0.35D - var33 * 0.8D;
                var26 = p_180558_1_.angler.prevPosY + var30 + (p_180558_1_.angler.posY - p_180558_1_.angler.prevPosY) * (double) p_180558_9_ - 0.45D;
                var28 = p_180558_1_.angler.prevPosZ + (p_180558_1_.angler.posZ - p_180558_1_.angler.prevPosZ) * (double) p_180558_9_ - var33 * 0.35D + var35 * 0.8D;
                var30 = p_180558_1_.angler.isSneaking() ? -0.1875D : 0.0D;
            }

            double var47 = p_180558_1_.prevPosX + (p_180558_1_.posX - p_180558_1_.prevPosX) * (double) p_180558_9_;
            double var34 = p_180558_1_.prevPosY + (p_180558_1_.posY - p_180558_1_.prevPosY) * (double) p_180558_9_ + 0.25D;
            double var36 = p_180558_1_.prevPosZ + (p_180558_1_.posZ - p_180558_1_.prevPosZ) * (double) p_180558_9_;
            double var38 = (double) ((float) (var24 - var47));
            double var40 = (double) ((float) (var26 - var34)) + var30;
            double var42 = (double) ((float) (var28 - var36));
            GlStateManager.func_179090_x();
            GlStateManager.disableLighting();
            var11.startDrawing(3);
            var11.func_178991_c(0);
            byte var44 = 16;

            for (int var45 = 0; var45 <= var44; ++var45) {
                float var46 = (float) var45 / (float) var44;
                var11.addVertex(p_180558_2_ + var38 * (double) var46, p_180558_4_ + var40 * (double) (var46 * var46 + var46) * 0.5D + 0.25D, p_180558_6_ + var42 * (double) var46);
            }

            var10.draw();
            GlStateManager.enableLighting();
            GlStateManager.func_179098_w();
            super.doRender(p_180558_1_, p_180558_2_, p_180558_4_, p_180558_6_, p_180558_8_, p_180558_9_);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFishHook p_110775_1_) {
        return field_110792_a;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityFishHook) p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_180558_a((EntityFishHook) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
