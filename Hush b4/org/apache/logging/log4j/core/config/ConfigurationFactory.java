// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.status.StatusLogger;
import java.net.URISyntaxException;
import org.apache.logging.log4j.core.helpers.Loader;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import org.apache.logging.log4j.core.helpers.FileUtils;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Collections;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import java.util.TreeSet;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.ArrayList;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.util.List;
import org.apache.logging.log4j.Logger;

public abstract class ConfigurationFactory
{
    public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
    public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
    protected static final Logger LOGGER;
    protected static final String TEST_PREFIX = "log4j2-test";
    protected static final String DEFAULT_PREFIX = "log4j2";
    private static final String CLASS_LOADER_SCHEME = "classloader";
    private static final int CLASS_LOADER_SCHEME_LENGTH;
    private static final String CLASS_PATH_SCHEME = "classpath";
    private static final int CLASS_PATH_SCHEME_LENGTH;
    private static volatile List<ConfigurationFactory> factories;
    private static ConfigurationFactory configFactory;
    protected final StrSubstitutor substitutor;
    
    public ConfigurationFactory() {
        this.substitutor = new StrSubstitutor(new Interpolator());
    }
    
    public static ConfigurationFactory getInstance() {
        if (ConfigurationFactory.factories == null) {
            synchronized ("log4j2-test") {
                if (ConfigurationFactory.factories == null) {
                    final List<ConfigurationFactory> list = new ArrayList<ConfigurationFactory>();
                    final String factoryClass = PropertiesUtil.getProperties().getStringProperty("log4j.configurationFactory");
                    if (factoryClass != null) {
                        addFactory(list, factoryClass);
                    }
                    final PluginManager manager = new PluginManager("ConfigurationFactory");
                    manager.collectPlugins();
                    final Map<String, PluginType<?>> plugins = manager.getPlugins();
                    final Set<WeightedFactory> ordered = new TreeSet<WeightedFactory>();
                    for (final PluginType<?> type : plugins.values()) {
                        try {
                            final Class<ConfigurationFactory> clazz = (Class<ConfigurationFactory>)type.getPluginClass();
                            final Order order = clazz.getAnnotation(Order.class);
                            if (order == null) {
                                continue;
                            }
                            final int weight = order.value();
                            ordered.add(new WeightedFactory(weight, clazz));
                        }
                        catch (Exception ex) {
                            ConfigurationFactory.LOGGER.warn("Unable to add class " + type.getPluginClass());
                        }
                    }
                    for (final WeightedFactory wf : ordered) {
                        addFactory(list, wf.factoryClass);
                    }
                    ConfigurationFactory.factories = Collections.unmodifiableList((List<? extends ConfigurationFactory>)list);
                }
            }
        }
        return ConfigurationFactory.configFactory;
    }
    
    private static void addFactory(final List<ConfigurationFactory> list, final String factoryClass) {
        try {
            addFactory(list, (Class<ConfigurationFactory>)Class.forName(factoryClass));
        }
        catch (ClassNotFoundException ex) {
            ConfigurationFactory.LOGGER.error("Unable to load class " + factoryClass, ex);
        }
        catch (Exception ex2) {
            ConfigurationFactory.LOGGER.error("Unable to load class " + factoryClass, ex2);
        }
    }
    
    private static void addFactory(final List<ConfigurationFactory> list, final Class<ConfigurationFactory> factoryClass) {
        try {
            list.add(factoryClass.newInstance());
        }
        catch (Exception ex) {
            ConfigurationFactory.LOGGER.error("Unable to create instance of " + factoryClass.getName(), ex);
        }
    }
    
    public static void setConfigurationFactory(final ConfigurationFactory factory) {
        ConfigurationFactory.configFactory = factory;
    }
    
    public static void resetConfigurationFactory() {
        ConfigurationFactory.configFactory = new Factory();
    }
    
    public static void removeConfigurationFactory(final ConfigurationFactory factory) {
        if (ConfigurationFactory.configFactory == factory) {
            ConfigurationFactory.configFactory = new Factory();
        }
    }
    
    protected abstract String[] getSupportedTypes();
    
    protected boolean isActive() {
        return true;
    }
    
    public abstract Configuration getConfiguration(final ConfigurationSource p0);
    
    public Configuration getConfiguration(final String name, final URI configLocation) {
        if (!this.isActive()) {
            return null;
        }
        if (configLocation != null) {
            final ConfigurationSource source = this.getInputFromURI(configLocation);
            if (source != null) {
                return this.getConfiguration(source);
            }
        }
        return null;
    }
    
    protected ConfigurationSource getInputFromURI(final URI configLocation) {
        final File configFile = FileUtils.fileFromURI(configLocation);
        if (configFile != null && configFile.exists() && configFile.canRead()) {
            try {
                return new ConfigurationSource(new FileInputStream(configFile), configFile);
            }
            catch (FileNotFoundException ex) {
                ConfigurationFactory.LOGGER.error("Cannot locate file " + configLocation.getPath(), ex);
            }
        }
        final String scheme = configLocation.getScheme();
        final boolean isClassLoaderScheme = scheme != null && scheme.equals("classloader");
        final boolean isClassPathScheme = scheme != null && !isClassLoaderScheme && scheme.equals("classpath");
        if (scheme == null || isClassLoaderScheme || isClassPathScheme) {
            final ClassLoader loader = this.getClass().getClassLoader();
            String path;
            if (isClassLoaderScheme) {
                path = configLocation.toString().substring(ConfigurationFactory.CLASS_LOADER_SCHEME_LENGTH);
            }
            else if (isClassPathScheme) {
                path = configLocation.toString().substring(ConfigurationFactory.CLASS_PATH_SCHEME_LENGTH);
            }
            else {
                path = configLocation.getPath();
            }
            final ConfigurationSource source = this.getInputFromResource(path, loader);
            if (source != null) {
                return source;
            }
        }
        try {
            return new ConfigurationSource(configLocation.toURL().openStream(), configLocation.getPath());
        }
        catch (MalformedURLException ex2) {
            ConfigurationFactory.LOGGER.error("Invalid URL " + configLocation.toString(), ex2);
        }
        catch (IOException ex3) {
            ConfigurationFactory.LOGGER.error("Unable to access " + configLocation.toString(), ex3);
        }
        catch (Exception ex4) {
            ConfigurationFactory.LOGGER.error("Unable to access " + configLocation.toString(), ex4);
        }
        return null;
    }
    
    protected ConfigurationSource getInputFromString(final String config, final ClassLoader loader) {
        try {
            final URL url = new URL(config);
            return new ConfigurationSource(url.openStream(), FileUtils.fileFromURI(url.toURI()));
        }
        catch (Exception ex) {
            final ConfigurationSource source = this.getInputFromResource(config, loader);
            if (source == null) {
                try {
                    final File file = new File(config);
                    return new ConfigurationSource(new FileInputStream(file), file);
                }
                catch (FileNotFoundException ex2) {}
            }
            return source;
        }
    }
    
    protected ConfigurationSource getInputFromResource(final String resource, final ClassLoader loader) {
        final URL url = Loader.getResource(resource, loader);
        if (url == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = url.openStream();
        }
        catch (IOException ioe) {
            return null;
        }
        if (is == null) {
            return null;
        }
        if (FileUtils.isFile(url)) {
            try {
                return new ConfigurationSource(is, FileUtils.fileFromURI(url.toURI()));
            }
            catch (URISyntaxException ex) {}
        }
        return new ConfigurationSource(is, resource);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        CLASS_LOADER_SCHEME_LENGTH = "classloader".length() + 1;
        CLASS_PATH_SCHEME_LENGTH = "classpath".length() + 1;
        ConfigurationFactory.factories = null;
        ConfigurationFactory.configFactory = new Factory();
    }
    
    private static class WeightedFactory implements Comparable<WeightedFactory>
    {
        private final int weight;
        private final Class<ConfigurationFactory> factoryClass;
        
        public WeightedFactory(final int weight, final Class<ConfigurationFactory> clazz) {
            this.weight = weight;
            this.factoryClass = clazz;
        }
        
        @Override
        public int compareTo(final WeightedFactory wf) {
            final int w = wf.weight;
            if (this.weight == w) {
                return 0;
            }
            if (this.weight > w) {
                return -1;
            }
            return 1;
        }
    }
    
    private static class Factory extends ConfigurationFactory
    {
        @Override
        public Configuration getConfiguration(final String name, final URI configLocation) {
            if (configLocation == null) {
                final String config = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty("log4j.configurationFile"));
                if (config != null) {
                    ConfigurationSource source = null;
                    try {
                        source = this.getInputFromURI(new URI(config));
                    }
                    catch (Exception ex) {}
                    if (source == null) {
                        final ClassLoader loader = this.getClass().getClassLoader();
                        source = this.getInputFromString(config, loader);
                    }
                    if (source != null) {
                        for (final ConfigurationFactory factory : ConfigurationFactory.factories) {
                            final String[] types = factory.getSupportedTypes();
                            if (types != null) {
                                for (final String type : types) {
                                    if (type.equals("*") || config.endsWith(type)) {
                                        final Configuration c = factory.getConfiguration(source);
                                        if (c != null) {
                                            return c;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                for (final ConfigurationFactory factory2 : ConfigurationFactory.factories) {
                    final String[] types2 = factory2.getSupportedTypes();
                    if (types2 != null) {
                        for (final String type2 : types2) {
                            if (type2.equals("*") || configLocation.toString().endsWith(type2)) {
                                final Configuration config2 = factory2.getConfiguration(name, configLocation);
                                if (config2 != null) {
                                    return config2;
                                }
                            }
                        }
                    }
                }
            }
            Configuration config3 = this.getConfiguration(true, name);
            if (config3 == null) {
                config3 = this.getConfiguration(true, null);
                if (config3 == null) {
                    config3 = this.getConfiguration(false, name);
                    if (config3 == null) {
                        config3 = this.getConfiguration(false, null);
                    }
                }
            }
            return (config3 != null) ? config3 : new DefaultConfiguration();
        }
        
        private Configuration getConfiguration(final boolean isTest, final String name) {
            final boolean named = name != null && name.length() > 0;
            final ClassLoader loader = this.getClass().getClassLoader();
            for (final ConfigurationFactory factory : ConfigurationFactory.factories) {
                final String prefix = isTest ? "log4j2-test" : "log4j2";
                final String[] types = factory.getSupportedTypes();
                if (types == null) {
                    continue;
                }
                for (final String suffix : types) {
                    if (!suffix.equals("*")) {
                        final String configName = named ? (prefix + name + suffix) : (prefix + suffix);
                        final ConfigurationSource source = this.getInputFromResource(configName, loader);
                        if (source != null) {
                            return factory.getConfiguration(source);
                        }
                    }
                }
            }
            return null;
        }
        
        public String[] getSupportedTypes() {
            return null;
        }
        
        @Override
        public Configuration getConfiguration(final ConfigurationSource source) {
            if (source != null) {
                final String config = source.getLocation();
                for (final ConfigurationFactory factory : ConfigurationFactory.factories) {
                    final String[] types = factory.getSupportedTypes();
                    if (types != null) {
                        final String[] arr$ = types;
                        final int len$ = arr$.length;
                        int i$2 = 0;
                        while (i$2 < len$) {
                            final String type = arr$[i$2];
                            if (type.equals("*") || (config != null && config.endsWith(type))) {
                                final Configuration c = factory.getConfiguration(source);
                                if (c != null) {
                                    return c;
                                }
                                Factory.LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", config);
                                return null;
                            }
                            else {
                                ++i$2;
                            }
                        }
                    }
                }
            }
            Factory.LOGGER.error("Cannot process configuration, input source is null");
            return null;
        }
    }
    
    public static class ConfigurationSource
    {
        private File file;
        private String location;
        private InputStream stream;
        
        public ConfigurationSource() {
        }
        
        public ConfigurationSource(final InputStream stream) {
            this.stream = stream;
            this.file = null;
            this.location = null;
        }
        
        public ConfigurationSource(final InputStream stream, final File file) {
            this.stream = stream;
            this.file = file;
            this.location = file.getAbsolutePath();
        }
        
        public ConfigurationSource(final InputStream stream, final String location) {
            this.stream = stream;
            this.location = location;
            this.file = null;
        }
        
        public File getFile() {
            return this.file;
        }
        
        public void setFile(final File file) {
            this.file = file;
        }
        
        public String getLocation() {
            return this.location;
        }
        
        public void setLocation(final String location) {
            this.location = location;
        }
        
        public InputStream getInputStream() {
            return this.stream;
        }
        
        public void setInputStream(final InputStream stream) {
            this.stream = stream;
        }
    }
}
