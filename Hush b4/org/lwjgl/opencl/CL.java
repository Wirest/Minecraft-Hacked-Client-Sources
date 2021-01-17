// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.Sys;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.LWJGLException;

public final class CL
{
    private static boolean created;
    
    private CL() {
    }
    
    private static native void nCreate(final String p0) throws LWJGLException;
    
    private static native void nCreateDefault() throws LWJGLException;
    
    private static native void nDestroy();
    
    public static boolean isCreated() {
        return CL.created;
    }
    
    public static void create() throws LWJGLException {
        if (CL.created) {
            return;
        }
        String libname = null;
        String[] library_names = null;
        switch (LWJGLUtil.getPlatform()) {
            case 3: {
                libname = "OpenCL";
                library_names = new String[] { "OpenCL.dll" };
                break;
            }
            case 1: {
                libname = "OpenCL";
                library_names = new String[] { "libOpenCL64.so", "libOpenCL.so" };
                break;
            }
            case 2: {
                libname = "OpenCL";
                library_names = new String[] { "OpenCL.dylib" };
                break;
            }
            default: {
                throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
            }
        }
        final String[] oclPaths = LWJGLUtil.getLibraryPaths(libname, library_names, CL.class.getClassLoader());
        LWJGLUtil.log("Found " + oclPaths.length + " OpenCL paths");
        final String[] arr$ = oclPaths;
        final int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            final String oclPath = arr$[i$];
            try {
                nCreate(oclPath);
                CL.created = true;
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to load " + oclPath + ": " + e.getMessage());
                ++i$;
                continue;
            }
            break;
        }
        if (!CL.created && LWJGLUtil.getPlatform() == 2) {
            nCreateDefault();
            CL.created = true;
        }
        if (!CL.created) {
            throw new LWJGLException("Could not locate OpenCL library.");
        }
        if (!CLCapabilities.OpenCL10) {
            throw new RuntimeException("OpenCL 1.0 not supported.");
        }
    }
    
    public static void destroy() {
    }
    
    static long getFunctionAddress(final String[] aliases) {
        for (final String aliase : aliases) {
            final long address = getFunctionAddress(aliase);
            if (address != 0L) {
                return address;
            }
        }
        return 0L;
    }
    
    static long getFunctionAddress(final String name) {
        final ByteBuffer buffer = MemoryUtil.encodeASCII(name);
        return ngetFunctionAddress(MemoryUtil.getAddress(buffer));
    }
    
    private static native long ngetFunctionAddress(final long p0);
    
    static native ByteBuffer getHostBuffer(final long p0, final int p1);
    
    private static native void resetNativeStubs(final Class p0);
    
    static {
        Sys.initialize();
    }
}
