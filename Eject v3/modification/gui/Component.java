package modification.gui;

import modification.extenders.Value;
import modification.interfaces.MCHook;

public abstract class Component
        implements MCHook {
    public final Value value;
    public float x;
    public float y;
    public int heightOffset;

    protected Component(Value paramValue) {
        this.value = paramValue;
        this.heightOffset = 1;
    }

    public abstract void draw(int paramInt1, int paramInt2);

    public abstract void click(int paramInt1, int paramInt2, int paramInt3);

    public abstract void release(int paramInt);
}




