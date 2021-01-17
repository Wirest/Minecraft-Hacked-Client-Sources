/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Event.events;

import Blizzard.Event.Event;

public class EventRender2D
extends Event {
    public int width;
    public int height;

    public EventRender2D(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

