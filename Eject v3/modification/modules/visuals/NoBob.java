package modification.modules.visuals;

import modification.enummerates.Category;
import modification.events.EventPreMotion;
import modification.extenders.Module;
import modification.interfaces.Event;

public final class NoBob
        extends Module {
    public NoBob(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventPreMotion)) {
            MC.thePlayer.distanceWalkedModified = 0.0F;
            MC.gameSettings.viewBobbing = true;
        }
    }

    protected void onDeactivated() {
    }
}




