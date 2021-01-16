/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.EventCancellable;
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

