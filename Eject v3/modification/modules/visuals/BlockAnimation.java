package modification.modules.visuals;

import modification.enummerates.Category;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;

public final class BlockAnimation
        extends Module {
    public final Value<String> mode = new Value("Mode", "Old", new String[]{"Old", "New", "Custom"}, this, new String[0]);
    public final Value<Float> height = new Value("Height", Float.valueOf(0.0F), 0.0F, 1.0F, 1, this, new String[]{"Mode", "Custom"});
    public final Value<Float> offset = new Value("Offset", Float.valueOf(1.0F), 1.0F, 10.0F, 1, this, new String[]{"Mode", "Custom"});
    public final Value<Float> chokeValue = new Value("Choke", Float.valueOf(1.0F), 1.0F, 10.0F, 0, this, new String[]{"Mode", "Custom"});

    public BlockAnimation(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
    }

    protected void onDeactivated() {
    }
}




