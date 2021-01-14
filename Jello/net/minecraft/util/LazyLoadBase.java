package net.minecraft.util;

public abstract class LazyLoadBase
{
    private Object value;
    private boolean isLoaded = false;
    

    public Object getValue()
    {
        if (!this.isLoaded)
        {
            this.isLoaded = true;
            this.value = this.load();
        }

        return this.value;
    }

    protected abstract Object load();
}
