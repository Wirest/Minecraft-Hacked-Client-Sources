// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiLockIconButton extends GuiButton
{
    private boolean field_175231_o;
    
    public GuiLockIconButton(final int p_i45538_1_, final int p_i45538_2_, final int p_i45538_3_) {
        super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
        this.field_175231_o = false;
    }
    
    public boolean func_175230_c() {
        return this.field_175231_o;
    }
    
    public void func_175229_b(final boolean p_175229_1_) {
        this.field_175231_o = p_175229_1_;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            Icon guilockiconbutton$icon;
            if (this.field_175231_o) {
                if (!this.enabled) {
                    guilockiconbutton$icon = Icon.LOCKED_DISABLED;
                }
                else if (flag) {
                    guilockiconbutton$icon = Icon.LOCKED_HOVER;
                }
                else {
                    guilockiconbutton$icon = Icon.LOCKED;
                }
            }
            else if (!this.enabled) {
                guilockiconbutton$icon = Icon.UNLOCKED_DISABLED;
            }
            else if (flag) {
                guilockiconbutton$icon = Icon.UNLOCKED_HOVER;
            }
            else {
                guilockiconbutton$icon = Icon.UNLOCKED;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, guilockiconbutton$icon.func_178910_a(), guilockiconbutton$icon.func_178912_b(), this.width, this.height);
        }
    }
    
    enum Icon
    {
        LOCKED("LOCKED", 0, 0, 146), 
        LOCKED_HOVER("LOCKED_HOVER", 1, 0, 166), 
        LOCKED_DISABLED("LOCKED_DISABLED", 2, 0, 186), 
        UNLOCKED("UNLOCKED", 3, 20, 146), 
        UNLOCKED_HOVER("UNLOCKED_HOVER", 4, 20, 166), 
        UNLOCKED_DISABLED("UNLOCKED_DISABLED", 5, 20, 186);
        
        private final int field_178914_g;
        private final int field_178920_h;
        
        private Icon(final String name, final int ordinal, final int p_i45537_3_, final int p_i45537_4_) {
            this.field_178914_g = p_i45537_3_;
            this.field_178920_h = p_i45537_4_;
        }
        
        public int func_178910_a() {
            return this.field_178914_g;
        }
        
        public int func_178912_b() {
            return this.field_178920_h;
        }
    }
}
