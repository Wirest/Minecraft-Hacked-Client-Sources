package skyline.specc.mods.move.speed;

import net.minecraft.client.Mineman;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.move.Speed;

import static net.minecraft.client.Mineman.thePlayer;


public class Area51Hop extends ModMode<Speed>
{
    public Area51Hop(Speed parent, String name) {
        super(parent, "FaithfulHop");
    }

    @EventListener
    public void onMotion(EventMotion event) {

        if (mc.thePlayer.isCollidedVertically) {
            mc.timer.timerSpeed = 1.0F;
            mc.thePlayer.motionY = 0.34F;
            
        }
        if (thePlayer.moveForward != 0.0 || thePlayer.moveStrafing != 0.0) {
            mc.timer.timerSpeed = 0.4F;
            thePlayer.setSpeed(0.65F); 
        }
    }


    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.setSpeed(0);
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
    }
}
