package cedo.modules.movement;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventMove;
import cedo.events.listeners.EventRenderWorld;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.movement.MovementUtil;
import cedo.util.random.EntityValidator;
import cedo.util.random.RotationUtils;
import cedo.util.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;


public final class TargetStrafe extends Module {
    private final NumberSetting radius = new NumberSetting("Radius", 2.0D, 0.1D, 4.0D, 0.1D);
    private final BooleanSetting render = new BooleanSetting("Render", true);
    private final BooleanSetting space = new BooleanSetting("Hold Space", true);
    EntityValidator targetValidator;
    cedo.modules.combat.Killaura killaura = Fan.killaura;
    private int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", Keyboard.KEY_U, Category.MOVEMENT);
        addSettings(radius, render, space);
    }


    public void onEvent(Event e) {
        if (e instanceof EventMove) {
            EventMove event = (EventMove) e;
            if (mc.thePlayer.isCollidedHorizontally) {
                switchDirection();
            }

            if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                direction = 1;
            }
            if (mc.gameSettings.keyBindRight.isKeyDown()) {
                direction = -1;
            }
        }
        if (e instanceof EventRenderWorld) {
            if (canStrafe() && render.isEnabled()) {
                drawCircle(killaura.target, mc.timer.elapsedPartialTicks, radius.getValue());
            }
        }
    }


    private void switchDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }

    public void strafe(EventMove event, final double moveSpeed) {
        final EntityLivingBase target = Fan.killaura.target;
        float[] rotations = RotationUtils.getRotationsEntity(target);
        if (mc.thePlayer.getDistanceToEntity(target) <= radius.getValue()) {
            MovementUtil.setSpeed(event, moveSpeed, rotations[0], direction, 0);
        } else {
            MovementUtil.setSpeed(event, moveSpeed, rotations[0], direction, 1);
        }
    }


    private void drawCircle(Entity entity, float partialTicks, double rad) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        RenderUtil.startSmooth();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(1.0f);
        glBegin(GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        final float r = ((float) 1 / 255) * Color.WHITE.getRed();
        final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
        final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

        final double pix2 = Math.PI * 2.0D;

        for (int i = 0; i <= 90; ++i) {
            glColor3f(r, g, b);
            glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y, z + rad * Math.sin(i * pix2 / 45.0));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        RenderUtil.endSmooth();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public boolean canStrafe() {
        return (Fan.speed.isEnabled() || Fan.fly.isEnabled()) && killaura.isEnabled() && killaura.target != null && killaura.target instanceof EntityPlayer && isEnabled() && (!space.isEnabled() || mc.gameSettings.keyBindJump.isKeyDown());
    }
}
