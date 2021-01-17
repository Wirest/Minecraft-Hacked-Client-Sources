// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;

public class PropertiesUtil
{
    private static final PropertiesUtil LOG4J_PROPERTIES;
    private static final Logger LOGGER;
    private final Properties props;
    
    public PropertiesUtil(final Properties props) {
        this.props = props;
    }
    
    static Properties loadClose(final InputStream in, final Object source) {
        final Properties props = new Properties();
        if (null != in) {
            try {
                props.load(in);
            }
            catch (IOException e) {
                PropertiesUtil.LOGGER.error("Unable to read " + source, e);
                try {
                    in.close();
                }
                catch (IOException e) {
                    PropertiesUtil.LOGGER.error("Unable to close " + source, e);
                }
            }
            finally {
                try {
                    in.close();
                }
                catch (IOException e2) {
                    PropertiesUtil.LOGGER.error("Unable to close " + source, e2);
                }
            }
        }
        return props;
    }
    
    public PropertiesUtil(final String propsLocn) {
        final ClassLoader loader = ProviderUtil.findClassLoader();
        final InputStream in = loader.getResourceAsStream(propsLocn);
        this.props = loadClose(in, propsLocn);
    }
    
    public static PropertiesUtil getProperties() {
        return PropertiesUtil.LOG4J_PROPERTIES;
    }
    
    public String getStringProperty(final String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException ex) {}
        return (prop == null) ? this.props.getProperty(name) : prop;
    }
    
    public int getIntegerProperty(final String name, final int defaultValue) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException ex2) {}
        if (prop == null) {
            prop = this.props.getProperty(name);
        }
        if (prop != null) {
            try {
                return Integer.parseInt(prop);
            }
            catch (Exception ex) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public long getLongProperty(final String name, final long defaultValue) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException ex2) {}
        if (prop == null) {
            prop = this.props.getProperty(name);
        }
        if (prop != null) {
            try {
                return Long.parseLong(prop);
            }
            catch (Exception ex) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public String getStringProperty(final String name, final String defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }
    
    public boolean getBooleanProperty(final String name) {
        return this.getBooleanProperty(name, false);
    }
    
    public boolean getBooleanProperty(final String name, final boolean defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }
    
    public static Properties getSystemProperties() {
        try {
            return new Properties(System.getProperties());
        }
        catch (SecurityException ex) {
            StatusLogger.getLogger().error("Unable to access system properties.");
            return new Properties();
        }
    }
    
    static {
        LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
        LOGGER = StatusLogger.getLogger();
    }
}
