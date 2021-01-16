package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderDragon extends RenderLiving
{
    private static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
    private static final ResourceLocation enderDragonExplodingTextures = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
    private static final ResourceLocation enderDragonTextures = new ResourceLocation("textures/entity/enderdragon/dragon.png");

    /** An instance of the dragon model in RenderDragon */
    protected ModelDragon modelDragon;
    private static final String __OBFID = "CL_00000988";

    public RenderDragon(RenderManager p_i46183_1_)
    {
        super(p_i46183_1_, new ModelDragon(0.0F), 0.5F);
        this.modelDragon = (ModelDragon)this.mainModel;
        this.addLayer(new LayerEnderDragonEyes(this));
        this.addLayer(new LayerEnderDragonDeath());
    }

    protected void func_180575_a(EntityDragon p_180575_1_, float p_180575_2_, float p_180575_3_, float p_180575_4_)
    {
        float var5 = (float)p_180575_1_.getMovementOffsets(7, p_180575_4_)[0];
        float var6 = (float)(p_180575_1_.getMovementOffsets(5, p_180575_4_)[1] - p_180575_1_.getMovementOffsets(10, p_180575_4_)[1]);
        GlStateManager.rotate(-var5, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var6 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 1.0F);

        if (p_180575_1_.deathTime > 0)
        {
            float var7 = ((float)p_180575_1_.deathTime + p_180575_4_ - 1.0F) / 20.0F * 1.6F;
            var7 = MathHelper.sqrt_float(var7);

            if (var7 > 1.0F)
            {
                var7 = 1.0F;
            }

            GlStateManager.rotate(var7 * this.getDeathMaxRotation(p_180575_1_), 0.0F, 0.0F, 1.0F);
        }
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityDragon p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
    {
        if (p_77036_1_.deathTicks > 0)
        {
            float var8 = (float)p_77036_1_.deathTicks / 200.0F;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, var8);
            this.bindTexture(enderDragonExplodingTextures);
            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.depthFunc(514);
        }

        this.bindEntityTexture(p_77036_1_);
        this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

        if (p_77036_1_.hurtTime > 0)
        {
            GlStateManager.depthFunc(514);
            GlStateManager.func_179090_x();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0F, 0.0F, 0.0F, 0.5F);
            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            GlStateManager.func_179098_w();
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityDragon p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        BossStatus.setBossStatus(p_76986_1_, false);
        super.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);

        if (p_76986_1_.healingEnderCrystal != null)
        {
            this.func_180574_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_9_);
        }
    }

    protected void func_180574_a(EntityDragon p_180574_1_, double p_180574_2_, double p_180574_4_, double p_180574_6_, float p_180574_8_)
    {
        float var9 = (float)p_180574_1_.healingEnderCrystal.innerRotation + p_180574_8_;
        float var10 = MathHelper.sin(var9 * 0.2F) / 2.0F + 0.5F;
        var10 = (var10 * var10 + var10) * 0.2F;
        float var11 = (float)(p_180574_1_.healingEnderCrystal.posX - p_180574_1_.posX - (p_180574_1_.prevPosX - p_180574_1_.posX) * (double)(1.0F - p_180574_8_));
        float var12 = (float)((double)var10 + p_180574_1_.healingEnderCrystal.posY - 1.0D - p_180574_1_.posY - (p_180574_1_.prevPosY - p_180574_1_.posY) * (double)(1.0F - p_180574_8_));
        float var13 = (float)(p_180574_1_.healingEnderCrystal.posZ - p_180574_1_.posZ - (p_180574_1_.prevPosZ - p_180574_1_.posZ) * (double)(1.0F - p_180574_8_));
        float var14 = MathHelper.sqrt_float(var11 * var11 + var13 * var13);
        float var15 = MathHelper.sqrt_float(var11 * var11 + var12 * var12 + var13 * var13);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180574_2_, (float)p_180574_4_ + 2.0F, (float)p_180574_6_);
        GlStateManager.rotate((float)(-Math.atan2((double)var13, (double)var11)) * 180.0F / (float)Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(-Math.atan2((double)var14, (double)var12)) * 180.0F / (float)Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
        Tessellator var16 = Tessellator.getInstance();
        WorldRenderer var17 = var16.getWorldRenderer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        this.bindTexture(enderDragonCrystalBeamTextures);
        GlStateManager.shadeModel(7425);
        float var18 = 0.0F - ((float)p_180574_1_.ticksExisted + p_180574_8_) * 0.01F;
        float var19 = MathHelper.sqrt_float(var11 * var11 + var12 * var12 + var13 * var13) / 32.0F - ((float)p_180574_1_.ticksExisted + p_180574_8_) * 0.01F;
        var17.startDrawing(5);
        byte var20 = 8;

        for (int var21 = 0; var21 <= var20; ++var21)
        {
            float var22 = MathHelper.sin((float)(var21 % var20) * (float)Math.PI * 2.0F / (float)var20) * 0.75F;
            float var23 = MathHelper.cos((float)(var21 % var20) * (float)Math.PI * 2.0F / (float)var20) * 0.75F;
            float var24 = (float)(var21 % var20) * 1.0F / (float)var20;
            var17.func_178991_c(0);
            var17.addVertexWithUV((double)(var22 * 0.2F), (double)(var23 * 0.2F), 0.0D, (double)var24, (double)var19);
            var17.func_178991_c(16777215);
            var17.addVertexWithUV((double)var22, (double)var23, (double)var15, (double)var24, (double)var18);
        }

        var16.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityDragon p_110775_1_)
    {
        return enderDragonTextures;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityDragon)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        this.func_180575_a((EntityDragon)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
    {
        this.renderModel((EntityDragon)p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityDragon)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityDragon)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityDragon)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
