// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.Map;
import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
import org.apache.logging.log4j.spi.Provider;
import java.util.TreeMap;
import org.apache.logging.log4j.util.ProviderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class LogManager
{
    private static LoggerContextFactory factory;
    private static final String FACTORY_PROPERTY_NAME = "log4j2.loggerContextFactory";
    private static final Logger LOGGER;
    public static final String ROOT_LOGGER_NAME = "";
    
    private static String getClassName(final int depth) {
        return new Throwable().getStackTrace()[depth].getClassName();
    }
    
    public static LoggerContext getContext() {
        return LogManager.factory.getContext(LogManager.class.getName(), null, true);
    }
    
    public static LoggerContext getContext(final boolean currentContext) {
        return LogManager.factory.getContext(LogManager.class.getName(), null, currentContext);
    }
    
    public static LoggerContext getContext(final ClassLoader loader, final boolean currentContext) {
        return LogManager.factory.getContext(LogManager.class.getName(), loader, currentContext);
    }
    
    public static LoggerContext getContext(final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        return LogManager.factory.getContext(LogManager.class.getName(), loader, currentContext, configLocation);
    }
    
    protected static LoggerContext getContext(final String fqcn, final boolean currentContext) {
        return LogManager.factory.getContext(fqcn, null, currentContext);
    }
    
    protected static LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return LogManager.factory.getContext(fqcn, loader, currentContext);
    }
    
    public static LoggerContextFactory getFactory() {
        return LogManager.factory;
    }
    
    public static Logger getFormatterLogger(final Class<?> clazz) {
        return getLogger((clazz != null) ? clazz.getName() : getClassName(2), StringFormatterMessageFactory.INSTANCE);
    }
    
    public static Logger getFormatterLogger(final Object value) {
        return getLogger((value != null) ? value.getClass().getName() : getClassName(2), StringFormatterMessageFactory.INSTANCE);
    }
    
    public static Logger getFormatterLogger(final String name) {
        return getLogger((name != null) ? name : getClassName(2), StringFormatterMessageFactory.INSTANCE);
    }
    
    public static Logger getLogger() {
        return getLogger(getClassName(2));
    }
    
    public static Logger getLogger(final Class<?> clazz) {
        return getLogger((clazz != null) ? clazz.getName() : getClassName(2));
    }
    
    public static Logger getLogger(final Class<?> clazz, final MessageFactory messageFactory) {
        return getLogger((clazz != null) ? clazz.getName() : getClassName(2), messageFactory);
    }
    
    public static Logger getLogger(final MessageFactory messageFactory) {
        return getLogger(getClassName(2), messageFactory);
    }
    
    public static Logger getLogger(final Object value) {
        return getLogger((value != null) ? value.getClass().getName() : getClassName(2));
    }
    
    public static Logger getLogger(final Object value, final MessageFactory messageFactory) {
        return getLogger((value != null) ? value.getClass().getName() : getClassName(2), messageFactory);
    }
    
    public static Logger getLogger(final String name) {
        final String actualName = (name != null) ? name : getClassName(2);
        return LogManager.factory.getContext(LogManager.class.getName(), null, false).getLogger(actualName);
    }
    
    public static Logger getLogger(final String name, final MessageFactory messageFactory) {
        final String actualName = (name != null) ? name : getClassName(2);
        return LogManager.factory.getContext(LogManager.class.getName(), null, false).getLogger(actualName, messageFactory);
    }
    
    protected static Logger getLogger(final String fqcn, final String name) {
        return LogManager.factory.getContext(fqcn, null, false).getLogger(name);
    }
    
    public static Logger getRootLogger() {
        return getLogger("");
    }
    
    protected LogManager() {
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        final PropertiesUtil managerProps = PropertiesUtil.getProperties();
        final String factoryClass = managerProps.getStringProperty("log4j2.loggerContextFactory");
        final ClassLoader cl = ProviderUtil.findClassLoader();
        if (factoryClass != null) {
            try {
                final Class<?> clazz = cl.loadClass(factoryClass);
                if (LoggerContextFactory.class.isAssignableFrom(clazz)) {
                    LogManager.factory = (LoggerContextFactory)clazz.newInstance();
                }
            }
            catch (ClassNotFoundException cnfe2) {
                LogManager.LOGGER.error("Unable to locate configured LoggerContextFactory {}", factoryClass);
            }
            catch (Exception ex) {
                LogManager.LOGGER.error("Unable to create configured LoggerContextFactory {}", factoryClass, ex);
            }
        }
        if (LogManager.factory == null) {
            final SortedMap<Integer, LoggerContextFactory> factories = new TreeMap<Integer, LoggerContextFactory>();
            if (ProviderUtil.hasProviders()) {
                final Iterator<Provider> providers = ProviderUtil.getProviders();
                while (providers.hasNext()) {
                    final Provider provider = providers.next();
                    final String className = provider.getClassName();
                    if (className != null) {
                        try {
                            final Class<?> clazz2 = cl.loadClass(className);
                            if (LoggerContextFactory.class.isAssignableFrom(clazz2)) {
                                factories.put(provider.getPriority(), (LoggerContextFactory)clazz2.newInstance());
                            }
                            else {
                                LogManager.LOGGER.error(className + " does not implement " + LoggerContextFactory.class.getName());
                            }
                        }
                        catch (ClassNotFoundException cnfe) {
                            LogManager.LOGGER.error("Unable to locate class " + className + " specified in " + provider.getURL().toString(), cnfe);
                        }
                        catch (IllegalAccessException iae) {
                            LogManager.LOGGER.error("Unable to create class " + className + " specified in " + provider.getURL().toString(), iae);
                        }
                        catch (Exception e) {
                            LogManager.LOGGER.error("Unable to create class " + className + " specified in " + provider.getURL().toString(), e);
                            e.printStackTrace();
                        }
                    }
                }
                if (factories.size() == 0) {
                    LogManager.LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
                    LogManager.factory = new SimpleLoggerContextFactory();
                }
                else {
                    final StringBuilder sb = new StringBuilder("Multiple logging implementations found: \n");
                    for (final Map.Entry<Integer, LoggerContextFactory> entry : factories.entrySet()) {
                        sb.append("Factory: ").append(entry.getValue().getClass().getName());
                        sb.append(", Weighting: ").append(entry.getKey()).append("\n");
                    }
                    LogManager.factory = factories.get(factories.lastKey());
                    sb.append("Using factory: ").append(LogManager.factory.getClass().getName());
                    LogManager.LOGGER.warn(sb.toString());
                }
            }
            else {
                LogManager.LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
                LogManager.factory = new SimpleLoggerContextFactory();
            }
        }
    }
}
