package com.etb.client.module.modules.render;

import java.awt.Color;

import com.etb.client.utils.MathUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;

import com.etb.client.Client;
import com.etb.client.event.events.render.Render3DEvent;
import com.etb.client.module.Module;
import com.etb.client.module.modules.combat.KillAura;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.value.impl.NumberValue;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Module {

    public Tracers() {
        super("Tracers", Category.RENDER, new Color(0xFF71EE6D).getRGB());
        setDescription("Lines to players");
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) o;
            if ((entity.isEntityAlive() && entity instanceof EntityPlayer && entity != mc.thePlayer)) {
                final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()
                        - RenderManager.renderPosX;
                final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()
                        - RenderManager.renderPosY;
                final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()
                        - RenderManager.renderPosZ;
                boolean old = mc.gameSettings.viewBobbing;
                RenderUtil.startDrawing();
                mc.gameSettings.viewBobbing = false;
                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
                mc.gameSettings.viewBobbing = old;
                float color = Math.round(255D - mc.thePlayer.getDistanceSqToEntity(entity) * 255D
                        / MathUtils.square(mc.gameSettings.renderDistanceChunks * 2.5)) / 255F;
                drawLine(entity, Client.INSTANCE.getFriendManager().isFriend(entity.getName()) ? new double[]{0, 1, 1} : new double[]{color, 1 - color, 0},posX, posY, posZ);
                RenderUtil.stopDrawing();
            }
        }
        GL11.glColor4f(1,1,1,1);
    }

    private void drawLine(Entity entity, double[] color, double x, double y, double z) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        if (color.length >= 4) {
            if (color[3] <= 0.1) {
                return;
            }
            GL11.glColor4d(color[0], color[1], color[2], color[3]);
        } else {
            GL11.glColor3d(color[0], color[1], color[2]);
        }
        GL11.glLineWidth(1);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}
