// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

abstract class DefaultSysImplementation implements SysImplementation
{
    public native int getJNIVersion();
    
    public native int getPointerSize();
    
    public native void setDebug(final boolean p0);
    
    public long getTimerResolution() {
        return 1000L;
    }
    
    public boolean has64Bit() {
        return false;
    }
    
    public abstract long getTime();
    
    public abstract void alert(final String p0, final String p1);
    
    public abstract String getClipboard();
}
