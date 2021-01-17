// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.nio.ByteBuffer;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Method;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.opengl.Display;

final class WindowsSysImplementation extends DefaultSysImplementation
{
    private static final int JNI_VERSION = 24;
    
    public int getRequiredJNIVersion() {
        return 24;
    }
    
    @Override
    public long getTimerResolution() {
        return 1000L;
    }
    
    @Override
    public long getTime() {
        return nGetTime();
    }
    
    private static native long nGetTime();
    
    @Override
    public boolean has64Bit() {
        return true;
    }
    
    private static long getHwnd() {
        if (!Display.isCreated()) {
            return 0L;
        }
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<Long>)new PrivilegedExceptionAction<Long>() {
                public Long run() throws Exception {
                    final Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation", (Class<?>[])new Class[0]);
                    getImplementation_method.setAccessible(true);
                    final Object display_impl = getImplementation_method.invoke(null, new Object[0]);
                    final Class<?> WindowsDisplay_class = Class.forName("org.lwjgl.opengl.WindowsDisplay");
                    final Method getHwnd_method = WindowsDisplay_class.getDeclaredMethod("getHwnd", (Class<?>[])new Class[0]);
                    getHwnd_method.setAccessible(true);
                    return (Long)getHwnd_method.invoke(display_impl, new Object[0]);
                }
            });
        }
        catch (PrivilegedActionException e) {
            throw new Error(e);
        }
    }
    
    @Override
    public void alert(final String title, final String message) {
        if (!Display.isCreated()) {
            initCommonControls();
        }
        LWJGLUtil.log(String.format("*** Alert *** %s\n%s\n", title, message));
        final ByteBuffer titleText = MemoryUtil.encodeUTF16(title);
        final ByteBuffer messageText = MemoryUtil.encodeUTF16(message);
        nAlert(getHwnd(), MemoryUtil.getAddress(titleText), MemoryUtil.getAddress(messageText));
    }
    
    private static native void nAlert(final long p0, final long p1, final long p2);
    
    private static native void initCommonControls();
    
    public boolean openURL(final String url) {
        try {
            LWJGLUtil.execPrivileged(new String[] { "rundll32", "url.dll,FileProtocolHandler", url });
            return true;
        }
        catch (Exception e) {
            LWJGLUtil.log("Failed to open url (" + url + "): " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getClipboard() {
        return nGetClipboard();
    }
    
    private static native String nGetClipboard();
    
    static {
        Sys.initialize();
    }
}
