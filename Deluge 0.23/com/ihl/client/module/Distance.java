package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.EventHandler;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueDouble;
import org.lwjgl.input.Mouse;

@EventHandler(events = {})
public class Distance extends Module {

    public Distance(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("distance", new Option("Distance", "Change the third person view distance", new ValueDouble(10, new double[]{4, 100}, 0.1), Option.Type.NUMBER));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void tick() {
        super.tick();
        if (active && Helper.mc().currentScreen == null) {
            ValueDouble distance = (ValueDouble) Option.get(options, "distance").value;
            distance.value = (double) (distance.value) - (Mouse.getDWheel() / 120d);
        }
        Helper.mc().entityRenderer.thirdPersonDistance += ((active ? Option.get(options, "distance").DOUBLE() : 4) - Helper.mc().entityRenderer.thirdPersonDistance) / 4;
    }
}
