// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.input;

import java.lang.reflect.Field;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Method;
import org.lwjgl.opengl.Display;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.opengl.InputImplementation;

final class OpenGLPackageAccess
{
    static final Object global_lock;
    
    static InputImplementation createImplementation() {
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<InputImplementation>)new PrivilegedExceptionAction<InputImplementation>() {
                public InputImplementation run() throws Exception {
                    final Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation", (Class<?>[])new Class[0]);
                    getImplementation_method.setAccessible(true);
                    return (InputImplementation)getImplementation_method.invoke(null, new Object[0]);
                }
            });
        }
        catch (PrivilegedActionException e) {
            throw new Error(e);
        }
    }
    
    static {
        try {
            global_lock = AccessController.doPrivileged((PrivilegedExceptionAction<Object>)new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    final Field lock_field = Class.forName("org.lwjgl.opengl.GlobalLock").getDeclaredField("lock");
                    lock_field.setAccessible(true);
                    return lock_field.get(null);
                }
            });
        }
        catch (PrivilegedActionException e) {
            throw new Error(e);
        }
    }
}
