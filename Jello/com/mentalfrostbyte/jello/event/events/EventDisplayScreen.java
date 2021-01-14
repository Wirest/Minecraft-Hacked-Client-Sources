package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.client.gui.GuiScreen;

public class EventDisplayScreen
extends EventCancellable {
    private GuiScreen guiScreen;

    public EventDisplayScreen(GuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }

    public void setGuiScreen(GuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }

    public GuiScreen getGuiScreen() {
        return this.guiScreen;
    }
}

