package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event3D;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.lang.annotation.Target;

import static org.lwjgl.opengl.GL11.*;

public class TargetStrafe extends Module {
    public static Setting radius;
    public static Setting spacebar;
    public TargetStrafe(){
        super("TargetStrafe", Keyboard.KEY_NONE, Category.MOVEMENT);
        Execution.instance.getSettingsManager().rSetting(radius = new Setting("Radius", this, 3, 0.5, 5, false));
        Execution.instance.getSettingsManager().rSetting(spacebar = new Setting("SpaceBar", this, false));
    }

    @EventTarget
    public final void onRender3D(Event3D event) {
        if (canStrafe()) {
            EntityLivingBase target = Execution.instance.getModuleManager().getModule(Aura.class).target;
            glColor3f(rainbow(100).getRed(), rainbow(100).getGreen(), rainbow(100).getRed());

            drawCircle(target, event.getPartialTicks(), radius.getValDouble());
        }
    }

    public static void strafe(EventMotion event, double moveSpeed, EntityLivingBase target,  boolean direction) {
        double direction1 = direction ? 1 : -1;
        float[] rotations = RotationUtils.getRotations(target);
        if ((double) Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= radius.getValDouble()) {
            MoveUtils.setSpeed(event, moveSpeed, rotations[0], direction1, 0.0D);
        } else {
            MoveUtils.setSpeed(event, moveSpeed, rotations[0], direction1, 1.0D);
        }

    }

    public static void strafe(EventMotionUpdate event, double moveSpeed, EntityLivingBase target, boolean direction) {
        double direction1 = direction ? 1 : -1;
        float[] rotations = RotationUtils.getRotations(target);
        if ((double) Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= radius.getValDouble()) {
            MoveUtils.setSpeed(event, moveSpeed, rotations[0], direction1, 0.0D);
        } else {
            MoveUtils.setSpeed(event, moveSpeed, rotations[0], direction1, 1.0D);
        }

    }
    public static boolean canStrafe(){
        return spacebar.getValBoolean() ? Execution.instance.getModuleManager().getModuleByName("Aura").isEnabled && Execution.instance.getModuleManager().getModule(Aura.class).target != null && MoveUtils.isMoving() && Execution.instance.getModuleManager().getModule(TargetStrafe.class).isEnabled && Minecraft.getMinecraft().gameSettings.keyBindJump.pressed: Execution.instance.getModuleManager().getModuleByName("Aura").isEnabled && Execution.instance.getModuleManager().getModule(Aura.class).target != null && MoveUtils.isMoving() && Execution.instance.getModuleManager().getModule(TargetStrafe.class).isEnabled;
    }

    private void drawCircle(Entity entity, float partialTicks, double rad) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(2.0f);
        glBegin(GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        final float r = ((float) 1 / 255) * Color.WHITE.getRed();
        final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
        final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

        final double pix2 = Math.PI * 2.0D;

        for (int i = 0; i <= 6; ++i) {
            GlStateManager.color(rainbow(i * 100).getRed(), rainbow(i * 100).getGreen(), rainbow(i * 100).getRed(), 255);
            glVertex3d(x + rad * Math.cos(i * pix2 / 6.0), y, z + rad * Math.sin(i * pix2 / 6.0));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }
    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f);
    }


}
