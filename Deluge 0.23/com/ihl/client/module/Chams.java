package com.ihl.client.module;

import com.ihl.client.event.Event;
import com.ihl.client.event.EventEntityRender;
import com.ihl.client.event.EventHandler;
import org.lwjgl.opengl.GL11;

@EventHandler(events = {EventEntityRender.class})
public class Chams extends Module {

    public Chams(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventEntityRender) {
            EventEntityRender e = (EventEntityRender) event;
            if (e.type == Event.Type.PRE) {
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1, -1000000);
            } else if (e.type == Event.Type.POST) {
                GL11.glDisable(32823);
                GL11.glPolygonOffset(1, 1000000);
            }
        }
    }

}
