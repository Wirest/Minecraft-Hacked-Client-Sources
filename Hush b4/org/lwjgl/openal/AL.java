// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

import java.nio.IntBuffer;
import org.lwjgl.Sys;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.LWJGLException;

public final class AL
{
    static ALCdevice device;
    static ALCcontext context;
    private static boolean created;
    
    private AL() {
    }
    
    private static native void nCreate(final String p0) throws LWJGLException;
    
    private static native void nCreateDefault() throws LWJGLException;
    
    private static native void nDestroy();
    
    public static boolean isCreated() {
        return AL.created;
    }
    
    public static void create(final String deviceArguments, final int contextFrequency, final int contextRefresh, final boolean contextSynchronized) throws LWJGLException {
        create(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, true);
    }
    
    public static void create(final String deviceArguments, final int contextFrequency, final int contextRefresh, final boolean contextSynchronized, final boolean openDevice) throws LWJGLException {
        if (AL.created) {
            throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
        }
        String libname = null;
        String[] library_names = null;
        switch (LWJGLUtil.getPlatform()) {
            case 3: {
                if (Sys.is64Bit()) {
                    libname = "OpenAL64";
                    library_names = new String[] { "OpenAL64.dll" };
                    break;
                }
                libname = "OpenAL32";
                library_names = new String[] { "OpenAL32.dll" };
                break;
            }
            case 1: {
                libname = "openal";
                library_names = new String[] { "libopenal64.so", "libopenal.so", "libopenal.so.0" };
                break;
            }
            case 2: {
                libname = "openal";
                library_names = new String[] { "openal.dylib" };
                break;
            }
            default: {
                throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
            }
        }
        final String[] oalPaths = LWJGLUtil.getLibraryPaths(libname, library_names, AL.class.getClassLoader());
        LWJGLUtil.log("Found " + oalPaths.length + " OpenAL paths");
        final String[] arr$ = oalPaths;
        final int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            final String oalPath = arr$[i$];
            try {
                nCreate(oalPath);
                AL.created = true;
                init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to load " + oalPath + ": " + e.getMessage());
                ++i$;
                continue;
            }
            break;
        }
        if (!AL.created && LWJGLUtil.getPlatform() == 2) {
            nCreateDefault();
            AL.created = true;
            init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
        }
        if (!AL.created) {
            throw new LWJGLException("Could not locate OpenAL library.");
        }
    }
    
    private static void init(final String deviceArguments, final int contextFrequency, final int contextRefresh, final boolean contextSynchronized, final boolean openDevice) throws LWJGLException {
        try {
            AL10.initNativeStubs();
            ALC10.initNativeStubs();
            if (openDevice) {
                AL.device = ALC10.alcOpenDevice(deviceArguments);
                if (AL.device == null) {
                    throw new LWJGLException("Could not open ALC device");
                }
                if (contextFrequency == -1) {
                    AL.context = ALC10.alcCreateContext(AL.device, null);
                }
                else {
                    AL.context = ALC10.alcCreateContext(AL.device, ALCcontext.createAttributeList(contextFrequency, contextRefresh, (int)(contextSynchronized ? 1 : 0)));
                }
                ALC10.alcMakeContextCurrent(AL.context);
            }
        }
        catch (LWJGLException e) {
            destroy();
            throw e;
        }
        ALC11.initialize();
        if (ALC10.alcIsExtensionPresent(AL.device, "ALC_EXT_EFX")) {
            EFX10.initNativeStubs();
        }
    }
    
    public static void create() throws LWJGLException {
        create(null, 44100, 60, false);
    }
    
    public static void destroy() {
        if (AL.context != null) {
            ALC10.alcMakeContextCurrent(null);
            ALC10.alcDestroyContext(AL.context);
            AL.context = null;
        }
        if (AL.device != null) {
            final boolean result = ALC10.alcCloseDevice(AL.device);
            AL.device = null;
        }
        resetNativeStubs(AL10.class);
        resetNativeStubs(AL11.class);
        resetNativeStubs(ALC10.class);
        resetNativeStubs(ALC11.class);
        resetNativeStubs(EFX10.class);
        if (AL.created) {
            nDestroy();
        }
        AL.created = false;
    }
    
    private static native void resetNativeStubs(final Class p0);
    
    public static ALCcontext getContext() {
        return AL.context;
    }
    
    public static ALCdevice getDevice() {
        return AL.device;
    }
    
    static {
        Sys.initialize();
    }
}
