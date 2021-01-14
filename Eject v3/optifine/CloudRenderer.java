package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class CloudRenderer {
    int cloudTickCounter;
    float partialTicks;
    private Minecraft mc;
    private boolean updated = false;
    private boolean renderFancy = false;
    private int glListClouds = -1;
    private int cloudTickCounterUpdate = 0;
    private double cloudPlayerX = 0.0D;
    private double cloudPlayerY = 0.0D;
    private double cloudPlayerZ = 0.0D;

    public CloudRenderer(Minecraft paramMinecraft) {
        this.mc = paramMinecraft;
        this.glListClouds = GLAllocation.generateDisplayLists(1);
    }

    public void prepareToRender(boolean paramBoolean, int paramInt, float paramFloat) {
        if (this.renderFancy != paramBoolean) {
            this.updated = false;
        }
        this.renderFancy = paramBoolean;
        this.cloudTickCounter = paramInt;
        this.partialTicks = paramFloat;
    }

    public boolean shouldUpdateGlList() {
        if (!this.updated) {
            return true;
        }
        if (this.cloudTickCounter >= (this.cloudTickCounterUpdate | 0x14)) {
            return true;
        }
        Entity localEntity = this.mc.getRenderViewEntity();
        int i = this.cloudPlayerY + localEntity.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F ? 1 : 0;
        int j = localEntity.prevPosY + localEntity.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F ? 1 : 0;
        return j != i;
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
        Entity localEntity = this.mc.getRenderViewEntity();
        double d1 = localEntity.prevPosX + (localEntity.posX - localEntity.prevPosX) * this.partialTicks;
        double d2 = localEntity.prevPosY + (localEntity.posY - localEntity.prevPosY) * this.partialTicks;
        double d3 = localEntity.prevPosZ + (localEntity.posZ - localEntity.prevPosZ) * this.partialTicks;
        double d4 = this.cloudTickCounter - this.cloudTickCounterUpdate + this.partialTicks;
        float f1 = (float) (d1 - this.cloudPlayerX + d4 * 0.03D);
        float f2 = (float) (d2 - this.cloudPlayerY);
        float f3 = (float) (d3 - this.cloudPlayerZ);
        GlStateManager.pushMatrix();
        if (this.renderFancy) {
            GlStateManager.translate(-f1 / 12.0F, -f2, -f3 / 12.0F);
        } else {
            GlStateManager.translate(-f1, -f2, -f3);
        }
        GlStateManager.callList(this.glListClouds);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public void reset() {
        this.updated = false;
    }
}




