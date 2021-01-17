// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.util.plugins;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.net.URLClassLoader;

public class PluginLoader extends URLClassLoader
{
    static final boolean DEBUG = false;
    File parentDir;
    boolean localDLLs;
    
    public PluginLoader(final File jf) throws MalformedURLException {
        super(new URL[] { jf.toURL() }, Thread.currentThread().getContextClassLoader());
        this.localDLLs = true;
        this.parentDir = jf.getParentFile();
        if (System.getProperty("net.java.games.util.plugins.nolocalnative") != null) {
            this.localDLLs = false;
        }
    }
    
    protected String findLibrary(final String libname) {
        if (this.localDLLs) {
            final String libpath = this.parentDir.getPath() + File.separator + System.mapLibraryName(libname);
            return libpath;
        }
        return super.findLibrary(libname);
    }
    
    public boolean attemptPluginDefine(final Class pc) {
        return !pc.isInterface() && this.classImplementsPlugin(pc);
    }
    
    private boolean classImplementsPlugin(final Class testClass) {
        if (testClass == null) {
            return false;
        }
        final Class[] implementedInterfaces = testClass.getInterfaces();
        for (int i = 0; i < implementedInterfaces.length; ++i) {
            if (implementedInterfaces[i] == Plugin.class) {
                return true;
            }
        }
        for (int i = 0; i < implementedInterfaces.length; ++i) {
            if (this.classImplementsPlugin(implementedInterfaces[i])) {
                return true;
            }
        }
        return this.classImplementsPlugin(testClass.getSuperclass());
    }
}
