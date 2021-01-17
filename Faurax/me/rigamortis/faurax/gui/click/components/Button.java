package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class Button implements RenderHelper
{
    public float x;
    public float y;
    public String name;
    public Panel panel;
    public boolean enabled;
    
    public Button(final Panel panel, final String name, final float x, final float y) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.buttonContructor(this, this.panel);
            }
        }
    }
    
    public void draw(final float x, final float y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.buttonDraw(this, this.panel, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.buttonMouseClicked(this, this.panel, x, y, button);
            }
        }
    }
}
