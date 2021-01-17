// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.Minecraft;

public class CloudRenderer
{
    private Minecraft mc;
    private boolean updated;
    private boolean renderFancy;
    int cloudTickCounter;
    float partialTicks;
    private int glListClouds;
    private int cloudTickCounterUpdate;
    private double cloudPlayerX;
    private double cloudPlayerY;
    private double cloudPlayerZ;
    
    public CloudRenderer(final Minecraft p_i28_1_) {
        this.updated = false;
        this.renderFancy = false;
        this.glListClouds = -1;
        this.cloudTickCounterUpdate = 0;
        this.cloudPlayerX = 0.0;
        this.cloudPlayerY = 0.0;
        this.cloudPlayerZ = 0.0;
        this.mc = p_i28_1_;
        this.glListClouds = GLAllocation.generateDisplayLists(1);
    }
    
    public void prepareToRender(final boolean p_prepareToRender_1_, final int p_prepareToRender_2_, final float p_prepareToRender_3_) {
        if (this.renderFancy != p_prepareToRender_1_) {
            this.updated = false;
        }
        this.renderFancy = p_prepareToRender_1_;
        this.cloudTickCounter = p_prepareToRender_2_;
        this.partialTicks = p_prepareToRender_3_;
    }
    
    public boolean shouldUpdateGlList() {
        if (!this.updated) {
            return true;
        }
        if (this.cloudTickCounter >= this.cloudTickCounterUpdate + 20) {
            return true;
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final boolean flag = this.cloudPlayerY + entity.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f;
        final boolean flag2 = entity.prevPosY + entity.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f;
        return flag2 ^ flag;
    }
    
    public void startUpdateGlList() {
        GL11.glNewList(this.glListClouds, 4864);
    }
    
    public void endUpdateGlList() {
        GL11.glEndList();
        this.cloudTickCounterUpdate = this.cloudTickCounter;
        this.cloudPlayerX = this.mc.getRenderViewEntity().prevPosX;
        this.cloudPlayerY = this.mc.getRenderViewEntity().prevPosY;
        this.cloudPlayerZ = this.mc.getRenderViewEntity().prevPosZ;
        this.updated = true;
        GlStateManager.resetColor();
    }
    
    public void renderGlList() {
        final Entity entity = this.mc.getRenderViewEntity();
        final double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * this.partialTicks;
        final double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * this.partialTicks;
        final double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * this.partialTicks;
        final double d4 = this.cloudTickCounter - this.cloudTickCounterUpdate + this.partialTicks;
        final float f = (float)(d0 - this.cloudPlayerX + d4 * 0.03);
        final float f2 = (float)(d2 - this.cloudPlayerY);
        final float f3 = (float)(d3 - this.cloudPlayerZ);
        GlStateManager.pushMatrix();
        if (this.renderFancy) {
            GlStateManager.translate(-f / 12.0f, -f2, -f3 / 12.0f);
        }
        else {
            GlStateManager.translate(-f, -f2, -f3);
        }
        GlStateManager.callList(this.glListClouds);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }
    
    public void reset() {
        this.updated = false;
    }
}
