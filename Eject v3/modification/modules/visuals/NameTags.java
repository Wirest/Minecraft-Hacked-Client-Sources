package modification.modules.visuals;

import modification.enummerates.Category;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;

public final class NameTags
        extends Module {
    public final Value<Float> alpha = new Value("Alpha", Float.valueOf(0.7F), 0.1F, 1.0F, 1, this, new String[0]);
    public final Value<Float> minScale = new Value("Min scale", Float.valueOf(1.6F), 0.5F, 2.0F, 1, this, new String[0]);
    public final Value<Float> maxScale = new Value("Max scale", Float.valueOf(5.0F), 0.0F, 9.0F, 0, this, new String[0]);

    public NameTags(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
    }

    protected void onDeactivated() {
    }
}




