// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.awt.GraphicsEnvironment;
import java.util.Iterator;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Array;
import java.io.FilenameFilter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Method;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.StringTokenizer;
import java.lang.reflect.Field;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.util.Map;

public final class Native
{
    private static final String VERSION = "3.4.0";
    private static final String VERSION_NATIVE = "3.4.0";
    private static String nativeLibraryPath;
    private static Map typeMappers;
    private static Map alignments;
    private static Map options;
    private static Map libraries;
    private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER;
    private static Callback.UncaughtExceptionHandler callbackExceptionHandler;
    public static final int POINTER_SIZE;
    public static final int LONG_SIZE;
    public static final int WCHAR_SIZE;
    public static final int SIZE_T_SIZE;
    private static final int TYPE_VOIDP = 0;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_WCHAR_T = 2;
    private static final int TYPE_SIZE_T = 3;
    private static final int THREAD_NOCHANGE = 0;
    private static final int THREAD_DETACH = -1;
    private static final int THREAD_LEAVE_ATTACHED = -2;
    private static final Object finalizer;
    private static final ThreadLocal lastError;
    private static Map registeredClasses;
    private static Map registeredLibraries;
    private static Object unloader;
    static final int CB_HAS_INITIALIZER = 1;
    private static final int CVT_UNSUPPORTED = -1;
    private static final int CVT_DEFAULT = 0;
    private static final int CVT_POINTER = 1;
    private static final int CVT_STRING = 2;
    private static final int CVT_STRUCTURE = 3;
    private static final int CVT_STRUCTURE_BYVAL = 4;
    private static final int CVT_BUFFER = 5;
    private static final int CVT_ARRAY_BYTE = 6;
    private static final int CVT_ARRAY_SHORT = 7;
    private static final int CVT_ARRAY_CHAR = 8;
    private static final int CVT_ARRAY_INT = 9;
    private static final int CVT_ARRAY_LONG = 10;
    private static final int CVT_ARRAY_FLOAT = 11;
    private static final int CVT_ARRAY_DOUBLE = 12;
    private static final int CVT_ARRAY_BOOLEAN = 13;
    private static final int CVT_BOOLEAN = 14;
    private static final int CVT_CALLBACK = 15;
    private static final int CVT_FLOAT = 16;
    private static final int CVT_NATIVE_MAPPED = 17;
    private static final int CVT_WSTRING = 18;
    private static final int CVT_INTEGER_TYPE = 19;
    private static final int CVT_POINTER_TYPE = 20;
    private static final int CVT_TYPE_MAPPER = 21;
    
    private static void dispose() {
        NativeLibrary.disposeAll();
        Native.nativeLibraryPath = null;
    }
    
    private static boolean deleteNativeLibrary(final String path) {
        final File flib = new File(path);
        if (flib.delete()) {
            return true;
        }
        markTemporaryFile(flib);
        return false;
    }
    
    private Native() {
    }
    
    private static native void initIDs();
    
    public static synchronized native void setProtected(final boolean p0);
    
    public static synchronized native boolean isProtected();
    
    public static synchronized native void setPreserveLastError(final boolean p0);
    
    public static synchronized native boolean getPreserveLastError();
    
    public static long getWindowID(final Window w) throws HeadlessException {
        return AWT.getWindowID(w);
    }
    
    public static long getComponentID(final Component c) throws HeadlessException {
        return AWT.getComponentID(c);
    }
    
    public static Pointer getWindowPointer(final Window w) throws HeadlessException {
        return new Pointer(AWT.getWindowID(w));
    }
    
    public static Pointer getComponentPointer(final Component c) throws HeadlessException {
        return new Pointer(AWT.getComponentID(c));
    }
    
    static native long getWindowHandle0(final Component p0);
    
    public static Pointer getDirectBufferPointer(final Buffer b) {
        final long peer = _getDirectBufferPointer(b);
        return (peer == 0L) ? null : new Pointer(peer);
    }
    
    private static native long _getDirectBufferPointer(final Buffer p0);
    
    public static String toString(final byte[] buf) {
        return toString(buf, System.getProperty("jna.encoding"));
    }
    
    public static String toString(final byte[] buf, final String encoding) {
        String s = null;
        if (encoding != null) {
            try {
                s = new String(buf, encoding);
            }
            catch (UnsupportedEncodingException ex) {}
        }
        if (s == null) {
            s = new String(buf);
        }
        final int term = s.indexOf(0);
        if (term != -1) {
            s = s.substring(0, term);
        }
        return s;
    }
    
    public static String toString(final char[] buf) {
        String s = new String(buf);
        final int term = s.indexOf(0);
        if (term != -1) {
            s = s.substring(0, term);
        }
        return s;
    }
    
    public static Object loadLibrary(final Class interfaceClass) {
        return loadLibrary(null, interfaceClass);
    }
    
    public static Object loadLibrary(final Class interfaceClass, final Map options) {
        return loadLibrary(null, interfaceClass, options);
    }
    
    public static Object loadLibrary(final String name, final Class interfaceClass) {
        return loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
    }
    
    public static Object loadLibrary(final String name, final Class interfaceClass, final Map options) {
        final Library.Handler handler = new Library.Handler(name, interfaceClass, options);
        final ClassLoader loader = interfaceClass.getClassLoader();
        final Library proxy = (Library)Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
        cacheOptions(interfaceClass, options, proxy);
        return proxy;
    }
    
    private static void loadLibraryInstance(final Class cls) {
        if (cls != null && !Native.libraries.containsKey(cls)) {
            try {
                final Field[] fields = cls.getFields();
                for (int i = 0; i < fields.length; ++i) {
                    final Field field = fields[i];
                    if (field.getType() == cls && Modifier.isStatic(field.getModifiers())) {
                        Native.libraries.put(cls, new WeakReference<Object>(field.get(null)));
                        break;
                    }
                }
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e + ")");
            }
        }
    }
    
    static Class findEnclosingLibraryClass(Class cls) {
        if (cls == null) {
            return null;
        }
        synchronized (Native.libraries) {
            if (Native.options.containsKey(cls)) {
                return cls;
            }
        }
        if (Library.class.isAssignableFrom(cls)) {
            return cls;
        }
        if (Callback.class.isAssignableFrom(cls)) {
            cls = CallbackReference.findCallbackClass(cls);
        }
        final Class declaring = cls.getDeclaringClass();
        final Class fromDeclaring = findEnclosingLibraryClass(declaring);
        if (fromDeclaring != null) {
            return fromDeclaring;
        }
        return findEnclosingLibraryClass(cls.getSuperclass());
    }
    
    public static Map getLibraryOptions(final Class type) {
        synchronized (Native.libraries) {
            Class interfaceClass = findEnclosingLibraryClass(type);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            }
            else {
                interfaceClass = type;
            }
            if (!Native.options.containsKey(interfaceClass)) {
                try {
                    final Field field = interfaceClass.getField("OPTIONS");
                    field.setAccessible(true);
                    Native.options.put(interfaceClass, field.get(null));
                }
                catch (NoSuchFieldException e2) {}
                catch (Exception e) {
                    throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e + "): " + interfaceClass);
                }
            }
            return Native.options.get(interfaceClass);
        }
    }
    
    public static TypeMapper getTypeMapper(final Class cls) {
        synchronized (Native.libraries) {
            Class interfaceClass = findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            }
            else {
                interfaceClass = cls;
            }
            if (!Native.typeMappers.containsKey(interfaceClass)) {
                try {
                    final Field field = interfaceClass.getField("TYPE_MAPPER");
                    field.setAccessible(true);
                    Native.typeMappers.put(interfaceClass, field.get(null));
                }
                catch (NoSuchFieldException e2) {
                    final Map options = getLibraryOptions(cls);
                    if (options != null && options.containsKey("type-mapper")) {
                        Native.typeMappers.put(interfaceClass, options.get("type-mapper"));
                    }
                }
                catch (Exception e) {
                    throw new IllegalArgumentException("TYPE_MAPPER must be a public field of type " + TypeMapper.class.getName() + " (" + e + "): " + interfaceClass);
                }
            }
            return Native.typeMappers.get(interfaceClass);
        }
    }
    
    public static int getStructureAlignment(final Class cls) {
        synchronized (Native.libraries) {
            Class interfaceClass = findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            }
            else {
                interfaceClass = cls;
            }
            if (!Native.alignments.containsKey(interfaceClass)) {
                try {
                    final Field field = interfaceClass.getField("STRUCTURE_ALIGNMENT");
                    field.setAccessible(true);
                    Native.alignments.put(interfaceClass, field.get(null));
                }
                catch (NoSuchFieldException e2) {
                    final Map options = getLibraryOptions(interfaceClass);
                    if (options != null && options.containsKey("structure-alignment")) {
                        Native.alignments.put(interfaceClass, options.get("structure-alignment"));
                    }
                }
                catch (Exception e) {
                    throw new IllegalArgumentException("STRUCTURE_ALIGNMENT must be a public field of type int (" + e + "): " + interfaceClass);
                }
            }
            final Integer value = Native.alignments.get(interfaceClass);
            return (value != null) ? value : 0;
        }
    }
    
    static byte[] getBytes(final String s) {
        try {
            return getBytes(s, System.getProperty("jna.encoding"));
        }
        catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }
    
    static byte[] getBytes(final String s, final String encoding) throws UnsupportedEncodingException {
        if (encoding != null) {
            return s.getBytes(encoding);
        }
        return s.getBytes();
    }
    
    public static byte[] toByteArray(final String s) {
        final byte[] bytes = getBytes(s);
        final byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }
    
    public static byte[] toByteArray(final String s, final String encoding) throws UnsupportedEncodingException {
        final byte[] bytes = getBytes(s, encoding);
        final byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }
    
    public static char[] toCharArray(final String s) {
        final char[] chars = s.toCharArray();
        final char[] buf = new char[chars.length + 1];
        System.arraycopy(chars, 0, buf, 0, chars.length);
        return buf;
    }
    
    static String getNativeLibraryResourcePath(final int osType, String arch, final String name) {
        arch = arch.toLowerCase();
        if ("powerpc".equals(arch)) {
            arch = "ppc";
        }
        else if ("powerpc64".equals(arch)) {
            arch = "ppc64";
        }
        String osPrefix = null;
        switch (osType) {
            case 2: {
                if ("i386".equals(arch)) {
                    arch = "x86";
                }
                osPrefix = "win32-" + arch;
                break;
            }
            case 6: {
                osPrefix = "w32ce-" + arch;
                break;
            }
            case 0: {
                osPrefix = "darwin";
                break;
            }
            case 1: {
                if ("x86".equals(arch)) {
                    arch = "i386";
                }
                else if ("x86_64".equals(arch)) {
                    arch = "amd64";
                }
                osPrefix = "linux-" + arch;
                break;
            }
            case 3: {
                osPrefix = "sunos-" + arch;
                break;
            }
            default: {
                osPrefix = name.toLowerCase();
                if ("x86".equals(arch)) {
                    arch = "i386";
                }
                if ("x86_64".equals(arch)) {
                    arch = "amd64";
                }
                final int space = osPrefix.indexOf(" ");
                if (space != -1) {
                    osPrefix = osPrefix.substring(0, space);
                }
                osPrefix = osPrefix + "-" + arch;
                break;
            }
        }
        return "/com/sun/jna/" + osPrefix;
    }
    
    private static void loadNativeLibrary() {
        removeTemporaryFiles();
        final String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
        final String bootPath = System.getProperty("jna.boot.library.path");
        if (bootPath != null) {
            final StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
            while (dirs.hasMoreTokens()) {
                final String dir = dirs.nextToken();
                final File file = new File(new File(dir), System.mapLibraryName(libName));
                String path = file.getAbsolutePath();
                if (file.exists()) {
                    try {
                        System.load(path);
                        Native.nativeLibraryPath = path;
                        return;
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
                }
                if (Platform.isMac()) {
                    String orig;
                    String ext;
                    if (path.endsWith("dylib")) {
                        orig = "dylib";
                        ext = "jnilib";
                    }
                    else {
                        orig = "jnilib";
                        ext = "dylib";
                    }
                    path = path.substring(0, path.lastIndexOf(orig)) + ext;
                    if (!new File(path).exists()) {
                        continue;
                    }
                    try {
                        System.load(path);
                        Native.nativeLibraryPath = path;
                        return;
                    }
                    catch (UnsatisfiedLinkError ex) {
                        System.err.println("File found at " + path + " but not loadable: " + ex.getMessage());
                    }
                }
            }
        }
        try {
            if (!Boolean.getBoolean("jna.nosys")) {
                System.loadLibrary(libName);
                return;
            }
        }
        catch (UnsatisfiedLinkError e) {
            if (Boolean.getBoolean("jna.nounpack")) {
                throw e;
            }
        }
        if (!Boolean.getBoolean("jna.nounpack")) {
            loadNativeLibraryFromJar();
            return;
        }
        throw new UnsatisfiedLinkError("Native jnidispatch library not found");
    }
    
    private static void loadNativeLibraryFromJar() {
        final String libname = System.mapLibraryName("jnidispatch");
        final String arch = System.getProperty("os.arch");
        final String name = System.getProperty("os.name");
        String resourceName = getNativeLibraryResourcePath(Platform.getOSType(), arch, name) + "/" + libname;
        URL url = Native.class.getResource(resourceName);
        boolean unpacked = false;
        if (url == null && Platform.isMac() && resourceName.endsWith(".dylib")) {
            resourceName = resourceName.substring(0, resourceName.lastIndexOf(".dylib")) + ".jnilib";
            url = Native.class.getResource(resourceName);
        }
        if (url == null) {
            throw new UnsatisfiedLinkError("jnidispatch (" + resourceName + ") not found in resource path");
        }
        File lib = null;
        if (url.getProtocol().toLowerCase().equals("file")) {
            try {
                lib = new File(new URI(url.toString()));
            }
            catch (URISyntaxException e2) {
                lib = new File(url.getPath());
            }
            if (!lib.exists()) {
                throw new Error("File URL " + url + " could not be properly decoded");
            }
        }
        else {
            final InputStream is = Native.class.getResourceAsStream(resourceName);
            if (is == null) {
                throw new Error("Can't obtain jnidispatch InputStream");
            }
            FileOutputStream fos = null;
            try {
                final File dir = getTempDir();
                lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
                lib.deleteOnExit();
                fos = new FileOutputStream(lib);
                final byte[] buf = new byte[1024];
                int count;
                while ((count = is.read(buf, 0, buf.length)) > 0) {
                    fos.write(buf, 0, count);
                }
                unpacked = true;
            }
            catch (IOException e) {
                throw new Error("Failed to create temporary file for jnidispatch library: " + e);
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException ex) {}
                if (fos != null) {
                    try {
                        fos.close();
                    }
                    catch (IOException ex2) {}
                }
            }
        }
        System.load(lib.getAbsolutePath());
        Native.nativeLibraryPath = lib.getAbsolutePath();
        if (unpacked) {
            deleteNativeLibrary(lib.getAbsolutePath());
        }
    }
    
    private static native int sizeof(final int p0);
    
    private static native String getNativeVersion();
    
    private static native String getAPIChecksum();
    
    public static int getLastError() {
        return Native.lastError.get();
    }
    
    public static native void setLastError(final int p0);
    
    static void updateLastError(final int e) {
        Native.lastError.set(new Integer(e));
    }
    
    public static Library synchronizedLibrary(final Library library) {
        final Class cls = library.getClass();
        if (!Proxy.isProxyClass(cls)) {
            throw new IllegalArgumentException("Library must be a proxy class");
        }
        final InvocationHandler ih = Proxy.getInvocationHandler(library);
        if (!(ih instanceof Library.Handler)) {
            throw new IllegalArgumentException("Unrecognized proxy handler: " + ih);
        }
        final Library.Handler handler = (Library.Handler)ih;
        final InvocationHandler newHandler = new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                synchronized (handler.getNativeLibrary()) {
                    return handler.invoke(library, method, args);
                }
            }
        };
        return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), newHandler);
    }
    
    public static String getWebStartLibraryPath(final String libName) {
        if (System.getProperty("javawebstart.version") == null) {
            return null;
        }
        try {
            final ClassLoader cl = Native.class.getClassLoader();
            final Method m = AccessController.doPrivileged((PrivilegedAction<Method>)new PrivilegedAction() {
                public Object run() {
                    try {
                        final Method m = ClassLoader.class.getDeclaredMethod("findLibrary", String.class);
                        m.setAccessible(true);
                        return m;
                    }
                    catch (Exception e) {
                        return null;
                    }
                }
            });
            final String libpath = (String)m.invoke(cl, libName);
            if (libpath != null) {
                return new File(libpath).getParent();
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    static void markTemporaryFile(final File file) {
        try {
            final File marker = new File(file.getParentFile(), file.getName() + ".x");
            marker.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static File getTempDir() {
        final File tmp = new File(System.getProperty("java.io.tmpdir"));
        final File jnatmp = new File(tmp, "jna");
        jnatmp.mkdirs();
        return jnatmp.exists() ? jnatmp : tmp;
    }
    
    static void removeTemporaryFiles() {
        final File dir = getTempDir();
        final FilenameFilter filter = new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".x") && name.indexOf("jna") != -1;
            }
        };
        final File[] files = dir.listFiles(filter);
        for (int i = 0; files != null && i < files.length; ++i) {
            final File marker = files[i];
            String name = marker.getName();
            name = name.substring(0, name.length() - 2);
            final File target = new File(marker.getParentFile(), name);
            if (!target.exists() || target.delete()) {
                marker.delete();
            }
        }
    }
    
    public static int getNativeSize(final Class type, Object value) {
        if (type.isArray()) {
            final int len = Array.getLength(value);
            if (len > 0) {
                final Object o = Array.get(value, 0);
                return len * getNativeSize(type.getComponentType(), o);
            }
            throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
        }
        else {
            if (Structure.class.isAssignableFrom(type) && !Structure.ByReference.class.isAssignableFrom(type)) {
                if (value == null) {
                    value = Structure.newInstance(type);
                }
                return ((Structure)value).size();
            }
            try {
                return getNativeSize(type);
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e.getMessage());
            }
        }
    }
    
    public static int getNativeSize(Class cls) {
        if (NativeMapped.class.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return 4;
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return 1;
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return 2;
        }
        if (cls == Character.TYPE || cls == Character.class) {
            return Native.WCHAR_SIZE;
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            return 4;
        }
        if (cls == Long.TYPE || cls == Long.class) {
            return 8;
        }
        if (cls == Float.TYPE || cls == Float.class) {
            return 4;
        }
        if (cls == Double.TYPE || cls == Double.class) {
            return 8;
        }
        if (Structure.class.isAssignableFrom(cls)) {
            if (Structure.ByValue.class.isAssignableFrom(cls)) {
                return Structure.newInstance(cls).size();
            }
            return Native.POINTER_SIZE;
        }
        else {
            if (Pointer.class.isAssignableFrom(cls) || (Platform.HAS_BUFFERS && Buffers.isBuffer(cls)) || Callback.class.isAssignableFrom(cls) || String.class == cls || WString.class == cls) {
                return Native.POINTER_SIZE;
            }
            throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
        }
    }
    
    public static boolean isSupportedNativeType(final Class cls) {
        if (Structure.class.isAssignableFrom(cls)) {
            return true;
        }
        try {
            return getNativeSize(cls) != 0;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public static void setCallbackExceptionHandler(final Callback.UncaughtExceptionHandler eh) {
        Native.callbackExceptionHandler = ((eh == null) ? Native.DEFAULT_HANDLER : eh);
    }
    
    public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
        return Native.callbackExceptionHandler;
    }
    
    public static void register(final String libName) {
        register(getNativeClass(getCallingClass()), NativeLibrary.getInstance(libName));
    }
    
    public static void register(final NativeLibrary lib) {
        register(getNativeClass(getCallingClass()), lib);
    }
    
    static Class getNativeClass(final Class cls) {
        final Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            if ((methods[i].getModifiers() & 0x100) != 0x0) {
                return cls;
            }
        }
        final int idx = cls.getName().lastIndexOf("$");
        if (idx != -1) {
            final String name = cls.getName().substring(0, idx);
            try {
                return getNativeClass(Class.forName(name, true, cls.getClassLoader()));
            }
            catch (ClassNotFoundException ex) {}
        }
        throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
    }
    
    static Class getCallingClass() {
        final Class[] context = new SecurityManager() {
            public Class[] getClassContext() {
                return super.getClassContext();
            }
        }.getClassContext();
        if (context.length < 4) {
            throw new IllegalStateException("This method must be called from the static initializer of a class");
        }
        return context[3];
    }
    
    public static void setCallbackThreadInitializer(final Callback cb, final CallbackThreadInitializer initializer) {
        CallbackReference.setCallbackThreadInitializer(cb, initializer);
    }
    
    public static void unregister() {
        unregister(getNativeClass(getCallingClass()));
    }
    
    public static void unregister(final Class cls) {
        synchronized (Native.registeredClasses) {
            if (Native.registeredClasses.containsKey(cls)) {
                unregister(cls, Native.registeredClasses.get(cls));
                Native.registeredClasses.remove(cls);
                Native.registeredLibraries.remove(cls);
            }
        }
    }
    
    private static native void unregister(final Class p0, final long[] p1);
    
    private static String getSignature(final Class cls) {
        if (cls.isArray()) {
            return "[" + getSignature(cls.getComponentType());
        }
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                return "V";
            }
            if (cls == Boolean.TYPE) {
                return "Z";
            }
            if (cls == Byte.TYPE) {
                return "B";
            }
            if (cls == Short.TYPE) {
                return "S";
            }
            if (cls == Character.TYPE) {
                return "C";
            }
            if (cls == Integer.TYPE) {
                return "I";
            }
            if (cls == Long.TYPE) {
                return "J";
            }
            if (cls == Float.TYPE) {
                return "F";
            }
            if (cls == Double.TYPE) {
                return "D";
            }
        }
        return "L" + replace(".", "/", cls.getName()) + ";";
    }
    
    static String replace(final String s1, final String s2, String str) {
        final StringBuffer buf = new StringBuffer();
        while (true) {
            final int idx = str.indexOf(s1);
            if (idx == -1) {
                break;
            }
            buf.append(str.substring(0, idx));
            buf.append(s2);
            str = str.substring(idx + s1.length());
        }
        buf.append(str);
        return buf.toString();
    }
    
    private static int getConversion(Class type, final TypeMapper mapper) {
        if (type == Boolean.class) {
            type = Boolean.TYPE;
        }
        else if (type == Byte.class) {
            type = Byte.TYPE;
        }
        else if (type == Short.class) {
            type = Short.TYPE;
        }
        else if (type == Character.class) {
            type = Character.TYPE;
        }
        else if (type == Integer.class) {
            type = Integer.TYPE;
        }
        else if (type == Long.class) {
            type = Long.TYPE;
        }
        else if (type == Float.class) {
            type = Float.TYPE;
        }
        else if (type == Double.class) {
            type = Double.TYPE;
        }
        else if (type == Void.class) {
            type = Void.TYPE;
        }
        if (mapper != null && (mapper.getFromNativeConverter((Class)type) != null || mapper.getToNativeConverter((Class)type) != null)) {
            return 21;
        }
        if (Pointer.class.isAssignableFrom((Class)type)) {
            return 1;
        }
        if (String.class == type) {
            return 2;
        }
        if (WString.class.isAssignableFrom((Class)type)) {
            return 18;
        }
        if (Platform.HAS_BUFFERS && Buffers.isBuffer((Class)type)) {
            return 5;
        }
        if (Structure.class.isAssignableFrom((Class)type)) {
            if (Structure.ByValue.class.isAssignableFrom((Class)type)) {
                return 4;
            }
            return 3;
        }
        else {
            if (((Class)type).isArray()) {
                switch (((Class)type).getName().charAt(1)) {
                    case 'Z': {
                        return 13;
                    }
                    case 'B': {
                        return 6;
                    }
                    case 'S': {
                        return 7;
                    }
                    case 'C': {
                        return 8;
                    }
                    case 'I': {
                        return 9;
                    }
                    case 'J': {
                        return 10;
                    }
                    case 'F': {
                        return 11;
                    }
                    case 'D': {
                        return 12;
                    }
                }
            }
            if (((Class)type).isPrimitive()) {
                return (type == Boolean.TYPE) ? 14 : 0;
            }
            if (Callback.class.isAssignableFrom((Class)type)) {
                return 15;
            }
            if (IntegerType.class.isAssignableFrom((Class)type)) {
                return 19;
            }
            if (PointerType.class.isAssignableFrom((Class)type)) {
                return 20;
            }
            if (NativeMapped.class.isAssignableFrom((Class)type)) {
                return 17;
            }
            return -1;
        }
    }
    
    public static void register(final Class cls, final NativeLibrary lib) {
        final Method[] methods = cls.getDeclaredMethods();
        final List mlist = new ArrayList();
        final TypeMapper mapper = lib.getOptions().get("type-mapper");
        for (int i = 0; i < methods.length; ++i) {
            if ((methods[i].getModifiers() & 0x100) != 0x0) {
                mlist.add(methods[i]);
            }
        }
        final long[] handles = new long[mlist.size()];
        for (int j = 0; j < handles.length; ++j) {
            final Method method = mlist.get(j);
            String sig = "(";
            final Class rclass = method.getReturnType();
            final Class[] ptypes = method.getParameterTypes();
            final long[] atypes = new long[ptypes.length];
            final long[] closure_atypes = new long[ptypes.length];
            final int[] cvt = new int[ptypes.length];
            final ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
            FromNativeConverter fromNative = null;
            final int rcvt = getConversion(rclass, mapper);
            boolean throwLastError = false;
            long closure_rtype = 0L;
            long rtype = 0L;
            switch (rcvt) {
                case -1: {
                    throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
                }
                case 21: {
                    fromNative = mapper.getFromNativeConverter(rclass);
                    closure_rtype = Structure.FFIType.get(rclass).peer;
                    rtype = Structure.FFIType.get(fromNative.nativeType()).peer;
                    break;
                }
                case 17:
                case 19:
                case 20: {
                    closure_rtype = Structure.FFIType.get(Pointer.class).peer;
                    rtype = Structure.FFIType.get(NativeMappedConverter.getInstance(rclass).nativeType()).peer;
                    break;
                }
                case 3: {
                    rtype = (closure_rtype = Structure.FFIType.get(Pointer.class).peer);
                    break;
                }
                case 4: {
                    closure_rtype = Structure.FFIType.get(Pointer.class).peer;
                    rtype = Structure.FFIType.get(rclass).peer;
                    break;
                }
                default: {
                    rtype = (closure_rtype = Structure.FFIType.get(rclass).peer);
                    break;
                }
            }
            for (int t = 0; t < ptypes.length; ++t) {
                Class type = ptypes[t];
                sig += getSignature(type);
                cvt[t] = getConversion(type, mapper);
                if (cvt[t] == -1) {
                    throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")");
                }
                if (cvt[t] == 17 || cvt[t] == 19) {
                    type = NativeMappedConverter.getInstance(type).nativeType();
                }
                else if (cvt[t] == 21) {
                    toNative[t] = mapper.getToNativeConverter(type);
                }
                switch (cvt[t]) {
                    case 4:
                    case 17:
                    case 19:
                    case 20: {
                        atypes[t] = Structure.FFIType.get(type).peer;
                        closure_atypes[t] = Structure.FFIType.get(Pointer.class).peer;
                        break;
                    }
                    case 21: {
                        if (type.isPrimitive()) {
                            closure_atypes[t] = Structure.FFIType.get(type).peer;
                        }
                        else {
                            closure_atypes[t] = Structure.FFIType.get(Pointer.class).peer;
                        }
                        atypes[t] = Structure.FFIType.get(toNative[t].nativeType()).peer;
                        break;
                    }
                    case 0: {
                        closure_atypes[t] = (atypes[t] = Structure.FFIType.get(type).peer);
                        break;
                    }
                    default: {
                        closure_atypes[t] = (atypes[t] = Structure.FFIType.get(Pointer.class).peer);
                        break;
                    }
                }
            }
            sig += ")";
            sig += getSignature(rclass);
            final Class[] etypes = method.getExceptionTypes();
            for (int e = 0; e < etypes.length; ++e) {
                if (LastErrorException.class.isAssignableFrom(etypes[e])) {
                    throwLastError = true;
                    break;
                }
            }
            String name = method.getName();
            final FunctionMapper fmapper = lib.getOptions().get("function-mapper");
            if (fmapper != null) {
                name = fmapper.getFunctionName(lib, method);
            }
            final Function f = lib.getFunction(name, method);
            try {
                handles[j] = registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, rclass, f.peer, f.getCallingConvention(), throwLastError, toNative, fromNative);
            }
            catch (NoSuchMethodError e2) {
                throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
            }
        }
        synchronized (Native.registeredClasses) {
            Native.registeredClasses.put(cls, handles);
            Native.registeredLibraries.put(cls, lib);
        }
        cacheOptions(cls, lib.getOptions(), null);
    }
    
    private static void cacheOptions(final Class cls, final Map libOptions, final Object proxy) {
        synchronized (Native.libraries) {
            if (!libOptions.isEmpty()) {
                Native.options.put(cls, libOptions);
            }
            if (libOptions.containsKey("type-mapper")) {
                Native.typeMappers.put(cls, libOptions.get("type-mapper"));
            }
            if (libOptions.containsKey("structure-alignment")) {
                Native.alignments.put(cls, libOptions.get("structure-alignment"));
            }
            if (proxy != null) {
                Native.libraries.put(cls, new WeakReference<Object>(proxy));
            }
            if (!cls.isInterface() && Library.class.isAssignableFrom(cls)) {
                final Class[] ifaces = cls.getInterfaces();
                for (int i = 0; i < ifaces.length; ++i) {
                    if (Library.class.isAssignableFrom(ifaces[i])) {
                        cacheOptions(ifaces[i], libOptions, proxy);
                        break;
                    }
                }
            }
        }
    }
    
    private static native long registerMethod(final Class p0, final String p1, final String p2, final int[] p3, final long[] p4, final long[] p5, final int p6, final long p7, final long p8, final Class p9, final long p10, final int p11, final boolean p12, final ToNativeConverter[] p13, final FromNativeConverter p14);
    
    private static NativeMapped fromNative(final Class cls, final Object value) {
        return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
    }
    
    private static Class nativeType(final Class cls) {
        return NativeMappedConverter.getInstance(cls).nativeType();
    }
    
    private static Object toNative(final ToNativeConverter cvt, final Object o) {
        return cvt.toNative(o, new ToNativeContext());
    }
    
    private static Object fromNative(final FromNativeConverter cvt, final Object o, final Class cls) {
        return cvt.fromNative(o, new FromNativeContext(cls));
    }
    
    public static native long ffi_prep_cif(final int p0, final int p1, final long p2, final long p3);
    
    public static native void ffi_call(final long p0, final long p1, final long p2, final long p3);
    
    public static native long ffi_prep_closure(final long p0, final ffi_callback p1);
    
    public static native void ffi_free_closure(final long p0);
    
    static native int initialize_ffi_type(final long p0);
    
    public static void main(final String[] args) {
        final String DEFAULT_TITLE = "Java Native Access (JNA)";
        final String DEFAULT_VERSION = "3.4.0";
        final String DEFAULT_BUILD = "3.4.0 (package information missing)";
        final Package pkg = Native.class.getPackage();
        String title = (pkg != null) ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
        if (title == null) {
            title = "Java Native Access (JNA)";
        }
        String version = (pkg != null) ? pkg.getSpecificationVersion() : "3.4.0";
        if (version == null) {
            version = "3.4.0";
        }
        title = title + " API Version " + version;
        System.out.println(title);
        version = ((pkg != null) ? pkg.getImplementationVersion() : "3.4.0 (package information missing)");
        if (version == null) {
            version = "3.4.0 (package information missing)";
        }
        System.out.println("Version: " + version);
        System.out.println(" Native: " + getNativeVersion() + " (" + getAPIChecksum() + ")");
        System.exit(0);
    }
    
    static synchronized native void freeNativeCallback(final long p0);
    
    static synchronized native long createNativeCallback(final Callback p0, final Method p1, final Class[] p2, final Class p3, final int p4, final boolean p5);
    
    static native int invokeInt(final long p0, final int p1, final Object[] p2);
    
    static native long invokeLong(final long p0, final int p1, final Object[] p2);
    
    static native void invokeVoid(final long p0, final int p1, final Object[] p2);
    
    static native float invokeFloat(final long p0, final int p1, final Object[] p2);
    
    static native double invokeDouble(final long p0, final int p1, final Object[] p2);
    
    static native long invokePointer(final long p0, final int p1, final Object[] p2);
    
    private static native void invokeStructure(final long p0, final int p1, final Object[] p2, final long p3, final long p4);
    
    static Structure invokeStructure(final long fp, final int callFlags, final Object[] args, final Structure s) {
        invokeStructure(fp, callFlags, args, s.getPointer().peer, s.getTypeInfo().peer);
        return s;
    }
    
    static native Object invokeObject(final long p0, final int p1, final Object[] p2);
    
    static native long open(final String p0);
    
    static native void close(final long p0);
    
    static native long findSymbol(final long p0, final String p1);
    
    static native long indexOf(final long p0, final byte p1);
    
    static native void read(final long p0, final byte[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final short[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final char[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final int[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final long[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final float[] p1, final int p2, final int p3);
    
    static native void read(final long p0, final double[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final byte[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final short[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final char[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final int[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final long[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final float[] p1, final int p2, final int p3);
    
    static native void write(final long p0, final double[] p1, final int p2, final int p3);
    
    static native byte getByte(final long p0);
    
    static native char getChar(final long p0);
    
    static native short getShort(final long p0);
    
    static native int getInt(final long p0);
    
    static native long getLong(final long p0);
    
    static native float getFloat(final long p0);
    
    static native double getDouble(final long p0);
    
    static Pointer getPointer(final long addr) {
        final long peer = _getPointer(addr);
        return (peer == 0L) ? null : new Pointer(peer);
    }
    
    private static native long _getPointer(final long p0);
    
    static native String getString(final long p0, final boolean p1);
    
    static native void setMemory(final long p0, final long p1, final byte p2);
    
    static native void setByte(final long p0, final byte p1);
    
    static native void setShort(final long p0, final short p1);
    
    static native void setChar(final long p0, final char p1);
    
    static native void setInt(final long p0, final int p1);
    
    static native void setLong(final long p0, final long p1);
    
    static native void setFloat(final long p0, final float p1);
    
    static native void setDouble(final long p0, final double p1);
    
    static native void setPointer(final long p0, final long p1);
    
    static native void setString(final long p0, final String p1, final boolean p2);
    
    public static native long malloc(final long p0);
    
    public static native void free(final long p0);
    
    public static native ByteBuffer getDirectByteBuffer(final long p0, final long p1);
    
    public static void detach(final boolean detach) {
        setLastError(detach ? -1 : -2);
    }
    
    static {
        Native.nativeLibraryPath = null;
        Native.typeMappers = new WeakHashMap();
        Native.alignments = new WeakHashMap();
        Native.options = new WeakHashMap();
        Native.libraries = new WeakHashMap();
        DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler() {
            public void uncaughtException(final Callback c, final Throwable e) {
                System.err.println("JNA: Callback " + c + " threw the following exception:");
                e.printStackTrace();
            }
        };
        Native.callbackExceptionHandler = Native.DEFAULT_HANDLER;
        loadNativeLibrary();
        POINTER_SIZE = sizeof(0);
        LONG_SIZE = sizeof(1);
        WCHAR_SIZE = sizeof(2);
        SIZE_T_SIZE = sizeof(3);
        initIDs();
        if (Boolean.getBoolean("jna.protected")) {
            setProtected(true);
        }
        final String version = getNativeVersion();
        if (!"3.4.0".equals(version)) {
            final String LS = System.getProperty("line.separator");
            throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
        }
        setPreserveLastError("true".equalsIgnoreCase(System.getProperty("jna.preserve_last_error", "true")));
        finalizer = new Object() {
            protected void finalize() {
                dispose();
            }
        };
        lastError = new ThreadLocal() {
            protected synchronized Object initialValue() {
                return new Integer(0);
            }
        };
        Native.registeredClasses = new HashMap();
        Native.registeredLibraries = new HashMap();
        Native.unloader = new Object() {
            protected void finalize() {
                synchronized (Native.registeredClasses) {
                    final Iterator i = Native.registeredClasses.entrySet().iterator();
                    while (i.hasNext()) {
                        final Map.Entry e = i.next();
                        unregister(e.getKey(), e.getValue());
                        i.remove();
                    }
                }
            }
        };
    }
    
    private static class Buffers
    {
        static boolean isBuffer(final Class cls) {
            return Buffer.class.isAssignableFrom(cls);
        }
    }
    
    private static class AWT
    {
        static long getWindowID(final Window w) throws HeadlessException {
            return getComponentID(w);
        }
        
        static long getComponentID(final Object o) throws HeadlessException {
            if (GraphicsEnvironment.isHeadless()) {
                throw new HeadlessException("No native windows when headless");
            }
            final Component c = (Component)o;
            if (c.isLightweight()) {
                throw new IllegalArgumentException("Component must be heavyweight");
            }
            if (!c.isDisplayable()) {
                throw new IllegalStateException("Component must be displayable");
            }
            if (Platform.isX11() && System.getProperty("java.version").startsWith("1.4") && !c.isVisible()) {
                throw new IllegalStateException("Component must be visible");
            }
            return Native.getWindowHandle0(c);
        }
    }
    
    public interface ffi_callback
    {
        void invoke(final long p0, final long p1, final long p2);
    }
}
