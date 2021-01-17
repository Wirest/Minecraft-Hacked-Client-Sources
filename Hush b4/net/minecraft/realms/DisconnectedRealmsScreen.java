// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import java.util.Iterator;
import java.util.List;
import net.minecraft.util.IChatComponent;

public class DisconnectedRealmsScreen extends RealmsScreen
{
    private String title;
    private IChatComponent reason;
    private List<String> lines;
    private final RealmsScreen parent;
    private int textHeight;
    
    public DisconnectedRealmsScreen(final RealmsScreen p_i45742_1_, final String p_i45742_2_, final IChatComponent p_i45742_3_) {
        this.parent = p_i45742_1_;
        this.title = RealmsScreen.getLocalizedString(p_i45742_2_);
        this.reason = p_i45742_3_;
    }
    
    @Override
    public void init() {
        Realms.setConnectedToRealms(false);
        this.buttonsClear();
        this.lines = this.fontSplit(this.reason.getFormattedText(), this.width() - 50);
        this.textHeight = this.lines.size() * this.fontLineHeight();
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 100, this.height() / 2 + this.textHeight / 2 + this.fontLineHeight(), RealmsScreen.getLocalizedString("gui.back")));
    }
    
    @Override
    public void keyPressed(final char p_keyPressed_1_, final int p_keyPressed_2_) {
        if (p_keyPressed_2_ == 1) {
            Realms.setScreen(this.parent);
        }
    }
    
    @Override
    public void buttonClicked(final RealmsButton p_buttonClicked_1_) {
        if (p_buttonClicked_1_.id() == 0) {
            Realms.setScreen(this.parent);
        }
    }
    
    @Override
    public void render(final int p_render_1_, final int p_render_2_, final float p_render_3_) {
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - this.textHeight / 2 - this.fontLineHeight() * 2, 11184810);
        int i = this.height() / 2 - this.textHeight / 2;
        if (this.lines != null) {
            for (final String s : this.lines) {
                this.drawCenteredString(s, this.width() / 2, i, 16777215);
                i += this.fontLineHeight();
            }
        }
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}
