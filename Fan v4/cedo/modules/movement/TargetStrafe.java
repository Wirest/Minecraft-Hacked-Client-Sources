package cedo.modules.movement;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventMove;
import cedo.events.listeners.EventRenderWorld;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.elements.Draw;
import cedo.util.ColorManager;
import cedo.util.movement.MovementUtil;
import cedo.util.random.RotationUtils;
import cedo.util.render.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public final class TargetStrafe extends Module {
    private final ModeSetting color = new ModeSetting("Color", "Rainbow", "Rainbow", "Astolfo");
    private final NumberSetting radius = new NumberSetting("Radius", 2.0D, 0.1D, 4.0D, 0.1D);
    private final NumberSetting sides = new NumberSetting("Circle Sides", 10.0D, 3, 20, 1D);
    private final BooleanSetting render = new BooleanSetting("Render", true);
    private final BooleanSetting retardMonkey = new BooleanSetting("Render On Player", true);
    private final BooleanSetting space = new BooleanSetting("Hold Space", true);
    double strafe;

    public TargetStrafe() {
        super("TargetStrafe", Keyboard.KEY_U, Category.MOVEMENT);
        addSettings(radius, render, space, sides, retardMonkey, color);
    }

    public void onEvent(Event e) {

        if (e instanceof EventMove) {

            if (mc.thePlayer.moveStrafing != 0)
                strafe = mc.thePlayer.moveStrafing;
        } else if (e instanceof EventRenderWorld) {
            if (retardMonkey.isEnabled()) {
                drawCircleAtTarget();
                glColor3f(255, 255, 255);
            }

            if (this.canStrafe()) {
                if (render.isEnabled()) {
                    drawCircleAtTarget();
                    glColor3f(255, 255, 255);
                }
            }
        }
    }

    public boolean drawCircleAtTarget() {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        RenderUtil.startSmooth();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(6.0f);
        glBegin(GL_LINE_STRIP);

        EntityLivingBase entity = mc.thePlayer;
        if (this.canStrafe()) {
            entity = Fan.killaura.target;
        }

        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.elapsedPartialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.elapsedPartialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.elapsedPartialTicks - mc.getRenderManager().viewerPosZ;


        int color1 = new Color(255, 255, 255).getRGB();

        switch (color.getSelected()) {

            case "Rainbow":
                color1 = ColorManager.rainbow(0, 12).getRGB();
                break;


            case "Astolfo":
                color1 = ColorManager.getAstoGayColor(3000, 0).getRGB();
                break;
        }
        double pix2 = Math.PI * 2.0D;
        for (int i = 0; i <= 90; ++i) {
            Draw.color(color1);
            glVertex3d(x + radius.getValue() * Math.cos(i * pix2 / sides.getValue()), y, z + radius.getValue() * Math.sin(i * pix2 / sides.getValue()));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        RenderUtil.endSmooth();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        RenderUtil.startSmooth();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(2.0f);
        glBegin(GL_LINE_STRIP);

        float r1 = ((float) 1 / 255) * Color.black.getRed();
        float g1 = ((float) 1 / 255) * Color.black.getGreen();
        float b1 = ((float) 1 / 255) * Color.black.getBlue();

        for (int i = 0; i <= 90; ++i) {
            glColor3f(r1, g1, b1);
            glVertex3d(x + (radius.getValue() + 0.01) * Math.cos(i * pix2 / sides.getValue()), y, z + (radius.getValue() + 0.01) * Math.sin(i * pix2 / sides.getValue()));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        RenderUtil.endSmooth();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        RenderUtil.startSmooth();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(2.0f);
        glBegin(GL_LINE_STRIP);


        for (int i = 0; i <= 90; ++i) {
            glColor3f(r1, g1, b1);
            glVertex3d(x + (radius.getValue() - 0.01) * Math.cos(i * pix2 / sides.getValue()), y, z + (radius.getValue() - 0.01) * Math.sin(i * pix2 / sides.getValue()));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
        RenderUtil.endSmooth();
        return true;
    }

    public void strafe(EventMove event, final double moveSpeed) {
        final EntityLivingBase target = Fan.killaura.target;
        float[] rotations = RotationUtils.getRotationsEntity(target);
        if (mc.thePlayer.getDistanceToEntity(target) <= radius.getValue()) {
            MovementUtil.setSpeed(event, moveSpeed, rotations[0], 1, 0);
        } else {
            MovementUtil.setSpeed(event, moveSpeed, rotations[0], 0, 1);
        }
    }

    public boolean canStrafe() {
        return (Fan.speed.isEnabled() || Fan.fly.isEnabled()) && Fan.killaura.isEnabled() && Fan.killaura.target instanceof EntityPlayer && isEnabled() && (!space.isEnabled() || mc.gameSettings.keyBindJump.isKeyDown());
    }
}
