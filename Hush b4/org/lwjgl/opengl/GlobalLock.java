// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

final class GlobalLock
{
    static final Object lock;
    
    static {
        lock = new Object();
    }
}
