// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

public interface LifeCycle
{
    void start();
    
    void stop();
    
    boolean isStarted();
}
