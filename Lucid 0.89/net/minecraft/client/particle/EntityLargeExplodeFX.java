package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityLargeExplodeFX extends EntityFX
{
    private static final ResourceLocation EXPLOSION_TEXTURE = new ResourceLocation("textures/entity/explosion.png");
    private int field_70581_a;
    private int field_70584_aq;

    /** The Rendering Engine. */
    private TextureManager theRenderEngine;
    private float field_70582_as;

    protected EntityLargeExplodeFX(TextureManager renderEngine, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1213_9_, double p_i1213_11_, double p_i1213_13_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.theRenderEngine = renderEngine;
        this.field_70584_aq = 6 + this.rand.nextInt(4);
        this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6F + 0.4F;
        this.field_70582_as = 1.0F - (float)p_i1213_9_ * 0.5F;
    }

    /**
     * Renders the particle
     *  
     * @param worldRendererIn The WorldRenderer instance
     */
    @Override
	public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        int var9 = (int)((this.field_70581_a + partialTicks) * 15.0F / this.field_70584_aq);

        if (var9 <= 15)
        {
            this.theRenderEngine.bindTexture(EXPLOSION_TEXTURE);
            float var10 = var9 % 4 / 4.0F;
            float var11 = var10 + 0.24975F;
            float var12 = var9 / 4 / 4.0F;
            float var13 = var12 + 0.24975F;
            float var14 = 2.0F * this.field_70582_as;
            float var15 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
            float var16 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
            float var17 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            RenderHelper.disableStandardItemLighting();
            worldRendererIn.startDrawingQuads();
            worldRendererIn.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 1.0F);
            worldRendererIn.setNormal(0.0F, 1.0F, 0.0F);
            worldRendererIn.setBrightness(240);
            worldRendererIn.addVertexWithUV(var15 - p_180434_4_ * var14 - p_180434_7_ * var14, var16 - p_180434_5_ * var14, var17 - p_180434_6_ * var14 - p_180434_8_ * var14, var11, var13);
            worldRendererIn.addVertexWithUV(var15 - p_180434_4_ * var14 + p_180434_7_ * var14, var16 + p_180434_5_ * var14, var17 - p_180434_6_ * var14 + p_180434_8_ * var14, var11, var12);
            worldRendererIn.addVertexWithUV(var15 + p_180434_4_ * var14 + p_180434_7_ * var14, var16 + p_180434_5_ * var14, var17 + p_180434_6_ * var14 + p_180434_8_ * var14, var10, var12);
            worldRendererIn.addVertexWithUV(var15 + p_180434_4_ * var14 - p_180434_7_ * var14, var16 - p_180434_5_ * var14, var17 + p_180434_6_ * var14 - p_180434_8_ * var14, var10, var13);
            Tessellator.getInstance().draw();
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.enableLighting();
        }
    }

    @Override
	public int getBrightnessForRender(float partialTicks)
    {
        return 61680;
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
        ++this.field_70581_a;

        if (this.field_70581_a == this.field_70584_aq)
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
            return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
