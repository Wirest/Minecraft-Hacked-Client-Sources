package net.minecraft.optifine;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class PlayerItemModel {
    private Dimension textureSize = null;
    private boolean usePlayerTexture = false;
    private PlayerItemRenderer[] modelRenderers = new PlayerItemRenderer[0];
    private ResourceLocation textureLocation = null;
    private BufferedImage textureImage = null;
    private DynamicTexture texture = null;
    private ResourceLocation locationMissing = new ResourceLocation("textures/blocks/wool_colored_red.png");
    public static final int ATTACH_BODY = 0;
    public static final int ATTACH_HEAD = 1;
    public static final int ATTACH_LEFT_ARM = 2;
    public static final int ATTACH_RIGHT_ARM = 3;
    public static final int ATTACH_LEFT_LEG = 4;
    public static final int ATTACH_RIGHT_LEG = 5;
    public static final int ATTACH_CAPE = 6;

    public PlayerItemModel(Dimension textureSize, boolean usePlayerTexture, PlayerItemRenderer[] modelRenderers) {
        this.textureSize = textureSize;
        this.usePlayerTexture = usePlayerTexture;
        this.modelRenderers = modelRenderers;
    }

    public void render(ModelBiped modelBiped, AbstractClientPlayer player, float scale, float partialTicks) {
        TextureManager textureManager = Config.getTextureManager();

        if (usePlayerTexture) {
            textureManager.bindTexture(player.getLocationSkin());
        } else if (textureLocation != null) {
            if (texture == null && textureImage != null) {
                texture = new DynamicTexture(textureImage);
                Minecraft.getMinecraft().getTextureManager().loadTexture(textureLocation, texture);
            }

            textureManager.bindTexture(textureLocation);
        } else {
            textureManager.bindTexture(locationMissing);
        }

        for (PlayerItemRenderer pir : modelRenderers) {
            GlStateManager.pushMatrix();

            if (player.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            pir.render(modelBiped, scale);
            GlStateManager.popMatrix();
        }
    }

    public static ModelRenderer getAttachModel(ModelBiped modelBiped, int attachTo) {
        switch (attachTo) {
            case 0:
                return modelBiped.bipedBody;

            case 1:
                return modelBiped.bipedHead;

            case 2:
                return modelBiped.bipedLeftArm;

            case 3:
                return modelBiped.bipedRightArm;

            case 4:
                return modelBiped.bipedLeftLeg;

            case 5:
                return modelBiped.bipedRightLeg;

            default:
                return null;
        }
    }

    public BufferedImage getTextureImage() {
        return textureImage;
    }

    public void setTextureImage(BufferedImage textureImage) {
        this.textureImage = textureImage;
    }

    public DynamicTexture getTexture() {
        return texture;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public void setTextureLocation(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    public boolean isUsePlayerTexture() {
        return usePlayerTexture;
    }
}
