package cedo.modules.render;


import cedo.events.Event;
import cedo.events.listeners.EventRenderWorld;
import cedo.events.listeners.EventUpdateModel;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static org.lwjgl.opengl.GL11.*;

public class Skeletons extends Module {
    private final Map<EntityPlayer, float[][]> playerRotationMap = new WeakHashMap<>();

    public BooleanSetting smoothLines = new BooleanSetting("Smooth-Lines", true);

    public Skeletons() {
        super("Skeletons", Keyboard.KEY_NONE, Category.RENDER);
        addSettings(smoothLines);
    }


    public void onEvent(Event e) {
        if (e instanceof EventUpdateModel) {
            EventUpdateModel event = (EventUpdateModel) e;
            final ModelPlayer model = event.modelPlayer;
            playerRotationMap.put((EntityPlayer) event.entity, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ},
                    {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ},
                    {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ},
                    {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ},
                    {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
        }


        if (e instanceof EventRenderWorld) {
            setupRender(true);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glDisable(2848);
            playerRotationMap.keySet().removeIf(this::contain);
            final Map<EntityPlayer, float[][]> playerRotationMap = this.playerRotationMap;
            final List<EntityPlayer> worldPlayers = mc.theWorld.playerEntities;
            final Object[] players = playerRotationMap.keySet().toArray();
            final Minecraft mc = Minecraft.getMinecraft();
            for (int i = 0, playersLength = players.length; i < playersLength; i++) {
                final EntityPlayer player = (EntityPlayer) players[i];
                float[][] entPos = playerRotationMap.get(player);
                if (entPos != null && player.getEntityId() != -1488 && player.isEntityAlive() && RenderUtil.isInViewFrustrum(player) && !player.isDead && player != mc.thePlayer && !player.isPlayerSleeping() && !player.isInvisible()) {

                    glPushMatrix();
                    final float[][] modelRotations = playerRotationMap.get(player);
                    glLineWidth(1.0f);
                    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    final double x = RenderUtil.interpolate(player.posX, player.lastTickPosX, mc.timer.elapsedPartialTicks) - mc.getRenderManager().getRenderPosX();
                    final double y = RenderUtil.interpolate(player.posY, player.lastTickPosY, mc.timer.elapsedPartialTicks) - mc.getRenderManager().getRenderPosY();
                    final double z = RenderUtil.interpolate(player.posZ, player.lastTickPosZ, mc.timer.elapsedPartialTicks) - mc.getRenderManager().getRenderPosZ();
                    glTranslated(x, y, z);
                    final float bodyYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * mc.timer.renderPartialTicks;
                    glRotatef(-bodyYawOffset, 0.0f, 1.0f, 0.0f);
                    glTranslated(0.0, 0.0, player.isSneaking() ? -0.235 : 0.0);
                    final float legHeight = player.isSneaking() ? 0.6f : 0.75f;
                    final float rad = (float) (180.0 / Math.PI);
                    glPushMatrix();
                    glTranslated(-0.125, legHeight, 0.0);
                    if (modelRotations[3][0] != 0.0f) {
                        glRotatef(modelRotations[3][0] * rad, 1.0f, 0.0f, 0.0f);
                    }
                    if (modelRotations[3][1] != 0.0f) {
                        glRotatef(modelRotations[3][1] * rad, 0.0f, 1.0f, 0.0f);
                    }
                    if (modelRotations[3][2] != 0.0f) {
                        glRotatef(modelRotations[3][2] * rad, 0.0f, 0.0f, 1.0f);
                    }
                    glBegin(3);
                    glVertex3d(0.0, 0.0, 0.0);
                    glVertex3d(0.0, -legHeight, 0.0);
                    glEnd();
                    glPopMatrix();
                    glPushMatrix();
                    glTranslated(0.125, legHeight, 0.0);
                    if (modelRotations[4][0] != 0.0f) {
                        glRotatef(modelRotations[4][0] * rad, 1.0f, 0.0f, 0.0f);
                    }
                    if (modelRotations[4][1] != 0.0f) {
                        glRotatef(modelRotations[4][1] * rad, 0.0f, 1.0f, 0.0f);
                    }
                    if (modelRotations[4][2] != 0.0f) {
                        glRotatef(modelRotations[4][2] * rad, 0.0f, 0.0f, 1.0f);
                    }
                    glBegin(3);
                    glVertex3d(0.0, 0.0, 0.0);
                    glVertex3d(0.0, -legHeight, 0.0);
                    glEnd();
                    glPopMatrix();
                    glTranslated(0.0, 0.0, player.isSneaking() ? 0.25 : 0.0);
                    glPushMatrix();
                    glTranslated(0.0, player.isSneaking() ? -0.05 : 0.0, player.isSneaking() ? -0.01725 : 0.0);

                    // Left arm
                    glPushMatrix();
                    glTranslated(-0.375, legHeight + 0.55D, 0.0);
                    if (modelRotations[1][0] != 0.0f) {
                        glRotatef(modelRotations[1][0] * rad, 1.0f, 0.0f, 0.0f);
                    }
                    if (modelRotations[1][1] != 0.0f) {
                        glRotatef(modelRotations[1][1] * rad, 0.0f, 1.0f, 0.0f);
                    }
                    if (modelRotations[1][2] != 0.0f) {
                        glRotatef(-modelRotations[1][2] * rad, 0.0f, 0.0f, 1.0f);
                    }
                    glBegin(3);
                    glVertex3d(0.0, 0.0, 0.0);
                    glVertex3d(0.0, -0.5, 0.0);
                    glEnd();
                    glPopMatrix();

                    // Right arm
                    glPushMatrix();
                    glTranslated(0.375, legHeight + 0.55D, 0.0);
                    if (modelRotations[2][0] != 0.0f) {
                        glRotatef(modelRotations[2][0] * rad, 1.0f, 0.0f, 0.0f);
                    }
                    if (modelRotations[2][1] != 0.0f) {
                        glRotatef(modelRotations[2][1] * rad, 0.0f, 1.0f, 0.0f);
                    }
                    if (modelRotations[2][2] != 0.0f) {
                        glRotatef(-modelRotations[2][2] * rad, 0.0f, 0.0f, 1.0f);
                    }
                    glBegin(3);
                    glVertex3d(0.0, 0.0, 0.0);
                    glVertex3d(0.0, -0.5, 0.0);
                    glEnd();
                    glPopMatrix();
                    glRotatef(bodyYawOffset - player.rotationYawHead, 0.0f, 1.0f, 0.0f);

                    // Head
                    glPushMatrix();
                    glTranslated(0.0, legHeight + 0.55D, 0.0);
                    if (modelRotations[0][0] != 0.0f) {
                        glRotatef(modelRotations[0][0] * rad, 1.0f, 0.0f, 0.0f);
                    }
                    glBegin(3);
                    glVertex3d(0.0, 0.0, 0.0);
                    glVertex3d(0.0, 0.3, 0.0);
                    glEnd();
                    glPopMatrix();

                    glPopMatrix();
                    glRotatef(player.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
                    glTranslated(0.0, player.isSneaking() ? -0.16175 : 0.0, player.isSneaking() ? -0.48025 : 0.0);

                    // Pelvis
                    glPushMatrix();
                    glTranslated(0.0, legHeight, 0.0);
                    glBegin(3);
                    glVertex3d(-0.125, 0.0, 0.0);
                    glVertex3d(0.125, 0.0, 0.0);
                    glEnd();
                    glPopMatrix();

                    // Body
                    glPushMatrix();
                    glTranslated(0.0, legHeight, 0.0);
                    glBegin(3);
                    glVertex3d(0.0, 0.0, 0.0);
                    glVertex3d(0.0, 0.55, 0.0);
                    glEnd();
                    glPopMatrix();

                    // Shoulder
                    glPushMatrix();
                    glTranslated(0.0, legHeight + 0.55D, 0.0);
                    glBegin(3);
                    glVertex3d(-0.375, 0.0, 0.0);
                    glVertex3d(0.375, 0.0, 0.0);
                    glEnd();
                    glPopMatrix();

                    glPopMatrix();
                }
            }
            setupRender(false);
        }
    }

    private void setupRender(boolean start) {
        final boolean smooth = smoothLines.isEnabled();

        if (start) {
            if (smooth) {
                RenderUtil.startSmooth();
            } else {
                glDisable(GL_LINE_SMOOTH);
            }
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_TEXTURE_2D);
        } else {
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_DEPTH_TEST);
            if (smooth) {
                RenderUtil.endSmooth();
            }
        }

        GL11.glDepthMask(!start);
    }

    private boolean contain(EntityPlayer var0) {
        return !mc.theWorld.playerEntities.contains(var0);
    }
}
