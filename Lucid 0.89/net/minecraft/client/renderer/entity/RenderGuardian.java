package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

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

public class RenderGuardian extends RenderLiving
{
    private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
    private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    int field_177115_a;

    public RenderGuardian(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelGuardian(), 0.5F);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }

    public boolean shouldRenderGuardian(EntityGuardian guardian, ICamera camera, double p_177113_3_, double p_177113_5_, double p_177113_7_)
    {
        if (super.shouldRenderLiving(guardian, camera, p_177113_3_, p_177113_5_, p_177113_7_))
        {
            return true;
        }
        else
        {
            if (guardian.hasTargetedEntity())
            {
                EntityLivingBase var9 = guardian.getTargetedEntity();

                if (var9 != null)
                {
                    Vec3 var10 = this.func_177110_a(var9, var9.height * 0.5D, 1.0F);
                    Vec3 var11 = this.func_177110_a(guardian, guardian.getEyeHeight(), 1.0F);

                    if (camera.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(var11.xCoord, var11.yCoord, var11.zCoord, var10.xCoord, var10.yCoord, var10.zCoord)))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private Vec3 func_177110_a(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_)
    {
        double var5 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * p_177110_4_;
        double var7 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * p_177110_4_;
        double var9 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * p_177110_4_;
        return new Vec3(var5, var7, var9);
    }

    public void func_177109_a(EntityGuardian guardian, double p_177109_2_, double p_177109_4_, double p_177109_6_, float p_177109_8_, float p_177109_9_)
    {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a())
        {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }

        super.doRender(guardian, p_177109_2_, p_177109_4_, p_177109_6_, p_177109_8_, p_177109_9_);
        EntityLivingBase var10 = guardian.getTargetedEntity();

        if (var10 != null)
        {
            float var11 = guardian.func_175477_p(p_177109_9_);
            Tessellator var12 = Tessellator.getInstance();
            WorldRenderer var13 = var12.getWorldRenderer();
            this.bindTexture(GUARDIAN_BEAM_TEXTURE);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float var14 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var14, var14);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float var15 = guardian.worldObj.getTotalWorldTime() + p_177109_9_;
            float var16 = var15 * 0.5F % 1.0F;
            float var17 = guardian.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_177109_2_, (float)p_177109_4_ + var17, (float)p_177109_6_);
            Vec3 var18 = this.func_177110_a(var10, var10.height * 0.5D, p_177109_9_);
            Vec3 var19 = this.func_177110_a(guardian, var17, p_177109_9_);
            Vec3 var20 = var18.subtract(var19);
            double var21 = var20.lengthVector() + 1.0D;
            var20 = var20.normalize();
            float var23 = (float)Math.acos(var20.yCoord);
            float var24 = (float)Math.atan2(var20.zCoord, var20.xCoord);
            GlStateManager.rotate((((float)Math.PI / 2F) + -var24) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var23 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            byte var25 = 1;
            double var26 = var15 * 0.05D * (1.0D - (var25 & 1) * 2.5D);
            var13.startDrawingQuads();
            float var28 = var11 * var11;
            var13.setColorRGBA(64 + (int)(var28 * 240.0F), 32 + (int)(var28 * 192.0F), 128 - (int)(var28 * 64.0F), 255);
            double var29 = var25 * 0.2D;
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
            double var71 = -1.0F + var16;
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

            if (guardian.ticksExisted % 2 == 0)
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

    protected void func_177112_a(EntityGuardian guardian, float p_177112_2_)
    {
        if (guardian.isElder())
        {
            GlStateManager.scale(2.35F, 2.35F, 2.35F);
        }
    }

    protected ResourceLocation func_177111_a(EntityGuardian guardian)
    {
        return guardian.isElder() ? GUARDIAN_ELDER_TEXTURE : GUARDIAN_TEXTURE;
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
	public void doRender(EntityLiving entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.func_177109_a((EntityGuardian)entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
	public boolean shouldRenderLiving(EntityLiving entityLivingIn, ICamera camera, double camX, double camY, double camZ)
    {
        return this.shouldRenderGuardian((EntityGuardian)entityLivingIn, camera, camX, camY, camZ);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
	protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime)
    {
        this.func_177112_a((EntityGuardian)entitylivingbaseIn, partialTickTime);
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
        this.func_177109_a((EntityGuardian)entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_177111_a((EntityGuardian)entity);
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
        this.func_177109_a((EntityGuardian)entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
	public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ)
    {
        return this.shouldRenderGuardian((EntityGuardian)entity, camera, camX, camY, camZ);
    }
}
