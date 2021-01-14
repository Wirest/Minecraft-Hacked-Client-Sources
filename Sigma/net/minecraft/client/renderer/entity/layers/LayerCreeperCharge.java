package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;

public class LayerCreeperCharge implements LayerRenderer {
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderCreeper creeperRenderer;
    private final ModelCreeper creeperModel = new ModelCreeper(2.0F);
    private static final String __OBFID = "CL_00002423";

    public LayerCreeperCharge(RenderCreeper p_i46121_1_) {
        this.creeperRenderer = p_i46121_1_;
    }

    public void doRenderLayer(EntityCreeper p_177169_1_, float p_177169_2_, float p_177169_3_, float p_177169_4_, float p_177169_5_, float p_177169_6_, float p_177169_7_, float p_177169_8_) {
        if (p_177169_1_.getPowered()) {
            GlStateManager.depthMask(!p_177169_1_.isInvisible());
            this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float var9 = (float) p_177169_1_.ticksExisted + p_177169_4_;
            GlStateManager.translate(var9 * 0.01F, var9 * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            float var10 = 0.5F;
            GlStateManager.color(var10, var10, var10, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
            this.creeperModel.render(p_177169_1_, p_177169_2_, p_177169_3_, p_177169_5_, p_177169_6_, p_177169_7_, p_177169_8_);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        this.doRenderLayer((EntityCreeper) p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
