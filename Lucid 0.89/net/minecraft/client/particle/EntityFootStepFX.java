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
    private static final ResourceLocation FOOTPRINT_TEXTURE = new ResourceLocation("textures/particle/footprint.png");
    private int footstepAge;
    private int footstepMaxAge;
    private TextureManager currentFootSteps;

    protected EntityFootStepFX(TextureManager currentFootStepsIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.currentFootSteps = currentFootStepsIn;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.footstepMaxAge = 200;
    }

    /**
     * Renders the particle
     *  
     * @param worldRendererIn The WorldRenderer instance
     */
    @Override
	public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float var9 = (this.footstepAge + partialTicks) / this.footstepMaxAge;
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
        this.currentFootSteps.bindTexture(FOOTPRINT_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        worldRendererIn.startDrawingQuads();
        worldRendererIn.setColorRGBA_F(var15, var15, var15, var10);
        worldRendererIn.addVertexWithUV(var12 - var11, var13, var14 + var11, 0.0D, 1.0D);
        worldRendererIn.addVertexWithUV(var12 + var11, var13, var14 + var11, 1.0D, 1.0D);
        worldRendererIn.addVertexWithUV(var12 + var11, var13, var14 - var11, 1.0D, 0.0D);
        worldRendererIn.addVertexWithUV(var12 - var11, var13, var14 - var11, 0.0D, 0.0D);
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        ++this.footstepAge;

        if (this.footstepAge == this.footstepMaxAge)
        {
            this.setDead();
        }
    }

    @Override
	public int getFXLayer()
    {
        return 3;
    }

    public static class Factory implements IParticleFactory
    {

        @Override
		public EntityFX getEntityFX(int p_178902_1_, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_)
        {
            return new EntityFootStepFX(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
