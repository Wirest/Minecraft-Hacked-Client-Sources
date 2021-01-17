// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.util.Locale;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import io.netty.util.internal.logging.InternalLogger;

public final class NativeLibraryLoader
{
    private static final InternalLogger logger;
    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final String OSNAME;
    private static final File WORKDIR;
    
    private static File tmpdir() {
        try {
            File f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (f != null) {
                NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f);
                return f;
            }
            f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (f != null) {
                NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (java.io.tmpdir)");
                return f;
            }
            if (isWindows()) {
                f = toDirectory(System.getenv("TEMP"));
                if (f != null) {
                    NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (%TEMP%)");
                    return f;
                }
                final String userprofile = System.getenv("USERPROFILE");
                if (userprofile != null) {
                    f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                    if (f != null) {
                        NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\AppData\\Local\\Temp)");
                        return f;
                    }
                    f = toDirectory(userprofile + "\\Local Settings\\Temp");
                    if (f != null) {
                        NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\Local Settings\\Temp)");
                        return f;
                    }
                }
            }
            else {
                f = toDirectory(System.getenv("TMPDIR"));
                if (f != null) {
                    NativeLibraryLoader.logger.debug("-Dio.netty.tmpdir: " + f + " ($TMPDIR)");
                    return f;
                }
            }
        }
        catch (Exception ex) {}
        File f;
        if (isWindows()) {
            f = new File("C:\\Windows\\Temp");
        }
        else {
            f = new File("/tmp");
        }
        NativeLibraryLoader.logger.warn("Failed to get the temporary directory; falling back to: " + f);
        return f;
    }
    
    private static File toDirectory(final String path) {
        if (path == null) {
            return null;
        }
        final File f = new File(path);
        f.mkdirs();
        if (!f.isDirectory()) {
            return null;
        }
        try {
            return f.getAbsoluteFile();
        }
        catch (Exception ignored) {
            return f;
        }
    }
    
    private static boolean isWindows() {
        return NativeLibraryLoader.OSNAME.startsWith("windows");
    }
    
    private static boolean isOSX() {
        return NativeLibraryLoader.OSNAME.startsWith("macosx") || NativeLibraryLoader.OSNAME.startsWith("osx");
    }
    
    public static void load(final String name, final ClassLoader loader) {
        final String libname = System.mapLibraryName(name);
        final String path = "META-INF/native/" + libname;
        URL url = loader.getResource(path);
        if (url == null && isOSX()) {
            if (path.endsWith(".jnilib")) {
                url = loader.getResource("META-INF/native/lib" + name + ".dynlib");
            }
            else {
                url = loader.getResource("META-INF/native/lib" + name + ".jnilib");
            }
        }
        if (url == null) {
            System.loadLibrary(name);
            return;
        }
        final int index = libname.lastIndexOf(46);
        final String prefix = libname.substring(0, index);
        final String suffix = libname.substring(index, libname.length());
        InputStream in = null;
        OutputStream out = null;
        File tmpFile = null;
        boolean loaded = false;
        try {
            tmpFile = File.createTempFile(prefix, suffix, NativeLibraryLoader.WORKDIR);
            in = url.openStream();
            out = new FileOutputStream(tmpFile);
            final byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            out.close();
            out = null;
            System.load(tmpFile.getPath());
            loaded = true;
        }
        catch (Exception e) {
            throw (UnsatisfiedLinkError)new UnsatisfiedLinkError("could not load a native library: " + name).initCause(e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ex) {}
            }
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException ex2) {}
            }
            if (tmpFile != null) {
                if (loaded) {
                    tmpFile.deleteOnExit();
                }
                else if (!tmpFile.delete()) {
                    tmpFile.deleteOnExit();
                }
            }
        }
    }
    
    private NativeLibraryLoader() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
        OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
        final String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
        if (workdir != null) {
            File f = new File(workdir);
            f.mkdirs();
            try {
                f = f.getAbsoluteFile();
            }
            catch (Exception ex) {}
            WORKDIR = f;
            NativeLibraryLoader.logger.debug("-Dio.netty.netty.workdir: " + NativeLibraryLoader.WORKDIR);
        }
        else {
            WORKDIR = tmpdir();
            NativeLibraryLoader.logger.debug("-Dio.netty.netty.workdir: " + NativeLibraryLoader.WORKDIR + " (io.netty.tmpdir)");
        }
    }
}
