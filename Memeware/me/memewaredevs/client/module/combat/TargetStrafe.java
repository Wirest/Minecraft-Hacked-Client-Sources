package me.memewaredevs.client.module.combat;


import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.Render3DEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

public class TargetStrafe extends Module {

    public static double dir = 1;

    public TargetStrafe(String name, int key, Category category) {
        super(name, key, category);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = event -> {
        if (event.isPre()) {
            if (mc.thePlayer.isCollidedHorizontally) {
                invertStrafe();
            }
            if (canStrafe()) {
                mc.thePlayer.movementInput.setForward(0);
            }
        }
    };

    private void invertStrafe() {
        dir = -dir;
    }

    @Handler
    public final void onRender3D(Render3DEvent event) {
        if (canStrafe()) {
            Entity entity = Aura.currentEntity;
            double ticks = event.getRenderTick();
            double radius = Module.getInstance(Aura.class).getDouble("Range") - 1;
            final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticks - mc.getRenderManager().viewerPosX;
            final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticks - mc.getRenderManager().viewerPosY;
            final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticks - mc.getRenderManager().viewerPosZ;

            final float r = Color.darkGray.getRed() / 255.0F;
            final float g = Color.darkGray.getGreen() / 255.0F;
            final float b = Color.darkGray.getBlue() / 255.0F;
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glLineWidth(3.0f);
            GL11.glBegin(GL11.GL_LINE_STRIP);

            for (int i = 0; i <= 90; ++i) {
                GL11.glColor3f(r, g, b);
                GL11.glVertex3d(x + radius * Math.cos(i * (Math.PI / 2.0F) / 45.0F), y, z + radius * Math.sin(i * (Math.PI / 2.0F) / 45.0F));
            }

            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    public static boolean canStrafe() {
        return Aura.currentEntity != null && Module.getInstance(TargetStrafe.class).isToggled();
    }
}
