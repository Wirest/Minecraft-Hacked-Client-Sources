package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class EntityBlockDustFX extends EntityDiggingFX {
    private static final String __OBFID = "CL_00000931";

    protected EntityBlockDustFX(World worldIn, double p_i46281_2_, double p_i46281_4_, double p_i46281_6_, double p_i46281_8_, double p_i46281_10_, double p_i46281_12_, IBlockState p_i46281_14_) {
        super(worldIn, p_i46281_2_, p_i46281_4_, p_i46281_6_, p_i46281_8_, p_i46281_10_, p_i46281_12_, p_i46281_14_);
        this.motionX = p_i46281_8_;
        this.motionY = p_i46281_10_;
        this.motionZ = p_i46281_12_;
    }

    public static class Factory implements IParticleFactory {
        private static final String __OBFID = "CL_00002576";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            IBlockState var16 = Block.getStateById(p_178902_15_[0]);
            return var16.getBlock().getRenderType() == -1 ? null : (new EntityBlockDustFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, var16)).func_174845_l();
        }
    }
}
