// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.render;

import net.minecraft.client.renderer.OpenGlHelper;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import me.nico.hush.utils.ESPUtils;
import me.nico.hush.utils.Event3DRender;
import com.darkmagician6.eventapi.EventManager;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class ESP extends Module
{
    public static boolean enabled;
    
    public ESP() {
        super("ESP", "ESP", 16777215, 0, Category.RENDER);
        Client.instance.settingManager.rSetting(new Setting("Players", this, true));
        Client.instance.settingManager.rSetting(new Setting("Outline", this, true));
        Client.instance.settingManager.rSetting(new Setting("Box", this, false));
        Client.instance.settingManager.rSetting(new Setting("Other", this, false));
        Client.instance.settingManager.rSetting(new Setting("Tracers", this, false));
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        ESP.enabled = true;
    }
    
    @Override
    public void onDisable() {
        EventManager.register(this);
        ESP.enabled = false;
    }
    
    @EventTarget
    public void onEvent(final Event3DRender event) {
        ESPUtils.startDrawing();
        for (final Object o : ESP.mc.theWorld.getLoadedEntityList()) {
            final Entity entity = (Entity)o;
            if (this.isTargetValid(entity)) {
                ESP.mc.getRenderManager();
                final double x = ESPUtils.interpolate(entity.lastTickPosX, entity.posX, event.getPartialTicks(), RenderManager.renderPosX);
                ESP.mc.getRenderManager();
                final double y = ESPUtils.interpolate(entity.lastTickPosY, entity.posY, event.getPartialTicks(), RenderManager.renderPosY);
                ESP.mc.getRenderManager();
                final double z = ESPUtils.interpolate(entity.lastTickPosZ, entity.posZ, event.getPartialTicks(), RenderManager.renderPosZ);
                if (!Client.instance.settingManager.getSettingByName("Box").getValBoolean()) {
                    continue;
                }
                AxisAlignedBB box = AxisAlignedBB.fromBounds(x - entity.width, y, z - entity.width, x + entity.width, y + entity.height + 0.2, z + entity.width);
                float maxHealth = 0.0f;
                float health = 0.0f;
                if (entity instanceof EntityLivingBase) {
                    box = AxisAlignedBB.fromBounds(x - entity.width + 0.2, y, z - entity.width + 0.2, x + entity.width - 0.2, y + entity.height + (entity.isSneaking() ? 0.02 : 0.2), z + entity.width - 0.2);
                    maxHealth = ((EntityLivingBase)entity).getMaxHealth() + ((EntityLivingBase)entity).getAbsorptionAmount();
                    health = ((EntityLivingBase)entity).getHealth() + ((EntityLivingBase)entity).getAbsorptionAmount();
                }
                float[] colour = null;
                if (entity.isSneaking()) {
                    colour = new float[] { 0.3f, 0.7f, 1.0f };
                }
                else if (entity instanceof EntityEnderPearl) {
                    colour = new float[] { 0.7f, 0.0f, 1.0f };
                }
                else if (entity.hurtResistantTime > 0) {
                    colour = new float[] { 1.0f, 0.0f, 0.0f };
                }
                else {
                    final Entity entity2 = entity;
                    final Minecraft mc = ESP.mc;
                    if (entity2.isInvisibleToPlayer(Minecraft.thePlayer)) {
                        colour = new float[] { 1.0f, 0.9f, 0.0f };
                    }
                    else if (entity instanceof EntityLivingBase) {
                        float green = health / maxHealth;
                        float red = 1.0f - green;
                        if (red > green) {
                            green += Math.abs(red - 1.0f);
                            red += Math.abs(red - 1.0f);
                        }
                        else {
                            red += Math.abs(green - 1.0f);
                            green += Math.abs(green - 1.0f);
                        }
                        colour = new float[] { red, green, 0.0f };
                    }
                }
                if (Client.instance.settingManager.getSettingByName("Tracers").getValBoolean()) {
                    GL11.glPushMatrix();
                    GL11.glColor3f(colour[0], colour[1], colour[2]);
                    final double x2 = 0.0;
                    final Minecraft mc2 = ESP.mc;
                    GL11.glTranslated(x2, Minecraft.thePlayer.height / 2.0f, 0.0);
                    GL11.glBegin(2);
                    GL11.glVertex3d(0.0, 0.0, 0.0);
                    final double posX = entity.posX;
                    final Minecraft mc3 = ESP.mc;
                    final double x3 = posX - Minecraft.thePlayer.posX;
                    final double posY = entity.posY;
                    final Minecraft mc4 = ESP.mc;
                    final double y2 = posY - Minecraft.thePlayer.posY;
                    final double posZ = entity.posZ;
                    final Minecraft mc5 = ESP.mc;
                    GL11.glVertex3d(x3, y2, posZ - Minecraft.thePlayer.posZ);
                    GL11.glEnd();
                    GL11.glPopMatrix();
                }
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-entity.rotationYaw, 0.0f, entity.height, 0.0f);
                GlStateManager.translate(-x, -y, -z);
                if (Client.instance.settingManager.getSettingByName("Other").getValBoolean()) {
                    ESPUtils.drawLines(box);
                }
                ESPUtils.drawOutlinedBoundingBox(box);
                GL11.glColor3f(255.0f, 255.0f, 255.0f);
                GlStateManager.popMatrix();
            }
        }
        ESPUtils.stopDrawing();
    }
    
    public static void renderOne() {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glLineWidth(0.5f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }
    
    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderFour() {
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }
    
    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }
    
    public boolean isTargetValid(final Entity entity) {
        if (entity != null && entity.ticksExisted > 20 && entity.getEntityId() != -1) {
            final Minecraft mc = ESP.mc;
            if (entity == Minecraft.thePlayer || !(entity instanceof EntityPlayer) || Client.instance.settingManager.getSettingByName("Players").getValBoolean()) {}
        }
        return this.toggled;
    }
}
