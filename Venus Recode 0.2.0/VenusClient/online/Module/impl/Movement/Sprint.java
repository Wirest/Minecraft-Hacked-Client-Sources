package VenusClient.online.Module.impl.Movement;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Sprint",Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event) {
        if (event.getType() == EventMotionUpdate.Type.PRE) {
            if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0)
                mc.thePlayer.setSprinting(true);
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();

        mc.thePlayer.setSprinting(false);
    }
}
