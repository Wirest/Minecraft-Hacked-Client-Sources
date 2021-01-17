// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.Sys;
import java.util.WeakHashMap;
import org.lwjgl.LWJGLException;
import java.lang.reflect.Method;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLUtil;
import java.util.StringTokenizer;
import java.util.Set;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

public final class GLContext
{
    private static final ThreadLocal<ContextCapabilities> current_capabilities;
    private static CapabilitiesCacheEntry fast_path_cache;
    private static final ThreadLocal<CapabilitiesCacheEntry> thread_cache_entries;
    private static final Map<Object, ContextCapabilities> capability_cache;
    private static int gl_ref_count;
    private static boolean did_auto_load;
    
    public static ContextCapabilities getCapabilities() {
        final ContextCapabilities caps = getCapabilitiesImpl();
        if (caps == null) {
            throw new RuntimeException("No OpenGL context found in the current thread.");
        }
        return caps;
    }
    
    private static ContextCapabilities getCapabilitiesImpl() {
        final CapabilitiesCacheEntry recent_cache_entry = GLContext.fast_path_cache;
        if (recent_cache_entry.owner == Thread.currentThread()) {
            return recent_cache_entry.capabilities;
        }
        return getThreadLocalCapabilities();
    }
    
    static ContextCapabilities getCapabilities(final Object context) {
        return GLContext.capability_cache.get(context);
    }
    
    private static ContextCapabilities getThreadLocalCapabilities() {
        return GLContext.current_capabilities.get();
    }
    
    static void setCapabilities(final ContextCapabilities capabilities) {
        GLContext.current_capabilities.set(capabilities);
        CapabilitiesCacheEntry thread_cache_entry = GLContext.thread_cache_entries.get();
        if (thread_cache_entry == null) {
            thread_cache_entry = new CapabilitiesCacheEntry();
            GLContext.thread_cache_entries.set(thread_cache_entry);
        }
        thread_cache_entry.owner = Thread.currentThread();
        thread_cache_entry.capabilities = capabilities;
        GLContext.fast_path_cache = thread_cache_entry;
    }
    
    static long getPlatformSpecificFunctionAddress(final String function_prefix, final String[] os_prefixes, final String[] os_function_prefixes, final String function) {
        final String os_name = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty("os.name");
            }
        });
        for (int i = 0; i < os_prefixes.length; ++i) {
            if (os_name.startsWith(os_prefixes[i])) {
                final String platform_function_name = function.replaceFirst(function_prefix, os_function_prefixes[i]);
                final long address = getFunctionAddress(platform_function_name);
                return address;
            }
        }
        return 0L;
    }
    
    static long getFunctionAddress(final String[] aliases) {
        for (final String alias : aliases) {
            final long address = getFunctionAddress(alias);
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
    
    static int getSupportedExtensions(final Set<String> supported_extensions) {
        final String version = GL11.glGetString(7938);
        if (version == null) {
            throw new IllegalStateException("glGetString(GL_VERSION) returned null - possibly caused by missing current context.");
        }
        final StringTokenizer version_tokenizer = new StringTokenizer(version, ". ");
        final String major_string = version_tokenizer.nextToken();
        final String minor_string = version_tokenizer.nextToken();
        int majorVersion = 0;
        int minorVersion = 0;
        try {
            majorVersion = Integer.parseInt(major_string);
            minorVersion = Integer.parseInt(minor_string);
        }
        catch (NumberFormatException e) {
            LWJGLUtil.log("The major and/or minor OpenGL version is malformed: " + e.getMessage());
        }
        final int[][] GL_VERSIONS = { { 1, 2, 3, 4, 5 }, { 0, 1 }, { 0, 1, 2, 3 }, { 0, 1, 2, 3, 4, 5 } };
        for (int major = 1; major <= GL_VERSIONS.length; ++major) {
            final int[] arr$;
            final int[] minors = arr$ = GL_VERSIONS[major - 1];
            for (final int minor : arr$) {
                if (major < majorVersion || (major == majorVersion && minor <= minorVersion)) {
                    supported_extensions.add("OpenGL" + Integer.toString(major) + Integer.toString(minor));
                }
            }
        }
        int profileMask = 0;
        if (majorVersion < 3) {
            final String extensions_string = GL11.glGetString(7939);
            if (extensions_string == null) {
                throw new IllegalStateException("glGetString(GL_EXTENSIONS) returned null - is there a context current?");
            }
            final StringTokenizer tokenizer = new StringTokenizer(extensions_string);
            while (tokenizer.hasMoreTokens()) {
                supported_extensions.add(tokenizer.nextToken());
            }
        }
        else {
            for (int extensionCount = GL11.glGetInteger(33309), i = 0; i < extensionCount; ++i) {
                supported_extensions.add(GL30.glGetStringi(7939, i));
            }
            if (3 < majorVersion || 2 <= minorVersion) {
                Util.checkGLError();
                try {
                    profileMask = GL11.glGetInteger(37158);
                    Util.checkGLError();
                }
                catch (OpenGLException e2) {
                    LWJGLUtil.log("Failed to retrieve CONTEXT_PROFILE_MASK");
                }
            }
        }
        return profileMask;
    }
    
    static void initNativeStubs(final Class<?> extension_class, final Set supported_extensions, final String ext_name) {
        resetNativeStubs(extension_class);
        if (supported_extensions.contains(ext_name)) {
            try {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>)new PrivilegedExceptionAction<Object>() {
                    public Object run() throws Exception {
                        final Method init_stubs_method = extension_class.getDeclaredMethod("initNativeStubs", (Class[])new Class[0]);
                        init_stubs_method.invoke(null, new Object[0]);
                        return null;
                    }
                });
            }
            catch (Exception e) {
                LWJGLUtil.log("Failed to initialize extension " + extension_class + " - exception: " + e);
                supported_extensions.remove(ext_name);
            }
        }
    }
    
    public static synchronized void useContext(final Object context) throws LWJGLException {
        useContext(context, false);
    }
    
    public static synchronized void useContext(final Object context, final boolean forwardCompatible) throws LWJGLException {
        if (context == null) {
            ContextCapabilities.unloadAllStubs();
            setCapabilities(null);
            if (GLContext.did_auto_load) {
                unloadOpenGLLibrary();
            }
            return;
        }
        if (GLContext.gl_ref_count == 0) {
            loadOpenGLLibrary();
            GLContext.did_auto_load = true;
        }
        try {
            final ContextCapabilities capabilities = GLContext.capability_cache.get(context);
            if (capabilities == null) {
                new ContextCapabilities(forwardCompatible);
                GLContext.capability_cache.put(context, getCapabilities());
            }
            else {
                setCapabilities(capabilities);
            }
        }
        catch (LWJGLException e) {
            if (GLContext.did_auto_load) {
                unloadOpenGLLibrary();
            }
            throw e;
        }
    }
    
    public static synchronized void loadOpenGLLibrary() throws LWJGLException {
        if (GLContext.gl_ref_count == 0) {
            nLoadOpenGLLibrary();
        }
        ++GLContext.gl_ref_count;
    }
    
    private static native void nLoadOpenGLLibrary() throws LWJGLException;
    
    public static synchronized void unloadOpenGLLibrary() {
        --GLContext.gl_ref_count;
        if (GLContext.gl_ref_count == 0 && LWJGLUtil.getPlatform() != 1) {
            nUnloadOpenGLLibrary();
        }
    }
    
    private static native void nUnloadOpenGLLibrary();
    
    static native void resetNativeStubs(final Class p0);
    
    static {
        current_capabilities = new ThreadLocal<ContextCapabilities>();
        GLContext.fast_path_cache = new CapabilitiesCacheEntry();
        thread_cache_entries = new ThreadLocal<CapabilitiesCacheEntry>();
        capability_cache = new WeakHashMap<Object, ContextCapabilities>();
        Sys.initialize();
    }
    
    private static final class CapabilitiesCacheEntry
    {
        Thread owner;
        ContextCapabilities capabilities;
    }
}
