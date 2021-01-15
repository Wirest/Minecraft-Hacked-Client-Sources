package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntitySuspendFX extends EntityFX
{
    private static final String __OBFID = "CL_00000928";

    protected EntitySuspendFX(World worldIn, double p_i1231_2_, double p_i1231_4_, double p_i1231_6_, double p_i1231_8_, double p_i1231_10_, double p_i1231_12_)
    {
        super(worldIn, p_i1231_2_, p_i1231_4_ - 0.125D, p_i1231_6_, p_i1231_8_, p_i1231_10_, p_i1231_12_);
        this.particleRed = 0.4F;
        this.particleGreen = 0.4F;
        this.particleBlue = 0.7F;
        this.setParticleTextureIndex(0);
        this.setSize(0.01F, 0.01F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
        this.motionX = p_i1231_8_ * 0.0D;
        this.motionY = p_i1231_10_ * 0.0D;
        this.motionZ = p_i1231_12_ * 0.0D;
        this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() != Material.water)
        {
            this.setDead();
        }

        if (this.particleMaxAge-- <= 0)
        {
            this.setDead();
        }
    }

    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002579";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
        {
            return new EntitySuspendFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
