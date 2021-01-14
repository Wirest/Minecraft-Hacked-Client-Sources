package modification.modules.visuals;

import modification.enummerates.Category;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.interfaces.Event;

public final class FullBright
        extends Module {
    public FullBright(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            MC.gameSettings.gammaSetting = 1000.0F;
        }
    }

    protected void onDeactivated() {
        MC.gameSettings.gammaSetting = 0.0F;
    }
}




