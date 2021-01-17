// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public abstract class LazyLoadBase<T>
{
    private T value;
    private boolean isLoaded;
    
    public LazyLoadBase() {
        this.isLoaded = false;
    }
    
    public T getValue() {
        if (!this.isLoaded) {
            this.isLoaded = true;
            this.value = this.load();
        }
        return this.value;
    }
    
    protected abstract T load();
}
