package cheatware.module.movement;

import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventCollide;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;

public class Step extends Module {
	
    public Step() {
        super("Step", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	if(mc.thePlayer.isCollidedHorizontally) {
    		if(mc.thePlayer.onGround && canStep()) {
    			mc.thePlayer.jump();
    		} else {
    			mc.thePlayer.motionY = -0.5F;
    		}
    	}
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.stepHeight = .5F;
    }
    
    public boolean canStep(){
        double yaw = mc.thePlayer.getDirection();
        double x = -Math.sin(yaw) * 0.4;
        double z = Math.cos(yaw) * 0.4;
        return mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,mc.thePlayer.getEntityBoundingBox().offset(x, 1.001335979112147, z)).isEmpty();
    }
}
