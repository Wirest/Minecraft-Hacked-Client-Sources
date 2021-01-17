// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

interface SysImplementation
{
    int getRequiredJNIVersion();
    
    int getJNIVersion();
    
    int getPointerSize();
    
    void setDebug(final boolean p0);
    
    long getTimerResolution();
    
    long getTime();
    
    void alert(final String p0, final String p1);
    
    boolean openURL(final String p0);
    
    String getClipboard();
    
    boolean has64Bit();
}
