// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class EntityLargeExplodeFX extends EntityFX
{
    private static final ResourceLocation EXPLOSION_TEXTURE;
    private static final VertexFormat field_181549_az;
    private int field_70581_a;
    private int field_70584_aq;
    private TextureManager theRenderEngine;
    private float field_70582_as;
    
    static {
        EXPLOSION_TEXTURE = new ResourceLocation("textures/entity/explosion.png");
        field_181549_az = new VertexFormat().func_181721_a(DefaultVertexFormats.POSITION_3F).func_181721_a(DefaultVertexFormats.TEX_2F).func_181721_a(DefaultVertexFormats.COLOR_4UB).func_181721_a(DefaultVertexFormats.TEX_2S).func_181721_a(DefaultVertexFormats.NORMAL_3B).func_181721_a(DefaultVertexFormats.PADDING_1B);
    }
    
    protected EntityLargeExplodeFX(final TextureManager renderEngine, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i1213_9_, final double p_i1213_11_, final double p_i1213_13_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.theRenderEngine = renderEngine;
        this.field_70584_aq = 6 + this.rand.nextInt(4);
        final float particleRed = this.rand.nextFloat() * 0.6f + 0.4f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.field_70582_as = 1.0f - (float)p_i1213_9_ * 0.5f;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final int i = (int)((this.field_70581_a + partialTicks) * 15.0f / this.field_70584_aq);
        if (i <= 15) {
            this.theRenderEngine.bindTexture(EntityLargeExplodeFX.EXPLOSION_TEXTURE);
            final float f = i % 4 / 4.0f;
            final float f2 = f + 0.24975f;
            final float f3 = i / 4 / 4.0f;
            final float f4 = f3 + 0.24975f;
            final float f5 = 2.0f * this.field_70582_as;
            final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - EntityLargeExplodeFX.interpPosX);
            final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - EntityLargeExplodeFX.interpPosY);
            final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - EntityLargeExplodeFX.interpPosZ);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            RenderHelper.disableStandardItemLighting();
            worldRendererIn.begin(7, EntityLargeExplodeFX.field_181549_az);
            worldRendererIn.pos(f6 - p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 - p_180434_6_ * f5 - p_180434_8_ * f5).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRendererIn.pos(f6 - p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 - p_180434_6_ * f5 + p_180434_8_ * f5).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRendererIn.pos(f6 + p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 + p_180434_6_ * f5 + p_180434_8_ * f5).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRendererIn.pos(f6 + p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 + p_180434_6_ * f5 - p_180434_8_ * f5).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.enableLighting();
        }
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        return 61680;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.field_70581_a;
        if (this.field_70581_a == this.field_70584_aq) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
