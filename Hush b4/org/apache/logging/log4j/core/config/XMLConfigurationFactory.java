// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "XMLConfigurationFactory", category = "ConfigurationFactory")
@Order(5)
public class XMLConfigurationFactory extends ConfigurationFactory
{
    public static final String[] SUFFIXES;
    
    @Override
    public Configuration getConfiguration(final ConfigurationSource source) {
        return new XMLConfiguration(source);
    }
    
    public String[] getSupportedTypes() {
        return XMLConfigurationFactory.SUFFIXES;
    }
    
    static {
        SUFFIXES = new String[] { ".xml", "*" };
    }
}
