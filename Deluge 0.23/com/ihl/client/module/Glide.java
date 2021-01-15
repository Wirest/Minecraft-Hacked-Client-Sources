package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPlayerLiving;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueDouble;

@EventHandler(events = {EventPlayerLiving.class})

public class Glide extends Module {

    public Glide(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("speed", new Option("Speed", "Glide decent speed", new ValueDouble(0.05, new double[] {0, 2}, 0.01), Option.Type.NUMBER));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        double speed = Option.get(options, "speed").DOUBLE();
        if (event instanceof EventPlayerLiving) {
            EventPlayerLiving e = (EventPlayerLiving) event;
            if (e.type == Event.Type.PRE) {
                Helper.player().motionY = Math.max(Helper.player().motionY, -speed);
            }
        }
    }
}
