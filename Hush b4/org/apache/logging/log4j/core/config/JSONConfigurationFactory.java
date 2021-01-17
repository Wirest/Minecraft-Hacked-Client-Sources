// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.io.File;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "JSONConfigurationFactory", category = "ConfigurationFactory")
@Order(6)
public class JSONConfigurationFactory extends ConfigurationFactory
{
    public static final String[] SUFFIXES;
    private static String[] dependencies;
    private final File configFile;
    private boolean isActive;
    
    public JSONConfigurationFactory() {
        this.configFile = null;
        try {
            for (final String item : JSONConfigurationFactory.dependencies) {
                Class.forName(item);
            }
        }
        catch (ClassNotFoundException ex) {
            JSONConfigurationFactory.LOGGER.debug("Missing dependencies for Json support");
            this.isActive = false;
            return;
        }
        this.isActive = true;
    }
    
    @Override
    protected boolean isActive() {
        return this.isActive;
    }
    
    @Override
    public Configuration getConfiguration(final ConfigurationSource source) {
        if (!this.isActive) {
            return null;
        }
        return new JSONConfiguration(source);
    }
    
    public String[] getSupportedTypes() {
        return JSONConfigurationFactory.SUFFIXES;
    }
    
    static {
        SUFFIXES = new String[] { ".json", ".jsn" };
        JSONConfigurationFactory.dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser" };
    }
}
