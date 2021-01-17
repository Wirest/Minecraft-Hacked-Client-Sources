package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class Slider implements RenderHelper
{
    public float x;
    public float y;
    public String name;
    public Panel panel;
    public boolean dragging;
    public float dragX;
    public float lastDragX;
    public Value value;
    
    public Slider(final Panel panel, final String name, final float x, final float y, final Value value) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.value = value;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.SliderContructor(this, this.panel);
            }
        }
    }
    
    public void draw(final float x, final float y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.SliderDraw(this, this.panel, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.SliderMouseClicked(this, this.panel, x, y, button);
            }
        }
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.SliderMouseMovedOrUp(this, this.panel, x, y, button);
            }
        }
    }
}
