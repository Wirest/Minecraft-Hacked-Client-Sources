// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.status.StatusLogger;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Enumeration;
import java.io.Closeable;
import org.apache.logging.log4j.core.helpers.Closer;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.io.IOException;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.helpers.Loader;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentMap;

public class PluginManager
{
    private static final long NANOS_PER_SECOND = 1000000000L;
    private static ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> pluginTypeMap;
    private static final CopyOnWriteArrayList<String> PACKAGES;
    private static final String PATH = "org/apache/logging/log4j/core/config/plugins/";
    private static final String FILENAME = "Log4j2Plugins.dat";
    private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
    private static final Logger LOGGER;
    private static String rootDir;
    private Map<String, PluginType<?>> plugins;
    private final String type;
    private final Class<?> clazz;
    
    public PluginManager(final String type) {
        this.plugins = new HashMap<String, PluginType<?>>();
        this.type = type;
        this.clazz = null;
    }
    
    public PluginManager(final String type, final Class<?> clazz) {
        this.plugins = new HashMap<String, PluginType<?>>();
        this.type = type;
        this.clazz = clazz;
    }
    
    public static void main(final String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.err.println("A target directory must be specified");
            System.exit(-1);
        }
        PluginManager.rootDir = ((args[0].endsWith("/") || args[0].endsWith("\\")) ? args[0] : (args[0] + "/"));
        final PluginManager manager = new PluginManager("Core");
        final String packages = (args.length == 2) ? args[1] : null;
        manager.collectPlugins(false, packages);
        encode(PluginManager.pluginTypeMap);
    }
    
    public static void addPackage(final String p) {
        if (PluginManager.PACKAGES.addIfAbsent(p)) {
            PluginManager.pluginTypeMap.clear();
        }
    }
    
    public PluginType<?> getPluginType(final String name) {
        return this.plugins.get(name.toLowerCase());
    }
    
    public Map<String, PluginType<?>> getPlugins() {
        return this.plugins;
    }
    
    public void collectPlugins() {
        this.collectPlugins(true, null);
    }
    
    public void collectPlugins(boolean preLoad, final String pkgs) {
        if (PluginManager.pluginTypeMap.containsKey(this.type)) {
            this.plugins = PluginManager.pluginTypeMap.get(this.type);
            preLoad = false;
        }
        final long start = System.nanoTime();
        final ResolverUtil resolver = new ResolverUtil();
        final ClassLoader classLoader = Loader.getClassLoader();
        if (classLoader != null) {
            resolver.setClassLoader(classLoader);
        }
        if (preLoad) {
            final ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> map = decode(classLoader);
            if (map != null) {
                PluginManager.pluginTypeMap = map;
                this.plugins = map.get(this.type);
            }
            else {
                PluginManager.LOGGER.warn("Plugin preloads not available from class loader {}", classLoader);
            }
        }
        if (this.plugins == null || this.plugins.size() == 0) {
            if (pkgs == null) {
                if (!PluginManager.PACKAGES.contains("org.apache.logging.log4j.core")) {
                    PluginManager.PACKAGES.add("org.apache.logging.log4j.core");
                }
            }
            else {
                final String[] arr$;
                final String[] names = arr$ = pkgs.split(",");
                for (final String name : arr$) {
                    PluginManager.PACKAGES.add(name);
                }
            }
        }
        final ResolverUtil.Test test = new PluginTest(this.clazz);
        for (final String pkg : PluginManager.PACKAGES) {
            resolver.findInPackage(test, pkg);
        }
        for (final Class<?> clazz : resolver.getClasses()) {
            final Plugin plugin = clazz.getAnnotation(Plugin.class);
            final String pluginCategory = plugin.category();
            if (!PluginManager.pluginTypeMap.containsKey(pluginCategory)) {
                PluginManager.pluginTypeMap.putIfAbsent(pluginCategory, new ConcurrentHashMap<String, PluginType<?>>());
            }
            final Map<String, PluginType<?>> map2 = PluginManager.pluginTypeMap.get(pluginCategory);
            String type = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
            PluginType pluginType = new PluginType((Class<T>)clazz, type, plugin.printObject(), plugin.deferChildren());
            map2.put(plugin.name().toLowerCase(), pluginType);
            final PluginAliases pluginAliases = clazz.getAnnotation(PluginAliases.class);
            if (pluginAliases != null) {
                for (final String alias : pluginAliases.value()) {
                    type = (plugin.elementType().equals("") ? alias : plugin.elementType());
                    pluginType = new PluginType((Class<T>)clazz, type, plugin.printObject(), plugin.deferChildren());
                    map2.put(alias.trim().toLowerCase(), pluginType);
                }
            }
        }
        long elapsed = System.nanoTime() - start;
        this.plugins = PluginManager.pluginTypeMap.get(this.type);
        final StringBuilder sb = new StringBuilder("Generated plugins");
        sb.append(" in ");
        DecimalFormat numFormat = new DecimalFormat("#0");
        final long seconds = elapsed / 1000000000L;
        elapsed %= 1000000000L;
        sb.append(numFormat.format(seconds)).append('.');
        numFormat = new DecimalFormat("000000000");
        sb.append(numFormat.format(elapsed)).append(" seconds");
        PluginManager.LOGGER.debug(sb.toString());
    }
    
    private static ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> decode(final ClassLoader classLoader) {
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources("org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
        }
        catch (IOException ioe) {
            PluginManager.LOGGER.warn("Unable to preload plugins", ioe);
            return null;
        }
        final ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> map = new ConcurrentHashMap<String, ConcurrentMap<String, PluginType<?>>>();
        while (resources.hasMoreElements()) {
            DataInputStream dis = null;
            try {
                final URL url = resources.nextElement();
                PluginManager.LOGGER.debug("Found Plugin Map at {}", url.toExternalForm());
                final InputStream is = url.openStream();
                final BufferedInputStream bis = new BufferedInputStream(is);
                dis = new DataInputStream(bis);
                for (int count = dis.readInt(), j = 0; j < count; ++j) {
                    final String type = dis.readUTF();
                    final int entries = dis.readInt();
                    ConcurrentMap<String, PluginType<?>> types = map.get(type);
                    if (types == null) {
                        types = new ConcurrentHashMap<String, PluginType<?>>(count);
                    }
                    for (int i = 0; i < entries; ++i) {
                        final String key = dis.readUTF();
                        final String className = dis.readUTF();
                        final String name = dis.readUTF();
                        final boolean printable = dis.readBoolean();
                        final boolean defer = dis.readBoolean();
                        final Class<?> clazz = Class.forName(className);
                        types.put(key, new PluginType<Object>((Class<Object>)clazz, name, printable, defer));
                    }
                    map.putIfAbsent(type, types);
                }
            }
            catch (Exception ex) {
                PluginManager.LOGGER.warn("Unable to preload plugins", ex);
                return null;
            }
            finally {
                Closer.closeSilent(dis);
            }
        }
        return (map.size() == 0) ? null : map;
    }
    
    private static void encode(final ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> map) {
        final String fileName = PluginManager.rootDir + "org/apache/logging/log4j/core/config/plugins/" + "Log4j2Plugins.dat";
        DataOutputStream dos = null;
        try {
            final File file = new File(PluginManager.rootDir + "org/apache/logging/log4j/core/config/plugins/");
            file.mkdirs();
            final FileOutputStream fos = new FileOutputStream(fileName);
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            dos.writeInt(map.size());
            for (final Map.Entry<String, ConcurrentMap<String, PluginType<?>>> outer : map.entrySet()) {
                dos.writeUTF(outer.getKey());
                dos.writeInt(outer.getValue().size());
                for (final Map.Entry<String, PluginType<?>> entry : outer.getValue().entrySet()) {
                    dos.writeUTF(entry.getKey());
                    final PluginType<?> pt = entry.getValue();
                    dos.writeUTF(pt.getPluginClass().getName());
                    dos.writeUTF(pt.getElementName());
                    dos.writeBoolean(pt.isObjectPrintable());
                    dos.writeBoolean(pt.isDeferChildren());
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            Closer.closeSilent(dos);
        }
    }
    
    static {
        PluginManager.pluginTypeMap = new ConcurrentHashMap<String, ConcurrentMap<String, PluginType<?>>>();
        PACKAGES = new CopyOnWriteArrayList<String>();
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class PluginTest extends ResolverUtil.ClassTest
    {
        private final Class<?> isA;
        
        public PluginTest(final Class<?> isA) {
            this.isA = isA;
        }
        
        @Override
        public boolean matches(final Class<?> type) {
            return type != null && type.isAnnotationPresent(Plugin.class) && (this.isA == null || this.isA.isAssignableFrom(type));
        }
        
        @Override
        public String toString() {
            final StringBuilder msg = new StringBuilder("annotated with @" + Plugin.class.getSimpleName());
            if (this.isA != null) {
                msg.append(" is assignable to " + this.isA.getSimpleName());
            }
            return msg.toString();
        }
    }
}
