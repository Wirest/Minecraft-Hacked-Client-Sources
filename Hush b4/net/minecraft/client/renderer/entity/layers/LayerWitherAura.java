// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.boss.EntityWither;

public class LayerWitherAura implements LayerRenderer<EntityWither>
{
    private static final ResourceLocation WITHER_ARMOR;
    private final RenderWither witherRenderer;
    private final ModelWither witherModel;
    
    static {
        WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
    }
    
    public LayerWitherAura(final RenderWither witherRendererIn) {
        this.witherModel = new ModelWither(0.5f);
        this.witherRenderer = witherRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityWither entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (entitylivingbaseIn.isArmored()) {
            GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
            this.witherRenderer.bindTexture(LayerWitherAura.WITHER_ARMOR);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            final float f = entitylivingbaseIn.ticksExisted + partialTicks;
            final float f2 = MathHelper.cos(f * 0.02f) * 3.0f;
            final float f3 = f * 0.01f;
            GlStateManager.translate(f2, f3, 0.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            final float f4 = 0.5f;
            GlStateManager.color(f4, f4, f4, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(1, 1);
            this.witherModel.setLivingAnimations(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks);
            this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
            this.witherModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
