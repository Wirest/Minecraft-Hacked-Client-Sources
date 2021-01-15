// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.account.screen;

public abstract class Screen
{
    public abstract void draw(final int p0, final int p1);
    
    public abstract void onClick(final int p0, final int p1, final int p2);
    
    public abstract void onKeyPress(final char p0, final int p1);
    
    public abstract void update();
}
