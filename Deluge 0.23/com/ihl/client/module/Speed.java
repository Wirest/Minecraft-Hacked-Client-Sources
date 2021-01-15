package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPlayerMove;
import com.ihl.client.event.EventPlayerUpdate;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueBoolean;
import com.ihl.client.module.option.ValueChoice;
import com.ihl.client.module.option.ValueDouble;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@EventHandler(events = {EventPlayerMove.class, EventPlayerUpdate.class})
public class Speed extends Module {

    private boolean jumped;

    public Speed(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("speed", new Option("Speed", "Movement speed multiplier", new ValueDouble(2, new double[]{0.1, 10}, 0.1), Option.Type.NUMBER));
        options.put("ncp", new Option("NCP", "Use NCP bypassed speed", new ValueBoolean(true), Option.Type.BOOLEAN, new Option[]{
                new Option("Mode", "NCP version selector", new ValueChoice(1, new String[]{"normal", "fast"}), Option.Type.CHOICE),
                new Option("Timer", "NCP speed timer modifier", new ValueDouble(1.15, new double[]{0.1, 10}, 0.01), Option.Type.NUMBER)
        }));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        jumped = false;
    }

    public void disable() {
        super.disable();
        Helper.mc().timer.timerSpeed = 1f;
    }

    protected void onEvent(Event event) {
        double speed = Option.get(options, "speed").DOUBLE();
        boolean ncp = Option.get(options, "ncp").BOOLEAN();
        String ncpMode = Option.get(options, "ncp", "mode").STRING();
        double ncpTimer = Option.get(options, "ncp", "timer").DOUBLE();

        if (event instanceof EventPlayerMove) {
            EventPlayerMove e = (EventPlayerMove) event;
            if (e.type == Event.Type.PRE) {
                if (!ncp) {
                    e.x *= speed;
                    e.z *= speed;
                }
            } else if (e.type == Event.Type.POST) {
                if (ncp) {
                    if (Helper.player().onGround && !Helper.mc().gameSettings.keyBindJump.getIsKeyPressed() && !Helper.player().isCollidedHorizontally && (Helper.player().moveForward != 0 || Helper.player().moveStrafing != 0) && !Helper.player().isSneaking()) {
                        double val = (Helper.player().rotationYaw + 90 + (Helper.player().moveForward > 0 ? 0 + (Helper.player().moveStrafing > 0 ? -45 : Helper.player().moveStrafing < 0 ? 45 : 0) : Helper.player().moveForward < 0 ? 180 + (Helper.player().moveStrafing > 0 ? 45 : Helper.player().moveStrafing < 0 ? -45 : 0) : 0 + (Helper.player().moveStrafing > 0 ? -90 : Helper.player().moveStrafing < 0 ? 90 : 0))) * Math.PI / 180;

                        double x = Math.cos(val) * (ncpMode.equalsIgnoreCase("fast") ? 0.27 : 0.2);
                        double z = Math.sin(val) * (ncpMode.equalsIgnoreCase("fast") ? 0.27 : 0.2);

                        Helper.player().motionX += x;
                        Helper.player().motionY = ncpMode.equalsIgnoreCase("fast") ? 0.145 : 0.21;
                        Helper.player().motionZ += z;

                        Helper.mc().timer.timerSpeed = (float) ncpTimer;

                        jumped = true;
                    } else {
                        Helper.mc().timer.timerSpeed = 1f;
                    }
                }
            }
        } else if (event instanceof EventPlayerUpdate) {
            EventPlayerUpdate e = (EventPlayerUpdate) event;
            if (e.type == Event.Type.POST) {
                if (ncp) {
                    if (jumped && !Helper.player().onGround && !Helper.mc().gameSettings.keyBindJump.getIsKeyPressed() && !Helper.player().isOnLadder()) {
                        Helper.player().motionY = Math.min(ncpMode.equalsIgnoreCase("fast") ? -0.2 : 0, Helper.player().motionY);
                        jumped = false;
                    }
                }
            }
        }
    }

}
