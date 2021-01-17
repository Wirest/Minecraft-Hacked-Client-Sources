// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityBreakingFX extends EntityFX
{
    protected EntityBreakingFX(final World worldIn, final double posXIn, final double posYIn, final double posZIn, final Item p_i1195_8_) {
        this(worldIn, posXIn, posYIn, posZIn, p_i1195_8_, 0);
    }
    
    protected EntityBreakingFX(final World worldIn, final double posXIn, final double posYIn, final double posZIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final Item p_i1197_14_, final int p_i1197_15_) {
        this(worldIn, posXIn, posYIn, posZIn, p_i1197_14_, p_i1197_15_);
        this.motionX *= 0.10000000149011612;
        this.motionY *= 0.10000000149011612;
        this.motionZ *= 0.10000000149011612;
        this.motionX += xSpeedIn;
        this.motionY += ySpeedIn;
        this.motionZ += zSpeedIn;
    }
    
    protected EntityBreakingFX(final World worldIn, final double posXIn, final double posYIn, final double posZIn, final Item p_i1196_8_, final int p_i1196_9_) {
        super(worldIn, posXIn, posYIn, posZIn, 0.0, 0.0, 0.0);
        this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i1196_8_, p_i1196_9_));
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleGravity = Blocks.snow.blockParticleGravity;
        this.particleScale /= 2.0f;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float f2 = f + 0.015609375f;
        float f3 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float f4 = f3 + 0.015609375f;
        final float f5 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
            f2 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            f3 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            f4 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - EntityBreakingFX.interpPosX);
        final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - EntityBreakingFX.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - EntityBreakingFX.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        worldRendererIn.pos(f6 - p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 - p_180434_6_ * f5 - p_180434_8_ * f5).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 - p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 - p_180434_6_ * f5 + p_180434_8_ * f5).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 + p_180434_6_ * f5 + p_180434_8_ * f5).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 + p_180434_6_ * f5 - p_180434_8_ * f5).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final int i = (p_178902_15_.length > 1) ? p_178902_15_[1] : 0;
            return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Item.getItemById(p_178902_15_[0]), i);
        }
    }
    
    public static class SlimeFactory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.slime_ball);
        }
    }
    
    public static class SnowballFactory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.snowball);
        }
    }
}
