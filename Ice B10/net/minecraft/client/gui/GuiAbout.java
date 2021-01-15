package net.minecraft.client.gui;

import net.minecraft.client.gui.*;
import java.util.*;
import me.onlyeli.ice.ui.*;
import me.onlyeli.ice.utils.*;

public final class GuiAbout extends GuiScreen
{
    private final List<String> log;
    
    public GuiAbout() {
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
        Gui.drawCenteredString(Wrapper.mc.fontRendererObj, "§6About", GuiAbout.width / 2, 20, -1);
        int logY = 50;
        for (final String text : this.log) {
            this.drawString(Wrapper.mc.fontRendererObj, text, GuiAbout.width / 6, logY, -1);
            logY += Wrapper.mc.fontRendererObj.FONT_HEIGHT;
        }
        if (this.log.isEmpty()) {
            this.drawString(Wrapper.mc.fontRendererObj, "§eLoading...", GuiAbout.width / 6, logY, -1);
        }
    }
    
    @Override
    public void initGui() {
        this.log.add("§bI am a new dev named §aOnlyEli_§b (or Eli for short)");
        this.log.add("§bI made the base of this client using §4BiLLYB0B1060's§b tutorial");
        this.log.add("§bThe rest is basicly §cskidded§b from 2 clients");
        this.log.add("§bI also felt like giving §dAlerithe§d credit cuz shes awesome :D");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiAbout.width / 2 - 100, this.height - 40, "Back"));
    }
}
