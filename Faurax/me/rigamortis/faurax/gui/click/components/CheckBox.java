package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class CheckBox implements RenderHelper
{
    public float x;
    public float y;
    public String name;
    public Panel panel;
    public boolean enabled;
    public Value value;
    
    public CheckBox(final Panel panel, final String name, final float x, final float y, final Value val) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.value = val;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.checkBoxContructor(this, this.panel);
            }
        }
    }
    
    public void draw(final float x, final float y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.checkBoxDraw(this, this.panel, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.checkBoxMouseClicked(this, this.panel, x, y, button);
            }
        }
    }
}
