// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.examples.lights;

import org.newdawn.slick.Color;

class Light
{
    private float xpos;
    private float ypos;
    private float strength;
    private Color col;
    
    public Light(final float x, final float y, final float str, final Color col) {
        this.xpos = x;
        this.ypos = y;
        this.strength = str;
        this.col = col;
    }
    
    public void setLocation(final float x, final float y) {
        this.xpos = x;
        this.ypos = y;
    }
    
    public float[] getEffectAt(final float x, final float y, final boolean colouredLights) {
        final float dx = x - this.xpos;
        final float dy = y - this.ypos;
        final float distance2 = dx * dx + dy * dy;
        float effect = 1.0f - distance2 / (this.strength * this.strength);
        if (effect < 0.0f) {
            effect = 0.0f;
        }
        if (colouredLights) {
            return new float[] { this.col.r * effect, this.col.g * effect, this.col.b * effect };
        }
        return new float[] { effect, effect, effect };
    }
}
