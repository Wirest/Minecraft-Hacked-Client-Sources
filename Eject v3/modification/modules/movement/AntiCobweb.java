package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventPostMotion;
import modification.extenders.Module;
import modification.interfaces.Event;

public final class AntiCobweb
        extends Module {
    public AntiCobweb(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if (((paramEvent instanceof EventPostMotion)) && (MC.thePlayer.isInWeb)) {
            MC.thePlayer.onGround = true;
        }
    }

    protected void onDeactivated() {
    }
}




