package modification.managers;

import modification.extenders.Module;
import modification.interfaces.Event;

public final class EventManager {
    public final void call(Event paramEvent) {
        for (int i = 0; i < ModuleManager.MODULES.size(); i++) {
            Module localModule = (Module) ModuleManager.MODULES.get(i);
            if ((localModule != null) && (localModule.enabled)) {
                localModule.onEvent(paramEvent);
            }
        }
    }
}




