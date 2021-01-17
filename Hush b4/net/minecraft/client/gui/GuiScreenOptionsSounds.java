// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiScreenOptionsSounds extends GuiScreen
{
    private final GuiScreen field_146505_f;
    private final GameSettings game_settings_4;
    protected String field_146507_a;
    private String field_146508_h;
    
    public GuiScreenOptionsSounds(final GuiScreen p_i45025_1_, final GameSettings p_i45025_2_) {
        this.field_146507_a = "Options";
        this.field_146505_f = p_i45025_1_;
        this.game_settings_4 = p_i45025_2_;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        this.field_146507_a = I18n.format("options.sounds.title", new Object[0]);
        this.field_146508_h = I18n.format("options.off", new Object[0]);
        this.buttonList.add(new Button(SoundCategory.MASTER.getCategoryId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), SoundCategory.MASTER, true));
        i += 2;
        SoundCategory[] values;
        for (int length = (values = SoundCategory.values()).length, j = 0; j < length; ++j) {
            final SoundCategory soundcategory = values[j];
            if (soundcategory != SoundCategory.MASTER) {
                this.buttonList.add(new Button(soundcategory.getCategoryId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), soundcategory, false));
                ++i;
            }
        }
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled && button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.field_146505_f);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146507_a, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected String getSoundVolume(final SoundCategory p_146504_1_) {
        final float f = this.game_settings_4.getSoundLevel(p_146504_1_);
        return (f == 0.0f) ? this.field_146508_h : (String.valueOf((int)(f * 100.0f)) + "%");
    }
    
    class Button extends GuiButton
    {
        private final SoundCategory field_146153_r;
        private final String field_146152_s;
        public float field_146156_o;
        public boolean field_146155_p;
        
        public Button(final int p_i45024_2_, final int p_i45024_3_, final int p_i45024_4_, final SoundCategory p_i45024_5_, final boolean p_i45024_6_) {
            super(p_i45024_2_, p_i45024_3_, p_i45024_4_, p_i45024_6_ ? 310 : 150, 20, "");
            this.field_146156_o = 1.0f;
            this.field_146153_r = p_i45024_5_;
            this.field_146152_s = I18n.format("soundCategory." + p_i45024_5_.getCategoryName(), new Object[0]);
            this.displayString = String.valueOf(this.field_146152_s) + ": " + GuiScreenOptionsSounds.this.getSoundVolume(p_i45024_5_);
            this.field_146156_o = GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(p_i45024_5_);
        }
        
        @Override
        protected int getHoverState(final boolean mouseOver) {
            return 0;
        }
        
        @Override
        protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
            if (this.visible) {
                if (this.field_146155_p) {
                    this.field_146156_o = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                    this.field_146156_o = MathHelper.clamp_float(this.field_146156_o, 0.0f, 1.0f);
                    mc.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
                    mc.gameSettings.saveOptions();
                    this.displayString = String.valueOf(this.field_146152_s) + ": " + GuiScreenOptionsSounds.this.getSoundVolume(this.field_146153_r);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.drawTexturedModalRect(this.xPosition + (int)(this.field_146156_o * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int)(this.field_146156_o * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            }
        }
        
        @Override
        public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                this.field_146156_o = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.field_146156_o = MathHelper.clamp_float(this.field_146156_o, 0.0f, 1.0f);
                mc.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
                mc.gameSettings.saveOptions();
                this.displayString = String.valueOf(this.field_146152_s) + ": " + GuiScreenOptionsSounds.this.getSoundVolume(this.field_146153_r);
                return this.field_146155_p = true;
            }
            return false;
        }
        
        @Override
        public void playPressSound(final SoundHandler soundHandlerIn) {
        }
        
        @Override
        public void mouseReleased(final int mouseX, final int mouseY) {
            if (this.field_146155_p) {
                if (this.field_146153_r != SoundCategory.MASTER) {
                    GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(this.field_146153_r);
                }
                GuiScreenOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
            }
            this.field_146155_p = false;
        }
    }
}
