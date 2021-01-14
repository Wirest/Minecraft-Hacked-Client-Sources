package org.lwjgl.input;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.InputImplementation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

final class OpenGLPackageAccess {
    static final Object global_lock;

    static {
        try {
            global_lock = AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run()
                        throws Exception {
                    Field localField = Class.forName("org.lwjgl.opengl.GlobalLock").getDeclaredField("lock");
                    localField.setAccessible(true);
                    return localField.get(null);
                }
            });
        } catch (PrivilegedActionException localPrivilegedActionException) {
            throw new Error(localPrivilegedActionException);
        }
    }

    static InputImplementation createImplementation() {
        try {
            (InputImplementation) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public InputImplementation run()
                        throws Exception {
                    Method localMethod = Display.class.getDeclaredMethod("getImplementation", new Class[0]);
                    localMethod.setAccessible(true);
                    return (InputImplementation) localMethod.invoke(null, new Object[0]);
                }
            });
        } catch (PrivilegedActionException localPrivilegedActionException) {
            throw new Error(localPrivilegedActionException);
        }
    }
}




