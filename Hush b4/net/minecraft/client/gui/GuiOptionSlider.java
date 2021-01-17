// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionSlider extends GuiButton
{
    private float sliderValue;
    public boolean dragging;
    private GameSettings.Options options;
    private final float field_146132_r;
    private final float field_146131_s;
    
    public GuiOptionSlider(final int p_i45016_1_, final int p_i45016_2_, final int p_i45016_3_, final GameSettings.Options p_i45016_4_) {
        this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0f, 1.0f);
    }
    
    public GuiOptionSlider(final int p_i45017_1_, final int p_i45017_2_, final int p_i45017_3_, final GameSettings.Options p_i45017_4_, final float p_i45017_5_, final float p_i45017_6_) {
        super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 150, 20, "");
        this.sliderValue = 1.0f;
        this.options = p_i45017_4_;
        this.field_146132_r = p_i45017_5_;
        this.field_146131_s = p_i45017_6_;
        final Minecraft minecraft = Minecraft.getMinecraft();
        this.sliderValue = p_i45017_4_.normalizeValue(minecraft.gameSettings.getOptionFloatValue(p_i45017_4_));
        this.displayString = minecraft.gameSettings.getKeyBinding(p_i45017_4_);
    }
    
    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
                final float f = this.options.denormalizeValue(this.sliderValue);
                mc.gameSettings.setOptionFloatValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f);
                this.displayString = mc.gameSettings.getKeyBinding(this.options);
            }
            mc.getTextureManager().bindTexture(GuiOptionSlider.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
            mc.gameSettings.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = mc.gameSettings.getKeyBinding(this.options);
            return this.dragging = true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }
}
