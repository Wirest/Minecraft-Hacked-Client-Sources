package org.lwjgl.openal;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;

public final class AL {
    static ALCdevice device;
    static ALCcontext context;
    private static boolean created;

    static {
    }

    private static native void nCreate(String paramString)
            throws LWJGLException;

    private static native void nCreateDefault()
            throws LWJGLException;

    private static native void nDestroy();

    public static boolean isCreated() {
        return created;
    }

    public static void create(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
            throws LWJGLException {
        create(paramString, paramInt1, paramInt2, paramBoolean, true);
    }

    public static void create(String paramString, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
            throws LWJGLException {
        if (created) {
            throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
        }
        String str1;
        String[] arrayOfString1;
        switch (LWJGLUtil.getPlatform()) {
            case 3:
                if (Sys.is64Bit()) {
                    str1 = "OpenAL64";
                    arrayOfString1 = new String[]{"OpenAL64.dll"};
                } else {
                    str1 = "OpenAL32";
                    arrayOfString1 = new String[]{"OpenAL32.dll"};
                }
                break;
            case 1:
                str1 = "openal";
                arrayOfString1 = new String[]{"libopenal64.so", "libopenal.so", "libopenal.so.0"};
                break;
            case 2:
                str1 = "openal";
                arrayOfString1 = new String[]{"openal.dylib"};
                break;
            default:
                throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
        }
        String[] arrayOfString2 = LWJGLUtil.getLibraryPaths(str1, arrayOfString1, AL.class.getClassLoader());
        LWJGLUtil.log("Found " + arrayOfString2.length + " OpenAL paths");
        String[] arrayOfString3 = arrayOfString2;
        int i = arrayOfString3.length;
        int j = 0;
        while (j < i) {
            String str2 = arrayOfString3[j];
            try {
                nCreate(str2);
                created = true;
                init(paramString, paramInt1, paramInt2, paramBoolean1, paramBoolean2);
            } catch (LWJGLException localLWJGLException) {
                LWJGLUtil.log("Failed to load " + str2 + ": " + localLWJGLException.getMessage());
                j++;
            }
        }
        if ((!created) && (LWJGLUtil.getPlatform() == 2)) {
            nCreateDefault();
            created = true;
            init(paramString, paramInt1, paramInt2, paramBoolean1, paramBoolean2);
        }
        if (!created) {
            throw new LWJGLException("Could not locate OpenAL library.");
        }
    }

    private static void init(String paramString, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
            throws LWJGLException {
        try {
            AL10.initNativeStubs();
            ALC10.initNativeStubs();
            if (paramBoolean2) {
                device = ALC10.alcOpenDevice(paramString);
                if (device == null) {
                    throw new LWJGLException("Could not open ALC device");
                }
                if (paramInt1 == -1) {
                    context = ALC10.alcCreateContext(device, null);
                } else {
                    context = ALC10.alcCreateContext(device, ALCcontext.createAttributeList(paramInt1, paramInt2, paramBoolean1 ? 1 : 0));
                }
                ALC10.alcMakeContextCurrent(context);
            }
        } catch (LWJGLException localLWJGLException) {
            destroy();
            throw localLWJGLException;
        }
        ALC11.initialize();
        if (ALC10.alcIsExtensionPresent(device, "ALC_EXT_EFX")) {
            EFX10.initNativeStubs();
        }
    }

    public static void create()
            throws LWJGLException {
        create(null, 44100, 60, false);
    }

    public static void destroy() {
        if (context != null) {
            ALC10.alcMakeContextCurrent(null);
            ALC10.alcDestroyContext(context);
            context = null;
        }
        if (device != null) {
            boolean bool = ALC10.alcCloseDevice(device);
            device = null;
        }
        resetNativeStubs(AL10.class);
        resetNativeStubs(AL11.class);
        resetNativeStubs(ALC10.class);
        resetNativeStubs(ALC11.class);
        resetNativeStubs(EFX10.class);
        if (created) {
            nDestroy();
        }
        created = false;
    }

    private static native void resetNativeStubs(Class paramClass);

    public static ALCcontext getContext() {
        return context;
    }

    public static ALCdevice getDevice() {
        return device;
    }
}




