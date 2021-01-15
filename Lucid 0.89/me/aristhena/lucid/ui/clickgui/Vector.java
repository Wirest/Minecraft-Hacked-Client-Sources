/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.clickgui;

public class Vector {
    public float x;
    public float y;

    public Vector(float x, float y) {
        this.set(x, y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector offset(float x, float y) {
        return new Vector(this.x + x, this.y + y);
    }

    public Vector offset(Vector vec) {
        return new Vector(this.x + vec.x, this.y + vec.y);
    }
}

