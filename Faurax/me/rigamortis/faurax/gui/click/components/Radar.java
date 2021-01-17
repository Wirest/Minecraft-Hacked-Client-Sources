package me.rigamortis.faurax.gui.click.components;

import me.rigamortis.faurax.module.helpers.*;

public class Radar implements RenderHelper
{
    private float x;
    private float y;
    private String name;
    public Panel panel;
    
    public Radar(final Panel panel, final String name, final float x, final float y) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public void draw(final float x, final float y) {
    }
}
