// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.net.JarURLConnection;
import java.util.jar.JarFile;
import java.net.URISyntaxException;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class URLHandler
{
    public static final String PROPNAME = "urlhandler.props";
    private static final Map<String, Method> handlers;
    private static final boolean DEBUG;
    
    public static URLHandler get(final URL url) {
        if (url == null) {
            return null;
        }
        final String protocol = url.getProtocol();
        if (URLHandler.handlers != null) {
            final Method m = URLHandler.handlers.get(protocol);
            if (m != null) {
                try {
                    final URLHandler handler = (URLHandler)m.invoke(null, url);
                    if (handler != null) {
                        return handler;
                    }
                }
                catch (IllegalAccessException e) {
                    if (URLHandler.DEBUG) {
                        System.err.println(e);
                    }
                }
                catch (IllegalArgumentException e2) {
                    if (URLHandler.DEBUG) {
                        System.err.println(e2);
                    }
                }
                catch (InvocationTargetException e3) {
                    if (URLHandler.DEBUG) {
                        System.err.println(e3);
                    }
                }
            }
        }
        return getDefault(url);
    }
    
    protected static URLHandler getDefault(final URL url) {
        URLHandler handler = null;
        final String protocol = url.getProtocol();
        try {
            if (protocol.equals("file")) {
                handler = new FileURLHandler(url);
            }
            else if (protocol.equals("jar") || protocol.equals("wsjar")) {
                handler = new JarURLHandler(url);
            }
        }
        catch (Exception ex) {}
        return handler;
    }
    
    public void guide(final URLVisitor visitor, final boolean recurse) {
        this.guide(visitor, recurse, true);
    }
    
    public abstract void guide(final URLVisitor p0, final boolean p1, final boolean p2);
    
    static {
        DEBUG = ICUDebug.enabled("URLHandler");
        Map<String, Method> h = null;
        try {
            InputStream is = URLHandler.class.getResourceAsStream("urlhandler.props");
            if (is == null) {
                final ClassLoader loader = Utility.getFallbackClassLoader();
                is = loader.getResourceAsStream("urlhandler.props");
            }
            if (is != null) {
                final Class<?>[] params = (Class<?>[])new Class[] { URL.class };
                final BufferedReader br = new BufferedReader(new InputStreamReader(is));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    line = line.trim();
                    if (line.length() != 0) {
                        if (line.charAt(0) != '#') {
                            final int ix = line.indexOf(61);
                            if (ix == -1) {
                                if (URLHandler.DEBUG) {
                                    System.err.println("bad urlhandler line: '" + line + "'");
                                    break;
                                }
                                break;
                            }
                            else {
                                final String key = line.substring(0, ix).trim();
                                final String value = line.substring(ix + 1).trim();
                                try {
                                    final Class<?> cl = Class.forName(value);
                                    final Method m = cl.getDeclaredMethod("get", params);
                                    if (h == null) {
                                        h = new HashMap<String, Method>();
                                    }
                                    h.put(key, m);
                                }
                                catch (ClassNotFoundException e) {
                                    if (URLHandler.DEBUG) {
                                        System.err.println(e);
                                    }
                                }
                                catch (NoSuchMethodException e2) {
                                    if (URLHandler.DEBUG) {
                                        System.err.println(e2);
                                    }
                                }
                                catch (SecurityException e3) {
                                    if (URLHandler.DEBUG) {
                                        System.err.println(e3);
                                    }
                                }
                            }
                        }
                    }
                }
                br.close();
            }
        }
        catch (Throwable t) {
            if (URLHandler.DEBUG) {
                System.err.println(t);
            }
        }
        handlers = h;
    }
    
    private static class FileURLHandler extends URLHandler
    {
        File file;
        
        FileURLHandler(final URL url) {
            try {
                this.file = new File(url.toURI());
            }
            catch (URISyntaxException ex) {}
            if (this.file == null || !this.file.exists()) {
                if (URLHandler.DEBUG) {
                    System.err.println("file does not exist - " + url.toString());
                }
                throw new IllegalArgumentException();
            }
        }
        
        @Override
        public void guide(final URLVisitor v, final boolean recurse, final boolean strip) {
            if (this.file.isDirectory()) {
                this.process(v, recurse, strip, "/", this.file.listFiles());
            }
            else {
                v.visit(this.file.getName());
            }
        }
        
        private void process(final URLVisitor v, final boolean recurse, final boolean strip, final String path, final File[] files) {
            for (int i = 0; i < files.length; ++i) {
                final File f = files[i];
                if (f.isDirectory()) {
                    if (recurse) {
                        this.process(v, recurse, strip, path + f.getName() + '/', f.listFiles());
                    }
                }
                else {
                    v.visit(strip ? f.getName() : (path + f.getName()));
                }
            }
        }
    }
    
    private static class JarURLHandler extends URLHandler
    {
        JarFile jarFile;
        String prefix;
        
        JarURLHandler(URL url) {
            try {
                this.prefix = url.getPath();
                final int ix = this.prefix.lastIndexOf("!/");
                if (ix >= 0) {
                    this.prefix = this.prefix.substring(ix + 2);
                }
                final String protocol = url.getProtocol();
                if (!protocol.equals("jar")) {
                    final String urlStr = url.toString();
                    final int idx = urlStr.indexOf(":");
                    if (idx != -1) {
                        url = new URL("jar" + urlStr.substring(idx));
                    }
                }
                final JarURLConnection conn = (JarURLConnection)url.openConnection();
                this.jarFile = conn.getJarFile();
            }
            catch (Exception e) {
                if (URLHandler.DEBUG) {
                    System.err.println("icurb jar error: " + e);
                }
                throw new IllegalArgumentException("jar error: " + e.getMessage());
            }
        }
        
        @Override
        public void guide(final URLVisitor v, final boolean recurse, final boolean strip) {
            try {
                final Enumeration<JarEntry> entries = this.jarFile.entries();
                while (entries.hasMoreElements()) {
                    final JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory()) {
                        String name = entry.getName();
                        if (!name.startsWith(this.prefix)) {
                            continue;
                        }
                        name = name.substring(this.prefix.length());
                        final int ix = name.lastIndexOf(47);
                        if (ix != -1) {
                            if (!recurse) {
                                continue;
                            }
                            if (strip) {
                                name = name.substring(ix + 1);
                            }
                        }
                        v.visit(name);
                    }
                }
            }
            catch (Exception e) {
                if (URLHandler.DEBUG) {
                    System.err.println("icurb jar error: " + e);
                }
            }
        }
    }
    
    public interface URLVisitor
    {
        void visit(final String p0);
    }
}
