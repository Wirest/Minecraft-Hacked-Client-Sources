package modification.modules.misc;

import modification.enummerates.Category;
import modification.events.EventSwingItem;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;

public final class NoSwing
        extends Module {
    public final Value<String> mode = new Value("Mode", "Server", new String[]{"Server", "Client"}, this, new String[0]);

    public NoSwing(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventSwingItem)) {
            ((EventSwingItem) paramEvent).canceled = true;
        }
    }

    protected void onDeactivated() {
    }
}




