/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventRender;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class GetDown
extends Mod {
    private double posX;
    private double posY;
    private double posZ;
    private boolean landed;

    public GetDown() {
        super("GetDown", Mod.Category.RENDER, Colors.RED.c);
    }

    @EventTarget
    public void onRender(EventRender event) {
        double[] dist = new double[]{3.609, 4.177, 4.461, 5.028, 5.323, 5.595, 5.879, 6.174, 6.729, 7.013};
        double[] damage = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.5, 2.0, 2.5, 3.5, 4.0};
        int max = 10;
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable((int)3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0f, 0.0f);
        int y = 0;
        while (y <= max) {
            double x = - dist[y];
            while (x <= dist[y]) {
                double z = - dist[y];
                while (z <= dist[y]) {
                    BlockPos pos = new BlockPos(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY - (double)y, this.mc.thePlayer.posZ + z);
                    Block block = this.mc.theWorld.getBlockState(pos).getBlock();
                    if (block.isFullBlock()) {
                        this.mc.getRenderManager();
                        double xRender = (double)pos.getX() - RenderManager.renderPosX;
                        this.mc.getRenderManager();
                        double yRender = (double)pos.getY() - RenderManager.renderPosY;
                        this.mc.getRenderManager();
                        double zRender = (double)pos.getZ() - RenderManager.renderPosZ;
                        double distBlock = Math.sqrt(x * x + z * z);
                        float red = 0.0f;
                        float green = 1.0f;
                        float blue = 0.0f;
                        GL11.glPushMatrix();
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        GL11.glDisable((int)3553);
                        GL11.glEnable((int)2848);
                        GL11.glDepthMask((boolean)false);
                        GL11.glColor4f((float)(red += (float)y / 10.0f), (float)(green -= (float)y / 10.0f), (float)blue, (float)0.25f);
                        RenderUtil.drawBoundingBox(new AxisAlignedBB(xRender - 0.05, yRender - 0.05, zRender - 0.05, xRender + 1.0 + 0.05, yRender + 1.0 + 0.05, zRender + 1.0 + 0.05));
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glDisable((int)2848);
                        GL11.glEnable((int)3553);
                        GL11.glDepthMask((boolean)true);
                        GL11.glDisable((int)3042);
                        GL11.glPopMatrix();
                        GL11.glPushMatrix();
                        GL11.glEnable((int)3042);
                        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.disableLighting();
                        GlStateManager.enableBlend();
                        GL11.glBlendFunc((int)770, (int)771);
                        GL11.glDisable((int)3553);
                        float SCALE = (float)(Math.min(Math.max(1.2000000476837158 * (distBlock * 0.15000000596046448), 1.25), 6.0) * 0.019999999552965164);
                        GlStateManager.translate((double)((float)xRender) + 0.5, (double)((float)yRender) + 1.5, (double)((float)zRender) + 0.5);
                        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.rotate(- this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.rotate(this.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
                        GL11.glScalef((float)(- SCALE), (float)(- SCALE), (float)SCALE);
                        GL11.glEnable((int)3553);
                        String s = String.valueOf(String.valueOf(y - 1)) + "m";
                        this.mc.fontRendererObj.drawStringWithShadow(s, (- this.mc.fontRendererObj.getStringWidth(s)) / 2, 0.0f, -1);
                        GlStateManager.translate(0.0f, 10.0f, 0.0f);
                        int color = Colors.GREEN.c;
                        if (y >= 3) {
                            color = Colors.ORANGE.c;
                        }
                        if (y >= 6) {
                            color = Colors.RED.c;
                        }
                        s = "Dmg : " + damage[y];
                        this.mc.fontRendererObj.drawStringWithShadow(s, (- this.mc.fontRendererObj.getStringWidth(s)) / 2, 0.0f, new Color(red, green, blue).getRGB());
                        GlStateManager.disableBlend();
                        GL11.glDisable((int)3042);
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glPopMatrix();
                    }
                    z += 1.0;
                }
                x += 1.0;
            }
            ++y;
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("GetDown Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("GetDown Enable", ClientNotification.Type.SUCCESS);
    }
}

