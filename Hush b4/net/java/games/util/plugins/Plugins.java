// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.util.plugins;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.List;

public class Plugins
{
    static final boolean DEBUG = true;
    List pluginList;
    
    public Plugins(final File pluginRoot) throws IOException {
        this.pluginList = new ArrayList();
        this.scanPlugins(pluginRoot);
    }
    
    private void scanPlugins(final File dir) throws IOException {
        final File[] files = dir.listFiles();
        if (files == null) {
            throw new FileNotFoundException("Plugin directory " + dir.getName() + " not found.");
        }
        for (int i = 0; i < files.length; ++i) {
            final File f = files[i];
            if (f.getName().endsWith(".jar")) {
                this.processJar(f);
            }
            else if (f.isDirectory()) {
                this.scanPlugins(f);
            }
        }
    }
    
    private void processJar(final File f) {
        try {
            System.out.println("Scanning jar: " + f.getName());
            final PluginLoader loader = new PluginLoader(f);
            final JarFile jf = new JarFile(f);
            final Enumeration en = jf.entries();
            while (en.hasMoreElements()) {
                final JarEntry je = en.nextElement();
                System.out.println("Examining file : " + je.getName());
                if (je.getName().endsWith("Plugin.class")) {
                    System.out.println("Found candidate class: " + je.getName());
                    String cname = je.getName();
                    cname = cname.substring(0, cname.length() - 6);
                    cname = cname.replace('/', '.');
                    final Class pc = loader.loadClass(cname);
                    if (!loader.attemptPluginDefine(pc)) {
                        continue;
                    }
                    System.out.println("Adding class to plugins:" + pc.getName());
                    this.pluginList.add(pc);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Class[] get() {
        final Class[] pluginArray = new Class[this.pluginList.size()];
        return this.pluginList.toArray(pluginArray);
    }
    
    public Class[] getImplementsAny(final Class[] interfaces) {
        final List matchList = new ArrayList(this.pluginList.size());
        final Set interfaceSet = new HashSet();
        for (int i = 0; i < interfaces.length; ++i) {
            interfaceSet.add(interfaces[i]);
        }
        for (final Class pluginClass : this.pluginList) {
            if (this.classImplementsAny(pluginClass, interfaceSet)) {
                matchList.add(pluginClass);
            }
        }
        final Class[] pluginArray = new Class[matchList.size()];
        return matchList.toArray(pluginArray);
    }
    
    private boolean classImplementsAny(final Class testClass, final Set interfaces) {
        if (testClass == null) {
            return false;
        }
        final Class[] implementedInterfaces = testClass.getInterfaces();
        for (int i = 0; i < implementedInterfaces.length; ++i) {
            if (interfaces.contains(implementedInterfaces[i])) {
                return true;
            }
        }
        for (int i = 0; i < implementedInterfaces.length; ++i) {
            if (this.classImplementsAny(implementedInterfaces[i], interfaces)) {
                return true;
            }
        }
        return this.classImplementsAny(testClass.getSuperclass(), interfaces);
    }
    
    public Class[] getImplementsAll(final Class[] interfaces) {
        final List matchList = new ArrayList(this.pluginList.size());
        final Set interfaceSet = new HashSet();
        for (int i = 0; i < interfaces.length; ++i) {
            interfaceSet.add(interfaces[i]);
        }
        for (final Class pluginClass : this.pluginList) {
            if (this.classImplementsAll(pluginClass, interfaceSet)) {
                matchList.add(pluginClass);
            }
        }
        final Class[] pluginArray = new Class[matchList.size()];
        return matchList.toArray(pluginArray);
    }
    
    private boolean classImplementsAll(final Class testClass, final Set interfaces) {
        if (testClass == null) {
            return false;
        }
        final Class[] implementedInterfaces = testClass.getInterfaces();
        for (int i = 0; i < implementedInterfaces.length; ++i) {
            if (interfaces.contains(implementedInterfaces[i])) {
                interfaces.remove(implementedInterfaces[i]);
                if (interfaces.size() == 0) {
                    return true;
                }
            }
        }
        for (int i = 0; i < implementedInterfaces.length; ++i) {
            if (this.classImplementsAll(implementedInterfaces[i], interfaces)) {
                return true;
            }
        }
        return this.classImplementsAll(testClass.getSuperclass(), interfaces);
    }
    
    public Class[] getExtends(final Class superclass) {
        final List matchList = new ArrayList(this.pluginList.size());
        for (final Class pluginClass : this.pluginList) {
            if (this.classExtends(pluginClass, superclass)) {
                matchList.add(pluginClass);
            }
        }
        final Class[] pluginArray = new Class[matchList.size()];
        return matchList.toArray(pluginArray);
    }
    
    private boolean classExtends(final Class testClass, final Class superclass) {
        return testClass != null && (testClass == superclass || this.classExtends(testClass.getSuperclass(), superclass));
    }
}
