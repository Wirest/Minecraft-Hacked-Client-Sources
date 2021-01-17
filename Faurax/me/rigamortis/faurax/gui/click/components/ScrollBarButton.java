package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.gui.click.theme.*;
import java.util.*;

public class ScrollBarButton implements RenderHelper
{
    public float x;
    public float y;
    public String name;
    public Panel panel;
    public ScrollBar scrollBar;
    public boolean enabled;
    
    public ScrollBarButton(final ScrollBar scrollBar, final Panel panel, final String name, final float x, final float y) {
        this.scrollBar = scrollBar;
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.ScrollBarButtonContructor(this, this.scrollBar, this.panel);
            }
        }
    }
    
    public void draw(final float x, final float y) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.ScrollBarButtonDraw(this, this.scrollBar, this.panel, x, y);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final Theme theme : Client.getThemes().themes) {
            if (theme.isVisible()) {
                theme.ScrollBarButtonMouseClicked(this, this.scrollBar, this.panel, x, y, button);
            }
        }
    }
}
