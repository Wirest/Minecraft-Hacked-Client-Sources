package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPlayerMove;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.util.HelperUtil;

@EventHandler(events = {EventPlayerMove.class})
public class Noclip extends Module {

    private boolean noClip;

    public Noclip(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("speed", new Option("Speed", "Movement speed", new ValueDouble(0.23, new double[] {0, 4}, 0.01), Option.Type.NUMBER));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        if (!HelperUtil.inGame()) {
            return;
        }
        noClip = Helper.player().noClip;
        Helper.player().noClip = true;
    }

    public void disable() {
        super.disable();
        if (!HelperUtil.inGame()) {
            return;
        }
        Helper.player().noClip = noClip;
    }

    protected void onEvent(Event event) {
        double speed = Option.get(options, "speed").DOUBLE();

        if (event instanceof EventPlayerMove) {
            EventPlayerMove e = (EventPlayerMove) event;
            if (e.type == Event.Type.PRE) {
                Helper.player().motionX = 0;
                Helper.player().motionY = 0;
                Helper.player().motionZ = 0;

                e.x = 0;
                e.y = 0;
                e.z = 0;

                double val = (Helper.player().rotationYaw + 90 + (Helper.player().moveForward > 0 ? 0 + (Helper.player().moveStrafing > 0 ? -45 : Helper.player().moveStrafing < 0 ? 45 : 0) : Helper.player().moveForward < 0 ? 180 + (Helper.player().moveStrafing > 0 ? 45 : Helper.player().moveStrafing < 0 ? -45 : 0) : 0 + (Helper.player().moveStrafing > 0 ? -90 : Helper.player().moveStrafing < 0 ? 90 : 0))) * Math.PI / 180;

                double sp = (Helper.player().moveForward != 0 || Helper.player().moveStrafing != 0 ? speed : 0);
                double x = Math.cos(val) * sp;
                double y = (Helper.mc().gameSettings.keyBindJump.getIsKeyPressed() ? speed : Helper.mc().gameSettings.keyBindSneak.getIsKeyPressed() ? -speed : 0);
                double z = Math.sin(val) * sp;

                if (x != 0 || y != 0 || z != 0) {
                    active = false;
                    boolean noclip = Helper.player().noClip;
                    Helper.player().noClip = true;
                    Helper.player().moveEntity(x, y, z);
                    Helper.player().noClip = noclip;
                    active = true;
                }
            }
        }
    }

}
