// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeLibrary
{
    private long handle;
    private final String libraryName;
    private final String libraryPath;
    private final Map functions;
    final int callFlags;
    final Map options;
    private static final Map libraries;
    private static final Map searchPaths;
    private static final List librarySearchPath;
    
    private static String functionKey(final String name, final int flags) {
        return name + "|" + flags;
    }
    
    private NativeLibrary(final String libraryName, final String libraryPath, final long handle, final Map options) {
        this.functions = new HashMap();
        this.libraryName = this.getLibraryName(libraryName);
        this.libraryPath = libraryPath;
        this.handle = handle;
        final Object option = options.get("calling-convention");
        final int callingConvention = (int)((option instanceof Integer) ? option : 0);
        this.callFlags = callingConvention;
        this.options = options;
        if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
            synchronized (this.functions) {
                final Function f = new Function(this, "GetLastError", 1) {
                    Object invoke(final Object[] args, final Class returnType, final boolean b) {
                        return new Integer(Native.getLastError());
                    }
                };
                this.functions.put(functionKey("GetLastError", this.callFlags), f);
            }
        }
    }
    
    private static NativeLibrary loadLibrary(final String libraryName, final Map options) {
        final List searchPath = new LinkedList();
        final String webstartPath = Native.getWebStartLibraryPath(libraryName);
        if (webstartPath != null) {
            searchPath.add(webstartPath);
        }
        final List customPaths = NativeLibrary.searchPaths.get(libraryName);
        if (customPaths != null) {
            synchronized (customPaths) {
                searchPath.addAll(0, customPaths);
            }
        }
        searchPath.addAll(initPaths("jna.library.path"));
        String libraryPath = findLibraryPath(libraryName, searchPath);
        long handle = 0L;
        try {
            handle = Native.open(libraryPath);
        }
        catch (UnsatisfiedLinkError e) {
            searchPath.addAll(NativeLibrary.librarySearchPath);
        }
        try {
            if (handle == 0L) {
                libraryPath = findLibraryPath(libraryName, searchPath);
                handle = Native.open(libraryPath);
                if (handle == 0L) {
                    throw new UnsatisfiedLinkError("Failed to load library '" + libraryName + "'");
                }
            }
        }
        catch (UnsatisfiedLinkError e) {
            if (Platform.isLinux()) {
                libraryPath = matchLibrary(libraryName, searchPath);
                if (libraryPath != null) {
                    try {
                        handle = Native.open(libraryPath);
                    }
                    catch (UnsatisfiedLinkError e2) {
                        e = e2;
                    }
                }
            }
            else if (Platform.isMac() && !libraryName.endsWith(".dylib")) {
                libraryPath = "/System/Library/Frameworks/" + libraryName + ".framework/" + libraryName;
                if (new File(libraryPath).exists()) {
                    try {
                        handle = Native.open(libraryPath);
                    }
                    catch (UnsatisfiedLinkError e2) {
                        e = e2;
                    }
                }
            }
            else if (Platform.isWindows()) {
                libraryPath = findLibraryPath("lib" + libraryName, searchPath);
                try {
                    handle = Native.open(libraryPath);
                }
                catch (UnsatisfiedLinkError e2) {
                    e = e2;
                }
            }
            if (handle == 0L) {
                throw new UnsatisfiedLinkError("Unable to load library '" + libraryName + "': " + e.getMessage());
            }
        }
        return new NativeLibrary(libraryName, libraryPath, handle, options);
    }
    
    private String getLibraryName(final String libraryName) {
        String simplified = libraryName;
        final String BASE = "---";
        final String template = mapLibraryName("---");
        final int prefixEnd = template.indexOf("---");
        if (prefixEnd > 0 && simplified.startsWith(template.substring(0, prefixEnd))) {
            simplified = simplified.substring(prefixEnd);
        }
        final String suffix = template.substring(prefixEnd + "---".length());
        final int suffixStart = simplified.indexOf(suffix);
        if (suffixStart != -1) {
            simplified = simplified.substring(0, suffixStart);
        }
        return simplified;
    }
    
    public static final NativeLibrary getInstance(final String libraryName) {
        return getInstance(libraryName, Collections.EMPTY_MAP);
    }
    
    public static final NativeLibrary getInstance(String libraryName, Map options) {
        options = new HashMap<String, Integer>(options);
        if (options.get("calling-convention") == null) {
            options.put("calling-convention", new Integer(0));
        }
        if (Platform.isLinux() && "c".equals(libraryName)) {
            libraryName = null;
        }
        synchronized (NativeLibrary.libraries) {
            WeakReference ref = (WeakReference)NativeLibrary.libraries.get(libraryName + options);
            NativeLibrary library = (ref != null) ? ((NativeLibrary)ref.get()) : null;
            if (library == null) {
                if (libraryName == null) {
                    library = new NativeLibrary("<process>", null, Native.open(null), options);
                }
                else {
                    library = loadLibrary(libraryName, options);
                }
                ref = new WeakReference((T)library);
                NativeLibrary.libraries.put(library.getName() + options, ref);
                final File file = library.getFile();
                if (file != null) {
                    NativeLibrary.libraries.put(file.getAbsolutePath() + options, ref);
                    NativeLibrary.libraries.put(file.getName() + options, ref);
                }
            }
            return library;
        }
    }
    
    public static final synchronized NativeLibrary getProcess() {
        return getInstance(null);
    }
    
    public static final synchronized NativeLibrary getProcess(final Map options) {
        return getInstance(null, options);
    }
    
    public static final void addSearchPath(final String libraryName, final String path) {
        synchronized (NativeLibrary.searchPaths) {
            List customPaths = NativeLibrary.searchPaths.get(libraryName);
            if (customPaths == null) {
                customPaths = Collections.synchronizedList(new LinkedList<Object>());
                NativeLibrary.searchPaths.put(libraryName, customPaths);
            }
            customPaths.add(path);
        }
    }
    
    public Function getFunction(final String functionName) {
        return this.getFunction(functionName, this.callFlags);
    }
    
    Function getFunction(final String name, final Method method) {
        int flags = this.callFlags;
        final Class[] etypes = method.getExceptionTypes();
        for (int i = 0; i < etypes.length; ++i) {
            if (LastErrorException.class.isAssignableFrom(etypes[i])) {
                flags |= 0x4;
            }
        }
        return this.getFunction(name, flags);
    }
    
    public Function getFunction(final String functionName, final int callFlags) {
        if (functionName == null) {
            throw new NullPointerException("Function name may not be null");
        }
        synchronized (this.functions) {
            final String key = functionKey(functionName, callFlags);
            Function function = this.functions.get(key);
            if (function == null) {
                function = new Function(this, functionName, callFlags);
                this.functions.put(key, function);
            }
            return function;
        }
    }
    
    public Map getOptions() {
        return this.options;
    }
    
    public Pointer getGlobalVariableAddress(final String symbolName) {
        try {
            return new Pointer(this.getSymbolAddress(symbolName));
        }
        catch (UnsatisfiedLinkError e) {
            throw new UnsatisfiedLinkError("Error looking up '" + symbolName + "': " + e.getMessage());
        }
    }
    
    long getSymbolAddress(final String name) {
        if (this.handle == 0L) {
            throw new UnsatisfiedLinkError("Library has been unloaded");
        }
        return Native.findSymbol(this.handle, name);
    }
    
    public String toString() {
        return "Native Library <" + this.libraryPath + "@" + this.handle + ">";
    }
    
    public String getName() {
        return this.libraryName;
    }
    
    public File getFile() {
        if (this.libraryPath == null) {
            return null;
        }
        return new File(this.libraryPath);
    }
    
    protected void finalize() {
        this.dispose();
    }
    
    static void disposeAll() {
        final Set values;
        synchronized (NativeLibrary.libraries) {
            values = new HashSet(NativeLibrary.libraries.values());
        }
        for (final Reference ref : values) {
            final NativeLibrary lib = ref.get();
            if (lib != null) {
                lib.dispose();
            }
        }
    }
    
    public void dispose() {
        synchronized (NativeLibrary.libraries) {
            final Iterator i = NativeLibrary.libraries.values().iterator();
            while (i.hasNext()) {
                final Reference ref = i.next();
                if (ref.get() == this) {
                    i.remove();
                }
            }
        }
        synchronized (this) {
            if (this.handle != 0L) {
                Native.close(this.handle);
                this.handle = 0L;
            }
        }
    }
    
    private static List initPaths(final String key) {
        final String value = System.getProperty(key, "");
        if ("".equals(value)) {
            return Collections.EMPTY_LIST;
        }
        final StringTokenizer st = new StringTokenizer(value, File.pathSeparator);
        final List list = new ArrayList();
        while (st.hasMoreTokens()) {
            final String path = st.nextToken();
            if (!"".equals(path)) {
                list.add(path);
            }
        }
        return list;
    }
    
    private static String findLibraryPath(final String libName, final List searchPath) {
        if (new File(libName).isAbsolute()) {
            return libName;
        }
        final String name = mapLibraryName(libName);
        for (final String path : searchPath) {
            File file = new File(path, name);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            if (!Platform.isMac() || !name.endsWith(".dylib")) {
                continue;
            }
            file = new File(path, name.substring(0, name.lastIndexOf(".dylib")) + ".jnilib");
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }
        return name;
    }
    
    private static String mapLibraryName(final String libName) {
        if (!Platform.isMac()) {
            if (Platform.isLinux()) {
                if (isVersionedName(libName) || libName.endsWith(".so")) {
                    return libName;
                }
            }
            else if (Platform.isWindows() && (libName.endsWith(".drv") || libName.endsWith(".dll"))) {
                return libName;
            }
            return System.mapLibraryName(libName);
        }
        if (libName.startsWith("lib") && (libName.endsWith(".dylib") || libName.endsWith(".jnilib"))) {
            return libName;
        }
        final String name = System.mapLibraryName(libName);
        if (name.endsWith(".jnilib")) {
            return name.substring(0, name.lastIndexOf(".jnilib")) + ".dylib";
        }
        return name;
    }
    
    private static boolean isVersionedName(final String name) {
        if (name.startsWith("lib")) {
            final int so = name.lastIndexOf(".so.");
            if (so != -1 && so + 4 < name.length()) {
                for (int i = so + 4; i < name.length(); ++i) {
                    final char ch = name.charAt(i);
                    if (!Character.isDigit(ch) && ch != '.') {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    static String matchLibrary(final String libName, List searchPath) {
        final File lib = new File(libName);
        if (lib.isAbsolute()) {
            searchPath = Arrays.asList(lib.getParent());
        }
        final FilenameFilter filter = new FilenameFilter() {
            public boolean accept(final File dir, final String filename) {
                return (filename.startsWith("lib" + libName + ".so") || (filename.startsWith(libName + ".so") && libName.startsWith("lib"))) && isVersionedName(filename);
            }
        };
        final List matches = new LinkedList();
        final Iterator it = searchPath.iterator();
        while (it.hasNext()) {
            final File[] files = new File(it.next()).listFiles(filter);
            if (files != null && files.length > 0) {
                matches.addAll(Arrays.asList(files));
            }
        }
        double bestVersion = -1.0;
        String bestMatch = null;
        final Iterator it2 = matches.iterator();
        while (it2.hasNext()) {
            final String path = it2.next().getAbsolutePath();
            final String ver = path.substring(path.lastIndexOf(".so.") + 4);
            final double version = parseVersion(ver);
            if (version > bestVersion) {
                bestVersion = version;
                bestMatch = path;
            }
        }
        return bestMatch;
    }
    
    static double parseVersion(String ver) {
        double v = 0.0;
        double divisor = 1.0;
        int dot = ver.indexOf(".");
        while (ver != null) {
            String num;
            if (dot != -1) {
                num = ver.substring(0, dot);
                ver = ver.substring(dot + 1);
                dot = ver.indexOf(".");
            }
            else {
                num = ver;
                ver = null;
            }
            try {
                v += Integer.parseInt(num) / divisor;
            }
            catch (NumberFormatException e) {
                return 0.0;
            }
            divisor *= 100.0;
        }
        return v;
    }
    
    static {
        libraries = new HashMap();
        searchPaths = Collections.synchronizedMap(new HashMap<Object, Object>());
        librarySearchPath = new LinkedList();
        if (Native.POINTER_SIZE == 0) {
            throw new Error("Native library not initialized");
        }
        final String webstartPath = Native.getWebStartLibraryPath("jnidispatch");
        if (webstartPath != null) {
            NativeLibrary.librarySearchPath.add(webstartPath);
        }
        if (System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
            String platformPath = "";
            String sep = "";
            String archPath = "";
            if (Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD()) {
                archPath = (Platform.isSolaris() ? "/" : "") + Pointer.SIZE * 8;
            }
            String[] paths = { "/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib" };
            if (Platform.isLinux()) {
                String cpu = "";
                final String kernel = "linux";
                String libc = "gnu";
                if (Platform.isIntel()) {
                    cpu = (Platform.is64Bit() ? "x86_64" : "i386");
                }
                else if (Platform.isPPC()) {
                    cpu = (Platform.is64Bit() ? "powerpc64" : "powerpc");
                }
                else if (Platform.isARM()) {
                    cpu = "arm";
                    libc = "gnueabi";
                }
                final String multiArchPath = cpu + "-" + kernel + "-" + libc;
                paths = new String[] { "/usr/lib/" + multiArchPath, "/lib/" + multiArchPath, "/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib" };
            }
            for (int i = 0; i < paths.length; ++i) {
                final File dir = new File(paths[i]);
                if (dir.exists() && dir.isDirectory()) {
                    platformPath = platformPath + sep + paths[i];
                    sep = File.pathSeparator;
                }
            }
            if (!"".equals(platformPath)) {
                System.setProperty("jna.platform.library.path", platformPath);
            }
        }
        NativeLibrary.librarySearchPath.addAll(initPaths("jna.platform.library.path"));
    }
}
