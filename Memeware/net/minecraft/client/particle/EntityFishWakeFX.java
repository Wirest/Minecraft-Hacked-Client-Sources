package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityFishWakeFX extends EntityFX {
    private static final String __OBFID = "CL_00000933";

    protected EntityFishWakeFX(World worldIn, double p_i45073_2_, double p_i45073_4_, double p_i45073_6_, double p_i45073_8_, double p_i45073_10_, double p_i45073_12_) {
        super(worldIn, p_i45073_2_, p_i45073_4_, p_i45073_6_, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.30000001192092896D;
        this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
        this.motionZ *= 0.30000001192092896D;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setParticleTextureIndex(19);
        this.setSize(0.01F, 0.01F);
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleGravity = 0.0F;
        this.motionX = p_i45073_8_;
        this.motionY = p_i45073_10_;
        this.motionZ = p_i45073_12_;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= (double) this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        int var1 = 60 - this.particleMaxAge;
        float var2 = (float) var1 * 0.001F;
        this.setSize(var2, var2);
        this.setParticleTextureIndex(19 + var1 % 4);

        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }

    public static class Factory implements IParticleFactory {
        private static final String __OBFID = "CL_00002573";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            return new EntityFishWakeFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
