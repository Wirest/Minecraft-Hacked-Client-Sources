package com.ihl.client.module;

import com.ihl.client.event.EventHandler;

@EventHandler(events = {})
public class CameraClip extends Module {

    public CameraClip(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }
}
