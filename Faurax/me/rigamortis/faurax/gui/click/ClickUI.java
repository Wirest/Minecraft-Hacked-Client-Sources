package me.rigamortis.faurax.gui.click;

import net.minecraft.client.gui.*;
import me.rigamortis.faurax.gui.click.components.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class ClickUI extends GuiScreen
{
    public ArrayList<Panel> panels;
    
    public ClickUI() {
        this.panels = new ArrayList<Panel>();
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.mainConstructor(this);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        for (final Panel p : this.panels) {
            p.draw(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Panel p : this.panels) {
            p.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int clickedMouseButton) {
        try {
            for (final Panel p : this.panels) {
                p.mouseClicked(mouseX, mouseY, clickedMouseButton);
            }
            super.mouseClicked(mouseX, mouseY, clickedMouseButton);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onGuiClosed() {
        Client.getConfig().saveConfig();
        Client.getConfig().saveGUI();
        Client.getConfig().saveMods();
        Client.getConfig().saveGUITheme();
    }
    
    public void getTopPanel(final Panel panel) {
        if (this.panels.contains(panel)) {
            this.panels.remove(panel);
        }
        this.panels.add(panel);
    }
}
