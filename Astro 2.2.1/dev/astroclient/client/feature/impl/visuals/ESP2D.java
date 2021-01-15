package dev.astroclient.client.feature.impl.visuals;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.render.EventRender2D;
import dev.astroclient.client.event.impl.render.EventRenderTag;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.EntityUtil;
import dev.astroclient.client.util.render.ColorHelper;
import dev.astroclient.client.util.render.Render2DUtil;
import dev.astroclient.client.util.render.Render3DUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Toggleable(label = "2DESP", category = Category.VISUALS)
public class ESP2D extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", true, "Off", new String[]{"Corners", "Box", "Off"});
    public BooleanProperty teamBasedColors = new BooleanProperty("Team Based Colors", true, false);
    public ColorProperty colorProperty = new ColorProperty("Color", new Color(255, 255, 255));
    public ColorProperty targetColor = new ColorProperty("Color When Target", new Color(255, 0, 0));

    public NumberProperty<Float> width = new NumberProperty<>("Width", true, .5F, .5F, .5F, 2F);
    public BooleanProperty tags = new BooleanProperty("Tags", true, false);
    public BooleanProperty healthNumber = new BooleanProperty("Health Number", true, false);
    public BooleanProperty healthBar = new BooleanProperty("Health Bar", true, true);
    public BooleanProperty localPlayer = new BooleanProperty("Local Player", true, true);

    public List<EntityPlayer> collectedEntities = new ArrayList<>();

    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    private final int black = new Color(0, 0, 0, 150).getRGB();

    @Subscribe
    public void onRenderNametag(EventRenderTag e) {
        if (e.getEntityLivingBase() instanceof EntityPlayer)
            if (isValid((EntityPlayer) e.getEntityLivingBase()) && tags.getValue())
                e.setCancelled(true);
    }

    @Subscribe
    public void onRender2D(EventRender2D event) {
        GL11.glPushMatrix();
        this.collectedEntities.clear();
        this.collectEntities();
        double boxWidth = this.width.getValue();
        double scaling = event.getScaledResolution().getScaleFactor() / Math.pow(event.getScaledResolution().getScaleFactor(), 2.0);
        GlStateManager.scale(scaling, scaling, scaling);
        for (EntityPlayer entity : collectedEntities) {
            if (isValid(entity) && Render3DUtil.isInViewFrustrum(entity)) {
                double x = Render3DUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks());
                double y = Render3DUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks());
                double z = Render3DUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks());
                double width = entity.width / 1.5;
                double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(event.getScaledResolution(), vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
                    if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }
                mc.entityRenderer.setupOverlayRendering();
                if (position != null) {
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;

                    int color2;
                    if (teamBasedColors.getValue())
                        color2 = EntityUtil.isOnSameTeam(entity) ? 0x16687E : 0xFF0000;
                    else
                        color2 = (Client.INSTANCE.featureManager.killAura.getState() && Client.INSTANCE.featureManager.killAura.target == entity) ? targetColor.getColor().getRGB() : colorProperty.getColor().getRGB();

                    if (mode.getValue().equalsIgnoreCase("Box")) {
                        // Left
                        Render2DUtil.drawRect(posX - 1, posY, posX + boxWidth, endPosY + .5,
                                black);
                        // Top
                        Render2DUtil.drawRect(posX - 1, posY - .5, endPosX + .5, posY + .5 + boxWidth,
                                black);
                        // Right
                        Render2DUtil.drawRect(endPosX - .5 - boxWidth, posY, endPosX + .5, endPosY + .5,
                                black);
                        // Bottom
                        Render2DUtil.drawRect(posX - 1, endPosY - boxWidth - .5, endPosX + .5, endPosY + .5,
                                black);

                        // Left
                        Render2DUtil.drawRect(posX - .5, posY, posX + boxWidth - .5, endPosY,
                                color2);
                        // Bottom
                        Render2DUtil.drawRect(posX, endPosY - boxWidth, endPosX, endPosY,
                                color2);
                        // Top
                        Render2DUtil.drawRect(posX - .5, posY, endPosX, posY + boxWidth,
                                color2);
                        // Right
                        Render2DUtil.drawRect(endPosX - boxWidth, posY, endPosX, endPosY,
                                color2);
                    }

                    if (mode.getValue().equalsIgnoreCase("Corners")) {
                        Render2DUtil.drawRect(posX + .5, posY, posX - 1, posY + (endPosY - posY) / 4 + .5,
                                black);
                        Render2DUtil.drawRect(posX - 1, endPosY, posX + .5, endPosY - (endPosY - posY) / 4 - .5,
                                black);
                        Render2DUtil.drawRect(posX - 1, posY - .5, posX + (endPosX - posX) / 3 + .5, posY + 1,
                                black);
                        Render2DUtil.drawRect(endPosX - (endPosX - posX) / 3 - .5, posY - .5, endPosX, posY + 1,
                                black);
                        Render2DUtil.drawRect(endPosX - 1, posY, endPosX + .5, posY + (endPosY - posY) / 4 + .5,
                                black);
                        Render2DUtil.drawRect(endPosX - 1, endPosY, endPosX + .5, endPosY - (endPosY - posY) / 4 - .5,
                                black);
                        Render2DUtil.drawRect(posX - 1, endPosY - 1, posX + (endPosX - posX) / 3 + .5, endPosY + .5,
                                black);
                        Render2DUtil.drawRect(endPosX - (endPosX - posX) / 3 - .5, endPosY - 1, endPosX + .5, endPosY + .5,
                                black);
                        Render2DUtil.drawRect(posX, posY, posX - .5, posY + (endPosY - posY) / 4,
                                color2);
                        Render2DUtil.drawRect(posX, endPosY, posX - .5, endPosY - (endPosY - posY) / 4,
                                color2);
                        Render2DUtil.drawRect(posX - .5, posY, posX + (endPosX - posX) / 3, posY + .5,
                                color2);
                        Render2DUtil.drawRect(endPosX - (endPosX - posX) / 3, posY, endPosX, posY + .5,
                                color2);
                        Render2DUtil.drawRect(endPosX - .5, posY, endPosX, posY + (endPosY - posY) / 4,
                                color2);
                        Render2DUtil.drawRect(endPosX - .5, endPosY, endPosX, endPosY - (endPosY - posY) / 4,
                                color2);
                        Render2DUtil.drawRect(posX, endPosY - .5, posX + (endPosX - posX) / 3, endPosY,
                                color2);
                        Render2DUtil.drawRect(endPosX - (endPosX - posX) / 3, endPosY - .5, endPosX - .5, endPosY,
                                color2);
                    }
                    if (tags.getValue()) {
                        double dif = (endPosX - posX) / 2;
                        String colorCode = EntityUtil.isOnSameTeam(entity) ? "\247a" : "\247c";
                        mc.fontRendererObj.drawStringWithShadow(colorCode + entity.getName(), (float)(posX + dif) -  (mc.fontRendererObj.getStringWidth(entity.getName()) / 2), (float)((posY - (9 / 1.5f * 2.0f)) + 2.0f), color2);
                    }
                    if (healthBar.getValue() || healthNumber.getValue()) {
                        double hpPercentage = entity.getHealth() / entity.getMaxHealth();
                        if (hpPercentage > 1)
                            hpPercentage = 1;
                        else if (hpPercentage < 0)
                            hpPercentage = 0;

                        float health = entity.getHealth();

                        double hpHeight = (endPosY - posY) * hpPercentage;

                        double difference = posY - endPosY + 0.5;

                        if (health > 0 && healthBar.getValue()) {
                            Render2DUtil.drawOutline(posX - 4, posY - .5, 2, (endPosY - posY) + .5, .5, black);
                            Render2DUtil.drawRect(posX - 4, posY - .5, posX - 2, endPosY + .5, new Color(0, 0, 0, 70).getRGB());
                            Render2DUtil.drawRect(posX - 3.5, endPosY - hpHeight, posX - 2.5, endPosY, ColorHelper.getHealthColor(entity).getRGB());
                        }

                        if (-difference > 50.0)
                            for (int i = 1; i < 10; ++i) {
                                double increment = difference / 10.0 * i;
                                Render2DUtil.drawRect(posX - 3.5, endPosY - 0.5 + increment, posX - 2.5, endPosY - 0.5 + increment - 1.0, black);
                            }

                        if (healthNumber.getValue()) {
                            mc.fontRendererObj.drawStringWithShadow((int)(hpPercentage * 100) + "%", (float)(posX - 13), (float)(endPosY - hpHeight), -1);
                        }
                    }
                }
            }
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
        mc.entityRenderer.setupOverlayRendering();
    }

    private void collectEntities() {
        for (EntityPlayer entity : mc.theWorld.playerEntities) {
            if (isValid(entity))
                collectedEntities.add(entity);
        }
    }

    private Vector3d project2D(ScaledResolution scaledResolution, double x, double y, double z) {
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / scaledResolution.getScaleFactor(),
                    (Display.getHeight() - vector.get(1)) / scaledResolution.getScaleFactor(),
                    vector.get(2));
        }
        return null;
    }

    private boolean isValid(EntityPlayer entityLivingBase) {
        return ((!localPlayer.getValue() || entityLivingBase != mc.thePlayer)) && !entityLivingBase.isDead && !entityLivingBase.isInvisible();
    }
}
