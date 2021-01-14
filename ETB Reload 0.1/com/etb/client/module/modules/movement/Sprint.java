package com.etb.client.module.modules.movement;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.BooleanValue;

public class Sprint extends Module {
    private BooleanValue multiDir = new BooleanValue("MultiDirectional",true);
    public Sprint() {
        super("Sprint", Category.MOVEMENT, new Color(0,255,0,255).getRGB());
        setDescription("Basically toggle sprint");
        addValues(multiDir);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        mc.thePlayer.setSprinting(canSprint());
    }
    private boolean canSprint() {
        return !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking() && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && (multiDir.isEnabled() ? mc.thePlayer.isMoving() : mc.thePlayer.moveForward > 0);
    }
}
