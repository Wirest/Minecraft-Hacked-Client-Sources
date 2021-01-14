package cn.kody.debug.ui.ClickGUI;

import org.lwjgl.input.Mouse;

import cn.kody.debug.Client;
import cn.kody.debug.mod.Category;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.ui.font.UnicodeFontRenderer;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.handler.MouseInputHandler;
import cn.kody.debug.utils.render.RenderUtil;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class ClickMenuCategory
{
    public Category c;
    UIMenuMods uiMenuMods;
    private MouseInputHandler handler;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    public int x2;
    public int y2;
    public boolean drag;
    private double arrowAngle;
    
    public ClickMenuCategory(Category c, int x, int y, int width, int tab_height, MouseInputHandler handler) {
        super();
        this.drag = true;
        this.arrowAngle = 0.0;
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.tab_height = tab_height;
        this.uiMenuMods = new UIMenuMods(c, handler);
        this.handler = handler;
    }
    
    public void draw(int p_draw_1_, int p_draw_2_) {
        this.open = true;
        this.uiMenuMods.open = true;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        UnicodeFontRenderer tahoma18 = Client.instance.fontMgr.tahoma18;
        String string = this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length());
        RenderUtil.drawRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.tab_height), Notification.reAlpha(-658186, 0.95f));
        tahoma18.drawString(string, this.x + 8.0f, (float)(this.y + (this.tab_height - tahoma18.FONT_HEIGHT) / 2), new Color(99, 99, 99).getRGB());
        double n = this.x + this.width - 6;
        double n2 = this.y + 9;
        this.upateUIMenuMods();
        this.uiMenuMods.draw(p_draw_1_, p_draw_2_);
        if (this.uiMenuMods.open) {
            Minecraft.getMinecraft().ingameGUI.drawGradientRect(this.x, this.y + this.tab_height - 2, this.x + this.width, this.y + this.tab_height + 3, Notification.reAlpha(Colors.BLACK.c, 0.3f), 0);
        }
        if (Client.instance.crink.menu.settingMode && Client.instance.crink.menu.currentMod != null) {
        }
        else {
            this.move(p_draw_1_, p_draw_2_);
        }
    }
    
    private void move(int n, int n2) {
        if (this.isHovering(n, n2) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = n - this.x;
            this.y2 = n2 - this.y;
        }
        if (!Mouse.isButtonDown(0)) {
            this.drag = false;
        }
        if (this.drag) {
            this.x = n - this.x2;
            this.y = n2 - this.y2;
        }
    }
    
    private boolean isHovering(int n, int n2) {
        boolean b;
        if (n >= this.x && n <= this.x + this.width && n2 >= this.y && n2 <= this.y + this.tab_height) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private void upateUIMenuMods() {
        this.uiMenuMods.x = this.x;
        this.uiMenuMods.y = this.y;
        this.uiMenuMods.tab_height = this.tab_height;
        this.uiMenuMods.width = this.width;
    }
    
    public void mouseClick(int p_mouseClick_1_, int p_mouseClick_2_) {
        this.uiMenuMods.mouseClick(p_mouseClick_1_, p_mouseClick_2_);
    }
    
    public void mouseRelease(int p_mouseRelease_1_, int p_mouseRelease_2_) {
        this.uiMenuMods.mouseRelease(p_mouseRelease_1_, p_mouseRelease_2_);
    }
}
