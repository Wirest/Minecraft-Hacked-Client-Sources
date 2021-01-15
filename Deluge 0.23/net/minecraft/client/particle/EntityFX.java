package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFX extends Entity
{
    protected int particleTextureIndexX;
    protected int particleTextureIndexY;
    protected float particleTextureJitterX;
    protected float particleTextureJitterY;
    protected int particleAge;
    protected int particleMaxAge;
    protected float particleScale;
    protected float particleGravity;

    /** The red amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0. */
    protected float particleRed;

    /**
     * The green amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
     */
    protected float particleGreen;

    /**
     * The blue amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
     */
    protected float particleBlue;

    /** Particle alpha */
    protected float particleAlpha;

    /** The icon field from which the given particle pulls its texture. */
    protected TextureAtlasSprite particleIcon;
    public static double interpPosX;
    public static double interpPosY;
    public static double interpPosZ;
    private static final String __OBFID = "CL_00000914";

    protected EntityFX(World worldIn, double p_i46352_2_, double p_i46352_4_, double p_i46352_6_)
    {
        super(worldIn);
        this.particleAlpha = 1.0F;
        this.setSize(0.2F, 0.2F);
        this.setPosition(p_i46352_2_, p_i46352_4_, p_i46352_6_);
        this.lastTickPosX = p_i46352_2_;
        this.lastTickPosY = p_i46352_4_;
        this.lastTickPosZ = p_i46352_6_;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
        this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
        this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
        this.particleAge = 0;
    }

    public EntityFX(World worldIn, double p_i1219_2_, double p_i1219_4_, double p_i1219_6_, double p_i1219_8_, double p_i1219_10_, double p_i1219_12_)
    {
        this(worldIn, p_i1219_2_, p_i1219_4_, p_i1219_6_);
        this.motionX = p_i1219_8_ + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionY = p_i1219_10_ + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionZ = p_i1219_12_ + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        float var14 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
        float var15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / (double)var15 * (double)var14 * 0.4000000059604645D;
        this.motionY = this.motionY / (double)var15 * (double)var14 * 0.4000000059604645D + 0.10000000149011612D;
        this.motionZ = this.motionZ / (double)var15 * (double)var14 * 0.4000000059604645D;
    }

    public EntityFX multiplyVelocity(float p_70543_1_)
    {
        this.motionX *= (double)p_70543_1_;
        this.motionY = (this.motionY - 0.10000000149011612D) * (double)p_70543_1_ + 0.10000000149011612D;
        this.motionZ *= (double)p_70543_1_;
        return this;
    }

    public EntityFX multipleParticleScaleBy(float p_70541_1_)
    {
        this.setSize(0.2F * p_70541_1_, 0.2F * p_70541_1_);
        this.particleScale *= p_70541_1_;
        return this;
    }

    public void setRBGColorF(float p_70538_1_, float p_70538_2_, float p_70538_3_)
    {
        this.particleRed = p_70538_1_;
        this.particleGreen = p_70538_2_;
        this.particleBlue = p_70538_3_;
    }

    /**
     * Sets the particle alpha (float)
     */
    public void setAlphaF(float p_82338_1_)
    {
        if (this.particleAlpha == 1.0F && p_82338_1_ < 1.0F)
        {
            Minecraft.getMinecraft().effectRenderer.func_178928_b(this);
        }
        else if (this.particleAlpha < 1.0F && p_82338_1_ == 1.0F)
        {
            Minecraft.getMinecraft().effectRenderer.func_178931_c(this);
        }

        this.particleAlpha = p_82338_1_;
    }

    public float getRedColorF()
    {
        return this.particleRed;
    }

    public float getGreenColorF()
    {
        return this.particleGreen;
    }

    public float getBlueColorF()
    {
        return this.particleBlue;
    }

    public float func_174838_j()
    {
        return this.particleAlpha;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {}

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.motionY -= 0.04D * (double)this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float var9 = (float)this.particleTextureIndexX / 16.0F;
        float var10 = var9 + 0.0624375F;
        float var11 = (float)this.particleTextureIndexY / 16.0F;
        float var12 = var11 + 0.0624375F;
        float var13 = 0.1F * this.particleScale;

        if (this.particleIcon != null)
        {
            var9 = this.particleIcon.getMinU();
            var10 = this.particleIcon.getMaxU();
            var11 = this.particleIcon.getMinV();
            var12 = this.particleIcon.getMaxV();
        }

        float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - interpPosX);
        float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - interpPosY);
        float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - interpPosZ);
        p_180434_1_.func_178960_a(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        p_180434_1_.addVertexWithUV((double)(var14 - p_180434_4_ * var13 - p_180434_7_ * var13), (double)(var15 - p_180434_5_ * var13), (double)(var16 - p_180434_6_ * var13 - p_180434_8_ * var13), (double)var10, (double)var12);
        p_180434_1_.addVertexWithUV((double)(var14 - p_180434_4_ * var13 + p_180434_7_ * var13), (double)(var15 + p_180434_5_ * var13), (double)(var16 - p_180434_6_ * var13 + p_180434_8_ * var13), (double)var10, (double)var11);
        p_180434_1_.addVertexWithUV((double)(var14 + p_180434_4_ * var13 + p_180434_7_ * var13), (double)(var15 + p_180434_5_ * var13), (double)(var16 + p_180434_6_ * var13 + p_180434_8_ * var13), (double)var9, (double)var11);
        p_180434_1_.addVertexWithUV((double)(var14 + p_180434_4_ * var13 - p_180434_7_ * var13), (double)(var15 - p_180434_5_ * var13), (double)(var16 + p_180434_6_ * var13 - p_180434_8_ * var13), (double)var9, (double)var12);
    }

    public int getFXLayer()
    {
        return 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {}

    public void func_180435_a(TextureAtlasSprite p_180435_1_)
    {
        int var2 = this.getFXLayer();

        if (var2 == 1)
        {
            this.particleIcon = p_180435_1_;
        }
        else
        {
            throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
        }
    }

    /**
     * Public method to set private field particleTextureIndex.
     */
    public void setParticleTextureIndex(int p_70536_1_)
    {
        if (this.getFXLayer() != 0)
        {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        else
        {
            this.particleTextureIndexX = p_70536_1_ % 16;
            this.particleTextureIndexY = p_70536_1_ / 16;
        }
    }

    public void nextTextureIndexX()
    {
        ++this.particleTextureIndexX;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
    }
}
