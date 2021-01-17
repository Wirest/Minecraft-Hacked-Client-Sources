// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

import java.io.IOException;
import java.io.InputStream;
import org.lwjgl.LWJGLUtil;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

public class MappedObjectClassLoader extends URLClassLoader
{
    static final String MAPPEDOBJECT_PACKAGE_PREFIX;
    static boolean FORKED;
    private static long total_time_transforming;
    
    public static boolean fork(final Class<?> mainClass, final String[] args) {
        if (MappedObjectClassLoader.FORKED) {
            return false;
        }
        MappedObjectClassLoader.FORKED = true;
        try {
            final MappedObjectClassLoader loader = new MappedObjectClassLoader(mainClass);
            loader.loadMappedObject();
            final Class<?> replacedMainClass = loader.loadClass(mainClass.getName());
            final Method mainMethod = replacedMainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, args);
        }
        catch (InvocationTargetException exc) {
            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exc.getCause());
        }
        catch (Throwable cause) {
            throw new Error("failed to fork", cause);
        }
        return true;
    }
    
    private MappedObjectClassLoader(final Class<?> mainClass) {
        super(((URLClassLoader)mainClass.getClassLoader()).getURLs());
    }
    
    protected synchronized Class<?> loadMappedObject() throws ClassNotFoundException {
        final String name = MappedObject.class.getName();
        final String className = name.replace('.', '/');
        byte[] bytecode = readStream(this.getResourceAsStream(className.concat(".class")));
        final long t0 = System.nanoTime();
        bytecode = MappedObjectTransformer.transformMappedObject(bytecode);
        final long t2 = System.nanoTime();
        MappedObjectClassLoader.total_time_transforming += t2 - t0;
        if (MappedObjectTransformer.PRINT_ACTIVITY) {
            printActivity(className, t0, t2);
        }
        final Class<?> clazz = super.defineClass(name, bytecode, 0, bytecode.length);
        this.resolveClass(clazz);
        return clazz;
    }
    
    @Override
    protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("sun.") || name.startsWith("sunw.") || name.startsWith("org.objectweb.asm.")) {
            return super.loadClass(name, resolve);
        }
        final String className = name.replace('.', '/');
        final boolean inThisPackage = name.startsWith(MappedObjectClassLoader.MAPPEDOBJECT_PACKAGE_PREFIX);
        if (inThisPackage && (name.equals(MappedObjectClassLoader.class.getName()) || name.equals(MappedObjectTransformer.class.getName()) || name.equals(CacheUtil.class.getName()))) {
            return super.loadClass(name, resolve);
        }
        byte[] bytecode = readStream(this.getResourceAsStream(className.concat(".class")));
        if (!inThisPackage || name.substring(MappedObjectClassLoader.MAPPEDOBJECT_PACKAGE_PREFIX.length()).indexOf(46) != -1) {
            final long t0 = System.nanoTime();
            final byte[] newBytecode = MappedObjectTransformer.transformMappedAPI(className, bytecode);
            final long t2 = System.nanoTime();
            MappedObjectClassLoader.total_time_transforming += t2 - t0;
            if (bytecode != newBytecode) {
                bytecode = newBytecode;
                if (MappedObjectTransformer.PRINT_ACTIVITY) {
                    printActivity(className, t0, t2);
                }
            }
        }
        final Class<?> clazz = super.defineClass(name, bytecode, 0, bytecode.length);
        if (resolve) {
            this.resolveClass(clazz);
        }
        return clazz;
    }
    
    private static void printActivity(final String className, final long t0, final long t1) {
        final StringBuilder msg = new StringBuilder(MappedObjectClassLoader.class.getSimpleName() + ": " + className);
        if (MappedObjectTransformer.PRINT_TIMING) {
            msg.append("\n\ttransforming took " + (t1 - t0) / 1000L + " micros (total: " + MappedObjectClassLoader.total_time_transforming / 1000L / 1000L + "ms)");
        }
        LWJGLUtil.log(msg);
    }
    
    private static byte[] readStream(final InputStream in) {
        byte[] bytecode = new byte[256];
        int len = 0;
        try {
            while (true) {
                if (bytecode.length == len) {
                    bytecode = copyOf(bytecode, len * 2);
                }
                final int got = in.read(bytecode, len, bytecode.length - len);
                if (got == -1) {
                    break;
                }
                len += got;
            }
        }
        catch (IOException exc) {}
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {}
        }
        return copyOf(bytecode, len);
    }
    
    private static byte[] copyOf(final byte[] original, final int newLength) {
        final byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    static {
        MAPPEDOBJECT_PACKAGE_PREFIX = MappedObjectClassLoader.class.getPackage().getName() + ".";
    }
}
