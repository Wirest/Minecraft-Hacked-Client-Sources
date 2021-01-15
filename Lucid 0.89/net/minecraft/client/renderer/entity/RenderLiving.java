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

public abstract class RenderLiving extends RendererLivingEntity
{

    public RenderLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLiving targetEntity)
    {
        return super.canRenderName(targetEntity) && (targetEntity.getAlwaysRenderNameTagForRender() || targetEntity.hasCustomName() && targetEntity == this.renderManager.pointedEntity);
    }

    public boolean shouldRenderLiving(EntityLiving entityLivingIn, ICamera camera, double camX, double camY, double camZ)
    {
        if (super.shouldRender(entityLivingIn, camera, camX, camY, camZ))
        {
            return true;
        }
        else if (entityLivingIn.getLeashed() && entityLivingIn.getLeashedToEntity() != null)
        {
            Entity var9 = entityLivingIn.getLeashedToEntity();
            return camera.isBoundingBoxInFrustum(var9.getEntityBoundingBox());
        }
        else
        {
            return false;
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    public void doRender(EntityLiving entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        this.renderLeash(entity, x, y, z, entityYaw, partialTicks);
    }

    public void func_177105_a(EntityLiving entityLivingIn, float partialTicks)
    {
        int var3 = entityLivingIn.getBrightnessForRender(partialTicks);
        int var4 = var3 % 65536;
        int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var4 / 1.0F, var5 / 1.0F);
    }

    /**
     * Gets the value between start and end according to pct
     */
    private double interpolateValue(double start, double end, double pct)
    {
        return start + (end - start) * pct;
    }

    protected void renderLeash(EntityLiving entityLivingIn, double p_110827_2_, double p_110827_4_, double p_110827_6_, float p_110827_8_, float p_110827_9_)
    {
        Entity var10 = entityLivingIn.getLeashedToEntity();

        if (var10 != null)
        {
            p_110827_4_ -= (1.6D - entityLivingIn.height) * 0.5D;
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();
            double var13 = this.interpolateValue(var10.prevRotationYaw, var10.rotationYaw, p_110827_9_ * 0.5F) * 0.01745329238474369D;
            double var15 = this.interpolateValue(var10.prevRotationPitch, var10.rotationPitch, p_110827_9_ * 0.5F) * 0.01745329238474369D;
            double var17 = Math.cos(var13);
            double var19 = Math.sin(var13);
            double var21 = Math.sin(var15);

            if (var10 instanceof EntityHanging)
            {
                var17 = 0.0D;
                var19 = 0.0D;
                var21 = -1.0D;
            }

            double var23 = Math.cos(var15);
            double var25 = this.interpolateValue(var10.prevPosX, var10.posX, p_110827_9_) - var17 * 0.7D - var19 * 0.5D * var23;
            double var27 = this.interpolateValue(var10.prevPosY + var10.getEyeHeight() * 0.7D, var10.posY + var10.getEyeHeight() * 0.7D, p_110827_9_) - var21 * 0.5D - 0.25D;
            double var29 = this.interpolateValue(var10.prevPosZ, var10.posZ, p_110827_9_) - var19 * 0.7D + var17 * 0.5D * var23;
            double var31 = this.interpolateValue(entityLivingIn.prevRenderYawOffset, entityLivingIn.renderYawOffset, p_110827_9_) * 0.01745329238474369D + (Math.PI / 2D);
            var17 = Math.cos(var31) * entityLivingIn.width * 0.4D;
            var19 = Math.sin(var31) * entityLivingIn.width * 0.4D;
            double var33 = this.interpolateValue(entityLivingIn.prevPosX, entityLivingIn.posX, p_110827_9_) + var17;
            double var35 = this.interpolateValue(entityLivingIn.prevPosY, entityLivingIn.posY, p_110827_9_);
            double var37 = this.interpolateValue(entityLivingIn.prevPosZ, entityLivingIn.posZ, p_110827_9_) + var19;
            p_110827_2_ += var17;
            p_110827_6_ += var19;
            double var39 = ((float)(var25 - var33));
            double var41 = ((float)(var27 - var35));
            double var43 = ((float)(var29 - var37));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            var12.startDrawing(5);
            int var48;
            float var49;

            for (var48 = 0; var48 <= 24; ++var48)
            {
                if (var48 % 2 == 0)
                {
                    var12.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                    var12.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                var49 = var48 / 24.0F;
                var12.addVertex(p_110827_2_ + var39 * var49 + 0.0D, p_110827_4_ + var41 * (var49 * var49 + var49) * 0.5D + ((24.0F - var48) / 18.0F + 0.125F), p_110827_6_ + var43 * var49);
                var12.addVertex(p_110827_2_ + var39 * var49 + 0.025D, p_110827_4_ + var41 * (var49 * var49 + var49) * 0.5D + ((24.0F - var48) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var43 * var49);
            }

            var11.draw();
            var12.startDrawing(5);

            for (var48 = 0; var48 <= 24; ++var48)
            {
                if (var48 % 2 == 0)
                {
                    var12.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                    var12.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                var49 = var48 / 24.0F;
                var12.addVertex(p_110827_2_ + var39 * var49 + 0.0D, p_110827_4_ + var41 * (var49 * var49 + var49) * 0.5D + ((24.0F - var48) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var43 * var49);
                var12.addVertex(p_110827_2_ + var39 * var49 + 0.025D, p_110827_4_ + var41 * (var49 * var49 + var49) * 0.5D + ((24.0F - var48) / 18.0F + 0.125F), p_110827_6_ + var43 * var49 + 0.025D);
            }

            var11.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }

    /**
     * Test if the entity name must be rendered
     */
    @Override
	protected boolean canRenderName(EntityLivingBase targetEntity)
    {
        return this.canRenderName((EntityLiving)targetEntity);
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
	public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.doRender((EntityLiving)entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
	protected boolean canRenderName(Entity entity)
    {
        return this.canRenderName((EntityLiving)entity);
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
        this.doRender((EntityLiving)entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
	public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ)
    {
        return this.shouldRenderLiving((EntityLiving)entity, camera, camX, camY, camZ);
    }
}
