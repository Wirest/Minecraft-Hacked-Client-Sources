// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.Sys;
import org.lwjgl.LWJGLException;

final class WindowsRegistry
{
    static final int HKEY_CLASSES_ROOT = 1;
    static final int HKEY_CURRENT_USER = 2;
    static final int HKEY_LOCAL_MACHINE = 3;
    static final int HKEY_USERS = 4;
    
    static String queryRegistrationKey(final int root_key, final String subkey, final String value) throws LWJGLException {
        switch (root_key) {
            case 1:
            case 2:
            case 3:
            case 4: {
                return nQueryRegistrationKey(root_key, subkey, value);
            }
            default: {
                throw new IllegalArgumentException("Invalid enum: " + root_key);
            }
        }
    }
    
    private static native String nQueryRegistrationKey(final int p0, final String p1, final String p2) throws LWJGLException;
    
    static {
        Sys.initialize();
    }
}
