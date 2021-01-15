package net.minecraft.client.gui;

import net.minecraft.client.gui.*;
import java.util.*;
import me.onlyeli.ice.ui.*;
import me.onlyeli.ice.utils.*;

public final class GuiChangelog extends GuiScreen
{
    private final List<String> log;
    
    public GuiChangelog() {
        this.log = new ArrayList<String>();
    }
    
    @Override
    protected void actionPerformed(final GuiButton p_146284_1_) {
        this.mc.displayGuiScreen(null);
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        this.drawDefaultBackground();
        super.drawScreen(x, y, z);
        Gui.drawCenteredString(Wrapper.mc.fontRendererObj, "Changelog", GuiChangelog.width / 2, 20, -1);
        int logY = 50;
        for (final String text : this.log) {
            this.drawString(Wrapper.mc.fontRendererObj, text, GuiChangelog.width / 6, logY, -1);
            logY += Wrapper.mc.fontRendererObj.FONT_HEIGHT;
        }
        if (this.log.isEmpty()) {
            this.drawString(Wrapper.mc.fontRendererObj, "§eLoading...", GuiChangelog.width / 6, logY, -1);
        }
    }
    
    @Override
    public void initGui() {
    	this.log.add("§b---Updated to build 10---");
        this.log.add("§a+ §bAdded Alt Manager");
        this.log.add("§a+ §bAdded some new modules");
        this.log.add("§c- §bRemoved font");
        this.log.add("§b---Updated to build 9---");
        this.log.add("§a+ §bAdded font");
        this.log.add("§c+ §bSkidded code D:");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiChangelog.width / 2 - 100, this.height - 40, "Back"));
    }
}
