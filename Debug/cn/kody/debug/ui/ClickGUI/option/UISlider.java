package cn.kody.debug.ui.ClickGUI.option;

import org.lwjgl.input.Mouse;

import cn.kody.debug.Client;
import cn.kody.debug.ui.font.UnicodeFontRenderer;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.render.RenderUtil;
import cn.kody.debug.value.Value;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.awt.Color;

public class UISlider
{
    Value<Double> value;
    public float x;
    public float y;
    private boolean isDraging;
    private boolean clickNotDraging;
    public boolean showValue;
    public int tX;
    public int tY;
    public int dragX;
    public int dragY;
    float ani;
    
    public UISlider(final Value value) {
        super();
        this.value = (Value<Double>)value;
    }
    
    public void draw(final float x, final float y) {
        this.x = x;
        this.y = y;
        final UnicodeFontRenderer tahoma20 = Client.instance.fontMgr.tahoma20;
        final double n = (this.value.getValueState() - this.value.getValueMin()) / (this.value.getValueMax() - this.value.getValueMin());
        tahoma20.drawString(this.value.getValueName().split("_")[1], x - 198.0f, y - 2.0f, Colors.BLACK.c);
        if (this.showValue) {
            Client.instance.fontMgr.tahoma15.drawString(this.value.getValueState() + "", x - 78.0f - tahoma20.getStringWidth(this.value.getValueState() + ""), y, Colors.GREY.c);
        }
        if (this.ani == 0.0f) {
            this.ani = (float)(x - 15.0f - (60.0 - 60.0 * n));
        }
        this.ani = (float) RenderUtil.getAnimationState(this.ani, (float)(x - 15.0f - (60.0 - 60.0 * n)), (float)Math.max(10.0, Math.abs(this.ani - (x - 15.0f - (60.0 - 60.0 * n))) * 30.0 * 0.3));
        RenderUtil.drawRoundedRect(x - 75.0f, y + 3.0f, x - 15.0f, y + 6.0f, (int) 1.0f, new Color(Colors.GREY.c).brighter().brighter().getRGB());
        RenderUtil.drawRoundedRect(x - 75.0f, y + 3.0f, this.ani, y + 6.0f, (int) 1.0f, new Color(-14848033).brighter().getRGB());
        RenderUtil.circle(this.ani, y + 4.5f, 4.0f, new Color(-14848033).brighter().getRGB());
    }
    
    public void onPress(final int tx, final int ty) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (this.isHovering(tx, ty, this.x - 100.0f, this.y - 3.0f, this.x - 10.0f, this.y + 10.0f)) {
            this.showValue = true;
        }
        else {
            this.showValue = false;
        }
        if (Mouse.isButtonDown(0)) {
            if (this.isHovering(tx, ty, this.x - 75.0f, this.y - 3.0f, this.x - 15.0f, this.y + 10.0f) || this.isDraging) {
                this.isDraging = true;
            }
            else {
                this.clickNotDraging = true;
            }
            if (this.isDraging && !this.clickNotDraging) {
                double n = (tx - this.x + 75.0f) / 60.0f;
                if (n < 0.0) {
                    n = 0.0;
                }
                if (n > 1.0) {
                    n = 1.0;
                }
                final double n2 = Math.round(((this.value.getValueMax() - this.value.getValueMin()) * n + this.value.getValueMin()) * (1.0 / this.value.getSteps())) / (1.0 / this.value.getSteps());
                Client.instance.fileMgr.saveValues();
                this.value.setValueState(n2);
            }
        }
        else {
            this.isDraging = false;
            this.clickNotDraging = false;
        }
        if (this.isDraging && !Client.instance.crink.menu.isDraggingSlider) {
            Client.instance.crink.menu.isDraggingSlider = true;
        }
        else {
            Client.instance.crink.menu.isDraggingSlider = false;
        }
        this.tX = tx;
        this.tY = ty;
    }
    
    private boolean isHovering(final int n, final int n2, final double n3, final double n4, final double n5, final double n6) {
        boolean b;
        if (n > n3 && n < n5 && n2 > n4 && n2 < n6) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
}
