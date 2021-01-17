// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.lang.reflect.Field;
import org.lwjgl.LWJGLUtil;
import java.util.Map;

public final class Util
{
    private static final Map<Integer, String> CL_ERROR_TOKENS;
    
    private Util() {
    }
    
    public static void checkCLError(final int errcode) {
        if (errcode != 0) {
            throwCLError(errcode);
        }
    }
    
    private static void throwCLError(final int errcode) {
        String errname = Util.CL_ERROR_TOKENS.get(errcode);
        if (errname == null) {
            errname = "UNKNOWN";
        }
        throw new OpenCLException("Error Code: " + errname + " (" + LWJGLUtil.toHexString(errcode) + ")");
    }
    
    static {
        CL_ERROR_TOKENS = LWJGLUtil.getClassTokens(new LWJGLUtil.TokenFilter() {
            public boolean accept(final Field field, final int value) {
                return value < 0;
            }
        }, null, CL10.class, CL11.class, KHRGLSharing.class, KHRICD.class, APPLEGLSharing.class, EXTDeviceFission.class);
    }
}
