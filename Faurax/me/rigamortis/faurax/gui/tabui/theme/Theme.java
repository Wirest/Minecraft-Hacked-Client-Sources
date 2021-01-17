package me.rigamortis.faurax.gui.tabui.theme;

import me.rigamortis.faurax.module.*;

public abstract class Theme
{
    public static String name;
    public static boolean active;
    
    public abstract void draw(final int p0, final boolean p1, final float p2, final int p3, final int p4, final Module p5, final int p6);
}
