package net.minecraft.util;

public abstract class LazyLoadBase {
    private Object value;
    private boolean isLoaded = false;
    private static final String __OBFID = "CL_00002263";

    public Object getValue() {
        if (!isLoaded) {
            isLoaded = true;
            value = load();
        }

        return value;
    }

    protected abstract Object load();
}
