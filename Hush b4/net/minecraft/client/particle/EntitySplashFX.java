// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntitySplashFX extends EntityRainFX
{
    protected EntitySplashFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleGravity = 0.04f;
        this.nextTextureIndexX();
        if (ySpeedIn == 0.0 && (xSpeedIn != 0.0 || zSpeedIn != 0.0)) {
            this.motionX = xSpeedIn;
            this.motionY = ySpeedIn + 0.1;
            this.motionZ = zSpeedIn;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new EntitySplashFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
