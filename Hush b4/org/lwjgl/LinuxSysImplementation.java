// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.awt.Toolkit;

final class LinuxSysImplementation extends J2SESysImplementation
{
    private static final int JNI_VERSION = 19;
    
    public int getRequiredJNIVersion() {
        return 19;
    }
    
    public boolean openURL(final String url) {
        final String[] arr$;
        final String[] browsers = arr$ = new String[] { "sensible-browser", "xdg-open", "google-chrome", "chromium", "firefox", "iceweasel", "mozilla", "opera", "konqueror", "nautilus", "galeon", "netscape" };
        final int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            final String browser = arr$[i$];
            try {
                LWJGLUtil.execPrivileged(new String[] { browser, url });
                return true;
            }
            catch (Exception e) {
                e.printStackTrace(System.err);
                ++i$;
                continue;
            }
            break;
        }
        return false;
    }
    
    @Override
    public boolean has64Bit() {
        return true;
    }
    
    static {
        Toolkit.getDefaultToolkit();
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    System.loadLibrary("jawt");
                }
                catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
                return null;
            }
        });
    }
}
