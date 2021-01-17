// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import java.awt.Dimension;

public class PlayerItemModel
{
    private Dimension textureSize;
    private boolean usePlayerTexture;
    private PlayerItemRenderer[] modelRenderers;
    private ResourceLocation textureLocation;
    private BufferedImage textureImage;
    private DynamicTexture texture;
    private ResourceLocation locationMissing;
    public static final int ATTACH_BODY = 0;
    public static final int ATTACH_HEAD = 1;
    public static final int ATTACH_LEFT_ARM = 2;
    public static final int ATTACH_RIGHT_ARM = 3;
    public static final int ATTACH_LEFT_LEG = 4;
    public static final int ATTACH_RIGHT_LEG = 5;
    public static final int ATTACH_CAPE = 6;
    
    public PlayerItemModel(final Dimension p_i74_1_, final boolean p_i74_2_, final PlayerItemRenderer[] p_i74_3_) {
        this.textureSize = null;
        this.usePlayerTexture = false;
        this.modelRenderers = new PlayerItemRenderer[0];
        this.textureLocation = null;
        this.textureImage = null;
        this.texture = null;
        this.locationMissing = new ResourceLocation("textures/blocks/wool_colored_red.png");
        this.textureSize = p_i74_1_;
        this.usePlayerTexture = p_i74_2_;
        this.modelRenderers = p_i74_3_;
    }
    
    public void render(final ModelBiped p_render_1_, final AbstractClientPlayer p_render_2_, final float p_render_3_, final float p_render_4_) {
        final TextureManager texturemanager = Config.getTextureManager();
        if (this.usePlayerTexture) {
            texturemanager.bindTexture(p_render_2_.getLocationSkin());
        }
        else if (this.textureLocation != null) {
            if (this.texture == null && this.textureImage != null) {
                this.texture = new DynamicTexture(this.textureImage);
                Minecraft.getMinecraft().getTextureManager().loadTexture(this.textureLocation, this.texture);
            }
            texturemanager.bindTexture(this.textureLocation);
        }
        else {
            texturemanager.bindTexture(this.locationMissing);
        }
        for (int i = 0; i < this.modelRenderers.length; ++i) {
            final PlayerItemRenderer playeritemrenderer = this.modelRenderers[i];
            GlStateManager.pushMatrix();
            if (p_render_2_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            playeritemrenderer.render(p_render_1_, p_render_3_);
            GlStateManager.popMatrix();
        }
    }
    
    public static ModelRenderer getAttachModel(final ModelBiped p_getAttachModel_0_, final int p_getAttachModel_1_) {
        switch (p_getAttachModel_1_) {
            case 0: {
                return p_getAttachModel_0_.bipedBody;
            }
            case 1: {
                return p_getAttachModel_0_.bipedHead;
            }
            case 2: {
                return p_getAttachModel_0_.bipedLeftArm;
            }
            case 3: {
                return p_getAttachModel_0_.bipedRightArm;
            }
            case 4: {
                return p_getAttachModel_0_.bipedLeftLeg;
            }
            case 5: {
                return p_getAttachModel_0_.bipedRightLeg;
            }
            default: {
                return null;
            }
        }
    }
    
    public BufferedImage getTextureImage() {
        return this.textureImage;
    }
    
    public void setTextureImage(final BufferedImage p_setTextureImage_1_) {
        this.textureImage = p_setTextureImage_1_;
    }
    
    public DynamicTexture getTexture() {
        return this.texture;
    }
    
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }
    
    public void setTextureLocation(final ResourceLocation p_setTextureLocation_1_) {
        this.textureLocation = p_setTextureLocation_1_;
    }
    
    public boolean isUsePlayerTexture() {
        return this.usePlayerTexture;
    }
}
