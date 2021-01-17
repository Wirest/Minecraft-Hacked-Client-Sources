// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerWrapperAbstract;

public final class ARBDebugOutputCallback extends PointerWrapperAbstract
{
    private static final int GL_DEBUG_SEVERITY_HIGH_ARB = 37190;
    private static final int GL_DEBUG_SEVERITY_MEDIUM_ARB = 37191;
    private static final int GL_DEBUG_SEVERITY_LOW_ARB = 37192;
    private static final int GL_DEBUG_SOURCE_API_ARB = 33350;
    private static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB = 33351;
    private static final int GL_DEBUG_SOURCE_SHADER_COMPILER_ARB = 33352;
    private static final int GL_DEBUG_SOURCE_THIRD_PARTY_ARB = 33353;
    private static final int GL_DEBUG_SOURCE_APPLICATION_ARB = 33354;
    private static final int GL_DEBUG_SOURCE_OTHER_ARB = 33355;
    private static final int GL_DEBUG_TYPE_ERROR_ARB = 33356;
    private static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB = 33357;
    private static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB = 33358;
    private static final int GL_DEBUG_TYPE_PORTABILITY_ARB = 33359;
    private static final int GL_DEBUG_TYPE_PERFORMANCE_ARB = 33360;
    private static final int GL_DEBUG_TYPE_OTHER_ARB = 33361;
    private static final long CALLBACK_POINTER;
    private final Handler handler;
    
    public ARBDebugOutputCallback() {
        this(new Handler() {
            public void handleMessage(final int source, final int type, final int id, final int severity, final String message) {
                System.err.println("[LWJGL] ARB_debug_output message");
                System.err.println("\tID: " + id);
                String description = null;
                switch (source) {
                    case 33350: {
                        description = "API";
                        break;
                    }
                    case 33351: {
                        description = "WINDOW SYSTEM";
                        break;
                    }
                    case 33352: {
                        description = "SHADER COMPILER";
                        break;
                    }
                    case 33353: {
                        description = "THIRD PARTY";
                        break;
                    }
                    case 33354: {
                        description = "APPLICATION";
                        break;
                    }
                    case 33355: {
                        description = "OTHER";
                        break;
                    }
                    default: {
                        description = this.printUnknownToken(source);
                        break;
                    }
                }
                System.err.println("\tSource: " + description);
                switch (type) {
                    case 33356: {
                        description = "ERROR";
                        break;
                    }
                    case 33357: {
                        description = "DEPRECATED BEHAVIOR";
                        break;
                    }
                    case 33358: {
                        description = "UNDEFINED BEHAVIOR";
                        break;
                    }
                    case 33359: {
                        description = "PORTABILITY";
                        break;
                    }
                    case 33360: {
                        description = "PERFORMANCE";
                        break;
                    }
                    case 33361: {
                        description = "OTHER";
                        break;
                    }
                    default: {
                        description = this.printUnknownToken(type);
                        break;
                    }
                }
                System.err.println("\tType: " + description);
                switch (severity) {
                    case 37190: {
                        description = "HIGH";
                        break;
                    }
                    case 37191: {
                        description = "MEDIUM";
                        break;
                    }
                    case 37192: {
                        description = "LOW";
                        break;
                    }
                    default: {
                        description = this.printUnknownToken(severity);
                        break;
                    }
                }
                System.err.println("\tSeverity: " + description);
                System.err.println("\tMessage: " + message);
            }
            
            private String printUnknownToken(final int token) {
                return "Unknown (0x" + Integer.toHexString(token).toUpperCase() + ")";
            }
        });
    }
    
    public ARBDebugOutputCallback(final Handler handler) {
        super(ARBDebugOutputCallback.CALLBACK_POINTER);
        this.handler = handler;
    }
    
    Handler getHandler() {
        return this.handler;
    }
    
    static {
        long pointer = 0L;
        try {
            pointer = (long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugOutputCallbackARB", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (Exception ex) {}
        CALLBACK_POINTER = pointer;
    }
    
    public interface Handler
    {
        void handleMessage(final int p0, final int p1, final int p2, final int p3, final String p4);
    }
}
