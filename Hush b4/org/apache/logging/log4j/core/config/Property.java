// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "property", category = "Core", printObject = true)
public final class Property
{
    private static final Logger LOGGER;
    private final String name;
    private final String value;
    
    private Property(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @PluginFactory
    public static Property createProperty(@PluginAttribute("name") final String key, @PluginValue("value") final String value) {
        if (key == null) {
            Property.LOGGER.error("Property key cannot be null");
        }
        return new Property(key, value);
    }
    
    @Override
    public String toString() {
        return this.name + "=" + this.value;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
