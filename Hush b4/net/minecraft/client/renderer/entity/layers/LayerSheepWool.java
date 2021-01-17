// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import optifine.CustomColors;
import optifine.Config;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.util.ResourceLocation;

public class LayerSheepWool implements LayerRenderer
{
    private static final ResourceLocation TEXTURE;
    private final RenderSheep sheepRenderer;
    private final ModelSheep1 sheepModel;
    private static final String __OBFID = "CL_00002413";
    
    static {
        TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    }
    
    public LayerSheepWool(final RenderSheep sheepRendererIn) {
        this.sheepModel = new ModelSheep1();
        this.sheepRenderer = sheepRendererIn;
    }
    
    public void doRenderLayer(final EntitySheep entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible()) {
            this.sheepRenderer.bindTexture(LayerSheepWool.TEXTURE);
            if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomNameTag())) {
                final boolean flag = true;
                final int i = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
                final int j = EnumDyeColor.values().length;
                final int k = i % j;
                final int l = (i + 1) % j;
                final float f = (entitylivingbaseIn.ticksExisted % 25 + partialTicks) / 25.0f;
                float[] afloat1 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(k));
                float[] afloat2 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(l));
                if (Config.isCustomColors()) {
                    afloat1 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(k), afloat1);
                    afloat2 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(l), afloat2);
                }
                GlStateManager.color(afloat1[0] * (1.0f - f) + afloat2[0] * f, afloat1[1] * (1.0f - f) + afloat2[1] * f, afloat1[2] * (1.0f - f) + afloat2[2] * f);
            }
            else {
                float[] afloat3 = EntitySheep.func_175513_a(entitylivingbaseIn.getFleeceColor());
                if (Config.isCustomColors()) {
                    afloat3 = CustomColors.getSheepColors(entitylivingbaseIn.getFleeceColor(), afloat3);
                }
                GlStateManager.color(afloat3[0], afloat3[1], afloat3[2]);
            }
            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks);
            this.sheepModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        this.doRenderLayer((EntitySheep)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
    }
}
