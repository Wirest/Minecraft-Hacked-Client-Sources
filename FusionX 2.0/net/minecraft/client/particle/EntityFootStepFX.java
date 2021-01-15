package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFootStepFX extends EntityFX
{
    private static final ResourceLocation field_110126_a = new ResourceLocation("textures/particle/footprint.png");
    private int footstepAge;
    private int footstepMaxAge;
    private TextureManager currentFootSteps;
    private static final String __OBFID = "CL_00000908";

    protected EntityFootStepFX(TextureManager p_i1210_1_, World worldIn, double p_i1210_3_, double p_i1210_5_, double p_i1210_7_)
    {
        super(worldIn, p_i1210_3_, p_i1210_5_, p_i1210_7_, 0.0D, 0.0D, 0.0D);
        this.currentFootSteps = p_i1210_1_;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.footstepMaxAge = 200;
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float var9 = ((float)this.footstepAge + p_180434_3_) / (float)this.footstepMaxAge;
        var9 *= var9;
        float var10 = 2.0F - var9 * 2.0F;

        if (var10 > 1.0F)
        {
            var10 = 1.0F;
        }

        var10 *= 0.2F;
        GlStateManager.disableLighting();
        float var11 = 0.125F;
        float var12 = (float)(this.posX - interpPosX);
        float var13 = (float)(this.posY - interpPosY);
        float var14 = (float)(this.posZ - interpPosZ);
        float var15 = this.worldObj.getLightBrightness(new BlockPos(this));
        this.currentFootSteps.bindTexture(field_110126_a);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        p_180434_1_.startDrawingQuads();
        p_180434_1_.func_178960_a(var15, var15, var15, var10);
        p_180434_1_.addVertexWithUV((double)(var12 - var11), (double)var13, (double)(var14 + var11), 0.0D, 1.0D);
        p_180434_1_.addVertexWithUV((double)(var12 + var11), (double)var13, (double)(var14 + var11), 1.0D, 1.0D);
        p_180434_1_.addVertexWithUV((double)(var12 + var11), (double)var13, (double)(var14 - var11), 1.0D, 0.0D);
        p_180434_1_.addVertexWithUV((double)(var12 - var11), (double)var13, (double)(var14 - var11), 0.0D, 0.0D);
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        ++this.footstepAge;

        if (this.footstepAge == this.footstepMaxAge)
        {
            this.setDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }

    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002601";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
        {
            return new EntityFootStepFX(Minecraft.getMinecraft().getTextureManager(), worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}
