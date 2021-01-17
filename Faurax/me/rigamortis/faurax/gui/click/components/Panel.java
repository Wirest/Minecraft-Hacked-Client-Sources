package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.client.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class Panel implements RenderHelper
{
    public String name;
    public float x;
    public float y;
    private Minecraft mc;
    public float dragX;
    public float dragY;
    public float lastDragX;
    public float lastDragY;
    public boolean dragging;
    public boolean visible;
    public boolean isOpen;
    public boolean isPinned;
    public ArrayList<Button> buttons;
    public ArrayList<Slider> sliders;
    public ArrayList<CheckBox> checkBoxes;
    public ArrayList<DropDown> dropDowns;
    public ArrayList<ScrollBar> scrollBars;
    public ArrayList<Radar> radar;
    
    public Panel(final String name, final float x, final float y) {
        this.mc = Minecraft.getMinecraft();
        this.buttons = new ArrayList<Button>();
        this.sliders = new ArrayList<Slider>();
        this.checkBoxes = new ArrayList<CheckBox>();
        this.dropDowns = new ArrayList<DropDown>();
        this.scrollBars = new ArrayList<ScrollBar>();
        this.radar = new ArrayList<Radar>();
        this.name = name;
        this.x = x;
        this.y = y;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.panelContructor(this, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int state) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.panelMouseClicked(this, x, y, state);
            }
        }
    }
    
    public void mouseMovedOrUp(final int x, final int y, final int state) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.panelMouseMovedOrUp(this, x, y, state);
            }
        }
    }
    
    public void draw(final int x, final int y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.panelDraw(this, x, y);
            }
        }
    }
    
    public boolean isOpen() {
        return this.isOpen;
    }
    
    public void setOpen(final boolean isOpen) {
        this.isOpen = isOpen;
    }
    
    public boolean isPinned() {
        return this.isPinned;
    }
    
    public void setPinned(final boolean isPinned) {
        this.isPinned = isPinned;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
