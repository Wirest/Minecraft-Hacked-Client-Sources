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
        parent = p_i45742_1_;
        title = RealmsScreen.getLocalizedString(p_i45742_2_);
        reason = p_i45742_3_;
    }

    @Override
    public void init() {
        buttonsClear();
        buttonsAdd(RealmsScreen.newButton(0, width() / 2 - 100, height() / 4 + 120 + 12, RealmsScreen.getLocalizedString("gui.back")));
        lines = fontSplit(reason.getFormattedText(), width() - 50);
    }

    @Override
    public void keyPressed(char p_keyPressed_1_, int p_keyPressed_2_) {
        if (p_keyPressed_2_ == 1) {
            Realms.setScreen(parent);
        }
    }

    @Override
    public void buttonClicked(RealmsButton p_buttonClicked_1_) {
        if (p_buttonClicked_1_.id() == 0) {
            Realms.setScreen(parent);
        }
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        drawCenteredString(title, width() / 2, height() / 2 - 50, 11184810);
        int var4 = height() / 2 - 30;

        if (lines != null) {
            for (Iterator var5 = lines.iterator(); var5.hasNext(); var4 += fontLineHeight()) {
                String var6 = (String) var5.next();
                drawCenteredString(var6, width() / 2, var4, 16777215);
            }
        }

        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}
