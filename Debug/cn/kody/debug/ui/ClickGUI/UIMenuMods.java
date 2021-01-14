package cn.kody.debug.ui.ClickGUI;

import java.util.Iterator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.kody.debug.Client;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.ui.font.UnicodeFontRenderer;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.handler.MouseInputHandler;
import cn.kody.debug.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;

public class UIMenuMods
{
    private ArrayList<Mod> modList;
    private MouseInputHandler handler;
    private MouseInputHandler rightCrink;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    private Category c;
    public double yPos;
    private boolean opened;
    private boolean closed;
    private int valueYAdd;
    private float scrollY;
    private float scrollAmount;
    
    public UIMenuMods(Category c, MouseInputHandler handler) {
        super();
        this.modList = new ArrayList<Mod>();
        this.rightCrink = new MouseInputHandler(1);
        this.valueYAdd = 0;
        this.c = c;
        this.handler = handler;
        this.addMods();
        this.yPos = -(this.y + this.tab_height + this.modList.size() * 20 + 10);
    }
    
    public void draw(int p_draw_1_, int p_draw_2_) {
        this.opened = true;
        int n = 160;
        if (p_draw_2_ > this.y + n) {
            p_draw_2_ = Integer.MAX_VALUE;
        }
        UnicodeFontRenderer tahoma16 = Client.instance.fontMgr.tahoma16;
        new StringBuilder().append(this.c.name().substring(0, 1)).append(this.c.name().toLowerCase().substring(1, this.c.name().length())).toString();
        this.yPos = this.y + this.tab_height - 2;
        int n2 = (int)this.yPos;
        Gui.drawRect((float)this.x, (float)n2, (float)(this.x + this.width), (float)(n2 + n - 23), -263429);
        int n3 = 15;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, scaledResolution.getScaledHeight());
        float n4 = (float)(this.modList.size() * n3 + n2 + this.valueYAdd);
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, Math.min(n - (this.tab_height - 2), this.modList.size() * n3 + this.valueYAdd));
        GL11.glTranslated(0.0, (double)this.scrollY, 0.0);
        p_draw_2_ -= (int)this.scrollY;
        this.valueYAdd = 0;
        for (Mod currentMod : this.modList) {
            if (currentMod.isEnabled()) {
                Gui.drawRect((float)this.x, (float)n2, (float)(this.x + this.width), (float)(n2 + n3), -14704385);
            }
            boolean b;
            if (this.yPos == this.y + this.tab_height - 2 && p_draw_1_ >= this.x && p_draw_1_ <= this.x + this.width - 12 && p_draw_2_ >= n2 && p_draw_2_ < n2 + n3 && p_draw_2_ + this.scrollY >= this.y + this.tab_height) {
                b = true;
            }
            else {
                b = false;
            }
            boolean b2 = b;
            if (!Client.instance.crink.menu.settingMode) {
                Mod mod = currentMod;
                float hoverOpacity;
                if (b2) {
                    hoverOpacity = (float) RenderUtil.getAnimationState(currentMod.hoverOpacity, 0.25f, 1.0f);
                }
                else {
                    hoverOpacity = (float) RenderUtil.getAnimationState(currentMod.hoverOpacity, 0.0f, 1.5f);
                }
                mod.hoverOpacity = hoverOpacity;
            }
            else {
                currentMod.hoverOpacity = 0.0f;
            }
            if (b2 && !Client.instance.crink.menu.settingMode && this.handler.canExcecute()) {
                Mod mod2 = currentMod;
                boolean b3;
                if (!currentMod.isEnabled()) {
                    b3 = true;
                }
                else {
                    b3 = false;
                }
                mod2.set(b3);
            }
            if (b2 && this.rightCrink.canExcecute() && !Client.instance.crink.menu.settingMode && Client.instance.crink.menu.currentMod == null && currentMod.hasValues()) {
                Client.instance.crink.menu.settingMode = true;
                Client.instance.crink.menu.currentMod = currentMod;
            }
            RenderUtil.drawRect((float)this.x, (float)n2, (float)(this.x + this.width), (float)(n2 + n3), Notification.reAlpha(Colors.BLACK.c, currentMod.hoverOpacity));
            if (currentMod.isEnabled()) {
                tahoma16.drawString(currentMod.getRenderName(), this.x + 12.0f, (float)(n2 + (n3 - tahoma16.FONT_HEIGHT) / 2), Colors.WHITE.c);
            }
            else {
                tahoma16.drawString(currentMod.getRenderName(), this.x + 8.0f, (float)(n2 + (n3 - tahoma16.FONT_HEIGHT) / 2), Colors.BLACK.c);
            }
            n2 += n3;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (Client.instance.crink.menu.settingMode && Client.instance.crink.menu.currentMod != null) {
        }
        else {
            if (p_draw_1_ >= this.x && p_draw_1_ <= this.x + this.width && p_draw_2_ + this.scrollY >= this.y && p_draw_2_ + this.scrollY <= n2) {
                this.scrollY += Mouse.getDWheel() / 10.0f;
            }
            if (n2 - n3 - this.tab_height >= n && n2 - this.y + this.scrollY < (double)n) {
                this.scrollY = n - (float)n2 + this.y;
            }
            if (this.scrollY > 0.0f || n2 - n3 - this.tab_height < n) {
                this.scrollY = 0.0f;
            }
        }
    }
    
    public void mouseClick(int p_mouseClick_1_, int p_mouseClick_2_) {
        p_mouseClick_2_ -= (int)this.scrollY;
    }
    
    public void mouseRelease(int p_mouseRelease_1_, int p_mouseRelease_2_) {
    }
    
    private void addMods() {
        ModManager modMgr = Client.instance.modMgr;
        for (Mod mod : ModManager.modList) {
            if (mod.getCategory() != this.c) {
                continue;
            }
            else {
                this.modList.add(mod);
                continue;
            }
        }
    }
}
