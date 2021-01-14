package com.mentalfrostbyte.jello.util;

import java.util.AbstractMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityUtils
{
    public static float getModelSize(final EntityLivingBase ent) {
        final Render render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject((Entity)ent);
        if (render instanceof RendererLivingEntity) {
            final RendererLivingEntity entRender = (RendererLivingEntity)render;
        }
        return 1.8f;
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final float scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableColorMaterial();
        //GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.translate((float)posX, (float)posY, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float f2 = ent.renderYawOffset;
        final float f3 = ent.rotationYaw;
        final float f4 = ent.rotationPitch;
        final float f5 = ent.prevRotationYawHead;
        final float f6 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
        ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        ent.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        //try {
            final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.func_178631_a(180.0f);
            rendermanager.func_178633_a(false);
            rendermanager.renderEntityWithPosYaw((EntityPlayer)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f);
            rendermanager.func_178633_a(true);
        //}
       // finally {
            ent.renderYawOffset = f2;
            ent.rotationYaw = f3;
            ent.rotationPitch = f4;
            ent.prevRotationYawHead = f5;
            ent.rotationYawHead = f6;
           // GlStateManager.popMatrix();
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.translate(0.0f, 0.0f, 20.0f);
        //}
    }
    
    public static float getEntityScale(final EntityLivingBase ent, final float baseScale, final float targetHeight) {
        return targetHeight / Math.max(ent.width, ent.height) * baseScale;
    }
    
    public static EntityLivingBase getRandomLivingEntity(final World world) {
        return getRandomLivingEntity(world, null, 5, null);
    }
    
    public static EntityLivingBase getRandomLivingEntity(final World world, final List blacklist, final int numberOfAttempts, final List<AbstractMap.SimpleEntry<UUID, String>> fallbackPlayerNames) {
        final Random random = new Random();
        final Set entities = new TreeSet(EntityList.stringToClassMapping.keySet());
        if (blacklist != null) {
            entities.removeAll(blacklist);
        }
        final Object[] entStrings = entities.toArray(new Object[0]);
        int tries = 0;
        Class clazz;
        int id;
        do {
            id = random.nextInt(entStrings.length);
            clazz = (Class) EntityList.stringToClassMapping.get(entStrings[id]);
        } while (!EntityLivingBase.class.isAssignableFrom(clazz) && ++tries <= numberOfAttempts);
        if (EntityLivingBase.class.isAssignableFrom(clazz)) {
            /*if (bspkrsCoreMod.instance.allowDebugOutput) {
                BSLog.info(entStrings[id].toString(), new Object[0]);
            }*/
            return (EntityLivingBase)EntityList.createEntityByName((String)entStrings[id], world);
        }
        if (fallbackPlayerNames != null) {
            final AbstractMap.SimpleEntry<UUID, String> entry = fallbackPlayerNames.get(random.nextInt(fallbackPlayerNames.size()));
            return (EntityLivingBase)new EntityOtherPlayerMP(world, Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile((UUID)entry.getKey(), (String)entry.getValue()), true));
        }
        return (EntityLivingBase)EntityList.createEntityByName("Chicken", world);
    }

	public static void updateLightmap(final Minecraft mc, final World world) {
        final float f = world.getSunBrightness(1.0f);
        final float f2 = f * 0.95f + 0.05f;
        for (int i = 0; i < 256; ++i) {
            final float f3 = world.provider.getLightBrightnessTable()[i / 16] * f2;
            final float f4 = world.provider.getLightBrightnessTable()[i % 16] * (mc.entityRenderer.torchFlickerX * 0.1f + 1.5f);
            final float f5 = f3 * (f * 0.65f + 0.35f);
            final float f6 = f3 * (f * 0.65f + 0.35f);
            final float f7 = f4 * ((f4 * 0.6f + 0.4f) * 0.6f + 0.4f);
            final float f8 = f4 * (f4 * f4 * 0.6f + 0.4f);
            float f9 = f5 + f4;
            float f10 = f6 + f7;
            float f11 = f3 + f8;
            f9 = f9 * 0.96f + 0.03f;
            f10 = f10 * 0.96f + 0.03f;
            f11 = f11 * 0.96f + 0.03f;
            if (f9 > 1.0f) {
                f9 = 1.0f;
            }
            if (f10 > 1.0f) {
                f10 = 1.0f;
            }
            if (f11 > 1.0f) {
                f11 = 1.0f;
            }
            final float f12 = mc.gameSettings.gammaSetting;
            float f13 = 1.0f - f9;
            float f14 = 1.0f - f10;
            float f15 = 1.0f - f11;
            f13 = 1.0f - f13 * f13 * f13 * f13;
            f14 = 1.0f - f14 * f14 * f14 * f14;
            f15 = 1.0f - f15 * f15 * f15 * f15;
            f9 = f9 * (1.0f - f12) + f13 * f12;
            f10 = f10 * (1.0f - f12) + f14 * f12;
            f11 = f11 * (1.0f - f12) + f15 * f12;
            f9 = f9 * 0.96f + 0.03f;
            f10 = f10 * 0.96f + 0.03f;
            f11 = f11 * 0.96f + 0.03f;
            if (f9 > 1.0f) {
                f9 = 1.0f;
            }
            if (f10 > 1.0f) {
                f10 = 1.0f;
            }
            if (f11 > 1.0f) {
                f11 = 1.0f;
            }
            if (f9 < 0.0f) {
                f9 = 0.0f;
            }
            if (f10 < 0.0f) {
                f10 = 0.0f;
            }
            if (f11 < 0.0f) {
                f11 = 0.0f;
            }
            final int j = 255;
            final int k = (int)(f9 * 255.0f);
            final int l = (int)(f10 * 255.0f);
            final int i2 = (int)(f11 * 255.0f);
            mc.entityRenderer.lightmapColors[i] = (0xFF000000 | k << 16 | l << 8 | i2);
        }
        mc.entityRenderer.lightmapTexture.updateDynamicTexture();
    }
}
