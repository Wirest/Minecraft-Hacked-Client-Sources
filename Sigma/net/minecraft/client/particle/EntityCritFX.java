package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityCritFX extends EntitySmokeFX {
    private static final String __OBFID = "CL_00000900";

    protected EntityCritFX(World worldIn, double p_i1201_2_, double p_i1201_4_, double p_i1201_6_, double p_i1201_8_, double p_i1201_10_, double p_i1201_12_) {
        super(worldIn, p_i1201_2_, p_i1201_4_, p_i1201_6_, p_i1201_8_, p_i1201_10_, p_i1201_12_, 2.5F);
    }

    public static class Factory implements IParticleFactory {
        private static final String __OBFID = "CL_00002596";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            return new EntityCritFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
