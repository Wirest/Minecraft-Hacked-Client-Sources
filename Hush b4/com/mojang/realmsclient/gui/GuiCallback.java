// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui;

import net.minecraft.realms.RealmsButton;

public interface GuiCallback
{
    void tick();
    
    void buttonClicked(final RealmsButton p0);
}
