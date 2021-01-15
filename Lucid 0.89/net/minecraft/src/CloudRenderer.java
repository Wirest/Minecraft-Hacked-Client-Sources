package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class CloudRenderer
{
    private Minecraft mc;
    private boolean updated = false;
    private boolean renderFancy = false;
    int cloudTickCounter;
    float partialTicks;
    private int glListClouds = -1;
    private int cloudTickCounterUpdate = 0;
    private double cloudPlayerX = 0.0D;
    private double cloudPlayerY = 0.0D;
    private double cloudPlayerZ = 0.0D;

    public CloudRenderer(Minecraft mc)
    {
        this.mc = mc;
        this.glListClouds = GLAllocation.generateDisplayLists(1);
    }

    public void prepareToRender(boolean renderFancy, int cloudTickCounter, float partialTicks)
    {
        if (this.renderFancy != renderFancy)
        {
            this.updated = false;
        }

        this.renderFancy = renderFancy;
        this.cloudTickCounter = cloudTickCounter;
        this.partialTicks = partialTicks;
    }

    public boolean shouldUpdateGlList()
    {
        if (!this.updated)
        {
            return true;
        }
        else if (this.cloudTickCounter >= this.cloudTickCounterUpdate + 20)
        {
            return true;
        }
        else
        {
            Entity rve = this.mc.getRenderViewEntity();
            boolean belowCloudsPrev = this.cloudPlayerY + rve.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F;
            boolean belowClouds = rve.prevPosY + rve.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F;
            return belowClouds != belowCloudsPrev;
        }
    }

    public void startUpdateGlList()
    {
        GL11.glNewList(this.glListClouds, GL11.GL_COMPILE);
    }

    public void endUpdateGlList()
    {
        GL11.glEndList();
        this.cloudTickCounterUpdate = this.cloudTickCounter;
        this.cloudPlayerX = this.mc.getRenderViewEntity().prevPosX;
        this.cloudPlayerY = this.mc.getRenderViewEntity().prevPosY;
        this.cloudPlayerZ = this.mc.getRenderViewEntity().prevPosZ;
        this.updated = true;
        GlStateManager.resetColor();
    }

    public void renderGlList()
    {
        Entity entityliving = this.mc.getRenderViewEntity();
        double exactPlayerX = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * this.partialTicks;
        double exactPlayerY = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * this.partialTicks;
        double exactPlayerZ = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * this.partialTicks;
        double dc = this.cloudTickCounter - this.cloudTickCounterUpdate + this.partialTicks;
        float cdx = (float)(exactPlayerX - this.cloudPlayerX + dc * 0.03D);
        float cdy = (float)(exactPlayerY - this.cloudPlayerY);
        float cdz = (float)(exactPlayerZ - this.cloudPlayerZ);
        GlStateManager.pushMatrix();

        if (this.renderFancy)
        {
            GlStateManager.translate(-cdx / 12.0F, -cdy, -cdz / 12.0F);
        }
        else
        {
            GlStateManager.translate(-cdx, -cdy, -cdz);
        }

        GlStateManager.callList(this.glListClouds);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public void reset()
    {
        this.updated = false;
    }
}
