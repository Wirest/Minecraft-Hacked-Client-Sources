package skyline.specc.mods.move.speed;

import net.minecraft.client.Mineman;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.mods.move.Speed;

import static net.minecraft.client.Mineman.thePlayer;


public class Faithful extends ModMode<Speed>
{
    public Faithful(Speed parent, String name) {
        super(parent, "FaithFul");
    }

    @EventListener
    public void onMotion(EventMotion event) {
        mc.timer.timerSpeed = 1.0F / 3F;
        if (thePlayer.moveForward != 0.0 || thePlayer.moveStrafing != 0.0 || !mc.thePlayer.movementInput.jump) {
            thePlayer.setSpeed(2.5F); 
            mc.thePlayer.motionY = -0.3;
        }
        if (mc.thePlayer.movementInput.jump) {
        	event.setCancelled(true);
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
