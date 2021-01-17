// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;

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
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    protected float particleAlpha;
    protected TextureAtlasSprite particleIcon;
    public static double interpPosX;
    public static double interpPosY;
    public static double interpPosZ;
    
    protected EntityFX(final World worldIn, final double posXIn, final double posYIn, final double posZIn) {
        super(worldIn);
        this.particleAlpha = 1.0f;
        this.setSize(0.2f, 0.2f);
        this.setPosition(posXIn, posYIn, posZIn);
        this.prevPosX = posXIn;
        this.lastTickPosX = posXIn;
        this.prevPosY = posYIn;
        this.lastTickPosY = posYIn;
        this.prevPosZ = posZIn;
        this.lastTickPosZ = posZIn;
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleTextureJitterX = this.rand.nextFloat() * 3.0f;
        this.particleTextureJitterY = this.rand.nextFloat() * 3.0f;
        this.particleScale = (this.rand.nextFloat() * 0.5f + 0.5f) * 2.0f;
        this.particleMaxAge = (int)(4.0f / (this.rand.nextFloat() * 0.9f + 0.1f));
        this.particleAge = 0;
    }
    
    public EntityFX(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.motionX = xSpeedIn + (Math.random() * 2.0 - 1.0) * 0.4000000059604645;
        this.motionY = ySpeedIn + (Math.random() * 2.0 - 1.0) * 0.4000000059604645;
        this.motionZ = zSpeedIn + (Math.random() * 2.0 - 1.0) * 0.4000000059604645;
        final float f = (float)(Math.random() + Math.random() + 1.0) * 0.15f;
        final float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / f2 * f * 0.4000000059604645;
        this.motionY = this.motionY / f2 * f * 0.4000000059604645 + 0.10000000149011612;
        this.motionZ = this.motionZ / f2 * f * 0.4000000059604645;
    }
    
    public EntityFX multiplyVelocity(final float multiplier) {
        this.motionX *= multiplier;
        this.motionY = (this.motionY - 0.10000000149011612) * multiplier + 0.10000000149011612;
        this.motionZ *= multiplier;
        return this;
    }
    
    public EntityFX multipleParticleScaleBy(final float p_70541_1_) {
        this.setSize(0.2f * p_70541_1_, 0.2f * p_70541_1_);
        this.particleScale *= p_70541_1_;
        return this;
    }
    
    public void setRBGColorF(final float particleRedIn, final float particleGreenIn, final float particleBlueIn) {
        this.particleRed = particleRedIn;
        this.particleGreen = particleGreenIn;
        this.particleBlue = particleBlueIn;
    }
    
    public void setAlphaF(final float alpha) {
        if (this.particleAlpha == 1.0f && alpha < 1.0f) {
            Minecraft.getMinecraft().effectRenderer.moveToAlphaLayer(this);
        }
        else if (this.particleAlpha < 1.0f && alpha == 1.0f) {
            Minecraft.getMinecraft().effectRenderer.moveToNoAlphaLayer(this);
        }
        this.particleAlpha = alpha;
    }
    
    public float getRedColorF() {
        return this.particleRed;
    }
    
    public float getGreenColorF() {
        return this.particleGreen;
    }
    
    public float getBlueColorF() {
        return this.particleBlue;
    }
    
    public float getAlpha() {
        return this.particleAlpha;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.motionY -= 0.04 * this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float f = this.particleTextureIndexX / 16.0f;
        float f2 = f + 0.0624375f;
        float f3 = this.particleTextureIndexY / 16.0f;
        float f4 = f3 + 0.0624375f;
        final float f5 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f = this.particleIcon.getMinU();
            f2 = this.particleIcon.getMaxU();
            f3 = this.particleIcon.getMinV();
            f4 = this.particleIcon.getMaxV();
        }
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - EntityFX.interpPosX);
        final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - EntityFX.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - EntityFX.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        worldRendererIn.pos(f6 - p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 - p_180434_6_ * f5 - p_180434_8_ * f5).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 - p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 - p_180434_6_ * f5 + p_180434_8_ * f5).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 + p_180434_6_ * f5 + p_180434_8_ * f5).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos(f6 + p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 + p_180434_6_ * f5 - p_180434_8_ * f5).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }
    
    public int getFXLayer() {
        return 0;
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    public void setParticleIcon(final TextureAtlasSprite icon) {
        final int i = this.getFXLayer();
        if (i == 1) {
            this.particleIcon = icon;
            return;
        }
        throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
    }
    
    public void setParticleTextureIndex(final int particleTextureIndex) {
        if (this.getFXLayer() != 0) {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        this.particleTextureIndexX = particleTextureIndex % 16;
        this.particleTextureIndexY = particleTextureIndex / 16;
    }
    
    public void nextTextureIndexX() {
        ++this.particleTextureIndexX;
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
    }
}
