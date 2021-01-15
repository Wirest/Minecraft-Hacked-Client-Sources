package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.EventHandler;
import com.ihl.client.gui.GuiHandle;
import com.ihl.client.util.HelperUtil;

@EventHandler(events = {})
public class GUI extends Module {

    public GUI(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        if (!(Helper.mc().currentScreen instanceof GuiHandle)) {
            Helper.mc().displayGuiScreen(new GuiHandle());
        }
        disable();
    }
}
