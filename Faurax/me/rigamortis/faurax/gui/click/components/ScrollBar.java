package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class ScrollBar implements RenderHelper
{
    public float x;
    public float y;
    private String name;
    public Panel panel;
    public boolean enabled;
    public boolean dragging;
    public float dragY;
    public float lastDragY;
    public float height;
    public ArrayList<ScrollBarButton> buttons;
    
    public ScrollBar(final Panel panel, final String name, final float x, final float y, final float height) {
        this.buttons = new ArrayList<ScrollBarButton>();
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.height = height;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.ScrollBarContructor(this, this.panel, x, y);
            }
        }
    }
    
    public void draw(final float x, final float y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.ScrollBarDraw(this, this.panel, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.ScrollBarMouseClicked(this, this.panel, x, y, button);
            }
        }
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.ScrollBarMouseMovedOrUp(this, this.panel, x, y, button);
            }
        }
    }
}
