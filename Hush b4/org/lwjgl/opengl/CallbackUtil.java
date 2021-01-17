// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.util.HashMap;
import java.util.Map;

final class CallbackUtil
{
    private static final Map<ContextCapabilities, Long> contextUserParamsARB;
    private static final Map<ContextCapabilities, Long> contextUserParamsAMD;
    private static final Map<ContextCapabilities, Long> contextUserParamsKHR;
    
    private CallbackUtil() {
    }
    
    static long createGlobalRef(final Object obj) {
        return (obj == null) ? 0L : ncreateGlobalRef(obj);
    }
    
    private static native long ncreateGlobalRef(final Object p0);
    
    private static native void deleteGlobalRef(final long p0);
    
    private static void registerContextCallback(final long userParam, final Map<ContextCapabilities, Long> contextUserData) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        if (caps == null) {
            deleteGlobalRef(userParam);
            throw new IllegalStateException("No context is current.");
        }
        final Long userParam_old = contextUserData.remove(caps);
        if (userParam_old != null) {
            deleteGlobalRef(userParam_old);
        }
        if (userParam != 0L) {
            contextUserData.put(caps, userParam);
        }
    }
    
    static void unregisterCallbacks(final Object context) {
        final ContextCapabilities caps = GLContext.getCapabilities(context);
        Long userParam = CallbackUtil.contextUserParamsARB.remove(caps);
        if (userParam != null) {
            deleteGlobalRef(userParam);
        }
        userParam = CallbackUtil.contextUserParamsAMD.remove(caps);
        if (userParam != null) {
            deleteGlobalRef(userParam);
        }
        userParam = CallbackUtil.contextUserParamsKHR.remove(caps);
        if (userParam != null) {
            deleteGlobalRef(userParam);
        }
    }
    
    static native long getDebugOutputCallbackARB();
    
    static void registerContextCallbackARB(final long userParam) {
        registerContextCallback(userParam, CallbackUtil.contextUserParamsARB);
    }
    
    static native long getDebugOutputCallbackAMD();
    
    static void registerContextCallbackAMD(final long userParam) {
        registerContextCallback(userParam, CallbackUtil.contextUserParamsAMD);
    }
    
    static native long getDebugCallbackKHR();
    
    static void registerContextCallbackKHR(final long userParam) {
        registerContextCallback(userParam, CallbackUtil.contextUserParamsKHR);
    }
    
    static {
        contextUserParamsARB = new HashMap<ContextCapabilities, Long>();
        contextUserParamsAMD = new HashMap<ContextCapabilities, Long>();
        contextUserParamsKHR = new HashMap<ContextCapabilities, Long>();
    }
}
