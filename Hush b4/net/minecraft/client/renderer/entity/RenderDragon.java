// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.boss.EntityDragon;

public class RenderDragon extends RenderLiving<EntityDragon>
{
    private static final ResourceLocation enderDragonCrystalBeamTextures;
    private static final ResourceLocation enderDragonExplodingTextures;
    private static final ResourceLocation enderDragonTextures;
    protected ModelDragon modelDragon;
    
    static {
        enderDragonCrystalBeamTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
        enderDragonExplodingTextures = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
        enderDragonTextures = new ResourceLocation("textures/entity/enderdragon/dragon.png");
    }
    
    public RenderDragon(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelDragon(0.0f), 0.5f);
        this.modelDragon = (ModelDragon)this.mainModel;
        this.addLayer(new LayerEnderDragonEyes(this));
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerEnderDragonDeath());
    }
    
    private void addLayer(final LayerEnderDragonEyes layerEnderDragonEyes) {
    }
    
    @Override
    protected void rotateCorpse(final EntityDragon bat, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        final float f = (float)bat.getMovementOffsets(7, partialTicks)[0];
        final float f2 = (float)(bat.getMovementOffsets(5, partialTicks)[1] - bat.getMovementOffsets(10, partialTicks)[1]);
        GlStateManager.rotate(-f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (bat.deathTime > 0) {
            float f3 = (bat.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            f3 = MathHelper.sqrt_float(f3);
            if (f3 > 1.0f) {
                f3 = 1.0f;
            }
            GlStateManager.rotate(f3 * this.getDeathMaxRotation(bat), 0.0f, 0.0f, 1.0f);
        }
    }
    
    @Override
    protected void renderModel(final EntityDragon entitylivingbaseIn, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float p_77036_7_) {
        if (entitylivingbaseIn.deathTicks > 0) {
            final float f = entitylivingbaseIn.deathTicks / 200.0f;
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, f);
            this.bindTexture(RenderDragon.enderDragonExplodingTextures);
            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.depthFunc(514);
        }
        this.bindEntityTexture(entitylivingbaseIn);
        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
        if (entitylivingbaseIn.hurtTime > 0) {
            GlStateManager.depthFunc(514);
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 0.0f, 0.0f, 0.5f);
            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }
    
    @Override
    public void doRender(final EntityDragon entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        BossStatus.setBossStatus(entity, false);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.healingEnderCrystal != null) {
            this.drawRechargeRay(entity, x, y, z, partialTicks);
        }
    }
    
    protected void drawRechargeRay(final EntityDragon dragon, final double p_180574_2_, final double p_180574_4_, final double p_180574_6_, final float p_180574_8_) {
        final float f = dragon.healingEnderCrystal.innerRotation + p_180574_8_;
        float f2 = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        f2 = (f2 * f2 + f2) * 0.2f;
        final float f3 = (float)(dragon.healingEnderCrystal.posX - dragon.posX - (dragon.prevPosX - dragon.posX) * (1.0f - p_180574_8_));
        final float f4 = (float)(f2 + dragon.healingEnderCrystal.posY - 1.0 - dragon.posY - (dragon.prevPosY - dragon.posY) * (1.0f - p_180574_8_));
        final float f5 = (float)(dragon.healingEnderCrystal.posZ - dragon.posZ - (dragon.prevPosZ - dragon.posZ) * (1.0f - p_180574_8_));
        final float f6 = MathHelper.sqrt_float(f3 * f3 + f5 * f5);
        final float f7 = MathHelper.sqrt_float(f3 * f3 + f4 * f4 + f5 * f5);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180574_2_, (float)p_180574_4_ + 2.0f, (float)p_180574_6_);
        GlStateManager.rotate((float)(-Math.atan2(f5, f3)) * 180.0f / 3.1415927f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(-Math.atan2(f6, f4)) * 180.0f / 3.1415927f - 90.0f, 1.0f, 0.0f, 0.0f);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        this.bindTexture(RenderDragon.enderDragonCrystalBeamTextures);
        GlStateManager.shadeModel(7425);
        final float f8 = 0.0f - (dragon.ticksExisted + p_180574_8_) * 0.01f;
        final float f9 = MathHelper.sqrt_float(f3 * f3 + f4 * f4 + f5 * f5) / 32.0f - (dragon.ticksExisted + p_180574_8_) * 0.01f;
        worldrenderer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
        final int i = 8;
        for (int j = 0; j <= 8; ++j) {
            final float f10 = MathHelper.sin(j % 8 * 3.1415927f * 2.0f / 8.0f) * 0.75f;
            final float f11 = MathHelper.cos(j % 8 * 3.1415927f * 2.0f / 8.0f) * 0.75f;
            final float f12 = j % 8 * 1.0f / 8.0f;
            worldrenderer.pos(f10 * 0.2f, f11 * 0.2f, 0.0).tex(f12, f9).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos(f10, f11, f7).tex(f12, f8).color(255, 255, 255, 255).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityDragon entity) {
        return RenderDragon.enderDragonTextures;
    }
}
