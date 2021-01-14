package com.minimap.events;

import net.minecraft.client.settings.*;

public class KeyEvent
{
    public KeyBinding kb;
    public boolean tickEnd;
    public boolean isRepeat;
    public boolean keyDown;
    
    public KeyEvent(final KeyBinding kb, final boolean tickEnd, final boolean isRepeat, final boolean keyDown) {
        this.kb = kb;
        this.tickEnd = tickEnd;
        this.isRepeat = isRepeat;
        this.keyDown = keyDown;
    }
}
