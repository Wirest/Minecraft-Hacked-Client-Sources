package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntitySplashFX extends EntityRainFX {
    private static final String __OBFID = "CL_00000927";

    protected EntitySplashFX(World worldIn, double p_i1230_2_, double p_i1230_4_, double p_i1230_6_, double p_i1230_8_, double p_i1230_10_, double p_i1230_12_) {
        super(worldIn, p_i1230_2_, p_i1230_4_, p_i1230_6_);
        this.particleGravity = 0.04F;
        this.nextTextureIndexX();

        if (p_i1230_10_ == 0.0D && (p_i1230_8_ != 0.0D || p_i1230_12_ != 0.0D)) {
            this.motionX = p_i1230_8_;
            this.motionY = p_i1230_10_ + 0.1D;
            this.motionZ = p_i1230_12_;
        }
    }

    public static class Factory implements IParticleFactory {
        private static final String __OBFID = "CL_00002580";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            return new EntitySplashFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
