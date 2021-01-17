// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityCreeper;

public class LayerCreeperCharge implements LayerRenderer<EntityCreeper>
{
    private static final ResourceLocation LIGHTNING_TEXTURE;
    private final RenderCreeper creeperRenderer;
    private final ModelCreeper creeperModel;
    
    static {
        LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }
    
    public LayerCreeperCharge(final RenderCreeper creeperRendererIn) {
        this.creeperModel = new ModelCreeper(2.0f);
        this.creeperRenderer = creeperRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityCreeper entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (entitylivingbaseIn.getPowered()) {
            final boolean flag = entitylivingbaseIn.isInvisible();
            GlStateManager.depthMask(!flag);
            this.creeperRenderer.bindTexture(LayerCreeperCharge.LIGHTNING_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            final float f = entitylivingbaseIn.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01f, f * 0.01f, 0.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            final float f2 = 0.5f;
            GlStateManager.color(f2, f2, f2, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
            this.creeperModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
