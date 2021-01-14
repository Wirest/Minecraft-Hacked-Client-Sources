package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.value.impl.EnumValue;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
public class FastLadder extends Module {
    private EnumValue<mode> Mode = new EnumValue<>("Mode", mode.NORMAL);

    public FastLadder() {
        super("Fast Ladder", Category.MOVEMENT, new Color(168, 166, 158).getRGB());
        setDescription("Climb Faster!");
    }
    public enum mode {
        NORMAL, AAC, SPARTAN, CUBECRAFT
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            setSuffix(StringUtils.capitalize(Mode.getValue().name().toLowerCase()));
            switch (Mode.getValue()) {
                case NORMAL:
                    if ((getMc().thePlayer.moveForward != 0.0f || getMc().thePlayer.moveStrafing != 0.0f) && getMc().thePlayer.isOnLadder() && getMc().thePlayer.isCollidedHorizontally) {
                        getMc().thePlayer.motionY = 0.75;
                    }
                break;
                case AAC:
                    if ((getMc().thePlayer.moveForward != 0.0f || getMc().thePlayer.moveStrafing != 0.0f) && getMc().thePlayer.isOnLadder() && getMc().thePlayer.isCollidedHorizontally) {
                        getMc().thePlayer.motionY *= 1.3;
                    }
                    break;
                case SPARTAN:

                    break;
                case CUBECRAFT:
                    if ((getMc().thePlayer.moveForward != 0.0f || getMc().thePlayer.moveStrafing != 0.0f) && getMc().thePlayer.isOnLadder() && getMc().thePlayer.isCollidedHorizontally) {
                        getMc().thePlayer.motionY *= 2.3;
                    }
                    break;
            }
        }
    }
}
