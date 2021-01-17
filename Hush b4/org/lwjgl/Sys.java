// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivilegedExceptionAction;
import java.lang.reflect.Method;
import org.lwjgl.input.Mouse;
import java.security.AccessController;
import java.io.File;
import java.security.PrivilegedAction;

public final class Sys
{
    private static final String JNI_LIBRARY_NAME = "lwjgl";
    private static final String VERSION = "2.9.4";
    private static final String POSTFIX64BIT = "64";
    private static final SysImplementation implementation;
    private static final boolean is64Bit;
    
    private static void doLoadLibrary(final String lib_name) {
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            public Object run() {
                final String library_path = System.getProperty("org.lwjgl.librarypath");
                if (library_path != null) {
                    System.load(library_path + File.separator + LWJGLUtil.mapLibraryName(lib_name));
                }
                else {
                    System.loadLibrary(lib_name);
                }
                return null;
            }
        });
    }
    
    private static void loadLibrary(final String lib_name) {
        final String osArch = System.getProperty("os.arch");
        final boolean try64First = LWJGLUtil.getPlatform() != 2 && ("amd64".equals(osArch) || "x86_64".equals(osArch));
        Error err = null;
        if (try64First) {
            try {
                doLoadLibrary(lib_name + "64");
                return;
            }
            catch (UnsatisfiedLinkError e) {
                err = e;
            }
        }
        try {
            doLoadLibrary(lib_name);
        }
        catch (UnsatisfiedLinkError e) {
            if (try64First) {
                throw err;
            }
            if (Sys.implementation.has64Bit()) {
                try {
                    doLoadLibrary(lib_name + "64");
                    return;
                }
                catch (UnsatisfiedLinkError e2) {
                    LWJGLUtil.log("Failed to load 64 bit library: " + e2.getMessage());
                }
            }
            throw e;
        }
    }
    
    private static SysImplementation createImplementation() {
        switch (LWJGLUtil.getPlatform()) {
            case 1: {
                return new LinuxSysImplementation();
            }
            case 3: {
                return new WindowsSysImplementation();
            }
            case 2: {
                return new MacOSXSysImplementation();
            }
            default: {
                throw new IllegalStateException("Unsupported platform");
            }
        }
    }
    
    private Sys() {
    }
    
    public static String getVersion() {
        return "2.9.4";
    }
    
    public static void initialize() {
    }
    
    public static boolean is64Bit() {
        return Sys.is64Bit;
    }
    
    public static long getTimerResolution() {
        return Sys.implementation.getTimerResolution();
    }
    
    public static long getTime() {
        return Sys.implementation.getTime() & Long.MAX_VALUE;
    }
    
    public static void alert(String title, String message) {
        final boolean grabbed = Mouse.isGrabbed();
        if (grabbed) {
            Mouse.setGrabbed(false);
        }
        if (title == null) {
            title = "";
        }
        if (message == null) {
            message = "";
        }
        Sys.implementation.alert(title, message);
        if (grabbed) {
            Mouse.setGrabbed(true);
        }
    }
    
    public static boolean openURL(final String url) {
        try {
            final Class<?> serviceManagerClass = Class.forName("javax.jnlp.ServiceManager");
            final Method lookupMethod = AccessController.doPrivileged((PrivilegedExceptionAction<Method>)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return serviceManagerClass.getMethod("lookup", String.class);
                }
            });
            final Object basicService = lookupMethod.invoke(serviceManagerClass, "javax.jnlp.BasicService");
            final Class<?> basicServiceClass = Class.forName("javax.jnlp.BasicService");
            final Method showDocumentMethod = AccessController.doPrivileged((PrivilegedExceptionAction<Method>)new PrivilegedExceptionAction<Method>() {
                public Method run() throws Exception {
                    return basicServiceClass.getMethod("showDocument", URL.class);
                }
            });
            try {
                final Boolean ret = (Boolean)showDocumentMethod.invoke(basicService, new URL(url));
                return ret;
            }
            catch (MalformedURLException e) {
                e.printStackTrace(System.err);
                return false;
            }
        }
        catch (Exception ue) {
            return Sys.implementation.openURL(url);
        }
    }
    
    public static String getClipboard() {
        return Sys.implementation.getClipboard();
    }
    
    static {
        implementation = createImplementation();
        loadLibrary("lwjgl");
        is64Bit = (Sys.implementation.getPointerSize() == 8);
        final int native_jni_version = Sys.implementation.getJNIVersion();
        final int required_version = Sys.implementation.getRequiredJNIVersion();
        if (native_jni_version != required_version) {
            throw new LinkageError("Version mismatch: jar version is '" + required_version + "', native library version is '" + native_jni_version + "'");
        }
        Sys.implementation.setDebug(LWJGLUtil.DEBUG);
    }
}
