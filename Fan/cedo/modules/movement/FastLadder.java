package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventPacket;
import cedo.modules.Module;
import cedo.util.Timer;
import cedo.util.movement.MovementUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class FastLadder extends Module {

    public Timer timer = new Timer(), lagbackTimer = new Timer();
    boolean speedBoost = false;

    public FastLadder() {
        super("FastLadder", Keyboard.KEY_NONE, Category.MOVEMENT);
        lagbackTimer.reset();
    }

    public void onEnable() {
        speedBoost = false;
        timer.reset();
        super.onEnable();
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1;
        lagbackTimer.reset();
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventPacket && e.isPre() && e.isIncoming()) {
            EventPacket event = (EventPacket) e;
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                timer.reset();
                mc.timer.timerSpeed = 1;
                speedBoost = false;
                lagbackTimer.reset();
            }
        }
    	/*if(e instanceof EventMove) {
    		EventMove event = (EventMove)e;
    		if(event.y > 0 && mc.thePlayer.isOnLadder())
    			event.y *= 2.5;
    	}*/
        if (e instanceof EventMotion && e.isPre()) {
            if (mc.thePlayer.isOnLadder() && MovementUtil.isMoving()) {
                if (timer.hasTimeElapsed(200, true)) {
                    speedBoost = !speedBoost;
                }

                if (lagbackTimer.hasTimeElapsed(5000, false)) {
                    if (speedBoost)
                        mc.timer.timerSpeed = 2f;
                    else {
                        mc.timer.timerSpeed = 1.09f;
                    }
                } else
                    mc.timer.timerSpeed = 1;

            } else {
                speedBoost = false;
                mc.timer.timerSpeed = 1;
            }
        }
    }
}
