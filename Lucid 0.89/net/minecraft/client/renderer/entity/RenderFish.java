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

public class RenderFish extends Render
{
    private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

    public RenderFish(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void func_180558_a(EntityFishHook fishHook, double p_180558_2_, double p_180558_4_, double p_180558_6_, float p_180558_8_, float p_180558_9_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180558_2_, (float)p_180558_4_, (float)p_180558_6_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(fishHook);
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        byte var12 = 1;
        byte var13 = 2;
        float var14 = (var12 * 8 + 0) / 128.0F;
        float var15 = (var12 * 8 + 8) / 128.0F;
        float var16 = (var13 * 8 + 0) / 128.0F;
        float var17 = (var13 * 8 + 8) / 128.0F;
        float var18 = 1.0F;
        float var19 = 0.5F;
        float var20 = 0.5F;
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var11.startDrawingQuads();
        var11.setNormal(0.0F, 1.0F, 0.0F);
        var11.addVertexWithUV(0.0F - var19, 0.0F - var20, 0.0D, var14, var17);
        var11.addVertexWithUV(var18 - var19, 0.0F - var20, 0.0D, var15, var17);
        var11.addVertexWithUV(var18 - var19, 1.0F - var20, 0.0D, var15, var16);
        var11.addVertexWithUV(0.0F - var19, 1.0F - var20, 0.0D, var14, var16);
        var10.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        if (fishHook.angler != null)
        {
            float var21 = fishHook.angler.getSwingProgress(p_180558_9_);
            float var22 = MathHelper.sin(MathHelper.sqrt_float(var21) * (float)Math.PI);
            Vec3 var23 = new Vec3(-0.36D, 0.03D, 0.35D);
            var23 = var23.rotatePitch(-(fishHook.angler.prevRotationPitch + (fishHook.angler.rotationPitch - fishHook.angler.prevRotationPitch) * p_180558_9_) * (float)Math.PI / 180.0F);
            var23 = var23.rotateYaw(-(fishHook.angler.prevRotationYaw + (fishHook.angler.rotationYaw - fishHook.angler.prevRotationYaw) * p_180558_9_) * (float)Math.PI / 180.0F);
            var23 = var23.rotateYaw(var22 * 0.5F);
            var23 = var23.rotatePitch(-var22 * 0.7F);
            double var24 = fishHook.angler.prevPosX + (fishHook.angler.posX - fishHook.angler.prevPosX) * p_180558_9_ + var23.xCoord;
            double var26 = fishHook.angler.prevPosY + (fishHook.angler.posY - fishHook.angler.prevPosY) * p_180558_9_ + var23.yCoord;
            double var28 = fishHook.angler.prevPosZ + (fishHook.angler.posZ - fishHook.angler.prevPosZ) * p_180558_9_ + var23.zCoord;
            double var30 = fishHook.angler.getEyeHeight();

            if (this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0 || fishHook.angler != Minecraft.getMinecraft().thePlayer)
            {
                float var32 = (fishHook.angler.prevRenderYawOffset + (fishHook.angler.renderYawOffset - fishHook.angler.prevRenderYawOffset) * p_180558_9_) * (float)Math.PI / 180.0F;
                double var33 = MathHelper.sin(var32);
                double var35 = MathHelper.cos(var32);
                var24 = fishHook.angler.prevPosX + (fishHook.angler.posX - fishHook.angler.prevPosX) * p_180558_9_ - var35 * 0.35D - var33 * 0.8D;
                var26 = fishHook.angler.prevPosY + var30 + (fishHook.angler.posY - fishHook.angler.prevPosY) * p_180558_9_ - 0.45D;
                var28 = fishHook.angler.prevPosZ + (fishHook.angler.posZ - fishHook.angler.prevPosZ) * p_180558_9_ - var33 * 0.35D + var35 * 0.8D;
                var30 = fishHook.angler.isSneaking() ? -0.1875D : 0.0D;
            }

            double var47 = fishHook.prevPosX + (fishHook.posX - fishHook.prevPosX) * p_180558_9_;
            double var34 = fishHook.prevPosY + (fishHook.posY - fishHook.prevPosY) * p_180558_9_ + 0.25D;
            double var36 = fishHook.prevPosZ + (fishHook.posZ - fishHook.prevPosZ) * p_180558_9_;
            double var38 = ((float)(var24 - var47));
            double var40 = ((float)(var26 - var34)) + var30;
            double var42 = ((float)(var28 - var36));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            var11.startDrawing(3);
            var11.setColorOpaque_I(0);
            byte var44 = 16;

            for (int var45 = 0; var45 <= var44; ++var45)
            {
                float var46 = (float)var45 / (float)var44;
                var11.addVertex(p_180558_2_ + var38 * var46, p_180558_4_ + var40 * (var46 * var46 + var46) * 0.5D + 0.25D, p_180558_6_ + var42 * var46);
            }

            var10.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(fishHook, p_180558_2_, p_180558_4_, p_180558_6_, p_180558_8_, p_180558_9_);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFishHook entity)
    {
        return FISH_PARTICLES;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityFishHook)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    @Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.func_180558_a((EntityFishHook)entity, x, y, z, entityYaw, partialTicks);
    }
}
