package me.rigamortis.faurax.module;

import java.net.*;
import java.io.*;
import java.util.jar.*;
import java.util.*;

public class ExternalModuleLoader
{
    public File folder;
    public URLClassLoader classLoader;
    public JarFile jar;
    
    public ExternalModuleLoader() {
        this.loadModules(this.folder = new File("Faurax 3.6/Plugins"));
    }
    
    public void loadModules(final File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("No folder found.");
            dir.mkdirs();
            return;
        }
        File[] listFiles;
        for (int length = (listFiles = dir.listFiles()).length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            if (file.getName().endsWith(".jar")) {
                try {
                    this.classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() });
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                    continue;
                }
                try {
                    this.jar = new JarFile(file);
                    System.out.println("Module found" + this.jar.getName());
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                    continue;
                }
                final Enumeration<JarEntry> entries = this.jar.entries();
                while (entries.hasMoreElements()) {
                    final JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory()) {
                        if (!entry.getName().endsWith(".class")) {
                            continue;
                        }
                        Class moduleClass;
                        try {
                            moduleClass = this.classLoader.loadClass(entry.getName().substring(0, entry.getName().length() - 6).replace("/", "."));
                        }
                        catch (ClassNotFoundException e3) {
                            e3.printStackTrace();
                            break;
                        }
                        if (!Module.class.isAssignableFrom(moduleClass)) {
                            continue;
                        }
                        try {
                            moduleClass.newInstance();
                            break;
                        }
                        catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
