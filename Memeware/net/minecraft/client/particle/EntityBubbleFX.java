package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityBubbleFX extends EntityFX {
    private static final String __OBFID = "CL_00000898";

    protected EntityBubbleFX(World worldIn, double p_i1198_2_, double p_i1198_4_, double p_i1198_6_, double p_i1198_8_, double p_i1198_10_, double p_i1198_12_) {
        super(worldIn, p_i1198_2_, p_i1198_4_, p_i1198_6_, p_i1198_8_, p_i1198_10_, p_i1198_12_);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setParticleTextureIndex(32);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
        this.motionX = p_i1198_8_ * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        this.motionY = p_i1198_10_ * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        this.motionZ = p_i1198_12_ * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY += 0.002D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.8500000238418579D;
        this.motionY *= 0.8500000238418579D;
        this.motionZ *= 0.8500000238418579D;

        if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() != Material.water) {
            this.setDead();
        }

        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }

    public static class Factory implements IParticleFactory {
        private static final String __OBFID = "CL_00002610";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            return new EntityBubbleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
