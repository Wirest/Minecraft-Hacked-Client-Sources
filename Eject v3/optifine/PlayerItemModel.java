package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerItemModel {
    public static final int ATTACH_BODY = 0;
    public static final int ATTACH_HEAD = 1;
    public static final int ATTACH_LEFT_ARM = 2;
    public static final int ATTACH_RIGHT_ARM = 3;
    public static final int ATTACH_LEFT_LEG = 4;
    public static final int ATTACH_RIGHT_LEG = 5;
    public static final int ATTACH_CAPE = 6;
    private Dimension textureSize = null;
    private boolean usePlayerTexture = false;
    private PlayerItemRenderer[] modelRenderers = new PlayerItemRenderer[0];
    private ResourceLocation textureLocation = null;
    private BufferedImage textureImage = null;
    private DynamicTexture texture = null;
    private ResourceLocation locationMissing = new ResourceLocation("textures/blocks/wool_colored_red.png");

    public PlayerItemModel(Dimension paramDimension, boolean paramBoolean, PlayerItemRenderer[] paramArrayOfPlayerItemRenderer) {
        this.textureSize = paramDimension;
        this.usePlayerTexture = paramBoolean;
        this.modelRenderers = paramArrayOfPlayerItemRenderer;
    }

    public static ModelRenderer getAttachModel(ModelBiped paramModelBiped, int paramInt) {
        switch (paramInt) {
            case 0:
                return paramModelBiped.bipedBody;
            case 1:
                return paramModelBiped.bipedHead;
            case 2:
                return paramModelBiped.bipedLeftArm;
            case 3:
                return paramModelBiped.bipedRightArm;
            case 4:
                return paramModelBiped.bipedLeftLeg;
            case 5:
                return paramModelBiped.bipedRightLeg;
        }
        return null;
    }

    public void render(ModelBiped paramModelBiped, AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2) {
        TextureManager localTextureManager = Config.getTextureManager();
        if (this.usePlayerTexture) {
            localTextureManager.bindTexture(paramAbstractClientPlayer.getLocationSkin());
        } else if (this.textureLocation != null) {
            if ((this.texture == null) && (this.textureImage != null)) {
                this.texture = new DynamicTexture(this.textureImage);
                Minecraft.getMinecraft().getTextureManager().loadTexture(this.textureLocation, this.texture);
            }
            localTextureManager.bindTexture(this.textureLocation);
        } else {
            localTextureManager.bindTexture(this.locationMissing);
        }
        for (int i = 0; i < this.modelRenderers.length; i++) {
            PlayerItemRenderer localPlayerItemRenderer = this.modelRenderers[i];
            GlStateManager.pushMatrix();
            if (paramAbstractClientPlayer.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            localPlayerItemRenderer.render(paramModelBiped, paramFloat1);
            GlStateManager.popMatrix();
        }
    }

    public BufferedImage getTextureImage() {
        return this.textureImage;
    }

    public void setTextureImage(BufferedImage paramBufferedImage) {
        this.textureImage = paramBufferedImage;
    }

    public DynamicTexture getTexture() {
        return this.texture;
    }

    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    public void setTextureLocation(ResourceLocation paramResourceLocation) {
        this.textureLocation = paramResourceLocation;
    }

    public boolean isUsePlayerTexture() {
        return this.usePlayerTexture;
    }
}




