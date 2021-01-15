package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityDiggingFX extends EntityFX
{
    private IBlockState field_174847_a;
    private static final String __OBFID = "CL_00000932";

    protected EntityDiggingFX(World worldIn, double p_i46280_2_, double p_i46280_4_, double p_i46280_6_, double p_i46280_8_, double p_i46280_10_, double p_i46280_12_, IBlockState p_i46280_14_)
    {
        super(worldIn, p_i46280_2_, p_i46280_4_, p_i46280_6_, p_i46280_8_, p_i46280_10_, p_i46280_12_);
        this.field_174847_a = p_i46280_14_;
        this.func_180435_a(Minecraft.getMinecraft().getBlockRendererDispatcher().func_175023_a().func_178122_a(p_i46280_14_));
        this.particleGravity = p_i46280_14_.getBlock().blockParticleGravity;
        this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
        this.particleScale /= 2.0F;
    }

    public EntityDiggingFX func_174846_a(BlockPos p_174846_1_)
    {
        if (this.field_174847_a.getBlock() == Blocks.grass)
        {
            return this;
        }
        else
        {
            int var2 = this.field_174847_a.getBlock().colorMultiplier(this.worldObj, p_174846_1_);
            this.particleRed *= (float)(var2 >> 16 & 255) / 255.0F;
            this.particleGreen *= (float)(var2 >> 8 & 255) / 255.0F;
            this.particleBlue *= (float)(var2 & 255) / 255.0F;
            return this;
        }
    }

    public EntityDiggingFX func_174845_l()
    {
        Block var1 = this.field_174847_a.getBlock();

        if (var1 == Blocks.grass)
        {
            return this;
        }
        else
        {
            int var2 = var1.getRenderColor(this.field_174847_a);
            this.particleRed *= (float)(var2 >> 16 & 255) / 255.0F;
            this.particleGreen *= (float)(var2 >> 8 & 255) / 255.0F;
            this.particleBlue *= (float)(var2 & 255) / 255.0F;
            return this;
        }
    }

    public int getFXLayer()
    {
        return 1;
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float var9 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
        float var10 = var9 + 0.015609375F;
        float var11 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
        float var12 = var11 + 0.015609375F;
        float var13 = 0.1F * this.particleScale;

        if (this.particleIcon != null)
        {
            var9 = this.particleIcon.getInterpolatedU((double)(this.particleTextureJitterX / 4.0F * 16.0F));
            var10 = this.particleIcon.getInterpolatedU((double)((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
            var11 = this.particleIcon.getInterpolatedV((double)(this.particleTextureJitterY / 4.0F * 16.0F));
            var12 = this.particleIcon.getInterpolatedV((double)((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
        }

        float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - interpPosX);
        float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - interpPosY);
        float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - interpPosZ);
        p_180434_1_.func_178986_b(this.particleRed, this.particleGreen, this.particleBlue);
        p_180434_1_.addVertexWithUV((double)(var14 - p_180434_4_ * var13 - p_180434_7_ * var13), (double)(var15 - p_180434_5_ * var13), (double)(var16 - p_180434_6_ * var13 - p_180434_8_ * var13), (double)var9, (double)var12);
        p_180434_1_.addVertexWithUV((double)(var14 - p_180434_4_ * var13 + p_180434_7_ * var13), (double)(var15 + p_180434_5_ * var13), (double)(var16 - p_180434_6_ * var13 + p_180434_8_ * var13), (double)var9, (double)var11);
        p_180434_1_.addVertexWithUV((double)(var14 + p_180434_4_ * var13 + p_180434_7_ * var13), (double)(var15 + p_180434_5_ * var13), (double)(var16 + p_180434_6_ * var13 + p_180434_8_ * var13), (double)var10, (double)var11);
        p_180434_1_.addVertexWithUV((double)(var14 + p_180434_4_ * var13 - p_180434_7_ * var13), (double)(var15 - p_180434_5_ * var13), (double)(var16 + p_180434_6_ * var13 - p_180434_8_ * var13), (double)var10, (double)var12);
    }

    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002575";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
        {
            return (new EntityDiggingFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, Block.getStateById(p_178902_15_[0]))).func_174845_l();
        }
    }
}
