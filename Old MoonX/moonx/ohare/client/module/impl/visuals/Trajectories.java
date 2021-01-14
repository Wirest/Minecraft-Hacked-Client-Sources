package moonx.ohare.client.module.impl.visuals;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.render.Render3DEvent;
import moonx.ohare.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.*;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;

public class Trajectories extends Module {

    public Trajectories() {
        super("Trajectories", Category.VISUALS, new Color(0xC1C039).getRGB());
    }


    @Handler
    public void onRender(Render3DEvent event) {
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        getMc().entityRenderer.orientCamera(event.getPartialTicks());
        GL11.glTranslated(-getMc().getRenderManager().getRenderPosX(), -getMc().getRenderManager().getRenderPosY(), -getMc().getRenderManager().getRenderPosZ());

        final EntityPlayerSP player = getMc().thePlayer;
        if (getMc().thePlayer.getHeldItem() != null && isThrowable(getMc().thePlayer.getHeldItem().getItem())) {
            double x = player.lastTickPosX
                    + (player.posX - player.lastTickPosX)
                    * (double) event.getPartialTicks()
                    - (double) (MathHelper.cos((float) Math.toRadians((double) player.rotationYaw)) * 0.16F);
            double y = player.lastTickPosY
                    + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks()
                    + (double) player.getEyeHeight() - 0.100149011612D;
            double z = player.lastTickPosZ
                    + (player.posZ - player.lastTickPosZ)
                    * (double) event.getPartialTicks()
                    - (double) (MathHelper.sin((float) Math.toRadians((double) player.rotationYaw)) * 0.16F);
            float con = 1.0F;
            if (!(player.inventory.getCurrentItem().getItem() instanceof ItemBow)) {
                con = 0.4F;
            }

            double motionX = (double) (-MathHelper.sin((float) Math.toRadians((double) player.rotationYaw))
                    * MathHelper.cos((float) Math.toRadians((double) player.rotationPitch)) * con);
            double motionZ = (double) (MathHelper.cos((float) Math.toRadians((double) player.rotationYaw))
                    * MathHelper.cos((float) Math.toRadians((double) player.rotationPitch)) * con);
            double motionY = (double) (-MathHelper.sin((float) Math.toRadians((double) player.rotationPitch)) * con);
            double ssum = Math.sqrt(motionX * motionX
                    + motionY * motionY + motionZ
                    * motionZ);

            motionX /= ssum;
            motionY /= ssum;
            motionZ /= ssum;

            GL11.glColor4d(1.0f, 0f, 0f, .5);

            if (player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
                float pow = (float) (72000 - player.getItemInUseCount()) / 20.0F;
                pow = (pow * pow + pow * 2.0F) / 3.0F;

                if (pow > 1.0F) {
                    pow = 1.0F;
                }

                if (pow <= 0.1F) {
                    pow = 1.0F;
                }

                pow *= 2.0F;
                pow *= 1.5F;
                motionX *= (double) pow;
                motionY *= (double) pow;
                motionZ *= (double) pow;
            } else {
                motionX *= 1.5D;
                motionY *= 1.5D;
                motionZ *= 1.5D;
            }

            GL11.glPushMatrix();
            enableDefaults();
            GL11.glLineWidth(1.5F);

            GL11.glBegin(GL11.GL_LINE_STRIP);
            double gravity = this.getGravity(player.inventory.getCurrentItem().getItem());

            for (int q = 0; q < 1000; ++q) {
                double rx = x * 1.0D;
                double ry = y * 1.0D;
                double rz = z * 1.0D;

                GL11.glColor3d(1, 0, 0);
                GL11.glVertex3d(rx, ry, rz);

                x += motionX;
                y += motionY;
                z += motionZ;
                motionX *= 0.99D;
                motionY *= 0.99D;
                motionZ *= 0.99D;
                motionY -= gravity;
            }

            GL11.glEnd();

            GL11.glPopMatrix();
            disableDefaults();
        }
        GL11.glTranslated(getMc().getRenderManager().getRenderPosX(), getMc().getRenderManager().getRenderPosY(), getMc().getRenderManager().getRenderPosZ());
        GL11.glPopMatrix();

    }

    private void enableDefaults() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2929);
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glDepthMask(false);
    }

    private void disableDefaults() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private double getGravity(Item item) {
        return item instanceof ItemBow ? 0.05D : 0.03D;
    }

    private boolean isThrowable(Item item) {
        return item instanceof ItemBow || item instanceof ItemSnowball
                || item instanceof ItemEgg || item instanceof ItemEnderPearl;
    }
}
