// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.InputStream;
import java.util.jar.JarEntry;
import java.io.FileNotFoundException;
import java.util.zip.ZipEntry;
import java.util.jar.JarFile;
import java.io.FileInputStream;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.File;
import java.io.FileFilter;

class PluginClassLoader extends ClassLoader
{
    private static String pluginDirectory;
    private static final FileFilter JAR_FILTER;
    
    public PluginClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }
    
    protected Class findClass(final String name) throws ClassNotFoundException {
        final byte[] b = this.loadClassData(name);
        return this.defineClass(name, b, 0, b.length);
    }
    
    private byte[] loadClassData(final String name) throws ClassNotFoundException {
        if (PluginClassLoader.pluginDirectory == null) {
            PluginClassLoader.pluginDirectory = DefaultControllerEnvironment.libPath + File.separator + "controller";
        }
        try {
            return this.loadClassFromDirectory(name);
        }
        catch (Exception e3) {
            try {
                return this.loadClassFromJAR(name);
            }
            catch (IOException e2) {
                throw new ClassNotFoundException(name, e2);
            }
        }
    }
    
    private byte[] loadClassFromDirectory(final String name) throws ClassNotFoundException, IOException {
        final StringTokenizer tokenizer = new StringTokenizer(name, ".");
        final StringBuffer path = new StringBuffer(PluginClassLoader.pluginDirectory);
        while (tokenizer.hasMoreTokens()) {
            path.append(File.separator);
            path.append(tokenizer.nextToken());
        }
        path.append(".class");
        final File file = new File(path.toString());
        if (!file.exists()) {
            throw new ClassNotFoundException(name);
        }
        final FileInputStream fileInputStream = new FileInputStream(file);
        assert file.length() <= 2147483647L;
        final int length = (int)file.length();
        final byte[] bytes = new byte[length];
        final int length2 = fileInputStream.read(bytes);
        assert length == length2;
        return bytes;
    }
    
    private byte[] loadClassFromJAR(final String name) throws ClassNotFoundException, IOException {
        final File dir = new File(PluginClassLoader.pluginDirectory);
        final File[] jarFiles = dir.listFiles(PluginClassLoader.JAR_FILTER);
        if (jarFiles == null) {
            throw new ClassNotFoundException("Could not find class " + name);
        }
        int i = 0;
        while (i < jarFiles.length) {
            final JarFile jarfile = new JarFile(jarFiles[i]);
            final JarEntry jarentry = jarfile.getJarEntry(name + ".class");
            if (jarentry != null) {
                final InputStream jarInputStream = jarfile.getInputStream(jarentry);
                assert jarentry.getSize() <= 2147483647L;
                final int length = (int)jarentry.getSize();
                assert length >= 0;
                final byte[] bytes = new byte[length];
                final int length2 = jarInputStream.read(bytes);
                assert length == length2;
                return bytes;
            }
            else {
                ++i;
            }
        }
        throw new FileNotFoundException(name);
    }
    
    static {
        JAR_FILTER = new JarFileFilter();
    }
    
    private static class JarFileFilter implements FileFilter
    {
        public boolean accept(final File file) {
            return file.getName().toUpperCase().endsWith(".JAR");
        }
    }
}
