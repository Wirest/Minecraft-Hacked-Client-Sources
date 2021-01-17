// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui;

import net.minecraft.realms.RealmsButton;

public class RealmsHideableButton extends RealmsButton
{
    boolean visible;
    
    public RealmsHideableButton(final int id, final int x, final int y, final int width, final int height, final String msg) {
        super(id, x, y, width, height, msg);
        this.visible = true;
    }
    
    @Override
    public void render(final int xm, final int ym) {
        if (!this.visible) {
            return;
        }
        super.render(xm, ym);
    }
    
    @Override
    public void clicked(final int mx, final int my) {
        if (!this.visible) {
            return;
        }
        super.clicked(mx, my);
    }
    
    @Override
    public void released(final int mx, final int my) {
        if (!this.visible) {
            return;
        }
        super.released(mx, my);
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public boolean getVisible() {
        return this.visible;
    }
}
