package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityFireworkSparkFX extends EntityFX
{
    private int baseTextureIndex = 160;
    private boolean trail;
    private boolean twinkle;
    private final EffectRenderer field_92047_az;
    private float fadeColourRed;
    private float fadeColourGreen;
    private float fadeColourBlue;
    private boolean hasFadeColour;

    public EntityFireworkSparkFX(World worldIn, double posXIn, double posYIn, double posZIn, double p_i46356_8_, double p_i46356_10_, double p_i46356_12_, EffectRenderer p_i46356_14_)
    {
        super(worldIn, posXIn, posYIn, posZIn);
        this.motionX = p_i46356_8_;
        this.motionY = p_i46356_10_;
        this.motionZ = p_i46356_12_;
        this.field_92047_az = p_i46356_14_;
        this.particleScale *= 0.75F;
        this.particleMaxAge = 48 + this.rand.nextInt(12);
        this.noClip = false;
    }

    public void setTrail(boolean trailIn)
    {
        this.trail = trailIn;
    }

    public void setTwinkle(boolean twinkleIn)
    {
        this.twinkle = twinkleIn;
    }

    public void setColour(int colour)
    {
        float var2 = ((colour & 16711680) >> 16) / 255.0F;
        float var3 = ((colour & 65280) >> 8) / 255.0F;
        float var4 = ((colour & 255) >> 0) / 255.0F;
        float var5 = 1.0F;
        this.setRBGColorF(var2 * var5, var3 * var5, var4 * var5);
    }

    public void setFadeColour(int faceColour)
    {
        this.fadeColourRed = ((faceColour & 16711680) >> 16) / 255.0F;
        this.fadeColourGreen = ((faceColour & 65280) >> 8) / 255.0F;
        this.fadeColourBlue = ((faceColour & 255) >> 0) / 255.0F;
        this.hasFadeColour = true;
    }

    /**
     * returns the bounding box for this entity
     */
    @Override
	public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
	public boolean canBePushed()
    {
        return false;
    }

    /**
     * Renders the particle
     *  
     * @param worldRendererIn The WorldRenderer instance
     */
    @Override
	public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        if (!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0)
        {
            super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        if (this.particleAge > this.particleMaxAge / 2)
        {
            this.setAlphaF(1.0F - ((float)this.particleAge - (float)(this.particleMaxAge / 2)) / this.particleMaxAge);

            if (this.hasFadeColour)
            {
                this.particleRed += (this.fadeColourRed - this.particleRed) * 0.2F;
                this.particleGreen += (this.fadeColourGreen - this.particleGreen) * 0.2F;
                this.particleBlue += (this.fadeColourBlue - this.particleBlue) * 0.2F;
            }
        }

        this.setParticleTextureIndex(this.baseTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY -= 0.004D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9100000262260437D;
        this.motionY *= 0.9100000262260437D;
        this.motionZ *= 0.9100000262260437D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        if (this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0)
        {
            EntityFireworkSparkFX var1 = new EntityFireworkSparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, this.field_92047_az);
            var1.setAlphaF(0.99F);
            var1.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
            var1.particleAge = var1.particleMaxAge / 2;

            if (this.hasFadeColour)
            {
                var1.hasFadeColour = true;
                var1.fadeColourRed = this.fadeColourRed;
                var1.fadeColourGreen = this.fadeColourGreen;
                var1.fadeColourBlue = this.fadeColourBlue;
            }

            var1.twinkle = this.twinkle;
            this.field_92047_az.addEffect(var1);
        }
    }

    @Override
	public int getBrightnessForRender(float partialTicks)
    {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    @Override
	public float getBrightness(float partialTicks)
    {
        return 1.0F;
    }
}
