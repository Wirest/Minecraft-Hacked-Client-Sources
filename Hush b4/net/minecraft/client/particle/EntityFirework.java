// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.item.ItemDye;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class EntityFirework
{
    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            final SparkFX entityfirework$sparkfx = new SparkFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Minecraft.getMinecraft().effectRenderer);
            entityfirework$sparkfx.setAlphaF(0.99f);
            return entityfirework$sparkfx;
        }
    }
    
    public static class OverlayFX extends EntityFX
    {
        protected OverlayFX(final World p_i46466_1_, final double p_i46466_2_, final double p_i46466_4_, final double p_i46466_6_) {
            super(p_i46466_1_, p_i46466_2_, p_i46466_4_, p_i46466_6_);
            this.particleMaxAge = 4;
        }
        
        @Override
        public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
            final float f = 0.25f;
            final float f2 = 0.5f;
            final float f3 = 0.125f;
            final float f4 = 0.375f;
            final float f5 = 7.1f * MathHelper.sin((this.particleAge + partialTicks - 1.0f) * 0.25f * 3.1415927f);
            this.particleAlpha = 0.6f - (this.particleAge + partialTicks - 1.0f) * 0.25f * 0.5f;
            final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - OverlayFX.interpPosX);
            final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - OverlayFX.interpPosY);
            final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - OverlayFX.interpPosZ);
            final int i = this.getBrightnessForRender(partialTicks);
            final int j = i >> 16 & 0xFFFF;
            final int k = i & 0xFFFF;
            worldRendererIn.pos(f6 - p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 - p_180434_6_ * f5 - p_180434_8_ * f5).tex(0.5, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            worldRendererIn.pos(f6 - p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 - p_180434_6_ * f5 + p_180434_8_ * f5).tex(0.5, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            worldRendererIn.pos(f6 + p_180434_4_ * f5 + p_180434_7_ * f5, f7 + p_180434_5_ * f5, f8 + p_180434_6_ * f5 + p_180434_8_ * f5).tex(0.25, 0.125).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            worldRendererIn.pos(f6 + p_180434_4_ * f5 - p_180434_7_ * f5, f7 - p_180434_5_ * f5, f8 + p_180434_6_ * f5 - p_180434_8_ * f5).tex(0.25, 0.375).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        }
    }
    
    public static class SparkFX extends EntityFX
    {
        private int baseTextureIndex;
        private boolean trail;
        private boolean twinkle;
        private final EffectRenderer field_92047_az;
        private float fadeColourRed;
        private float fadeColourGreen;
        private float fadeColourBlue;
        private boolean hasFadeColour;
        
        public SparkFX(final World p_i46465_1_, final double p_i46465_2_, final double p_i46465_4_, final double p_i46465_6_, final double p_i46465_8_, final double p_i46465_10_, final double p_i46465_12_, final EffectRenderer p_i46465_14_) {
            super(p_i46465_1_, p_i46465_2_, p_i46465_4_, p_i46465_6_);
            this.baseTextureIndex = 160;
            this.motionX = p_i46465_8_;
            this.motionY = p_i46465_10_;
            this.motionZ = p_i46465_12_;
            this.field_92047_az = p_i46465_14_;
            this.particleScale *= 0.75f;
            this.particleMaxAge = 48 + this.rand.nextInt(12);
            this.noClip = false;
        }
        
        public void setTrail(final boolean trailIn) {
            this.trail = trailIn;
        }
        
        public void setTwinkle(final boolean twinkleIn) {
            this.twinkle = twinkleIn;
        }
        
        public void setColour(final int colour) {
            final float f = ((colour & 0xFF0000) >> 16) / 255.0f;
            final float f2 = ((colour & 0xFF00) >> 8) / 255.0f;
            final float f3 = ((colour & 0xFF) >> 0) / 255.0f;
            final float f4 = 1.0f;
            this.setRBGColorF(f * f4, f2 * f4, f3 * f4);
        }
        
        public void setFadeColour(final int faceColour) {
            this.fadeColourRed = ((faceColour & 0xFF0000) >> 16) / 255.0f;
            this.fadeColourGreen = ((faceColour & 0xFF00) >> 8) / 255.0f;
            this.fadeColourBlue = ((faceColour & 0xFF) >> 0) / 255.0f;
            this.hasFadeColour = true;
        }
        
        @Override
        public AxisAlignedBB getCollisionBoundingBox() {
            return null;
        }
        
        @Override
        public boolean canBePushed() {
            return false;
        }
        
        @Override
        public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
            if (!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
                super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
            }
        }
        
        @Override
        public void onUpdate() {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (this.particleAge++ >= this.particleMaxAge) {
                this.setDead();
            }
            if (this.particleAge > this.particleMaxAge / 2) {
                this.setAlphaF(1.0f - (this.particleAge - (float)(this.particleMaxAge / 2)) / this.particleMaxAge);
                if (this.hasFadeColour) {
                    this.particleRed += (this.fadeColourRed - this.particleRed) * 0.2f;
                    this.particleGreen += (this.fadeColourGreen - this.particleGreen) * 0.2f;
                    this.particleBlue += (this.fadeColourBlue - this.particleBlue) * 0.2f;
                }
            }
            this.setParticleTextureIndex(this.baseTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
            this.motionY -= 0.004;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9100000262260437;
            this.motionY *= 0.9100000262260437;
            this.motionZ *= 0.9100000262260437;
            if (this.onGround) {
                this.motionX *= 0.699999988079071;
                this.motionZ *= 0.699999988079071;
            }
            if (this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
                final SparkFX entityfirework$sparkfx = new SparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, this.field_92047_az);
                entityfirework$sparkfx.setAlphaF(0.99f);
                entityfirework$sparkfx.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
                entityfirework$sparkfx.particleAge = entityfirework$sparkfx.particleMaxAge / 2;
                if (this.hasFadeColour) {
                    entityfirework$sparkfx.hasFadeColour = true;
                    entityfirework$sparkfx.fadeColourRed = this.fadeColourRed;
                    entityfirework$sparkfx.fadeColourGreen = this.fadeColourGreen;
                    entityfirework$sparkfx.fadeColourBlue = this.fadeColourBlue;
                }
                entityfirework$sparkfx.twinkle = this.twinkle;
                this.field_92047_az.addEffect(entityfirework$sparkfx);
            }
        }
        
        @Override
        public int getBrightnessForRender(final float partialTicks) {
            return 15728880;
        }
        
        @Override
        public float getBrightness(final float partialTicks) {
            return 1.0f;
        }
    }
    
    public static class StarterFX extends EntityFX
    {
        private int fireworkAge;
        private final EffectRenderer theEffectRenderer;
        private NBTTagList fireworkExplosions;
        boolean twinkle;
        
        public StarterFX(final World p_i46464_1_, final double p_i46464_2_, final double p_i46464_4_, final double p_i46464_6_, final double p_i46464_8_, final double p_i46464_10_, final double p_i46464_12_, final EffectRenderer p_i46464_14_, final NBTTagCompound p_i46464_15_) {
            super(p_i46464_1_, p_i46464_2_, p_i46464_4_, p_i46464_6_, 0.0, 0.0, 0.0);
            this.motionX = p_i46464_8_;
            this.motionY = p_i46464_10_;
            this.motionZ = p_i46464_12_;
            this.theEffectRenderer = p_i46464_14_;
            this.particleMaxAge = 8;
            if (p_i46464_15_ != null) {
                this.fireworkExplosions = p_i46464_15_.getTagList("Explosions", 10);
                if (this.fireworkExplosions.tagCount() == 0) {
                    this.fireworkExplosions = null;
                }
                else {
                    this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;
                    for (int i = 0; i < this.fireworkExplosions.tagCount(); ++i) {
                        final NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);
                        if (nbttagcompound.getBoolean("Flicker")) {
                            this.twinkle = true;
                            this.particleMaxAge += 15;
                            break;
                        }
                    }
                }
            }
        }
        
        @Override
        public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        }
        
        @Override
        public void onUpdate() {
            if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
                final boolean flag = this.func_92037_i();
                boolean flag2 = false;
                if (this.fireworkExplosions.tagCount() >= 3) {
                    flag2 = true;
                }
                else {
                    for (int i = 0; i < this.fireworkExplosions.tagCount(); ++i) {
                        final NBTTagCompound nbttagcompound = this.fireworkExplosions.getCompoundTagAt(i);
                        if (nbttagcompound.getByte("Type") == 1) {
                            flag2 = true;
                            break;
                        }
                    }
                }
                final String s1 = "fireworks." + (flag2 ? "largeBlast" : "blast") + (flag ? "_far" : "");
                this.worldObj.playSound(this.posX, this.posY, this.posZ, s1, 20.0f, 0.95f + this.rand.nextFloat() * 0.1f, true);
            }
            if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
                final int k = this.fireworkAge / 2;
                final NBTTagCompound nbttagcompound2 = this.fireworkExplosions.getCompoundTagAt(k);
                final int l = nbttagcompound2.getByte("Type");
                final boolean flag3 = nbttagcompound2.getBoolean("Trail");
                final boolean flag4 = nbttagcompound2.getBoolean("Flicker");
                int[] aint = nbttagcompound2.getIntArray("Colors");
                final int[] aint2 = nbttagcompound2.getIntArray("FadeColors");
                if (aint.length == 0) {
                    aint = new int[] { ItemDye.dyeColors[0] };
                }
                if (l == 1) {
                    this.createBall(0.5, 4, aint, aint2, flag3, flag4);
                }
                else if (l == 2) {
                    this.createShaped(0.5, new double[][] { { 0.0, 1.0 }, { 0.3455, 0.309 }, { 0.9511, 0.309 }, { 0.3795918367346939, -0.12653061224489795 }, { 0.6122448979591837, -0.8040816326530612 }, { 0.0, -0.35918367346938773 } }, aint, aint2, flag3, flag4, false);
                }
                else if (l == 3) {
                    this.createShaped(0.5, new double[][] { { 0.0, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.6 }, { 0.6, 0.6 }, { 0.6, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.0 }, { 0.4, 0.0 }, { 0.4, -0.6 }, { 0.2, -0.6 }, { 0.2, -0.4 }, { 0.0, -0.4 } }, aint, aint2, flag3, flag4, true);
                }
                else if (l == 4) {
                    this.createBurst(aint, aint2, flag3, flag4);
                }
                else {
                    this.createBall(0.25, 2, aint, aint2, flag3, flag4);
                }
                final int j = aint[0];
                final float f = ((j & 0xFF0000) >> 16) / 255.0f;
                final float f2 = ((j & 0xFF00) >> 8) / 255.0f;
                final float f3 = ((j & 0xFF) >> 0) / 255.0f;
                final OverlayFX entityfirework$overlayfx = new OverlayFX(this.worldObj, this.posX, this.posY, this.posZ);
                entityfirework$overlayfx.setRBGColorF(f, f2, f3);
                this.theEffectRenderer.addEffect(entityfirework$overlayfx);
            }
            ++this.fireworkAge;
            if (this.fireworkAge > this.particleMaxAge) {
                if (this.twinkle) {
                    final boolean flag5 = this.func_92037_i();
                    final String s2 = "fireworks." + (flag5 ? "twinkle_far" : "twinkle");
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, s2, 20.0f, 0.9f + this.rand.nextFloat() * 0.15f, true);
                }
                this.setDead();
            }
        }
        
        private boolean func_92037_i() {
            final Minecraft minecraft = Minecraft.getMinecraft();
            return minecraft == null || minecraft.getRenderViewEntity() == null || minecraft.getRenderViewEntity().getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0;
        }
        
        private void createParticle(final double p_92034_1_, final double p_92034_3_, final double p_92034_5_, final double p_92034_7_, final double p_92034_9_, final double p_92034_11_, final int[] p_92034_13_, final int[] p_92034_14_, final boolean p_92034_15_, final boolean p_92034_16_) {
            final SparkFX entityfirework$sparkfx = new SparkFX(this.worldObj, p_92034_1_, p_92034_3_, p_92034_5_, p_92034_7_, p_92034_9_, p_92034_11_, this.theEffectRenderer);
            entityfirework$sparkfx.setAlphaF(0.99f);
            entityfirework$sparkfx.setTrail(p_92034_15_);
            entityfirework$sparkfx.setTwinkle(p_92034_16_);
            final int i = this.rand.nextInt(p_92034_13_.length);
            entityfirework$sparkfx.setColour(p_92034_13_[i]);
            if (p_92034_14_ != null && p_92034_14_.length > 0) {
                entityfirework$sparkfx.setFadeColour(p_92034_14_[this.rand.nextInt(p_92034_14_.length)]);
            }
            this.theEffectRenderer.addEffect(entityfirework$sparkfx);
        }
        
        private void createBall(final double speed, final int size, final int[] colours, final int[] fadeColours, final boolean trail, final boolean twinkleIn) {
            final double d0 = this.posX;
            final double d2 = this.posY;
            final double d3 = this.posZ;
            for (int i = -size; i <= size; ++i) {
                for (int j = -size; j <= size; ++j) {
                    for (int k = -size; k <= size; ++k) {
                        final double d4 = j + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        final double d5 = i + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        final double d6 = k + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                        final double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6) / speed + this.rand.nextGaussian() * 0.05;
                        this.createParticle(d0, d2, d3, d4 / d7, d5 / d7, d6 / d7, colours, fadeColours, trail, twinkleIn);
                        if (i != -size && i != size && j != -size && j != size) {
                            k += size * 2 - 1;
                        }
                    }
                }
            }
        }
        
        private void createShaped(final double speed, final double[][] shape, final int[] colours, final int[] fadeColours, final boolean trail, final boolean twinkleIn, final boolean p_92038_8_) {
            final double d0 = shape[0][0];
            final double d2 = shape[0][1];
            this.createParticle(this.posX, this.posY, this.posZ, d0 * speed, d2 * speed, 0.0, colours, fadeColours, trail, twinkleIn);
            final float f = this.rand.nextFloat() * 3.1415927f;
            final double d3 = p_92038_8_ ? 0.034 : 0.34;
            for (int i = 0; i < 3; ++i) {
                final double d4 = f + i * 3.1415927f * d3;
                double d5 = d0;
                double d6 = d2;
                for (int j = 1; j < shape.length; ++j) {
                    final double d7 = shape[j][0];
                    final double d8 = shape[j][1];
                    for (double d9 = 0.25; d9 <= 1.0; d9 += 0.25) {
                        double d10 = (d5 + (d7 - d5) * d9) * speed;
                        final double d11 = (d6 + (d8 - d6) * d9) * speed;
                        final double d12 = d10 * Math.sin(d4);
                        d10 *= Math.cos(d4);
                        for (double d13 = -1.0; d13 <= 1.0; d13 += 2.0) {
                            this.createParticle(this.posX, this.posY, this.posZ, d10 * d13, d11, d12 * d13, colours, fadeColours, trail, twinkleIn);
                        }
                    }
                    d5 = d7;
                    d6 = d8;
                }
            }
        }
        
        private void createBurst(final int[] colours, final int[] fadeColours, final boolean trail, final boolean twinkleIn) {
            final double d0 = this.rand.nextGaussian() * 0.05;
            final double d2 = this.rand.nextGaussian() * 0.05;
            for (int i = 0; i < 70; ++i) {
                final double d3 = this.motionX * 0.5 + this.rand.nextGaussian() * 0.15 + d0;
                final double d4 = this.motionZ * 0.5 + this.rand.nextGaussian() * 0.15 + d2;
                final double d5 = this.motionY * 0.5 + this.rand.nextDouble() * 0.5;
                this.createParticle(this.posX, this.posY, this.posZ, d3, d5, d4, colours, fadeColours, trail, twinkleIn);
            }
        }
        
        @Override
        public int getFXLayer() {
            return 0;
        }
    }
}
