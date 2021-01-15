package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderPainting extends Render
{
    private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");

    public RenderPainting(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    public void doRender(EntityPainting entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);
        EntityPainting.EnumArt var10 = entity.art;
        float var11 = 0.0625F;
        GlStateManager.scale(var11, var11, var11);
        this.func_77010_a(entity, var10.sizeX, var10.sizeY, var10.offsetX, var10.offsetY);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation func_180562_a(EntityPainting painting)
    {
        return KRISTOFFER_PAINTING_TEXTURE;
    }

    private void func_77010_a(EntityPainting painting, int p_77010_2_, int p_77010_3_, int p_77010_4_, int p_77010_5_)
    {
        float var6 = (-p_77010_2_) / 2.0F;
        float var7 = (-p_77010_3_) / 2.0F;
        float var8 = 0.5F;
        float var9 = 0.75F;
        float var10 = 0.8125F;
        float var11 = 0.0F;
        float var12 = 0.0625F;
        float var13 = 0.75F;
        float var14 = 0.8125F;
        float var15 = 0.001953125F;
        float var16 = 0.001953125F;
        float var17 = 0.7519531F;
        float var18 = 0.7519531F;
        float var19 = 0.0F;
        float var20 = 0.0625F;

        for (int var21 = 0; var21 < p_77010_2_ / 16; ++var21)
        {
            for (int var22 = 0; var22 < p_77010_3_ / 16; ++var22)
            {
                float var23 = var6 + (var21 + 1) * 16;
                float var24 = var6 + var21 * 16;
                float var25 = var7 + (var22 + 1) * 16;
                float var26 = var7 + var22 * 16;
                this.func_77008_a(painting, (var23 + var24) / 2.0F, (var25 + var26) / 2.0F);
                float var27 = (p_77010_4_ + p_77010_2_ - var21 * 16) / 256.0F;
                float var28 = (p_77010_4_ + p_77010_2_ - (var21 + 1) * 16) / 256.0F;
                float var29 = (p_77010_5_ + p_77010_3_ - var22 * 16) / 256.0F;
                float var30 = (p_77010_5_ + p_77010_3_ - (var22 + 1) * 16) / 256.0F;
                Tessellator var31 = Tessellator.getInstance();
                WorldRenderer var32 = var31.getWorldRenderer();
                var32.startDrawingQuads();
                var32.setNormal(0.0F, 0.0F, -1.0F);
                var32.addVertexWithUV(var23, var26, (-var8), var28, var29);
                var32.addVertexWithUV(var24, var26, (-var8), var27, var29);
                var32.addVertexWithUV(var24, var25, (-var8), var27, var30);
                var32.addVertexWithUV(var23, var25, (-var8), var28, var30);
                var32.setNormal(0.0F, 0.0F, 1.0F);
                var32.addVertexWithUV(var23, var25, var8, var9, var11);
                var32.addVertexWithUV(var24, var25, var8, var10, var11);
                var32.addVertexWithUV(var24, var26, var8, var10, var12);
                var32.addVertexWithUV(var23, var26, var8, var9, var12);
                var32.setNormal(0.0F, 1.0F, 0.0F);
                var32.addVertexWithUV(var23, var25, (-var8), var13, var15);
                var32.addVertexWithUV(var24, var25, (-var8), var14, var15);
                var32.addVertexWithUV(var24, var25, var8, var14, var16);
                var32.addVertexWithUV(var23, var25, var8, var13, var16);
                var32.setNormal(0.0F, -1.0F, 0.0F);
                var32.addVertexWithUV(var23, var26, var8, var13, var15);
                var32.addVertexWithUV(var24, var26, var8, var14, var15);
                var32.addVertexWithUV(var24, var26, (-var8), var14, var16);
                var32.addVertexWithUV(var23, var26, (-var8), var13, var16);
                var32.setNormal(-1.0F, 0.0F, 0.0F);
                var32.addVertexWithUV(var23, var25, var8, var18, var19);
                var32.addVertexWithUV(var23, var26, var8, var18, var20);
                var32.addVertexWithUV(var23, var26, (-var8), var17, var20);
                var32.addVertexWithUV(var23, var25, (-var8), var17, var19);
                var32.setNormal(1.0F, 0.0F, 0.0F);
                var32.addVertexWithUV(var24, var25, (-var8), var18, var19);
                var32.addVertexWithUV(var24, var26, (-var8), var18, var20);
                var32.addVertexWithUV(var24, var26, var8, var17, var20);
                var32.addVertexWithUV(var24, var25, var8, var17, var19);
                var31.draw();
            }
        }
    }

    private void func_77008_a(EntityPainting painting, float p_77008_2_, float p_77008_3_)
    {
        int var4 = MathHelper.floor_double(painting.posX);
        int var5 = MathHelper.floor_double(painting.posY + p_77008_3_ / 16.0F);
        int var6 = MathHelper.floor_double(painting.posZ);
        EnumFacing var7 = painting.facingDirection;

        if (var7 == EnumFacing.NORTH)
        {
            var4 = MathHelper.floor_double(painting.posX + p_77008_2_ / 16.0F);
        }

        if (var7 == EnumFacing.WEST)
        {
            var6 = MathHelper.floor_double(painting.posZ - p_77008_2_ / 16.0F);
        }

        if (var7 == EnumFacing.SOUTH)
        {
            var4 = MathHelper.floor_double(painting.posX - p_77008_2_ / 16.0F);
        }

        if (var7 == EnumFacing.EAST)
        {
            var6 = MathHelper.floor_double(painting.posZ + p_77008_2_ / 16.0F);
        }

        int var8 = this.renderManager.worldObj.getCombinedLight(new BlockPos(var4, var5, var6), 0);
        int var9 = var8 % 65536;
        int var10 = var8 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var9, var10);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180562_a((EntityPainting)entity);
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
        this.doRender((EntityPainting)entity, x, y, z, entityYaw, partialTicks);
    }
}
