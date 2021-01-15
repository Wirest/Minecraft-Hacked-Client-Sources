package dev.astroclient.client.feature.impl.visuals;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.render.EventRender3D;
import dev.astroclient.client.event.impl.render.EventRenderModel;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.EntityUtil;
import dev.astroclient.client.util.render.animation.Opacity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@Toggleable(label = "Chams", category = Category.VISUALS)
public class Chams extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", "Flat", new String[]{"Flat", "Texture", "Wireframe"});
    public StringProperty colorMode = new StringProperty("Color Mode", "Custom", new String[]{"Rainbow", "Custom", "Teams"});
    public ColorProperty visibleColor = new ColorProperty("Visible Color", true, new Color(255, 147, 240, 255));
    public ColorProperty hiddenColor = new ColorProperty("Hidden Color", true, new Color(255, 255, 255));
    public ColorProperty handColor = new ColorProperty("Hand Color", true, new Color(255, 147, 240, 255));
    public NumberProperty<Float> visibleOpacity = new NumberProperty<>("Visible Opacity", true, 1F, .05F, .05F, 1F);
    public NumberProperty<Float> hiddenOpacity = new NumberProperty<>("Hidden Opacity", true, .25F, .05F, .05F, 1F);
    public NumberProperty<Float> handOpacity = new NumberProperty<>("Hand Opacity", true, 1F, .05F, .05F, 1F);

    public BooleanProperty localPlayer = new BooleanProperty("Local Player", true, true);
    public BooleanProperty hands = new BooleanProperty("Hands", true, true);

    public Opacity visibleHue = new Opacity(0);
    public Opacity hiddenHue = new Opacity(0);

    @Subscribe
    public void onEvent(EventRenderModel eventRenderModel) {
        if (isValid(eventRenderModel.getEntityLivingBase())) {
            if (eventRenderModel.getEntityLivingBase() == mc.thePlayer && !localPlayer.getValue()) return;

            Color color = Color.getHSBColor(hiddenHue.getColor() / 255.0F, 0.8F, 1.0F);
            if (colorMode.getValue().equalsIgnoreCase("Teams"))
                color = EntityUtil.isOnSameTeam(eventRenderModel.getEntityLivingBase()) ? new Color(14, 61, 75) : new Color(255, 0, 0);

            if (eventRenderModel.getEventType() == EventType.PRE) {
                switch (mode.getValue()) {
                    case "Flat":
                        GlStateManager.pushMatrix();
                        glEnable(GL11.GL_POLYGON_OFFSET_LINE);
                        glPolygonOffset(1.0f, 1000000.0f);
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
                        glDisable(GL11.GL_LIGHTING);
                        glDisable(GL11.GL_TEXTURE_2D);
                        glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(false);
                        if (colorMode.getValue().equalsIgnoreCase("Custom"))
                            GlStateManager.color(hiddenColor.getColor().getRed() / 255.0F, hiddenColor.getColor().getGreen() / 255.0F, hiddenColor.getColor().getBlue() / 255.0F, hiddenOpacity.getValue());
                        else
                            GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, hiddenOpacity.getValue());
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(770, 771);
                        GlStateManager.alphaFunc(516, 0.003921569F);
                        break;
                    case "Texture":
                        GlStateManager.pushMatrix();
                        glEnable(GL11.GL_POLYGON_OFFSET_LINE);
                        glPolygonOffset(1.0f, 1000000.0f);
                        glDisable(GL11.GL_TEXTURE_2D);
                        glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(false);
                        if (colorMode.getValue().equalsIgnoreCase("Custom"))
                            GlStateManager.color(hiddenColor.getColor().getRed() / 255.0F, hiddenColor.getColor().getGreen() / 255.0F, hiddenColor.getColor().getBlue() / 255.0F, hiddenOpacity.getValue());
                        else
                            GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, hiddenOpacity.getValue());
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(770, 771);
                        GlStateManager.alphaFunc(516, 0.003921569F);
                        break;
                    case "Wireframe":
                        GlStateManager.pushMatrix();
                        glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                        glEnable(GL11.GL_POLYGON_OFFSET_LINE);
                        glPolygonOffset(1.0f, -1100000.0f);
                        glDisable(GL11.GL_TEXTURE_2D);
                        glDisable(GL11.GL_LIGHTING);
                        glEnable(GL_LINE_SMOOTH);
                        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
                        glLineWidth(1);
                        if (colorMode.getValue().equalsIgnoreCase("Custom"))
                            GlStateManager.color(hiddenColor.getColor().getRed() / 255.0F, hiddenColor.getColor().getGreen() / 255.0F, hiddenColor.getColor().getBlue() / 255.0F, hiddenOpacity.getValue());
                        else
                            GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, hiddenOpacity.getValue());
                        break;
                }
            }
            if (eventRenderModel.getEventType() == EventType.POST) {
                switch (mode.getValue()) {
                    case "Texture":
                        GlStateManager.pushMatrix();
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(true);

                        Color color1 = Color.getHSBColor(hiddenHue.getColor() / 255.0F, 0.8F, 1.0F);
                        if (colorMode.getValue().equalsIgnoreCase("Teams"))
                            color1 = EntityUtil.isOnSameTeam(eventRenderModel.getEntityLivingBase()) ? new Color(22, 104, 126) : new Color(255, 0, 0);
                        if (colorMode.getValue().equalsIgnoreCase("Custom"))
                            GlStateManager.color(visibleColor.getColor().getRed() / 255.0F, visibleColor.getColor().getGreen() / 255.0F, visibleColor.getColor().getBlue() / 255.0F, visibleOpacity.getValue());
                        else
                            GlStateManager.color(color1.getRed() / 255.0F, color1.getGreen() / 255.0F, color1.getBlue() / 255.0F, visibleOpacity.getValue());
                        eventRenderModel.getModel().render(eventRenderModel.getEntityLivingBase(), eventRenderModel.getP_77036_2_(), eventRenderModel.getP_77036_3_(), eventRenderModel.getP_77036_4_(), eventRenderModel.getP_77036_5_(), eventRenderModel.getP_77036_6_(), eventRenderModel.getP_77036_7_());
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GlStateManager.disableBlend();
                        GlStateManager.alphaFunc(516, 0.1F);
                        GL11.glPolygonOffset(1.0f, -1000000.0f);
                        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
                        GlStateManager.popMatrix();
                        break;
                    case "Wireframe":
                        GlStateManager.pushMatrix();
                        glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                        glDisable(GL11.GL_POLYGON_OFFSET_LINE);
                        glPolygonOffset(1.0f, 1100000.0f);
                        glEnable(GL11.GL_TEXTURE_2D);
                        glEnable(GL11.GL_LIGHTING);
                        glDisable(GL_LINE_SMOOTH);
                        GlStateManager.popMatrix();
                        break;
                    case "Flat":
                        GlStateManager.pushMatrix();
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(true);

                        Color color2 = Color.getHSBColor(hiddenHue.getColor() / 255.0F, 0.8F, 1.0F);
                        if (colorMode.getValue().equalsIgnoreCase("Teams"))
                            color2 = EntityUtil.isOnSameTeam(eventRenderModel.getEntityLivingBase()) ? new Color(22, 104, 126) : new Color(255, 0, 0);
                        if (colorMode.getValue().equalsIgnoreCase("Custom"))
                            GlStateManager.color(visibleColor.getColor().getRed() / 255.0F, visibleColor.getColor().getGreen() / 255.0F, visibleColor.getColor().getBlue() / 255.0F, visibleOpacity.getValue());
                        else
                            GlStateManager.color(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, visibleOpacity.getValue());
                        eventRenderModel.getModel().render(eventRenderModel.getEntityLivingBase(), eventRenderModel.getP_77036_2_(), eventRenderModel.getP_77036_3_(), eventRenderModel.getP_77036_4_(), eventRenderModel.getP_77036_5_(), eventRenderModel.getP_77036_6_(), eventRenderModel.getP_77036_7_());
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GlStateManager.disableBlend();
                        GlStateManager.alphaFunc(516, 0.1F);
                        GL11.glPolygonOffset(1.0f, -1000000.0f);
                        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
                        GlStateManager.popMatrix();
                        break;
                }
            }
        }
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if (colorMode.getValue().equals("Rainbow"))
            this.visibleHue.increase(255.0F, 2);
    }

    @Subscribe
    public void onEvent(EventRender3D eventRender3D) {
        if (colorMode.getValue().equals("Rainbow")) {
            float hue = 255.0F - this.visibleHue.getColor();
            if (hue > 255.0F) {
                hue = 0.0F;
            }

            if (hue < 0.0F) {
                hue = 255.0F;
            }
            this.hiddenHue.setColor(hue);
        }
    }

    public boolean isValid(EntityLivingBase e) {
        return e instanceof EntityPlayer && !e.isInvisible();
    }

}
