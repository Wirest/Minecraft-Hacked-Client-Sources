package net.minecraft.realms;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.IChatComponent;

public class DisconnectedRealmsScreen extends RealmsScreen {
    private String title;
    private IChatComponent reason;
    private List lines;
    private final RealmsScreen parent;
    private static final String __OBFID = "CL_00002145";

    public DisconnectedRealmsScreen(RealmsScreen p_i45742_1_, String p_i45742_2_, IChatComponent p_i45742_3_) {
        this.parent = p_i45742_1_;
        this.title = getLocalizedString(p_i45742_2_);
        this.reason = p_i45742_3_;
    }

    public void init() {
        this.buttonsClear();
        this.buttonsAdd(newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 12, getLocalizedString("gui.back")));
        this.lines = this.fontSplit(this.reason.getFormattedText(), this.width() - 50);
    }

    public void keyPressed(char p_keyPressed_1_, int p_keyPressed_2_) {
        if (p_keyPressed_2_ == 1) {
            Realms.setScreen(this.parent);
        }
    }

    public void buttonClicked(RealmsButton p_buttonClicked_1_) {
        if (p_buttonClicked_1_.id() == 0) {
            Realms.setScreen(this.parent);
        }
    }

    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - 50, 11184810);
        int var4 = this.height() / 2 - 30;

        if (this.lines != null) {
            for (Iterator var5 = this.lines.iterator(); var5.hasNext(); var4 += this.fontLineHeight()) {
                String var6 = (String) var5.next();
                this.drawCenteredString(var6, this.width() / 2, var4, 16777215);
            }
        }

        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}
