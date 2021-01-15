package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityEnchantmentTableParticleFX extends EntityFX
{
    private float field_70565_a;
    private double field_70568_aq;
    private double field_70567_ar;
    private double field_70566_as;
    private static final String __OBFID = "CL_00000902";

    protected EntityEnchantmentTableParticleFX(World worldIn, double p_i1204_2_, double p_i1204_4_, double p_i1204_6_, double p_i1204_8_, double p_i1204_10_, double p_i1204_12_)
    {
        super(worldIn, p_i1204_2_, p_i1204_4_, p_i1204_6_, p_i1204_8_, p_i1204_10_, p_i1204_12_);
        this.motionX = p_i1204_8_;
        this.motionY = p_i1204_10_;
        this.motionZ = p_i1204_12_;
        this.field_70568_aq = p_i1204_2_;
        this.field_70567_ar = p_i1204_4_;
        this.field_70566_as = p_i1204_6_;
        this.posX = this.prevPosX = p_i1204_2_ + p_i1204_8_;
        this.posY = this.prevPosY = p_i1204_4_ + p_i1204_10_;
        this.posZ = this.prevPosZ = p_i1204_6_ + p_i1204_12_;
        float var14 = this.rand.nextFloat() * 0.6F + 0.4F;
        this.field_70565_a = this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F * var14;
        this.particleGreen *= 0.9F;
        this.particleRed *= 0.9F;
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 30;
        this.noClip = true;
        this.setParticleTextureIndex((int)(Math.random() * 26.0D + 1.0D + 224.0D));
    }

    public int getBrightnessForRender(float p_70070_1_)
    {
        int var2 = super.getBrightnessForRender(p_70070_1_);
        float var3 = (float)this.particleAge / (float)this.particleMaxAge;
        var3 *= var3;
        var3 *= var3;
        int var4 = var2 & 255;
        int var5 = var2 >> 16 & 255;
        var5 += (int)(var3 * 15.0F * 16.0F);

        if (var5 > 240)
        {
            var5 = 240;
        }

        return var4 | var5 << 16;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_)
    {
        float var2 = super.getBrightness(p_70013_1_);
        float var3 = (float)this.particleAge / (float)this.particleMaxAge;
        var3 *= var3;
        var3 *= var3;
        return var2 * (1.0F - var3) + var3;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float var1 = (float)this.particleAge / (float)this.particleMaxAge;
        var1 = 1.0F - var1;
        float var2 = 1.0F - var1;
        var2 *= var2;
        var2 *= var2;
        this.posX = this.field_70568_aq + this.motionX * (double)var1;
        this.posY = this.field_70567_ar + this.motionY * (double)var1 - (double)(var2 * 1.2F);
        this.posZ = this.field_70566_as + this.motionZ * (double)var1;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }
    }

    public static class EnchantmentTable implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002605";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
        {
            return new EntityEnchantmentTableParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
