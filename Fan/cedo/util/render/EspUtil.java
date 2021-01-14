package cedo.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

public class EspUtil {
    static Minecraft mc = Minecraft.getMinecraft();


    public static void chestESPBox(TileEntity entity, int mode, Color color) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);


        double x = entity.getPos().getX();
        double y = entity.getPos().getY();
        double z = entity.getPos().getZ();
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_LINE_SMOOTH);


        GlStateManager.color(Objects.requireNonNull(color).getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        RenderGlobal.func_181561_a(
                new AxisAlignedBB(
                        x - 1 / 2 + 0.05 - x + (x - mc.getRenderManager().renderPosX),
                        y - y + (y - mc.getRenderManager().renderPosY),
                        z - 1 / 2 + 0.05 - z + (z - mc.getRenderManager().renderPosZ),
                        x + 1 - 0.05 - x + (x - mc.getRenderManager().renderPosX),
                        y + 1 - 0.05 - y + (y - mc.getRenderManager().renderPosY),
                        z + 1 - 0.05 - z + (z - mc.getRenderManager().renderPosZ)
                ));
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }


    public static void entityESPBox(Entity entity, int mode, Color color) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);


        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.timer.renderPartialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.timer.renderPartialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.timer.renderPartialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.timer.renderPartialTicks;
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.rotate(-yaw, 0, 1, 0);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_LINE_SMOOTH);


        GlStateManager.color(Objects.requireNonNull(color).getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        RenderGlobal.func_181561_a(
                new AxisAlignedBB(
                        x - entity.width / 2 - 0.05 - x + (x - mc.getRenderManager().renderPosX),
                        y - y + (y - mc.getRenderManager().renderPosY),
                        z - entity.width / 2 - 0.05 - z + (z - mc.getRenderManager().renderPosZ),
                        x + entity.width / 2 + 0.05 - x + (x - mc.getRenderManager().renderPosX),
                        y + entity.height + 0.1 - y + (y - mc.getRenderManager().renderPosY),
                        z + entity.width / 2 + 0.05 - z + (z - mc.getRenderManager().renderPosZ)
                ));
        GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
        GlStateManager.rotate(yaw, 0, 1, 0);
        GlStateManager.translate(-(x - mc.getRenderManager().renderPosX), -(y - mc.getRenderManager().renderPosY), -(z - mc.getRenderManager().renderPosZ));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
