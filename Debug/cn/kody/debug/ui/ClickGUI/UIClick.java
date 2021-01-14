package cn.kody.debug.ui.ClickGUI;

import net.minecraft.client.gui.*;
import java.io.*;

import cn.kody.debug.Client;

public class UIClick extends GuiScreen
{
    public ClickMenu menu;
    private ScaledResolution res;
    public boolean initialized;
    
    public void initGui() {
        this.res = new ScaledResolution(this.mc);
        if (!this.initialized) {
            this.menu = new ClickMenu();
            this.initialized = true;
        }
    }
    
    public void load() {
        if (!this.initialized) {
            new Thread((Runnable)new UIThread(this)).start();
        }
    }
    
    public void drawScreen(final int n, final int n2, final float n3) {
        if (!this.mc.gameSettings.ofFastRender) {
//            Class310.blurAll(5.0f);
        }
        this.menu.draw(n, n2);
    }
    
    protected void mouseClicked(final int n, final int n2, final int n3) throws IOException {
        this.menu.mouseClick(n, n2);
    }
    
    protected void mouseReleased(final int n, final int n2, final int n3) {
        this.menu.mouseRelease(n, n2);
    }
    
    private boolean isHovering(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        boolean b;
        if (n >= n3 && n <= n5 && n2 >= n4 && n2 <= n6) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    protected void actionPerformed(final GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
    }
    
    public void onGuiClosed() {
        this.save();
        super.onGuiClosed();
    }
    
    public void save() {
        Client.instance.fileMgr.saveKeys();
        Client.instance.fileMgr.saveMods();
        Client.instance.fileMgr.saveValues();
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
}
