package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class DropDown implements RenderHelper
{
    public float x;
    public float y;
    public String name;
    public Panel panel;
    public boolean open;
    public ArrayList<DropDownButton> buttons;
    public String[] items;
    public Value value;
    
    public DropDown(final Panel panel, final String name, final float x, final float y, final String[] items, final Value value) {
        this.buttons = new ArrayList<DropDownButton>();
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.items = items;
        this.value = value;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.dropDownContructor(this, this.panel, x, y);
            }
        }
    }
    
    public void draw(final float x, final float y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.dropDownDraw(this, this.panel, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.dropDownMouseClicked(this, this.panel, x, y, button);
            }
        }
    }
}
