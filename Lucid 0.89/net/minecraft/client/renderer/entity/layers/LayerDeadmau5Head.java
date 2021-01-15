package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;

public class LayerDeadmau5Head implements LayerRenderer
{
    private final RenderPlayer playerRenderer;

    public LayerDeadmau5Head(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
    }

    public void doRenderDeadmau5Head(AbstractClientPlayer clientPlayer, float p_177207_2_, float p_177207_3_, float p_177207_4_, float p_177207_5_, float p_177207_6_, float p_177207_7_, float p_177207_8_)
    {
        if (clientPlayer.getCommandSenderName().equals("deadmau5") && clientPlayer.hasSkin() && !clientPlayer.isInvisible())
        {
            this.playerRenderer.bindTexture(clientPlayer.getLocationSkin());

            for (int var9 = 0; var9 < 2; ++var9)
            {
                float var10 = clientPlayer.prevRotationYaw + (clientPlayer.rotationYaw - clientPlayer.prevRotationYaw) * p_177207_4_ - (clientPlayer.prevRenderYawOffset + (clientPlayer.renderYawOffset - clientPlayer.prevRenderYawOffset) * p_177207_4_);
                float var11 = clientPlayer.prevRotationPitch + (clientPlayer.rotationPitch - clientPlayer.prevRotationPitch) * p_177207_4_;
                GlStateManager.pushMatrix();
                GlStateManager.rotate(var10, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(var11, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.375F * (var9 * 2 - 1), 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.375F, 0.0F);
                GlStateManager.rotate(-var11, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-var10, 0.0F, 1.0F, 0.0F);
                float var12 = 1.3333334F;
                GlStateManager.scale(var12, var12, var12);
                this.playerRenderer.getPlayerModel().renderDeadmau5Head(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return true;
    }

    @Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        this.doRenderDeadmau5Head((AbstractClientPlayer)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
