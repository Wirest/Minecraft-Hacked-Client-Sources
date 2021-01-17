package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class DropDownButton implements RenderHelper
{
    public float x;
    public float y;
    public String name;
    public Panel panel;
    public DropDown dropDown;
    public boolean enabled;
    public Value value;
    
    public DropDownButton(final Panel panel, final DropDown dropDown, final String name, final float x, final float y, final Value value) {
        this.panel = panel;
        this.dropDown = dropDown;
        this.name = name;
        this.x = x;
        this.y = y;
        this.value = value;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.dropDownButtonContructor(this, this.dropDown, this.panel);
            }
        }
    }
    
    public void draw(final float x, final float y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.dropDownButtonDraw(this, this.dropDown, this.panel, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.dropDownButtonMouseClicked(this, this.dropDown, this.panel, x, y, button);
            }
        }
    }
}
