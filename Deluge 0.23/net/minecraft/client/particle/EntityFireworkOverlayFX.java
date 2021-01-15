package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkOverlayFX extends EntityFX
{
    private static final String __OBFID = "CL_00000904";

    protected EntityFireworkOverlayFX(World worldIn, double p_i46357_2_, double p_i46357_4_, double p_i46357_6_)
    {
        super(worldIn, p_i46357_2_, p_i46357_4_, p_i46357_6_);
        this.particleMaxAge = 4;
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float var9 = 0.25F;
        float var10 = var9 + 0.25F;
        float var11 = 0.125F;
        float var12 = var11 + 0.25F;
        float var13 = 7.1F * MathHelper.sin(((float)this.particleAge + p_180434_3_ - 1.0F) * 0.25F * (float)Math.PI);
        this.particleAlpha = 0.6F - ((float)this.particleAge + p_180434_3_ - 1.0F) * 0.25F * 0.5F;
        float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - interpPosX);
        float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - interpPosY);
        float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - interpPosZ);
        p_180434_1_.func_178960_a(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        p_180434_1_.addVertexWithUV((double)(var14 - p_180434_4_ * var13 - p_180434_7_ * var13), (double)(var15 - p_180434_5_ * var13), (double)(var16 - p_180434_6_ * var13 - p_180434_8_ * var13), (double)var10, (double)var12);
        p_180434_1_.addVertexWithUV((double)(var14 - p_180434_4_ * var13 + p_180434_7_ * var13), (double)(var15 + p_180434_5_ * var13), (double)(var16 - p_180434_6_ * var13 + p_180434_8_ * var13), (double)var10, (double)var11);
        p_180434_1_.addVertexWithUV((double)(var14 + p_180434_4_ * var13 + p_180434_7_ * var13), (double)(var15 + p_180434_5_ * var13), (double)(var16 + p_180434_6_ * var13 + p_180434_8_ * var13), (double)var9, (double)var11);
        p_180434_1_.addVertexWithUV((double)(var14 + p_180434_4_ * var13 - p_180434_7_ * var13), (double)(var15 - p_180434_5_ * var13), (double)(var16 + p_180434_6_ * var13 - p_180434_8_ * var13), (double)var9, (double)var12);
    }
}
