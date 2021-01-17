// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityHugeExplodeFX extends EntityFX
{
    private int timeSinceStart;
    private int maximumTime;
    
    protected EntityHugeExplodeFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i1214_8_, final double p_i1214_10_, final double p_i1214_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.maximumTime = 8;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
    }
    
    @Override
    public void onUpdate() {
        for (int i = 0; i < 6; ++i) {
            final double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            final double d2 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            final double d3 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d2, d3, this.timeSinceStart / (float)this.maximumTime, 0.0, 0.0, new int[0]);
        }
        ++this.timeSinceStart;
        if (this.timeSinceStart == this.maximumTime) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntityHugeExplodeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
