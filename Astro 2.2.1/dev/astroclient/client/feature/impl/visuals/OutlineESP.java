package dev.astroclient.client.feature.impl.visuals;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.render.EventRenderModel;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.EntityUtil;
import dev.astroclient.client.util.render.OutlineUtil;
import dev.astroclient.client.util.render.animation.Opacity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * made by Xen for Astro
 * at 12/13/2019
 **/
@Toggleable(label = "Glow", category = Category.VISUALS)
public class OutlineESP extends ToggleableFeature {

    public StringProperty colorMode = new StringProperty("Color Mode", "Teams", new String[]{"Team", "Rainbow", "Custom"});
    public ColorProperty color = new ColorProperty("Color", true, new Color(0xFF0000));
    public NumberProperty<Float> width = new NumberProperty<>("Width", false, 1.0F, 0.01F, 1.0F, 10.0F);
    private BooleanProperty localPlayer = new BooleanProperty("Local Player", false, true);

    private Opacity hue = new Opacity(0);

    @Subscribe
    public void onEvent(EventRenderModel eventRenderModel) {
        if (eventRenderModel.getEntityLivingBase() == mc.thePlayer && !localPlayer.getValue()) return;

        if (eventRenderModel.getEventType() == EventType.PRE) {
            GL11.glPushMatrix();
            GlStateManager.depthMask(true);
            if (isValid(eventRenderModel.getEntityLivingBase())) {
                if (Minecraft.getMinecraft().theWorld != null) {
                    Color c;
                    float h = 255 - hue.getColor();
                    switch (colorMode.getValue()) {
                        case "Teams":
                            c = EntityUtil.isOnSameTeam(eventRenderModel.getEntityLivingBase()) ? new Color(0x0E3D4B) : new Color(0xFF0000);
                            break;
                        case "Rainbow":
                            c = Color.getHSBColor(h / 255, .8F, 1.0F);
                            break;
                        default:
                            c = color.getColor();
                            break;
                    }

                    eventRenderModel.getModel().render(eventRenderModel.getEntityLivingBase(), eventRenderModel.getP_77036_2_(), eventRenderModel.getP_77036_3_(), eventRenderModel.getP_77036_4_(), eventRenderModel.getP_77036_5_(), eventRenderModel.getP_77036_6_(), eventRenderModel.getP_77036_7_());
                    OutlineUtil.renderOne(width.getValue());
                    eventRenderModel.getModel().render(eventRenderModel.getEntityLivingBase(), eventRenderModel.getP_77036_2_(), eventRenderModel.getP_77036_3_(), eventRenderModel.getP_77036_4_(), eventRenderModel.getP_77036_5_(), eventRenderModel.getP_77036_6_(), eventRenderModel.getP_77036_7_());
                    OutlineUtil.renderTwo();
                    eventRenderModel.getModel().render(eventRenderModel.getEntityLivingBase(), eventRenderModel.getP_77036_2_(), eventRenderModel.getP_77036_3_(), eventRenderModel.getP_77036_4_(), eventRenderModel.getP_77036_5_(), eventRenderModel.getP_77036_6_(), eventRenderModel.getP_77036_7_());
                    OutlineUtil.renderThree();
                    OutlineUtil.renderFour(c);
                    eventRenderModel.getModel().render(eventRenderModel.getEntityLivingBase(), eventRenderModel.getP_77036_2_(), eventRenderModel.getP_77036_3_(), eventRenderModel.getP_77036_4_(), eventRenderModel.getP_77036_5_(), eventRenderModel.getP_77036_6_(), eventRenderModel.getP_77036_7_());
                    OutlineUtil.renderFive();
                }
            }
            GL11.glPopMatrix();
        }
    }

    public boolean isValid(EntityLivingBase entity) {
        return isValidType(entity) && entity.isEntityAlive();
    }

    private boolean isValidType(EntityLivingBase entity) {
        return entity instanceof EntityPlayer;
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if ("Rainbow".equals(colorMode.getValue()))
            hue.increase(255.0F, 2);
    }

}
