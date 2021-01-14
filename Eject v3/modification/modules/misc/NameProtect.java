package modification.modules.misc;

import modification.enummerates.Category;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.interfaces.Event;
import modification.main.Modification;

public final class NameProtect
        extends Module {
    public NameProtect(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if (((paramEvent instanceof EventTick)) && (!Modification.FRIEND_MANAGER.containsFriend(MC.thePlayer.getName())) && (Modification.user.equals("haze"))) {
            Modification.FRIEND_MANAGER.add(MC.thePlayer.getName(), Modification.user);
        }
    }

    protected void onDeactivated() {
        if (Modification.FRIEND_MANAGER.containsFriend(MC.thePlayer.getName())) {
            Modification.FRIEND_MANAGER.remove(MC.thePlayer.getName());
        }
    }
}




