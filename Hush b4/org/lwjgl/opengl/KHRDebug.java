// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.PointerWrapper;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class KHRDebug
{
    public static final int GL_DEBUG_OUTPUT = 37600;
    public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 33346;
    public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 2;
    public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 37187;
    public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 37188;
    public static final int GL_DEBUG_LOGGED_MESSAGES = 37189;
    public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 33347;
    public static final int GL_MAX_DEBUG_GROUP_STACK_DEPTH = 33388;
    public static final int GL_DEBUG_GROUP_STACK_DEPTH = 33389;
    public static final int GL_MAX_LABEL_LENGTH = 33512;
    public static final int GL_DEBUG_CALLBACK_FUNCTION = 33348;
    public static final int GL_DEBUG_CALLBACK_USER_PARAM = 33349;
    public static final int GL_DEBUG_SOURCE_API = 33350;
    public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 33351;
    public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 33352;
    public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 33353;
    public static final int GL_DEBUG_SOURCE_APPLICATION = 33354;
    public static final int GL_DEBUG_SOURCE_OTHER = 33355;
    public static final int GL_DEBUG_TYPE_ERROR = 33356;
    public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 33357;
    public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 33358;
    public static final int GL_DEBUG_TYPE_PORTABILITY = 33359;
    public static final int GL_DEBUG_TYPE_PERFORMANCE = 33360;
    public static final int GL_DEBUG_TYPE_OTHER = 33361;
    public static final int GL_DEBUG_TYPE_MARKER = 33384;
    public static final int GL_DEBUG_TYPE_PUSH_GROUP = 33385;
    public static final int GL_DEBUG_TYPE_POP_GROUP = 33386;
    public static final int GL_DEBUG_SEVERITY_HIGH = 37190;
    public static final int GL_DEBUG_SEVERITY_MEDIUM = 37191;
    public static final int GL_DEBUG_SEVERITY_LOW = 37192;
    public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 33387;
    public static final int GL_BUFFER = 33504;
    public static final int GL_SHADER = 33505;
    public static final int GL_PROGRAM = 33506;
    public static final int GL_QUERY = 33507;
    public static final int GL_PROGRAM_PIPELINE = 33508;
    public static final int GL_SAMPLER = 33510;
    public static final int GL_DISPLAY_LIST = 33511;
    
    private KHRDebug() {
    }
    
    public static void glDebugMessageControl(final int source, final int type, final int severity, final IntBuffer ids, final boolean enabled) {
        GL43.glDebugMessageControl(source, type, severity, ids, enabled);
    }
    
    public static void glDebugMessageInsert(final int source, final int type, final int id, final int severity, final ByteBuffer buf) {
        GL43.glDebugMessageInsert(source, type, id, severity, buf);
    }
    
    public static void glDebugMessageInsert(final int source, final int type, final int id, final int severity, final CharSequence buf) {
        GL43.glDebugMessageInsert(source, type, id, severity, buf);
    }
    
    public static void glDebugMessageCallback(final KHRDebugCallback callback) {
        GL43.glDebugMessageCallback(callback);
    }
    
    public static int glGetDebugMessageLog(final int count, final IntBuffer sources, final IntBuffer types, final IntBuffer ids, final IntBuffer severities, final IntBuffer lengths, final ByteBuffer messageLog) {
        return GL43.glGetDebugMessageLog(count, sources, types, ids, severities, lengths, messageLog);
    }
    
    public static void glPushDebugGroup(final int source, final int id, final ByteBuffer message) {
        GL43.glPushDebugGroup(source, id, message);
    }
    
    public static void glPushDebugGroup(final int source, final int id, final CharSequence message) {
        GL43.glPushDebugGroup(source, id, message);
    }
    
    public static void glPopDebugGroup() {
        GL43.glPopDebugGroup();
    }
    
    public static void glObjectLabel(final int identifier, final int name, final ByteBuffer label) {
        GL43.glObjectLabel(identifier, name, label);
    }
    
    public static void glObjectLabel(final int identifier, final int name, final CharSequence label) {
        GL43.glObjectLabel(identifier, name, label);
    }
    
    public static void glGetObjectLabel(final int identifier, final int name, final IntBuffer length, final ByteBuffer label) {
        GL43.glGetObjectLabel(identifier, name, length, label);
    }
    
    public static String glGetObjectLabel(final int identifier, final int name, final int bufSize) {
        return GL43.glGetObjectLabel(identifier, name, bufSize);
    }
    
    public static void glObjectPtrLabel(final PointerWrapper ptr, final ByteBuffer label) {
        GL43.glObjectPtrLabel(ptr, label);
    }
    
    public static void glObjectPtrLabel(final PointerWrapper ptr, final CharSequence label) {
        GL43.glObjectPtrLabel(ptr, label);
    }
    
    public static void glGetObjectPtrLabel(final PointerWrapper ptr, final IntBuffer length, final ByteBuffer label) {
        GL43.glGetObjectPtrLabel(ptr, length, label);
    }
    
    public static String glGetObjectPtrLabel(final PointerWrapper ptr, final int bufSize) {
        return GL43.glGetObjectPtrLabel(ptr, bufSize);
    }
}
