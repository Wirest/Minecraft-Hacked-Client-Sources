// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier extends EntityFX
{
    protected Barrier(final World worldIn, final double p_i46286_2_, final double p_i46286_4_, final double p_i46286_6_, final Item p_i46286_8_) {
        super(worldIn, p_i46286_2_, p_i46286_4_, p_i46286_6_, 0.0, 0.0, 0.0);
        this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i46286_8_));
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 80;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final float f = this.particleIcon.getMinU();
        final float f2 = this.particleIcon.getMaxU();
        final float f3 = this.particleIcon.getMinV();
        final float f4 = this.particleIcon.getMaxV();
        final float f5 = 0.5f;
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - Barrier.interpPosX);
        final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - Barrier.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - Barrier.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        worldRendererIn.pos(f6 - p_180434_4_ * 0.5f - p_180434_7_ * 0.5f, f7 - p_180434_5_ * 0.5f, f8 - p_180434_6_ * 0.5f - p_180434_8_ * 0.5f).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 - p_180434_4_ * 0.5f + p_180434_7_ * 0.5f, f7 + p_180434_5_ * 0.5f, f8 - p_180434_6_ * 0.5f + p_180434_8_ * 0.5f).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + p_180434_4_ * 0.5f + p_180434_7_ * 0.5f, f7 + p_180434_5_ * 0.5f, f8 + p_180434_6_ * 0.5f + p_180434_8_ * 0.5f).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + p_180434_4_ * 0.5f - p_180434_7_ * 0.5f, f7 - p_180434_5_ * 0.5f, f8 + p_180434_6_ * 0.5f - p_180434_8_ * 0.5f).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(j, k).endVertex();
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new Barrier(worldIn, xCoordIn, yCoordIn, zCoordIn, Item.getItemFromBlock(Blocks.barrier));
        }
    }
}
