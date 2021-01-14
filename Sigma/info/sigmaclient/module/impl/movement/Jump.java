package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;

import javax.vecmath.Vector2d;

public class Jump extends Module {
    private boolean wasOnGround = false;

    public Jump(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                if (!mc.thePlayer.onGround) {
                    if (wasOnGround) {//S'il vient de quitter le sol
                        wasOnGround = false;
                        if (!mc.gameSettings.keyBindJump.pressed) {
                            mc.thePlayer.jump();
                            //ChatUtil.printChat(mc.thePlayer.motionX+" "+mc.thePlayer.motionZ);
                            Vector2d vector3d = new Vector2d(mc.thePlayer.motionX, mc.thePlayer.motionZ);
                            vector3d.normalize();
                            vector3d.scale(0.4);
                            mc.thePlayer.motionX = vector3d.getX();
                            mc.thePlayer.motionZ = vector3d.getY();
                        }
                    } else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -0.9D, 0)).isEmpty() && !mc.gameSettings.keyBindJump.pressed) {//Si il a un block en dessous de ses pieds
                        mc.thePlayer.motionY = -1;
                        if (!mc.gameSettings.keyBindForward.pressed && !mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindRight.pressed) {
                            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                        }
                    }
                } else {
                    /*if (!mc.thePlayer.isSprinting()) {
                        mc.thePlayer.setSprinting(true);
                    }*/
                    wasOnGround = true;
                }
            }
        }
    }
}
