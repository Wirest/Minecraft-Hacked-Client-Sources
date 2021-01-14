package cn.kody.debug.ui.ClickGUI.button;

import java.awt.Color;

import cn.kody.debug.Client;
import cn.kody.debug.ui.font.UnicodeFontRenderer;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.render.RenderUtil;

public class Button
{
    public String parent;
    public float x;
    public float y;
    public boolean state;
    
    public Button(String parent, boolean state) {
        this.parent = parent;
        this.state = state;
    }
    
    public void draw(float p_draw_1_, float p_draw_2_) {
        this.x = p_draw_1_;
        this.y = p_draw_2_;
        float n = p_draw_1_ - 4.0f;
        float n2 = 5.0f;
        int n3;
        if (this.state) {
            n3 = new Color(-14848033).brighter().getRGB();
        }
        else {
            n3 = new Color(Colors.WHITE.c).darker().getRGB();
        }
        RenderUtil.circle(n, p_draw_2_, n2, n3);
        if (this.state) {
            UnicodeFontRenderer icon30 = Client.instance.fontMgr.icon30;
            String s = "f";
            float n4 = p_draw_1_ - 11.5f;
            float n5 = p_draw_2_ - 7.5f;
            int n6;
            if (this.state) {
                n6 = Colors.WHITE.c;
            }
            else {
                n6 = Colors.BLACK.c;
            }
            icon30.drawString(s, n4, n5, n6);
        }
    }
    
    public void toggle() {
        boolean state;
        if (!this.state) {
            state = true;
        }
        else {
            state = false;
        }
        this.state = state;
    }
    
    public void isPressed(int p_isPressed_1_, int p_isPressed_2_) {
        if (this.isHovering(p_isPressed_1_, p_isPressed_2_, this.x - 12.0f, this.y - 5.0f, this.x + 12.0f, this.y + 5.0f)) {
            this.onPress();
        }
    }
    
    private boolean isHovering(int p_isHovering_1_, int p_isHovering_2_, double p_isHovering_3_, double p_isHovering_5_, double p_isHovering_7_, double p_isHovering_9_) {
        boolean b;
        if (p_isHovering_1_ > p_isHovering_3_ && p_isHovering_1_ < p_isHovering_7_ && p_isHovering_2_ > p_isHovering_5_ && p_isHovering_2_ < p_isHovering_9_) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public void onPress() {
        this.toggle();
        Client.instance.fileMgr.saveValues();
    }
}
