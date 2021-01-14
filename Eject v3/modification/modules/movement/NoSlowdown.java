package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventSlowdown;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;

public final class NoSlowdown
        extends Module {
    private final Value<Float> motion = new Value("Slowdown value", Float.valueOf(0.5F), 0.0F, 0.8F, 1, this, new String[0]);

    public NoSlowdown(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            this.tag = Float.toString(((Float) this.motion.value).floatValue());
        }
        if ((paramEvent instanceof EventSlowdown)) {
            MC.thePlayer.setSprinting(false);
            ((EventSlowdown) paramEvent).motionValue = (1.0F - ((Float) this.motion.value).floatValue());
        }
    }

    protected void onDeactivated() {
    }
}




