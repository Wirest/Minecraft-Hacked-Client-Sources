package cedo.modules.movement;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventMove;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.util.movement.MovementUtil;
import org.lwjgl.input.Keyboard;


public class LongJump extends Module {

    double movementSpeed;
    boolean didHop;
    boolean ifair;

    public BooleanSetting damage = new BooleanSetting("Damage", false);
    public BooleanSetting autoDisable = new BooleanSetting("AutoDisable", true);

    public LongJump() {
        super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT);
        addSettings(damage);
    }

    public void onEnable() {
    	ifair = false;
        movementSpeed = 0.4;
        super.onEnable();
    }

    public void onDisable() {
    	ifair = false;
        super.onDisable();
    }
    //public void onEvent(Event e) {

    public void onEvent(Event e) {
    	if(e instanceof EventMotion) {
    		if(mc.thePlayer.ticksExisted % 2 == 0) {
    		((EventMotion) e).setY(((EventMotion) e).getY() + 1.0E-8);
    		}
    	}
    	
    	
        if (e instanceof EventMove) {
            if (MovementUtil.isMovingOnGround()) {
                if(damage.isEnabled())
                    Fan.fly.damage();
                ((EventMove) e).setY(mc.thePlayer.motionY = MovementUtil.getJumpBoostModifier(0.42F));
                movementSpeed = !damage.isEnabled() ? MovementUtil.getRetarded() * 2.15F : MovementUtil.getRetarded() * 2.19F;
                didHop = true;
            } else {
                if (didHop) {
                    movementSpeed = 1.2835F;
                    didHop = false;
                } else {
                    movementSpeed -= movementSpeed / 159;
                }
            }
            MovementUtil.setSpeed((EventMove) e, movementSpeed);
        }
        if (e instanceof EventUpdate) {
        	if(autoDisable.isEnabled()) {
            if (!ifair && !mc.thePlayer.onGround) {
                ifair = true;
            } else if (ifair && mc.thePlayer.onGround) {
                toggle();
            	}
        	}
        }
    }
}


