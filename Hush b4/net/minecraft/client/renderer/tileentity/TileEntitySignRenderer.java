// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.block.Block;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import optifine.CustomColors;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.model.ModelSign;
import net.minecraft.util.ResourceLocation;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation SIGN_TEXTURE;
    private final ModelSign model;
    private static final String __OBFID = "CL_00000970";
    
    static {
        SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    }
    
    public TileEntitySignRenderer() {
        this.model = new ModelSign();
    }
    
    public void renderTileEntityAt(final TileEntitySign te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final Block block = te.getBlockType();
        GlStateManager.pushMatrix();
        final float f = 0.6666667f;
        if (block == Blocks.standing_sign) {
            GlStateManager.translate((float)x + 0.5f, (float)y + 0.75f * f, (float)z + 0.5f);
            final float f2 = te.getBlockMetadata() * 360 / 16.0f;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            this.model.signStick.showModel = true;
        }
        else {
            final int k = te.getBlockMetadata();
            float f3 = 0.0f;
            if (k == 2) {
                f3 = 180.0f;
            }
            if (k == 4) {
                f3 = 90.0f;
            }
            if (k == 5) {
                f3 = -90.0f;
            }
            GlStateManager.translate((float)x + 0.5f, (float)y + 0.75f * f, (float)z + 0.5f);
            GlStateManager.rotate(-f3, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.model.signStick.showModel = false;
        }
        if (destroyStage >= 0) {
            this.bindTexture(TileEntitySignRenderer.DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            this.bindTexture(TileEntitySignRenderer.SIGN_TEXTURE);
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(f, -f, -f);
        this.model.renderSign();
        GlStateManager.popMatrix();
        final FontRenderer fontrenderer = this.getFontRenderer();
        final float f4 = 0.015625f * f;
        GlStateManager.translate(0.0f, 0.5f * f, 0.07f * f);
        GlStateManager.scale(f4, -f4, f4);
        GL11.glNormal3f(0.0f, 0.0f, -1.0f * f4);
        GlStateManager.depthMask(false);
        int i = 0;
        if (Config.isCustomColors()) {
            i = CustomColors.getSignTextColor(i);
        }
        if (destroyStage < 0) {
            for (int j = 0; j < te.signText.length; ++j) {
                if (te.signText[j] != null) {
                    final IChatComponent ichatcomponent = te.signText[j];
                    final List list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false, true);
                    String s = (list != null && list.size() > 0) ? list.get(0).getFormattedText() : "";
                    if (j == te.lineBeingEdited) {
                        s = "> " + s + " <";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                    }
                    else {
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                    }
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        this.renderTileEntityAt((TileEntitySign)te, x, y, z, partialTicks, destroyStage);
    }
}
