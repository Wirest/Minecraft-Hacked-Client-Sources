package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderMinecart extends Render
{
    private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");

    /** instance of ModelMinecart for rendering */
    protected ModelBase modelMinecart = new ModelMinecart();
    private static final String __OBFID = "CL_00001013";

    public RenderMinecart(RenderManager p_i46155_1_)
    {
        super(p_i46155_1_);
        this.shadowSize = 0.5F;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityMinecart p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(p_76986_1_);
        long var10 = (long)p_76986_1_.getEntityId() * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        float var12 = (((float)(var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var13 = (((float)(var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var14 = (((float)(var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GlStateManager.translate(var12, var13, var14);
        double var15 = p_76986_1_.lastTickPosX + (p_76986_1_.posX - p_76986_1_.lastTickPosX) * (double)p_76986_9_;
        double var17 = p_76986_1_.lastTickPosY + (p_76986_1_.posY - p_76986_1_.lastTickPosY) * (double)p_76986_9_;
        double var19 = p_76986_1_.lastTickPosZ + (p_76986_1_.posZ - p_76986_1_.lastTickPosZ) * (double)p_76986_9_;
        double var21 = 0.30000001192092896D;
        Vec3 var23 = p_76986_1_.func_70489_a(var15, var17, var19);
        float var24 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;

        if (var23 != null)
        {
            Vec3 var25 = p_76986_1_.func_70495_a(var15, var17, var19, var21);
            Vec3 var26 = p_76986_1_.func_70495_a(var15, var17, var19, -var21);

            if (var25 == null)
            {
                var25 = var23;
            }

            if (var26 == null)
            {
                var26 = var23;
            }

            p_76986_2_ += var23.xCoord - var15;
            p_76986_4_ += (var25.yCoord + var26.yCoord) / 2.0D - var17;
            p_76986_6_ += var23.zCoord - var19;
            Vec3 var27 = var26.addVector(-var25.xCoord, -var25.yCoord, -var25.zCoord);

            if (var27.lengthVector() != 0.0D)
            {
                var27 = var27.normalize();
                p_76986_8_ = (float)(Math.atan2(var27.zCoord, var27.xCoord) * 180.0D / Math.PI);
                var24 = (float)(Math.atan(var27.yCoord) * 73.0D);
            }
        }

        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_ + 0.375F, (float)p_76986_6_);
        GlStateManager.rotate(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-var24, 0.0F, 0.0F, 1.0F);
        float var30 = (float)p_76986_1_.getRollingAmplitude() - p_76986_9_;
        float var31 = p_76986_1_.getDamage() - p_76986_9_;

        if (var31 < 0.0F)
        {
            var31 = 0.0F;
        }

        if (var30 > 0.0F)
        {
            GlStateManager.rotate(MathHelper.sin(var30) * var30 * var31 / 10.0F * (float)p_76986_1_.getRollingDirection(), 1.0F, 0.0F, 0.0F);
        }

        int var32 = p_76986_1_.getDisplayTileOffset();
        IBlockState var28 = p_76986_1_.func_174897_t();

        if (var28.getBlock().getRenderType() != -1)
        {
            GlStateManager.pushMatrix();
            this.bindTexture(TextureMap.locationBlocksTexture);
            float var29 = 0.75F;
            GlStateManager.scale(var29, var29, var29);
            GlStateManager.translate(-0.5F, (float)(var32 - 8) / 16.0F, 0.5F);
            this.func_180560_a(p_76986_1_, p_76986_9_, var28);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindEntityTexture(p_76986_1_);
        }

        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        this.modelMinecart.render(p_76986_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityMinecart p_110775_1_)
    {
        return minecartTextures;
    }

    protected void func_180560_a(EntityMinecart p_180560_1_, float p_180560_2_, IBlockState p_180560_3_)
    {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getBlockRendererDispatcher().func_175016_a(p_180560_3_, p_180560_1_.getBrightness(p_180560_2_));
        GlStateManager.popMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityMinecart)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityMinecart)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
